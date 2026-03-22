package com.apptruyen.service;

import com.apptruyen.dto.response.ChapterContentDTO;
import com.apptruyen.entity.Chapter;
import com.apptruyen.entity.Story;
import com.apptruyen.exception.ResourceNotFoundException;
import com.apptruyen.repository.ChapterRepository;
import com.apptruyen.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChapterService {

    private final StoryRepository storyRepository;
    private final ChapterRepository chapterRepository;

    /**
     * Lấy nội dung chương để đọc.
     * Đồng thời tăng views của truyện lên 1.
     */
    @Transactional
    public ChapterContentDTO getChapterContent(String storySlug, Integer chapterNumber) {
        Story story = storyRepository.findBySlug(storySlug)
                .orElseThrow(() -> new ResourceNotFoundException("Story", "slug", storySlug));

        Chapter chapter = chapterRepository.findByStoryAndChapterNumber(story, chapterNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Chapter", "chapterNumber", chapterNumber));

        // Tăng lượt xem
        storyRepository.incrementViews(story.getId());

        // Tìm chương trước/sau
        Integer prevNumber = chapterNumber > 1 ? chapterNumber - 1 : null;
        Integer nextNumber = chapterNumber < story.getTotalChapters() ? chapterNumber + 1 : null;

        // Kiểm tra chương trước/sau thực sự tồn tại trong DB
        if (prevNumber != null &&
            chapterRepository.findByStoryAndChapterNumber(story, prevNumber).isEmpty()) {
            prevNumber = null;
        }
        if (nextNumber != null &&
            chapterRepository.findByStoryAndChapterNumber(story, nextNumber).isEmpty()) {
            nextNumber = null;
        }

        return ChapterContentDTO.builder()
                .id(chapter.getId())
                .chapterNumber(chapter.getChapterNumber())
                .title(chapter.getTitle())
                .content(chapter.getContent())
                .wordCount(chapter.getWordCount())
                .createdAt(chapter.getCreatedAt())
                .prevChapterNumber(prevNumber)
                .nextChapterNumber(nextNumber)
                .storySlug(story.getSlug())
                .storyTitle(story.getTitle())
                .build();
    }

    /**
     * Lấy nội dung chương theo chapter ID trực tiếp.
     * Đồng thời tăng views của truyện lên 1.
     */
    @Transactional
    public ChapterContentDTO getChapterContentById(Integer chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter", "id", chapterId));
        Story story = chapter.getStory();

        // Tăng lượt xem
        storyRepository.incrementViews(story.getId());

        Integer chapterNumber = chapter.getChapterNumber();
        Integer prevNumber = chapterNumber > 1 ? chapterNumber - 1 : null;
        Integer nextNumber = chapterNumber < story.getTotalChapters() ? chapterNumber + 1 : null;

        if (prevNumber != null &&
            chapterRepository.findByStoryAndChapterNumber(story, prevNumber).isEmpty()) {
            prevNumber = null;
        }
        if (nextNumber != null &&
            chapterRepository.findByStoryAndChapterNumber(story, nextNumber).isEmpty()) {
            nextNumber = null;
        }

        return ChapterContentDTO.builder()
                .id(chapter.getId())
                .chapterNumber(chapter.getChapterNumber())
                .title(chapter.getTitle())
                .content(chapter.getContent())
                .wordCount(chapter.getWordCount())
                .createdAt(chapter.getCreatedAt())
                .prevChapterNumber(prevNumber)
                .nextChapterNumber(nextNumber)
                .storySlug(story.getSlug())
                .storyTitle(story.getTitle())
                .build();
    }
}
