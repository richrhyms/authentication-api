package com.richotaru.authenticationapi.serviceImpl;
import com.richotaru.authenticationapi.dao.ClientSystemRepository;
import com.richotaru.authenticationapi.dao.ClientUserRepository;
import com.richotaru.authenticationapi.dao.PortalAccountRepository;
import com.richotaru.authenticationapi.domain.entity.ClientSystem;
import com.richotaru.authenticationapi.domain.entity.ClientUser;
import com.richotaru.authenticationapi.domain.entity.PortalAccount;
import com.richotaru.authenticationapi.domain.enums.AccountTypeConstant;
import com.richotaru.authenticationapi.domain.enums.GenericStatusConstant;
import com.richotaru.authenticationapi.domain.enums.RoleConstant;
import com.richotaru.authenticationapi.domain.model.dto.AccountAuthDto;
import com.richotaru.authenticationapi.domain.model.dto.AccountCreationDto;
import com.richotaru.authenticationapi.domain.model.pojo.PortalAccountAuthPojo;
import com.richotaru.authenticationapi.domain.model.pojo.PortalAccountPojo;
import com.richotaru.authenticationapi.service.ClientUserService;
import com.richotaru.authenticationapi.service.PortalAccountService;
import com.richotaru.authenticationapi.utils.JwtUtils;
import com.richotaru.authenticationapi.utils.sequenceGenerators.SequenceGenerator;
import com.richotaru.authenticationapi.utils.sequenceGenerators.qualifiers.PortalAccountCodeSequence;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */

@Service
public class PortalAccountServiceImpl implements PortalAccountService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PortalAccountRepository portalAccountRepository;
    private final ClientSystemRepository clientSystemRepository;
    private final ClientUserRepository clientUserRepository;
    private final SequenceGenerator sequenceGenerator;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public PortalAccountServiceImpl(PortalAccountRepository portalAccountRepository,
                                    ClientSystemRepository clientSystemRepository,
                                    ClientUserRepository clientUserRepository, PasswordEncoder passwordEncoder,
                                    @PortalAccountCodeSequence SequenceGenerator sequenceGenerator,
                                    JwtUtils jwtUtils) {
        this.portalAccountRepository = portalAccountRepository;
        this.clientSystemRepository = clientSystemRepository;
        this.clientUserRepository = clientUserRepository;
        this.sequenceGenerator = sequenceGenerator;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;

    }
    @Transactional
    @Override
    public PortalAccount createPortalAccount(AccountCreationDto dto, boolean isClient) {
        PortalAccount portalAccount = new PortalAccount();
        portalAccount.setDisplayName(dto.getDisplayName());
        portalAccount.setUsername(dto.getUsername());
        portalAccount.setPassword(passwordEncoder.encode(dto.getPassword()));
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
    public PortalAccount updatePortalAccount(PortalAccount account) {
        return portalAccountRepository.save(account);
    }

    @Override
    public PortalAccountAuthPojo authenticate(AccountAuthDto dto) throws Exception {
        PortalAccount user = portalAccountRepository.findByUsernameAndStatus(dto.getUsername(),
                GenericStatusConstant.ACTIVE).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String jwtToken = jwtUtils.generateToken(user.getUsername(), true);
            user.setJwtToken(jwtToken);
            updatePortalAccount(user);

        try{
            if(passwordEncoder.matches(dto.getPassword(), user.getPassword())){
                Date expirationDate = jwtUtils.extractExpiration(jwtToken);

                PortalAccountAuthPojo response = new PortalAccountAuthPojo();
                response.setJwtToken(jwtToken);
                response.setExpirationDate(expirationDate);
                response.setAccountTypeConstant(user.getAccountType());
                response.setRoles(resolveRoles(user));
                logger.info("Authenticated");

                return response;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        throw new JwtException("Authentication Failed");
    }

    @Override
    public PortalAccountPojo getAuthenticatedAccount(String username) throws UsernameNotFoundException {
        PortalAccount portalAccount = portalAccountRepository.findByUsernameAndStatus(username,
                GenericStatusConstant.ACTIVE).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        PortalAccountPojo pojo = new PortalAccountPojo(portalAccount);

        if(portalAccount.getAccountType() == AccountTypeConstant.CLIENT_SYSTEM){
            ClientSystem clientSystem = clientSystemRepository.findByClientNameAndStatus(username,
                    GenericStatusConstant.ACTIVE).orElseThrow(()
                    -> new UsernameNotFoundException("Client System not found"));
            pojo.setClient(clientSystem);
        }
        if(portalAccount.getAccountType() == AccountTypeConstant.CLIENT_USER){
            ClientUser clientUser = clientUserRepository.findClientUserByEmailAndStatus(username,
                    GenericStatusConstant.ACTIVE).orElseThrow(()
                    -> new UsernameNotFoundException("Client User not found"));
            pojo.setUser(clientUser);
        }


        return pojo;
    }
    private List<SimpleGrantedAuthority> resolveRoles(PortalAccount account) {
        return Arrays.stream(account.getRoles().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
