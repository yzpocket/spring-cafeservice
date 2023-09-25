package com.sparta.springcafeservice.repository;


import com.sparta.springcafeservice.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByStoreNameContaining(String keyword);

    Store findByBusinessNum(String checkBusinessNum);

    boolean existsByStoreName(String storeName);
}
