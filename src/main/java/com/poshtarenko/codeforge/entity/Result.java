package com.poshtarenko.codeforge.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Results")
public class Result extends BaseEntity {

    @Column(name = "score")
    Integer score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id")
    Test test;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "respondent_id")
    Respondent respondent;

    public Result(Long id) {
        super(id);
    }
}
