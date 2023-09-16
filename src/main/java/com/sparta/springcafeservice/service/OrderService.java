package com.sparta.springcafeservice.service;


import com.sparta.springcafeservice.dto.OrderRequestDto;
import com.sparta.springcafeservice.dto.OrderResponseDto;
import com.sparta.springcafeservice.dto.StoreResponseDto;
import com.sparta.springcafeservice.entity.Menu;
import com.sparta.springcafeservice.entity.Order;
import com.sparta.springcafeservice.entity.Store;
import com.sparta.springcafeservice.entity.User;
import com.sparta.springcafeservice.repository.MenuRepository;
import com.sparta.springcafeservice.repository.OrderRepository;
import com.sparta.springcafeservice.repository.StoreRepository;
import com.sparta.springcafeservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.channels.IllegalChannelGroupException;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;



    // create
    public OrderResponseDto createOrder(OrderRequestDto requestDto, User user) {
        //db확인
        Menu menu = menuRepository.findById(requestDto.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴를 찾을 수 없습니다."));
        Store store = storeRepository.findById(requestDto.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다."));

        Order order = orderRepository.save(new Order(requestDto, user, menu, store));
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
        } else {
            return new OrderResponseDto(order);
        }
    }

    // update
    @Transactional
    public ResponseEntity<String> updateOrder(Long id, OrderRequestDto requestDto, User user) {
        Order order = findOrder(id);
        // user가 order에서 가져온 userId값과 다를 때(동일 사장) 예외처리
        if (!user.getId().equals(requestDto.getUserId())) {
            throw new IllegalArgumentException("주문 취소 권한이 없습니다.");
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("주문 취소 권한이 없습니다.");
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
        if (!user.getId().equals(requestDto.getUserId())) {
            throw new IllegalArgumentException("주문 삭제 권한이 없습니다.");
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("주문 취소 권한이 없습니다.");
        }
        return ResponseEntity.ok("주문을 취소하였습니다.");
    }


    private Order findOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalIdentifierException("해당 주문은 존재하지 않습니다."));
    }

    private Order findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

}
