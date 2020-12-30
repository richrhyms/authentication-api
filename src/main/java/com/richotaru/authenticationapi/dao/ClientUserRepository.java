package com.richotaru.authenticationapi.dao;
import com.richotaru.authenticationapi.domain.entity.ClientUser;
import com.richotaru.authenticationapi.domain.enums.GenericStatusConstant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@Repository
public interface ClientUserRepository extends JpaRepository<ClientUser, Long> {
    @Query("select cu from client_user cu left join fetch cu.portalAccount pa left join fetch cu.clientSystem cs" +
            " where cu.email = ?1 AND cu.status = ?2 ")
    Optional<ClientUser> findClientUserByEmailAndStatus(String clientName, GenericStatusConstant constant);
}
