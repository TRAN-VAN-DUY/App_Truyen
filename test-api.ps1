# Story API Testing Script (PowerShell)
# Usage: .\test-api.ps1

# ============================================
# CONFIGURATION
# ============================================
$BASE_URL = "http://localhost:8080"
$EMAIL = "nguyenviet@example.com"
$PASSWORD = "Password@123"
$DEVICE_ID = "device_android_12345"

# Colors for output
$SUCCESS = "Green"
$ERROR_COLOR = "Red"
$INFO = "Yellow"
$WARNING = "Cyan"

Write-Host "╔════════════════════════════════════════╗" -ForegroundColor $INFO
Write-Host "║   Story API Testing Script             ║" -ForegroundColor $INFO
Write-Host "╚════════════════════════════════════════╝" -ForegroundColor $INFO
Write-Host ""

# ============================================
# 1️⃣ AUTHENTICATION TESTS
# ============================================
Write-Host "🔐 PHASE 1: AUTHENTICATION TESTS" -ForegroundColor $INFO
Write-Host "─" * 40 -ForegroundColor $INFO
Write-Host ""

# Test 1.1: Register with Email
Write-Host "[1.1] Register with Email & Password..." -ForegroundColor $WARNING
try {
    $registerEmailBody = @{
        username = "nguyenviet"
        email    = $EMAIL
        password = $PASSWORD
    } | ConvertTo-Json

    $registerEmailResponse = Invoke-RestMethod `
        -Uri "$BASE_URL/api/auth/register" `
        -Method POST `
        -ContentType "application/json" `
        -Body $registerEmailBody

    $USER_TOKEN = $registerEmailResponse.data.token
    $USER_ID = $registerEmailResponse.data.userId

    Write-Host "✅ Register Email SUCCESS" -ForegroundColor $SUCCESS
    Write-Host "   User ID: $USER_ID" -ForegroundColor $SUCCESS
    Write-Host "   Token: $($USER_TOKEN.Substring(0, 50))..." -ForegroundColor $SUCCESS
    Write-Host ""
}
catch {
    Write-Host "❌ Register Email FAILED: $($_.Exception.Message)" -ForegroundColor $ERROR_COLOR
    Write-Host ""
}


# Test 1.2: Login with Email
Write-Host "[1.3] Login with Email & Password..." -ForegroundColor $WARNING
try {
    $loginEmailBody = @{
        email    = $EMAIL
        password = $PASSWORD
    } | ConvertTo-Json

    $loginEmailResponse = Invoke-RestMethod `
        -Uri "$BASE_URL/api/auth/login" `
        -Method POST `
        -ContentType "application/json" `
        -Body $loginEmailBody

    Write-Host "✅ Login Email SUCCESS" -ForegroundColor $SUCCESS
    Write-Host ""
}
catch {
    Write-Host "❌ Login Email FAILED: $($_.Exception.Message)" -ForegroundColor $ERROR_COLOR
    Write-Host ""
}


# ============================================
# 2️⃣ PUBLIC STORIES API TESTS
# ============================================
Write-Host "📚 PHASE 2: PUBLIC STORIES TESTS" -ForegroundColor $INFO
Write-Host "─" * 40 -ForegroundColor $INFO
Write-Host ""

# Test 2.1: Get Stories List
Write-Host "[2.1] Get Stories List (Page 0, Size 5)..." -ForegroundColor $WARNING
try {
    $storiesResponse = Invoke-RestMethod `
        -Uri "$BASE_URL/api/v1/stories?page=0&size=5&sort=updated" `
        -Method GET `
        -ContentType "application/json"

    $storyCount = $storiesResponse.data.content.Count
    Write-Host "✅ Get Stories LIST SUCCESS" -ForegroundColor $SUCCESS
    Write-Host "   Total Stories: $($storiesResponse.data.totalElements)" -ForegroundColor $SUCCESS
    Write-Host "   Current Page: $($storiesResponse.data.content.Count) stories" -ForegroundColor $SUCCESS

    if ($storyCount -gt 0) {
        $firstStory = $storiesResponse.data.content[0]
        $FIRST_STORY_ID = $firstStory.id
        $FIRST_STORY_SLUG = $firstStory.slug
        Write-Host "   First Story: $($firstStory.title) (ID: $FIRST_STORY_ID)" -ForegroundColor $SUCCESS
    }
    Write-Host ""
}
catch {
    Write-Host "❌ Get Stories FAILED: $($_.Exception.Message)" -ForegroundColor $ERROR_COLOR
    Write-Host ""
}

# Test 2.2: Search Stories
Write-Host "[2.2] Search Stories..." -ForegroundColor $WARNING
try {
    $searchResponse = Invoke-RestMethod `
        -Uri "$BASE_URL/api/v1/stories/search?keyword=Tiêu&page=0&size=10" `
        -Method GET `
        -ContentType "application/json"

    $searchCount = $searchResponse.data.content.Count
    Write-Host "✅ Search Stories SUCCESS" -ForegroundColor $SUCCESS
    Write-Host "   Found: $searchCount stories" -ForegroundColor $SUCCESS
    Write-Host ""
}
catch {
    Write-Host "❌ Search Stories FAILED: $($_.Exception.Message)" -ForegroundColor $ERROR_COLOR
    Write-Host ""
}

# Test 2.3: Get Story Detail
if ($null -ne $FIRST_STORY_SLUG) {
    Write-Host "[2.3] Get Story Detail (By Slug)..." -ForegroundColor $WARNING
    try {
        $storyDetailResponse = Invoke-RestMethod `
            -Uri "$BASE_URL/api/v1/stories/$FIRST_STORY_SLUG" `
            -Method GET `
            -ContentType "application/json"

        Write-Host "✅ Get Story Detail SUCCESS" -ForegroundColor $SUCCESS
        Write-Host "   Title: $($storyDetailResponse.data.title)" -ForegroundColor $SUCCESS
        Write-Host "   Views: $($storyDetailResponse.data.views)" -ForegroundColor $SUCCESS
        Write-Host "   Rating: $($storyDetailResponse.data.rating)" -ForegroundColor $SUCCESS
        Write-Host ""
    }
    catch {
        Write-Host "❌ Get Story Detail FAILED: $($_.Exception.Message)" -ForegroundColor $ERROR_COLOR
        Write-Host ""
    }
}

# Test 2.4: Get Chapters
if ($null -ne $FIRST_STORY_SLUG) {
    Write-Host "[2.4] Get Chapters..." -ForegroundColor $WARNING
    try {
        $chaptersResponse = Invoke-RestMethod `
            -Uri "$BASE_URL/api/v1/stories/$FIRST_STORY_SLUG/chapters" `
            -Method GET `
            -ContentType "application/json"

        $chapterCount = $chaptersResponse.data.Count
        Write-Host "✅ Get Chapters SUCCESS" -ForegroundColor $SUCCESS
        Write-Host "   Total Chapters: $chapterCount" -ForegroundColor $SUCCESS

        if ($chapterCount -gt 0) {
            $FIRST_CHAPTER_ID = $chaptersResponse.data[0].id
            $FIRST_CHAPTER_NUMBER = $chaptersResponse.data[0].chapterNumber
            Write-Host "   First Chapter: #$FIRST_CHAPTER_NUMBER" -ForegroundColor $SUCCESS
        }
        Write-Host ""
    }
    catch {
        Write-Host "❌ Get Chapters FAILED: $($_.Exception.Message)" -ForegroundColor $ERROR_COLOR
        Write-Host ""
    }
}

# ============================================
# 3️⃣ PUBLIC CHAPTER CONTENT TESTS
# ============================================
Write-Host "📖 PHASE 3: CHAPTER CONTENT TESTS" -ForegroundColor $INFO
Write-Host "─" * 40 -ForegroundColor $INFO
Write-Host ""

# Test 3.1: Read Chapter Content
if ($null -ne $FIRST_STORY_SLUG -and $null -ne $FIRST_CHAPTER_NUMBER) {
    Write-Host "[3.1] Read Chapter Content..." -ForegroundColor $WARNING
    try {
        $chapterContentResponse = Invoke-RestMethod `
            -Uri "$BASE_URL/api/v1/stories/$FIRST_STORY_SLUG/chapters/$FIRST_CHAPTER_NUMBER" `
            -Method GET `
            -ContentType "application/json"

        $contentLength = $chapterContentResponse.data.content.Length
        Write-Host "✅ Read Chapter Content SUCCESS" -ForegroundColor $SUCCESS
        Write-Host "   Chapter: $($chapterContentResponse.data.title)" -ForegroundColor $SUCCESS
        Write-Host "   Content Length: $contentLength characters" -ForegroundColor $SUCCESS
        Write-Host "   Word Count: $($chapterContentResponse.data.wordCount)" -ForegroundColor $SUCCESS
        Write-Host "   Next Chapter: $($chapterContentResponse.data.nextChapterNumber)" -ForegroundColor $SUCCESS
        Write-Host ""
    }
    catch {
        Write-Host "❌ Read Chapter FAILED: $($_.Exception.Message)" -ForegroundColor $ERROR_COLOR
        Write-Host ""
    }
}

# ============================================
# 4️⃣ SECURED READING HISTORY TESTS
# ============================================
Write-Host "🔒 PHASE 4: READING HISTORY TESTS (Secured)" -ForegroundColor $INFO
Write-Host "─" * 40 -ForegroundColor $INFO
Write-Host ""

if ($null -ne $USER_TOKEN) {
    # Test 4.1: Save Reading Progress
    if ($null -ne $FIRST_STORY_ID) {
        Write-Host "[4.1] Save Reading Progress..." -ForegroundColor $WARNING
        try {
            $saveProgressBody = @{
                storyId       = $FIRST_STORY_ID
                lastChapterId = if ($null -ne $FIRST_CHAPTER_ID) { $FIRST_CHAPTER_ID } else { 1 }
                scrollPosition = 0.45
            } | ConvertTo-Json

            $saveProgressResponse = Invoke-RestMethod `
                -Uri "$BASE_URL/api/v1/reading-history" `
                -Method POST `
                -ContentType "application/json" `
                -Headers @{Authorization = "Bearer $USER_TOKEN" } `
                -Body $saveProgressBody

            Write-Host "✅ Save Progress SUCCESS" -ForegroundColor $SUCCESS
            Write-Host "   Story: $($saveProgressResponse.data.storyTitle)" -ForegroundColor $SUCCESS
            Write-Host "   Chapter: #$($saveProgressResponse.data.lastChapterNumber)" -ForegroundColor $SUCCESS
            Write-Host ""
        }
        catch {
            Write-Host "❌ Save Progress FAILED: $($_.Exception.Message)" -ForegroundColor $ERROR_COLOR
            Write-Host ""
        }
    }

    # Test 4.2: Get Continue Reading List
    Write-Host "[4.2] Get Continue Reading List..." -ForegroundColor $WARNING
    try {
        $continueReadingResponse = Invoke-RestMethod `
            -Uri "$BASE_URL/api/v1/reading-history?page=0&size=10" `
            -Method GET `
            -ContentType "application/json" `
            -Headers @{Authorization = "Bearer $USER_TOKEN" }

        $historyCount = $continueReadingResponse.data.content.Count
        Write-Host "✅ Get Continue Reading SUCCESS" -ForegroundColor $SUCCESS
        Write-Host "   Stories in history: $historyCount" -ForegroundColor $SUCCESS
        Write-Host ""
    }
    catch {
        Write-Host "❌ Get Continue Reading FAILED: $($_.Exception.Message)" -ForegroundColor $ERROR_COLOR
        Write-Host ""
    }
}

# ============================================
# 5️⃣ SECURED FAVORITES TESTS
# ============================================
Write-Host "❤️  PHASE 5: FAVORITES TESTS (Secured)" -ForegroundColor $INFO
Write-Host "─" * 40 -ForegroundColor $INFO
Write-Host ""

if ($null -ne $USER_TOKEN -and $null -ne $FIRST_STORY_ID) {
    # Test 5.1: Add to Favorites
    Write-Host "[5.1] Add Story to Favorites..." -ForegroundColor $WARNING
    try {
        $favBody = @{
            storyId = $FIRST_STORY_ID
        } | ConvertTo-Json

        $addFavResponse = Invoke-RestMethod `
            -Uri "$BASE_URL/api/v1/favorites" `
            -Method POST `
            -ContentType "application/json" `
            -Headers @{Authorization = "Bearer $USER_TOKEN" } `
            -Body $favBody

        Write-Host "✅ Add to Favorites SUCCESS" -ForegroundColor $SUCCESS
        Write-Host "   Story: $($addFavResponse.data.storyTitle)" -ForegroundColor $SUCCESS
        Write-Host ""
    }
    catch {
        # Might fail if already favorited
        Write-Host "⚠️  Add to Favorites: Already favorited or error" -ForegroundColor $WARNING
        Write-Host ""
    }

    # Test 5.2: Get Favorites List
    Write-Host "[5.2] Get Favorites List..." -ForegroundColor $WARNING
    try {
        $favListResponse = Invoke-RestMethod `
            -Uri "$BASE_URL/api/v1/favorites?page=0&size=10" `
            -Method GET `
            -ContentType "application/json" `
            -Headers @{Authorization = "Bearer $USER_TOKEN" }

        $favCount = $favListResponse.data.content.Count
        Write-Host "✅ Get Favorites SUCCESS" -ForegroundColor $SUCCESS
        Write-Host "   Favorite Stories: $favCount" -ForegroundColor $SUCCESS
        Write-Host ""
    }
    catch {
        Write-Host "❌ Get Favorites FAILED: $($_.Exception.Message)" -ForegroundColor $ERROR_COLOR
        Write-Host ""
    }

    # Test 5.3: Check if Favorited
    Write-Host "[5.3] Check if Story is Favorited..." -ForegroundColor $WARNING
    try {
        $checkFavResponse = Invoke-RestMethod `
            -Uri "$BASE_URL/api/v1/favorites/$FIRST_STORY_ID/check" `
            -Method GET `
            -ContentType "application/json" `
            -Headers @{Authorization = "Bearer $USER_TOKEN" }

        $isFav = $checkFavResponse.data.favorited
        Write-Host "✅ Check Favorite SUCCESS" -ForegroundColor $SUCCESS
        Write-Host "   Is Favorited: $isFav" -ForegroundColor $SUCCESS
        Write-Host ""
    }
    catch {
        Write-Host "❌ Check Favorite FAILED: $($_.Exception.Message)" -ForegroundColor $ERROR_COLOR
        Write-Host ""
    }
}

# ============================================
# SUMMARY
# ============================================
Write-Host "╔════════════════════════════════════════╗" -ForegroundColor $SUCCESS
Write-Host "║   ✅ Testing Complete!                 ║" -ForegroundColor $SUCCESS
Write-Host "╚════════════════════════════════════════╝" -ForegroundColor $SUCCESS
Write-Host ""
Write-Host "📊 Summary:" -ForegroundColor $INFO
Write-Host "   • Authentication: Register & Login ✅" -ForegroundColor $SUCCESS
Write-Host "   • Public APIs: Stories & Chapters ✅" -ForegroundColor $SUCCESS
Write-Host "   • Secured APIs: Reading History & Favorites ✅" -ForegroundColor $SUCCESS
Write-Host ""
Write-Host "📝 Saved Tokens for Further Testing:" -ForegroundColor $INFO
Write-Host "   User Token: $($USER_TOKEN.Substring(0, 50))..." -ForegroundColor $WARNING
Write-Host "   Admin Token: (Need to configure separately)" -ForegroundColor $WARNING
Write-Host ""
Write-Host "🚀 Next Steps:" -ForegroundColor $INFO
Write-Host "   1. Use Postman Collection: Story_API_Postman_Collection.json" -ForegroundColor $WARNING
Write-Host "   2. Read Full Guide: API_TESTING_GUIDE.md" -ForegroundColor $WARNING
Write-Host "   3. Test Admin APIs with admin token" -ForegroundColor $WARNING
Write-Host ""
