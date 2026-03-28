package com.apptruyen.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chapters",
    uniqueConstraints = @UniqueConstraint(columnNames = {"story_id", "chapter_number"})
)
@NoArgsConstructor @AllArgsConstructor
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "story_id", nullable = false)
    private Story story;

    @Column(name = "chapter_number", nullable = false)
    private Integer chapterNumber;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "word_count")
    private Integer wordCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Explicit getters
    public Integer getId() { return id; }
    public Story getStory() { return story; }
    public Integer getChapterNumber() { return chapterNumber; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public Integer getWordCount() { return wordCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Explicit setters
    public void setId(Integer id) { this.id = id; }
    public void setStory(Story story) { this.story = story; }
    public void setChapterNumber(Integer chapterNumber) { this.chapterNumber = chapterNumber; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setWordCount(Integer wordCount) { this.wordCount = wordCount; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Builder support
    public static ChapterBuilder builder() {
        return new ChapterBuilder();
    }

    public static class ChapterBuilder {
        private Integer id;
        private Story story;
        private Integer chapterNumber;
        private String title;
        private String content;
        private Integer wordCount = 0;
        private LocalDateTime createdAt;

        public ChapterBuilder id(Integer id) { this.id = id; return this; }
        public ChapterBuilder story(Story story) { this.story = story; return this; }
        public ChapterBuilder chapterNumber(Integer chapterNumber) { this.chapterNumber = chapterNumber; return this; }
        public ChapterBuilder title(String title) { this.title = title; return this; }
        public ChapterBuilder content(String content) { this.content = content; return this; }
        public ChapterBuilder wordCount(Integer wordCount) { this.wordCount = wordCount; return this; }
        public ChapterBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Chapter build() {
            Chapter c = new Chapter();
            c.id = this.id;
            c.story = this.story;
            c.chapterNumber = this.chapterNumber;
            c.title = this.title;
            c.content = this.content;
            c.wordCount = this.wordCount;
            c.createdAt = this.createdAt;
            return c;
        }
    }
}
