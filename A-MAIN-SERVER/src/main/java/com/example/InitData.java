package com.example;

import com.example.generic.money.Money;
import com.example.order.entity.Order;
import com.example.order.entity.OrderEntry;
import com.example.shop.entity.Product;
import com.example.shop.entity.Shop;
import com.example.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@RequiredArgsConstructor
public class InitData {
    private final InitService initService;

//    @PostConstruct
//    public void init() {
//        initService.init();
//    }

    @Component
    static class InitService {
        @PersistenceContext
        EntityManager em;

        @Transactional
        public void init() {
            Shop shop1 = new Shop("맥도날드");
            Shop shop2 = new Shop("버거킹");
            em.persist(shop1);
            em.persist(shop2);

            User user1 = new User("손님1");
            User user2 = new User("손님2");
            em.persist(user1);
            em.persist(user2);

            Product product1 = new Product(shop1.getId(), "빅맥", Money.wons(6900));
            Product product2 = new Product(shop1.getId(), "베이컨 토마토 디럭스", Money.wons(7000));
            Product product3 = new Product(shop1.getId(), "스낵랩", Money.wons(3400));
            Product product4 = new Product(shop2.getId(), "치즈 와퍼", Money.wons(5400));
            Product product5 = new Product(shop2.getId(), "쉬림프 와퍼", Money.wons(6300));
            Product product6 = new Product(shop2.getId(), "콜라", Money.wons(1500));
            em.persist(product1);
            em.persist(product2);
            em.persist(product3);
            em.persist(product4);
            em.persist(product5);
            em.persist(product6);
            System.out.println(user1.getId());

            Order order1 = new Order(user1.getId());
            em.persist(order1);
            OrderEntry orderEntry1 = new OrderEntry(1, order1, product1);
            OrderEntry orderEntry2 = new OrderEntry(1, order1, product2);
            OrderEntry orderEntry3 = new OrderEntry(1, order1, product3);
            em.persist(orderEntry1);
            em.persist(orderEntry2);
            em.persist(orderEntry3);

            for (int i = 0; i < 500; i++) {
                Order order2 = new Order(user1.getId());
                em.persist(order2);
                OrderEntry orderEntry4 = new OrderEntry(1, order2, product4);
                OrderEntry orderEntry5 = new OrderEntry(1, order2, product5);
                OrderEntry orderEntry6 = new OrderEntry(1, order2, product6);

                em.persist(orderEntry4);
                em.persist(orderEntry5);
                em.persist(orderEntry6);
            }

            for (int i = 0; i < 500; i++) {
                Order order2 = new Order(user1.getId());
                em.persist(order2);
                OrderEntry orderEntry4 = new OrderEntry(1, order2, product1);
                OrderEntry orderEntry5 = new OrderEntry(1, order2, product2);

                em.persist(orderEntry4);
                em.persist(orderEntry5);
            }


            for (int i = 0; i < 500; i++) {
                Order order3 = new Order(user2.getId());
                em.persist(order3);
                OrderEntry orderEntry7 = new OrderEntry(1, order3, product1);
                OrderEntry orderEntry8 = new OrderEntry(1, order3, product3);

                em.persist(orderEntry7);
                em.persist(orderEntry8);
            }

            for (int i = 0; i < 500; i++) {
                Order order3 = new Order(user2.getId());
                em.persist(order3);
                OrderEntry orderEntry7 = new OrderEntry(1, order3, product5);
                OrderEntry orderEntry8 = new OrderEntry(1, order3, product6);

                em.persist(orderEntry7);
                em.persist(orderEntry8);
            }

        }
    }
}
