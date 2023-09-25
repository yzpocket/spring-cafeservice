package com.sparta.springcafeservice.dto;


import com.sparta.springcafeservice.entity.Order;
import com.sparta.springcafeservice.entity.OrderStatusEnum;
import lombok.Getter;

@Getter
public class OrderResponseDto {

    private Long id; // 주문 ID
    private String contents; // 주문 취소 사유
    private OrderStatusEnum orderStatus; // 주문 상태

    public OrderResponseDto(Order order) {
        this.id = order.getId();
        this.contents = order.getContents();
        this.orderStatus = order.getOrderStatus();
    }

    // 로그 출력용 toString() 오버라이드
    @Override
    public String toString() {
        return "OrderResponseDto{" +
                "id=" + id +
                ", contents='" + contents + '\'' +
                ", orderStatus=" + orderStatus +
                '}';
    }

}
