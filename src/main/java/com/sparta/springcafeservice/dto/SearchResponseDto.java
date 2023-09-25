package com.sparta.springcafeservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class SearchResponseDto {

    private Long store_id;
    private String storeName; // 가게 이름
    private String storeAddress; // 가게 주소
    private String information; // 가게 정보
    private String type; // 결과의 유형 (예: "menu" 또는 "store")

    // 로그 출력용 toString() 오버라이드
    @Override
    public String toString() {
        return "SearchResponseDto{" +
                "store_id=" + store_id +
                ", storeName='" + storeName + '\'' +
                ", storeAddress='" + storeAddress + '\'' +
                ", information='" + information + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

}