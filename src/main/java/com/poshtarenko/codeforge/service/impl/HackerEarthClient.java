package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.pojo.CodeEvaluationRequest;
import com.poshtarenko.codeforge.pojo.HackerEarthCodeEvaluationResult;
import com.poshtarenko.codeforge.pojo.HackerEarthQueueingResponse;
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
import java.util.stream.Collectors;

@Component
public class HackerEarthClient {

    private static final String API_URL = "https://api.hackerearth.com/";
    private static final String AUTH_HEADER_KEY = "client-secret";
    private static final String AUTH_HEADER_VALUE = "8bd98e6f777bb6a2a30cb31108117162aca67d8a";
    private static final String QUEUEING_URL = "/v4/partner/code-evaluation/submissions/";
    private static final String EVALUATION_RESULT_URL = "/v4/partner/code-evaluation/submissions/%s/";

    private final WebClient client = WebClient.builder()
            .baseUrl(API_URL)
            .defaultHeader(AUTH_HEADER_KEY, AUTH_HEADER_VALUE)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    @SneakyThrows
    public String evaluateCode(CodeEvaluationRequest request) {
        String heId = queueRequest(request).heId();

        Thread.sleep(200);
        HackerEarthCodeEvaluationResult evaluationResult = getEvaluationResult(heId);
        while (!evaluationResult.requestStatus().code().equals("REQUEST_COMPLETED")) {
            evaluationResult = getEvaluationResult(heId);
            Thread.sleep(100);
        }
        String outputURL = evaluationResult.result().runStatus().output();

        return downloadCodeEvaluationResult(outputURL);
    }

    private HackerEarthQueueingResponse queueRequest(CodeEvaluationRequest request) {
        UriSpec<RequestBodySpec> uriSpec = client.method(HttpMethod.POST);
        RequestBodySpec bodySpec = uriSpec.uri(QUEUEING_URL);

        RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue(
                """
                        {"lang": "%s", "source": "%s"}
                        """.formatted(request.lang(), request.code()));

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
