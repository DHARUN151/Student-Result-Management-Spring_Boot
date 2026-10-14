package com.studentresult.management.repository;

import com.studentresult.management.entity.Marks;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MarksRepository
        extends JpaRepository<Marks, Integer> {
    boolean existsByStudent_StudentIdAndOffering_OfferingIdAndExamination_ExamId(
            Integer studentId,
            Integer offeringId,
            Integer examId);
    List<Marks> findByOffering_OfferingIdAndExamination_ExamId(
            Integer offeringId,
            Integer examId);
    List<Marks> findByStudent_StudentIdOrderByMarkIdDesc(
            Integer studentId);

    List<Marks> findByStudent_StudentIdOrderByEnteredAtDesc(
            Integer studentId);
}
