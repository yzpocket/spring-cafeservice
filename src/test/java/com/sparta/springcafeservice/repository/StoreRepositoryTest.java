package com.sparta.springcafeservice.repository;

import com.sparta.springcafeservice.entity.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StoreRepositoryTest {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private TestEntityManager testEntityManager;

//    @Test
//    @DisplayName("가게이름으로 찾기")
//    void findByStoreNameContaining() {
//        Store store = new Store("store1");
//        testEntityManager.persist(store);
//        testEntityManager.flush();
//
//        List<Store> foundStores = storeRepository.findByStoreNameContaining("store");
//
//        assertFalse(foundStores.isEmpty());
//        assertEquals("store", foundStores.get(0).getStoreName());
//
//    }

    @Test
    void findByBusinessNum() {
    }

    @Test
    void existsByStoreName() {
    }
}