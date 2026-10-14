
package com.studentresult.management.controller;

import com.studentresult.management.dto.MarksEntryDTO;
import com.studentresult.management.entity.Marks;
import com.studentresult.management.entity.User;
import com.studentresult.management.repository.UserRepository;
import com.studentresult.management.service.MarksEntryService;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/faculty/marks-entry")
public class MarksEntryController {

    private final MarksEntryService marksEntryService;
    private final UserRepository userRepository;

    public MarksEntryController(
            MarksEntryService marksEntryService,
            UserRepository userRepository) {
        this.marksEntryService = marksEntryService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> enterMarks(
            @RequestBody MarksEntryDTO dto,
            Authentication authentication) {

        if (authentication == null
                || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Authentication required");
        }

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

        Marks marks = marksEntryService.enterMarks(
                dto, user.getUserId());

        return Map.of(
                "message", "Marks entered successfully",
                "markId", marks.getMarkId(),
                "studentId", marks.getStudent().getStudentId(),
                "offeringId", marks.getOffering().getOfferingId(),
                "examId", marks.getExamination().getExamId(),
                "totalMark", marks.getTotalMark(),
                "grade", marks.getGrade(),
                "resultOutcome", marks.getResultOutcome()
        );
    }
}
