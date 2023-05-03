package com.poshtarenko.codeforge.integration.security;

import com.poshtarenko.codeforge.entity.ERole;
import com.poshtarenko.codeforge.security.userdetails.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class TestSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {


    private final TestSecurityUsersInitializer usersInitializer;

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserDetailsImpl userDetails = usersInitializer.getUserDetails(customUser.role());

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
        context.setAuthentication(auth);
        return context;
    }

    private boolean extractRole(String[] roles, ERole role) {
        return Arrays.stream(roles)
                .map(ERole::valueOf)
                .anyMatch(r -> r.equals(role));
    }
}
