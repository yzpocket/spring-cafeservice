package com.sparta.springcafeservice.controller;


import com.sparta.springcafeservice.dto.OrderRequestDto;
import com.sparta.springcafeservice.dto.OrderResponseDto;
import com.sparta.springcafeservice.dto.StatusResponseDto;
import com.sparta.springcafeservice.security.UserDetailsImpl;
import com.sparta.springcafeservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    // 주문 등록
    @PostMapping("/orders")
    public ResponseEntity<StatusResponseDto> createOrder(@RequestBody OrderRequestDto requestDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.createOrder(requestDto, userDetails.getUser());
    }

    // 주문 조회 (userId)
    @GetMapping("/orders")
    public List<OrderResponseDto> getAllOrdersByUserId(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.getAllOrdersByUserId(userDetails.getUser());
    }

    // 주문 조회 (storeId)
    @GetMapping("/orders/stores")
    public List<OrderResponseDto> getAllOrdersByStoreId(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.getAllOrdersByStoreId(userDetails.getUser());

    }

    // 주문 수정
    @PutMapping("/orders/{id}")
    public ResponseEntity<StatusResponseDto> updateOrder(@PathVariable Long id,
                                                         @RequestBody OrderRequestDto requestDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.updateOrder(id, requestDto, userDetails.getUser());
    }

    // 주문 삭제
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<StatusResponseDto> deleteOrder(@PathVariable Long id,
                                                         @RequestBody OrderRequestDto requestDto,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.deleteOrder(id, requestDto, userDetails.getUser());
    }

}
