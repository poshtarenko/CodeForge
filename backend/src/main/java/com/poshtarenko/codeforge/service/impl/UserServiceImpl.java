package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.entity.user.Author;
import com.poshtarenko.codeforge.entity.user.ERole;
import com.poshtarenko.codeforge.entity.user.Respondent;
import com.poshtarenko.codeforge.entity.user.User;
import com.poshtarenko.codeforge.repository.AuthorRepository;
import com.poshtarenko.codeforge.repository.RespondentRepository;
import com.poshtarenko.codeforge.repository.RoleRepository;
import com.poshtarenko.codeforge.security.jwt.JwtUtils;
import com.poshtarenko.codeforge.security.pojo.JwtResponse;
import com.poshtarenko.codeforge.security.pojo.SignInRequest;
import com.poshtarenko.codeforge.security.pojo.SignUpRequest;
import com.poshtarenko.codeforge.security.userdetails.UserDetailsImpl;
import com.poshtarenko.codeforge.service.RefreshTokenService;
import com.poshtarenko.codeforge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final RespondentRepository respondentRepository;
    private final RoleRepository roleRepository;
    private final AuthorRepository authorRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;

    @Override
    public User register(SignUpRequest signUpRequest) {
        String encodedPassword = passwordEncoder.encode(signUpRequest.password());

        User user;
        if (signUpRequest.role().equals(ERole.RESPONDENT)) {
            user = respondentRepository.save(new Respondent(
                    signUpRequest.email(),
                    signUpRequest.username(),
                    encodedPassword,
                    Collections.singletonList(roleRepository.findByName(ERole.RESPONDENT).orElseThrow(
                            () -> new RuntimeException("Role RESPONDENT dont found")
                    ))
            ));
        } else if (signUpRequest.role().equals(ERole.AUTHOR)) {
            user = authorRepository.save(new Author(
                    signUpRequest.email(),
                    signUpRequest.username(),
                    encodedPassword,
                    Collections.singletonList(roleRepository.findByName(ERole.AUTHOR).orElseThrow(
                            () -> new RuntimeException("Role AUTHOR dont found")
                    ))
            ));
        } else {
            throw new RuntimeException("User not created : unknown role");
        }

        return user;
    }

    @Override
    public JwtResponse auth(SignInRequest signInRequest) {
        UsernamePasswordAuthenticationToken authData = new UsernamePasswordAuthenticationToken(
                signInRequest.email(),
                signInRequest.password()
        );
        Authentication authentication = authenticationManager.authenticate(authData);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwt(userDetails.getEmail());
        String refreshToken = refreshTokenService.createToken(userDetails.getId());
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new JwtResponse(
                jwt,
                refreshToken,
                roles
        );
    }
}
