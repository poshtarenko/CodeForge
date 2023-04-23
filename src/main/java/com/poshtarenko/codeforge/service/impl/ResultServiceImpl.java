package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.SaveResultDTO;
import com.poshtarenko.codeforge.dto.ViewResultDTO;
import com.poshtarenko.codeforge.dto.mapper.ResultMapper;
import com.poshtarenko.codeforge.entity.Result;
import com.poshtarenko.codeforge.entity.Task;
import com.poshtarenko.codeforge.entity.Test;
import com.poshtarenko.codeforge.exception.EntityAccessDeniedException;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.ResultRepository;
import com.poshtarenko.codeforge.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ResultServiceImpl implements ResultService {

    private final ResultRepository resultRepository;
    private final ResultMapper resultMapper;

    public ResultServiceImpl(ResultRepository resultRepository, ResultMapper resultMapper) {
        this.resultRepository = resultRepository;
        this.resultMapper = resultMapper;
    }

    @Override
    public ViewResultDTO find(long id) {
        return resultRepository.findById(id)
                .map(resultMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        Result.class, "Result with id " + id + " not found")
                );
    }

    @Override
    public ViewResultDTO save(SaveResultDTO resultDTO) {
        Result result = resultRepository.save(resultMapper.toEntity(resultDTO));
        return resultMapper.toDto(result);
    }

    @Override
    public void delete(long id) {
        resultRepository.deleteById(id);
    }

    @Override
    public void checkAccess(long resultId, long authorId) {
        if (resultRepository.findById(resultId).isEmpty()) {
            throw new EntityNotFoundException(Result.class, "Result with id %d not found".formatted(resultId));
        }
        if (!resultRepository.checkAccess(resultId, authorId)) {
            throw new EntityAccessDeniedException(Result.class, resultId, authorId);
        }
    }
}
