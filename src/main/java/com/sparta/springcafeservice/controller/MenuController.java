package com.sparta.springcafeservice.controller;

import com.sparta.springcafeservice.dto.MenuRequestDto;
import com.sparta.springcafeservice.dto.MenuResponseDto;
import com.sparta.springcafeservice.dto.StatusResponseDto;
import com.sparta.springcafeservice.security.UserDetailsImpl;
import com.sparta.springcafeservice.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class MenuController {

    private final MenuService menuService;

    // 메뉴 등록
    @ResponseBody
    @PostMapping("/menus")
    public StatusResponseDto createMenu(@RequestBody MenuRequestDto menuRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return menuService.createMenu(menuRequestDto, userDetails.getUser());
    }

    // 메뉴 조회
    @ResponseBody
    @GetMapping("/menus")
    public List<MenuResponseDto> getMenus() {
        return menuService.getMenus();
    }

    // 선택한 메뉴 조회
    @ResponseBody
    @GetMapping("/menus/{id}")
    public MenuResponseDto getMenu(@PathVariable Long id) {
        return menuService.getMenu(id);
    }

    // 메뉴 수정
    @ResponseBody
    @PutMapping("/menus/{id}")
    public StatusResponseDto updateMenu(@PathVariable Long id,
                                        @RequestBody MenuRequestDto menuRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return menuService.updateMenu(id, menuRequestDto, userDetails.getUser());
    }

    // 메뉴 삭제
    @ResponseBody
    @DeleteMapping("/menus/{id}")
    public StatusResponseDto deleteMenu(@PathVariable Long id,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return menuService.deleteMenu(id, userDetails.getUser());
    }

}
