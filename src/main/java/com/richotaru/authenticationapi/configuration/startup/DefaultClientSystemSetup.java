package com.richotaru.authenticationapi.configuration.startup;

import com.google.common.collect.Lists;
import com.richotaru.authenticationapi.dao.ClientSystemRepository;
import com.richotaru.authenticationapi.dao.PortalAccountRepository;
import com.richotaru.authenticationapi.domain.entity.ClientSystem;
import com.richotaru.authenticationapi.domain.entity.PortalAccount;
import com.richotaru.authenticationapi.domain.enums.AccountTypeConstant;
import com.richotaru.authenticationapi.domain.enums.GenericStatusConstant;
import com.richotaru.authenticationapi.domain.enums.RoleConstant;
import com.richotaru.authenticationapi.utils.JwtUtils;
import com.richotaru.authenticationapi.utils.sequenceGenerators.SequenceGenerator;
import com.richotaru.authenticationapi.utils.sequenceGenerators.qualifiers.PortalAccountCodeSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Timestamp;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */

@Component
public class DefaultClientSystemSetup {
    private final ClientSystemRepository clientSystemRepository;
    private final PortalAccountRepository portalAccountRepository;
    private final SequenceGenerator sequenceGenerator;
    private final TransactionTemplate transactionTemplate;
    private JwtUtils jwtUtils;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public DefaultClientSystemSetup(ClientSystemRepository clientSystemRepository,
                                    PortalAccountRepository portalAccountRepository,
                                    @PortalAccountCodeSequence SequenceGenerator sequenceGenerator,
                                    TransactionTemplate transactionTemplate,
                                    JwtUtils jwtUtils) {
        this.clientSystemRepository = clientSystemRepository;
        this.portalAccountRepository = portalAccountRepository;
        this.sequenceGenerator = sequenceGenerator;
        this.transactionTemplate = transactionTemplate;
        this.jwtUtils = jwtUtils;
    }


    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        transactionTemplate.execute(tx -> {
            try {
                logger.info("Searching for Default Portal Account...");
                portalAccountRepository.findByUsernameAndStatus("admin", GenericStatusConstant.ACTIVE)
                        .orElseGet(() -> {
                            logger.warn("Default Portal Account Not Found");
                            logger.info("Creating Default Portal Account...");
                            PortalAccount portalAccount = new PortalAccount();
                            portalAccount.setDisplayName("System Admin");
                            portalAccount.setUsername("Admin");
                            portalAccount.setPassword("Admin");
                            portalAccount.setAccountCode(sequenceGenerator.getNext());
                            portalAccount.setAccountType(AccountTypeConstant.CLIENT_SYSTEM);
                            portalAccount.setJwtToken(jwtUtils.generateToken(portalAccount.getUsername(), true));
                            portalAccount.setRoles(RoleConstant.SYSTEM_ADMIN.name());
                            portalAccount.setDateCreated(new Timestamp(new java.util.Date().getTime()));
                            portalAccount.setLastUpdated(new Timestamp(new java.util.Date().getTime()));
                            portalAccount.setStatus(GenericStatusConstant.ACTIVE);

                            portalAccountRepository.save(portalAccount);

                            logger.info("Creating Default Client System");
                            ClientSystem clientSystem = new ClientSystem();
                            clientSystem.setClientName("Admin");
                            clientSystem.setClientCode(portalAccount.getAccountCode());
                            clientSystem.setDisplayName("Default System Admin");
                            clientSystem.setUsers(Lists.newArrayList());
                            clientSystem.setDateCreated(new Timestamp(new java.util.Date().getTime()));
                            clientSystem.setLastUpdated(new Timestamp(new java.util.Date().getTime()));
                            clientSystem.setStatus(GenericStatusConstant.ACTIVE);
                            clientSystem.setPortalAccount(portalAccount);

                            ClientSystem created = clientSystemRepository.save(clientSystem);
                            logger.info("Default Client System Created {}",created);
                            return null;
                        });

            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
            return null;
        });
    }
}
