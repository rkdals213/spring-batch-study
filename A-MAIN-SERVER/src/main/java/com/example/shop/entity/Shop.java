package com.example.shop.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "SHOP")
@Getter
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SHOP_ID")
    private Long id;

    @Column(name = "LABEL")
    String label;

    public Shop() {
    }

    public Shop(String label) {
        this.label = label;
    }
}
