package com.example.app_truyen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private final List<StoryItem> sourceStories = new ArrayList<>();
    private final List<StoryItem> filteredStories = new ArrayList<>();
    private StoryAdapter storyAdapter;
    private EditText inputSearch;
    private String selectedTag = "";

    private TextView tagAll;
    private TextView tagFantasy;
    private TextView tagAction;
    private TextView tagRomance;
    private TextView tagMystery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        RecyclerView recyclerSearchStories = findViewById(R.id.recyclerSearchStories);
        storyAdapter = new StoryAdapter();
        recyclerSearchStories.setLayoutManager(new LinearLayoutManager(this));
        recyclerSearchStories.setAdapter(storyAdapter);

        sourceStories.addAll(createStories());
        filteredStories.addAll(sourceStories);
        storyAdapter.submitList(filteredStories);

        inputSearch = findViewById(R.id.inputSearchStory);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No-op
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilter(s.toString(), selectedTag);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No-op
            }
        });

        setupTags();

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

    private void setupTags() {
        tagAll = findViewById(R.id.tagAll);
        tagFantasy = findViewById(R.id.tagFantasy);
        tagAction = findViewById(R.id.tagAction);
        tagRomance = findViewById(R.id.tagRomance);
        tagMystery = findViewById(R.id.tagMystery);

        tagAll.setOnClickListener(v -> {
            selectedTag = "";
            setTagSelection(tagAll);
            applyFilter(inputSearch.getText().toString(), selectedTag);
        });

        tagFantasy.setOnClickListener(v -> {
            selectedTag = "fantasy";
            setTagSelection(tagFantasy);
            applyFilter(inputSearch.getText().toString(), selectedTag);
        });

        tagAction.setOnClickListener(v -> {
            selectedTag = "hành động";
            setTagSelection(tagAction);
            applyFilter(inputSearch.getText().toString(), selectedTag);
        });

        tagRomance.setOnClickListener(v -> {
            selectedTag = "tình cảm";
            setTagSelection(tagRomance);
            applyFilter(inputSearch.getText().toString(), selectedTag);
        });

        tagMystery.setOnClickListener(v -> {
            selectedTag = "bí ẩn";
            setTagSelection(tagMystery);
            applyFilter(inputSearch.getText().toString(), selectedTag);
        });
    }

    private void setTagSelection(TextView activeTag) {
        TextView[] allTags = { tagAll, tagFantasy, tagAction, tagRomance, tagMystery };
        for (TextView tag : allTags) {
            boolean isActive = tag == activeTag;
            tag.setBackgroundResource(isActive ? R.drawable.bg_tag_active : R.drawable.bg_tag_inactive);
            tag.setTextColor(getColor(isActive ? R.color.tag_text_active : R.color.tag_text_inactive));
            tag.setTypeface(tag.getTypeface(),
                    isActive ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
        }
    }

    private void applyFilter(String keyword, String tag) {
        String lower = keyword.trim().toLowerCase(Locale.ROOT);
        filteredStories.clear();

        for (StoryItem item : sourceStories) {
            boolean matchedKeyword = lower.isEmpty()
                    || item.getTitle().toLowerCase(Locale.ROOT).contains(lower)
                    || item.getCategory().toLowerCase(Locale.ROOT).contains(lower)
                    || item.getAuthor().toLowerCase(Locale.ROOT).contains(lower);

            boolean matchedTag = tag.isEmpty() || item.getCategory().toLowerCase(Locale.ROOT).contains(tag);

            if (matchedKeyword && matchedTag) {
                filteredStories.add(item);
            }
        }

        storyAdapter.submitList(filteredStories);
    }

    private List<StoryItem> createStories() {
        List<StoryItem> stories = new ArrayList<>();
        stories.add(new StoryItem("Chị Gái Lại Toang Nữa Rồi", "Fantasy", "450", "Se Chan", "Đang cập nhật", "4.5/5",
                "121.004", false));
        stories.add(new StoryItem("Đêm bên làn nước", "Hành động", "Ngoại truyện 12", "Eo Ju", "Đang hoàn", "5/5",
                "549.297", true));
        stories.add(new StoryItem("Chỉ vì tốt cho em", "Tình cảm", "44", "aesthetics & Eobul", "Đang cập nhật", "4/5",
                "367.921", false));
        stories.add(new StoryItem("Bí mật thành phố mưa", "Bí ẩn", "102", "Khánh Duy", "Đang cập nhật", "4.5/5",
                "218.313", false));
        stories.add(new StoryItem("Lời hứa mùa hạ", "Tình cảm", "87", "Nhi Linh", "Đang hoàn", "4.8/5",
                "498.110", true));
        return stories;
    }
}