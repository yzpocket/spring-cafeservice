package com.sparta.springcafeservice.service;

import com.sparta.springcafeservice.dto.MenuRequestDto;
import com.sparta.springcafeservice.dto.MenuResponseDto;
import com.sparta.springcafeservice.dto.StatusResponseDto;
import com.sparta.springcafeservice.entity.Menu;
import com.sparta.springcafeservice.entity.User;
import com.sparta.springcafeservice.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    private final PasswordEncoder passwordEncoder;

    // 메뉴 등록
    @Transactional
    public ResponseEntity<StatusResponseDto> createMenu(MenuRequestDto menuRequestDto, User user) {
        Menu menu = new Menu(menuRequestDto);
        Menu checkMenu = menuRepository.findByMenuName(menu.getMenuName());

        // 사업자 구분
        if (user.getRegistNum() == null || user.getRegistNum().isEmpty()) {
            StatusResponseDto result = new StatusResponseDto("사업자가 아닙니다.", 400);
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }

        // 중복 메뉴 이름
        // entity unique에서 예외 터질 때 -> 예외 발생
        // 예외는 바디로 안보여서 서비스로직 작성
        if (checkMenu != null){
            StatusResponseDto result = new StatusResponseDto("중복된 메뉴 이름입니다.", 400);
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        menuRepository.save(menu);
        StatusResponseDto result = new StatusResponseDto("메뉴를 등록했습니다.", 200);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    // 메뉴 조회
    // readOnly = 데이터 변경 금지, 더티체킹 생략
    @Transactional(readOnly = true)
    public List<MenuResponseDto> getMenus() {
        List<Menu> menus = menuRepository.findAll();
        List<MenuResponseDto> responseDtos = new ArrayList<>();

        for (Menu menu : menus) {
            responseDtos.add(new MenuResponseDto(menu));
        }

        return responseDtos;
    }


    // 메뉴 선택 조회
    // 추후에는 키워드 기반으로 나온 메뉴 List로 변경
    public MenuResponseDto getMenu(Long id){
        Menu menu = findMenu(id);

        return new MenuResponseDto(menu);
    }


    // 메뉴 수정
    @Transactional
    public ResponseEntity<?> updateMenu(Long id, MenuRequestDto menuRequestDto, User user){
        Menu menu = findMenu(id);

        if (validateUserAuthority(user)) {
            menu.update(menuRequestDto);
            MenuResponseDto res = new MenuResponseDto(menu);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } else {
            StatusResponseDto res = new StatusResponseDto("사업자가 아닙니다.", 400);
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }
    }


    // 메뉴 삭제
    public ResponseEntity<StatusResponseDto> deleteMenu(Long id, String password, User user) {
        Menu menu = findMenu(id);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            StatusResponseDto res = new StatusResponseDto("비밀번호가 틀렸습니다.", 400);
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        } else {
            menuRepository.delete(menu);
            StatusResponseDto res = new StatusResponseDto("메뉴가 삭제되었습니다.", 400);
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
    }



    // id 찾기
    public Menu findMenu(Long id){
        return menuRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 메뉴는 존재하지 않습니다.")
        );
    }

    // User 체킹 -> 사업자 인지
    public boolean validateUserAuthority(User user) {
        if (!user.getRegistNum().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
