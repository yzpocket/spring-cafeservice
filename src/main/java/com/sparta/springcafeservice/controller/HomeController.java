package com.sparta.springcafeservice.controller;

import com.sparta.springcafeservice.dto.StoreAllResponseDto;
import com.sparta.springcafeservice.dto.StoreResponseDto;
import com.sparta.springcafeservice.entity.Menu;
import com.sparta.springcafeservice.entity.Review;
import com.sparta.springcafeservice.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final StoreService storeService;


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "안녕하세요!");
        return "index"; // 뷰 이름은 templates 폴더에 있는 템플릿 파일명과 일치해야 합니다.
    }

    // 로그인(+회원가입) 뷰페이지 렌더링
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // 리뷰 뷰페이지 렌더링
    @GetMapping("/stores/{storeId}/reviews")
    public String showReviewForm(@PathVariable Long storeId, Model model) {
        Review reviews = new Review();
        model.addAttribute("reviews", reviews);
        return "add-reviews";
    }


    // 가게 리스트 뷰페이지 렌더링 (index-contents레이어에 카드형태)
    @GetMapping("/stores")
    public String getAllStores(Model model) {
        List<StoreAllResponseDto> stores = storeService.getAllStores();
        model.addAttribute("stores", stores);
        return "index";
    }


    // 가게 상세정보 뷰페이지 렌더링
    @GetMapping("/stores/{storeId}")
    public String getStore(@PathVariable(name = "storeId") Long storeId, Model model) {
        StoreResponseDto storeResponse = storeService.getStore(storeId);
        model.addAttribute("store", storeResponse);
        return "getStore";
    }


    // 메뉴 뷰페이지 렌더링
    @GetMapping("/stores/{storeId}/menus")
    public String menus(@PathVariable Long storeId, Model model) {
        Menu menu = new Menu();
        model.addAttribute("menu", menu);
        return "add-menus";
    }
}
