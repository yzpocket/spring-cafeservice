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
import org.hibernate.boot.model.naming.IllegalIdentifierException;
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

        Order order = orderRepository.save(new Order(requestDto, user, menu, store));

        user.setPoint(user.getPoint() - menu.getPrice());
        userRepository.save(user);

        return new OrderResponseDto(order);
    }

    // readAll
    public List<OrderResponseDto> getAllOrders(User user) {
        List<Order> orderList = orderRepository.findAllByOrderByModifiedAtDesc();
        return orderList.stream().map(OrderResponseDto::new).collect(Collectors.toList());
    }

    //read -> 일반 USER 는 자신이 주문한 주문을 조회할 수 있다. ->userId 별로 찾기
    public OrderResponseDto getOrder(Long id, User user) {
        Order order = findByUserId(user.getId());

        if (order == null) {
            throw new IllegalArgumentException("주문 내역이 없습니다.");
        }
        return new OrderResponseDto(order);
    }

    // update
    @Transactional
    public ResponseEntity<String> updateOrder(Long id, OrderRequestDto requestDto, User user) {
        Order order = findOrder(id);
        // user가 order에서 가져온 userId값과 다를 때(동일 사장) 예외처리
        if (!user.getId().equals(order.getStore().getUser().getId())) {
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
        // user가 order에서 가져온 userId값과 다를 때(동일 사장) 예외처리
        if (!user.getId().equals(order.getStore().getUser().getId())) {
            throw new IllegalArgumentException("주문을 취소할 권한이 없습니다.");
        }

        return ResponseEntity.ok("주문을 취소하였습니다.");
    }


    private Order findOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(() ->
                new RestApiException("선택한 주문는 존재하지 않습니다.", HttpStatus.NOT_FOUND.value())
        );
    }

    private Order findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

}
