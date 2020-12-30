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
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.inject.Provider;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */

@Service
public class ClientUserServiceImpl implements ClientUserService {
    private final ClientUserRepository clientUserRepository;
    private final PortalAccountService portalAccountService;
    private final Provider<RequestPrincipal> requestPrincipalProvider;
    private final SequenceGenerator sequenceGenerator;
    private final JwtUtils jwtUtils;

    public ClientUserServiceImpl(ClientUserRepository clientUserRepository,
                                 PortalAccountService portalAccountService,
                                 Provider<RequestPrincipal> requestPrincipalProvider,
                                 @PortalAccountCodeSequence SequenceGenerator sequenceGenerator,
                                 JwtUtils jwtUtils) {
        this.clientUserRepository = clientUserRepository;
        this.portalAccountService = portalAccountService;
        this.requestPrincipalProvider = requestPrincipalProvider;
        this.sequenceGenerator = sequenceGenerator;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    @Override
    public ClientUserPojo createClientUser(ClientUserDto dto) {
        try {
            ClientSystem client = requestPrincipalProvider.get().getClient();
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
            user.setClientSystem(client);
            user.setDateCreated(new Timestamp(new java.util.Date().getTime()));
            user.setLastUpdated(new Timestamp(new java.util.Date().getTime()));
            user.setStatus(GenericStatusConstant.ACTIVE);

            return new ClientUserPojo(clientUserRepository.save(user));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
