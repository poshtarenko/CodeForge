package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.entity.Author;
import com.poshtarenko.codeforge.entity.ERole;
import com.poshtarenko.codeforge.entity.Respondent;
import com.poshtarenko.codeforge.entity.User;
import com.poshtarenko.codeforge.repository.AuthorRepository;
import com.poshtarenko.codeforge.repository.RespondentRepository;
import com.poshtarenko.codeforge.repository.RoleRepository;
import com.poshtarenko.codeforge.repository.UserRepository;
import com.poshtarenko.codeforge.security.pojo.SignUpRequest;
import com.poshtarenko.codeforge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final RespondentRepository respondentRepository;
    private final RoleRepository roleRepository;
    private final AuthorRepository authorRepository;
    private final UserRepository userRepository;

    @Override
    public User register(SignUpRequest signUpRequest) {

        String encodedPassword = passwordEncoder.encode(signUpRequest.password());

        User user = null;
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
        } else if (signUpRequest.role().equals(ERole.ADMIN)) {
            user = userRepository.save(new User(
                    signUpRequest.email(),
                    signUpRequest.username(),
                    encodedPassword,
                    Collections.singletonList(roleRepository.findByName(ERole.ADMIN).orElseThrow(
                            () -> new RuntimeException("Role ADMIN dont found")
                    ))
            ));
        } else {
            throw new RuntimeException("User not created : unknown role");
        }

        return user;
    }
}
