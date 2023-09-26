package com.sparta.springcafeservice.service;


import com.sparta.springcafeservice.dto.StatusResponseDto;
import com.sparta.springcafeservice.dto.StoreAllResponseDto;
import com.sparta.springcafeservice.dto.StoreRequestDto;
import com.sparta.springcafeservice.dto.StoreResponseDto;
import com.sparta.springcafeservice.entity.*;
import com.sparta.springcafeservice.exception.RestApiException;
import com.sparta.springcafeservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StoreAddressRepository storeAddressRepository;

    // 가게 등록
    @Transactional
    public StatusResponseDto createStore(StoreRequestDto requestDto, User user) {
        log.info("가게 생성 로그 ");

        List<Store> existStoreName = storeRepository.findByStoreNameContaining(requestDto.getStoreName());
        Optional<Store> existBizNum = storeRepository.findByBusinessNum(requestDto.getBusinessNum());
        StoreAddress storeAddress = requestDto.toStoreAddress();
        storeAddressRepository.save(storeAddress);

        // 가게 이름 중복 금지
        if (storeRepository.existsByStoreName(requestDto.getStoreName())) {
            throw new IllegalArgumentException("중복된 가게이름이 존재합니다.");
        }
        // 가게 이름 중복체크
        if (!existStoreName.isEmpty()) {
            throw new IllegalArgumentException("중복된 가게 이름입니다.");
        }

        // 유저아이디로 만든 스토어가 있는지 확인 (레파지토리)
        if (storeRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("이미 가게를 등록하였습니다.");
        }

        // 사업자 중복 체크
        if (existBizNum.isPresent()) {
            throw new IllegalArgumentException("중복된 사업자 번호입니다.");
        }


        // store 를 등록하는 사용자의 point -> 0으로 세팅 / user 의 role USER ->BIZ 로 변경
        user.setPoint(0);
        user.setRole(UserRoleEnum.BIZ);
        userRepository.save(user);
        Store store = new Store(requestDto, user, storeAddress);
        storeRepository.save(store);

        return new StatusResponseDto("가게가 등록되었습니다.", 200);
    }


    // 가게 조회
    public List<StoreAllResponseDto> getAllStores() {
        List<Store> storeList = storeRepository.findAll();
        return storeList.stream().map(StoreAllResponseDto::new).collect(Collectors.toList());
    }


    // 선택한 가게 조회
    public StoreResponseDto getStore(Long storeId) {
        return new StoreResponseDto(checkStoreExist(storeId));
    }


    // 가게 수정
    @Transactional
    public StatusResponseDto updateStore(Long id, StoreRequestDto requestDto, User user) {
        Store store = checkStoreExist(id);


        // 비밀 번호가 다를 시 예외처리
        if (!passwordEncoder.matches(requestDto.getPassword(), store.getUser().getPassword())) {
            throw new IllegalArgumentException("비밀번호가 다릅니다");
        }
        //가게 사장만 수정 가능(엔티티의 고유 식별자인 id 값으로 비교하는 것으로 바꿨습니다..)
        if (!user.getId().equals(store.getUser().getId())) {
            throw new IllegalArgumentException("사용자가 다릅니다");
        }

        store.update(requestDto);
        return new StatusResponseDto("가게 정보가 수정되었습니다.", 200);
    }


    // 가게 삭제
    @Transactional
    public StatusResponseDto deleteStore(Long storeId, StoreRequestDto requestDto, User user) {
        Store store = checkStoreExist(storeId);

        //가게 사장만 삭제 가능
        if (!user.getId().equals(store.getUser().getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다");
        }
        // 비밀 번호가 다를 시 예외처리
        if (!passwordEncoder.matches(requestDto.getPassword(), store.getUser().getPassword())) {
            throw new IllegalArgumentException("비밀번호가 다릅니다");
        }

        user.setRole(UserRoleEnum.USER);
        userRepository.save(user);

        storeRepository.delete(store);
        return new StatusResponseDto("가게 정보가 삭제되었습니다.", 200);
    }


    private Store checkStoreExist(Long id) {
        // 가게 ID를 사용하여 특정 가게을 조회하고, 존재하지 않을 경우 예외 발생
        return storeRepository.findById(id).orElseThrow(() ->
                new RestApiException("선택한 가게는 존재하지 않습니다.", HttpStatus.NOT_FOUND.value())
        );
    }


    // 특정 가게 리뷰 모두 조회
    public List<Review> getReviewsByStoreId(Long storeId) {
        return reviewRepository.findAllByStoreId(storeId);
    }


    public List<Menu> getMenusByStoreId(Long storeId) {
        return menuRepository.findAllByStoreId(storeId);
    }
}
