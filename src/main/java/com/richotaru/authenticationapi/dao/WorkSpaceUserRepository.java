package com.richotaru.authenticationapi.dao;

import com.richotaru.authenticationapi.entity.WorkSpaceUser;
import com.richotaru.authenticationapi.enumeration.GenericStatusConstant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkSpaceUserRepository extends JpaRepository<WorkSpaceUser, Long> {
    @Query("select wu from WorkSpaceUser wu " +
            " where wu.email = ?1 AND wu.status = ?2 ")
    Optional<WorkSpaceUser> findWorkSpaceUserByEmailAndStatus(String clientName, GenericStatusConstant constant);

    @Query("select wu from WorkSpaceUser wu " +
            " where wu.username = ?1 AND wu.status = ?2 ")
    Optional<WorkSpaceUser> findByUsernameAndStatus(String username, GenericStatusConstant constant);

}
