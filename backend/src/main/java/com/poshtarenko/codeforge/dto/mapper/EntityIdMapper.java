package com.poshtarenko.codeforge.dto.mapper;

import com.poshtarenko.codeforge.entity.BaseEntity;
import com.poshtarenko.codeforge.entity.code.Language;
import com.poshtarenko.codeforge.entity.test.*;
import com.poshtarenko.codeforge.entity.user.Author;
import com.poshtarenko.codeforge.entity.user.Respondent;
import com.poshtarenko.codeforge.entity.user.User;
import org.springframework.stereotype.Component;

@Component
public class EntityIdMapper {

    public Long toIdEntity(BaseEntity entity) {
        return entity.getId();
    }

    public User toUser(Long id) {
        return new User(id);
    }

    public Author toAuthor(Long id) {
        return new Author(id);
    }

    public Respondent toRespondent(Long id) {
        return new Respondent(id);
    }

    public Test toTest(Long id) {
        return new Test(id);
    }

    public Task toTask(Long id) {
        return new Task(id);
    }

    public Problem toProblem(Long id) {
        return new Problem(id);
    }

    public Language toLanguage(Long id) {
        return new Language(id);
    }

    public Category toCategory(Long id) {
        return new Category(id);
    }

    public Solution toAnswer(Long id) {
        return new Solution(id);
    }

    public Answer toResult(Long id) {
        return new Answer(id);
    }
}
