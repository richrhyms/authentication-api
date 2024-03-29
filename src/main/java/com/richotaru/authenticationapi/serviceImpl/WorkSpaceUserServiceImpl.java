package com.richotaru.authenticationapi.serviceImpl;

import com.richotaru.authenticationapi.dao.WorkSpaceMembershipRepository;
import com.richotaru.authenticationapi.dao.WorkSpaceRepository;
import com.richotaru.authenticationapi.dao.WorkSpaceUserRepository;
import com.richotaru.authenticationapi.domain.model.RequestPrincipal;
import com.richotaru.authenticationapi.domain.model.dto.AccountAuthDto;
import com.richotaru.authenticationapi.domain.model.dto.WorkSpaceUserDto;
import com.richotaru.authenticationapi.domain.model.pojo.WorkSpaceUserAuthPojo;
import com.richotaru.authenticationapi.domain.model.pojo.WorkSpaceUserPojo;
import com.richotaru.authenticationapi.entity.ClientSystem;
import com.richotaru.authenticationapi.entity.WorkSpace;
import com.richotaru.authenticationapi.entity.WorkSpaceMembership;
import com.richotaru.authenticationapi.entity.WorkSpaceUser;
import com.richotaru.authenticationapi.enumeration.AccessModeConstant;
import com.richotaru.authenticationapi.enumeration.GenericStatusConstant;
import com.richotaru.authenticationapi.service.ClientSystemService;
import com.richotaru.authenticationapi.service.WorkSpaceUserService;
import com.richotaru.authenticationapi.utils.JwtUtils;
import com.richotaru.authenticationapi.utils.sequenceGenerators.SequenceGenerator;
import com.richotaru.authenticationapi.utils.sequenceGenerators.qualifiers.WorkSpaceUserCodeSequence;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Provider;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */

@Service
public class WorkSpaceUserServiceImpl implements WorkSpaceUserService {
    private final WorkSpaceUserRepository workSpaceUserRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final ClientSystemService clientSystemService;
    private final Provider<RequestPrincipal> requestPrincipalProvider;
    private final SequenceGenerator sequenceGenerator;
    private final WorkSpaceRepository workSpaceRepository;
    private final WorkSpaceMembershipRepository workSpaceMembershipRepository;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public WorkSpaceUserServiceImpl(
                                    WorkSpaceUserRepository workSpaceUserRepository,
                                    WorkSpaceRepository workSpaceRepository,
                                    WorkSpaceMembershipRepository workSpaceMembershipRepository,
                                    ClientSystemService clientSystemService,
                                    Provider<RequestPrincipal> requestPrincipalProvider,
                                    JwtUtils jwtUtils,
                                    PasswordEncoder passwordEncoder,
                                    @WorkSpaceUserCodeSequence SequenceGenerator sequenceGenerator) {
        this.workSpaceRepository = workSpaceRepository;
        this.workSpaceUserRepository = workSpaceUserRepository;
        this.clientSystemService = clientSystemService;
        this.requestPrincipalProvider = requestPrincipalProvider;
        this.sequenceGenerator = sequenceGenerator;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.workSpaceMembershipRepository = workSpaceMembershipRepository;
    }

    @Transactional
    @Override
    public WorkSpaceUserPojo createWorkSpaceUser(WorkSpaceUserDto dto) {
        try {
            if(!clientSystemService.isValidKey(dto.getClientKey())){
                throw  new IllegalArgumentException(" Client Key is Invalid");
            }
            WorkSpace workSpace = requestPrincipalProvider.get().getWorkSpace();

            if(workSpace == null){
               workSpace = workSpaceRepository
                       .findByCodeAndStatus(dto.getWorkspaceCode(), GenericStatusConstant.ACTIVE)
                       .orElseThrow(()-> new IllegalArgumentException(" Cannot determine workspace"));
            }

            Optional<WorkSpaceUser> existingUser = workSpaceUserRepository.findByUsernameAndStatus(dto.getUsername(),
                    GenericStatusConstant.ACTIVE);

            if(existingUser.isPresent()){
                throw  new IllegalArgumentException(" Workspace User with this USERNAME already exist");
            }
            WorkSpaceUser user = new WorkSpaceUser();
            BeanUtils.copyProperties(dto, user);




            user.setDateOfBirth(Timestamp.valueOf(LocalDateTime.of(dto.getDob(), LocalTime.now())));
            user.setCreatedAt(LocalDateTime.now());
            user.setUserId(sequenceGenerator.getNext());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setLastUpdatedAt(LocalDateTime.now());
            user.setStatus(GenericStatusConstant.ACTIVE);

            workSpaceUserRepository.save(user);

            WorkSpaceMembership membership = new WorkSpaceMembership();
            membership.setWorkSpace(workSpace);
            membership.setWorkSpaceUser(user);
            membership.setCreatedAt(LocalDateTime.now());
            membership.setLastUpdatedAt(LocalDateTime.now());
            membership.setStatus(GenericStatusConstant.ACTIVE);
            membership.setCreatedBy(requestPrincipalProvider.get().getWorkSpaceUser().getUser());
            workSpaceMembershipRepository.save(membership);

            logger.info("Workspace User CREATED :: ID::" + user.getUserId());
            return new WorkSpaceUserPojo(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public WorkSpaceUser getWorkSpaceUserByEmail(String emailAddress){
        return  workSpaceUserRepository.findWorkSpaceUserByEmailAndStatus(emailAddress, GenericStatusConstant.ACTIVE).orElseThrow(()
                -> new UsernameNotFoundException("User not found"));
    }
    @Override
    public WorkSpaceUser getWorkSpaceUserByUsername(String username){
        return  workSpaceUserRepository.findByUsernameAndStatus(username, GenericStatusConstant.ACTIVE).orElseThrow(()
                -> new UsernameNotFoundException("User not found"));
    }


    @Override
    public WorkSpaceUserAuthPojo authenticate(AccountAuthDto dto){
        if(!clientSystemService.isValidKey(dto.getClientKey())){
            throw  new IllegalArgumentException(" Client Key is not valid");
        }
        WorkSpace workSpace_to_access = workSpaceRepository
                    .findByCodeAndStatus_fetchJoinClientSystem(dto.getWorkspaceCode(), GenericStatusConstant.ACTIVE)
                    .orElseThrow(()-> new IllegalArgumentException(" Cannot determine workspace"));


        WorkSpaceUser user = workSpaceUserRepository.findByUsernameAndStatus(dto.getUsername(),
                GenericStatusConstant.ACTIVE).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        confirmUserWorkspaceAccess(user,workSpace_to_access);

        ClientSystem clientSystem = workSpace_to_access.getClientSystem();


        try{
            WorkSpaceUserAuthPojo response = new WorkSpaceUserAuthPojo();
            response.setUsername(user.getUsername());
            response.setValid(true);
            response.setAccessMode(clientSystem.getAccessMode());
            response.setWorkspaceCode(workSpace_to_access.getCode());


            if(user.getJwtToken().isPresent() && jwtUtils.validateToken(user.getJwtToken().get(), dto.getUsername())){
                String jwtToken = user.getJwtToken().get();
                response.setJwtToken(jwtToken);
                response.setExpirationDate(jwtUtils.extractExpiration(jwtToken));
                return response;
            }
            if(passwordEncoder.matches(dto.getPassword(), user.getPassword())){
                String jwtToken = jwtUtils.generateToken(user.getUsername());

                user.setJwtToken(jwtToken);
                workSpaceUserRepository.save(user);

                Date expirationDate = jwtUtils.extractExpiration(jwtToken);
                response.setJwtToken(jwtToken);
                response.setExpirationDate(expirationDate);
                return response;
            }
            throw new JwtException("Authentication Failed");

        }catch (Exception e){
            e.printStackTrace();
        }
        throw new JwtException("Authentication Failed");
    }

    @Override
    public WorkSpaceUserPojo getAuthenticatedUser(String username, String workspaceCode){
        WorkSpaceUser user = workSpaceUserRepository.findByUsernameAndStatus(username,
                GenericStatusConstant.ACTIVE).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        WorkSpace workSpace_to_access = workSpaceRepository
                .findByCodeAndStatus_fetchJoinClientSystem(workspaceCode, GenericStatusConstant.ACTIVE)
                .orElseThrow(() -> new IllegalArgumentException("Workspace not found!!"));

        confirmUserWorkspaceAccess(user,workSpace_to_access);

        WorkSpaceUserPojo pojo = new WorkSpaceUserPojo(user);
        pojo.setWorkSpace(workSpace_to_access);

        return pojo;
    }

    private void confirmUserWorkspaceAccess(WorkSpaceUser user, WorkSpace workSpace){
        if (workSpace.getClientSystem().getAccessMode() == AccessModeConstant.STRICT) {
            workSpaceMembershipRepository.findByWorkSpaceAndWorkSpaceUserAndStatus(workSpace, user,
                    GenericStatusConstant.ACTIVE).orElseThrow(() ->
                    new IllegalArgumentException(" '" + user.getUsername()
                            + "' cannot access this workspace in STRICT mode")
            );
        }else{
            List<WorkSpaceMembership> user_workspaces = workSpaceMembershipRepository
                    .findAllByWorkSpaceUserAndStatus(user, GenericStatusConstant.ACTIVE);
            user_workspaces.stream()
                    .filter(it-> it.getWorkSpace()
                            .getClientSystem()
                            .equals(workSpace.getClientSystem()))
                    .findFirst()
                    .orElseThrow(()->
                            new IllegalArgumentException(" '"+user.getUsername()
                                    +"' does not have access to any workspace in this system")
                    );
        }
    }
}
