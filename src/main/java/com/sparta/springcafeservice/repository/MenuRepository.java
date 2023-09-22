package com.sparta.springcafeservice.repository;

import com.sparta.springcafeservice.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByMenuName(String menuName);
    List<Menu> findByMenuNameContaining(String keyword);
    List<Menu> findAllByStoreId(Long storeId);
}
