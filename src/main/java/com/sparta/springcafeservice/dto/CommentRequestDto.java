package com.sparta.springcafeservice.dto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
//클라이언트가 댓글 달때 얻어올 Dto
public class CommentRequestDto {
    private Long storeId;
    private Long userId;
    private byte star;
    private String comment;
}