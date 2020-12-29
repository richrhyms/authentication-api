package com.richotaru.authenticationapi.serviceImpl;
import com.google.common.collect.Lists;
import com.richotaru.authenticationapi.dao.ClientSystemRepository;
import com.richotaru.authenticationapi.dao.PortalAccountRepository;
import com.richotaru.authenticationapi.domain.entity.ClientSystem;
import com.richotaru.authenticationapi.domain.entity.PortalAccount;
import com.richotaru.authenticationapi.domain.enums.AccountTypeConstant;
import com.richotaru.authenticationapi.domain.enums.GenericStatusConstant;
import com.richotaru.authenticationapi.domain.enums.RoleConstant;
import com.richotaru.authenticationapi.domain.model.dto.AccountCreationDto;
import com.richotaru.authenticationapi.domain.model.dto.ClientSystemAuthDto;
import com.richotaru.authenticationapi.domain.model.dto.ClientSystemDto;
import com.richotaru.authenticationapi.domain.model.pojo.ClientSystemAuthPojo;
import com.richotaru.authenticationapi.domain.model.pojo.ClientSystemPojo;
import com.richotaru.authenticationapi.service.ClientSystemService;
import com.richotaru.authenticationapi.service.PortalAccountService;
import com.richotaru.authenticationapi.utils.JwtUtils;
import com.richotaru.authenticationapi.utils.sequenceGenerators.SequenceGenerator;
import com.richotaru.authenticationapi.utils.sequenceGenerators.qualifiers.ClientSystemCodeSequence;
import com.richotaru.authenticationapi.utils.sequenceGenerators.qualifiers.PortalAccountCodeSequence;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */

@Service
public class PortalAccountServiceImpl implements PortalAccountService {
    private final PortalAccountRepository portalAccountRepository;
    private final SequenceGenerator sequenceGenerator;
    private final JwtUtils jwtUtils;

    public PortalAccountServiceImpl(PortalAccountRepository portalAccountRepository,
                                    @PortalAccountCodeSequence SequenceGenerator sequenceGenerator,
                                    JwtUtils jwtUtils) {
        this.portalAccountRepository = portalAccountRepository;
        this.sequenceGenerator = sequenceGenerator;
        this.jwtUtils = jwtUtils;
    }
    @Transactional
    @Override
    public PortalAccount createPortalAccount(AccountCreationDto dto, boolean isClient) {
        PortalAccount portalAccount = new PortalAccount();
        portalAccount.setDisplayName(dto.getDisplayName());
        portalAccount.setUsername(dto.getUsername());
        portalAccount.setPassword(dto.getPassword());
        portalAccount.setAccountCode(sequenceGenerator.getNext());
        portalAccount.setAccountType(dto.getAccountType());
        portalAccount.setJwtToken(jwtUtils.generateToken(portalAccount.getUsername(), isClient));
        portalAccount.setDateCreated(new Timestamp(new java.util.Date().getTime()));
        portalAccount.setLastUpdated(new Timestamp(new java.util.Date().getTime()));
        portalAccount.setStatus(GenericStatusConstant.ACTIVE);
        portalAccount.setRoles(RoleConstant.getValidRolesAsString(dto.getRoles()));
       return portalAccountRepository.save(portalAccount);
    }

    @Transactional
    @Override
    public PortalAccount savePortalAccount(PortalAccount account) {
        return portalAccountRepository.save(account);
    }
}
