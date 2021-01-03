package com.richotaru.authenticationapi.service;
import com.richotaru.authenticationapi.domain.entity.ClientSystem;
import com.richotaru.authenticationapi.domain.entity.PortalAccount;
import com.richotaru.authenticationapi.domain.model.dto.AccountAuthDto;
import com.richotaru.authenticationapi.domain.model.dto.AccountCreationDto;
import com.richotaru.authenticationapi.domain.model.dto.ClientSystemAuthDto;
import com.richotaru.authenticationapi.domain.model.pojo.ClientSystemAuthPojo;
import com.richotaru.authenticationapi.domain.model.pojo.PortalAccountAuthPojo;
import com.richotaru.authenticationapi.domain.model.pojo.PortalAccountPojo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.sound.sampled.Port;
import java.util.Optional;
import java.util.function.Supplier;

public interface PortalAccountService {
    PortalAccount createPortalAccount(AccountCreationDto dto,  boolean isClient);
    PortalAccount updatePortalAccount(PortalAccount account);
    PortalAccountAuthPojo authenticate(AccountAuthDto dto) throws Exception;
    PortalAccountPojo getAuthenticatedAccount(String username) throws UsernameNotFoundException;
}
