package com.sparta.springcafeservice.controller;

import com.sparta.springcafeservice.dto.CommentRequestDto;
import com.sparta.springcafeservice.dto.CommentResponseDto;
import com.sparta.springcafeservice.entity.User;
import com.sparta.springcafeservice.exception.RestApiException;
import com.sparta.springcafeservice.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 리뷰 관련 HTTP 요청 처리를 담당하는 컨트롤러
 */
@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    // 리뷰 작성 API - by userId, storeID
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto commentRequestDto, @RequestParam("storeId") Long storeId, HttpServletRequest req) {
        try {
            // 현재 사용자의 정보를 HttpServletRequest에서 추출
            User user = (User) req.getAttribute("userId");
            // CommentService를 사용하여 리뷰 작성
            CommentResponseDto createdComment = commentService.createComment(commentRequestDto, user.getUserId());
            // HTTP 201 Created 상태 코드와 작성된 리뷰를 반환
            return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
        } catch (RestApiException ex) {
            // 예외 발생 시 예외 처리
            return ResponseEntity.status(ex.getStatusCode()).body(new CommentResponseDto(ex.getMessage(), ex.getStatusCode()));
        }
    }

    // 리뷰 조회 API
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getComments() {
        try {
            // CommentService를 사용하여 리뷰 목록 조회
            List<CommentResponseDto> comments = commentService.getComments();

            // 리뷰 목록이 비어있을 경우 예외 발생
            if (comments.isEmpty()) {
                throw new RestApiException("리뷰가 없습니다.", HttpStatus.NOT_FOUND.value());
            }

            // HTTP 200 OK 상태 코드와 리뷰 목록을 반환
            return ResponseEntity.ok(comments);
        } catch (RestApiException ex) {
            // 예외 처리 메소드에서 예외를 캐치하고 클라이언트에 응답 반환
            return ResponseEntity.status(ex.getStatusCode()).body(new CommentResponseDto(ex.getMessage(), ex.getStatusCode()));
        }
    }

    // 리뷰 수정 API
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto commentRequestDto, HttpServletRequest req) {
        try {
            // 현재 사용자의 정보를 HttpServletRequest에서 추출
            User user = (User) req.getAttribute("userId");

            // CommentService를 사용하여 리뷰 수정
            CommentResponseDto updatedComment = commentService.updateComment(id, commentRequestDto, user.getUserId());

            // HTTP 200 OK 상태 코드와 업데이트된 리뷰를 반환
            return ResponseEntity.ok(updatedComment);
        } catch (RestApiException ex) {
            // 예외 처리 메소드에서 예외를 캐치하고 클라이언트에 응답 반환
            return ResponseEntity.status(ex.getStatusCode()).body(new CommentResponseDto(ex.getMessage(), ex.getStatusCode()));
        }
    }

    // 리뷰 삭제 API by Id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id, HttpServletRequest req) {
        try {
            // 현재 사용자의 정보를 HttpServletRequest에서 추출
            User user = (User) req.getAttribute("userId");
            // CommentService를 사용하여 리뷰 삭제
            commentService.deleteComment(id, user.getUserId());
            // HTTP 204 No Content 상태코드 반환
            return ResponseEntity.noContent().build();
        } catch (RestApiException ex) {
            // 예외 발생 시 예외 처리
            return ResponseEntity.status(ex.getStatusCode()).body(new CommentResponseDto(ex.getMessage(), ex.getStatusCode()));
        }
    }
}