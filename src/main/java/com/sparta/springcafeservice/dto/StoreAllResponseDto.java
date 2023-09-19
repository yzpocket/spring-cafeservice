package com.sparta.springcafeservice.dto;

import com.sparta.springcafeservice.entity.Store;
import lombok.Getter;



@Getter
public class StoreAllResponseDto {
    private Long id;

    private String storeName;
    private String storeAddress;
    private String information;

//    private List<MenuResponseDto> menuResponseDtoList;

    public StoreAllResponseDto(Store store) {
        this.storeName = store.getStoreName();
        this.storeAddress = store.getStoreAddress();
        this.information = store.getInformation();
    }
    public Long getId() {
        return id;
    }
}
