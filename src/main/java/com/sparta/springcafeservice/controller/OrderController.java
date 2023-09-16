package com.sparta.springcafeservice.controller;


import com.sparta.springcafeservice.dto.OrderRequestDto;
import com.sparta.springcafeservice.dto.OrderResponseDto;
import com.sparta.springcafeservice.entity.User;
import com.sparta.springcafeservice.security.UserDetailsImpl;
import com.sparta.springcafeservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

//create
    @PostMapping("/orders")
    public OrderResponseDto createOrder(@RequestBody OrderRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.createOrder(requestDto, userDetails.getUser());
    }

    //readAll
    @GetMapping("/orders")
    public List<OrderResponseDto> getAllOrders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.getAllOrders(userDetails.getUser());
    }

    //read
    @GetMapping("/orders/{id}")
    public OrderResponseDto getOrder(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.getOrder(id, userDetails.getUser());
    }

    //update
    @PutMapping("/orders/{id}")
    public ResponseEntity<String> updateOrder(@PathVariable Long id, @RequestBody OrderRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.updateOrder(id, requestDto, userDetails.getUser());
    }

    //delete
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id, @RequestBody OrderRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return orderService.deleteOrder(id, requestDto, userDetails.getUser());
    }

}
