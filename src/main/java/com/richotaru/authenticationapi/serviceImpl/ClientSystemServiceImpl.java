package com.richotaru.authenticationapi.serviceImpl;
import com.richotaru.authenticationapi.domain.enums.AccountTypeConstant;
import com.google.common.collect.Lists;
import com.richotaru.authenticationapi.dao.ClientSystemRepository;
import com.richotaru.authenticationapi.domain.entity.ClientSystem;
import com.richotaru.authenticationapi.domain.entity.PortalAccount;
import com.richotaru.authenticationapi.domain.enums.GenericStatusConstant;
import com.richotaru.authenticationapi.domain.model.dto.AccountCreationDto;
import com.richotaru.authenticationapi.domain.model.dto.ClientSystemAuthDto;
import com.richotaru.authenticationapi.domain.model.dto.ClientSystemDto;
import com.richotaru.authenticationapi.domain.model.pojo.ClientSystemAuthPojo;
import com.richotaru.authenticationapi.domain.model.pojo.ClientSystemPojo;
import com.richotaru.authenticationapi.service.ClientSystemService;
import com.richotaru.authenticationapi.service.PortalAccountService;
import com.richotaru.authenticationapi.utils.JwtUtils;
import com.richotaru.authenticationapi.utils.sequenceGenerators.SequenceGenerator;
import com.richotaru.authenticationapi.utils.sequenceGenerators.qualifiers.PortalAccountCodeSequence;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */

@Service
public class ClientSystemServiceImpl implements ClientSystemService {
    private final ClientSystemRepository clientSystemRepository;
    private final PortalAccountService portalAccountService;
    private final SequenceGenerator sequenceGenerator;
    private final JwtUtils jwtUtils;

    public ClientSystemServiceImpl(ClientSystemRepository clientSystemRepository,
                                   PortalAccountService portalAccountService,
                                   @PortalAccountCodeSequence SequenceGenerator sequenceGenerator,
                                   JwtUtils jwtUtils) {
        this.clientSystemRepository = clientSystemRepository;
        this.portalAccountService = portalAccountService;
        this.sequenceGenerator = sequenceGenerator;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public ClientSystemPojo createClientSystem(ClientSystemDto dto) {
        AccountCreationDto creationDto = new AccountCreationDto();
        creationDto.setAccountType(AccountTypeConstant.CLIENT_SYSTEM);
        creationDto.setDisplayName(dto.getDisplayName());
        creationDto.setUsername(dto.getClientName());
        creationDto.setPassword(dto.getClientKey());
        creationDto.setRoles(dto.getRoles());
        PortalAccount portalAccount = portalAccountService.createPortalAccount(creationDto, true);

        ClientSystem clientSystem = new ClientSystem();
        clientSystem.setClientName(dto.getClientName());
        clientSystem.setClientCode(sequenceGenerator.getNext());
        clientSystem.setDisplayName(dto.getDisplayName());
        clientSystem.setUsers(Lists.newArrayList());
        clientSystem.setDateCreated(new Timestamp(new java.util.Date().getTime()));
        clientSystem.setLastUpdated(new Timestamp(new java.util.Date().getTime()));
        clientSystem.setStatus(GenericStatusConstant.ACTIVE);
        clientSystem.setPortalAccount(portalAccount);

        ClientSystem created = clientSystemRepository.save(clientSystem);
        return new ClientSystemPojo(created);
    }

    @Transactional
    @Override
    public ClientSystemAuthPojo authenticateClient(ClientSystemAuthDto dto) throws UsernameNotFoundException {
        ClientSystem user = clientSystemRepository.findByClientNameAndStatus(dto.getUsername(),GenericStatusConstant.ACTIVE).orElseThrow(()
                -> new UsernameNotFoundException("User not found"));

        String jwtToken = user.getPortalAccount().getJwtToken();

        if(!jwtUtils.validateToken(jwtToken, user.getClientName())) {
            jwtToken = jwtUtils.generateToken(user.getClientName(), true);
            PortalAccount portalAccount = user.getPortalAccount();
            portalAccount.setJwtToken(jwtToken);
            portalAccountService.savePortalAccount(portalAccount);
        }

        Date expirationDate = jwtUtils.extractExpiration(jwtToken);

        ClientSystemAuthPojo response = new ClientSystemAuthPojo();
        response.setJwtToken(jwtToken);
        response.setExpirationDate(expirationDate);
        return response;
    }
    @Transactional
    @Override
    public ClientSystemPojo getAuthenticatedClient(String username) throws UsernameNotFoundException {
        ClientSystem user = clientSystemRepository.findByClientNameAndStatus(username, GenericStatusConstant.ACTIVE).orElseThrow(()
                -> new UsernameNotFoundException("User not found"));
        ClientSystemPojo pojo = new ClientSystemPojo(user);
        pojo.setExpirationDate(new Timestamp(jwtUtils.extractExpiration(pojo.getJwtToken()).getTime()));
        return pojo;
    }
}
