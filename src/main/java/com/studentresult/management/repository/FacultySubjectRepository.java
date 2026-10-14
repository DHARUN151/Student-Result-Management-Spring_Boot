package com.studentresult.management.repository;

import com.studentresult.management.entity.FacultySubject;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface FacultySubjectRepository
        extends JpaRepository<FacultySubject, Integer> {

    boolean existsByFaculty_FacultyIdAndOffering_OfferingIdAndStatus(
            Integer facultyId,
            Integer offeringId,
            String status);
    List<FacultySubject> findByFaculty_FacultyIdAndStatus(
            Integer facultyId, String status);
}
