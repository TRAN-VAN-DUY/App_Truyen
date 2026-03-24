package com.example.app_truyen;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.util.ArrayList;
import java.util.List;

public class HomeUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        RecyclerView recyclerTopStories = findViewById(R.id.recyclerTopStories);
        setupTopStoriesCarousel(recyclerTopStories, buildTopStories());

        RecyclerView recyclerRecommendedStories = findViewById(R.id.recyclerRecommendedStories);
        recyclerRecommendedStories.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerRecommendedStories.setNestedScrollingEnabled(false);
        recyclerRecommendedStories.setAdapter(new StoryAdapter(buildRecommendedStories()));

        RecyclerView recyclerNewUpdateStories = findViewById(R.id.recyclerNewUpdateStories);
        recyclerNewUpdateStories.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerNewUpdateStories.setNestedScrollingEnabled(false);
        recyclerNewUpdateStories.setAdapter(new StoryAdapter(buildNewUpdateStories()));

        RecyclerView recyclerHotStories = findViewById(R.id.recyclerHotStories);
        recyclerHotStories.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        recyclerHotStories.setNestedScrollingEnabled(false);
        recyclerHotStories.setAdapter(new StoryAdapter(buildHotStories()));

        setViewAllAction(R.id.textViewAllRecommended);
        setViewAllAction(R.id.textViewAllNewUpdates);
        setViewAllAction(R.id.textViewAllHot);

        // Footer search tab listener
        android.widget.LinearLayout menuMain = findViewById(R.id.menuMain);
        if (menuMain != null && menuMain.getChildCount() > 2) {
            android.view.View searchTab = menuMain.getChildAt(2);
            searchTab.setOnClickListener(v -> {
                android.content.Intent searchIntent = new android.content.Intent(HomeUserActivity.this,
                        SearchActivity.class);
                startActivity(searchIntent);
            });
        }
    }

    private void setupTopStoriesCarousel(RecyclerView recyclerTopStories, List<StoryItem> topStories) {
        LinearLayoutManager topLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerTopStories.setLayoutManager(topLayoutManager);
        recyclerTopStories.setNestedScrollingEnabled(false);
        recyclerTopStories.setAdapter(new TopStoryAdapter(topStories));

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerTopStories);

        int virtualMiddle = 5000;
        int offset = topStories.isEmpty() ? 0 : (virtualMiddle % topStories.size());
        int centeredStart = virtualMiddle - offset;
        recyclerTopStories.scrollToPosition(centeredStart);

        recyclerTopStories.post(() -> {
            // Keep the centered card fully visible and let side cards peek out.
            int itemWidth = dpToPx(119);
            int sidePadding = Math.max(0, (recyclerTopStories.getWidth() - itemWidth) / 2);
            recyclerTopStories.setPadding(sidePadding, recyclerTopStories.getPaddingTop(), sidePadding,
                    recyclerTopStories.getPaddingBottom());
            recyclerTopStories.setClipToPadding(false);
            scaleCenterItems(recyclerTopStories);
        });

        recyclerTopStories.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@androidx.annotation.NonNull RecyclerView recyclerView, int dx, int dy) {
                scaleCenterItems(recyclerView);
            }

            @Override
            public void onScrollStateChanged(@androidx.annotation.NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    scaleCenterItems(recyclerView);
                }
            }
        });
    }

    private void scaleCenterItems(RecyclerView recyclerView) {
        float recyclerCenterX = recyclerView.getWidth() / 2f;
        float maxDistance = recyclerView.getWidth() / 2f;

        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            android.view.View child = recyclerView.getChildAt(i);
            float childCenterX = (child.getLeft() + child.getRight()) / 2f;
            float distance = Math.abs(recyclerCenterX - childCenterX);
            float normalized = Math.min(1f, distance / maxDistance);
            float scale = 0.85f + (1f - normalized) * 0.25f;
            child.setScaleX(scale);
            child.setScaleY(scale);
            child.setAlpha(0.65f + (1f - normalized) * 0.35f);
        }
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    private void setViewAllAction(int textViewId) {
        TextView textView = findViewById(textViewId);
        textView.setOnClickListener(
                v -> Toast.makeText(this, R.string.view_all_click_message, Toast.LENGTH_SHORT).show());
    }

    private List<StoryItem> buildTopStories() {
        List<StoryItem> items = new ArrayList<>();
        items.add(new StoryItem("Mặt Trăng Đỏ", "Tác giả: Hà Yên", "Chương 124", "Phiêu lưu"));
        items.add(new StoryItem("Bóng Đêm Ở Kyoto", "Tác giả: Minh Kha", "Chương 57", "Huyền bí"));
        items.add(new StoryItem("Hành Trình Của Gió", "Tác giả: Tuấn Vũ", "Chương 88", "Fantasy"));
        items.add(new StoryItem("Lời Nguyền Mùa Đông", "Tác giả: Phương Anh", "Chương 39", "Tâm lý"));
        items.add(new StoryItem("Sương Đỏ", "Tác giả: Trường An", "Chương 16", "Huyền bí"));
        return items;
    }

    private List<StoryItem> buildRecommendedStories() {
        List<StoryItem> items = new ArrayList<>();
        items.add(new StoryItem("Đêm Không Sao", "Tác giả: Gia Bảo", "Chương 201", "Kinh dị"));
        items.add(new StoryItem("Nắng Cuối Ngày", "Tác giả: Lan Chi", "Chương 74", "Đời thường"));
        items.add(new StoryItem("Hỏa Ấn", "Tác giả: Quang Huy", "Chương 116", "Hành động"));
        items.add(new StoryItem("Mê Cung Ký Ức", "Tác giả: Diệu Linh", "Chương 43", "Trinh thám"));
        items.add(new StoryItem("Bờ Biển Xa", "Tác giả: Thái An", "Chương 12", "Tình cảm"));
        items.add(new StoryItem("Huyễn Mộng", "Tác giả: Bảo Nhi", "Chương 9", "Fantasy"));
        items.add(new StoryItem("Khóa Cửa Đêm", "Tác giả: Phúc Khang", "Chương 61", "Huyền bí"));
        items.add(new StoryItem("Tàn Tro", "Tác giả: Ngọc My", "Chương 38", "Drama"));
        items.add(new StoryItem("Dấu Vết", "Tác giả: Nam Long", "Chương 83", "Trinh thám"));
        items.add(new StoryItem("Sương Mù", "Tác giả: Hoài Thu", "Chương 27", "Kinh dị"));
        return items;
    }

    private List<StoryItem> buildNewUpdateStories() {
        List<StoryItem> items = new ArrayList<>();
        items.add(new StoryItem("Thiên Hà", "Tác giả: Yến Nhi", "Chương 221", "Viễn tưởng"));
        items.add(new StoryItem("Dị Vực", "Tác giả: Thành Luân", "Chương 102", "Phiêu lưu"));
        items.add(new StoryItem("Góc Khuất", "Tác giả: Vân Khôi", "Chương 50", "Trinh thám"));
        items.add(new StoryItem("Mật Danh", "Tác giả: Minh Đức", "Chương 37", "Hành động"));
        items.add(new StoryItem("Mưa Đêm", "Tác giả: Lệ Thu", "Chương 44", "Drama"));
        items.add(new StoryItem("Dòng Chảy", "Tác giả: Gia Nghi", "Chương 81", "Đời thường"));
        items.add(new StoryItem("Lăng Kính", "Tác giả: An Vy", "Chương 29", "Tâm lý"));
        items.add(new StoryItem("Kẻ Gác Cổng", "Tác giả: Đức Hải", "Chương 11", "Kinh dị"));
        items.add(new StoryItem("Mùa Hè Cuối", "Tác giả: Huyền My", "Chương 67", "Tình cảm"));
        items.add(new StoryItem("Mật Mã Đỏ", "Tác giả: Quốc Việt", "Chương 90", "Trinh thám"));
        return items;
    }

    private List<StoryItem> buildHotStories() {
        List<StoryItem> items = new ArrayList<>();
        items.add(new StoryItem("Hắc Nguyệt", "Tác giả: Bảo Trân", "Chương 140", "Fantasy"));
        items.add(new StoryItem("Vùng Cấm", "Tác giả: Tiến Đạt", "Chương 53", "Kinh dị"));
        items.add(new StoryItem("Nhịp Tim", "Tác giả: Ánh Dương", "Chương 78", "Tình cảm"));
        items.add(new StoryItem("Truy Đuổi", "Tác giả: Nhật Long", "Chương 64", "Hành động"));
        items.add(new StoryItem("Rừng Đêm", "Tác giả: Phan Khuê", "Chương 34", "Phiêu lưu"));
        items.add(new StoryItem("Mặt Nạ", "Tác giả: Thục Linh", "Chương 42", "Tâm lý"));
        items.add(new StoryItem("Lời Hứa", "Tác giả: Tú Uyên", "Chương 19", "Đời thường"));
        items.add(new StoryItem("Điểm Mù", "Tác giả: Đình Quang", "Chương 87", "Trinh thám"));
        items.add(new StoryItem("Nhật Thực", "Tác giả: Mỹ Hạnh", "Chương 112", "Huyền bí"));
        items.add(new StoryItem("Ánh Đèn", "Tác giả: Minh Tâm", "Chương 24", "Drama"));
        return items;
    }
}
