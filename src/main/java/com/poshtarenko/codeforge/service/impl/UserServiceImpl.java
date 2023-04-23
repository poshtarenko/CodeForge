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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final RespondentRepository respondentRepository;
    private final RoleRepository roleRepository;
    private final AuthorRepository authorRepository;
    private final UserRepository userRepository;

    public UserServiceImpl(RespondentRepository respondentRepository,
                           RoleRepository roleRepository,
                           AuthorRepository authorRepository,
                           UserRepository userRepository) {
        this.respondentRepository = respondentRepository;
        this.roleRepository = roleRepository;
        this.authorRepository = authorRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void register(SignUpRequest signUpRequest) {
        if (signUpRequest.getRole().equals(ERole.RESPONDENT)) {
            Respondent respondent = new Respondent(
                    signUpRequest.getEmail(),
                    signUpRequest.getUsername(),
                    signUpRequest.getPassword(),
                    Collections.singletonList(roleRepository.findByName(ERole.RESPONDENT).orElseThrow(
                            () -> new RuntimeException("Role RESPONDENT dont found")
                    ))
            );
            respondentRepository.save(respondent);
        } else if (signUpRequest.getRole().equals(ERole.AUTHOR)) {
            Author author = new Author(
                    signUpRequest.getEmail(),
                    signUpRequest.getUsername(),
                    signUpRequest.getPassword(),
                    Collections.singletonList(roleRepository.findByName(ERole.AUTHOR).orElseThrow(
                            () -> new RuntimeException("Role AUTHOR dont found")
                    ))
            );
            authorRepository.save(author);
        } else if (signUpRequest.getRole().equals(ERole.ADMIN)) {
            User user = new User(
                    signUpRequest.getEmail(),
                    signUpRequest.getUsername(),
                    signUpRequest.getPassword(),
                    Collections.singletonList(roleRepository.findByName(ERole.ADMIN).orElseThrow(
                            () -> new RuntimeException("Role ADMIN dont found")
                    ))
            );
            userRepository.save(user);
        }
    }
}
