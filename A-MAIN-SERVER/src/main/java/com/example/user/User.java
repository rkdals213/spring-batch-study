package com.example.user;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "USERS")
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "LABEL")
    String label;

    public User() {
    }

    public User(String label) {
        this.label = label;
    }
}
