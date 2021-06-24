package com.example.order.entity;

import com.example.generic.money.Money;
import com.example.shop.entity.Product;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "ORDER_ENTRY")
@Getter
public class OrderEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ENTRY_ID")
    private Long id;

    @Column(name = "QUANTITY")
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;

    public OrderEntry() {
    }

    public OrderEntry(int quantity, Order order, Product product) {
        this.quantity = quantity;
        this.order = order;
        this.product = product;
    }

    public Money calculatePrice() {
        return Money.wons(product.getPrice().doubleValue());
    }
}
