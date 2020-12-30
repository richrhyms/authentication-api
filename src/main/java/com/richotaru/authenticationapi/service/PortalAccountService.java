package com.richotaru.authenticationapi.service;
import com.richotaru.authenticationapi.domain.entity.PortalAccount;
import com.richotaru.authenticationapi.domain.model.dto.AccountCreationDto;

import javax.sound.sampled.Port;
import java.util.Optional;
import java.util.function.Supplier;

public interface PortalAccountService {
    PortalAccount createPortalAccount(AccountCreationDto dto,  boolean isClient);
    PortalAccount savePortalAccount(PortalAccount account);
}
