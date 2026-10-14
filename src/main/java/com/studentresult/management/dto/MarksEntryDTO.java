package com.studentresult.management.dto;

import java.math.BigDecimal;

public class MarksEntryDTO {

    private Integer studentId;
    private Integer offeringId;
    private Integer examId;
    private Integer internalMark;
    private Integer externalMark;

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getOfferingId() {
        return offeringId;
    }

    public void setOfferingId(Integer offeringId) {
        this.offeringId = offeringId;
    }

    public Integer getExamId() {
        return examId;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
    }

    public Integer getInternalMark() {
        return internalMark;
    }

    public void setInternalMark(Integer internalMark) {
        this.internalMark = internalMark;
    }

    public Integer getExternalMark() {
        return externalMark;
    }

    public void setExternalMark(Integer externalMark) {
        this.externalMark = externalMark;
    }
}