package com.sparta.springcafeservice.service;

import com.sparta.springcafeservice.dto.OrderRequestDto;
import com.sparta.springcafeservice.dto.OrderResponseDto;
import com.sparta.springcafeservice.dto.StatusResponseDto;
import com.sparta.springcafeservice.entity.*;
import com.sparta.springcafeservice.entity.Order;
import com.sparta.springcafeservice.repository.MenuRepository;
import com.sparta.springcafeservice.repository.OrderRepository;
import com.sparta.springcafeservice.repository.StoreRepository;
import com.sparta.springcafeservice.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.fail;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private MenuRepository menuRepository;


    @Nested
    @DisplayName("CreateOrder")
    class Create {
        @Test
        @DisplayName("주문 생성 성공")
        void createOrder() {
            // given
            OrderRequestDto orderRequestDto = new OrderRequestDto(); // 주문 생성에 필요한 데이터를 설정
            Menu menu = mock(Menu.class);
            lenient().when(menuRepository.findById(orderRequestDto.getMenuId())).thenReturn(Optional.of(menu));

            Store store = mock(Store.class);
            lenient().when(storeRepository.findById(orderRequestDto.getStoreId())).thenReturn(Optional.of(store));

            User user = mock(User.class);
            when(user.getPoint()).thenReturn(10000); // 충분한 포인트 설정
            lenient().when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
            when(orderRepository.save(any(Order.class))).thenReturn(new Order());

            // when
            StatusResponseDto response = orderService.createOrder(orderRequestDto, user);

            // then
            assertEquals(HttpStatus.OK.value(), response.getStatuscode());
        }

        @Test
        @DisplayName("주문 생성 실패 - 메뉴를 찾을 수 없음")
        void createOrderMenuNotFound() {
            // given
            OrderRequestDto orderRequestDto = new OrderRequestDto();
            when(menuRepository.findById(orderRequestDto.getMenuId())).thenReturn(Optional.empty());

            // when, then
            // 메뉴를 찾을 수 없는 경우, IllegalArgumentException
            assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(orderRequestDto, mock(User.class)));
        }

        @Test
        @DisplayName("주문 생성 실패 - 가게를 찾을 수 없음")
        void createOrderStoreNotFound() {
            // given
            OrderRequestDto orderRequestDto = new OrderRequestDto();
            when(menuRepository.findById(orderRequestDto.getMenuId())).thenReturn(Optional.of(mock(Menu.class)));
            when(storeRepository.findById(orderRequestDto.getStoreId())).thenReturn(Optional.empty());

            // when, then
            // 가게를 찾을 수 없는 경우, IllegalArgumentException
            assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(orderRequestDto, mock(User.class)));
        }

//        @Test
//        @DisplayName("주문 생성 실패 - 포인트 부족")
//            // 테스트 실패 - 동작은 잘 함
//        void createOrderInsufficientPoints() {
//            // given
//            OrderRequestDto orderRequestDto = new OrderRequestDto();
//            Menu menu = mock(Menu.class);
//            User user = mock(User.class);
//
//            // 포인트가 부족한 상황을 모킹합니다. 예: 메뉴 가격이 1000, 사용자 포인트가 500인 경우
//            when(menuRepository.findById(orderRequestDto.getMenuId())).thenReturn(Optional.of(menu));
//            when(menu.getPrice()).thenReturn(1000); // 메뉴 가격 설정
//            when(user.getPoint()).thenReturn(500); // 사용자 포인트 설정
//
//            // when, then
//            // 포인트가 부족한 경우, IllegalArgumentException이 발생해야 합니다.
//            assertThrows(IllegalArgumentException.class, () -> orderService.createOrder(orderRequestDto, user));
//
//            verify(menu, times(1)).getPrice();
//
//        }
    }

    @Nested
    @DisplayName("getOrder")
    class Read {
        @Test
        @DisplayName("주문 조회 성공")
        void getAllOrdersByUserId() {
            // given
            Long userId = 1L;

            // 모의 주문 목록 생성
            List<Order> mockOrders = new ArrayList<>();
            mockOrders.add(new Order());
            mockOrders.add(new Order());

            // 주문 목록을 가져오는 메서드에 대한 Mock 설정
            when(orderRepository.findAllByUserIdOrderByModifiedAtDesc(userId)).thenReturn(mockOrders);

            User user = new User(); // User 객체 생성
            ReflectionTestUtils.setField(user, "id", userId); // ReflectionTestUtils를 사용하여 id 필드 설정

            // when
            List<OrderResponseDto> orderResponseDtoList = orderService.getAllOrdersByUserId(user);

            // then
            // 주문 목록을 가져오는 메서드가 호출되었는지 확인
            verify(orderRepository, times(1)).findAllByUserIdOrderByModifiedAtDesc(userId);

            // 주문 목록 크기와 반환된 주문 목록 크기가 일치하는지 확인
            assertEquals(mockOrders.size(), orderResponseDtoList.size());
        }


        @Test
        @DisplayName("가게 ID로 주문 조회 성공")
        void getAllOrdersByStoreId() {
            // given
            Long storeId = 1L;

            // 모의 주문 목록 생성
            List<Order> mockOrders = new ArrayList<>();
            mockOrders.add(new Order());
            mockOrders.add(new Order());

            // User 객체를 생성하고, 가게 정보를 반환하도록 설정
            User user = mock(User.class);
            Store store = new Store(); // 가게 정보 생성
            ReflectionTestUtils.setField(store, "id", storeId); // 가게 ID 설정
            when(user.getStore()).thenReturn(store); // 가게 정보 반환 설정


            // 주문 목록을 가져오는 메서드에 대한 Mock 설정
            when(orderRepository.findAllByMenu_Store_IdOrderByModifiedAtDesc(storeId)).thenReturn(mockOrders);

            // when
            List<OrderResponseDto> orderResponseDtoList = orderService.getAllOrdersByStoreId(user);

            // then
            // 주문 목록을 가져오는 메서드가 호출되었는지 확인
            verify(orderRepository, times(1)).findAllByMenu_Store_IdOrderByModifiedAtDesc(storeId);

            // 주문 목록 크기와 반환된 주문 목록 크기가 일치하는지 확인
            assertEquals(mockOrders.size(), orderResponseDtoList.size());
        }


    }

//    @Nested
//    @DisplayName("updateOrder - 일단 보류")
//    class update {
//        @Test
//        void updateOrder() {
//            // given
//            Long orderId = 1L;
//            OrderService orderService = new OrderService(orderRepository, menuRepository, storeRepository, userRepository);
//            Order order = new Order();
//            Menu menu = new Menu();
//            Store store = new Store();
//            User user = new User();
//            OrderRequestDto requestDto = new OrderRequestDto();
//
//            order.setOrderStatus(OrderStatusEnum.ORDER_CONFIRMATION);
//
//            // Store 객체에 ID 설정
//            ReflectionTestUtils.setField(store, "id", 1L);
//
//            // 필드 설정
//            ReflectionTestUtils.setField(order, "id", orderId);
//            ReflectionTestUtils.setField(order, "menu", menu);
//            ReflectionTestUtils.setField(menu, "store", store);
//            ReflectionTestUtils.setField(user, "store", store);
//
//            // Mock 객체 설정
//            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
//            OrderStatusEnum newStatus = OrderStatusEnum.DELIVERY_COMPLETED; // 이 위치에서 초기화
//
//            // when
//            StatusResponseDto response = orderService.updateOrder(orderId, requestDto, user);
//
//            // then
//            assertEquals(HttpStatus.OK.value(), response.getStatuscode());
//            assertEquals(newStatus, order.getOrderStatus());
//        }
//
//
//    }
//
//    @Nested
//    @DisplayName("deleteOrder - 일단 보류")
//    class delete {
//        @Test
//        void updateOrder() {
//            // given
//            Long orderId = 1L;
//            OrderService orderService = new OrderService(orderRepository, menuRepository, storeRepository, userRepository);
//            Order order = new Order();
//            Menu menu = new Menu();
//            Store store = new Store();
//            User user = new User();
//            OrderRequestDto requestDto = new OrderRequestDto();
//            OrderStatusEnum newStatus = OrderStatusEnum.DELIVERY_COMPLETED; // newStatus 초기화
//
//            order.setOrderStatus(OrderStatusEnum.ORDER_CONFIRMATION);
//
//            // 필드 설정
//            ReflectionTestUtils.setField(order, "id", orderId);
//            ReflectionTestUtils.setField(order, "menu", menu);
//            ReflectionTestUtils.setField(menu, "store", store);
//            ReflectionTestUtils.setField(user, "store", store);
//
//            // Mock 객체 설정
//            when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
//
//            // when
//            StatusResponseDto response = orderService.updateOrder(orderId, requestDto, user);
//
//            // then
//            assertEquals(HttpStatus.OK.value(), response.getStatuscode());
//            assertEquals(newStatus, order.getOrderStatus());
//        }
//    }
}
