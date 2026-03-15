"""
Crawler truyện từ truyenfull.vn
Cài đặt: pip install requests beautifulsoup4 mysql-connector-python python-slugify
"""

import time
import re
import os
import requests
import mysql.connector
from bs4 import BeautifulSoup
from slugify import slugify
from requests.adapters import HTTPAdapter
from urllib3.util.retry import Retry

# ─────────────────────────────────────────────
# CẤU HÌNH
# ─────────────────────────────────────────────
DB_CONFIG = {
    "host":     "localhost",
    "user":     "root",
    "password": "123456",
    "database": "story_db",
    "charset":  "utf8mb4",
    "auth_plugin": "mysql_native_password",
}

BASE_URL_CANDIDATES = [
    "https://truyenfull.vn",
    "https://truyenfull.vision",
    "https://truyenfull.ink",
]

# Có thể override nhanh bằng env:
# set STORY_SOURCE_URLS=https://domain1,https://domain2
env_urls = os.getenv("STORY_SOURCE_URLS", "").strip()
if env_urls:
    BASE_URL_CANDIDATES = [u.strip().rstrip("/") for u in env_urls.split(",") if u.strip()]

BASE_URL = BASE_URL_CANDIDATES[0]
HEADERS    = {
    "User-Agent": (
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
        "AppleWebKit/537.36 (KHTML, like Gecko) "
        "Chrome/120.0.0.0 Safari/537.36"
    )
}
DELAY = 1.5   # giây nghỉ giữa các request, tránh bị ban
REQUEST_TIMEOUT = 20


def build_http_session() -> requests.Session:
    retry = Retry(
        total=3,
        connect=3,
        read=3,
        backoff_factor=1,
        status_forcelist=[429, 500, 502, 503, 504],
        allowed_methods=["GET", "HEAD"],
        raise_on_status=False,
    )
    adapter = HTTPAdapter(max_retries=retry)
    session = requests.Session()
    session.mount("https://", adapter)
    session.mount("http://", adapter)
    return session


SESSION = build_http_session()

# ─────────────────────────────────────────────
# KẾT NỐI DATABASE
# ─────────────────────────────────────────────
def get_db():
    return mysql.connector.connect(**DB_CONFIG)


def resolve_base_url() -> str | None:
    """Tự tìm domain còn sống từ danh sách candidate."""
    global BASE_URL
    for candidate in BASE_URL_CANDIDATES:
        test_url = f"{candidate.rstrip('/')}/danh-sach/truyen-moi/?page=1"
        try:
            resp = SESSION.get(test_url, headers=HEADERS, timeout=REQUEST_TIMEOUT)
            if resp.status_code == 200 and "truyen-title" in resp.text:
                BASE_URL = candidate.rstrip("/")
                print(f"[INFO] Dùng nguồn: {BASE_URL}")
                return BASE_URL
        except requests.RequestException:
            pass

    print("[ERROR] Không kết nối được nguồn truyện nào trong BASE_URL_CANDIDATES")
    print("[GỢI Ý] Đổi DNS/VPN hoặc set env STORY_SOURCE_URLS để thử domain khác")
    return None


# ─────────────────────────────────────────────
# HELPER: lấy HTML an toàn
# ─────────────────────────────────────────────
def fetch(url: str) -> BeautifulSoup | None:
    try:
        resp = SESSION.get(url, headers=HEADERS, timeout=REQUEST_TIMEOUT)
        resp.raise_for_status()
        resp.encoding = "utf-8"
        return BeautifulSoup(resp.text, "html.parser")
    except requests.RequestException as e:
        print(f"[WARN] Lỗi fetch {url}: {e}")
        return None


# ─────────────────────────────────────────────
# LẤY DANH SÁCH TRUYỆN TỪ TRANG DANH SÁCH
# Trả về list dict: {title, url}
# ─────────────────────────────────────────────
def get_story_list(page: int = 1) -> list[dict]:
    # Một số mirror có thể khác nhẹ path, thử lần lượt.
    url_variants = [
        f"{BASE_URL}/danh-sach/truyen-moi/?page={page}",
        f"{BASE_URL}/danh-sach/truyen-moi?page={page}",
    ]

    soup = None
    for url in url_variants:
        soup = fetch(url)
        if soup and soup.select_one("div.list-truyen"):
            break

    if not soup:
        return []

    stories = []
    for item in soup.select("div.list-truyen .row"):
        a_tag = item.select_one("h3.truyen-title a")
        if a_tag:
            stories.append({
                "title": a_tag.get_text(strip=True),
                "url":   a_tag["href"],
            })
    return stories


# ─────────────────────────────────────────────
# LẤY THÔNG TIN CHI TIẾT TRUYỆN
# ─────────────────────────────────────────────
def get_story_detail(url: str, fallback_title: str = "") -> dict | None:
    soup = fetch(url)
    if not soup:
        return None

    info   = soup.select_one("div.book")

    # Một số mirror thay đổi cấu trúc HTML, nên thử nhiều selector.
    title = (
        soup.select_one("h3.title")
        or soup.select_one("div.info h3")
        or soup.select_one("meta[property='og:title']")
    )
    author = (
        soup.select_one("a[itemprop='author']")
        or soup.select_one("div.info a[href*='/tac-gia/']")
    )
    desc   = soup.select_one("div.desc-text")
    cover  = (
        (info.select_one("div.book-img img") if info else None)
        or soup.select_one("meta[property='og:image']")
    )
    status_tag = soup.select_one("span.text-success, span.text-primary")
    genres = [g.get_text(strip=True) for g in soup.select("div.info-holder a[itemprop='genre']")]

    raw_title = ""
    if title:
        if title.name == "meta":
            raw_title = (title.get("content") or "").strip()
        else:
            raw_title = title.get_text(strip=True)
    if raw_title:
        raw_title = raw_title.split(" - Truyện Full")[0].split(" - Truyenfull")[0].strip()
    final_title = raw_title or fallback_title or "Unknown"

    cover_url = ""
    if cover:
        if cover.name == "meta":
            cover_url = cover.get("content") or ""
        else:
            cover_url = cover.get("src") or ""

    # Tổng số chương (lấy từ phân trang)
    last_page_tag = soup.select("ul.pagination li a")
    total_chapters = 0
    if last_page_tag:
        nums = [
            int(a["data-page"])
            for a in last_page_tag
            if a.get("data-page", "").isdigit()
        ]
        total_chapters = max(nums) * 50 if nums else 0

    status_map = {"Đang ra": "ongoing", "Hoàn thành": "completed", "Tạm dừng": "hiatus"}
    raw_status = status_tag.get_text(strip=True) if status_tag else ""
    status     = status_map.get(raw_status, "ongoing")

    return {
        "title":          final_title,
        "author":         author.get_text(strip=True) if author else "Unknown",
        "description":    desc.get_text(strip=True)   if desc   else "",
        "cover_url":      cover_url,
        "status":         status,
        "total_chapters": total_chapters,
        "genres":         genres,
        "source_url":     url,
    }


# ─────────────────────────────────────────────
# LẤY DANH SÁCH CHƯƠNG (URL) CỦA TRUYỆN
# ─────────────────────────────────────────────
def get_chapter_url_list(story_url: str, limit: int | None = None) -> list[dict]:
    """Trả về list {number, title, url}; có thể dừng sớm theo limit."""
    chapters = []
    page = 1

    while True:
        url  = f"{story_url}trang-{page}/#list-chapter"
        soup = fetch(url)
        if not soup:
            break

        items = soup.select("ul.list-chapter li a")
        if not items:
            break

        for a in items:
            chap_text = a.get_text(strip=True)
            match = re.search(r"[Cc]hương\s*(\d+)", chap_text)
            number = int(match.group(1)) if match else len(chapters) + 1
            chapters.append({
                "number": number,
                "title":  chap_text,
                "url":    a["href"],
            })
            if limit and len(chapters) >= limit:
                return chapters

        # Kiểm tra còn trang tiếp không
        next_btn = soup.select_one("ul.pagination li.active + li a")
        if not next_btn:
            break
        page += 1
        time.sleep(DELAY)

    return chapters


# ─────────────────────────────────────────────
# LẤY NỘI DUNG CHƯƠNG
# ─────────────────────────────────────────────
def get_chapter_content(url: str) -> str:
    soup = fetch(url)
    if not soup:
        return ""

    content_div = soup.select_one("div#chapter-c")
    if not content_div:
        return ""

    # Xóa các tag quảng cáo thường gặp
    for tag in content_div.select("div.ads-responsive, script, ins"):
        tag.decompose()

    return content_div.get_text(separator="\n", strip=True)


# ─────────────────────────────────────────────
# LƯU VÀO DATABASE
# ─────────────────────────────────────────────
def upsert_genre(cursor, name: str) -> int:
    sl = slugify(name, allow_unicode=False)
    cursor.execute(
        "INSERT INTO genres (name, slug) VALUES (%s, %s) ON DUPLICATE KEY UPDATE id=LAST_INSERT_ID(id)",
        (name, sl)
    )
    return cursor.lastrowid


def insert_story(cursor, data: dict) -> int:
    def make_unique_slug(base_title: str, exclude_story_id: int | None = None) -> str:
        slug_base = slugify(base_title, allow_unicode=False) or "story"
        slug = slug_base
        suffix = 1
        while True:
            cursor.execute("SELECT id FROM stories WHERE slug = %s", (slug,))
            row = cursor.fetchone()
            if not row:
                return slug
            if exclude_story_id is not None and row[0] == exclude_story_id:
                return slug
            slug = f"{slug_base}-{suffix}"
            suffix += 1

    # Ưu tiên định danh bằng source_url để tránh tạo bản ghi trùng.
    cursor.execute("SELECT id FROM stories WHERE source_url = %s LIMIT 1", (data["source_url"],))
    existing = cursor.fetchone()

    if existing:
        story_id = existing[0]
        sl = make_unique_slug(data["title"], exclude_story_id=story_id)
        cursor.execute(
            """
            UPDATE stories
            SET title = %s,
                slug = %s,
                author = %s,
                description = %s,
                cover_url = %s,
                status = %s,
                total_chapters = %s,
                updated_at = CURRENT_TIMESTAMP
            WHERE id = %s
            """,
            (
                data["title"], sl, data["author"], data["description"],
                data["cover_url"], data["status"], data["total_chapters"], story_id
            )
        )
        return story_id

    sl = make_unique_slug(data["title"])
    cursor.execute(
        """
        INSERT INTO stories
            (title, slug, author, description, cover_url, status, total_chapters, source_url)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
        """,
        (
            data["title"], sl, data["author"], data["description"],
            data["cover_url"], data["status"], data["total_chapters"], data["source_url"]
        )
    )
    return cursor.lastrowid


def insert_chapter(cursor, story_id: int, chap: dict, content: str):
    word_count = len(content.split())
    cursor.execute(
        """
        INSERT INTO chapters (story_id, chapter_number, title, content, word_count)
        VALUES (%s, %s, %s, %s, %s)
        ON DUPLICATE KEY UPDATE
            title      = VALUES(title),
            content    = VALUES(content),
            word_count = VALUES(word_count)
        """,
        (story_id, chap["number"], chap["title"], content, word_count)
    )


# ─────────────────────────────────────────────
# HÀM CHÍNH
# ─────────────────────────────────────────────
def crawl(max_pages: int = 5, max_chapters_per_story: int = 30):
    """
    max_pages              : số trang danh sách truyện cần crawl
    max_chapters_per_story : giới hạn số chương lấy mỗi truyện (None = tất cả)
    """
    if not resolve_base_url():
        return

    db     = get_db()
    cursor = db.cursor()

    for page in range(1, max_pages + 1):
        print(f"\n=== Trang danh sách {page}/{max_pages} ===")
        story_list = get_story_list(page)
        if not story_list:
            break

        for story_meta in story_list:
            print(f"\n[Truyện] {story_meta['title']}")
            detail = get_story_detail(
                story_meta["url"],
                fallback_title=story_meta["title"],
            )
            if not detail:
                continue

            # Lưu truyện
            story_id = insert_story(cursor, detail)
            db.commit()

            # Lưu thể loại
            for genre_name in detail["genres"]:
                genre_id = upsert_genre(cursor, genre_name)
                cursor.execute(
                    "INSERT IGNORE INTO story_genres (story_id, genre_id) VALUES (%s, %s)",
                    (story_id, genre_id)
                )
            db.commit()

            # Crawl chương
            chapter_urls = get_chapter_url_list(
                story_meta["url"],
                limit=max_chapters_per_story,
            )

            for i, chap in enumerate(chapter_urls, 1):
                print(f"  Chương {chap['number']} ({i}/{len(chapter_urls)})", end="\r")
                content = get_chapter_content(chap["url"])
                insert_chapter(cursor, story_id, chap, content)
                db.commit()
                time.sleep(DELAY)

            # Cập nhật tổng số chương thực tế
            cursor.execute(
                "UPDATE stories SET total_chapters = %s WHERE id = %s",
                (len(chapter_urls), story_id)
            )
            db.commit()
            time.sleep(DELAY)

    cursor.close()
    db.close()
    print("\n✓ Crawl hoàn thành!")


if __name__ == "__main__":
    crawl(max_pages=3, max_chapters_per_story=10)
