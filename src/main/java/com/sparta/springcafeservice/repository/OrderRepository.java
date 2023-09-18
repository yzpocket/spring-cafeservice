package com.sparta.springcafeservice.repository;

import com.sparta.springcafeservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByOrderByModifiedAtDesc();

    Order findByUserId(Long userId);
}
