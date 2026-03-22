package com.apptruyen.dto.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO cho danh sách chương (không kèm content).
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChapterDTO {
    private Integer id;
    private Integer chapterNumber;
    private String title;
    private Integer wordCount;
    private LocalDateTime createdAt;
}
