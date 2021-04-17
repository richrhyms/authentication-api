package com.richotaru.authenticationapi.dao;

import com.richotaru.authenticationapi.enumeration.GenericStatusConstant;
import com.richotaru.authenticationapi.entity.WorkSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkSpaceRepository extends JpaRepository<WorkSpace, Long> {

    Optional<WorkSpace> findByCodeAndStatus(String code, GenericStatusConstant status);

    @Query("select ws from WorkSpace ws left join fetch ws.clientSystem cs " +
            " where ws.code = ?1 AND ws.status = ?2 ")
    Optional<WorkSpace> findByCodeAndStatus_fetchJoinClientSystem(String code, GenericStatusConstant status);
}
