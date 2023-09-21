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

    private final StoreService storeService; // 서비스 클래스 주입

    @GetMapping("/")
    public String home(Model model) {
        // 모델에 데이터를 추가하여 템플릿에 전달
        model.addAttribute("message", "안녕하세요!");
        return "index"; // 뷰 이름은 templates 폴더에 있는 템플릿 파일명과 일치해야 합니다.
    }

    @GetMapping("/login")
    public String login() {
        return "login"; // res/templates/login.html
    }

    @GetMapping("/add-reviews")
    public String reviews(Model model) {
        Review review = new Review(); // 새로운 Review 객체를 생성합니다. 필요에 따라 다른 로직으로 Menu 객체를 가져올 수도 있습니다.
        model.addAttribute("review", review); // 모델에 Menu 객체를 추가합니다.
        return "add-reviews";
    }

    // 리뷰 뷰페이지 렌더링
    @GetMapping("/stores/{storeId}/reviews")
    public String showReviewForm(@PathVariable Long storeId, Model model) {
        Review reviews = new Review(); // 새로운 Review 객체를 생성합니다. 필요에 따라 다른 로직으로 Menu 객체를 가져올 수도 있습니다.
        model.addAttribute("reviews", reviews);
        return "add-reviews";
    }

    @GetMapping("/stores")
    public String getAllStores(Model model) {
        List<StoreAllResponseDto> stores = storeService.getAllStores();
        model.addAttribute("stores", stores);
        return "index";
    }

    @GetMapping("/stores/{storeId}")
    public String getStore(@PathVariable(name = "storeId") Long storeId, Model model) {
        StoreResponseDto storeResponse = storeService.getStore(storeId);
        model.addAttribute("store", storeResponse);
        return "getStore";
    }



    @GetMapping("/stores/{storeId}/menus")
    public String menus(@PathVariable Long storeId, Model model) {
        Menu menu = new Menu(); // 새로운 Menu 객체를 생성합니다. 필요에 따라 다른 로직으로 Menu 객체를 가져올 수도 있습니다.
        model.addAttribute("menu", menu); // 모델에 Menu 객체를 추가합니다.
        return "add-menu";
    }
}
