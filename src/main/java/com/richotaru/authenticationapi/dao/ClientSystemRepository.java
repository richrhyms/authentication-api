package com.richotaru.authenticationapi.dao;

import com.richotaru.authenticationapi.entity.ClientSystem;
import com.richotaru.authenticationapi.enumeration.GenericStatusConstant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientSystemRepository extends JpaRepository<ClientSystem, Long> {

    @Query("select cs from ClientSystem cs" +
            " where lower(cs.displayName) = lower(?1) AND cs.status =?2 ")
    Optional<ClientSystem> findByDisplayNameAndStatus(String clientName, GenericStatusConstant constant);

    @Query("select cs from ClientSystem cs" +
            " where lower(cs.code) = lower(?1) AND cs.status =?2 ")
    Optional<ClientSystem> findByClientCodeAndStatus(String clientName, GenericStatusConstant constant);
}
