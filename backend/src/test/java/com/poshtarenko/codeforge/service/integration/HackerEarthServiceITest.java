package com.poshtarenko.codeforge.service.integration;

import com.poshtarenko.codeforge.dto.request.CodeEvaluationRequest;
import com.poshtarenko.codeforge.entity.code.EvaluationResult;
import com.poshtarenko.codeforge.service.impl.HackerEarthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HackerEarthServiceITest {

    @Autowired
    private HackerEarthService hackerEarthService;

    @Test
    void evaluateValidCode() {
        CodeEvaluationRequest request = new CodeEvaluationRequest("JAVA8", """
                public class Main {
                    public static void main(String[] args) {
                        System.out.println("SUCCESS");
                    }
                }
                """);

        EvaluationResult result = hackerEarthService.evaluateCode(request);

        assertEquals("SUCCESS", result.getOutput());
        assertNull(result.getError());
    }

    @Test
    void evaluateInvalidCode() {
        CodeEvaluationRequest request = new CodeEvaluationRequest("JAVA8", "very bad code");

        EvaluationResult result = hackerEarthService.evaluateCode(request);

        assertNull(result.getOutput());
        assertFalse(result.getError().isBlank());
    }

    @Test
    void evaluateInvalidCode2() {
        CodeEvaluationRequest request = new CodeEvaluationRequest("JAVA8", """
                public class Main {
                    public static void main(String[] args) {
                        veryBadCode();
                    }
                }
                """);

        EvaluationResult result = hackerEarthService.evaluateCode(request);

        assertNull(result.getOutput());
        assertFalse(result.getError().isBlank());
    }

}