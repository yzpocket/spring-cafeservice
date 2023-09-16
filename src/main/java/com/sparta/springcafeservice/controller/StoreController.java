package com.sparta.springcafeservice.controller;

import com.sparta.springcafeservice.dto.StoreRequestDto;
import com.sparta.springcafeservice.dto.StoreResponseDto;
import com.sparta.springcafeservice.security.UserDetailsImpl;
import com.sparta.springcafeservice.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // Create
    @PostMapping("/stores")
    public StoreResponseDto createStore(@RequestBody StoreRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return storeService.createStore(requestDto, userDetails.getUser());
    }

    // ReadAll
    @GetMapping("/stores")
    public List<StoreResponseDto> getAllStores(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return storeService.getAllStores(userDetails.getUser());
    }

    // Read
    @GetMapping("/stores/{id}")
    public StoreResponseDto getStore(@PathVariable Long id,  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return storeService.getStore(id, userDetails.getUser());
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
