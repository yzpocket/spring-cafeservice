package com.sparta.springcafeservice.service;

import com.sparta.springcafeservice.dto.MenuRequestDto;
import com.sparta.springcafeservice.dto.MenuResponseDto;
import com.sparta.springcafeservice.dto.StatusResponseDto;
import com.sparta.springcafeservice.entity.Menu;
import com.sparta.springcafeservice.entity.Store;
import com.sparta.springcafeservice.entity.User;
import com.sparta.springcafeservice.repository.MenuRepository;
import com.sparta.springcafeservice.repository.StoreRepository;
import com.sparta.springcafeservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;


    // 메뉴 등록
    @Transactional
    public StatusResponseDto createMenu(MenuRequestDto menuRequestDto, User user) {
        //로그인 확인
        if (user == null) {
            throw new IllegalArgumentException("로그인 해주세요!");
        }

        Menu menu = new Menu(menuRequestDto, user.getStore()); // 가게 메뉴 생성
        List<Menu> checkMenus = menuRepository.findByMenuName(menu.getMenuName()); // 등록된 메뉴 이름으로 불러오기
        Store userStore = user.getStore();

        // 가게 유무 체크
        if (userStore == null) {
            throw new IllegalArgumentException("해당 가게는 존재하지 않습니다.");
        }
        // 가게 소유 여부 체크
        if (user.getStore() == null) {
            throw new IllegalArgumentException("사용자의 가게 정보가 없습니다.");
        }
        // 가게 사장인지 확인(가게의 userId와 사용자 id 비교)
        if (!checkStoreOwner(user)) {
            throw new IllegalArgumentException("가게 사장이 아닙니다.");
        }
        // 중복 메뉴 이름
        if (!checkMenus.isEmpty()) {
            throw new IllegalArgumentException("중복된 메뉴이름입니다.");
        }

        menuRepository.save(menu);
        return new StatusResponseDto("메뉴를 등록했습니다.", 200);
    }


    // 메뉴 조회
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
    public MenuResponseDto getMenu(Long id) {
        Menu menu = checkMenuExist(id);

        return new MenuResponseDto(menu);
    }


    // 메뉴 수정
    @Transactional
    public StatusResponseDto updateMenu(Long id, MenuRequestDto menuRequestDto, User user) {
        Menu menu = checkMenuExist(id);

        //사용자가 사장인지 확인
        if (!checkStoreOwner(user)) {
            throw new IllegalArgumentException("사업자가 아닙니다.");
        } else {
            menu.update(menuRequestDto);
            MenuResponseDto res = new MenuResponseDto(menu);
        }
        return new StatusResponseDto("메뉴가 수정되었습니다.", 200);
    }


    // 메뉴 삭제
    @Transactional
    public StatusResponseDto deleteMenu(Long id, User user) {
        Menu menu = checkMenuExist(id);

        // 사용자가 가게 사장인지 확인
        if (!checkStoreOwner(user)) {
            throw new IllegalArgumentException("잘못된 접근 입니다.");
        } else {
            menuRepository.delete(menu);
        }
        return new StatusResponseDto("메뉴가 삭제되었습니다.", 200);
    }

    // 메뉴 찾기(id)
    public Menu checkMenuExist(Long id) {
        return menuRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 메뉴는 존재하지 않습니다.")
        );
    }

    // 사장인지 아닌지 확인 -> 사장일 경우 true
    private boolean checkStoreOwner(User user) {
        return user.getStore() != null && user.getId().equals(user.getStore().getUser().getId());
    }
}
