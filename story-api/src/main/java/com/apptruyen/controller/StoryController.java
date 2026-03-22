package com.apptruyen.controller;

import com.apptruyen.dto.response.*;
import com.apptruyen.service.StoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stories")
@RequiredArgsConstructor
@Tag(name = "Stories", description = "API đọc truyện")
public class StoryController {

    private final StoryService storyService;

    @GetMapping
    @Operation(summary = "Danh sách truyện",
        description = "Lấy danh sách truyện có phân trang, filter theo thể loại, trạng thái, sắp xếp")
    public ResponseEntity<ApiResponse<PagedResponse<StorySummaryDTO>>> getStories(
            @Parameter(description = "Số trang (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số item mỗi trang") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Slug thể loại, ví dụ: action") @RequestParam(required = false) String genre,
            @Parameter(description = "Trạng thái: ongoing | completed | hiatus") @RequestParam(required = false) String status,
            @Parameter(description = "Sắp xếp: updated (default) | views | rating | newest") @RequestParam(defaultValue = "updated") String sort
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok(storyService.getStories(page, size, genre, status, sort))
        );
    }

    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm truyện theo tên hoặc tác giả")
    public ResponseEntity<ApiResponse<PagedResponse<StorySummaryDTO>>> searchStories(
            @Parameter(description = "Từ khoá tìm kiếm", required = true) @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok(storyService.searchStories(keyword, page, size))
        );
    }

    @GetMapping("/{slug}")
    @Operation(summary = "Chi tiết truyện theo slug")
    public ResponseEntity<ApiResponse<StoryDetailDTO>> getStoryDetail(
            @PathVariable String slug) {
        return ResponseEntity.ok(
                ApiResponse.ok(storyService.getStoryBySlug(slug))
        );
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Chi tiết truyện theo ID")
    public ResponseEntity<ApiResponse<StoryDetailDTO>> getStoryById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(
                ApiResponse.ok(storyService.getStoryById(id))
        );
    }

    @GetMapping("/{slug}/chapters")
    @Operation(summary = "Danh sách chương theo slug")
    public ResponseEntity<ApiResponse<List<ChapterDTO>>> getChaptersBySlug(
            @PathVariable String slug) {
        return ResponseEntity.ok(
                ApiResponse.ok(storyService.getChaptersByStorySlug(slug))
        );
    }

    @GetMapping("/id/{id}/chapters")
    @Operation(summary = "Danh sách chương theo story ID")
    public ResponseEntity<ApiResponse<List<ChapterDTO>>> getChaptersByStoryId(
            @PathVariable Integer id) {
        return ResponseEntity.ok(
                ApiResponse.ok(storyService.getChaptersByStoryId(id))
        );
    }
}
