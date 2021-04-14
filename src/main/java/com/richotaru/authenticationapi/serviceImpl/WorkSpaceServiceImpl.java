package com.richotaru.authenticationapi.serviceImpl;

import com.richotaru.authenticationapi.dao.ClientSystemRepository;
import com.richotaru.authenticationapi.dao.WorkSpaceRepository;
import com.richotaru.authenticationapi.domain.model.dto.WorkSpaceCreationDto;
import com.richotaru.authenticationapi.entity.ClientSystem;
import com.richotaru.authenticationapi.entity.WorkSpace;
import com.richotaru.authenticationapi.enumeration.GenericStatusConstant;
import com.richotaru.authenticationapi.service.ClientSystemService;
import com.richotaru.authenticationapi.service.WorkSpaceService;
import com.richotaru.authenticationapi.utils.JwtUtils;
import com.richotaru.authenticationapi.utils.sequenceGenerators.SequenceGenerator;
import com.richotaru.authenticationapi.utils.sequenceGenerators.qualifiers.PortalAccountCodeSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */

@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final WorkSpaceRepository workSpaceRepository;
    private final ClientSystemService clientSystemService;
    private final ClientSystemRepository clientSystemRepository;
    private final SequenceGenerator sequenceGenerator;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    public WorkSpaceServiceImpl(WorkSpaceRepository workSpaceRepository,
                                ClientSystemService clientSystemService,
                                ClientSystemRepository clientSystemRepository, PasswordEncoder passwordEncoder,
                                @PortalAccountCodeSequence SequenceGenerator sequenceGenerator,
                                JwtUtils jwtUtils) {
        this.workSpaceRepository = workSpaceRepository;
        this.clientSystemService = clientSystemService;
        this.clientSystemRepository = clientSystemRepository;
        this.sequenceGenerator = sequenceGenerator;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;

    }
    @Transactional
    @Override
    public WorkSpace createWorkSpace(WorkSpaceCreationDto dto) {

        if(clientSystemService.isValidKey(dto.getClientKey())){
            throw  new IllegalArgumentException(" Client Key is Invalid!");
        }

        ClientSystem clientSystem = clientSystemRepository.findByClientCodeAndStatus(dto.getSystemCode(), GenericStatusConstant.ACTIVE).orElse(null);

        if(clientSystem == null){
            throw  new IllegalArgumentException(" Client System not found!");
        }
        WorkSpace workSpace = new WorkSpace();
        workSpace.setName(dto.getDisplayName());
        workSpace.setCode(sequenceGenerator.getNext());
        workSpace.setType(dto.getAccountType());
        workSpace.setClientSystem(clientSystem);

//        workSpace.set(dto.getUsername());
//        portalAccount.setPassword(passwordEncoder.encode(dto.getPassword()));
//        portalAccount.setJwtToken(jwtUtils.generateToken(portalAccount.getUsername()));
        workSpace.setCreatedAt(LocalDateTime.now());
        workSpace.setLastUpdatedAt(LocalDateTime.now());
        workSpace.setStatus(GenericStatusConstant.ACTIVE);
       return workSpaceRepository.save(workSpace);
    }

    @Transactional
    @Override
    public WorkSpace updateWorkSpace(WorkSpace account) {
        return workSpaceRepository.save(account);
    }
//
//    @Override
//    public PortalAccountAuthPojo authenticate(AccountAuthDto dto) throws Exception {
//        PortalAccount user = workSpaceRepository.findByUsernameAndStatus(dto.getUsername(),
//                GenericStatusConstant.ACTIVE).orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        String jwtToken = jwtUtils.generateToken(user.getUsername(), true);
//            user.setJwtToken(jwtToken);
//            updatePortalAccount(user);
//
//        try{
//            if(passwordEncoder.matches(dto.getPassword(), user.getPassword())){
//                Date expirationDate = jwtUtils.extractExpiration(jwtToken);
//
//                PortalAccountAuthPojo response = new PortalAccountAuthPojo();
//                response.setJwtToken(jwtToken);
//                response.setExpirationDate(expirationDate);
//                response.setAccountTypeConstant(user.getAccountType());
//                response.setRoles(resolveRoles(user));
//                logger.info("Authenticated");
//
//                return response;
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        throw new JwtException("Authentication Failed");
//    }

//    @Override
//    public PortalAccountPojo getAuthenticatedAccount(String username) throws UsernameNotFoundException {
//        PortalAccount portalAccount = workSpaceRepository.findByUsernameAndStatus(username,
//                GenericStatusConstant.ACTIVE).orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        PortalAccountPojo pojo = new PortalAccountPojo(portalAccount);
//
//        if(portalAccount.getAccountType() == AccountTypeConstant.CLIENT_SYSTEM){
//            ClientSystem clientSystem = clientSystemRepository.findByClientNameAndStatus(username,
//                    GenericStatusConstant.ACTIVE).orElseThrow(()
//                    -> new UsernameNotFoundException("Client System not found"));
//            pojo.setClient(clientSystem);
//        }
//        if(portalAccount.getAccountType() == AccountTypeConstant.CLIENT_USER){
//            ClientUser clientUser = clientUserRepository.findClientUserByEmailAndStatus(username,
//                    GenericStatusConstant.ACTIVE).orElseThrow(()
//                    -> new UsernameNotFoundException("Client User not found"));
//            pojo.setUser(clientUser);
//        }
//
//
//        return pojo;
//    }

}
