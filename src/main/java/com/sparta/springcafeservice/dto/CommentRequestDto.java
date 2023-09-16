package com.sparta.springcafeservice.dto;

import lombok.Getter;

@Getter
//클라이언트가 댓글 달때 얻어올 Dto
public class CommentRequestDto {
    private byte star;
    private String comment;

    private Long storeId; // 가게 식별자
}