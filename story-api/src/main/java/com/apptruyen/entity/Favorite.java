package com.apptruyen.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "favorites",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "story_id"})
)
@NoArgsConstructor @AllArgsConstructor
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "story_id", nullable = false)
    private Story story;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Explicit getters
    public Integer getId() { return id; }
    public User getUser() { return user; }
    public Story getStory() { return story; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Explicit setters
    public void setId(Integer id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setStory(Story story) { this.story = story; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Builder support
    public static FavoriteBuilder builder() {
        return new FavoriteBuilder();
    }

    public static class FavoriteBuilder {
        private Integer id;
        private User user;
        private Story story;
        private LocalDateTime createdAt;

        public FavoriteBuilder id(Integer id) { this.id = id; return this; }
        public FavoriteBuilder user(User user) { this.user = user; return this; }
        public FavoriteBuilder story(Story story) { this.story = story; return this; }
        public FavoriteBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public Favorite build() {
            Favorite f = new Favorite();
            f.id = this.id;
            f.user = this.user;
            f.story = this.story;
            f.createdAt = this.createdAt;
            return f;
        }
    }
}
