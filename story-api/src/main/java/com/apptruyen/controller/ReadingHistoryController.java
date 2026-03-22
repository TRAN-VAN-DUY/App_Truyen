package com.apptruyen.controller;

import com.apptruyen.dto.request.SaveProgressRequest;
import com.apptruyen.dto.response.ApiResponse;
import com.apptruyen.dto.response.ContinueReadingDTO;
import com.apptruyen.dto.response.PagedResponse;
import com.apptruyen.security.JwtUtil;
import com.apptruyen.service.ReadingHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reading-history")
@RequiredArgsConstructor
@Tag(name = "Reading History", description = "API lịch sử đọc / Xem tiếp (yêu cầu JWT)")
@SecurityRequirement(name = "bearerAuth")
public class ReadingHistoryController {

    private final ReadingHistoryService historyService;
    private final JwtUtil jwtUtil;

    @GetMapping
    @Operation(summary = "Danh sách truyện đang đọc (Xem tiếp)",
        description = "Trả về danh sách truyện user đang đọc dở, sắp xếp theo lần đọc gần nhất")
    public ResponseEntity<ApiResponse<PagedResponse<ContinueReadingDTO>>> getContinueReading(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Integer userId = jwtUtil.getUserIdFromRequest(request);
        return ResponseEntity.ok(
                ApiResponse.ok(historyService.getContinueReading(userId, page, size))
        );
    }

    @PostMapping
    @Operation(summary = "Lưu tiến trình đọc (upsert)",
        description = "Tạo mới hoặc cập nhật chapter đang đọc và scroll position cho truyện")
    public ResponseEntity<ApiResponse<ContinueReadingDTO>> saveProgress(
            HttpServletRequest request,
            @Valid @RequestBody SaveProgressRequest body) {
        Integer userId = jwtUtil.getUserIdFromRequest(request);
        return ResponseEntity.ok(
                ApiResponse.ok("Đã lưu tiến trình", historyService.saveProgress(userId, body))
        );
    }

    @GetMapping("/{storyId}")
    @Operation(summary = "Lấy tiến trình đọc của 1 truyện cụ thể")
    public ResponseEntity<ApiResponse<ContinueReadingDTO>> getProgressByStory(
            HttpServletRequest request,
            @PathVariable Integer storyId) {
        Integer userId = jwtUtil.getUserIdFromRequest(request);
        return ResponseEntity.ok(
                ApiResponse.ok(historyService.getProgressByStory(userId, storyId))
        );
    }
}
