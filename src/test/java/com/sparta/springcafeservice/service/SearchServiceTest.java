package com.sparta.springcafeservice.service;

import com.sparta.springcafeservice.dto.SearchResponseDto;
import com.sparta.springcafeservice.entity.Menu;
import com.sparta.springcafeservice.entity.Store;
import com.sparta.springcafeservice.repository.MenuRepository;
import com.sparta.springcafeservice.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


class SearchServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private SearchService searchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("메뉴와 가게 이름으로 검색 성공")
    public void testSearchStoresAndMenusByKeyword_Success() {
        // given
        String keyword = "1";
        List<Menu> mockMenus = new ArrayList<>();
        List<Store> mockStores = new ArrayList<>();

        // Mock 객체 생성 및 설정
        Menu menu1 = mock(Menu.class);
        when(menu1.getMenuName()).thenReturn("Menu 1");
        Store store1 = mock(Store.class);
        when(store1.getId()).thenReturn(1L);
        when(store1.getStoreName()).thenReturn("Store 1");
        when(menu1.getStore()).thenReturn(store1);
        mockMenus.add(menu1);

        Menu menu2 = mock(Menu.class);
        when(menu2.getMenuName()).thenReturn("Menu 2");
        Store store2 = mock(Store.class);
        when(store2.getId()).thenReturn(2L);
        when(store2.getStoreName()).thenReturn("Store 2");
        when(menu2.getStore()).thenReturn(store2);
        mockMenus.add(menu2);

        when(menuRepository.findByMenuNameContaining(keyword)).thenReturn(mockMenus);
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

    @Test
    @DisplayName("메뉴와 가게 이름으로 검색 실패 - 데이터 없음")
    public void testSearchStoresAndMenusByKeyword_Failure_NoData() {
        // given
        String keyword = "nonexistent";
        List<Menu> mockMenus = new ArrayList<>();
        List<Store> mockStores = new ArrayList<>();

        when(menuRepository.findByMenuNameContaining(keyword)).thenReturn(mockMenus);
        when(storeRepository.findByStoreNameContaining(keyword)).thenReturn(mockStores);

        // when
        List<SearchResponseDto> responseDtos = searchService.searchStoresAndMenusByKeyword(keyword);

        // then
        assertEquals(0, responseDtos.size());
    }

}
