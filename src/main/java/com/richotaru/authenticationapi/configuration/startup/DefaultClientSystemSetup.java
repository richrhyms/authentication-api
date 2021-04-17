package com.richotaru.authenticationapi.configuration.startup;

import com.richotaru.authenticationapi.dao.ClientSystemRepository;
import com.richotaru.authenticationapi.dao.WorkSpaceMembershipRepository;
import com.richotaru.authenticationapi.dao.WorkSpaceRepository;
import com.richotaru.authenticationapi.dao.WorkSpaceUserRepository;
import com.richotaru.authenticationapi.entity.ClientSystem;
import com.richotaru.authenticationapi.entity.WorkSpace;
import com.richotaru.authenticationapi.entity.WorkSpaceMembership;
import com.richotaru.authenticationapi.entity.WorkSpaceUser;
import com.richotaru.authenticationapi.enumeration.*;
import com.richotaru.authenticationapi.service.SettingService;
import com.richotaru.authenticationapi.utils.JwtUtils;
import com.richotaru.authenticationapi.utils.sequenceGenerators.SequenceGenerator;
import com.richotaru.authenticationapi.utils.sequenceGenerators.qualifiers.WorkSpaceCodeSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */

@Component
public class DefaultClientSystemSetup {
    private final ClientSystemRepository clientSystemRepository;
    private final WorkSpaceRepository workSpaceRepository;
    private final WorkSpaceUserRepository workSpaceUserRepository;
    private final WorkSpaceMembershipRepository workSpaceMembershipRepository;
    private final SequenceGenerator sequenceGenerator;
    private final TransactionTemplate transactionTemplate;
    private final PasswordEncoder passwordEncoder;
    private final SettingService settingService;

    private final JwtUtils jwtUtils;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public DefaultClientSystemSetup(ClientSystemRepository clientSystemRepository,
                                    WorkSpaceMembershipRepository workSpaceMembershipRepository,
                                    WorkSpaceRepository workSpaceRepository,
                                    WorkSpaceUserRepository workSpaceUserRepository,
                                    @WorkSpaceCodeSequence SequenceGenerator sequenceGenerator,
                                    TransactionTemplate transactionTemplate,
                                    PasswordEncoder passwordEncoder, SettingService settingService,
                                    JwtUtils jwtUtils) {
        this.clientSystemRepository = clientSystemRepository;
        this.workSpaceMembershipRepository = workSpaceMembershipRepository;
        this.workSpaceRepository = workSpaceRepository;
        this.workSpaceUserRepository = workSpaceUserRepository;
        this.sequenceGenerator = sequenceGenerator;
        this.transactionTemplate = transactionTemplate;
        this.passwordEncoder = passwordEncoder;
        this.settingService = settingService;
        this.jwtUtils = jwtUtils;
    }


    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        transactionTemplate.execute(tx -> {
            try {
                logger.info("Searching for Default Portal Account...");
                workSpaceUserRepository.findByUsernameAndStatus(settingService.getString("DEFAULT_ADMIN_USERNAME","super_admin"), GenericStatusConstant.ACTIVE)
                        .orElseGet(() -> {
                            logger.warn("Default WorkSpace User Not Found");
                            logger.info("Creating Default Client System...");
                            ClientSystem clientSystem = new ClientSystem();
                            clientSystem.setSystemType(ClientSystemTypeConstant.WEB);
                            clientSystem.setWorkSpaceAccountType(WorkSpaceAccountTypeConstant.SINGLE_ACCOUNT);
                            clientSystem.setAccessMode(AccessModeConstant.STRICT);
                            clientSystem.setCreatedAt(LocalDateTime.now());
                            clientSystem.setLastUpdatedAt(LocalDateTime.now());
                            clientSystem.setStatus(GenericStatusConstant.ACTIVE);
                            clientSystem.setCode(sequenceGenerator.getNext());
                            clientSystem.setDisplayName("AUTHENTICATION API :: DEFAULT CLIENT");

                            clientSystemRepository.save(clientSystem);


                            logger.info("Creating Default Work Space...");

                            WorkSpace workSpace = new WorkSpace();
                            workSpace.setName("AUTHENTICATION :: DEFAULT WORKSPACE");
                            workSpace.setCode(sequenceGenerator.getNext());
                            workSpace.setType(WorkSpaceTypeConstant.ADMIN);
                            workSpace.setClientSystem(clientSystem);
                            workSpace.setCreatedAt(LocalDateTime.now());
                            workSpace.setLastUpdatedAt(LocalDateTime.now());
                            workSpace.setStatus(GenericStatusConstant.ACTIVE);
                            workSpaceRepository.save(workSpace);

                            logger.info("Creating Default Work Space User...");
                            WorkSpaceUser workSpaceUser = new WorkSpaceUser();
                            workSpaceUser.setDisplayName("AUTHENTICATION ADMIN :: DEFAULT WORKSPACE USER");
                            workSpaceUser.setUsername(settingService.getString("DEFAULT_ADMIN_USERNAME","super_admin"));
                            workSpaceUser.setPassword(passwordEncoder.encode(settingService.getString("DEFAULT_ADMIN_PASSWORD","P@ssw0rd!")));
                            workSpaceUser.setFirstName("ASH");
                            workSpaceUser.setLastName("OTARU");
                            workSpaceUser.setOtherNames("FAVOUR");
                            workSpaceUser.setDateOfBirth(Timestamp.valueOf(LocalDateTime.now()));
                            workSpaceUser.setEmail(settingService.getString("DEFAULT_ADMIN_EMAIL","richotaru@gmail.com"));
                            workSpaceUser.setEmailVerified(true);
                            workSpaceUser.setGender(GenderConstant.MALE);
                            workSpaceUser.setPhoneNumber("+2347032804231");
                            workSpaceUser.setPreferredName("RICH");
                            workSpaceUser.setSetupComplete(true);
                            workSpaceUser.setGeneratedPassword("password");
                            workSpaceUser.setCreatedAt(LocalDateTime.now());
                            workSpaceUser.setLastUpdatedAt(LocalDateTime.now());
                            workSpaceUser.setJwtToken(jwtUtils.generateToken(workSpaceUser.getUsername()));
                            workSpaceUser.setStatus(GenericStatusConstant.ACTIVE);

                            workSpaceUserRepository.save(workSpaceUser);

                            logger.info("Creating Default Membership for User...");
                            WorkSpaceMembership membership = new WorkSpaceMembership();
                            membership.setWorkSpaceUser(workSpaceUser);
                            membership.setWorkSpace(workSpace);
                            membership.setCreatedBy(workSpaceUser);
                            membership.setCreatedAt(LocalDateTime.now());
                            membership.setLastUpdatedAt(LocalDateTime.now());
                            membership.setStatus(GenericStatusConstant.ACTIVE);
                            workSpaceMembershipRepository.save(membership);


                            logger.info("Updating Workspace user Membership info...");
                            workSpaceUser.setWorkSpaceMemberships(Collections.singletonList(membership));
                            workSpaceUserRepository.save(workSpaceUser);


                            logger.info("Default Workspace user Created {}",workSpaceUser);
                            return null;
                        });

            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
            return null;
        });
    }
}
