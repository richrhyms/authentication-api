package com.richotaru.authenticationapi.dao;

import com.richotaru.authenticationapi.entity.WorkSpace;
import com.richotaru.authenticationapi.entity.WorkSpaceMembership;
import com.richotaru.authenticationapi.entity.WorkSpaceUser;
import com.richotaru.authenticationapi.enumeration.GenericStatusConstant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkSpaceMembershipRepository extends JpaRepository<WorkSpaceMembership, Long> {
    Optional<WorkSpaceMembership> findByWorkSpaceAndWorkSpaceUserAndStatus(WorkSpace workSpace, WorkSpaceUser user, GenericStatusConstant status);
    List<WorkSpaceMembership> findAllByWorkSpaceUserAndStatus(WorkSpaceUser user, GenericStatusConstant status);
}
