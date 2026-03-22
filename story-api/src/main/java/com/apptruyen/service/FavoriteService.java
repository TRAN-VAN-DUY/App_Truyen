package com.apptruyen.service;

import com.apptruyen.dto.response.FavoriteDTO;
import com.apptruyen.dto.response.PagedResponse;
import com.apptruyen.entity.Favorite;
import com.apptruyen.entity.Story;
import com.apptruyen.entity.User;
import com.apptruyen.exception.DuplicateResourceException;
import com.apptruyen.exception.ResourceNotFoundException;
import com.apptruyen.repository.FavoriteRepository;
import com.apptruyen.repository.StoryRepository;
import com.apptruyen.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final StoryRepository storyRepository;
    private final DtoMapper mapper;

    /** Danh sách yêu thích của user, phân trang */
    @Transactional(readOnly = true)
    public PagedResponse<FavoriteDTO> getFavorites(Integer userId, int page, int size) {
        User user = getUser(userId);
        Page<FavoriteDTO> dtoPage = favoriteRepository
                .findAllByUserOrderByCreatedAtDesc(user, PageRequest.of(page, size))
                .map(mapper::toFavoriteDTO);
        return mapper.toPagedResponse(dtoPage);
    }

    /** Thêm vào yêu thích */
    @Transactional
    public FavoriteDTO addFavorite(Integer userId, Integer storyId) {
        User user = getUser(userId);
        Story story = getStory(storyId);

        if (favoriteRepository.existsByUserAndStory(user, story)) {
            throw new DuplicateResourceException("Story đã có trong danh sách yêu thích");
        }

        Favorite saved = favoriteRepository.save(
                Favorite.builder().user(user).story(story).build()
        );
        return mapper.toFavoriteDTO(saved);
    }

    /** Xoá khỏi yêu thích */
    @Transactional
    public void removeFavorite(Integer userId, Integer storyId) {
        User user = getUser(userId);
        Story story = getStory(storyId);

        if (!favoriteRepository.existsByUserAndStory(user, story)) {
            throw new ResourceNotFoundException("Favorite not found for story " + storyId);
        }
        favoriteRepository.deleteByUserAndStory(user, story);
    }

    /** Kiểm tra đã yêu thích chưa */
    @Transactional(readOnly = true)
    public boolean isFavorited(Integer userId, Integer storyId) {
        User user = getUser(userId);
        Story story = getStory(storyId);
        return favoriteRepository.existsByUserAndStory(user, story);
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
}
