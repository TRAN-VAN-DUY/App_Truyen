package com.apptruyen.controller;

import com.apptruyen.dto.response.ApiResponse;
import com.apptruyen.dto.response.ChapterContentDTO;
import com.apptruyen.service.ChapterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stories")
@RequiredArgsConstructor
@Tag(name = "Chapters", description = "API đọc nội dung chương")
public class ChapterController {

    private final ChapterService chapterService;

    @GetMapping("/{slug}/chapters/{chapterNumber}")
    @Operation(summary = "Dọc nội dung chương theo slug + số chương",
        description = "Trả về nội dung chương kèm navigation trước/sau và tự động tăng views")
    public ResponseEntity<ApiResponse<ChapterContentDTO>> getChapterContent(
            @PathVariable String slug,
            @PathVariable Integer chapterNumber) {
        return ResponseEntity.ok(
                ApiResponse.ok(chapterService.getChapterContent(slug, chapterNumber))
        );
    }

    @GetMapping("/chapters/{chapterId}")
    @Operation(summary = "Đọc nội dung chương theo ID",
        description = "Trả về nội dung chương kèm navigation trước/sau và tự động tăng views")
    public ResponseEntity<ApiResponse<ChapterContentDTO>> getChapterById(
            @PathVariable Integer chapterId) {
        return ResponseEntity.ok(
                ApiResponse.ok(chapterService.getChapterContentById(chapterId))
        );
    }

}
