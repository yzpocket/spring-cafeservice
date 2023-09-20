package com.sparta.springcafeservice.service;

import com.sparta.springcafeservice.dto.SearchResponseDto;
import com.sparta.springcafeservice.entity.Menu;
import com.sparta.springcafeservice.entity.Store;
import com.sparta.springcafeservice.repository.MenuRepository;
import com.sparta.springcafeservice.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Transactional(readOnly = true)
    public List<SearchResponseDto> searchStoresByMenuKeyword(String keyword) {
        // 메뉴 이름에 키워드를 포함하는 메뉴를 검색
        List<Menu> menus = menuRepository.findByMenuNameContaining(keyword);

        // 메뉴를 가지고 있는 가게 정보를 얻음
        Set<Store> stores = menus.stream()
                .map(Menu::getStore)
                .filter(Objects::nonNull) // Null인 경우 제외
                .collect(Collectors.toSet());

        // SearchResponseDto로 변환
        List<SearchResponseDto> responseDtos = stores.stream()
                .map(store -> {
                    SearchResponseDto responseDto = new SearchResponseDto();
                    responseDto.setStoreName(store.getStoreName());
                    responseDto.setStoreAddress(store.getStoreAddress());
                    responseDto.setInformation(store.getInformation());
                    return responseDto;
                })
                .collect(Collectors.toList());

        return responseDtos;
    }

    @Transactional(readOnly = true)
    public List<SearchResponseDto> searchStoresByNameKeyword(String keyword) {
        // 가게 이름에 키워드를 포함하는 가게를 검색
        List<Store> stores = storeRepository.findByStoreNameContaining(keyword);

        // SearchResponseDto로 변환
        List<SearchResponseDto> responseDtos = stores.stream()
                .map(store -> {
                    SearchResponseDto responseDto = new SearchResponseDto();
                    responseDto.setStoreName(store.getStoreName());
                    responseDto.setStoreAddress(store.getStoreAddress());
                    responseDto.setInformation(store.getInformation());
                    return responseDto;
                })
                .collect(Collectors.toList());

        return responseDtos;
    }
}