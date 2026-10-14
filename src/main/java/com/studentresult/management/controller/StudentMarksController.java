
package com.studentresult.management.controller;

import com.studentresult.management.dto.StudentMarksDTO;
import com.studentresult.management.service.StudentMarksService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student/marks")
public class StudentMarksController {

    private final StudentMarksService studentMarksService;

    public StudentMarksController(
            StudentMarksService studentMarksService) {
        this.studentMarksService = studentMarksService;
    }

    @GetMapping
    public List<StudentMarksDTO> getMyMarks(
            Authentication authentication) {

        return studentMarksService.getMyMarks(
                authentication.getName());
    }
}
