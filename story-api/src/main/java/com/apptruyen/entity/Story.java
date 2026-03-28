package com.apptruyen.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "stories")
@NoArgsConstructor @AllArgsConstructor
public class Story {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "slug", nullable = false, unique = true, length = 255)
    private String slug;

    @Column(name = "author", length = 255)
    private String author;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "cover_url", length = 500)
    private String coverUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StoryStatus status = StoryStatus.ongoing;

    @Column(name = "total_chapters")
    private Integer totalChapters = 0;

    @Column(name = "views")
    private Long views = 0L;

    @Column(name = "rating", precision = 3, scale = 1)
    private BigDecimal rating = BigDecimal.ZERO;

    @Column(name = "source_url", length = 500)
    private String sourceUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "story_genres",
        joinColumns = @JoinColumn(name = "story_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum StoryStatus {
        ongoing, completed, hiatus
    }

    // Explicit getters
    public Integer getId() { return id; }
    public String getTitle() { return title; }
    public String getSlug() { return slug; }
    public String getAuthor() { return author; }
    public String getDescription() { return description; }
    public String getCoverUrl() { return coverUrl; }
    public StoryStatus getStatus() { return status; }
    public Integer getTotalChapters() { return totalChapters; }
    public Long getViews() { return views; }
    public BigDecimal getRating() { return rating; }
    public String getSourceUrl() { return sourceUrl; }
    public Set<Genre> getGenres() { return genres; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Explicit setters
    public void setId(Integer id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setSlug(String slug) { this.slug = slug; }
    public void setAuthor(String author) { this.author = author; }
    public void setDescription(String description) { this.description = description; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }
    public void setStatus(StoryStatus status) { this.status = status; }
    public void setTotalChapters(Integer totalChapters) { this.totalChapters = totalChapters; }
    public void setViews(Long views) { this.views = views; }
    public void setRating(BigDecimal rating) { this.rating = rating; }
    public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }
    public void setGenres(Set<Genre> genres) { this.genres = genres; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Builder support
    public static StoryBuilder builder() {
        return new StoryBuilder();
    }

    public static class StoryBuilder {
        private Integer id;
        private String title;
        private String slug;
        private String author;
        private String description;
        private String coverUrl;
        private StoryStatus status = StoryStatus.ongoing;
        private Integer totalChapters = 0;
        private Long views = 0L;
        private BigDecimal rating = BigDecimal.ZERO;
        private String sourceUrl;
        private Set<Genre> genres = new HashSet<>();
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public StoryBuilder id(Integer id) { this.id = id; return this; }
        public StoryBuilder title(String title) { this.title = title; return this; }
        public StoryBuilder slug(String slug) { this.slug = slug; return this; }
        public StoryBuilder author(String author) { this.author = author; return this; }
        public StoryBuilder description(String description) { this.description = description; return this; }
        public StoryBuilder coverUrl(String coverUrl) { this.coverUrl = coverUrl; return this; }
        public StoryBuilder status(StoryStatus status) { this.status = status; return this; }
        public StoryBuilder totalChapters(Integer totalChapters) { this.totalChapters = totalChapters; return this; }
        public StoryBuilder views(Long views) { this.views = views; return this; }
        public StoryBuilder rating(BigDecimal rating) { this.rating = rating; return this; }
        public StoryBuilder sourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; return this; }
        public StoryBuilder genres(Set<Genre> genres) { this.genres = genres; return this; }
        public StoryBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public StoryBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Story build() {
            Story s = new Story();
            s.id = this.id;
            s.title = this.title;
            s.slug = this.slug;
            s.author = this.author;
            s.description = this.description;
            s.coverUrl = this.coverUrl;
            s.status = this.status;
            s.totalChapters = this.totalChapters;
            s.views = this.views;
            s.rating = this.rating;
            s.sourceUrl = this.sourceUrl;
            s.genres = this.genres;
            s.createdAt = this.createdAt;
            s.updatedAt = this.updatedAt;
            return s;
        }
    }
}
