
package com.studentresult.management.dto;

public class StudentResultSummaryDTO {

    private Integer totalSubjects;
    private Integer subjectsPassed;
    private Integer subjectsFailed;
    private Double passPercentage;

    private Integer gradeO;
    private Integer gradeAPlus;
    private Integer gradeA;
    private Integer gradeBPlus;
    private Integer gradeB;
    private Integer gradeC;
    private Integer gradeF;

    public StudentResultSummaryDTO(
            Integer totalSubjects,
            Integer subjectsPassed,
            Integer subjectsFailed,
            Double passPercentage,
            Integer gradeO,
            Integer gradeAPlus,
            Integer gradeA,
            Integer gradeBPlus,
            Integer gradeB,
            Integer gradeC,
            Integer gradeF) {

        this.totalSubjects = totalSubjects;
        this.subjectsPassed = subjectsPassed;
        this.subjectsFailed = subjectsFailed;
        this.passPercentage = passPercentage;
        this.gradeO = gradeO;
        this.gradeAPlus = gradeAPlus;
        this.gradeA = gradeA;
        this.gradeBPlus = gradeBPlus;
        this.gradeB = gradeB;
        this.gradeC = gradeC;
        this.gradeF = gradeF;
    }

    public Integer getTotalSubjects() { return totalSubjects; }
    public Integer getSubjectsPassed() { return subjectsPassed; }
    public Integer getSubjectsFailed() { return subjectsFailed; }
    public Double getPassPercentage() { return passPercentage; }

    public Integer getGradeO() { return gradeO; }
    public Integer getGradeAPlus() { return gradeAPlus; }
    public Integer getGradeA() { return gradeA; }
    public Integer getGradeBPlus() { return gradeBPlus; }
    public Integer getGradeB() { return gradeB; }
    public Integer getGradeC() { return gradeC; }
    public Integer getGradeF() { return gradeF; }
}
