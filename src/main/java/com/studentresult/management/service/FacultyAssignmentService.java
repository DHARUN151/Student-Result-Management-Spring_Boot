
package com.studentresult.management.service;

import com.studentresult.management.dto.FacultyAssignmentDTO;
import com.studentresult.management.entity.FacultySubject;
import com.studentresult.management.entity.User;
import com.studentresult.management.repository.FacultySubjectRepository;
import com.studentresult.management.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FacultyAssignmentService {

    private final FacultySubjectRepository facultySubjectRepository;
    private final UserRepository userRepository;

    public FacultyAssignmentService(
            FacultySubjectRepository facultySubjectRepository,
            UserRepository userRepository) {
        this.facultySubjectRepository = facultySubjectRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<FacultyAssignmentDTO> getMyAssignments(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Authenticated user not found"));

        Integer facultyId = user.getFacultyId();

        if (facultyId == null) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "No faculty profile is linked to this account");
        }

        List<FacultySubject> assignments =
                facultySubjectRepository
                        .findByFaculty_FacultyIdAndStatus(
                                facultyId, "ACTIVE");

        return assignments.stream()
                .map(assignment -> {
                    var offering = assignment.getOffering();
                    var subject = offering.getSubject();
                    var semester = offering.getSemester();
                    var academicYear = offering.getAcademicYear();

                    return new FacultyAssignmentDTO(
                            assignment.getAssignmentId(),
                            offering.getOfferingId(),
                            subject.getSubjectCode(),
                            subject.getSubjectName(),
                            semester.getSemesterNumber(),
                            academicYear.getYearName(),
                            assignment.getStatus());
                })
                .toList();
    }
}
