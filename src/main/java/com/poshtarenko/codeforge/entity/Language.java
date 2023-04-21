package com.poshtarenko.codeforge.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "Languages")
public class Language extends BaseEntity {

    @Column(name = "name")
    String name;

    @OneToMany(mappedBy = "language")
    List<Problem> problems;

    public Language(String name) {
        this.name = name;
    }

    public Language(Long id) {
        super(id);
    }
}
