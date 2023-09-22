package com.sparta.springcafeservice.dto;

import lombok.Getter;

@Getter
public class ReviewRequestDto {

    private byte star;
    private String review;
    private Long storeId;
}