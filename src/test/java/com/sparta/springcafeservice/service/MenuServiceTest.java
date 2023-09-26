package com.sparta.springcafeservice.service;

import com.sparta.springcafeservice.dto.MenuRequestDto;
import com.sparta.springcafeservice.dto.MenuResponseDto;
import com.sparta.springcafeservice.dto.StatusResponseDto;
import com.sparta.springcafeservice.entity.Menu;
import com.sparta.springcafeservice.entity.Store;
import com.sparta.springcafeservice.entity.User;
import com.sparta.springcafeservice.repository.MenuRepository;
import com.sparta.springcafeservice.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class MenuServiceTest {
    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private User user;

    @Mock // Store도 모의 객체로 만듭니다.
    private Store virtualStore;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        initMocks(this);
        when(menuRepository.findByMenuName(anyString())).thenReturn(Collections.emptyList());
        when(menuRepository.save(any(Menu.class))).thenAnswer(i -> i.getArguments()[0]);


        Field storeIdField = Store.class.getDeclaredField("id");
        storeIdField.setAccessible(true);
        storeIdField.set(virtualStore, 1L); // 가상 가게 ID 설정

        when(user.getStore()).thenReturn(virtualStore);

        // 추가: virtualStore.getUser() 호출 시 user 모의 객체 반환
        when(virtualStore.getUser()).thenReturn(user);
    }


    @Test
    public void testCreateMenu_Success() throws NoSuchFieldException, IllegalAccessException {
        MenuRequestDto requestDto = new MenuRequestDto();

        // 리플렉션을 사용하여 private 필드에 값 설정
        Field menuNameField = MenuRequestDto.class.getDeclaredField("menuName");
        menuNameField.setAccessible(true);
        menuNameField.set(requestDto, "testMenu");

        Field imageField = MenuRequestDto.class.getDeclaredField("image");
        imageField.setAccessible(true);
        imageField.set(requestDto, "testImage");

        Field priceField = MenuRequestDto.class.getDeclaredField("price");
        priceField.setAccessible(true);
        priceField.set(requestDto, 10000);



        StatusResponseDto response = menuService.createMenu(requestDto, user);

        verify(menuRepository, times(1)).findByMenuName(anyString());

        assertEquals("메뉴를 등록했습니다.", response.getMsg());
        assertEquals(200, response.getStatuscode());

        System.out.println(response.getMsg());  // 성공 메시지 출력


    }

    @Test
    public void testCreateMenu_LoginRequired_Failure() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.createMenu(new MenuRequestDto(), null));
        assertEquals("로그인 해주세요!", exception.getMessage());
    }

    @Test
    public void testCreateMenu_StoreNotExist_Failure() {
        when(user.getStore()).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.createMenu(new MenuRequestDto(), user));
        assertEquals("해당 가게는 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    public void testCreateMenu_NotOwnerOfTheStore_Failure() {
        when(user.getId()).thenReturn(2L); // 다른 사용자 ID

        User storeUser = mock(User.class);
        when(storeUser.getId()).thenReturn(1L); // 가게 소유주의 ID

        when(virtualStore.getUser()).thenReturn(storeUser);

        Exception exception =  assertThrows(IllegalArgumentException.class,
                () -> menuService.createMenu(new MenuRequestDto(), user));
        assertEquals("가게 사장이 아닙니다.", exception.getMessage());

    }

    @Test
    public void testCreateMenu_DuplicateName_Failure() throws NoSuchFieldException, IllegalAccessException {
        List<Menu> existingMenus = new ArrayList<>();

        // Menu 객체 생성 및 필드 값 설정
        Menu menu = new Menu();

        Field menuNameField = Menu.class.getDeclaredField("menuName");
        menuNameField.setAccessible(true);
        menuNameField.set(menu, "test");

        Field imageField = Menu.class.getDeclaredField("image");
        imageField.setAccessible(true);
        imageField.set(menu, "image");

        Field priceField = Menu.class.getDeclaredField("price");
        priceField.setAccessible(true);
        priceField.setInt(menu, 10000);  // int 타입 필드에 대해서는 setInt() 메소드 사용

        existingMenus.add(menu);



        // 기본적으로 모든 문자열에 대해 빈 리스트를 반환하도록 설정
        when(menuRepository.findByMenuName(anyString())).thenReturn(Collections.emptyList());

        // "test"라는 이름일 경우만 existingMenus를 반환하도록 설정
        when(menuRepository.findByMenuName("test")).thenReturn(existingMenus);


        MenuRequestDto requestDto = new MenuRequestDto();

        Field requestMenuName = requestDto.getClass().getDeclaredField("menuName");
        requestMenuName.setAccessible(true);
        requestMenuName.set(requestDto, "test");


        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> menuService.createMenu(requestDto, user));
        assertEquals("중복된 메뉴이름입니다.", exception.getMessage());
    }

    //--------------------------------------------------------------------------- 메뉴조회

    @Test
    public void testGetMenus_Success() throws NoSuchFieldException, IllegalAccessException {

        Menu menu = new Menu();
        Field menuNameField = Menu.class.getDeclaredField("menuName");
        menuNameField.setAccessible(true);
        menuNameField.set(menu, "TestMenu");

        // FindAll이 호출되면 준비된 메뉴가 포함된 목록을 반환하도록 MenuRepository를 구성
        when(menuRepository.findAll()).thenReturn(Collections.singletonList(menu));

        /// getMenus를 호출하여 예상 이름을 가진 한 요소가 포함된 목록을 반환
        List<MenuResponseDto> result = menuService.getMenus();

        assertEquals(1, result.size());
        assertEquals("TestMenu", result.get(0).getMenuName());
    }

    @Test
    public void testGetMenus_Empty_Failure() {
        // 메뉴 조회는 실패 -> 빈 리스트 반환
        when(menuRepository.findAll()).thenReturn(new ArrayList<>());

        List<MenuResponseDto> result = menuService.getMenus();

        assertTrue(result.isEmpty());
    }


    //--------------------------------------------------------------------------- 메뉴수정

    @Test
    public void testUpdateMenu_Success() throws NoSuchFieldException, IllegalAccessException {
        User user = new User();
        Field idFieldUser = User.class.getDeclaredField("id");
        idFieldUser.setAccessible(true);
        idFieldUser.set(user , 1L);

        Store store = new Store();
        Field userStore= store.getClass().getDeclaredField("user");
        userStore.setAccessible(true);
        userStore.set(store , user);

        Field storeId= store.getClass().getDeclaredField("id");
        storeId.setAccessible(true);
        storeId.set(store , 1L);

        Field userId= user.getClass().getDeclaredField("store");
        userId.setAccessible(true);
        userId.set(user , store);

        MenuRequestDto requestDto= new MenuRequestDto();

        Menu menu= new Menu(requestDto,user.getStore());
        Long id= 1L;

        when(menuRepository.findById(id)).thenReturn(Optional.of(menu));

        StatusResponseDto responseDto = menuService.updateMenu(id,requestDto,user);

        assertEquals("메뉴가 수정되었습니다.", responseDto.getMsg());

    }

    @Test
    public void testUpdateMenu_Failure(){
        User notOwnerUser=new User();

        Long wrongId=-1L;

        Exception exception = assertThrows(IllegalArgumentException.class,
                () ->  menuService.updateMenu(wrongId,new MenuRequestDto(),notOwnerUser));

        assertEquals("해당 메뉴는 존재하지 않습니다.", exception.getMessage());
    }


    //--------------------------------------------------------------------------- 메뉴삭제

    @Test
    public void testDeleteMenu_Success() throws NoSuchFieldException, IllegalAccessException{
        User user = new User();
        Field idFieldUser = User.class.getDeclaredField("id");
        idFieldUser.setAccessible(true);
        idFieldUser.set(user , 1L);

        Store store = new Store();
        Field userStore= store.getClass().getDeclaredField("user");
        userStore.setAccessible(true);
        userStore.set(store , user);

        Field storeId= store.getClass().getDeclaredField("id");
        storeId.setAccessible(true);
        storeId.set(store , 1L);

        Field userId= user.getClass().getDeclaredField("store");
        userId.setAccessible(true);
        userId.set(user , store);

        Menu menu= new Menu(new MenuRequestDto(),user.getStore());
        Long id= 1L;

        when(menuRepository.findById(id)).thenReturn(Optional.of(menu));

        StatusResponseDto responseDto = menuService.deleteMenu(id,user);

        assertEquals("메뉴가 삭제되었습니다.", responseDto.getMsg());
    }

    @Test
    public void testDeleteMenu_Failure(){
        User notOwnerUser=new User();

        Long wrongId=-1L;

        Exception exception = assertThrows(IllegalArgumentException.class,
                () ->  menuService.deleteMenu(wrongId,notOwnerUser));

        assertEquals("해당 메뉴는 존재하지 않습니다.", exception.getMessage());
    }

}