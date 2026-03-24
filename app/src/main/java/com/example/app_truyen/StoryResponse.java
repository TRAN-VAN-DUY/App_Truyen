package com.example.app_truyen;

public class StoryResponse {
    public long id;
    public String title;
    public String slug;
    public String description;
    public String author;
    public String coverImage;
    public String status;
    public String category;
    public double rating;
    public int views;
    public int chapterCount;
    public boolean isFavorited;

    public StoryResponse() {
    }

    public StoryResponse(String title, String author, String chapter, String genre) {
        this.title = title;
        this.author = author;
        this.category = genre;
        this.chapterCount = Integer.parseInt(chapter);
    }
}
