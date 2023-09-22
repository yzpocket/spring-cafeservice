package com.sparta.springcafeservice.dto;

import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class MenuRequestDto {

    private String menuName;
    private String image;
    private int price;
//    private Long storeId;
}
