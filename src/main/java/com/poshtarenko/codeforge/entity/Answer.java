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

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Answers")
public class Answer extends BaseEntity {

    @Column(name = "is_finished")
    Boolean isFinished;

    @Column(name = "score")
    Integer score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_id")
    Test test;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "respondent_id")
    Respondent respondent;

    @OneToMany(mappedBy = "answer", fetch = FetchType.LAZY)
    List<Solution> solutions;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    public Answer(Long id) {
        super(id);
    }

    public Answer(Test test, Respondent respondent) {
        this.test = test;
        this.respondent = respondent;
    }
}
