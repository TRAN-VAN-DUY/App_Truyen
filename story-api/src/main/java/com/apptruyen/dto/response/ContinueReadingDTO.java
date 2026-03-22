package com.apptruyen.dto.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO "xem tiếp" — thông tin truyện + chapter đang đọc dở + scroll position.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ContinueReadingDTO {
    private Integer historyId;
    private StorySummaryDTO story;
    private ChapterDTO lastChapter;
    /** Phần trăm đã đọc trong chương (0-100) */
    private Integer scrollPosition;
    private LocalDateTime updatedAt;
}
