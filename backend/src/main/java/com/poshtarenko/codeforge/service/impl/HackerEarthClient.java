package com.poshtarenko.codeforge.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poshtarenko.codeforge.dto.hackerearth.HackerEarthCodeEvaluationResult;
import com.poshtarenko.codeforge.dto.hackerearth.HackerEarthQueueingResponse;
import com.poshtarenko.codeforge.dto.request.CodeEvaluationRequest;
import com.poshtarenko.codeforge.dto.request.HackerEarthCodeEvaluationRequest;
import com.poshtarenko.codeforge.entity.code.EvaluationResult;
import com.poshtarenko.codeforge.service.CodeEvaluationProvider;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.UriSpec;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HackerEarthClient implements CodeEvaluationProvider {

    private static final String API_URL = "https://api.hackerearth.com/";
    private static final String AUTH_HEADER_KEY = "client-secret";
    private static final String AUTH_HEADER_VALUE = "8bd98e6f777bb6a2a30cb31108117162aca67d8a";
    private static final String QUEUEING_URL = "/v4/partner/code-evaluation/submissions/";
    private static final String EVALUATION_RESULT_URL = "/v4/partner/code-evaluation/submissions/%s/";

    private static final int DELAY_BEFORE_REQUEST_MS = 150;
    private static final int COMPILATION_WAIT_DELAY = 150;

    private static final List<String> PREPARATORY_REQUEST_STATUSES = List.of(
            "REQUEST_INITIATED",
            "REQUEST_QUEUED"
    );
    private static final String CODE_SUCCESSFULLY_COMPILED_STATUS = "CODE_COMPILED";
    private static final String CODE_PROCESSED_STATUS = "REQUEST_COMPLETED";

    private final WebClient client = WebClient.builder()
            .baseUrl(API_URL)
            .defaultHeader(AUTH_HEADER_KEY, AUTH_HEADER_VALUE)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    private final ObjectMapper objectMapper;

    @SneakyThrows
    public EvaluationResult evaluateCode(CodeEvaluationRequest request) {
        String heId = queueRequest(request).heId();

        Thread.sleep(DELAY_BEFORE_REQUEST_MS);
        HackerEarthCodeEvaluationResult evaluationResult = getEvaluationResult(heId);
        String requestCode = evaluationResult.requestStatus().code();

        while (PREPARATORY_REQUEST_STATUSES.contains(requestCode)) {
            evaluationResult = getEvaluationResult(heId);
            requestCode = evaluationResult.requestStatus().code();
            Thread.sleep(COMPILATION_WAIT_DELAY);
        }

        String error = null;
        if (requestCode.equals(CODE_SUCCESSFULLY_COMPILED_STATUS)) {
            String compileStatus = evaluationResult.result().compileStatus();
            if (compileStatus.equals("OK")) {
                while (!requestCode.equals(CODE_PROCESSED_STATUS)) {
                    evaluationResult = getEvaluationResult(heId);
                    requestCode = evaluationResult.requestStatus().code();
                    Thread.sleep(COMPILATION_WAIT_DELAY);
                }
            } else {
                error = "Compilation error:\n" + compileStatus;
            }
        }

        String output = null;
        if (requestCode.equals(CODE_PROCESSED_STATUS)) {
            String outputURL = evaluationResult.result().runStatus().output();
            output = downloadCodeEvaluationResult(outputURL);
        }

        return new EvaluationResult(
                output,
                error
        );
    }

    @SneakyThrows
    private HackerEarthQueueingResponse queueRequest(CodeEvaluationRequest request) {
        UriSpec<RequestBodySpec> uriSpec = client.method(HttpMethod.POST);
        RequestBodySpec bodySpec = uriSpec.uri(QUEUEING_URL);

        HackerEarthCodeEvaluationRequest codeEvaluationRequest = new HackerEarthCodeEvaluationRequest(
                "JAVA8",
                request.code()
        );

        RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue(objectMapper.writeValueAsString(codeEvaluationRequest));

        return headersSpec.retrieve()
                .bodyToMono(HackerEarthQueueingResponse.class)
                .block();
    }

    private HackerEarthCodeEvaluationResult getEvaluationResult(String heId) {
        return client.get()
                .uri(EVALUATION_RESULT_URL.formatted(heId))
                .retrieve()
                .bodyToMono(HackerEarthCodeEvaluationResult.class)
                .block();
    }

    @SneakyThrows
    private String downloadCodeEvaluationResult(String url) {
        InputStream in = new URL(url).openStream();
        return new BufferedReader(new InputStreamReader(in))
                .lines().collect(Collectors.joining("\n"));
    }

}
