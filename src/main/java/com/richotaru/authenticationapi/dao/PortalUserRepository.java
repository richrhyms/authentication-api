package com.richotaru.authenticationapi.dao;
import com.richotaru.authenticationapi.domain.entity.PortalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface PortalUserRepository extends JpaRepository<PortalUser, Long> {
//    Optional<PortalUser> findPortalUserByUsernameAndC(String clientName);

}
