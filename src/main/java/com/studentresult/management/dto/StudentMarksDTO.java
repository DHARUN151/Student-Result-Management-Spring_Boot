
package com.studentresult.management.dto;

import java.time.LocalDateTime;

public class StudentMarksDTO {

    private Integer markId;
    private Integer offeringId;
    private String subjectName;
    private Integer semester;
    private String academicYear;
    private Integer examId;
    private String examName;
    private Integer internalMark;
    private Integer externalMark;
    private Integer totalMark;
    private String grade;
    private String resultOutcome;
    private LocalDateTime enteredAt;

    public StudentMarksDTO(
            Integer markId,
            Integer offeringId,
            String subjectName,
            Integer semester,
            String academicYear,
            Integer examId,
            String examName,
            Integer internalMark,
            Integer externalMark,
            Integer totalMark,
            String grade,
            String resultOutcome,
            LocalDateTime enteredAt) {

        this.markId = markId;
        this.offeringId = offeringId;
        this.subjectName = subjectName;
        this.semester = semester;
        this.academicYear = academicYear;
        this.examId = examId;
        this.examName = examName;
        this.internalMark = internalMark;
        this.externalMark = externalMark;
        this.totalMark = totalMark;
        this.grade = grade;
        this.resultOutcome = resultOutcome;
        this.enteredAt = enteredAt;
    }

    public Integer getMarkId() {
        return markId;
    }

    public Integer getOfferingId() {
        return offeringId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public Integer getSemester() {
        return semester;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public Integer getExamId() {
        return examId;
    }

    public String getExamName() {
        return examName;
    }

    public Integer getInternalMark() {
        return internalMark;
    }

    public Integer getExternalMark() {
        return externalMark;
    }

    public Integer getTotalMark() {
        return totalMark;
    }

    public String getGrade() {
        return grade;
    }

    public String getResultOutcome() {
        return resultOutcome;
    }

    public LocalDateTime getEnteredAt() {
        return enteredAt;
    }
}
