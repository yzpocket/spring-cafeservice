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
    public ResponseEntity<StatusResponseDto> createOrder(OrderRequestDto requestDto, User user) {
        return handleServiceRequest(()->{

            Menu menu = menuRepository.findById(requestDto.getMenuId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 메뉴를 찾을 수 없습니다."));
            Store store = storeRepository.findById(requestDto.getStoreId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 가게를 찾을 수 없습니다."));

            // 잔액 예외 처리 -> 포인트 업데이트
            if (user.getPoint() < menu.getPrice()) {
                throw new IllegalArgumentException("포인트가 부족해 주문할 수 없습니다");
            }
            user.setPoint(user.getPoint() - menu.getPrice());
            userRepository.save(user);
            return new StatusResponseDto("주문을 등록했습니다.", 200);
        });
    }


    // 주문 조회
    public List<OrderResponseDto> getAllOrders(User user) {
        List<Order> orderList = orderRepository.findAllByOrderByModifiedAtDesc();
        return orderList.stream().map(OrderResponseDto::new).collect(Collectors.toList());
    }


    // 선택한 주문 조회
    public List<OrderResponseDto> getAllOrdersByStoreId(User user) {
        Long storeId = user.getStore().getId();
        List<Order> orders = orderRepository.findAllByOrderByModifiedAtDesc();
        List<OrderResponseDto> storeOrders = orders.stream().filter(order -> order.getMenu().getStore().getId().equals(storeId))
                .map(OrderResponseDto::new)
                .collect(Collectors.toList());
        return storeOrders;
    }


    // 주문 수정
    @Transactional
    public ResponseEntity<StatusResponseDto> updateOrder(Long id, OrderRequestDto requestDto, User user) {
        return handleServiceRequest(()->{

            Order order = checkOrderExist(id);
            //변경사항 확인
            OrderStatusEnum currentStatus = order.getOrderStatus();
            OrderStatusEnum newStatus = requestDto.getOrderStatus();

            if (!order.getMenu().getStore().getId().equals(user.getStore().getId())) {
                throw new IllegalArgumentException("주문상태를 변경할 권한이 없습니다.");
            }

            //주문 상태가 바뀌면 사장에게 돈을 입금
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
        });
    }


    // 주문 삭제
    @Transactional
    public ResponseEntity<StatusResponseDto> deleteOrder(Long id, OrderRequestDto requestDto, User user) {
        return handleServiceRequest(()->{

            Order order = checkOrderExist(id);
            if (!order.getMenu().getStore().getId().equals(user.getStore().getId())) {
                throw new IllegalArgumentException("주문을 취소할 권한이 없습니다.");
            }
            return new StatusResponseDto("주문을 취소하였습니다.", 200);
        });


    }


    // 주문 찾기(id)
    private Order checkOrderExist(Long id) {
        return orderRepository.findById(id).orElseThrow(() ->
                new RestApiException("선택한 주문은 존재하지 않습니다.", HttpStatus.NOT_FOUND.value())
        );
    }


    // 중복 코드 제거를 위한 메소드
    private ResponseEntity<StatusResponseDto> handleServiceRequest(Supplier<StatusResponseDto> action) {
        try {
            return new ResponseEntity<>(action.get(), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(new StatusResponseDto(ex.getMessage(), 400), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(new StatusResponseDto("서비스 요청 중 오류가 발생했습니다.", 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
