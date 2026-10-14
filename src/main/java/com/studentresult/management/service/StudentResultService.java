package com.studentresult.management.service;

import com.studentresult.management.dto.StudentResultDTO;
import com.studentresult.management.entity.Result;
import com.studentresult.management.repository.ResultRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class StudentResultService {

    private final ResultRepository resultRepository;

    public StudentResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    @Transactional(readOnly = true)
    public List<StudentResultDTO> getStudentResults(Integer studentId) {

        List<Result> results =
                resultRepository
                        .findByStudent_StudentIdOrderByCreatedAtDesc(studentId);

        return results.stream()
                .map(result -> new StudentResultDTO(
                        result.getResultId(),
                        result.getExamination().getExamName(),
                        result.getSemester().getSemesterNumber(),
                        result.getTotalMarks(),
                        result.getMaximumMarks(),
                        result.getPercentage(),
                        result.getCgpa(),
                        result.getResultClass(),
                        result.getWorkflowStatus(),
                        result.getPublishedAt()
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public StudentResultDTO getStudentResult(
            Integer resultId,
            Integer studentId) {

        Result result = resultRepository
                .findByResultIdAndStudent_StudentId(resultId, studentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Result not found"));

        return new StudentResultDTO(
                result.getResultId(),
                result.getExamination().getExamName(),
                result.getSemester().getSemesterNumber(),
                result.getTotalMarks(),
                result.getMaximumMarks(),
                result.getPercentage(),
                result.getCgpa(),
                result.getResultClass(),
                result.getWorkflowStatus(),
                result.getPublishedAt()
        );
    }
}