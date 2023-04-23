package com.poshtarenko.codeforge.service.impl;

import com.poshtarenko.codeforge.dto.SaveAnswerDTO;
import com.poshtarenko.codeforge.dto.UpdateAnswerDTO;
import com.poshtarenko.codeforge.dto.ViewAnswerDTO;
import com.poshtarenko.codeforge.dto.ViewProblemDTO;
import com.poshtarenko.codeforge.dto.mapper.AnswerMapper;
import com.poshtarenko.codeforge.entity.Answer;
import com.poshtarenko.codeforge.entity.Problem;
import com.poshtarenko.codeforge.entity.Test;
import com.poshtarenko.codeforge.exception.EntityAccessDeniedException;
import com.poshtarenko.codeforge.exception.EntityNotFoundException;
import com.poshtarenko.codeforge.pojo.CodeEvaluationRequest;
import com.poshtarenko.codeforge.pojo.CodeEvaluationResult;
import com.poshtarenko.codeforge.repository.AnswerRepository;
import com.poshtarenko.codeforge.service.AnswerService;
import com.poshtarenko.codeforge.service.CodeEvaluationProvider;
import com.poshtarenko.codeforge.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final AnswerMapper answerMapper;
    private final CodeEvaluationProvider codeEvaluationProvider;
    private final ProblemService problemService;

    public AnswerServiceImpl(AnswerRepository answerRepository,
                             AnswerMapper answerMapper,
                             CodeEvaluationProvider codeEvaluationProvider,
                             ProblemService problemService) {
        this.answerRepository = answerRepository;
        this.answerMapper = answerMapper;
        this.codeEvaluationProvider = codeEvaluationProvider;
        this.problemService = problemService;
    }

    @Override
    public ViewAnswerDTO find(long id) {
        return answerRepository.findById(id)
                .map(answerMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        Answer.class, "Answer with id " + id + " not found")
                );
    }

    @Override
    public ViewAnswerDTO save(SaveAnswerDTO answerDTO) {
        Answer answer = answerMapper.toEntity(answerDTO);

        ViewProblemDTO problem = problemService.findByTask(answer.getTask().getId());

        CodeEvaluationRequest codeEvaluationRequest = new CodeEvaluationRequest(
                "JAVA",
                problem.testingCode().formatted(answer.getCode())
        );
        CodeEvaluationResult codeEvaluationResult = codeEvaluationProvider.evaluateCode(codeEvaluationRequest);

        answer.setIsCompleted(codeEvaluationResult.isCompleted());
        answer.setEvaluationTime(codeEvaluationResult.evaluationTime());

        return answerMapper.toDto(answerRepository.save(answer));
    }

    @Override
    public ViewAnswerDTO update(UpdateAnswerDTO answerDTO) {
        Answer answer = answerRepository.findById(answerDTO.id())
                .orElseThrow(() -> new EntityNotFoundException(
                        Test.class,
                        "Test with id %d not found".formatted(answerDTO.id())));
        answer.setCode(answerDTO.code());
        answer.setIsCompleted(true);
        answer.setEvaluationTime(1L);
        return answerMapper.toDto(answerRepository.save(answer));
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
