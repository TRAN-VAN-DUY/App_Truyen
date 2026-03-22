package com.apptruyen.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GenreDTO {
    private Integer id;
    private String name;
    private String slug;
}
