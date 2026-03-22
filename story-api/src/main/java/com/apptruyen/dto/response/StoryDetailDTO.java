package com.apptruyen.dto.response;

import com.apptruyen.entity.Story;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO chi tiết truyện (thêm description so với StorySummaryDTO).
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class StoryDetailDTO {

    private Integer id;
    private String title;
    private String slug;
    private String author;
    private String description;
    private String coverUrl;
    private Story.StoryStatus status;
    private Integer totalChapters;
    private Long views;
    private BigDecimal rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<GenreDTO> genres;
}
