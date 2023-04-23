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
@Table(name = "Answers")
public class Answer extends BaseEntity {

    @Column(name = "code")
    String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "respondent_id")
    Respondent respondent;

    @Column(name = "evaluation_time")
    Long evaluationTime;

    @Column(name = "is_completed")
    Boolean isCompleted;

    public Answer(Long id) {
        super(id);
    }
}
