package com.example.user;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "USERS")
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "LABEL")
    private String label;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true
    )
    private List<UserGrade> userGrades;

    public User() {
    }

    public User(String label) {
        this.label = label;
    }
}
