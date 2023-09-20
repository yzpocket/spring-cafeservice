package com.sparta.springcafeservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model) {
        // 모델에 데이터를 추가하여 템플릿에 전달
        model.addAttribute("message", "안녕하세요!");
        return "index"; // 뷰 이름은 templates 폴더에 있는 템플릿 파일명과 일치해야 합니다.
    }
    //@GetMapping("/")
    //public String index() {
    //    return "index"; // "index"는 templates 디렉터리에 있는 HTML 템플릿 파일의 이름입니다.
    //}
    @GetMapping("/login")
    public String login() {
        return "login"; // res/templates/login.html
    }
}
