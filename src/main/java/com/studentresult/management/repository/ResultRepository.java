
package com.studentresult.management.repository;

import com.studentresult.management.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
public interface ResultRepository
        extends JpaRepository<Result, Integer> {
    List<Result> findByStudent_StudentIdOrderByCreatedAtDesc(
            Integer studentId
    );

    Optional<Result> findByResultIdAndStudent_StudentId(
            Integer resultId,
            Integer studentId
    );
}