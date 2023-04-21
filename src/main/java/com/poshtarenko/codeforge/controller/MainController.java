package com.poshtarenko.codeforge.controller;

import com.poshtarenko.codeforge.pojo.CodeEvaluationRequest;
import com.poshtarenko.codeforge.service.impl.HackerEarthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    private final HackerEarthClient hackerEarthClient;

    @Autowired
    public MainController(HackerEarthClient hackerEarthClient) {
        this.hackerEarthClient = hackerEarthClient;
    }

    @PostMapping("/t")
    public ResponseEntity<CodeEvaluationRequest> uploadAnswer(@RequestBody CodeEvaluationRequest request) {
        return ResponseEntity.ok(
                new CodeEvaluationRequest(
                        request.lang(),
                        hackerEarthClient.evaluateCode(request)
                )
        );
    }

}
