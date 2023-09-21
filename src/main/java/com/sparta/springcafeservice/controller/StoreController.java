package com.sparta.springcafeservice.controller;

import com.sparta.springcafeservice.dto.StatusResponseDto;
import com.sparta.springcafeservice.dto.StoreAllResponseDto;
import com.sparta.springcafeservice.dto.StoreRequestDto;
import com.sparta.springcafeservice.dto.StoreResponseDto;
import com.sparta.springcafeservice.security.UserDetailsImpl;
import com.sparta.springcafeservice.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:8080") // 로컬 웹 애플리케이션의 도메인을 지정
@RequestMapping("/api")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // Create
    @PostMapping("/stores")
    public StoreAllResponseDto createStore(@RequestBody StoreRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return storeService.createStore(requestDto, userDetails.getUser());
    }

    @GetMapping("/stores")
    public List<StoreAllResponseDto> getAllStores() {
        return storeService.getAllStores();
    }

    // Read
    @GetMapping("/stores/{storeId}")
    public StoreResponseDto getStore(@PathVariable Long storeId) {
        return storeService.getStore(storeId);
    }

    // Update
    @PutMapping("/stores/{storeId}")
    public ResponseEntity<StoreAllResponseDto> updateStore(@PathVariable Long storeId, @RequestBody StoreRequestDto requestDto
            , @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return storeService.updateStore(storeId, requestDto, userDetails.getUser());

    }


    // Delete
    @DeleteMapping("/stores/{storeId}")
    public ResponseEntity<StoreAllResponseDto> deleteStore(@PathVariable Long storeId,  @RequestBody StoreRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return storeService.deleteStore(storeId, requestDto, userDetails.getUser());
    }

}
