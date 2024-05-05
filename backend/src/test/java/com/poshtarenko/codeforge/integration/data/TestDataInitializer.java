package com.poshtarenko.codeforge.integration.data;

import com.poshtarenko.codeforge.entity.code.Language;
import com.poshtarenko.codeforge.entity.lesson.Lesson;
import com.poshtarenko.codeforge.entity.lesson.Participation;
import com.poshtarenko.codeforge.entity.test.*;
import com.poshtarenko.codeforge.entity.user.Author;
import com.poshtarenko.codeforge.entity.user.Respondent;
import com.poshtarenko.codeforge.integration.security.TestSecurityUsersInitializer;
import com.poshtarenko.codeforge.repository.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

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
    private final LessonRepository lessonRepository;
    private final ParticipationRepository participationRepository;

    @Getter
    private Test test;
    @Getter
    private Task task;
    @Getter
    private Language language;
    @Getter
    private Category category;
    @Getter
    private Problem problem;
    @Getter
    private Solution solution;
    @Getter
    private Answer answer;
    @Getter
    private Lesson lesson;
    @Getter
    private Participation participation;

    public void setupData() {
        usersInitializer.setup();
        category = createCategory();
        language = createLanguage();
        problem = createProblem(language, category);
        test = createTest();
        task = createTask(problem, test);
        answer = createAnswer(test);
        solution = createSolution(task, answer);
        lesson = createLesson(language);
        participation = createParticipation(lesson);
    }

    public void clearData() {
        solutionRepository.deleteAll();
        answerRepository.deleteAll();
        taskRepository.deleteAll();
        testRepository.deleteAll();
        problemRepository.deleteAll();
        categoryRepository.deleteAll();
        languageRepository.deleteAll();
        lessonRepository.deleteAll();
    }

    private Participation createParticipation(Lesson lesson) {
        Participation p = new Participation();
        p.setLesson(lesson);
        p.setCode("some code");
        p.setUser(getAuthor());
        return participationRepository.save(p);
    }

    private Lesson createLesson(Language language) {
        Lesson l = new Lesson();
        l.setAuthor(getAuthor());
        l.setName("Lesson name");
        l.setInviteCode("abcdef");
        l.setLanguage(language);
        return lessonRepository.save(l);
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
        Solution s = new Solution();
        s.setCode("class Solution { public List<Integer> sort(List<Integer> list){ Collections.sort(list); return list; } }");
        s.setTask(task);
        s.setAnswer(answer);
//        s.setSolutionResult(new SolutionResult(true, ""));
        return solutionRepository.save(s);
    }

    private Answer createAnswer(Test test) {
        return answerRepository.save(new Answer(
                false,
                test,
                usersInitializer.getRespondent()
        ));
    }

    public Author getAuthor() {
        return usersInitializer.getAuthor();
    }

    public Respondent getRespondent() {
        return usersInitializer.getRespondent();
    }
}
