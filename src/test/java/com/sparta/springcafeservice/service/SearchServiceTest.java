package com.sparta.springcafeservice.service;

import com.sparta.springcafeservice.dto.MenuRequestDto;
import com.sparta.springcafeservice.dto.SearchResponseDto;
import com.sparta.springcafeservice.entity.Menu;
import com.sparta.springcafeservice.entity.Store;
import com.sparta.springcafeservice.entity.User;
import com.sparta.springcafeservice.repository.MenuRepository;
import com.sparta.springcafeservice.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private SearchService searchService;

    @Test
    @DisplayName("메뉴와 가게 이름으로 검색 성공")
    public void testSearchStoresAndMenusByKeyword_Success() {
        // given
        String keyword = "keyword";
        List<Menu> mockMenus = new ArrayList<>();
        List<Store> mockStores = new ArrayList<>();

        User user = new User();

        // 가짜 메뉴와 가게 데이터를 생성하고 Mock 설정
        Menu menu1 = new Menu(new MenuRequestDto(), )
        when(menu1.getMenuName()).thenReturn("Menu 1");
        mockMenus.add(menu1);

        Menu menu2 = mock(Menu.class);
        when(menu2.getMenuName()).thenReturn("Menu 2");
        mockMenus.add(menu2);

        when(menuRepository.findByMenuNameContaining(keyword)).thenReturn(mockMenus);

        Store store1 = mock(Store.class);
        when(store1.getStoreName()).thenReturn("Store 1");
        mockStores.add(store1);

        Store store2 = mock(Store.class);
        when(store2.getStoreName()).thenReturn("Store 2");
        mockStores.add(store2);

        when(storeRepository.findByStoreNameContaining(keyword)).thenReturn(mockStores);

        // when
        List<SearchResponseDto> responseDtos = searchService.searchStoresAndMenusByKeyword(keyword);

        // then
        assertEquals(mockMenus.size() + mockStores.size(), responseDtos.size());

        // 메뉴 검색 결과 확인
        for (Menu menu : mockMenus) {
            assertTrue(responseDtos.stream().anyMatch(dto -> dto.getType().equals("menu")
                    && dto.getStoreName().equals(menu.getStore().getStoreName())));
        }

        // 가게 검색 결과 확인
        for (Store store : mockStores) {
            assertTrue(responseDtos.stream().anyMatch(dto -> dto.getType().equals("store")
                    && dto.getStoreName().equals(store.getStoreName())));
        }
    }
}
