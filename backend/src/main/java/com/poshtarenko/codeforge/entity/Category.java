package com.poshtarenko.codeforge.entity;

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
@Table(name = "Categories")
public class Category extends BaseEntity {

    @Column(name = "name")
    String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    List<Problem> problems;

    public Category(Long id) {
        super(id);
    }

    public Category(String name) {
        this.name = name;
    }
}
