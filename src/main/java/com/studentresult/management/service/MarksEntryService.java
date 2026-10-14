
package com.studentresult.management.service;

import com.studentresult.management.dto.MarksEntryDTO;
import com.studentresult.management.entity.Marks;
import com.studentresult.management.entity.SubjectOffering;
import com.studentresult.management.entity.User;
import com.studentresult.management.entity.Examination;
import com.studentresult.management.repository.ExaminationRepository;
import com.studentresult.management.repository.FacultySubjectRepository;
import com.studentresult.management.repository.MarksRepository;
import com.studentresult.management.repository.StudentRepository;
import com.studentresult.management.repository.SubjectOfferingRepository;
import com.studentresult.management.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class MarksEntryService {

    private final MarksRepository marksRepository;
    private final StudentRepository studentRepository;
    private final SubjectOfferingRepository subjectOfferingRepository;
    private final ExaminationRepository examinationRepository;
    private final UserRepository userRepository;
    private final FacultySubjectRepository facultySubjectRepository;

    public MarksEntryService(
            MarksRepository marksRepository,
            StudentRepository studentRepository,
            SubjectOfferingRepository subjectOfferingRepository,
            ExaminationRepository examinationRepository,
            UserRepository userRepository,
            FacultySubjectRepository facultySubjectRepository) {

        this.marksRepository = marksRepository;
        this.studentRepository = studentRepository;
        this.subjectOfferingRepository = subjectOfferingRepository;
        this.examinationRepository = examinationRepository;
        this.userRepository = userRepository;
        this.facultySubjectRepository = facultySubjectRepository;
    }

    @Transactional
    public Marks enterMarks(
            MarksEntryDTO dto,
            Integer facultyUserId) {

        validateDto(dto);
        User facultyUser = getFacultyUser(facultyUserId);

        SubjectOffering offering = subjectOfferingRepository
                .findById(dto.getOfferingId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Subject offering not found"));

        validateFacultyAssignment(facultyUser, dto.getOfferingId());
        validateMarks(dto, offering);

        var student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Student not found"));

        Examination examination = examinationRepository
                .findById(dto.getExamId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Examination not found"));

        validateExamination(offering, examination);

        boolean alreadyExists = marksRepository
                .existsByStudent_StudentIdAndOffering_OfferingIdAndExamination_ExamId(
                        dto.getStudentId(),
                        dto.getOfferingId(),
                        dto.getExamId());

        if (alreadyExists) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Marks already exist for this student, offering, and examination");
        }

        Marks marks = new Marks();
        marks.setStudent(student);
        marks.setOffering(offering);
        marks.setExamination(examination);
        marks.setInternalMark(dto.getInternalMark());
        marks.setExternalMark(dto.getExternalMark());

        int totalMark = calculateTotalMark(dto);
        marks.setTotalMark(totalMark);
        marks.setGrade(calculateGrade(totalMark));
        marks.setResultOutcome(calculateResultOutcome(totalMark));

        marks.setEnteredBy(facultyUserId);
        marks.setEnteredAt(LocalDateTime.now());

        return marksRepository.save(marks);
    }

    @Transactional
    public Marks updateMarks(
            Integer markId,
            MarksEntryDTO dto,
            Integer facultyUserId) {

        if (markId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Mark ID is required");
        }

        Marks existing = marksRepository.findById(markId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Marks record not found"));

        validateDto(dto);

        // Do not allow the record's student, offering, or examination
        // to be changed through a marks update.
        if (!existing.getStudent().getStudentId()
                .equals(dto.getStudentId())
                || !existing.getOffering().getOfferingId()
                .equals(dto.getOfferingId())
                || !existing.getExamination().getExamId()
                .equals(dto.getExamId())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Student, offering, and examination cannot be changed");
        }

        User facultyUser = getFacultyUser(facultyUserId);
        SubjectOffering offering = existing.getOffering();

        validateFacultyAssignment(
                facultyUser,
                offering.getOfferingId());

        validateMarks(dto, offering);

        int totalMark = calculateTotalMark(dto);

        existing.setInternalMark(dto.getInternalMark());
        existing.setExternalMark(dto.getExternalMark());
        existing.setTotalMark(totalMark);
        existing.setGrade(calculateGrade(totalMark));
        existing.setResultOutcome(calculateResultOutcome(totalMark));

        existing.setUpdatedBy(facultyUserId);
        existing.setUpdatedAt(LocalDateTime.now());

        return marksRepository.save(existing);
    }

    private void validateDto(MarksEntryDTO dto) {
        if (dto == null
                || dto.getStudentId() == null
                || dto.getOfferingId() == null
                || dto.getExamId() == null
                || dto.getInternalMark() == null
                || dto.getExternalMark() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "All marks fields are required");
        }
    }

    private User getFacultyUser(Integer facultyUserId) {
        if (facultyUserId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Faculty user is not authenticated");
        }

        return userRepository.findById(facultyUserId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Authenticated user not found"));
    }

    private void validateFacultyAssignment(
            User facultyUser,
            Integer offeringId) {

        Integer facultyId = facultyUser.getFacultyId();

        if (facultyId == null) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "User is not linked to a faculty record");
        }

        boolean assigned = facultySubjectRepository
                .existsByFaculty_FacultyIdAndOffering_OfferingIdAndStatus(
                        facultyId,
                        offeringId,
                        "ACTIVE");

        if (!assigned) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not assigned to this subject offering");
        }
    }

    private void validateMarks(
            MarksEntryDTO dto,
            SubjectOffering offering) {

        if (dto.getInternalMark() < 0
                || dto.getExternalMark() < 0) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Marks cannot be negative");
        }

        if (offering.getSubject() == null
                || offering.getSubject().getMaxInternalMarks() == null
                || offering.getSubject().getMaxExternalMarks() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Subject maximum marks are not configured");
        }

        if (dto.getInternalMark()
                > offering.getSubject().getMaxInternalMarks()
                || dto.getExternalMark()
                > offering.getSubject().getMaxExternalMarks()) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Marks exceed the maximum allowed for this subject");
        }
    }

    private void validateExamination(
            SubjectOffering offering,
            Examination examination) {

        if (offering.getSemester() == null
                || offering.getAcademicYear() == null
                || examination.getSemester() == null
                || examination.getAcademicYear() == null
                || !offering.getSemester().getSemesterId()
                .equals(examination.getSemester().getSemesterId())
                || !offering.getAcademicYear().getAcademicYearId()
                .equals(examination.getAcademicYear().getAcademicYearId())) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Examination does not match the subject offering");
        }
    }

    // Internal marks + external marks
    private int calculateTotalMark(MarksEntryDTO dto) {
        return dto.getInternalMark() + dto.getExternalMark();
    }

    // Grade scale confirmed for this application
    private String calculateGrade(int totalMark) {
        if (totalMark >= 90) return "O";
        if (totalMark >= 80) return "A+";
        if (totalMark >= 70) return "A";
        if (totalMark >= 60) return "B+";
        if (totalMark >= 50) return "B";
        if (totalMark >= 40) return "C";
        return "F";
    }

    // PASS when total marks are at least 40
    private String calculateResultOutcome(int totalMark) {
        return totalMark >= 40 ? "PASS" : "FAIL";
    }
}
