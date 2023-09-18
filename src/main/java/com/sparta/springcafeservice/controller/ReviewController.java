package com.sparta.springcafeservice.controller;

import com.sparta.springcafeservice.dto.ReviewRequestDto;
import com.sparta.springcafeservice.dto.ReviewResponseDto;
import com.sparta.springcafeservice.security.UserDetailsImpl;
import com.sparta.springcafeservice.service.ReviewService;
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
//@Controller
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 작성 API
    @PostMapping("/reviews")
    public ReviewResponseDto createReview(
            @RequestBody ReviewRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    )
    {
        return reviewService.createReview(requestDto, userDetails.getUser());
    }

    //// 리뷰 모두 조회 API
    //@GetMapping("/reviews")
    //public List<ReviewResponseDto> getAllReviews(
    //        @AuthenticationPrincipal UserDetailsImpl userDetails
    //)
    //{
    //    return reviewService.getAllReviews(userDetails.getUser());
    //}

    //// 리뷰 선택 조회 API
    //@GetMapping("/reviews/{id}")
    //public ReviewResponseDto getReview(
    //        @PathVariable Long id,
    //        @AuthenticationPrincipal UserDetailsImpl userDetails
    //) {
    //    return reviewService.getReview(id, userDetails.getUser());
    //}

    // 특정 가게 ID에 해당하는 리뷰 조회 API
    @GetMapping("/reviews/{store_id}")
    public List<ReviewResponseDto> getReviewsByStoreId(
            @PathVariable Long store_id
    ) {
        return reviewService.getReviewsByStoreId(store_id);
    }

    // 리뷰 수정 API
    @PutMapping("/reviews/{id}")
    public ReviewResponseDto updateReview(
            @PathVariable Long id,
            @RequestBody ReviewRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            //@AuthenticationPrincipal StoreDetailsImpl storeDetails
    ) {
        return reviewService.updateReview(id, requestDto, userDetails.getUser());
    }

    // 리뷰 삭제 API by Id
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<String> deleteReview(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return reviewService.deleteReview(id, userDetails.getUser());
    }
}
