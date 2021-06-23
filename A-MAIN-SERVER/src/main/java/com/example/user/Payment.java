package com.example.user;

import com.example.generic.money.Money;
import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PAYMENT")
@Getter
public class Payment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "USER_ID")
    private Long userId;

    @Id
    @Column(name = "SHOP_ID")
    private Long shopId;

    @Column(name = "TOTAL_PAYMENT")
    private Money totalPayment;

    public Payment() {
    }

    public Payment(Long userId, Long shopId, Money totalPayment) {
        this.userId = userId;
        this.shopId = shopId;
        this.totalPayment = totalPayment;
    }
}
