
package com.studentresult.management.controller;

import com.studentresult.management.dto.FacultyAssignmentDTO;
import com.studentresult.management.service.FacultyAssignmentService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/faculty/assignments")
public class FacultyAssignmentController {

    private final FacultyAssignmentService facultyAssignmentService;

    public FacultyAssignmentController(
            FacultyAssignmentService facultyAssignmentService) {
        this.facultyAssignmentService = facultyAssignmentService;
    }

    @GetMapping
    public List<FacultyAssignmentDTO> getMyAssignments(
            Authentication authentication) {

        return facultyAssignmentService.getMyAssignments(
                authentication.getName());
    }
}
