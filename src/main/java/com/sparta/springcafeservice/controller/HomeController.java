package com.sparta.springcafeservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "redirect:/index.html"; // 정적 HTML 파일로 리다이렉트
    }
}
