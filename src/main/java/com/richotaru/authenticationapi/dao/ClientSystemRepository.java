package com.richotaru.authenticationapi.dao;

import com.richotaru.authenticationapi.domain.entity.ClientSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@Repository
public interface ClientSystemRepository extends JpaRepository<ClientSystem, Long> {

    Optional<ClientSystem> findClientSystemByClientName(String clientName);
}
