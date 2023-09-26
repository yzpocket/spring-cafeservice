package com.sparta.springcafeservice.repository;

import com.sparta.springcafeservice.entity.Menu;
import com.sparta.springcafeservice.entity.Order;
import com.sparta.springcafeservice.entity.Store;
import com.sparta.springcafeservice.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    private User user;
    private Store store;
    private Menu menu;

    @BeforeEach
    void setUp() {
        // 필요한 엔티티를 미리 생성하여 entityManager를 통해 저장합니다.
        user = new User();
        entityManager.persist(user);

        store = new Store();
        entityManager.persist(store);

        menu = new Menu(store); // Menu 엔티티 생성 시에 store 정보를 주입합니다.
        entityManager.persist(menu);
    }


    @Test
    void findAllByUserIdOrderByModifiedAtDesc() {
        // given
        Order order1 = new Order();
        entityManager.persist(order1);

        Order order2 = new Order();
        entityManager.persist(order2);

        // when
        List<Order> orders = orderRepository.findAllByUserIdOrderByModifiedAtDesc(user.getId());

        // then
        assertThat(orders).hasSize(2); // 예상대로 두 개의 주문이 조회되어야 합니다.
    }

    @Test
    void findAllByMenu_Store_IdOrderByModifiedAtDesc() {
        // given
        Order order1 = new Order();
        entityManager.persist(order1);

        Order order2 = new Order();
        entityManager.persist(order2);

        // when
        List<Order> orders = orderRepository.findAllByMenu_Store_IdOrderByModifiedAtDesc(store.getId());

        // then
        assertThat(orders).hasSize(2); // 예상대로 두 개의 주문이 조회되어야 합니다.
    }
}