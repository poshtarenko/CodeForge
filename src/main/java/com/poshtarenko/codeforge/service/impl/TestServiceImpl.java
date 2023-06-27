package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.mapper.TestMapper;
import com.poshtarenko.codeforge.dto.request.SaveTestDTO;
import com.poshtarenko.codeforge.dto.request.UpdateTestDTO;
import com.poshtarenko.codeforge.dto.response.ViewTestDTO;
import com.poshtarenko.codeforge.entity.Test;
import com.poshtarenko.codeforge.exception.EntityAccessDeniedException;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.AnswerRepository;
import com.poshtarenko.codeforge.repository.TestRepository;
import com.poshtarenko.codeforge.service.TestService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private static final int TEST_INVITE_CODE_LENGTH = 8;

    private final TestRepository testRepository;
    private final AnswerRepository answerRepository;
    private final TestMapper testMapper;

    @Override
    @Transactional(readOnly = true)
    public ViewTestDTO find(long id) {
        return testMapper.toDto(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ViewTestDTO findByInviteCode(String inviteCode) {
        return testRepository.findByInviteCode(inviteCode)
                .map(testMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        Test.class, "Test with code " + inviteCode + " not found")
                );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewTestDTO> findByAuthor(long authorId) {
        return testRepository.findAllByAuthorId(authorId)
                .stream()
                .map(testMapper::toDto)
                .collect(toList());
    }

    @Override
    public ViewTestDTO save(SaveTestDTO testDTO) {
        Test test = testMapper.toEntity(testDTO);

        String code;
        Optional<Test> testWithCode;

        do {
            code = RandomStringUtils.randomAlphabetic(TEST_INVITE_CODE_LENGTH);
            testWithCode = testRepository.findByInviteCode(code);
        } while (testWithCode.isPresent());

        test.setInviteCode(code);
        Test saved = testRepository.save(test);

        return testMapper.toDto(saved);
    }

    @Override
    public ViewTestDTO update(UpdateTestDTO testDTO) {
        Test test = testRepository.findById(testDTO.id())
                .orElseThrow(() -> new EntityNotFoundException(
                        Test.class,
                        "Test with id %d not found".formatted(testDTO.id())));

        test.setName(testDTO.name());
        test.setMaxDuration(testDTO.maxDuration());

        Test savedTest = testRepository.save(test);
        return testMapper.toDto(savedTest);
    }

    @Override
    public void delete(long id) {
        testRepository.deleteById(id);
    }

    @Override
    public void checkRespondentConnectedToTest(long respondentId, long testId) {
        if (!answerRepository.existsByRespondentIdAndTestId(respondentId, testId)) {
            throw new RuntimeException("Respondent is not connected to test");
        }
    }

    @Override
    public void checkAccess(long testId, long authorId) {
        Test test = findById(testId);
        if (!test.getAuthor().getId().equals(authorId)) {
            throw new EntityAccessDeniedException(Test.class, testId, authorId);
        }
    }

    private Test findById(long testId) {
        return testRepository.findById(testId)
                .orElseThrow(() -> new EntityNotFoundException(
                        Test.class, "Test with id " + testId + " not found")
                );
    }
}
