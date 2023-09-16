package com.sparta.springcafeservice.repository;

import com.sparta.springcafeservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderByModifiedAtDesc();

    Order findByUserId(Long userId);
}
