package com.example.app_truyen;

public class StoryItem {
    public final String title;
    public final String author;
    public final String chapter;
    public final String genre;
    public final String coverImage;

    public StoryItem(String title, String author, String chapter, String genre) {
        this(title, author, chapter, genre, null);
    }

    public StoryItem(String title, String author, String chapter, String genre, String coverImage) {
        this.title = title;
        this.author = author;
        this.chapter = chapter;
        this.genre = genre;
        this.coverImage = coverImage;
    }
}
