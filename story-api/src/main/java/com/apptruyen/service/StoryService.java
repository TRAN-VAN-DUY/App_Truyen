package com.apptruyen.service;

import com.apptruyen.dto.response.*;
import com.apptruyen.entity.Story;
import com.apptruyen.exception.ResourceNotFoundException;
import com.apptruyen.repository.ChapterRepository;
import com.apptruyen.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoryService {

    private final StoryRepository storyRepository;
    private final ChapterRepository chapterRepository;
    private final DtoMapper mapper;

    /** Lấy danh sách truyện với filter và sort */
    public PagedResponse<StorySummaryDTO> getStories(
            int page, int size,
            String genre,
            String status,
            String sort) {

        Pageable pageable = buildPageable(page, size, sort);
        Page<Story> storyPage;

        boolean hasGenre = genre != null && !genre.isBlank();
        boolean hasStatus = status != null && !status.isBlank();

        if (hasGenre && hasStatus) {
            Story.StoryStatus storyStatus = parseStatus(status);
            storyPage = storyRepository.findByGenreSlugAndStatus(genre, storyStatus, pageable);
        } else if (hasGenre) {
            storyPage = storyRepository.findByGenreSlug(genre, pageable);
        } else if (hasStatus) {
            Story.StoryStatus storyStatus = parseStatus(status);
            storyPage = storyRepository.findByStatus(storyStatus, pageable);
        } else {
            storyPage = storyRepository.findAll(pageable);
        }

        Page<StorySummaryDTO> dtoPage = storyPage.map(mapper::toStorySummaryDTO);
        return mapper.toPagedResponse(dtoPage);
    }

    /** Tìm kiếm truyện theo tên/tác giả */
    public PagedResponse<StorySummaryDTO> searchStories(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<StorySummaryDTO> dtoPage = storyRepository
                .searchByKeyword(keyword, pageable)
                .map(mapper::toStorySummaryDTO);
        return mapper.toPagedResponse(dtoPage);
    }

    /** Chi tiết truyện theo slug */
    public StoryDetailDTO getStoryBySlug(String slug) {
        Story story = storyRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Story", "slug", slug));
        return mapper.toStoryDetailDTO(story);
    }

    /** Chi tiết truyện theo ID */
    public StoryDetailDTO getStoryById(Integer id) {
        Story story = storyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Story", "id", id));
        return mapper.toStoryDetailDTO(story);
    }

    /** Danh sách chương theo slug */
    public List<ChapterDTO> getChaptersByStorySlug(String slug) {
        Story story = storyRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Story", "slug", slug));
        return chapterRepository.findAllByStoryOrderByChapterNumberAsc(story)
                .stream()
                .map(mapper::toChapterDTO)
                .collect(Collectors.toList());
    }

    /** Danh sách chương theo story ID */
    public List<ChapterDTO> getChaptersByStoryId(Integer id) {
        Story story = storyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Story", "id", id));
        return chapterRepository.findAllByStoryOrderByChapterNumberAsc(story)
                .stream()
                .map(mapper::toChapterDTO)
                .collect(Collectors.toList());
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Pageable buildPageable(int page, int size, String sort) {
        Sort sortObj = switch (sort == null ? "" : sort) {
            case "views"   -> Sort.by(Sort.Direction.DESC, "views");
            case "rating"  -> Sort.by(Sort.Direction.DESC, "rating");
            case "newest"  -> Sort.by(Sort.Direction.DESC, "createdAt");
            default        -> Sort.by(Sort.Direction.DESC, "updatedAt"); // mới cập nhật
        };
        return PageRequest.of(page, size, sortObj);
    }

    private Story.StoryStatus parseStatus(String status) {
        try {
            return Story.StoryStatus.valueOf(status.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status +
                    ". Allowed: ongoing, completed, hiatus");
        }
    }
}
