package com.poshtarenko.codeforge.entity.lesson;

import com.poshtarenko.codeforge.entity.BaseEntity;
import com.poshtarenko.codeforge.entity.code.Language;
import com.poshtarenko.codeforge.entity.user.Author;
import jakarta.persistence.*;
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
@Table(name = "Lessons")
public class Lesson extends BaseEntity {

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    Language language;

    @Column(name = "invite_code")
    String inviteCode;

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY)
    List<Participation> participations;

    public Lesson(Long id) {
        super(id);
    }

}