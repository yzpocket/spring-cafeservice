package com.sparta.springcafeservice.service;

import com.sparta.springcafeservice.dto.ReviewRequestDto;
import com.sparta.springcafeservice.dto.ReviewResponseDto;
import com.sparta.springcafeservice.dto.StatusResponseDto;
import com.sparta.springcafeservice.entity.Review;
import com.sparta.springcafeservice.entity.Store;
import com.sparta.springcafeservice.entity.User;
import com.sparta.springcafeservice.repository.ReviewRepository;
import com.sparta.springcafeservice.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final StoreRepository storeRepository;

    // 리뷰 작성
    @Transactional
    public ResponseEntity<?> createReview(ReviewRequestDto reviewRequestDto, User user) {
        Review review = new Review(reviewRequestDto, user);

        Store store = findStore(reviewRequestDto.getStoreId());
        review.setStore(store);

        Review savedReview = reviewRepository.save(review);
        store.addReview(review);

        ReviewResponseDto responseDto = new ReviewResponseDto(savedReview);
        return ResponseEntity.ok(responseDto);
    }

/*이부분 StoreService로 가야된다는 것 같음 = 주체가 가게조회에 댓글이 조회되는 것이니 리뷰 서비스에서 처리할 문제가 아닌듯*/

    // 특정 가게 리뷰 조회
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsByStoreId(Long storeId) {
        Store store = findStore(storeId);
        List<Review> reviews = store.getReviews();

        return reviews.stream()
                .map(ReviewResponseDto::new)
                .collect(Collectors.toList());
    }

    // 리뷰 수정
    @Transactional
    public ResponseEntity<?> updateReview(Long id, ReviewRequestDto requestDto, User user) {
        Review review = findReview(id);

        if (!validateUserAuthority(user.getId(), review.getUser())) {
            StatusResponseDto res = new StatusResponseDto("리뷰 작성자만 수정할 수 있습니다.", 400);
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }

        review.update(requestDto);
        ReviewResponseDto responseDto = new ReviewResponseDto(review);
        return ResponseEntity.ok(responseDto);
    }

    // DELETE - 선택 리뷰 삭제
    @Transactional
    public ResponseEntity<StatusResponseDto> deleteReview(Long id, User user) {
        Review review = findReview(id);

        if (!validateUserAuthority(user.getId(), review.getUser())) {
            StatusResponseDto res = new StatusResponseDto("리뷰 작성자만 삭제할 수 있습니다.", 400);
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }

        reviewRepository.delete(review);
        StatusResponseDto res = new StatusResponseDto("리뷰가 삭제되었습니다.", 200);
        return ResponseEntity.ok(res);
    }


    // 가게 체크
    private Store findStore(Long id) {
        return storeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 가게는 존재하지 않습니다.")
        );
    }

    // 리뷰 체크
    private Review findReview(Long id) {
        return reviewRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 리뷰는 존재하지 않습니다.")
        );
    }

    // User 체킹 -> 동일 유저 인지
    private boolean validateUserAuthority(Long reviewUserId, User user) {
        return reviewUserId.equals(user.getId());
    }

}