package com.sparta.springcafeservice.dto;

import com.sparta.springcafeservice.entity.Store;
import com.sparta.springcafeservice.entity.StoreAddress;
import lombok.Getter;



@Getter
public class StoreAllResponseDto {

    private Long id;
    private String storeName;
    private String information;

    private StoreAddress storeAddress;

    public StoreAllResponseDto(Store store) {
        this.id = store.getId();
        this.storeName = store.getStoreName();
        this.information = store.getInformation();

        this.storeAddress = new StoreAddress(store.getStoreAddress().getPostNum(),
                                             store.getStoreAddress().getCity(),
                                             store.getStoreAddress().getDistrict(),
                                             store.getStoreAddress().getNeighborhood());
    }
    public Long getId() {
        return id;
    }
}


