package com.sparta.springcafeservice.dto;

import lombok.Getter;

@Getter
public class ReviewRequestDto {

    private byte star;

    private String review;

    private Long storeId;

    @Override
    public String toString() {
        return "ReviewRequestDto{" +
                "star='" + star + '\'' +
                ", review='" + review + '\'' +
                '}';
    }
}