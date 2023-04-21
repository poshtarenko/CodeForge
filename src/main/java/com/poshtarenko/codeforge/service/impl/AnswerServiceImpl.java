package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.SaveAnswerDTO;
import com.poshtarenko.codeforge.dto.UpdateAnswerDTO;
import com.poshtarenko.codeforge.dto.ViewAnswerDTO;
import com.poshtarenko.codeforge.dto.mapper.AnswerMapper;
import com.poshtarenko.codeforge.entity.Answer;
import com.poshtarenko.codeforge.entity.Task;
import com.poshtarenko.codeforge.entity.Test;
import com.poshtarenko.codeforge.exception.EntityAccessDeniedException;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.repository.AnswerRepository;
import com.poshtarenko.codeforge.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnswerServiceImpl implements AnswerService {

    AnswerRepository answerRepository;
    AnswerMapper answerMapper;

    @Autowired
    public AnswerServiceImpl(AnswerRepository answerRepository, AnswerMapper answerMapper) {
        this.answerRepository = answerRepository;
        this.answerMapper = answerMapper;
    }

    @Override
    public ViewAnswerDTO find(long id) {
        return answerRepository.findById(id)
                .map(answerMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        Answer.class, "" +
                        "Answer with id " + id + " not found")
                );
    }

    @Override
    public ViewAnswerDTO save(SaveAnswerDTO answerDTO) {
        Answer answer = answerRepository.save(answerMapper.toEntity(answerDTO));
        return answerMapper.toDto(answer);
    }

    @Override
    public ViewAnswerDTO update(UpdateAnswerDTO answerDTO) {
        answerRepository.findById(answerDTO.id())
                .orElseThrow(() -> new EntityNotFoundException(
                        Test.class,
                        "Test with id %d not found".formatted(answerDTO.id())));

        Answer answer = answerRepository.save(answerMapper.toEntity(answerDTO));
        return answerMapper.toDto(answer);
    }

    @Override
    public void delete(long id) {
        answerRepository.deleteById(id);
    }

    @Override
    public void checkAccess(long answerId, long authorId) {
        if (answerRepository.findById(answerId).isEmpty()) {
            throw new EntityNotFoundException(Answer.class, "Answer with id %d not found".formatted(answerId));
        }
        if (!answerRepository.checkAccess(answerId, authorId)) {
            throw new EntityAccessDeniedException(Answer.class, answerId, authorId);
        }
    }
}
