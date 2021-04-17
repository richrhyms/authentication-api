package com.richotaru.authenticationapi.dao;

import com.richotaru.authenticationapi.entity.WorkSpaceMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkSpaceMembershipRepository extends JpaRepository<WorkSpaceMembership, Long> {

}
