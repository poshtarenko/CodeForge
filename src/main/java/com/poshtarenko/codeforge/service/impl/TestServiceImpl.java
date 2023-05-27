package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.request.SaveTestDTO;
import com.poshtarenko.codeforge.dto.request.UpdateTestDTO;
import com.poshtarenko.codeforge.dto.response.ViewTestDTO;
import com.poshtarenko.codeforge.dto.mapper.TestMapper;
import com.poshtarenko.codeforge.entity.Test;
import com.poshtarenko.codeforge.exception.EntityAccessDeniedException;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
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
    private final TestMapper testMapper;

    @Override
    public ViewTestDTO find(long id) {
        return testRepository.findById(id)
                .map(testMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        Test.class, "Test with id " + id + " not found")
                );
    }

    @Override
    public ViewTestDTO findByCode(String code) {
        return testRepository.findByCode(code)
                .map(testMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        Test.class, "Test with code " + code + " not found")
                );
    }

    @Override
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
            testWithCode = testRepository.findByCode(code);
        } while (testWithCode.isPresent());

        test.setCode(code);
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
    public void checkAccess(long testId, long authorId) {
        if (testRepository.findById(testId).isEmpty()) {
            throw new EntityNotFoundException(Test.class, "Test with id %d not found".formatted(testId));
        }
        if (!testRepository.checkAccess(testId, authorId)) {
            throw new EntityAccessDeniedException(Test.class, testId, authorId);
        }
    }
}
