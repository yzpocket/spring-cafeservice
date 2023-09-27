package com.sparta.springcafeservice.service;

import com.sparta.springcafeservice.dto.*;
import com.sparta.springcafeservice.entity.*;
import com.sparta.springcafeservice.repository.ReviewRepository;
import com.sparta.springcafeservice.repository.StoreRepository;
import com.sparta.springcafeservice.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    ReviewService reviewService;
    @InjectMocks
    StoreService storeService;
    @Nested
    @DisplayName("CreateReview")
    class Create {
        @Test
        @DisplayName("리뷰 작성 성공")
        public void testCreateReview_Success() {
            // given
            ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
            User user = new User("testUser", "password", "test@test.com", UserRoleEnum.USER);

            // 가짜 Store 생성 및 설정
            Store store = mock(Store.class);
            Long storeId = 1L; // 유효한 가게 ID
            when(storeRepository.findById(reviewRequestDto.getStoreId())).thenReturn(Optional.of(store));

            // 가짜 Review 생성 및 설정
            Review review = new Review();
            when(reviewRepository.save(any(Review.class))).thenReturn(review);

            // when
            StatusResponseDto response = reviewService.createReview(reviewRequestDto, user);

            // then
            assertEquals("리뷰를 등록했습니다.", response.getMsg());
            assertEquals(200, response.getStatuscode());
        }

        @Test
        @DisplayName("리뷰 작성 실패 - 가게 없는 경우")
        public void testCreateReview_Failure() {
            // given
            ReviewRequestDto reviewRequestDto = new ReviewRequestDto();
            when(storeRepository.findById(reviewRequestDto.getStoreId())).thenReturn(Optional.empty());

            // when, then
            // 가게를 찾을 수 없는 경우, IllegalArgumentException
            assertThrows(IllegalArgumentException.class, () -> reviewService.createReview(reviewRequestDto, mock(User.class)));
        }

        @Test
        @DisplayName("로그인 필요")
        public void testCreateReview_LoginRequired_Failure() {
            User user = new User("testUser", "password", "test@test.com", UserRoleEnum.USER);
            Store store = mock(Store.class);
            Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> reviewService.createReview(new ReviewRequestDto(), null));
            assertEquals("로그인 해주세요!", exception.getMessage());
        }

        @Test
        @DisplayName("리뷰 작성자 체크 실패")
        public void testCreateReview_NotOwnerOfTheReview_Failure() {
            // given
            Long reviewId = 1L;
            ReviewRequestDto reviewRequestDto = new ReviewRequestDto();

            // User 객체를 모킹하고 사용자 ID를 설정
            User user1 = mock(User.class);
            when(user1.getId()).thenReturn(1L); //set
            User user2 = mock(User.class);
            when(user2.getId()).thenReturn(2L);

            // 가게와 메뉴 생성
            Store store = new Store(new StoreRequestDto(/* 스토어 정보 */), user1, new StoreAddress(/* 주소 정보 */));


            Review review = new Review(reviewRequestDto, user1, store);

            // storeRepository.findById()가 해당 가게를 반환하도록 설정
            when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

            // 사용자 ID가 다른 경우, 예외가 발생해야 합니다.
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> reviewService.updateReview(reviewId, reviewRequestDto, user2));
            assertEquals("리뷰 작성자만 접근할 수 있습니다.", exception.getMessage());
        }
    }
    @Nested
    @DisplayName("UpdateReview")
    class Update{
        @Test
        @DisplayName("리뷰 수정 성공")
        public void testUpdateReview_Success() throws NoSuchFieldException, IllegalAccessException {
            // 사용자 생성
            User user = new User("testUser", "password", "test@test.com", UserRoleEnum.USER);
            Field idFieldUser = User.class.getDeclaredField("id");
            idFieldUser.setAccessible(true);
            idFieldUser.set(user , 1L);


            // 다른 사용자 생성
            User user2 = new User("testUser", "password", "test@test.com", UserRoleEnum.USER);
            Field idFieldUser2 = User.class.getDeclaredField("id");
            idFieldUser2.setAccessible(true);
            idFieldUser2.set(user , 2L);


            // 가게와 메뉴 생성
            Store store = new Store(new StoreRequestDto(/* 스토어 정보 */), user, new StoreAddress(/* 주소 정보 */));

            Menu menu = new Menu(new MenuRequestDto(/* 메뉴 정보*/), store);

            // 리뷰 생성
            ReviewRequestDto existingReviewData = new ReviewRequestDto(/* 기존 리뷰 데이터 */);
            Review review = new Review(existingReviewData, user, store);
            Long reviewId = 1L;

            when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review)); // 기존 리뷰 찾는 로직

            // 수정된 매개변수 삽입
            ReviewRequestDto updateData = new ReviewRequestDto(/* 수정할 데이터 */);

            assertDoesNotThrow(() -> {
                StatusResponseDto response = reviewService.updateReview(1L, updateData, user);
                assertEquals("리뷰가 수정되었습니다.", response.getMsg());
            });
        }

        @Test
        @DisplayName("리뷰 수정 실패 - 특정 id 리뷰 없음")
        public void testUpdateReview_Failure(){
            User notOwnerUser=new User();

            Long wrongId=-1L;

            Exception exception = assertThrows(IllegalArgumentException.class,
                    () ->  reviewService.updateReview(wrongId,new ReviewRequestDto(),notOwnerUser));

            assertEquals("해당 리뷰는 존재하지 않습니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("DeleteReview")
    class Delete {

        @Test
        @DisplayName("리뷰 삭제 성공")
        public void testDeleteReview_Success() throws NoSuchFieldException, IllegalAccessException {
            // 사용자 생성
            User user = new User("testUser", "password", "test@test.com", UserRoleEnum.USER);
            Field idFieldUser = User.class.getDeclaredField("id");
            idFieldUser.setAccessible(true);
            idFieldUser.set(user, 1L);

            // 가게와 메뉴 생성
            Store store = new Store(new StoreRequestDto(/* 스토어 정보 */), user, new StoreAddress(/* 주소 정보 */));

            Menu menu = new Menu(new MenuRequestDto(/* 메뉴 정보*/), store);

            // 리뷰 생성
            ReviewRequestDto existingReviewData = new ReviewRequestDto(/* 기존 리뷰 데이터 */);
            Review review = new Review(existingReviewData, user, store);
            Long reviewId = 1L;

            when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review)); // 기존 리뷰 찾는 로직

            assertDoesNotThrow(() -> {
                StatusResponseDto response = reviewService.deleteReview(1L, user);
                assertEquals("리뷰가 삭제되었습니다.", response.getMsg());
            });
        }

        @Test
        @DisplayName("리뷰 삭제 실패 - 특정 id 리뷰 없음")
        public void testDeleteReview_Failure() {
            User notOwnerUser = new User();

            Long wrongId = -1L;

            Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> reviewService.deleteReview(wrongId, notOwnerUser));

            assertEquals("해당 리뷰는 존재하지 않습니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("CreateReview")
    class Read {
        @Test
        @DisplayName("리뷰 조회 성공")
        public void testReadReview_Success() {
            // given
            Long storeId = 1L; // 가게 ID
            List<Review> expectedReviews = new ArrayList<>();
            // 가짜 리뷰 데이터를 생성하고 예상 결과에 추가
            expectedReviews.add(new Review());
            expectedReviews.add(new Review());

            // Mock 객체 설정: reviewRepository.findAllByStoreId 메서드에 대한 Mock 설정
            when(reviewRepository.findAllByStoreId(storeId)).thenReturn(expectedReviews);

            // when
            List<Review> actualReviews = storeService.getReviewsByStoreId(storeId);

            // then
            assertEquals(expectedReviews.size(), actualReviews.size());
            // 만약 리뷰 목록의 크기가 같다면 모든 리뷰 객체가 같은지 확인할 수도 있습니다.
            // assertEquals(expectedReviews, actualReviews);
            // 이 경우, Review 클래스에 equals 및 hashCode 메서드가 적절히 구현되어야 합니다.
        }
        }
    }