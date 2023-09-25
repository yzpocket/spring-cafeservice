package com.sparta.springcafeservice.dto;

import com.sparta.springcafeservice.entity.Store;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;


@Getter
public class StoreResponseDto {

    private String storeName;
    private String storeAddress;
    private String information;

    private List<MenuResponseDto> menuResponseDtoList;
    private List<ReviewResponseDto> reviewResponseDtoList;

    public StoreResponseDto(Store store) {
        this.storeName = store.getStoreName();
        this.storeAddress = store.getStoreAddress();
        this.information = store.getInformation();
        this.menuResponseDtoList = store.getMenuList().stream().map(MenuResponseDto::new).collect(Collectors.toList());
        this.reviewResponseDtoList = store.getReviewList().stream().map(ReviewResponseDto::new).collect(Collectors.toList());

    }
}
