package com.sparta.springcafeservice.controller;

import com.sparta.springcafeservice.dto.StoreAllResponseDto;
import com.sparta.springcafeservice.dto.StoreRequestDto;
import com.sparta.springcafeservice.dto.StoreResponseDto;
import com.sparta.springcafeservice.security.UserDetailsImpl;
import com.sparta.springcafeservice.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//@Controller
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
    @GetMapping("/stores/{id}")
    public StoreResponseDto getStore(@PathVariable Long id) {
        return storeService.getStore(id);
    }

    // Update
    @PutMapping("/stores/{id}")
    public ResponseEntity<String> updateStore(@PathVariable Long id, @RequestBody StoreRequestDto requestDto
            , @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return storeService.updateStore(id, requestDto, userDetails.getUser());

    }


    // Delete
    @DeleteMapping("/stores/{id}")
    public ResponseEntity<String> deleteStore(@PathVariable Long id,  @RequestBody StoreRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return storeService.deleteStore(id, requestDto, userDetails.getUser());
    }

}
