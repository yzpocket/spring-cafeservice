package com.sparta.springcafeservice.entity;

public enum OrderStatusEnum {
    ORDER_CONFIRMATION, // 주문 완료(디폴트)

    DELIVERY_COMPLETED, //배달 완료
    CANCELED// 배달 삭제 -> 취소
}
