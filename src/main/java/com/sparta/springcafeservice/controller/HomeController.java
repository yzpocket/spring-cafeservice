package com.sparta.springcafeservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "index"; // "index"는 templates 디렉터리에 있는 HTML 템플릿 파일의 이름입니다.
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
    //@GetMapping("/blog")
    //public String blog() {
    //    return "blog"; // "index"는 templates 디렉터리에 있는 HTML 템플릿 파일의 이름입니다.
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
