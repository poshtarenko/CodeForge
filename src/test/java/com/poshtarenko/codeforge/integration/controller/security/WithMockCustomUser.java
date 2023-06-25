package com.poshtarenko.codeforge.integration.controller.security;

import com.poshtarenko.codeforge.entity.ERole;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = TestSecurityContextFactory.class)
public @interface WithMockCustomUser {

    ERole role() default ERole.RESPONDENT;

}