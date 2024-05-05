package com.poshtarenko.codeforge.entity.test;

import com.poshtarenko.codeforge.entity.BaseEntity;
import com.poshtarenko.codeforge.entity.code.Language;
import com.poshtarenko.codeforge.entity.user.Author;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    Author owner;

    @Column(name = "testing_code")
    String testingCode;

    @Column(name = "template_code")
    String templateCode;

    @Column(name = "is_completed")
    Boolean isCompleted;

    public Problem(Long id) {
        super(id);
    }
}