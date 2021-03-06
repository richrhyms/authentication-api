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
import com.richotaru.authenticationapi.domain.model.pojo.ClientUserAuthPojo;
import com.richotaru.authenticationapi.domain.model.pojo.ClientUserPojo;
import com.richotaru.authenticationapi.service.ClientUserService;
import com.richotaru.authenticationapi.service.PortalAccountService;
import com.richotaru.authenticationapi.utils.JwtUtils;
import com.richotaru.authenticationapi.utils.sequenceGenerators.SequenceGenerator;
import com.richotaru.authenticationapi.utils.sequenceGenerators.qualifiers.PortalAccountCodeSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.inject.Provider;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */

@Service
public class ClientUserServiceImpl implements ClientUserService {
    private final ClientUserRepository clientUserRepository;
    private final ClientSystemRepository clientSystemRepository;
    private final PortalAccountService portalAccountService;
    private final Provider<RequestPrincipal> requestPrincipalProvider;
    private final SequenceGenerator sequenceGenerator;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ClientUserServiceImpl(ClientUserRepository clientUserRepository,
                                 ClientSystemRepository clientSystemRepository,
                                 PortalAccountService portalAccountService,
                                 Provider<RequestPrincipal> requestPrincipalProvider,
                                 @PortalAccountCodeSequence SequenceGenerator sequenceGenerator) {
        this.clientUserRepository = clientUserRepository;
        this.clientSystemRepository = clientSystemRepository;
        this.portalAccountService = portalAccountService;
        this.requestPrincipalProvider = requestPrincipalProvider;
        this.sequenceGenerator = sequenceGenerator;
    }

    @Transactional
    @Override
    public ClientUserPojo createClientUser(ClientUserDto dto) {
        try {
            ClientSystem client = requestPrincipalProvider.get().getPortalAccount().getClient();
            Optional<ClientSystem> clientSystem = clientSystemRepository.findById(client.getId());
            AccountCreationDto creationDto = new AccountCreationDto();
            creationDto.setAccountType(AccountTypeConstant.CLIENT_USER);
            creationDto.setDisplayName(dto.getDisplayName());
            creationDto.setUsername(dto.getEmail());
            creationDto.setPassword(dto.getPassword());
            creationDto.setRoles(dto.getRoles());
            PortalAccount portalAccount = portalAccountService.createPortalAccount(creationDto, false);

            ClientUser user = new ClientUser();
            BeanUtils.copyProperties(dto, user);

            user.setPortalAccount(portalAccount);
            user.setAccountCode(sequenceGenerator.getNext());
            user.setDateCreated(new Timestamp(new java.util.Date().getTime()));
            user.setLastUpdated(new Timestamp(new java.util.Date().getTime()));
            user.setStatus(GenericStatusConstant.ACTIVE);
            ClientUser savedUser = clientUserRepository.save(user);


            logger.info("Client ID::" +client.getId()+ "CLIENT INFO {}", client);
            savedUser.setClientSystem(client);
            clientUserRepository.save(savedUser);

            return new ClientUserPojo(savedUser);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Transactional
    @Override
    public ClientUser getClientUser(String emailAddress) throws UsernameNotFoundException {
//        ClientUser user =
//        ClientUserPojo pojo = new ClientUserPojo(user);
//        pojo.setRoles(RoleConstant.getValidRolesAsSet(user.getPortalAccount().getRoles()));
        return  clientUserRepository.findClientUserByEmailAndStatus(emailAddress, GenericStatusConstant.ACTIVE).orElseThrow(()
                -> new UsernameNotFoundException("User not found"));
    }
}
