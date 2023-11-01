package com.poshtarenko.codeforge.entity.test;

import com.poshtarenko.codeforge.entity.BaseEntity;
import com.poshtarenko.codeforge.entity.user.Respondent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Answers")
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    Instant createdAt;

    public Answer(Long id) {
        super(id);
    }

    public Answer(Boolean isFinished, Test test, Respondent respondent) {
        this.isFinished = isFinished;
        this.test = test;
        this.respondent = respondent;
    }
}
