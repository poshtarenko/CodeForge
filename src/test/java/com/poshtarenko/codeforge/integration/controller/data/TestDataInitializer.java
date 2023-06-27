package com.poshtarenko.codeforge.integration.controller.data;

import com.poshtarenko.codeforge.entity.*;
import com.poshtarenko.codeforge.integration.controller.security.TestSecurityUsersInitializer;
import com.poshtarenko.codeforge.repository.SolutionRepository;
import com.poshtarenko.codeforge.repository.CategoryRepository;
import com.poshtarenko.codeforge.repository.LanguageRepository;
import com.poshtarenko.codeforge.repository.ProblemRepository;
import com.poshtarenko.codeforge.repository.AnswerRepository;
import com.poshtarenko.codeforge.repository.TaskRepository;
import com.poshtarenko.codeforge.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Profile("integration")
public class TestDataInitializer {

    private final TestSecurityUsersInitializer usersInitializer;
    private final TestRepository testRepository;
    private final TaskRepository taskRepository;
    private final ProblemRepository problemRepository;
    private final LanguageRepository languageRepository;
    private final CategoryRepository categoryRepository;
    private final SolutionRepository solutionRepository;
    private final AnswerRepository answerRepository;

    private Test test;
    private Task task;
    private Language language;
    private Category category;
    private Problem problem;
    private Solution solution;
    private Answer answer;

    public void setupData() {
        usersInitializer.setup();
        category = createCategory();
        language = createLanguage();
        problem = createProblem(language, category);
        test = createTest();
        task = createTask(problem, test);
        answer = createAnswer(test);
        solution = createSolution(task, answer);
    }

    public void clearData() {
        solutionRepository.deleteAll();
        answerRepository.deleteAll();
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
                usersInitializer.getAuthor(),
                "iQnfwEqDQ"
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
                "import java.util.ArrayList; import java.util.Collections; import java.util.List; public class Main { private static final Solution solution = new Solution(); public static void main(String[] args) { List<Integer> listUnsorted = new ArrayList<>(); listUnsorted.add(3); listUnsorted.add(5); listUnsorted.add(2); List<Integer> listExpected = new ArrayList<>(); listExpected.add(2); listExpected.add(3); listExpected.add(5); List<Integer> result = solution.sort(listUnsorted); if (result.equals(listExpected)) { System.out.println(\"SUCCESS\"); } else { System.out.println(\"FAILURE\"); } } } %s",
                ""
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

    private Solution createSolution(Task task, Answer answer) {
        return solutionRepository.save(new Solution(
                "class Solution { public List<Integer> sort(List<Integer> list){ Collections.sort(list); return list; } }",
                task,
                answer,
                150L,
                true
        ));
    }

    private Answer createAnswer(Test test) {
        return answerRepository.save(new Answer(
                false,
                test,
                usersInitializer.getRespondent()
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

    public Solution getSolution() {
        return solution;
    }

    public Answer getAnswer() {
        return answer;
    }

    public Author getAuthor() {
        return usersInitializer.getAuthor();
    }

    public Respondent getRespondent() {
        return usersInitializer.getRespondent();}
}
