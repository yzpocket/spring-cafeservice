package com.sparta.springcafeservice.controller;

import com.sparta.springcafeservice.dto.CommentRequestDto;
import com.sparta.springcafeservice.dto.CommentResponseDto;
import com.sparta.springcafeservice.entity.User;
import com.sparta.springcafeservice.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    // 리뷰 작성 by userId, storeID API
    @PostMapping("/comments")
    public CommentResponseDto createComment(@RequestBody CommentRequestDto commentRequestDto, @RequestParam("storeId") Long storeId, HttpServletRequest req) {

        User user = (User) req.getAttribute("userId");

        return commentService.createComment(commentRequestDto, user.getUserId(), storeId);
    }


    // 리뷰 수정 (refactor : API response Type change)
    @PutMapping("/comments/{id}")
    public CommentResponseDto updateItem(@PathVariable Long id, @RequestBody CommentRequestDto requestDto,  HttpServletRequest req) {
        // 현재 사용자의 정보를 HttpServletRequest에서 추출
        User user = (User) req.getAttribute("userId");
        // CommentService를 사용하여 리뷰 작성
        CommentResponseDto updatedComment = commentService.updateComment(id, requestDto, user.getUserId());
        // HTTP 201 Created 상태 코드와 작성된 리뷰를 반환
        return new CommentResponseDto(updatedComment);
    }

    // 리뷰 조회
    @GetMapping("/comments")
    public List<CommentResponseDto> getComment(){
        return commentService.getComment();
    }

    // 리뷰 삭제 by Id API // 리뷰 수정, 삭제는 궂이 storeId가 필요하지 않을것 같음 리뷰 작성자만 확인하도록.
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id, HttpServletRequest req) {
        User user = (User) req.getAttribute("user");
        return commentService.deleteComment(id, user.getUserId());
    }
}