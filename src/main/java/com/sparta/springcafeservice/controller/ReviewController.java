package com.sparta.springcafeservice.controller;

import com.sparta.springcafeservice.dto.ReviewRequestDto;
import com.sparta.springcafeservice.dto.StatusResponseDto;
import com.sparta.springcafeservice.entity.Review;
import com.sparta.springcafeservice.security.UserDetailsImpl;
import com.sparta.springcafeservice.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
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




    // 리뷰 삭제 권한 확인용
    @GetMapping("/reviews/{id}/check-ownership")
    public ResponseEntity<?> checkReviewOwnership(@PathVariable Long id,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Review review = reviewService.getReviewById(id);

        if (review == null) {
            // 해당 리뷰를 찾을 수 없는 경우 에러 응답을 반환
            return ResponseEntity.notFound().build();
        }

        // 리뷰 작성자와 현재 로그인한 사용자의 ID를 비교하여 소유권을 확인
        Long reviewOwnerId = review.getUser().getId();
        Long currentUserId = userDetails.getUser().getId();

        if (reviewOwnerId.equals(currentUserId)) {
            // 소유권이 일치하는 경우, 성공 응답을 반환
            return ResponseEntity.ok().build();
        } else {
            // 소유권이 일치하지 않는 경우, 에러 응답을 반환
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
