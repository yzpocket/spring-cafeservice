package com.sparta.springcafeservice.service;

import com.sparta.springcafeservice.dto.StatusResponseDto;
import com.sparta.springcafeservice.dto.StoreAllResponseDto;
import com.sparta.springcafeservice.dto.StoreRequestDto;
import com.sparta.springcafeservice.dto.StoreResponseDto;
import com.sparta.springcafeservice.entity.Store;
import com.sparta.springcafeservice.entity.StoreAddress;
import com.sparta.springcafeservice.entity.User;
import com.sparta.springcafeservice.repository.StoreAddressRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;
    @Mock
    private StoreAddressRepository storeAddressRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    StoreService storeService;

    @Nested
    @DisplayName("CreateStore")
    class Create {
        @Test
        @DisplayName("가게 생성 성공")
        void createStore() {
            // given
            StoreRequestDto storeRequestDto = new StoreRequestDto(); // 가게 생성에 필요한 데이터를 설정
            when(storeRepository.save(any(Store.class))).thenReturn(new Store());

            User user = mock(User.class);
            // when
            StatusResponseDto response = storeService.createStore(storeRequestDto, user);
            // then
            assertEquals(HttpStatus.OK.value(), response.getStatuscode());
        }

        @Test
        @DisplayName("가게 생성 실패- 중복된 가게 이름")
        void failCreateStoreDuplicateName() {
            // given
            StoreRequestDto storeRequestDto = new StoreRequestDto(); // 가게 생성에 필요한 데이터를 설정
            when(storeRepository.existsByStoreName(storeRequestDto.getStoreName())).thenReturn(true); // 중복된 가게 이름이 존재한다 (true)

            User user = mock(User.class);

            // when, then
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> storeService.createStore(storeRequestDto, user));
            assertEquals("중복된 가게이름이 존재합니다.", illegalArgumentException.getMessage());
        }

        @Test
        @DisplayName("가게 생성 실패- 이미 사용자가 가게를 등록")
        void failCreateStoreAlreadyCreated() {
            // given
            StoreRequestDto storeRequestDto = new StoreRequestDto(); // 가게 생성에 필요한 데이터를 설정

            User user = mock(User.class);
            when(storeRepository.existsById(user.getId())).thenReturn(true); // 이미 가게를 등록했다고 가정

            // when
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> storeService.createStore(storeRequestDto, user));
            // then
            assertEquals("이미 가게를 등록하였습니다.", illegalArgumentException.getMessage());
        }

        @Test
        @DisplayName("가게 생성 실패- 중복된 사업자 번호")
        void failCreateStoreDuplicateBusinessNum() {
            // given
            StoreRequestDto storeRequestDto = new StoreRequestDto(); // 가게 생성에 필요한 데이터를 설정
            when(storeRepository.findByBusinessNum(storeRequestDto.getBusinessNum())).thenReturn(Optional.of(new Store())); // 중복된 가게 이름이 존재한다 (true)

            User user = mock(User.class);

            // when, then
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> storeService.createStore(storeRequestDto, user));
            assertEquals("중복된 사업자 번호입니다.", illegalArgumentException.getMessage());
        }


    }

    @Nested
    @DisplayName("getStore")
    class Read {
        @Test
        @DisplayName("가게 전체 불러오기")
        void getAllStores() {
            // given
            StoreRequestDto storeRequestDto = new StoreRequestDto(); // 가게 생성에 필요한 데이터를 설정
            User user = mock(User.class);

            List<Store> storeList = Arrays.asList(
                    new Store(storeRequestDto, user, new StoreAddress()),
                    new Store(storeRequestDto, user, new StoreAddress()),
                    new Store(storeRequestDto, user, new StoreAddress())
            );
            when(storeRepository.findAll()).thenReturn(storeList);

            // when
            List<StoreAllResponseDto> responseDtos = storeService.getAllStores();

            // then
            assertEquals(storeList.size(), responseDtos.size()); // 반환하는 사이즈를 확인한다.
        }

        @Test
        @DisplayName("가게 선택 조회")
            // null 값인 경우 throw exception 없음
        void getStore() {
            // given
            Long storeId = 1L;
            StoreRequestDto storeRequestDto = new StoreRequestDto(); // 가게 생성에 필요한 데이터를 설정
            User user = mock(User.class);

            Store store = new Store(storeRequestDto, user, new StoreAddress());

            when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

            // when
            StoreResponseDto responseDto = storeService.getStore(storeId);

            // then
            assertEquals(store.getStoreName(), responseDto.getStoreName());
        }
    }

    @Nested
    @DisplayName("updateStore")
    class Update {

        @Test
        @DisplayName("가게 정보 수정 성공")
        void updateStore() {
            // given
            Long storeId = 1L;
            StoreRequestDto storeRequestDto = new StoreRequestDto();
            User user = mock(User.class);
            Store store = new Store(storeRequestDto, user, new StoreAddress());

            when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
            when(passwordEncoder.matches(storeRequestDto.getPassword(), user.getPassword())).thenReturn(true);
            when(user.getId()).thenReturn(1L);
            when(store.getUser().getId()).thenReturn(1L);

            // when
            StatusResponseDto response = storeService.updateStore(storeId, storeRequestDto, user);

            // then
            assertEquals(200, response.getStatuscode());
            assertEquals("가게 정보가 수정되었습니다.", response.getMsg());
        }

        @Test
        @DisplayName("가게 수정 실패 - Password 다름")
        void failUpdateStoreIncorrectPassword() {
            // given
            Long storeId = 1L;
            StoreRequestDto storeRequestDto = new StoreRequestDto();
            User user = mock(User.class);
            Store store = new Store(storeRequestDto, user, new StoreAddress());

            when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

            // 사용자 비밀번호 다르게
            when(passwordEncoder.matches(storeRequestDto.getPassword(), store.getUser().getPassword())).thenReturn(false);

            // when, then
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> storeService.updateStore(storeId, storeRequestDto, user));
            assertEquals("비밀번호가 다릅니다", illegalArgumentException.getMessage());
        }

        @Test
        @DisplayName("가게 수정 실패 - 사용자 ID 다름")
        void failUpdateStoreUserId() {
            // given
            Long storeId = 1L;
            StoreRequestDto storeRequestDto = new StoreRequestDto();

            // User 객체를 모킹하고 사용자 ID를 설정
            User user1 = mock(User.class);
            when(user1.getId()).thenReturn(1L);
            User user2 = mock(User.class);
            when(user2.getId()).thenReturn(2L);

            Store store = new Store(storeRequestDto, user1, new StoreAddress());

            // storeRepository.findById()가 해당 가게를 반환하도록 설정
            when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
            when(passwordEncoder.matches(storeRequestDto.getPassword(), store.getUser().getPassword())).thenReturn(true);

            // 사용자 ID가 다른 경우, 예외가 발생해야 합니다.
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> storeService.updateStore(storeId, storeRequestDto, user2));
            assertEquals("사용자가 다릅니다", exception.getMessage());
        }


    }


    @Nested
    @DisplayName("deleteStore")
    class Delete {
        @Test
        @DisplayName("가게 삭제 성공")
        void deleteStore() {
            // given
            Long storeId = 1L;
            StoreRequestDto storeRequestDto = new StoreRequestDto();
            User user = mock(User.class);
            Store store = new Store(storeRequestDto, user, new StoreAddress());

            when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
            when(passwordEncoder.matches(storeRequestDto.getPassword(), user.getPassword())).thenReturn(true);
            when(userRepository.save(user)).thenReturn(user);

            // when
            StatusResponseDto response = storeService.deleteStore(storeId, storeRequestDto, user);

            // then
            assertEquals("가게 정보가 삭제되었습니다.", response.getMsg());
            assertEquals(200, response.getStatuscode());
        }

        @Test
        @DisplayName("가게 삭제 실패 - 권한이 없는 경우")
        void failDeleteStoreUserId() {
            // given
            Long storeId = 1L;
            StoreRequestDto storeRequestDto = new StoreRequestDto();

            // User 객체를 모킹하고 사용자 ID를 설정
            User user1 = mock(User.class);
            when(user1.getId()).thenReturn(1L);
            User user2 = mock(User.class);
            when(user2.getId()).thenReturn(2L);

            Store store = new Store(storeRequestDto, user1, new StoreAddress());

            // storeRepository.findById()가 해당 가게를 반환하도록 설정
            when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

            // 사용자 ID가 다른 경우, 예외가 발생해야 합니다.
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> storeService.deleteStore(storeId, storeRequestDto, user2));
            assertEquals("삭제 권한이 없습니다", exception.getMessage());
        }

        @Test
        @DisplayName("가게 삭제 실패 - Password 다름")
        void failDeleteStorePassword() {
            // given
            Long storeId = 1L;
            StoreRequestDto storeRequestDto = new StoreRequestDto();
            User user = mock(User.class);
            Store store = new Store(storeRequestDto, user, new StoreAddress());

            when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));

            // 사용자 비밀번호 다르게
            when(passwordEncoder.matches(storeRequestDto.getPassword(), store.getUser().getPassword())).thenReturn(false);

            // when, then
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class, () -> storeService.updateStore(storeId, storeRequestDto, user));
            assertEquals("비밀번호가 다릅니다", illegalArgumentException.getMessage());
        }
    }
}