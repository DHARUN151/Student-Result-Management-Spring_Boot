package com.studentresult.management.dto;

import java.time.LocalDateTime;

public class FacultyMarksDTO {

    private Integer markId;
    private Integer studentId;
    private String studentName;
    private Integer offeringId;
    private Integer examId;
    private Integer internalMark;
    private Integer externalMark;
    private Integer totalMark;
    private String grade;
    private String resultOutcome;
    private Integer enteredBy;
    private LocalDateTime enteredAt;
    private Integer updatedBy;
    private LocalDateTime updatedAt;

    public FacultyMarksDTO(
            Integer markId,
            Integer studentId,
            String studentName,
            Integer offeringId,
            Integer examId,
            Integer internalMark,
            Integer externalMark,
            Integer totalMark,
            String grade,
            String resultOutcome,
            Integer enteredBy,
            LocalDateTime enteredAt,
            Integer updatedBy,
            LocalDateTime updatedAt) {

        this.markId = markId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.offeringId = offeringId;
        this.examId = examId;
        this.internalMark = internalMark;
        this.externalMark = externalMark;
        this.totalMark = totalMark;
        this.grade = grade;
        this.resultOutcome = resultOutcome;
        this.enteredBy = enteredBy;
        this.enteredAt = enteredAt;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
    }

    public Integer getMarkId() { return markId; }
    public Integer getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public Integer getOfferingId() { return offeringId; }
    public Integer getExamId() { return examId; }
    public Integer getInternalMark() { return internalMark; }
    public Integer getExternalMark() { return externalMark; }
    public Integer getTotalMark() { return totalMark; }
    public String getGrade() { return grade; }
    public String getResultOutcome() { return resultOutcome; }
    public Integer getEnteredBy() { return enteredBy; }
    public LocalDateTime getEnteredAt() { return enteredAt; }
    public Integer getUpdatedBy() { return updatedBy; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
