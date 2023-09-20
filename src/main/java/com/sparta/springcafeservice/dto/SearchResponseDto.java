package com.sparta.springcafeservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchResponseDto {
    private String storeName; // 가게 이름
    private String storeAddress; // 가게 주소
    private String information; // 가게 정보
}