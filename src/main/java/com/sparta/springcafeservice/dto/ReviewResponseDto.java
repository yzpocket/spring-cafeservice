package com.sparta.springcafeservice.dto;

import com.sparta.springcafeservice.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 클라이언트에 반환할 Dto
@Getter
@NoArgsConstructor
public class ReviewResponseDto {  // response Dto
    private byte star;
    private String review;


    // 리뷰 반환용 Dto 생성자
    public ReviewResponseDto(Review updatedReview) {
        this.star = updatedReview.getStar();
        this.review = updatedReview.getReview();
    }
}