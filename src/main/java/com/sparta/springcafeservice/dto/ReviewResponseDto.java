package com.sparta.springcafeservice.dto;

import com.sparta.springcafeservice.entity.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewResponseDto {

    private byte star;

    private String review;


    public ReviewResponseDto(Review updatedReview) {
        this.star = updatedReview.getStar();
        this.review = updatedReview.getReview();
    }
}