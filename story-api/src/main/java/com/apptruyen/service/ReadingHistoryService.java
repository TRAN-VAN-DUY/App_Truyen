package com.apptruyen.service;

import com.apptruyen.dto.request.SaveProgressRequest;
import com.apptruyen.dto.response.ContinueReadingDTO;
import com.apptruyen.dto.response.PagedResponse;
import com.apptruyen.entity.Chapter;
import com.apptruyen.entity.ReadingHistory;
import com.apptruyen.entity.Story;
import com.apptruyen.entity.User;
import com.apptruyen.exception.ResourceNotFoundException;
import com.apptruyen.repository.ChapterRepository;
import com.apptruyen.repository.ReadingHistoryRepository;
import com.apptruyen.repository.StoryRepository;
import com.apptruyen.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReadingHistoryService {

    private final ReadingHistoryRepository historyRepository;
    private final UserRepository userRepository;
    private final StoryRepository storyRepository;
    private final ChapterRepository chapterRepository;
    private final DtoMapper mapper;

    /**
     * Danh sách truyện đang đọc, sắp xếp theo thời gian đọc gần nhất.
     * Frontend dùng để hiện màn hình "Xem tiếp".
     */
    @Transactional(readOnly = true)
    public PagedResponse<ContinueReadingDTO> getContinueReading(
            Integer userId, int page, int size) {
        User user = getUser(userId);
        Page<ContinueReadingDTO> dtoPage = historyRepository
                .findAllByUserOrderByUpdatedAtDesc(user, PageRequest.of(page, size))
                .map(mapper::toContinueReadingDTO);
        return mapper.toPagedResponse(dtoPage);
    }

    /**
     * Lưu tiến trình đọc (upsert).
     * Nếu đã đọc truyện này trước đó → cập nhật; nếu chưa → tạo mới.
     */
    @Transactional
    public ContinueReadingDTO saveProgress(Integer userId, SaveProgressRequest request) {
        User user = getUser(userId);
        Story story = getStory(request.getStoryId());
        Chapter chapter = getChapter(request.getLastChapterId());

        // Kiểm tra chapter thuộc story
        if (!chapter.getStory().getId().equals(story.getId())) {
            throw new IllegalArgumentException(
                    "Chapter " + chapter.getId() + " không thuộc story " + story.getId());
        }

        ReadingHistory history = historyRepository
                .findByUserAndStory(user, story)
                .orElse(ReadingHistory.builder().user(user).story(story).build());

        history.setLastChapter(chapter);
        history.setScrollPosition(request.getScrollPosition() != null
                ? request.getScrollPosition() : 0);

        ReadingHistory saved = historyRepository.save(history);
        return mapper.toContinueReadingDTO(saved);
    }

    /**
     * Lấy tiến trình đọc của 1 truyện cụ thể cho user.
     */
    @Transactional(readOnly = true)
    public ContinueReadingDTO getProgressByStory(Integer userId, Integer storyId) {
        User user = getUser(userId);
        Story story = getStory(storyId);
        ReadingHistory history = historyRepository.findByUserAndStory(user, story)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Chưa có lịch sử đọc cho story " + storyId));
        return mapper.toContinueReadingDTO(history);
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    private User getUser(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    private Story getStory(Integer storyId) {
        return storyRepository.findById(storyId)
                .orElseThrow(() -> new ResourceNotFoundException("Story", "id", storyId));
    }

    private Chapter getChapter(Integer chapterId) {
        return chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter", "id", chapterId));
    }
}
