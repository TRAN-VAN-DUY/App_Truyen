package com.apptruyen.dto.request;

import jakarta.validation.constraints.NotNull;

public class FavoriteRequest {

    @NotNull(message = "storyId is required")
    private Integer storyId;

    public FavoriteRequest() {}

    public FavoriteRequest(Integer storyId) {
        this.storyId = storyId;
    }

    public Integer getStoryId() { return storyId; }
    public void setStoryId(Integer storyId) { this.storyId = storyId; }
}
