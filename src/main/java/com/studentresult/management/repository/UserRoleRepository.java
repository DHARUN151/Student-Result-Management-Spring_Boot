package com.studentresult.management.repository;

import com.studentresult.management.entity.UserRole;
import com.studentresult.management.entity.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleRepository
        extends JpaRepository<UserRole, UserRoleId> {

    @Query("""
        SELECT r.roleName
        FROM UserRole ur
        JOIN ur.role r
        WHERE ur.user.username = :username
        """)
    List<String> findRoleNamesByUsername(
            @Param("username") String username);
}
