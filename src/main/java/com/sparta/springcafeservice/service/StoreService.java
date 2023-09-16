package com.sparta.springcafeservice.service;


import com.sparta.springcafeservice.dto.StoreRequestDto;
import com.sparta.springcafeservice.dto.StoreResponseDto;
import com.sparta.springcafeservice.entity.Store;
import com.sparta.springcafeservice.entity.User;
import com.sparta.springcafeservice.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Create
    public StoreResponseDto createStore(StoreRequestDto requestDto, User user) {
        Store store = storeRepository.save(new Store(requestDto, user));
        //Entity -> ResponseDto
        return new StoreResponseDto(store);
    }


    //ReadAll
    public List<StoreResponseDto> getAllStores(User user) {
        List<Store> storeList = storeRepository.findAll();
        return storeList.stream().map(StoreResponseDto::new).collect(Collectors.toList());
    }


    // Read
    public StoreResponseDto getStore(Long id, User user) {
        return new StoreResponseDto(findStore(id));
    }


    // Update
    @Transactional
    public ResponseEntity<String> updateStore(Long id, StoreRequestDto requestDto, User user) {
        Store store = findStore(id);
        // 해당 메모의 작성자와 현재 로그인한 사용자를 비교하여 작성자가 같지 않으면 예외 발생, 어드민이 아닐 시
//        if (user.getRole() == UserRoleEnum.USER && !store.getUser().getUserId().equals(user.getUserId())) {
//            throw new IllegalArgumentException("사장님만 수정할 수 있습니다.");
//        }
        // 비밀 번호가 다를 시 예외처리
        if (!passwordEncoder.matches(requestDto.getPassword(), store.getUser().getPassword())) {
            throw new IllegalArgumentException("비밀번호가 다릅니다");
        }

        store.update(requestDto);
        // 수정 성공
        return ResponseEntity.ok("수정 성공!");

    }


    // Delete
    @Transactional
    public ResponseEntity<String> deleteStore(Long id, StoreRequestDto requestDto, User user) {
        Store store = findStore(id);

//        if (user.getRole() == UserRoleEnum.USER && !store.getUser().getUserId().equals(user.getUserId())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사장님만 삭제할 수 있습니다.");
//        }

        // 비밀 번호가 다를 시 예외처리
        if (!passwordEncoder.matches(requestDto.getPassword(), store.getUser().getPassword())) {
            throw new IllegalArgumentException("비밀번호가 다릅니다");
        }


        storeRepository.delete(store);

        return ResponseEntity.ok("삭제 성공!");
    }


    private Store findStore(Long id) {
        return storeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 가게는 존재하지 않습니다."));
    }
}
