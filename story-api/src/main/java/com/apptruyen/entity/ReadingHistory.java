package com.apptruyen.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reading_history",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "story_id"})
)
@NoArgsConstructor @AllArgsConstructor
public class ReadingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "story_id", nullable = false)
    private Story story;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "last_chapter_id", nullable = false)
    private Chapter lastChapter;

    /**
     * Phần trăm đã đọc trong chương hiện tại (0 - 100)
     */
    @Column(name = "scroll_position")
    private Integer scrollPosition = 0;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Explicit getters
    public Integer getId() { return id; }
    public User getUser() { return user; }
    public Story getStory() { return story; }
    public Chapter getLastChapter() { return lastChapter; }
    public Integer getScrollPosition() { return scrollPosition; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Explicit setters
    public void setId(Integer id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setStory(Story story) { this.story = story; }
    public void setLastChapter(Chapter lastChapter) { this.lastChapter = lastChapter; }
    public void setScrollPosition(Integer scrollPosition) { this.scrollPosition = scrollPosition; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Builder support
    public static ReadingHistoryBuilder builder() {
        return new ReadingHistoryBuilder();
    }

    public static class ReadingHistoryBuilder {
        private Integer id;
        private User user;
        private Story story;
        private Chapter lastChapter;
        private Integer scrollPosition = 0;
        private LocalDateTime updatedAt;

        public ReadingHistoryBuilder id(Integer id) { this.id = id; return this; }
        public ReadingHistoryBuilder user(User user) { this.user = user; return this; }
        public ReadingHistoryBuilder story(Story story) { this.story = story; return this; }
        public ReadingHistoryBuilder lastChapter(Chapter lastChapter) { this.lastChapter = lastChapter; return this; }
        public ReadingHistoryBuilder scrollPosition(Integer scrollPosition) { this.scrollPosition = scrollPosition; return this; }
        public ReadingHistoryBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public ReadingHistory build() {
            ReadingHistory rh = new ReadingHistory();
            rh.id = this.id;
            rh.user = this.user;
            rh.story = this.story;
            rh.lastChapter = this.lastChapter;
            rh.scrollPosition = this.scrollPosition;
            rh.updatedAt = this.updatedAt;
            return rh;
        }
    }
}
