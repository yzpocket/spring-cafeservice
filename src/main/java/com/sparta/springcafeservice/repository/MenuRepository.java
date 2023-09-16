package com.sparta.springcafeservice.repository;

import com.sparta.springcafeservice.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    Menu findByMenuName(String menuName);

}
