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
@Table(name = "Tests")
public class Test extends BaseEntity {

    @Column(name = "name")
    String name;

    @Column(name = "max_duration")
    Integer maxDuration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    Author author;

    @OneToMany(mappedBy = "test")
    List<Task> tasks;

    public Test(Long id) {
        super(id);
    }

    public Test(String name, Integer maxDuration, Author author) {
        this.name = name;
        this.maxDuration = maxDuration;
        this.author = author;
    }
}
