package com.sparta.springcafeservice.controller;

import com.sparta.springcafeservice.dto.StatusResponseDto;
import com.sparta.springcafeservice.dto.StoreAllResponseDto;
import com.sparta.springcafeservice.dto.StoreRequestDto;
import com.sparta.springcafeservice.dto.StoreResponseDto;
import com.sparta.springcafeservice.entity.Menu;
import com.sparta.springcafeservice.entity.Review;
import com.sparta.springcafeservice.security.UserDetailsImpl;
import com.sparta.springcafeservice.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 가게 등록
    @PostMapping("/stores")
    public ResponseEntity<StatusResponseDto>  createStore(@RequestBody StoreRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return storeService.createStore(requestDto, userDetails.getUser());
    }

    // 가게 조회
    @GetMapping("/stores")
    public List<StoreAllResponseDto> getAllStores() {
        return storeService.getAllStores();
    }

    // 선택한 가게 조회
    @GetMapping("/stores/{storeId}")
    public StoreResponseDto getStore(@PathVariable Long storeId) {
        return storeService.getStore(storeId);
    }

    // 가게 수정
    @PutMapping("/stores/{storeId}")
    public ResponseEntity<StatusResponseDto> updateStore(@PathVariable Long storeId,
                                                           @RequestBody StoreRequestDto requestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return storeService.updateStore(storeId, requestDto, userDetails.getUser());

    }


    // 가게 삭제
    @DeleteMapping("/stores/{storeId}")
    public ResponseEntity<StatusResponseDto> deleteStore(@PathVariable Long storeId,
                                                           @RequestBody StoreRequestDto requestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return storeService.deleteStore(storeId, requestDto, userDetails.getUser());
    }



    // Read - [select * from reviews where store_id = id]
    @GetMapping("/stores/{storeId}/reviews")
    public List<Review> getReviewsByStoreId(@PathVariable Long storeId) {
        return storeService.getReviewsByStoreId(storeId);
    }

    // Read - [select * from menus where store_id = id]
    @GetMapping("/stores/{storeId}/menus")
    public List<Menu> getMenusByStoreId(@PathVariable Long storeId) {
        return storeService.getMenusByStoreId(storeId);
    }
}
