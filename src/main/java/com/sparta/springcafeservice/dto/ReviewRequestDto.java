package com.sparta.springcafeservice.dto;

import lombok.Getter;

@Getter
//클라이언트가 댓글 달때 얻어올 Dto
public class ReviewRequestDto {
    private byte star;
    private String review;

    private Long storeId; // 가게 식별자
}