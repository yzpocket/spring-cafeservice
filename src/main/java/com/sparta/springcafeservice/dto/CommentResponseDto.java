package com.sparta.springcafeservice.dto;

import com.sparta.springcafeservice.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// 클라이언트에 반환할 Dto
@Getter
@NoArgsConstructor
public class CommentResponseDto {  // response Dto
    private byte star;
    private String comment;


    // 리뷰 반환용 Dto 생성자
    public CommentResponseDto(Comment updatedComment) {
        this.star = updatedComment.getStar();
        this.comment = updatedComment.getComment();
    }
}