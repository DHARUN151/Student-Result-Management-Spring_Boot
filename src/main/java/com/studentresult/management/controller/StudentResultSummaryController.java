
package com.studentresult.management.controller;

import com.studentresult.management.dto.StudentResultSummaryDTO;
import com.studentresult.management.service.StudentMarksService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student/result-summary")
public class StudentResultSummaryController {

    private final StudentMarksService studentMarksService;

    public StudentResultSummaryController(
            StudentMarksService studentMarksService) {
        this.studentMarksService = studentMarksService;
    }

    @GetMapping
    public StudentResultSummaryDTO getResultSummary(
            Authentication authentication) {

        return studentMarksService.getMyResultSummary(
                authentication.getName());
    }
}
