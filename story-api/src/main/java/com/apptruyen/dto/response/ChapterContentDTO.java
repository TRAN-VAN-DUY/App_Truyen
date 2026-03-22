package com.apptruyen.dto.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO đọc nội dung chương, kèm navigation sang chương trước/sau.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChapterContentDTO {
    private Integer id;
    private Integer chapterNumber;
    private String title;
    private String content;
    private Integer wordCount;
    private LocalDateTime createdAt;

    // Navigation
    private Integer prevChapterNumber;   // null nếu là chương đầu
    private Integer nextChapterNumber;   // null nếu là chương cuối

    private String storySlug;
    private String storyTitle;
}
