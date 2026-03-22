package com.apptruyen.dto.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO truyện yêu thích.
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FavoriteDTO {
    private Integer id;
    private StorySummaryDTO story;
    private LocalDateTime createdAt;
}
