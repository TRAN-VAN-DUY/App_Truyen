package com.apptruyen.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reading_history",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "story_id"})
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
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
    @Builder.Default
    @Column(name = "scroll_position")
    private Integer scrollPosition = 0;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
