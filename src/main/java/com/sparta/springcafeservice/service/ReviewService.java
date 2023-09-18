package com.sparta.springcafeservice.service;

import com.sparta.springcafeservice.dto.ReviewRequestDto;
import com.sparta.springcafeservice.dto.ReviewResponseDto;
import com.sparta.springcafeservice.entity.Review;
import com.sparta.springcafeservice.entity.Store;
import com.sparta.springcafeservice.entity.User;
import com.sparta.springcafeservice.exception.RestApiException;
import com.sparta.springcafeservice.repository.ReviewRepository;
import com.sparta.springcafeservice.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;

    // CREATE - 리뷰 작성
    public ReviewResponseDto createReview(ReviewRequestDto reviewRequestDto, User user) {
        // 가게 존재 여부 확인
        Store store = findStore(reviewRequestDto.getStoreId());

        // Entity로 <- RequestDto를
        Review review = new Review(reviewRequestDto, user, store);

        // DB 저장
        Review savedReview = reviewRepository.save(review);

        // 저장된 리뷰를 가게의 리뷰 리스트에 추가
        store.addReviewList(review);
        System.out.println("리뷰가 등록되었습니다.");

        return new ReviewResponseDto(savedReview);
    }

    // READ ALL by storeId - 특정 가게의 리뷰 전체 조회
    public List<ReviewResponseDto> getReviewsByStoreId(Long storeId) {
        // 가게 존재 여부 확인
        Store store = findStore(storeId);

        // 특정 가게 ID에 해당하는 리뷰 목록 조회
        List<Review> reviews = reviewRepository.findByStoreId(storeId);

        // 조회된 리뷰 목록을 ReviewResponseDto로 매핑하여 리스트로 반환
        return reviews.stream()
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList());
    }

    // UPDATE - 선택 리뷰 수정
    @Transactional
    public ReviewResponseDto updateReview(Long id, ReviewRequestDto requestDto, User user) {
        // 특정 리뷰 DB 존재 여부 확인
        Review review = findReview(id);

        // 리뷰의 작성자와 현재 로그인한 사용자를 비교하여 작성자가 같지 않으면 예외 발생
        if (!review.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("리뷰 작성자만 수정할 수 있습니다.");
        }

        // 리뷰을 업데이트
        review.update(requestDto);
        System.out.println("리뷰번호"+id+"번 리뷰 수정 완료.");

        // 업데이트된 리뷰을 반환
        return new ReviewResponseDto(review);
    }

    // DELETE - 선택 리뷰 삭제
    @Transactional
    public ResponseEntity<String> deleteReview(Long id, User user) {
        // 특정 리뷰 DB 존재 여부 확인
        Review review = findReview(id);

        // DB기록된 리뷰의 작성자와 현재 로그인한 사용자를 비교하여 작성자가 같지 않으면 예외 발생
        if (!review.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("리뷰 작성자만 삭제할 수 있습니다.");
        }

        // 리뷰를 삭제
        reviewRepository.delete(review);
        System.out.println("리뷰번호"+id+"번 리뷰 삭제 완료.");

        // 삭제가 성공한 응답을 반환
        return ResponseEntity.ok("리뷰가 삭제되었습니다.");
    }


    // find1 - 리뷰 ID로 존재 여부 확인
    private Store findStore(Long id) {
        // 가게 ID를 사용하여 특정 가게을 조회하고, 존재하지 않을 경우 예외 발생
        return storeRepository.findById(id).orElseThrow(() ->
                new RestApiException("선택한 가게는 존재하지 않습니다.", HttpStatus.NOT_FOUND.value())
        );
    }

    // find2 - 리뷰 ID로 존재 여부 확인
    private Review findReview(Long id) {
        // 리뷰 ID를 사용하여 특정 리뷰를 조회하고, 존재하지 않을 경우 예외 발생
        return reviewRepository.findById(id).orElseThrow(() ->
                new RestApiException("선택한 리뷰는 존재하지 않습니다.", HttpStatus.NOT_FOUND.value())
        );
    }


}