package com.poshtarenko.codeforge.entity.lesson;

import com.poshtarenko.codeforge.entity.BaseEntity;
import com.poshtarenko.codeforge.entity.code.EvaluationResult;
import com.poshtarenko.codeforge.entity.test.SolutionResult;
import com.poshtarenko.codeforge.entity.user.Respondent;
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
@Table(name = "Participations")
public class Participation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "respondent_id")
    Respondent respondent;

    @Column(name = "code")
    String code;

    @Embedded
    EvaluationResult evaluationResult;

    public Participation(Long id) {
        super(id);
    }

}