package com.apptruyen.controller;

import com.apptruyen.dto.request.FavoriteRequest;
import com.apptruyen.dto.response.ApiResponse;
import com.apptruyen.dto.response.FavoriteDTO;
import com.apptruyen.dto.response.PagedResponse;
import com.apptruyen.security.JwtUtil;
import com.apptruyen.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
@Tag(name = "Favorites", description = "API truyện yêu thích (yêu cầu JWT)")
@SecurityRequirement(name = "bearerAuth")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final JwtUtil jwtUtil;

    @GetMapping
    @Operation(summary = "Danh sách truyện yêu thích của user")
    public ResponseEntity<ApiResponse<PagedResponse<FavoriteDTO>>> getFavorites(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Integer userId = jwtUtil.getUserIdFromRequest(request);
        return ResponseEntity.ok(
                ApiResponse.ok(favoriteService.getFavorites(userId, page, size))
        );
    }

    @PostMapping
    @Operation(summary = "Thêm truyện vào danh sách yêu thích")
    public ResponseEntity<ApiResponse<FavoriteDTO>> addFavorite(
            HttpServletRequest request,
            @Valid @RequestBody FavoriteRequest body) {
        Integer userId = jwtUtil.getUserIdFromRequest(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Đã thêm vào yêu thích",
                        favoriteService.addFavorite(userId, body.getStoryId())));
    }

    @DeleteMapping("/{storyId}")
    @Operation(summary = "Xoá truyện khỏi danh sách yêu thích")
    public ResponseEntity<ApiResponse<Void>> removeFavorite(
            HttpServletRequest request,
            @PathVariable Integer storyId) {
        Integer userId = jwtUtil.getUserIdFromRequest(request);
        favoriteService.removeFavorite(userId, storyId);
        return ResponseEntity.ok(ApiResponse.ok("Đã xoá khỏi yêu thích", null));
    }

    @GetMapping("/{storyId}/check")
    @Operation(summary = "Kiểm tra xem user đã yêu thích truyện này chưa")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> checkFavorite(
            HttpServletRequest request,
            @PathVariable Integer storyId) {
        Integer userId = jwtUtil.getUserIdFromRequest(request);
        boolean isFav = favoriteService.isFavorited(userId, storyId);
        return ResponseEntity.ok(
                ApiResponse.ok(Map.of("favorited", isFav))
        );
    }
}
