package com.sparta.springcafeservice.entity;


import com.sparta.springcafeservice.dto.OrderRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity @Getter
@NoArgsConstructor
@Table(name = "orders")
public class Order extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String contents; //주문 취소 사유

    @Column
    @Enumerated(value = EnumType.STRING)
    private OrderStatusEnum orderStatus;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "int default 1")
    private int quantity;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime orderDate;

    public Order(OrderRequestDto requestDto, User user, Menu menu) {
        this.menu = menu;
        this.user = user;
        this.contents = requestDto.getContents();
        this.orderStatus = OrderStatusEnum.ORDER_CONFIRMATION;
        this.orderDate = LocalDateTime.now(); //주문 시간(생성일자, 수정일자와는 다릅니다.)
        this.quantity = requestDto.getQuantity();

    }

    public void update(OrderRequestDto requestDto) {
        this.orderStatus = requestDto.getOrderStatus();
    }

    public void setOrderStatus(OrderStatusEnum orderStatus) {
        this.orderStatus = orderStatus;
    }
}
