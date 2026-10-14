package com.studentresult.management.dto;

public class StudentDashboardDTO {

    private Integer studentId;
    private String regNum;
    private String name;
    private String email;

    private String programName;
    private String departmentName;
    private Integer semesterNumber;
    private String academicYear;

    public StudentDashboardDTO() {
    }

    public StudentDashboardDTO(
            Integer studentId,
            String regNum,
            String name,
            String email,
            String programName,
            String departmentName,
            Integer semesterNumber,
            String academicYear) {

        this.studentId = studentId;
        this.regNum = regNum;
        this.name = name;
        this.email = email;
        this.programName = programName;
        this.departmentName = departmentName;
        this.semesterNumber = semesterNumber;
        this.academicYear = academicYear;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public String getRegNum() {
        return regNum;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProgramName() {
        return programName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public Integer getSemesterNumber() {
        return semesterNumber;
    }

    public String getAcademicYear() {
        return academicYear;
    }
}