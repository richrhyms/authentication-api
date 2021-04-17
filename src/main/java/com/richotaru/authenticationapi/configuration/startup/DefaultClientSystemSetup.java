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
import com.richotaru.authenticationapi.utils.sequenceGenerators.ClientSystemSequenceGenerator;
import com.richotaru.authenticationapi.utils.sequenceGenerators.WorkSpaceSequenceGenerator;
import com.richotaru.authenticationapi.utils.sequenceGenerators.WorkSpaceUserSequenceGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */

@Component
public class DefaultClientSystemSetup {
    private final ClientSystemRepository clientSystemRepository;
    private final WorkSpaceRepository workSpaceRepository;
    private final WorkSpaceUserRepository workSpaceUserRepository;
    private final WorkSpaceMembershipRepository workSpaceMembershipRepository;
    private final WorkSpaceSequenceGenerator workSpaceSequenceGenerator;
    private final WorkSpaceUserSequenceGenerator workSpaceUserSequenceGenerator;
    private final ClientSystemSequenceGenerator clientSystemSequenceGenerator;
    private final TransactionTemplate transactionTemplate;
    private final PasswordEncoder passwordEncoder;
    private final SettingService settingService;

    private final JwtUtils jwtUtils;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public DefaultClientSystemSetup(ClientSystemRepository clientSystemRepository,
                                    WorkSpaceMembershipRepository workSpaceMembershipRepository,
                                    WorkSpaceRepository workSpaceRepository,
                                    WorkSpaceUserRepository workSpaceUserRepository,
                                    WorkSpaceSequenceGenerator workSpaceSequenceGenerator,
                                    WorkSpaceUserSequenceGenerator workSpaceUserSequenceGenerator,
                                    ClientSystemSequenceGenerator clientSystemSequenceGenerator,
                                    TransactionTemplate transactionTemplate,
                                    PasswordEncoder passwordEncoder, SettingService settingService,
                                    JwtUtils jwtUtils) {
        this.clientSystemRepository = clientSystemRepository;
        this.workSpaceMembershipRepository = workSpaceMembershipRepository;
        this.workSpaceRepository = workSpaceRepository;
        this.workSpaceUserRepository = workSpaceUserRepository;
        this.workSpaceSequenceGenerator = workSpaceSequenceGenerator;
        this.workSpaceUserSequenceGenerator = workSpaceUserSequenceGenerator;
        this.clientSystemSequenceGenerator = clientSystemSequenceGenerator;
        this.transactionTemplate = transactionTemplate;
        this.passwordEncoder = passwordEncoder;
        this.settingService = settingService;
        this.jwtUtils = jwtUtils;
    }


    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        transactionTemplate.execute(tx -> {
            try {
                logger.info("Searching for Default Workspace User...");
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
                            clientSystem.setCode(clientSystemSequenceGenerator.getNext());
                            clientSystem.setDisplayName("AUTHENTICATION API :: DEFAULT CLIENT");

                            clientSystemRepository.save(clientSystem);
//                            settingService.getString("DEFAULT_CLIENT_CODE",clientSystem.getCode());

                            logger.info("Creating Default Work Space...");

                            WorkSpace workSpace = new WorkSpace();
                            workSpace.setName("AUTHENTICATION :: DEFAULT WORKSPACE");
                            workSpace.setCode(workSpaceSequenceGenerator.getNext());
                            workSpace.setType(WorkSpaceTypeConstant.ADMIN);
                            workSpace.setClientSystem(clientSystem);
                            workSpace.setCreatedAt(LocalDateTime.now());
                            workSpace.setLastUpdatedAt(LocalDateTime.now());
                            workSpace.setStatus(GenericStatusConstant.ACTIVE);
                            workSpaceRepository.save(workSpace);

//                            settingService.getString("DEFAULT_WORKSPACE_CODE",workSpace.getCode());

                            logger.info("Creating Default Work Space User...");
                            WorkSpaceUser workSpaceUser = new WorkSpaceUser();
                            workSpaceUser.setDisplayName("AUTHENTICATION ADMIN :: DEFAULT WORKSPACE USER");
                            workSpaceUser.setUsername(settingService.getString("DEFAULT_ADMIN_USERNAME","super_admin"));
                            workSpaceUser.setPassword(passwordEncoder.encode(settingService.getString("DEFAULT_ADMIN_PASSWORD","P@ssw0rd!")));
                            workSpaceUser.setUserId(workSpaceUserSequenceGenerator.getNext());
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


//                            logger.info("Updating Workspace user Membership info...");
//                            List<WorkSpaceMembership> memberships = new ArrayList<>();
//                            memberships.add(membership);
//                            workSpaceUser.setWorkSpaceMemberships(memberships);
//                            workSpaceUserRepository.save(workSpaceUser);


                            logger.info("Default Workspace user Created {}", workSpaceUser.getDisplayName());
                            return null;
                        });

            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
            logger.info("Found Default Workspace User...");
            return null;
        });
    }
}
