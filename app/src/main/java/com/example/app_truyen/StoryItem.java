package com.example.app_truyen;

public class StoryItem {
    private final String title;
    private final String category;
    private final String chapter;
    private final String author;
    private final String status;
    private final String score;
    private final String views;
    private final boolean favorite;

    public StoryItem(String title, String category, String chapter, String rating) {
        this(title, category, chapter, "", "", rating, "", false);
    }

    public StoryItem(String title, String category, String chapter, String author,
            String status, String score, String views, boolean favorite) {
        this.title = title;
        this.category = category;
        this.chapter = chapter;
        this.author = author;
        this.status = status;
        this.score = score;
        this.views = views;
        this.favorite = favorite;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getChapter() {
        return chapter;
    }

    public String getAuthor() {
        return author;
    }

    public String getStatus() {
        return status;
    }

    public String getScore() {
        return score;
    }

    public String getViews() {
        return views;
    }

    public boolean isFavorite() {
        return favorite;
    }
}
