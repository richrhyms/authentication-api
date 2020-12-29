package com.richotaru.authenticationapi.serviceImpl;

import com.richotaru.authenticationapi.dao.ClientSystemRepository;
import com.richotaru.authenticationapi.dao.ClientUserRepository;
import com.richotaru.authenticationapi.domain.entity.ClientSystem;
import com.richotaru.authenticationapi.domain.entity.ClientUser;
import com.richotaru.authenticationapi.domain.entity.PortalAccount;
import com.richotaru.authenticationapi.domain.enums.AccountTypeConstant;
import com.richotaru.authenticationapi.domain.enums.GenericStatusConstant;
import com.richotaru.authenticationapi.domain.enums.RoleConstant;
import com.richotaru.authenticationapi.domain.model.RequestPrincipal;
import com.richotaru.authenticationapi.domain.model.dto.*;
import com.richotaru.authenticationapi.domain.model.pojo.ClientSystemAuthPojo;
import com.richotaru.authenticationapi.domain.model.pojo.ClientSystemPojo;
import com.richotaru.authenticationapi.domain.model.pojo.ClientUserAuthPojo;
import com.richotaru.authenticationapi.domain.model.pojo.ClientUserPojo;
import com.richotaru.authenticationapi.service.ClientUserService;
import com.richotaru.authenticationapi.service.PortalAccountService;
import com.richotaru.authenticationapi.utils.JwtUtils;
import com.richotaru.authenticationapi.utils.sequenceGenerators.SequenceGenerator;
import com.richotaru.authenticationapi.utils.sequenceGenerators.qualifiers.PortalAccountCodeSequence;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */

@Service
public class ClientUserServiceImpl implements ClientUserService {
    private final ClientSystemRepository clientSystemRepository;
    private final ClientUserRepository clientUserRepository;
    private final PortalAccountService portalAccountService;
    private final RequestPrincipal requestPrincipal;
    private final SequenceGenerator sequenceGenerator;
    private final JwtUtils jwtUtils;

    public ClientUserServiceImpl(ClientSystemRepository clientSystemRepository,
                                 ClientUserRepository clientUserRepository, PortalAccountService portalAccountService,
                                 RequestPrincipal requestPrincipal, @PortalAccountCodeSequence SequenceGenerator sequenceGenerator,
                                 JwtUtils jwtUtils) {
        this.clientSystemRepository = clientSystemRepository;
        this.clientUserRepository = clientUserRepository;
        this.portalAccountService = portalAccountService;
        this.requestPrincipal = requestPrincipal;
        this.sequenceGenerator = sequenceGenerator;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    @Override
    public ClientUserPojo createClientUser(ClientUserDto dto) {
        AccountCreationDto creationDto = new AccountCreationDto();
        creationDto.setAccountType(AccountTypeConstant.CLIENT_SYSTEM);
        creationDto.setDisplayName(dto.getDisplayName());
        creationDto.setUsername(dto.getEmail());
        creationDto.setPassword(dto.getPassword());
        creationDto.setRoles(dto.getRoles());
        PortalAccount portalAccount = portalAccountService.createPortalAccount(creationDto, false);

        ClientUser user = new ClientUser();
        BeanUtils.copyProperties(dto, user);

        user.setPortalAccount(portalAccount);
        user.setAccountCode(sequenceGenerator.getNext());
        user.setClientSystem(requestPrincipal.getClient());
        user.setDateCreated(new Timestamp(new java.util.Date().getTime()));
        user.setLastUpdated(new Timestamp(new java.util.Date().getTime()));
        user.setStatus(GenericStatusConstant.ACTIVE);

        return new ClientUserPojo(clientUserRepository.save(user));
    }
    @Transactional
    @Override
    public ClientUserAuthPojo authenticateClientUser(ClientUserAuthDto dto) throws UsernameNotFoundException {
        ClientUser user = clientUserRepository.findClientUserByEmailAndStatus(dto.getUsername(),GenericStatusConstant.ACTIVE).orElseThrow(()
                -> new UsernameNotFoundException("User not found"));

        String jwtToken = user.getPortalAccount().getJwtToken();

        if(!jwtUtils.validateToken(jwtToken, user.getEmail())) {
            jwtToken = jwtUtils.generateToken(user.getPortalAccount().getUsername(), false);
            PortalAccount portalAccount = user.getPortalAccount();
            portalAccount.setJwtToken(jwtToken);
            portalAccountService.savePortalAccount(portalAccount);
        }

        Date expirationDate = jwtUtils.extractExpiration(jwtToken);

        ClientUserAuthPojo response = new ClientUserAuthPojo();
        response.setJwtToken(jwtToken);
        response.setExpirationDate(expirationDate);
        return response;
    }
    @Transactional
    @Override
    public ClientUserPojo getAuthenticatedClientUser(String emailAddress) throws UsernameNotFoundException {
        ClientUser user = clientUserRepository.findClientUserByEmailAndStatus(emailAddress, GenericStatusConstant.ACTIVE).orElseThrow(()
                -> new UsernameNotFoundException("User not found"));
        ClientUserPojo pojo = new ClientUserPojo(user);
        pojo.setRoles(RoleConstant.getValidRolesAsSet(user.getPortalAccount().getRoles()));
        return pojo;
    }
}
