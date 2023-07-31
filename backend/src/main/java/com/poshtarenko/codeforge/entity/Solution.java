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
@Table(name = "Solutions")
public class Solution extends BaseEntity {

    @Column(name = "code")
    String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    Answer answer;

    @Column(name = "evaluation_time")
    Long evaluationTime;

    @Column(name = "is_completed")
    Boolean isCompleted;

    public Solution(Long id) {
        super(id);
    }
}
