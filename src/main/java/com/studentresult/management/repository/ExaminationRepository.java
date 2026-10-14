package com.studentresult.management.repository;

import com.studentresult.management.entity.Examination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExaminationRepository
        extends JpaRepository<Examination, Integer> {
}
