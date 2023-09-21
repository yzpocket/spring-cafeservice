package com.sparta.springcafeservice.entity;

import com.sparta.springcafeservice.dto.MenuRequestDto;
import com.sparta.springcafeservice.repository.StoreRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Entity @Getter
@NoArgsConstructor
@Table(name = "menus")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String menuName;

//    @Column(nullable = false)
//    private String image;

    @Column(nullable = false)
    private String imagePath; // 이미지 경로 저장


    @Column(nullable = false)
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @OneToMany(mappedBy = "menu", orphanRemoval = true)
    List<Order> orderList = new ArrayList<>();


    public Menu(MenuRequestDto menuRequestDto) {
        this.menuName = menuRequestDto.getMenuName();
        this.imagePath = menuRequestDto.getImage();
        this.price = menuRequestDto.getPrice();
    }

//    public Menu(String menuName, String price, byte[] imageBytes) {
//        this.menuName = menuName;
//        this.price = Integer.parseInt(price); // 문자열을 정수로 변환
//        this.image = Base64.getEncoder().encodeToString(imageBytes); // 바이트 배열을 Base64로 인코딩하여 저장
//    }
//

    public Menu(String menuName, String price, String imagePath) {
        this.menuName = menuName;
        this.price = Integer.parseInt(price); // 문자열을 정수로 변환
        this.imagePath = imagePath;
    }


    public void update(MenuRequestDto menuRequestDto) {
        this.menuName = menuRequestDto.getMenuName();
        this.imagePath = menuRequestDto.getImage();
        this.price = menuRequestDto.getPrice();
    }

    public void setStore(Store checkId) {
        this.store = checkId;
    }
}
