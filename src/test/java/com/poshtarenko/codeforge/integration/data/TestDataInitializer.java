package com.poshtarenko.codeforge.integration.data;

import com.poshtarenko.codeforge.entity.Answer;
import com.poshtarenko.codeforge.entity.Category;
import com.poshtarenko.codeforge.entity.Language;
import com.poshtarenko.codeforge.entity.Problem;
import com.poshtarenko.codeforge.entity.Task;
import com.poshtarenko.codeforge.entity.Test;
import com.poshtarenko.codeforge.integration.security.TestSecurityUsersInitializer;
import com.poshtarenko.codeforge.repository.AnswerRepository;
import com.poshtarenko.codeforge.repository.CategoryRepository;
import com.poshtarenko.codeforge.repository.LanguageRepository;
import com.poshtarenko.codeforge.repository.ProblemRepository;
import com.poshtarenko.codeforge.repository.ResultRepository;
import com.poshtarenko.codeforge.repository.TaskRepository;
import com.poshtarenko.codeforge.repository.TestRepository;
import org.springframework.stereotype.Component;

@Component
public class TestDataInitializer {

    private final TestSecurityUsersInitializer usersInitializer;
    private final TestRepository testRepository;
    private final TaskRepository taskRepository;
    private final ProblemRepository problemRepository;
    private final LanguageRepository languageRepository;
    private final CategoryRepository categoryRepository;
    private final AnswerRepository answerRepository;
    private final ResultRepository resultRepository;

    private Test test;
    private Task task;
    private Language language;
    private Category category;
    private Problem problem;
    private Answer answer;

    public TestDataInitializer(TestSecurityUsersInitializer usersInitializer,
                               TestRepository testRepository,
                               TaskRepository taskRepository,
                               ProblemRepository problemRepository,
                               LanguageRepository languageRepository,
                               CategoryRepository categoryRepository,
                               AnswerRepository answerRepository,
                               ResultRepository resultRepository) {
        this.usersInitializer = usersInitializer;
        this.testRepository = testRepository;
        this.taskRepository = taskRepository;
        this.problemRepository = problemRepository;
        this.languageRepository = languageRepository;
        this.categoryRepository = categoryRepository;
        this.answerRepository = answerRepository;
        this.resultRepository = resultRepository;
    }

    public void setupData() {
        category = createCategory();
        language = createLanguage();
        problem = createProblem(language, category);
        test = createTest();
        task = createTask(problem, test);
        answer = createAnswer(task);
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
                "import java.util.ArrayList; import java.util.Collections; import java.util.List; public class Main { private static final Solution solution = new Solution(); public static void main(String[] args) { List<Integer> listUnsorted = new ArrayList<>(); listUnsorted.add(3); listUnsorted.add(5); listUnsorted.add(2); List<Integer> listExpected = new ArrayList<>(); listExpected.add(2); listExpected.add(3); listExpected.add(5); List<Integer> result = solution.sort(listUnsorted); if (result.equals(listExpected)) { System.out.println(\"SUCCESS\"); } else { System.out.println(\"FAILURE\"); } } } %s"
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

    private Answer createAnswer(Task task) {
        return answerRepository.save(new Answer(
                "class Solution { public List<Integer> sort(List<Integer> list){ Collections.sort(list); return list; } }",
                task,
                usersInitializer.getRespondent(),
                150L,
                true
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

    public Answer getAnswer() {
        return answer;
    }
}
