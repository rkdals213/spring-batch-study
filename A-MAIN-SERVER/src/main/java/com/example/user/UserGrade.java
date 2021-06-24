package com.example.user;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "USER_GRADE")
@Getter
public class UserGrade {
    public enum Grade {C, B, A, S}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_GRADE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "SHOP_ID")
    private Long shopId;

    @Column(name = "GRADE")
    @Enumerated(EnumType.STRING)
    private Grade grade;

    public UserGrade() {
    }

    public UserGrade(User user, Long shopId, Grade grade) {
        this.user = user;
        this.shopId = shopId;
        this.grade = grade;
    }
}
