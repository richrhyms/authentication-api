package com.richotaru.authenticationapi.dao;

import com.richotaru.authenticationapi.entity.WorkSpace;
import com.richotaru.authenticationapi.enumeration.GenericStatusConstant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkSpaceRepository extends JpaRepository<WorkSpace, Long> {
    Optional<WorkSpace> findByUsernameAndStatus(String username, GenericStatusConstant constant);
}
