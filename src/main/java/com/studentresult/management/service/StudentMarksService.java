
package com.studentresult.management.service;

import com.studentresult.management.dto.StudentMarksDTO;
import com.studentresult.management.entity.Marks;
import com.studentresult.management.entity.User;
import com.studentresult.management.repository.MarksRepository;
import com.studentresult.management.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.studentresult.management.dto.StudentResultSummaryDTO;
import com.studentresult.management.dto.StudentResultSummaryDTO;
import java.util.List;

@Service
public class StudentMarksService {

    private final MarksRepository marksRepository;
    private final UserRepository userRepository;

    public StudentMarksService(
            MarksRepository marksRepository,
            UserRepository userRepository) {
        this.marksRepository = marksRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<StudentMarksDTO> getMyMarks(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Authenticated user not found"));

        Integer studentId = user.getStudentId();

        if (studentId == null) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "This account is not linked to a student");
        }

        List<Marks> marksList = marksRepository
                .findByStudent_StudentIdOrderByEnteredAtDesc(studentId);

        return marksList.stream()
                .map(mark -> new StudentMarksDTO(
                        mark.getMarkId(),
                        mark.getOffering().getOfferingId(),
                        mark.getOffering().getSubject().getSubjectName(),
                        mark.getOffering().getSemester().getSemesterNumber(),
                        mark.getOffering().getAcademicYear().getYearName(),
                        mark.getExamination().getExamId(),
                        mark.getExamination().getExamName(),
                        mark.getInternalMark(),
                        mark.getExternalMark(),
                        mark.getTotalMark(),
                        mark.getGrade(),
                        mark.getResultOutcome(),
                        mark.getEnteredAt()))
                .toList();
    }


    @Transactional(readOnly = true)
    public StudentResultSummaryDTO getMyResultSummary(String username) {

        List<StudentMarksDTO> marks = getMyMarks(username);

        int total = marks.size();

        int passed = (int) marks.stream()
                .filter(m -> "PASS".equalsIgnoreCase(m.getResultOutcome()))
                .count();

        int failed = (int) marks.stream()
                .filter(m -> "FAIL".equalsIgnoreCase(m.getResultOutcome()))
                .count();

        double percentage = total == 0 ? 0.0
                : Math.round(passed * 10000.0 / total) / 100.0;

        int gradeO = countGrade(marks, "O");
        int gradeAPlus = countGrade(marks, "A+");
        int gradeA = countGrade(marks, "A");
        int gradeBPlus = countGrade(marks, "B+");
        int gradeB = countGrade(marks, "B");
        int gradeC = countGrade(marks, "C");
        int gradeF = countGrade(marks, "F");

        return new StudentResultSummaryDTO(
                total, passed, failed, percentage,
                gradeO, gradeAPlus, gradeA,
                gradeBPlus, gradeB, gradeC, gradeF);
    }

    private int countGrade(List<StudentMarksDTO> marks, String grade) {
        return (int) marks.stream()
                .filter(m -> grade.equalsIgnoreCase(m.getGrade()))
                .count();
    }

}


