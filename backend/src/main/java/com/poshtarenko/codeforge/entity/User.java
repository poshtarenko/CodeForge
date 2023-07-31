package com.poshtarenko.codeforge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends BaseEntity {
    @Column(name = "email", unique = true)
    String email;

    String username;

    String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    List<Role> roles;

    public User(Long id) {
        super(id);
    }

}