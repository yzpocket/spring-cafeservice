package com.sparta.springcafeservice.controller;

import com.sparta.springcafeservice.dto.LoginRequestDto;
import com.sparta.springcafeservice.dto.MenuRequestDto;
import com.sparta.springcafeservice.dto.MenuResponseDto;
import com.sparta.springcafeservice.dto.StatusResponseDto;
import com.sparta.springcafeservice.entity.User;
import com.sparta.springcafeservice.security.UserDetailsImpl;
import com.sparta.springcafeservice.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class MenuController {

    private final MenuService menuService;
//
//    // 메뉴 등록
//    @ResponseBody
//    @PostMapping("/{storeId}/menus")
//    public ResponseEntity<StatusResponseDto> createMenu(@PathVariable Long storeId,
//                                                        @RequestBody MenuRequestDto menuRequestDto,
//                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        return menuService.createMenu(storeId, menuRequestDto, userDetails.getUser());
//    }

    // 메뉴 등록 (파일 업로드 지원)
    // 메뉴 등록 (파일 업로드 지원)
    @PostMapping("/{storeId}/menus")
    public ResponseEntity<StatusResponseDto> createMenu(@PathVariable Long storeId,
                                                        @RequestParam("menuName") String menuName,
                                                        @RequestParam("price") String price,
                                                        @RequestParam("image") MultipartFile file,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            // MultipartFile을 사용하여 업로드된 파일을 처리
            byte[] imageBytes = file.getBytes();
            // 여기에서 업로드된 파일을 저장하거나 처리하는 로직을 추가할 수 있습니다.

            // 나머지 데이터를 사용하여 메뉴를 생성하고 저장하는 로직
            ResponseEntity<StatusResponseDto> response = menuService.createMenu(storeId, menuName, price, imageBytes, userDetails.getUser());

            return response;
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new StatusResponseDto("파일 업로드 중 오류가 발생했습니다.", 400));
        }
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
    public ResponseEntity<?> updateMenu(@PathVariable Long id, @RequestBody MenuRequestDto menuRequestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return menuService.updateMenu(id, menuRequestDto, userDetails.getUser());
    }

    // 메뉴 삭제
    @ResponseBody
    @DeleteMapping("/menus/{id}")
    public ResponseEntity<StatusResponseDto> deleteMenu(@PathVariable Long id, @RequestBody LoginRequestDto loginRequestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return menuService.deleteMenu(id, loginRequestDto.getPassword(), userDetails.getUser());
    }
}
