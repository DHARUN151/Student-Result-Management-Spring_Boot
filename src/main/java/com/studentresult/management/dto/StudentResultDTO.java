package com.studentresult.management.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StudentResultDTO {

    private Integer resultId;
    private String examName;
    private Integer semesterNumber;
    private Integer totalMarks;
    private Integer maximumMarks;
    private BigDecimal percentage;
    private BigDecimal cgpa;
    private String resultClass;
    private String workflowStatus;
    private LocalDateTime publishedAt;

    public StudentResultDTO(
            Integer resultId,
            String examName,
            Integer semesterNumber,
            Integer totalMarks,
            Integer maximumMarks,
            BigDecimal percentage,
            BigDecimal cgpa,
            String resultClass,
            String workflowStatus,
            LocalDateTime publishedAt) {

        this.resultId = resultId;
        this.examName = examName;
        this.semesterNumber = semesterNumber;
        this.totalMarks = totalMarks;
        this.maximumMarks = maximumMarks;
        this.percentage = percentage;
        this.cgpa = cgpa;
        this.resultClass = resultClass;
        this.workflowStatus = workflowStatus;
        this.publishedAt = publishedAt;
    }

    public Integer getResultId() { return resultId; }
    public String getExamName() { return examName; }
    public Integer getSemesterNumber() { return semesterNumber; }
    public Integer getTotalMarks() { return totalMarks; }
    public Integer getMaximumMarks() { return maximumMarks; }
    public BigDecimal getPercentage() { return percentage; }
    public BigDecimal getCgpa() { return cgpa; }
    public String getResultClass() { return resultClass; }
    public String getWorkflowStatus() { return workflowStatus; }
    public LocalDateTime getPublishedAt() { return publishedAt; }
}
