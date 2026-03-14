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
    private SearchStoryAdapter storyAdapter;
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
        storyAdapter = new SearchStoryAdapter(filteredStories);
        recyclerSearchStories.setLayoutManager(new LinearLayoutManager(this));
        recyclerSearchStories.setAdapter(storyAdapter);

        sourceStories.addAll(createStories());
        filteredStories.addAll(sourceStories);
        storyAdapter.notifyDataSetChanged();

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
                    || item.title.toLowerCase(Locale.ROOT).contains(lower)
                    || item.genre.toLowerCase(Locale.ROOT).contains(lower)
                    || item.author.toLowerCase(Locale.ROOT).contains(lower);

            boolean matchedTag = tag.isEmpty() || item.genre.toLowerCase(Locale.ROOT).contains(tag);

            if (matchedKeyword && matchedTag) {
                filteredStories.add(item);
            }
        }

        storyAdapter.notifyDataSetChanged();
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