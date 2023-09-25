package com.sparta.springcafeservice.dto;

import com.sparta.springcafeservice.entity.StoreAddress;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class SearchResponseDto {

    private Long store_id;
    private String storeName; // 가게 이름

    private String information; // 가게 정보
    private String type; // 결과의 유형 (예: "menu" 또는 "store")


    private StoreAddress storeAddress; // 가게 주소
}