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
import java.util.Optional;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;


    // 메뉴 등록
    @Transactional
    public ResponseEntity<StatusResponseDto> createMenu(MenuRequestDto menuRequestDto, User user) {
       return handleServiceRequest(()->{

           Menu menu = new Menu(menuRequestDto, user.getStore());
           List<Menu> checkMenus = menuRepository.findByMenuName(menu.getMenuName());
           List<User> authorRegistNum = userRepository.findByRegistNum(user.getRegistNum());

           // 가게 소유 여부 체크
           if (user.getStore() == null) {
               throw new IllegalArgumentException("사용자의 가게 정보가 없습니다.");
           }
           Optional<Store> storeCheck = storeRepository.findById(user.getStore().getId());

           // 가게 체크
           if ( storeCheck == null || !storeCheck.isPresent()) {
               throw new IllegalArgumentException("해당 가게는 존재하지 않습니다.");
           }

           // null값으로 구분
           if (user.getRegistNum() == null || user.getRegistNum() == 0) {
               throw new IllegalArgumentException("사업자가 아닙니다.");
           }
           // 사업자 번호 중복체크
           if (authorRegistNum.isEmpty() || !user.getRegistNum().equals(authorRegistNum.get(0).getRegistNum())) {
               throw new IllegalArgumentException("사업자 번호가 유효하지 않습니다.");
           }

           // 중복 메뉴 이름
           if (!checkMenus.isEmpty()){
               throw new IllegalArgumentException("중복된 메뉴이름입니다.");
           }

           menuRepository.save(menu);
           return new StatusResponseDto("메뉴를 등록했습니다.", 200);
       });
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
    public MenuResponseDto getMenu(Long id){
        Menu menu = checkMenuExist(id);

        return new MenuResponseDto(menu);
    }


    // 메뉴 수정
    @Transactional
    public ResponseEntity<StatusResponseDto> updateMenu(Long id, MenuRequestDto menuRequestDto, User user){
        return handleServiceRequest(()->{

            Menu menu = checkMenuExist(id);

            if (!validateUserAuthority(user)) {
                throw new IllegalArgumentException("사업자가 아닙니다.");
            } else {
                menu.update(menuRequestDto);
                MenuResponseDto res = new MenuResponseDto(menu);
            }
            return new StatusResponseDto("메뉴가 수정되었습니다.", 200);
        });
    }


    // 메뉴 삭제
    @Transactional
    public ResponseEntity<StatusResponseDto> deleteMenu(Long id, User user) {
        return handleServiceRequest(() -> {

            Menu menu = checkMenuExist(id);
            if(!validateUserAuthority(user)){
                throw new IllegalArgumentException("잘못된 접근 입니다.");
            }else{
                menuRepository.delete(menu);
            }
            return new StatusResponseDto("메뉴가 삭제되었습니다.", 200);
        });
    }



    // 메뉴 찾기(id)
    public Menu checkMenuExist(Long id){
        return menuRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 메뉴는 존재하지 않습니다.")
        );
    }

    // 사업자 체크
    public boolean validateUserAuthority(User user) {
        if (user.getRegistNum() != 0) {
            return true;
        } else {
            return false;
        }
    }

    // 중복 코드 제거를 위한 메소드
    private ResponseEntity<StatusResponseDto> handleServiceRequest(Supplier<StatusResponseDto> action) {
        try {
            return new ResponseEntity<>(action.get(), HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(new StatusResponseDto(ex.getMessage(), 400), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(new StatusResponseDto("서비스 요청 중 오류가 발생했습니다.", 500), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
