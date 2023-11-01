package com.poshtarenko.codeforge.entity.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Respondents")
public class Respondent extends User {

    public Respondent(Long id) {
        super(id);
    }

    public Respondent(String email, String username, String password, List<Role> roles) {
        super(email, username, password, roles);
    }

}
