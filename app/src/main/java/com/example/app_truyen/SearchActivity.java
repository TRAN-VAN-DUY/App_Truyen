package com.example.app_truyen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class SearchActivity extends AppCompatActivity {

    private final List<StoryResponse> sourceStories = new ArrayList<>();
    private final List<StoryResponse> filteredStories = new ArrayList<>();
    private SearchStoryAdapter storyAdapter;
    private EditText inputSearch;
    private String selectedTag = "";
    private StoryApiService apiService;
    private LinearLayout tagsContainer;
    private TextView activeTagView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        apiService = ApiClient.getClient().create(StoryApiService.class);

        RecyclerView recyclerSearchStories = findViewById(R.id.recyclerSearchStories);
        storyAdapter = new SearchStoryAdapter(filteredStories);
        recyclerSearchStories.setLayoutManager(new LinearLayoutManager(this));
        recyclerSearchStories.setAdapter(storyAdapter);

        // Load danh sách truyện ban đầu
        loadInitialStories();

        inputSearch = findViewById(R.id.inputSearchStory);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No-op
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();
                if (keyword.isEmpty()) {
                    // Reset về danh sách ban đầu khi clear input
                    filteredStories.clear();
                    filteredStories.addAll(sourceStories);
                    storyAdapter.notifyDataSetChanged();
                } else {
                    searchFromApi(keyword);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No-op
            }
        });

        tagsContainer = findViewById(R.id.tagsContainer);
        renderTagsFromStories();

        TextView buttonBackSearch = findViewById(R.id.buttonBackSearch);
        buttonBackSearch.setOnClickListener(v -> finish());

        LinearLayout footerTabHome = findViewById(R.id.footerTabHomeSearchPage);
        footerTabHome.setOnClickListener(v -> {
            Intent homeIntent = new Intent(SearchActivity.this, HomeUserActivity.class);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(homeIntent);
            finish();
        });
    }

    private void renderTagsFromStories() {
        if (tagsContainer == null) {
            return;
        }

        tagsContainer.removeAllViews();
        List<String> tags = collectTagsFromStories();

        selectedTag = "";
        TextView allChip = createTagChip("Tất cả", "");
        tagsContainer.addView(allChip);
        activeTagView = allChip;
        updateTagSelectionState(activeTagView);

        for (String tag : tags) {
            TextView tagChip = createTagChip(tag, tag.toLowerCase(Locale.ROOT));
            tagsContainer.addView(tagChip);
        }
    }

    private List<String> collectTagsFromStories() {
        Set<String> uniqueTags = new LinkedHashSet<>();
        for (StoryResponse item : sourceStories) {
            if (item.category == null || item.category.trim().isEmpty()) {
                continue;
            }

            String[] splitTags = item.category.split(",");
            for (String rawTag : splitTags) {
                String cleanTag = rawTag.trim();
                if (!cleanTag.isEmpty()) {
                    uniqueTags.add(cleanTag);
                }
            }
        }
        return new ArrayList<>(uniqueTags);
    }

    private TextView createTagChip(String label, String tagValue) {
        TextView chip = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                dpToPx(28));
        params.setMarginEnd(dpToPx(8));
        chip.setLayoutParams(params);
        chip.setGravity(android.view.Gravity.CENTER);
        chip.setPadding(dpToPx(12), 0, dpToPx(12), 0);
        chip.setText(label);
        chip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        chip.setOnClickListener(v -> {
            selectedTag = tagValue;
            activeTagView = chip;
            updateTagSelectionState(activeTagView);
            applyFilter(inputSearch.getText().toString(), selectedTag);
        });
        return chip;
    }

    private void updateTagSelectionState(TextView activeTag) {
        for (int i = 0; i < tagsContainer.getChildCount(); i++) {
            TextView chip = (TextView) tagsContainer.getChildAt(i);
            boolean isActive = chip == activeTag;
            chip.setBackgroundResource(isActive ? R.drawable.bg_tag_active : R.drawable.bg_tag_inactive);
            chip.setTextColor(getColor(isActive ? R.color.tag_text_active : R.color.tag_text_inactive));
            chip.setTypeface(chip.getTypeface(),
                    isActive ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
        }
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    private void applyFilter(String keyword, String tag) {
        String lower = keyword.trim().toLowerCase(Locale.ROOT);
        filteredStories.clear();

        for (StoryResponse item : sourceStories) {
            String title = item.title == null ? "" : item.title;
            String category = item.category == null ? "" : item.category;
            String author = item.author == null ? "" : item.author;
            boolean matchedKeyword = lower.isEmpty()
                    || title.toLowerCase(Locale.ROOT).contains(lower)
                    || category.toLowerCase(Locale.ROOT).contains(lower)
                    || author.toLowerCase(Locale.ROOT).contains(lower);

            boolean matchedTag = tag.isEmpty() || category.toLowerCase(Locale.ROOT).contains(tag);

            if (matchedKeyword && matchedTag) {
                filteredStories.add(item);
            }
        }

        storyAdapter.notifyDataSetChanged();
    }

    private void loadInitialStories() {
        Call<ApiResponse<SearchResponse>> call = apiService.getStories(0, 10);
        call.enqueue(new Callback<ApiResponse<SearchResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<SearchResponse>> call,
                    Response<ApiResponse<SearchResponse>> response) {
                ApiResponse<SearchResponse> body = response.body();
                if (response.isSuccessful() && body != null && body.data != null && body.data.content != null) {
                    sourceStories.clear();
                    sourceStories.addAll(body.data.content);
                    renderTagsFromStories();
                    applyFilter(inputSearch.getText().toString(), selectedTag);
                    android.util.Log.d("SearchActivity", "Loaded " + sourceStories.size() + " stories");
                } else {
                    android.util.Log.e("SearchActivity", "Response failed: " + response.code());
                    Toast.makeText(SearchActivity.this, "Không thể tải danh sách (code: " + response.code() + ")",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<SearchResponse>> call, Throwable t) {
                android.util.Log.e("SearchActivity", "API Error: " + t.getMessage());
                Toast.makeText(SearchActivity.this, "Không thể tải danh sách: " + t.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void searchFromApi(String keyword) {
        Call<ApiResponse<SearchResponse>> call = apiService.searchStories(keyword);
        call.enqueue(new Callback<ApiResponse<SearchResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<SearchResponse>> call,
                    Response<ApiResponse<SearchResponse>> response) {
                ApiResponse<SearchResponse> body = response.body();
                if (response.isSuccessful() && body != null && body.data != null && body.data.content != null) {
                    sourceStories.clear();
                    sourceStories.addAll(body.data.content);
                    renderTagsFromStories();
                    applyFilter(keyword, selectedTag);
                } else {
                    Toast.makeText(SearchActivity.this, "Lỗi tìm kiếm, thử lại sau", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<SearchResponse>> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Không thể kết nối API: " + t.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private List<StoryItem> createStories() {
        List<StoryItem> stories = new ArrayList<>();
        stories.add(new StoryItem("Chi Gai Lai Toang Nua Roi", "Se Chan", "450", "Fantasy"));
        stories.add(new StoryItem("Dem Ben Lan Nuoc", "Eo Ju", "Ngoai truyen 12", "Hanh dong"));
        stories.add(new StoryItem("Chi Vi Tot Cho Em", "aesthetics & Eobul", "44", "Tinh cam"));
        stories.add(new StoryItem("Bi Mat Thanh Pho Mua", "Khanh Duy", "102", "Bi an"));
        stories.add(new StoryItem("Loi Hua Mua Ha", "Nhi Linh", "87", "Tinh cam"));
        return stories;
    }
}