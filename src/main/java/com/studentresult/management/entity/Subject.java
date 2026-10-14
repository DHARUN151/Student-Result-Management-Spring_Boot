package com.studentresult.management.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "subjects", schema = "enterprise")
public class Subject {

    @Id
    @Column(name = "subject_id")
    private Integer subjectId;

    @Column(name = "subject_code")
    private String subjectCode;

    @Column(name = "subject_name")
    private String subjectName;

    @Column(name = "credits")
    private Integer credits;

    @Column(name = "subject_type")
    private String subjectType;

    @Column(name = "max_internal_marks")
    private Integer maxInternalMarks;

    @Column(name = "max_external_marks")
    private Integer maxExternalMarks;

    @Column(name = "pass_marks")
    private Integer passMarks;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Subject() {
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public Integer getMaxInternalMarks() {
        return maxInternalMarks;
    }

    public void setMaxInternalMarks(Integer maxInternalMarks) {
        this.maxInternalMarks = maxInternalMarks;
    }

    public Integer getMaxExternalMarks() {
        return maxExternalMarks;
    }

    public void setMaxExternalMarks(Integer maxExternalMarks) {
        this.maxExternalMarks = maxExternalMarks;
    }

    public Integer getPassMarks() {
        return passMarks;
    }

    public void setPassMarks(Integer passMarks) {
        this.passMarks = passMarks;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}