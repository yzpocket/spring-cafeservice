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
    public ResponseEntity<String> updateComment(
            @PathVariable Long id,
            @RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            //@AuthenticationPrincipal StoreDetailsImpl storeDetails
            ) {
        return commentService.updateComment(id, requestDto, userDetails.getUser()); //, storeDetails.getStore()

    }

    // 리뷰 삭제 API by Id
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long id,
            //@RequestBody CommentRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            //@AuthenticationPrincipal StoreDetailsImpl storeDetails
    ) {
        return commentService.deleteComment(id, userDetails.getUser());
    }
}
//보여주신 코드에서 @DeleteMapping("/comment/{id}") 엔드포인트는 리뷰를 삭제하는 API 엔드포인트로 보입니다. 그러나 @DeleteMapping을 사용하여 HTTP DELETE 요청을 처리하려면 일반적으로 @RequestBody를 사용하지 않습니다.