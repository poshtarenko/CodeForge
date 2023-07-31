package com.poshtarenko.codeforge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Problems")
public class Problem extends BaseEntity {

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    Language language;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    Category category;

    @Column(name = "testing_code")
    String testingCode;

    @Column(name = "template_code")
    String templateCode;

    public Problem(Long id) {
        super(id);
    }
}
