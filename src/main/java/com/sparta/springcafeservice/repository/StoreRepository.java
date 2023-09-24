package com.sparta.springcafeservice.repository;


import com.sparta.springcafeservice.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByStoreNameContaining(String keyword);

    Optional<Store> findByBusinessNum(int businessNum);

//    Store findByBusinessNum(Integer checkBusinessNum);
}
