package com.poshtarenko.codeforge.dto.mapper;

import com.poshtarenko.codeforge.entity.Answer;
import com.poshtarenko.codeforge.entity.Author;
import com.poshtarenko.codeforge.entity.BaseEntity;
import com.poshtarenko.codeforge.entity.Category;
import com.poshtarenko.codeforge.entity.Language;
import com.poshtarenko.codeforge.entity.Problem;
import com.poshtarenko.codeforge.entity.Respondent;
import com.poshtarenko.codeforge.entity.Result;
import com.poshtarenko.codeforge.entity.Task;
import com.poshtarenko.codeforge.entity.Test;
import com.poshtarenko.codeforge.entity.User;
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

    public Answer toAnswer(Long id) {
        return new Answer(id);
    }

    public Result toResult(Long id) {
        return new Result(id);
    }
}
