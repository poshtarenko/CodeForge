package com.poshtarenko.codeforge.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

    @OneToMany(mappedBy = "category")
    List<Problem> problems;

    public Category(Long id) {
        super(id);
    }

    public Category(String name) {
        this.name = name;
    }
}
