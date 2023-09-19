package com.sparta.springcafeservice.controller;

import com.sparta.springcafeservice.dto.ReviewRequestDto;
import com.sparta.springcafeservice.dto.ReviewResponseDto;
import com.sparta.springcafeservice.dto.StatusResponseDto;
import com.sparta.springcafeservice.security.UserDetailsImpl;
import com.sparta.springcafeservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 리뷰 관련 HTTP 요청 처리를 담당하는 컨트롤러
 */
//@Controller
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 작성
    @PostMapping("/reviews")
    public ResponseEntity<?> createReview(@RequestBody ReviewRequestDto requestDto,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return reviewService.createReview(requestDto, userDetails.getUser());
    }

    // 특정 가게의 리뷰 조회
    @GetMapping("/reviews/{store_id}")
    public List<ReviewResponseDto> getReviewsByStoreId(@PathVariable Long store_id) {
        return reviewService.getReviewsByStoreId(store_id);
    }

    // 리뷰 수정
    @PutMapping("/reviews/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Long id, @RequestBody ReviewRequestDto requestDto,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return reviewService.updateReview(id, requestDto, userDetails.getUser());
    }

    // 리뷰 삭제
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<StatusResponseDto> deleteReview(@PathVariable Long id,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return reviewService.deleteReview(id, userDetails.getUser());
    }
}
