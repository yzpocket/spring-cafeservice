package com.sparta.springcafeservice.service;


import com.sparta.springcafeservice.dto.OrderRequestDto;
import com.sparta.springcafeservice.dto.OrderResponseDto;
import com.sparta.springcafeservice.dto.StatusResponseDto;
import com.sparta.springcafeservice.entity.*;
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
import java.util.function.Supplier;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;


    // 주문 등록
    @Transactional
    public StatusResponseDto createOrder(OrderRequestDto requestDto, User user) {
        Menu menu = menuRepository.findById(requestDto.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴를 찾을 수 없습니다."));
        Store store = storeRepository.findById(requestDto.getStoreId())
                .orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다."));

        if (user.getPoint() < menu.getPrice()) {
            throw new IllegalArgumentException("포인트가 부족해 주문할 수 없습니다");
        }

        user.setPoint(user.getPoint() - menu.getPrice());
        userRepository.save(user);

        orderRepository.save(new Order(requestDto, user, menu));
        return new StatusResponseDto("주문을 등록했습니다.", 200);
    }


    // 주문 조회 (userId)
    public List<OrderResponseDto> getAllOrdersByUserId(User user) {
        List<Order> orderList = orderRepository.findAllByUserIdOrderByModifiedAtDesc(user.getId());
        return orderList.stream().map(OrderResponseDto::new).collect(Collectors.toList());
    }


    // 주문 조회 (storeId)
    public List<OrderResponseDto> getAllOrdersByStoreId(User user) {
        Long storeId = user.getStore().getId();
        List<Order> storeOrders = orderRepository.findAllByMenu_Store_IdOrderByModifiedAtDesc(storeId);
        return storeOrders.stream().map(OrderResponseDto::new).collect(Collectors.toList());
    }




    // 주문 수정
    @Transactional
    public StatusResponseDto updateOrder(Long id, OrderRequestDto requestDto, User user) {
        Order order = checkOrderExist(id);

        OrderStatusEnum currentStatus = order.getOrderStatus();
        OrderStatusEnum newStatus = requestDto.getOrderStatus();

        if (!order.getMenu().getStore().getId().equals(user.getStore().getId())) {
            throw new IllegalArgumentException("주문상태를 변경할 권한이 없습니다.");
        }

        if (!currentStatus.equals(newStatus)) {
            if (newStatus.equals(OrderStatusEnum.DELIVERY_COMPLETED)) {
                int menuPrice = order.getMenu().getPrice();
                User owner = order.getMenu().getStore().getUser();

                int notUpdatePoint = owner.getPoint();
                int updatePoint = (notUpdatePoint + menuPrice);

                owner.setPoint(updatePoint);
            }
        }

        order.update(requestDto);
        return new StatusResponseDto("주문 상태가 바뀌었습니다.", 200);
    }


    // 주문 삭제 -> 취소
    @Transactional
    public StatusResponseDto deleteOrder(Long id, OrderRequestDto requestDto, User user) {
        Order order = checkOrderExist(id);

        if (!order.getMenu().getStore().getId().equals(user.getStore().getId())) {
            throw new IllegalArgumentException("주문을 취소할 권한이 없습니다.");
        }

        int orderPrice = order.getMenu().getPrice();
        user.setPoint(user.getPoint() + orderPrice);
        userRepository.save(user);

        order.setOrderStatus(OrderStatusEnum.CANCELED);

        return new StatusResponseDto("주문을 취소하였습니다.", 200);
    }


    // 주문 찾기(id)
    private Order checkOrderExist(Long id) {
        return orderRepository.findById(id).orElseThrow(() ->
                new RestApiException("선택한 주문은 존재하지 않습니다.", HttpStatus.NOT_FOUND.value())
        );
    }
}
