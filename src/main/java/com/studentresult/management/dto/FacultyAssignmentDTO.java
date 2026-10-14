
package com.studentresult.management.dto;

public class FacultyAssignmentDTO {

    private Integer assignmentId;
    private Integer offeringId;
    private String subjectCode;
    private String subjectName;
    private Integer semester;
    private String academicYear;
    private String status;

    public FacultyAssignmentDTO(
            Integer assignmentId,
            Integer offeringId,
            String subjectCode,
            String subjectName,
            Integer semester,
            String academicYear,
            String status) {

        this.assignmentId = assignmentId;
        this.offeringId = offeringId;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.semester = semester;
        this.academicYear = academicYear;
        this.status = status;
    }

    public Integer getAssignmentId() {
        return assignmentId;
    }

    public Integer getOfferingId() {
        return offeringId;
    }

    public String getSubjectCode() {
        return subjectCode;
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

    public String getStatus() {
        return status;
    }
}
