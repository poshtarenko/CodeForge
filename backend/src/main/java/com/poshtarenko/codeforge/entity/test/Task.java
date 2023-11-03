package com.poshtarenko.codeforge.entity.test;

import com.poshtarenko.codeforge.entity.BaseEntity;
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
@Table(name = "Tasks")
public class Task extends BaseEntity {

    @Column(name = "note")
    String note;

    @Column(name = "max_score")
    Integer maxScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id")
    Test test;

    public Task(Long id) {
        super(id);
    }
}
