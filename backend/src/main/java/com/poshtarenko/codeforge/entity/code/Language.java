package com.poshtarenko.codeforge.entity.code;

import com.poshtarenko.codeforge.entity.BaseEntity;
import com.poshtarenko.codeforge.entity.test.Problem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Languages")
public class Language extends BaseEntity {

    @Column(name = "name")
    String name;

    @OneToMany(mappedBy = "language", fetch = FetchType.LAZY)
    List<Problem> problems;

    public Language(String name) {
        this.name = name;
    }

    public Language(Long id) {
        super(id);
    }
}