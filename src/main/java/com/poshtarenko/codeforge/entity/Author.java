package com.poshtarenko.codeforge.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Authors")
public class Author extends User {

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    List<Test> tests;

    public Author(Long id) {
        super(id);
    }

    public Author(String email, String username, String password, List<Role> roles) {
        super(email, username, password, roles);
    }
}
