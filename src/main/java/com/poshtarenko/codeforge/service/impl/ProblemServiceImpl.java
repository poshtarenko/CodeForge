package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.mapper.ProblemMapper;
import com.poshtarenko.codeforge.dto.request.SaveProblemDTO;
import com.poshtarenko.codeforge.dto.request.UpdateProblemDTO;
import com.poshtarenko.codeforge.dto.response.ViewProblemDTO;
import com.poshtarenko.codeforge.entity.Problem;
import com.poshtarenko.codeforge.entity.Test;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
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

    private final ProblemRepository problemRepository;
    private final ProblemMapper problemMapper;

    @Override
    public ViewProblemDTO find(long id) {
        return problemRepository.findById(id)
                .map(problemMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        Problem.class, "Problem with id " + id + " not found")
                );
    }

    @Override
    public List<ViewProblemDTO> findAll() {
        return problemRepository.findAll().stream()
                .map(problemMapper::toDto)
                .toList();
    }

    @Override
    public Problem findByTask(long taskId) {
        return problemRepository.findByTask(taskId)
                .orElseThrow(() -> new EntityNotFoundException(
                        Problem.class, "Problem by task_id " + taskId + " not found")
                );
    }

    @Override
    @Transactional
    public ViewProblemDTO save(SaveProblemDTO problemDTO) {
        Problem problem = problemRepository.save(problemMapper.toEntity(problemDTO));
        return problemMapper.toDto(problem);
    }

    @Override
    @Transactional
    public ViewProblemDTO update(UpdateProblemDTO problemDTO) {
        problemRepository.findById(problemDTO.id())
                .orElseThrow(() -> new EntityNotFoundException(
                        Test.class,
                        "Test with id %d not found".formatted(problemDTO.id())));

        Problem problem = problemRepository.save(problemMapper.toEntity(problemDTO));
        return problemMapper.toDto(problem);
    }

    @Override
    @Transactional
    public void delete(long id) {
        problemRepository.deleteById(id);
    }
}
