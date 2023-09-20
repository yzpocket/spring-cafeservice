package com.sparta.springcafeservice.controller;

import com.sparta.springcafeservice.entity.Menu;
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

    @GetMapping("/getStore")
    public String stores(Model model) {
        model.addAttribute("message", "안녕하세요!");
        return "getStore"; // "index"는 templates 디렉터리에 있는 HTML 템플릿 파일의 이름입니다.
    }

    @GetMapping("/add-menu")
    public String menus(Model model) {
        Menu menu = new Menu(); // 새로운 Menu 객체를 생성합니다. 필요에 따라 다른 로직으로 Menu 객체를 가져올 수도 있습니다.
        model.addAttribute("menu", menu); // 모델에 Menu 객체를 추가합니다.
        return "add-menu";
    }



    //@GetMapping("/index")
    //public String index() {
    //    return "index"; // "index"는 templates 디렉터리에 있는 HTML 템플릿 파일의 이름입니다.
    //}
    //@GetMapping("/menu")
    //public String menu() {
    //    return "menu"; // "index"는 templates 디렉터리에 있는 HTML 템플릿 파일의 이름입니다.
    //}
    //@GetMapping("/services")
    //public String service() {
    //    return "services"; // "index"는 templates 디렉터리에 있는 HTML 템플릿 파일의 이름입니다.
    //}

    //
    //@GetMapping("/about")
    //public String about() {
    //    return "about"; // "index"는 templates 디렉터리에 있는 HTML 템플릿 파일의 이름입니다.
    //}
    //
    //@GetMapping("/contact")
    //public String contact() {
    //    return "contact"; // "index"는 templates 디렉터리에 있는 HTML 템플릿 파일의 이름입니다.
    //}
}
