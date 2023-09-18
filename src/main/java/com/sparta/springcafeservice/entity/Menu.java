package com.sparta.springcafeservice.entity;

import com.sparta.springcafeservice.dto.MenuRequestDto;
import com.sparta.springcafeservice.repository.StoreRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@NoArgsConstructor
@Table(name = "menus")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String menuName;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Order order;


    public Menu(MenuRequestDto menuRequestDto) {
        this.menuName = menuRequestDto.getMenuName();
        this.image = menuRequestDto.getImage();
        this.price = menuRequestDto.getPrice();
    }

    public void update(MenuRequestDto menuRequestDto) {
        this.menuName = menuRequestDto.getMenuName();
        this.image = menuRequestDto.getImage();
        this.price = menuRequestDto.getPrice();
    }

    public void setStore(Store checkId) {
        this.store = checkId;
    }
}
