package com.sparta.springcafeservice.controller;

import com.sparta.springcafeservice.dto.SearchResponseDto;
import com.sparta.springcafeservice.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SearchController {
    private final SearchService searchService;

    // 통합버전
    @GetMapping("/search/stores-and-menus")
    public List<SearchResponseDto> searchStoresAndMenusByKeyword(@RequestParam("keyword") String keyword) {
        return searchService.searchStoresAndMenusByKeyword(keyword);
    }
}

    // 분리버전
    //// 메뉴 이름으로 검색하여 가게 정보 조회
    //@GetMapping("/search/stores-by-menu")
    //public List<SearchResponseDto> searchStoresByMenuKeyword(@RequestParam("keyword") String keyword) {
    //    List<SearchResponseDto> searchResponseDtos = searchService.searchStoresByMenuKeyword(keyword);
    //    return searchResponseDtos;
    //}
    //
    //// 가게 이름으로 검색하여 가게 정보 조회
    //@GetMapping("/search/stores-by-name")
    //public List<SearchResponseDto> searchStoresByNameKeyword(@RequestParam("keyword") String keyword) {
    //    List<SearchResponseDto> searchResponseDtos = searchService.searchStoresByNameKeyword(keyword);
    //    return searchResponseDtos;
    //}