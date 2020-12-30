package com.richotaru.authenticationapi.dao;

import com.richotaru.authenticationapi.domain.entity.ClientSystem;
import com.richotaru.authenticationapi.domain.enums.GenericStatusConstant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@Repository
public interface ClientSystemRepository extends JpaRepository<ClientSystem, Long> {

    @Query("select cs from client_system cs left join fetch cs.portalAccount" +
            " where lower(cs.clientName) = lower(?1) AND cs.status =?2 ")
    Optional<ClientSystem> findByClientNameAndStatus(String clientName, GenericStatusConstant constant);

//    Optional<ClientSystem> findByClientNameAndStatus(String clientName,GenericStatusConstant constant);
}
