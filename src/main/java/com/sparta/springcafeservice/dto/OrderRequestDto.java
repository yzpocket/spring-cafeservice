package com.sparta.springcafeservice.dto;

import com.sparta.springcafeservice.entity.OrderStatusEnum;
import lombok.Getter;

@Getter
public class OrderRequestDto {
    private Long menuId;
    private Long storeId;
    private Long userId;

    private String contents;
    private OrderStatusEnum orderStatus;
}
