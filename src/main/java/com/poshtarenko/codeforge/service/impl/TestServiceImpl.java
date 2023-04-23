package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.SaveTestDTO;
import com.poshtarenko.codeforge.dto.UpdateTestDTO;
import com.poshtarenko.codeforge.dto.ViewTestDTO;
import com.poshtarenko.codeforge.dto.mapper.TestMapper;
import com.poshtarenko.codeforge.entity.Test;
import com.poshtarenko.codeforge.exception.EntityAccessDeniedException;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.TestRepository;
import com.poshtarenko.codeforge.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class TestServiceImpl implements TestService {

    private final TestRepository testRepository;
    private final TestMapper testMapper;

    public TestServiceImpl(TestRepository testRepository, TestMapper testMapper) {
        this.testRepository = testRepository;
        this.testMapper = testMapper;
    }

    @Override
    public ViewTestDTO find(long id) {
        return testRepository.findById(id)
                .map(testMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        Test.class, "Test with id " + id + " not found")
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
        Test test = testRepository.save(testMapper.toEntity(testDTO));
        return testMapper.toDto(test);
    }

    @Override
    public ViewTestDTO update(UpdateTestDTO testDTO) {
        testRepository.findById(testDTO.id())
                .orElseThrow(() -> new EntityNotFoundException(
                        Test.class,
                        "Test with id %d not found".formatted(testDTO.id())));

        Test test = testRepository.save(testMapper.toEntity(testDTO));
        return testMapper.toDto(test);
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
