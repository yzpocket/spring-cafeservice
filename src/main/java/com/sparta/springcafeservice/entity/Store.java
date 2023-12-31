package com.sparta.springcafeservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.springcafeservice.dto.StoreRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity @Getter
@Table(name = "stores")
@NoArgsConstructor
public class Store extends TimeStamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "storeName", unique = true)
    private String storeName; // 가게 이름

//    @Column(name = "storeAddress")
//    private String storeAddress; // 가게 주소



    @Column(name = "information")
    private String information; //가게 정보

    @Column(name = "businessNum")
    private int businessNum; //사업자 번호


    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "store", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Menu> menuList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "store", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Review> reviewList = new ArrayList<>();

    @OneToOne(orphanRemoval = true)
    private StoreAddress storeAddress;



    public Store(StoreRequestDto requestDto, User user, StoreAddress storeAddress) {
        this.storeName = requestDto.getStoreName();
        this.information = requestDto.getInformation();
        this.businessNum = requestDto.getBusinessNum();
        this.user = user;

        this.storeAddress = storeAddress;
    }

    public void update(StoreRequestDto requestDto) {
        this.storeName = requestDto.getStoreName();
        this.information = requestDto.getInformation();

        this.storeAddress.updateAddress(requestDto.getPostNum(),
                                        requestDto.getCity(),
                                        requestDto.getDistrict(),
                                        requestDto.getNeighborhood());
    }

    public List<Menu> getMenuList() {
        return menuList;
    }
}
