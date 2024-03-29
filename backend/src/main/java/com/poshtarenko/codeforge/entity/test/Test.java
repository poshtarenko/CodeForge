package com.poshtarenko.codeforge.entity.test;

import com.poshtarenko.codeforge.entity.BaseEntity;
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
@Table(name = "Tests")
public class Test extends BaseEntity {

    @Column(name = "name")
    String name;

    @Column(name = "max_duration")
    Integer maxDuration;

    @Column(name = "invite_code")
    String inviteCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    Author author;

    @OneToMany(mappedBy = "test", fetch = FetchType.LAZY)
    List<Task> tasks;

    @OneToMany(mappedBy = "test", fetch = FetchType.LAZY)
    List<Answer> answers;

    public Test(Long id) {
        super(id);
    }

    public Test(String name, Integer maxDuration, Author author, String inviteCode) {
        this.name = name;
        this.maxDuration = maxDuration;
        this.author = author;
        this.inviteCode = inviteCode;
    }
}
