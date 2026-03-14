package com.example.app_truyen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        RecyclerView recyclerStories = findViewById(R.id.recyclerStories);
        StoryAdapter storyAdapter = new StoryAdapter();
        recyclerStories.setLayoutManager(new LinearLayoutManager(this));
        recyclerStories.setAdapter(storyAdapter);
        storyAdapter.submitList(createStories());

        LinearLayout footerSearch = findViewById(R.id.footerTabSearch);
        footerSearch.setOnClickListener(v -> {
            Intent searchIntent = new Intent(HomeUserActivity.this, SearchActivity.class);
            startActivity(searchIntent);
        });
    }

    private List<StoryItem> createStories() {
        List<StoryItem> stories = new ArrayList<>();
        stories.add(new StoryItem("Thánh Nữ Quỷ Vương", "Fantasy", "Chương 124", "4.9"));
        stories.add(new StoryItem("Cửu Mệnh Miêu", "Hành động", "Chương 77", "4.8"));
        stories.add(new StoryItem("Mộng Du Ký", "Tình cảm", "Chương 31", "4.7"));
        stories.add(new StoryItem("Huyết Ảnh", "Trinh thám", "Chương 96", "4.8"));
        stories.add(new StoryItem("Kẻ Trở Lại", "Phiêu lưu", "Chương 58", "4.6"));
        stories.add(new StoryItem("Đại Lục Ma Pháp", "Fantasy", "Chương 202", "5.0"));
        stories.add(new StoryItem("Vết Cắt Ký Ức", "Bí ẩn", "Chương 42", "4.7"));
        stories.add(new StoryItem("Hắc Dạ Thành", "Kinh dị", "Chương 19", "4.5"));
        return stories;
    }
}
