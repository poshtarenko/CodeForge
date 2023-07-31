package com.poshtarenko.codeforge.integration.controller.security;

import com.poshtarenko.codeforge.security.userdetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("integration")
public class TestSecurityContextFactory implements WithSecurityContextFactory<MockUser> {


    private final TestSecurityUsersInitializer usersInitializer;

    @Override
    public SecurityContext createSecurityContext(MockUser customUser) {
        usersInitializer.setup();
        UserDetailsImpl userDetails = usersInitializer.getUserDetails(customUser.role());

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        return context;
    }
}
