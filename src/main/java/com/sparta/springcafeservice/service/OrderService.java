package com.sparta.springcafeservice.service;


import com.sparta.springcafeservice.dto.OrderRequestDto;
import com.sparta.springcafeservice.dto.OrderResponseDto;
import com.sparta.springcafeservice.entity.Menu;
import com.sparta.springcafeservice.entity.Order;
import com.sparta.springcafeservice.entity.Store;
import com.sparta.springcafeservice.entity.User;
import com.sparta.springcafeservice.exception.RestApiException;
import com.sparta.springcafeservice.repository.MenuRepository;
import com.sparta.springcafeservice.repository.OrderRepository;
import com.sparta.springcafeservice.repository.StoreRepository;
import com.sparta.springcafeservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;


    // create
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto requestDto, User user) {
        //db확인
        Menu menu = menuRepository.findById(requestDto.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴를 찾을 수 없습니다."));
        Store store = storeRepository.findById(requestDto.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다."));

        //user가 가진 포인트 확인
        if (user.getPoint() < menu.getPrice()) {
            throw new IllegalArgumentException("포인트가 부족해 주문할 수 없습니다");
        }

        Order order = orderRepository.save(new Order(requestDto, user, menu));

        user.setPoint(user.getPoint() - menu.getPrice());
        userRepository.save(user);

        return new OrderResponseDto(order);
    }

    // readAll
    public List<OrderResponseDto> getAllOrders(User user) {
        List<Order> orderList = orderRepository.findAllByOrderByModifiedAtDesc();
        return orderList.stream().map(OrderResponseDto::new).collect(Collectors.toList());
    }

    public List<OrderResponseDto> getAllOrdersByStoreId(User user) {
        Long storeId = user.getStore().getId();
        List<Order> orders = orderRepository.findAllByOrderByModifiedAtDesc();
        List<OrderResponseDto> storeOrders = orders.stream().filter(order -> order.getMenu().getStore().getId().equals(storeId))
                .map(OrderResponseDto::new)
                .collect(Collectors.toList());

        return storeOrders;
    }

    // update
    @Transactional
    public ResponseEntity<String> updateOrder(Long id, OrderRequestDto requestDto, User user) {
        Order order = findOrder(id);
        //주문의 storeId와 user의 storeId를 비교한다 -> 사장인지 확인!
        if (!order.getMenu().getStore().getId().equals(user.getStore().getId())) {
            throw new IllegalArgumentException("주문상태를 변경할 권한이 없습니다.");
        }
        order.update(requestDto);
        //수정 성공
        return ResponseEntity.ok("주문 상태가 바뀌었습니다.");
    }

    // delete
    @Transactional
    public ResponseEntity<String> deleteOrder(Long id, OrderRequestDto requestDto, User user) {
        Order order = findOrder(id);

        if (!order.getMenu().getStore().getId().equals(user.getStore().getId())) {
            throw new IllegalArgumentException("주문을 취소할 권한이 없습니다.");
        }

        return ResponseEntity.ok("주문을 취소하였습니다.");
    }


    private Order findOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(() ->
                new RestApiException("선택한 주문은 존재하지 않습니다.", HttpStatus.NOT_FOUND.value())
        );
    }

    private Order findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

}
