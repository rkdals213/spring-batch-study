package com.example.order.entity;

import com.example.generic.money.Money;
import lombok.Getter;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "ORDERS")
@Getter
public class Order {
    public enum OrderStatus {ORDERED, PAYED, DELIVERED}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Long id;

    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "ORDERED_TIME")
    private ZonedDateTime orderedTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private OrderStatus orderStatus;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "order", orphanRemoval = true
    )
    List<OrderEntry> entries;

    public Order() {
    }

    public Order(Long userId) {
        this.userId = userId;
        this.orderedTime = ZonedDateTime.now();
        this.orderStatus = OrderStatus.ORDERED;
    }

    public Money calculateTotalPrice() {
        return Money.sum(entries, OrderEntry::calculatePrice);
    }
}
