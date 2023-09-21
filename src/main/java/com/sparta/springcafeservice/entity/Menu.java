package com.sparta.springcafeservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.springcafeservice.dto.MenuRequestDto;
import com.sparta.springcafeservice.repository.StoreRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private int price;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    @OneToMany(mappedBy = "menu", orphanRemoval = true)
    List<Order> orderList = new ArrayList<>();


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
