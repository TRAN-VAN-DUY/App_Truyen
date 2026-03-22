package com.apptruyen.service;

import com.apptruyen.dto.response.*;
import com.apptruyen.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class để convert Entity → DTO.
 */
@Component
public class DtoMapper {

    public GenreDTO toGenreDTO(Genre genre) {
        return GenreDTO.builder()
                .id(genre.getId())
                .name(genre.getName())
                .slug(genre.getSlug())
                .build();
    }

    public StorySummaryDTO toStorySummaryDTO(Story story) {
        List<GenreDTO> genres = story.getGenres().stream()
                .map(this::toGenreDTO)
                .collect(Collectors.toList());

        return StorySummaryDTO.builder()
                .id(story.getId())
                .title(story.getTitle())
                .slug(story.getSlug())
                .author(story.getAuthor())
                .coverUrl(story.getCoverUrl())
                .status(story.getStatus())
                .totalChapters(story.getTotalChapters())
                .views(story.getViews())
                .rating(story.getRating())
                .updatedAt(story.getUpdatedAt())
                .genres(genres)
                .build();
    }

    public StoryDetailDTO toStoryDetailDTO(Story story) {
        List<GenreDTO> genres = story.getGenres().stream()
                .map(this::toGenreDTO)
                .collect(Collectors.toList());

        return StoryDetailDTO.builder()
                .id(story.getId())
                .title(story.getTitle())
                .slug(story.getSlug())
                .author(story.getAuthor())
                .description(story.getDescription())
                .coverUrl(story.getCoverUrl())
                .status(story.getStatus())
                .totalChapters(story.getTotalChapters())
                .views(story.getViews())
                .rating(story.getRating())
                .createdAt(story.getCreatedAt())
                .updatedAt(story.getUpdatedAt())
                .genres(genres)
                .build();
    }

    public ChapterDTO toChapterDTO(Chapter chapter) {
        return ChapterDTO.builder()
                .id(chapter.getId())
                .chapterNumber(chapter.getChapterNumber())
                .title(chapter.getTitle())
                .wordCount(chapter.getWordCount())
                .createdAt(chapter.getCreatedAt())
                .build();
    }

    public FavoriteDTO toFavoriteDTO(Favorite favorite) {
        return FavoriteDTO.builder()
                .id(favorite.getId())
                .story(toStorySummaryDTO(favorite.getStory()))
                .createdAt(favorite.getCreatedAt())
                .build();
    }

    public ContinueReadingDTO toContinueReadingDTO(ReadingHistory history) {
        return ContinueReadingDTO.builder()
                .historyId(history.getId())
                .story(toStorySummaryDTO(history.getStory()))
                .lastChapter(toChapterDTO(history.getLastChapter()))
                .scrollPosition(history.getScrollPosition())
                .updatedAt(history.getUpdatedAt())
                .build();
    }

    public <T> PagedResponse<T> toPagedResponse(Page<T> page) {
        return PagedResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
