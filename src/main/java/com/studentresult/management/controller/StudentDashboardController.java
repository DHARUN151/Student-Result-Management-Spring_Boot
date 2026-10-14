package com.studentresult.management.controller;

import com.studentresult.management.entity.User;
import com.studentresult.management.repository.UserRepository;
import com.studentresult.management.service.StudentDashboardService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import com.studentresult.management.dto.StudentResultDTO;
import com.studentresult.management.service.StudentResultService;
import com.studentresult.management.dto.StudentResultDTO;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/student")
public class StudentDashboardController {

    private final UserRepository userRepository;
    private final StudentDashboardService dashboardService;
    private final StudentResultService studentResultService;

    public StudentDashboardController(
            UserRepository userRepository,
            StudentDashboardService dashboardService, StudentResultService studentResultService) {
        this.userRepository = userRepository;
        this.dashboardService = dashboardService;
        this.studentResultService = studentResultService;
    }

    @GetMapping(
            value = "/dashboard",
            produces = "application/json"
    )
    public Object getDashboard(Authentication authentication) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        if (user.getStudentId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "This account is not linked to a student");
        }

        return dashboardService.getDashboard(user.getStudentId());
    }

    @GetMapping("/results")

    public java.util.List<StudentResultDTO> getStudentResults(
            Authentication authentication) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        if (user.getStudentId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "This account is not linked to a student");
        }

        return studentResultService.getStudentResults(
                user.getStudentId());
    }

    @GetMapping("/results/{resultId}")
    public StudentResultDTO getStudentResult(
            @PathVariable Integer resultId,
            Authentication authentication) {

        User user = userRepository
                .findByUsername(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        if (user.getStudentId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "This account is not linked to a student");
        }

        return studentResultService.getStudentResult(
                resultId,
                user.getStudentId());
    }
}
