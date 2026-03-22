package com.apptruyen.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class SaveProgressRequest {

    @NotNull(message = "storyId is required")
    private Integer storyId;

    @NotNull(message = "lastChapterId is required")
    private Integer lastChapterId;

    @Min(0) @Max(100)
    private Integer scrollPosition = 0;

    public SaveProgressRequest() {}

    public SaveProgressRequest(Integer storyId, Integer lastChapterId, Integer scrollPosition) {
        this.storyId = storyId;
        this.lastChapterId = lastChapterId;
        this.scrollPosition = scrollPosition;
    }

    public Integer getStoryId() { return storyId; }
    public void setStoryId(Integer storyId) { this.storyId = storyId; }

    public Integer getLastChapterId() { return lastChapterId; }
    public void setLastChapterId(Integer lastChapterId) { this.lastChapterId = lastChapterId; }

    public Integer getScrollPosition() { return scrollPosition; }
    public void setScrollPosition(Integer scrollPosition) { this.scrollPosition = scrollPosition; }
}
