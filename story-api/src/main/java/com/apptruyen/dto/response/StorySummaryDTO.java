package com.apptruyen.dto.response;

import com.apptruyen.entity.Story;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO hiển thị thông tin tóm tắt của truyện trong danh sách.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StorySummaryDTO {

    private Integer id;
    private String title;
    private String slug;
    private String author;
    private String coverUrl;
    private Story.StoryStatus status;
    private Integer totalChapters;
    private Long views;
    private BigDecimal rating;
    private LocalDateTime updatedAt;
    private List<GenreDTO> genres;
}
