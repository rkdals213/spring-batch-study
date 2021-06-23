package com.example.shop.entity;

import com.example.generic.money.Money;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCT")
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID")
    private Long id;

    @Column(name = "SHOP_ID")
    private Long shopId;

    @Column(name = "LABEL")
    String label;

    @Column(name = "PRICE")
    private Money price;

    public Product() {
    }

    public Product(Long shopId, String label, Money price) {
        this.shopId = shopId;
        this.label = label;
        this.price = price;
    }
}
