package com.studentresult.management.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "marks", schema = "enterprise")
public class Marks {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "marks_id_generator"
    )
    @SequenceGenerator(
            name = "marks_id_generator",
            sequenceName = "marks_mark_id_seq",
            schema = "enterprise",
            allocationSize = 1
    )
    @Column(name = "mark_id")
    private Integer markId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offering_id", nullable = false)
    private SubjectOffering offering;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Examination examination;

    @Column(name = "internal_mark", nullable = false)
    private Integer internalMark;

    @Column(name = "external_mark", nullable = false)
    private Integer externalMark;

    @Column(name = "total_mark", nullable = false)
    private Integer totalMark;

    @Column(name = "grade")
    private String grade;

    @Column(name = "result_outcome")
    private String resultOutcome;

    @Column(name = "entered_by")
    private Integer enteredBy;

    @Column(name = "entered_at", nullable = false)
    private LocalDateTime enteredAt;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Integer version = 1;

    public Marks() {
    }

    public Integer getMarkId() {
        return markId;
    }

    public void setMarkId(Integer markId) {
        this.markId = markId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public SubjectOffering getOffering() {
        return offering;
    }

    public void setOffering(SubjectOffering offering) {
        this.offering = offering;
    }

    public Examination getExamination() {
        return examination;
    }

    public void setExamination(Examination examination) {
        this.examination = examination;
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

    public Integer getTotalMark() {
        return totalMark;
    }

    public void setTotalMark(Integer totalMark) {
        this.totalMark = totalMark;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getResultOutcome() {
        return resultOutcome;
    }

    public void setResultOutcome(String resultOutcome) {
        this.resultOutcome = resultOutcome;
    }

    public Integer getEnteredBy() {
        return enteredBy;
    }

    public void setEnteredBy(Integer enteredBy) {
        this.enteredBy = enteredBy;
    }

    public LocalDateTime getEnteredAt() {
        return enteredAt;
    }

    public void setEnteredAt(LocalDateTime enteredAt) {
        this.enteredAt = enteredAt;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
