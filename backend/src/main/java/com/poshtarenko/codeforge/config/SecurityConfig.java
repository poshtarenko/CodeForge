package com.poshtarenko.codeforge.config;

import com.poshtarenko.codeforge.security.jwt.AuthEntryPointJwt;
import com.poshtarenko.codeforge.security.jwt.AuthTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
//@EnableWebSocketSecurity
public class SecurityConfig {

    private static final String[] WHITELIST = {
            "/auth/**",
            "/ws/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/v2/api-docs",
            "/webjars/**",
            "/index",
            "/error"
    };

    private final AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       UserDetailsService userDetailsService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITELIST).permitAll()
                        .anyRequest().authenticated())
                .addFilterAfter(authenticationJwtTokenFilter(), BasicAuthenticationFilter.class)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic();
        return http.build();
    }
}
