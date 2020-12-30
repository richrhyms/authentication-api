package com.richotaru.authenticationapi.dao;
import com.richotaru.authenticationapi.domain.entity.PortalAccount;
import com.richotaru.authenticationapi.domain.enums.GenericStatusConstant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//@Repository
public interface PortalAccountRepository extends JpaRepository<PortalAccount, Long> {
    Optional<PortalAccount> findByUsernameAndStatus(String username, GenericStatusConstant constant);
}
