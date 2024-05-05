package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.mapper.ProblemMapper;
import com.poshtarenko.codeforge.dto.request.CreateProblemDTO;
import com.poshtarenko.codeforge.dto.request.UpdateProblemDTO;
import com.poshtarenko.codeforge.dto.response.ViewProblemDTO;
import com.poshtarenko.codeforge.entity.test.Category;
import com.poshtarenko.codeforge.entity.test.Problem;
import com.poshtarenko.codeforge.entity.test.Test;
import com.poshtarenko.codeforge.entity.user.Author;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.AuthorRepository;
import com.poshtarenko.codeforge.repository.CategoryRepository;
import com.poshtarenko.codeforge.repository.ProblemRepository;
import com.poshtarenko.codeforge.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {

    public static final long AUTHOR_CUSTOM_PROBLEM_CATEGORY_ID = 7;
    private final ProblemRepository problemRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final ProblemMapper problemMapper;

    @Override
    public ViewProblemDTO find(long id) {
        return problemRepository.findById(id)
                .map(problemMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(Problem.class, id));
    }

    @Override
    public List<ViewProblemDTO> findAllAvailableToAuthor(Long authorId) {
        return problemRepository.findAllAvailableToAuthor(authorId).stream()
                .map(problemMapper::toDto)
                .toList();
    }

    @Override
    public List<ViewProblemDTO> findAuthorCustomProblems(Long authorId) {
        return problemRepository.findByOwnerId(authorId).stream()
                .map(problemMapper::toDto)
                .toList();
    }

    @Override
    public Problem findByTask(long taskId) {
        return problemRepository.findByTask(taskId)
                .orElseThrow(() -> new EntityNotFoundException(Problem.class, taskId));
    }

    @Override
    @Transactional
    public ViewProblemDTO createCustomProblem(CreateProblemDTO problemDTO, Long authorId) {
        Problem problem = problemMapper.toEntity(problemDTO);
        problem.setIsCompleted(false);
        problem.setName(problemDTO.name());
        problem.setOwner(authorRepository.findById(authorId).orElseThrow(
                () -> new EntityNotFoundException(Author.class, authorId)));
        problem.setCategory(categoryRepository.findById(AUTHOR_CUSTOM_PROBLEM_CATEGORY_ID).orElseThrow(
                () -> new EntityNotFoundException(Category.class, AUTHOR_CUSTOM_PROBLEM_CATEGORY_ID)));
        problem = problemRepository.save(problem);
        return problemMapper.toDto(problem);
    }

    @Override
    @Transactional
    public ViewProblemDTO update(Long problemId, UpdateProblemDTO problemDTO) {
        problemRepository.findById(problemId)
                .orElseThrow(() -> new EntityNotFoundException(Problem.class, problemId));
        Problem problem = problemRepository.save(problemMapper.toEntity(problemDTO));
        return problemMapper.toDto(problem);
    }

    @Override
    @Transactional
    public void delete(long id) {
        problemRepository.deleteById(id);
    }
}
