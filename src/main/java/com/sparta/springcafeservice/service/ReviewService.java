package com.sparta.springcafeservice.service;

import com.sparta.springcafeservice.aop.ServiceExceptionHandlerAspect;
import com.sparta.springcafeservice.controller.ReviewController;
import com.sparta.springcafeservice.dto.ReviewRequestDto;
import com.sparta.springcafeservice.dto.StatusResponseDto;
import com.sparta.springcafeservice.entity.Review;
import com.sparta.springcafeservice.entity.Store;
import com.sparta.springcafeservice.entity.User;
import com.sparta.springcafeservice.repository.ReviewRepository;
import com.sparta.springcafeservice.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableAspectJAutoProxy
@RequiredArgsConstructor
public class ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    private final ReviewRepository reviewRepository;

    private final StoreRepository storeRepository;

    private final ServiceExceptionHandlerAspect serviceExceptionHandlerAspect;

    // 리뷰 작성
    @Transactional
    public ResponseEntity<StatusResponseDto> createReview(ReviewRequestDto reviewRequestDto, User user) {
        StatusResponseDto statusResponse = handleServiceRequest(() -> {
            Store store = checkStoreExist(reviewRequestDto.getStoreId());
            Review review = new Review(reviewRequestDto, user, store);
            reviewRepository.save(review);
            return new StatusResponseDto("리뷰를 등록했습니다.", 200);
        });
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    // 리뷰 수정
    @Transactional
    public ResponseEntity<StatusResponseDto> updateReview(Long id, ReviewRequestDto requestDto, User user) {
        return handleServiceRequest(() -> {

            Review review = checkReviewExist(id);
            validateUserAuthority(user.getId(), review.getUser());
            review.update(requestDto);
            return new StatusResponseDto("리뷰가 수정되었습니다.", 200);
        });
    }

    // 선택 리뷰 삭제
    @Transactional
    public ResponseEntity<StatusResponseDto> deleteReview(Long id, User user) {
        return handleServiceRequest(() -> {

            Review review = checkReviewExist(id);
            validateUserAuthority(user.getId(), review.getUser());
            reviewRepository.delete(review);
            return new StatusResponseDto("리뷰가 삭제되었습니다.", 200);
        });
    }


    // 가게 체크
    private Store checkStoreExist(Long id) {
        return storeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 가게는 존재하지 않습니다.")
        );
    }

    // 리뷰 체크
    private Review checkReviewExist(Long id) {
        return reviewRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 리뷰는 존재하지 않습니다.")
        );
    }

    // 동일 유저 체크
    private void validateUserAuthority(Long reviewUserId, User user) {
        if (!reviewUserId.equals(user.getId())) {
            throw new IllegalArgumentException("리뷰 작성자만 접근할 수 있습니다.");
        }
    }

    // 프론트 삭제 권한 확인용 메소드
    public Review getReviewById(Long id) {
        return reviewRepository.findById(id).orElse(null);
    }
}