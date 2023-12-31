package com.sparta.springcafeservice.dto;


import com.sparta.springcafeservice.entity.Order;
import com.sparta.springcafeservice.entity.OrderStatusEnum;
import lombok.Getter;

@Getter
public class OrderResponseDto {

    private Long id; // 주문 ID
    private String contents; // 주문 취소 사유
    private OrderStatusEnum orderStatus; // 주문 상태
    private int quantity; // 메뉴 주문 수량

    public OrderResponseDto(Order order) {
        this.id = order.getId();
        this.contents = order.getContents();
        this.orderStatus = order.getOrderStatus();
        this.quantity = order.getQuantity();

    }
}
