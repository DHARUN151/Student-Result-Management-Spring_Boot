package com.studentresult.management.repository;

import com.studentresult.management.entity.ResultApproval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultApprovalRepository
        extends JpaRepository<ResultApproval, Integer> {

    List<ResultApproval> findByResult_ResultId(Integer resultId);
}
