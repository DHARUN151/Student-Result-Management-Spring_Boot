package com.studentresult.management.controller;

import com.studentresult.management.dto.FacultyMarksDTO;
import com.studentresult.management.dto.MarksEntryDTO;
import com.studentresult.management.entity.Marks;
import com.studentresult.management.entity.User;
import com.studentresult.management.repository.FacultySubjectRepository;
import com.studentresult.management.repository.MarksRepository;
import com.studentresult.management.repository.UserRepository;
import com.studentresult.management.service.MarksEntryService;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/faculty/marks")
public class FacultyMarksController {

    private final MarksRepository marksRepository;
    private final FacultySubjectRepository facultySubjectRepository;
    private final UserRepository userRepository;
    private final MarksEntryService marksEntryService;

    public FacultyMarksController(
            MarksRepository marksRepository,
            FacultySubjectRepository facultySubjectRepository,
            UserRepository userRepository,
            MarksEntryService marksEntryService) {

        this.marksRepository = marksRepository;
        this.facultySubjectRepository = facultySubjectRepository;
        this.userRepository = userRepository;
        this.marksEntryService = marksEntryService;
    }

    // View marks for an assigned subject offering and examination
    @GetMapping
    public List<FacultyMarksDTO> getMarks(
            @RequestParam Integer offeringId,
            @RequestParam Integer examId,
            Authentication authentication) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Authenticated user not found"));

        Integer facultyId = user.getFacultyId();

        if (facultyId == null) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No faculty profile is linked to this account");
        }

        boolean assigned = facultySubjectRepository
                .existsByFaculty_FacultyIdAndOffering_OfferingIdAndStatus(
                        facultyId, offeringId, "ACTIVE");

        if (!assigned) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not assigned to this subject offering");
        }

        List<Marks> marksList = marksRepository
                .findByOffering_OfferingIdAndExamination_ExamId(
                        offeringId, examId);

        return marksList.stream()
                .map(mark -> new FacultyMarksDTO(
                        mark.getMarkId(),
                        mark.getStudent().getStudentId(),
                        mark.getStudent().getName(),
                        mark.getOffering().getOfferingId(),
                        mark.getExamination().getExamId(),
                        mark.getInternalMark(),
                        mark.getExternalMark(),
                        mark.getTotalMark(),
                        mark.getGrade(),
                        mark.getResultOutcome(),
                        mark.getEnteredBy(),
                        mark.getEnteredAt(),
                        mark.getUpdatedBy(),
                        mark.getUpdatedAt()
                ))
                .toList();
    }

    // Update marks without changing the student, offering, or examination
    @PutMapping("/{markId}")
    public Map<String, Object> updateMarks(
            @PathVariable Integer markId,
            @RequestBody MarksEntryDTO dto,
            Authentication authentication) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Authenticated user not found"));

        Marks updated = marksEntryService.updateMarks(
                markId,
                dto,
                user.getUserId());

        return Map.of(
                "message", "Marks updated successfully",
                "markId", updated.getMarkId(),
                "studentId", updated.getStudent().getStudentId(),
                "offeringId", updated.getOffering().getOfferingId(),
                "examId", updated.getExamination().getExamId(),
                "totalMark", updated.getTotalMark());
    }


    @GetMapping("/{markId}")
    public FacultyMarksDTO getMarksById(
            @PathVariable Integer markId,
            Authentication authentication) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Authenticated user not found"));

        Marks mark = marksRepository.findById(markId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Marks record not found"));

        Integer facultyId = user.getFacultyId();

        if (facultyId == null
                || !facultySubjectRepository
                .existsByFaculty_FacultyIdAndOffering_OfferingIdAndStatus(
                        facultyId,
                        mark.getOffering().getOfferingId(),
                        "ACTIVE")) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not assigned to this subject offering");
        }

        return new FacultyMarksDTO(
                mark.getMarkId(),
                mark.getStudent().getStudentId(),
                mark.getStudent().getName(),
                mark.getOffering().getOfferingId(),
                mark.getExamination().getExamId(),
                mark.getInternalMark(),
                mark.getExternalMark(),
                mark.getTotalMark(),
                mark.getGrade(),
                mark.getResultOutcome(),
                mark.getEnteredBy(),
                mark.getEnteredAt(),
                mark.getUpdatedBy(),
                mark.getUpdatedAt()
        );
    }

    @PostMapping
    public Map<String, Object> enterMarks(
            @RequestBody MarksEntryDTO dto,
            Authentication authentication) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Authenticated user not found"));

        if (user.getFacultyId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No faculty profile is linked to this account");
        }

        Marks saved = marksEntryService.enterMarks(
                dto, user.getUserId());

        return Map.of(
                "message", "Marks entered successfully",
                "markId", saved.getMarkId(),
                "studentId", saved.getStudent().getStudentId(),
                "offeringId", saved.getOffering().getOfferingId(),
                "examId", saved.getExamination().getExamId(),
                "totalMark", saved.getTotalMark(),
                "grade", saved.getGrade(),
                "resultOutcome", saved.getResultOutcome()
        );
    }

}
