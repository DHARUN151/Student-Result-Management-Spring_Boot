package com.studentresult.management.service;

import com.studentresult.management.dto.StudentDashboardDTO;
import com.studentresult.management.entity.AcademicDetails;
import com.studentresult.management.entity.Student;
import com.studentresult.management.repository.StudentRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
public class StudentDashboardService {

    private final StudentRepository studentRepository;

    public StudentDashboardService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    @Transactional(readOnly = true)
    public StudentDashboardDTO getDashboard(Integer studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found with ID: " + studentId));

        AcademicDetails academic = student.getAcademicDetails();

        if (academic == null) {
            throw new RuntimeException(
                    "Academic details not found for student ID: " + studentId);
        }

        String programName = null;
        String departmentName = null;
        Integer semesterNumber = null;
        String academicYear = null;

        if (academic.getProgram() != null) {
            programName = academic.getProgram().getProgramName();

            if (academic.getProgram().getDepartment() != null) {
                departmentName =
                        academic.getProgram().getDepartment().getDepartmentName();
            }
        }

        if (academic.getCurrentSemester() != null) {
            semesterNumber =
                    academic.getCurrentSemester().getSemesterNumber();

            if (academic.getCurrentSemester().getAcademicYear() != null) {
                academicYear =
                        academic.getCurrentSemester()
                                .getAcademicYear()
                                .getYearName();
            }
        }

        return new StudentDashboardDTO(
                student.getStudentId(),
                student.getRegNum(),
                student.getName(),
                student.getEmail(),
                programName,
                departmentName,
                semesterNumber,
                academicYear
        );
    }
}
