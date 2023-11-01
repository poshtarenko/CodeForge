package com.poshtarenko.codeforge.integration;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class IntegrationTest {

    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15.2");

    @BeforeAll
    static void startContainer() {
        container.start();
    }

    @DynamicPropertySource
    static void containerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
    }

}
