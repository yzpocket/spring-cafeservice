package com.sparta.springcafeservice.controller;

import com.sparta.springcafeservice.dto.CommentRequestDto;
import com.sparta.springcafeservice.dto.CommentResponseDto;
import com.sparta.springcafeservice.security.UserDetailsImpl;
import com.sparta.springcafeservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 리뷰 관련 HTTP 요청 처리를 담당하는 컨트롤러
 */
@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    // 리뷰 작성 API
    @PostMapping("/comments")
    public CommentResponseDto createComment(
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    )
    {
        return commentService.createComment(requestDto, userDetails.getUser());
    }

    // 리뷰 모두 조회 API
    @GetMapping("/comments")
    public List<CommentResponseDto> getAllComments(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    )
    {
        return commentService.getAllComments(userDetails.getUser());
    }

    // 리뷰 선택 조회 API
    @GetMapping("/comments/{id}")
    public CommentResponseDto getComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return commentService.getComment(id, userDetails.getUser());
    }

    // 리뷰 수정 API
    @PutMapping("/comments/{id}")
    public CommentResponseDto updateComment(
            @PathVariable Long id,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            //@AuthenticationPrincipal StoreDetailsImpl storeDetails
    ) {
        return commentService.updateComment(id, requestDto, userDetails.getUser());
    }

    // 리뷰 삭제 API by Id
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return commentService.deleteComment(id, userDetails.getUser());
    }
}
