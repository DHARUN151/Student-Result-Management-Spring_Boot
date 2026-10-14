
package com.studentresult.management.repository;

import com.studentresult.management.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository
        extends JpaRepository<Permission, Integer> {
}
