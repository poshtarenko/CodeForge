package com.poshtarenko.codeforge.integration.data;

import com.poshtarenko.codeforge.entity.Category;
import com.poshtarenko.codeforge.entity.Language;
import com.poshtarenko.codeforge.entity.Problem;
import com.poshtarenko.codeforge.entity.Task;
import com.poshtarenko.codeforge.entity.Test;
import com.poshtarenko.codeforge.integration.security.TestSecurityUsersInitializer;
import com.poshtarenko.codeforge.repository.CategoryRepository;
import com.poshtarenko.codeforge.repository.LanguageRepository;
import com.poshtarenko.codeforge.repository.ProblemRepository;
import com.poshtarenko.codeforge.repository.TaskRepository;
import com.poshtarenko.codeforge.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestDataInitializer {

    private final TestSecurityUsersInitializer usersInitializer;
    private final TestRepository testRepository;
    private final TaskRepository taskRepository;
    private final ProblemRepository problemRepository;
    private final LanguageRepository languageRepository;
    private final CategoryRepository categoryRepository;

    private Test test;
    private Task task;
    private Language language;
    private Category category;
    private Problem problem;

    @Autowired
    public TestDataInitializer(TestSecurityUsersInitializer usersInitializer,
                               TestRepository testRepository,
                               TaskRepository taskRepository,
                               ProblemRepository problemRepository,
                               LanguageRepository languageRepository,
                               CategoryRepository categoryRepository) {
        this.usersInitializer = usersInitializer;
        this.testRepository = testRepository;
        this.taskRepository = taskRepository;
        this.problemRepository = problemRepository;
        this.languageRepository = languageRepository;
        this.categoryRepository = categoryRepository;
    }

    public void setupData() {
        category = createCategory();
        language = createLanguage();
        problem = createProblem(language, category);
        test = createTest();
        task = createTask(problem, test);
    }

    public void clearData() {
        taskRepository.deleteAll();
        testRepository.deleteAll();
        problemRepository.deleteAll();
        categoryRepository.deleteAll();
        languageRepository.deleteAll();
    }

    private Test createTest() {
        return testRepository.save(new Test(
                "Test name 1",
                100,
                usersInitializer.getAuthor()
        ));
    }

    private Language createLanguage() {
        return languageRepository.save(new Language("Java"));
    }

    private Category createCategory() {
        return categoryRepository.save(new Category("Arrays"));
    }

    private Problem createProblem(Language language, Category category) {
        return problemRepository.save(new Problem(
                "Problem name",
                "Description...",
                language,
                category,
                "public void main {...}"
        ));
    }

    private Task createTask(Problem problem, Test test) {
        return taskRepository.save(new Task(
                "Note...",
                100,
                problem,
                test
        ));
    }

    public Test getTest() {
        return test;
    }

    public Task getTask() {
        return task;
    }

    public Language getLanguage() {
        return language;
    }

    public Category getCategory() {
        return category;
    }

    public Problem getProblem() {
        return problem;
    }
}
