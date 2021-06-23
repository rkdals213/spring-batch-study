package com.example;

import com.example.generic.money.Money;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PaymentDto {
    private Long userId;
    private Long shopId;
    private Long money;

    public PaymentDto() {
    }

    public PaymentDto(Long userId, Long shopId, Long money) {
        this.userId = userId;
        this.shopId = shopId;
        this.money = money;
    }
}
