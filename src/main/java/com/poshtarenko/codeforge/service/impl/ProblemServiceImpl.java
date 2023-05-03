package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.SaveProblemDTO;
import com.poshtarenko.codeforge.dto.UpdateProblemDTO;
import com.poshtarenko.codeforge.dto.ViewProblemDTO;
import com.poshtarenko.codeforge.dto.mapper.ProblemMapper;
import com.poshtarenko.codeforge.entity.Problem;
import com.poshtarenko.codeforge.entity.Test;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.ProblemRepository;
import com.poshtarenko.codeforge.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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
    public ViewProblemDTO findByTask(long taskId) {
        return problemRepository.findByTask(taskId)
                .map(problemMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        Problem.class, "Problem by task_id " + taskId + " not found")
                );
    }

    @Override
    public ViewProblemDTO save(SaveProblemDTO problemDTO) {
        Problem problem = problemRepository.save(problemMapper.toEntity(problemDTO));
        return problemMapper.toDto(problem);
    }

    @Override
    public ViewProblemDTO update(UpdateProblemDTO problemDTO) {
        problemRepository.findById(problemDTO.id())
                .orElseThrow(() -> new EntityNotFoundException(
                        Test.class,
                        "Test with id %d not found".formatted(problemDTO.id())));

        Problem problem = problemRepository.save(problemMapper.toEntity(problemDTO));
        return problemMapper.toDto(problem);
    }

    @Override
    public void delete(long id) {
        problemRepository.deleteById(id);
    }
}
