package com.richotaru.authenticationapi.configuration.startup;

import com.google.common.collect.Lists;
import com.richotaru.authenticationapi.dao.ClientSystemRepository;
import com.richotaru.authenticationapi.domain.entity.ClientSystem;
import com.richotaru.authenticationapi.domain.enums.GenericStatusConstant;
import com.richotaru.authenticationapi.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final TransactionTemplate transactionTemplate;
    private JwtUtils jwtUtils;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public DefaultClientSystemSetup(ClientSystemRepository clientSystemRepository,
                                    TransactionTemplate transactionTemplate,
                                    JwtUtils jwtUtils) {
        this.clientSystemRepository = clientSystemRepository;
        this.transactionTemplate = transactionTemplate;
        this.jwtUtils = jwtUtils;
    }


    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        transactionTemplate.execute(tx -> {
            try {
                logger.info("Searching for Default Client System");

                clientSystemRepository.findClientSystemByClientName("admin")
                        .orElseGet(() -> {
                            logger.warn("Default Client System Not Found");
                            logger.info("Creating Default Client System");
                            ClientSystem clientSystem = new ClientSystem();
                            clientSystem.setClientName("admin");
                            clientSystem.setClientKey("admin");
                            clientSystem.setDisplayName("Default Admin");
                            clientSystem.setDateRegistered(new Timestamp(new java.util.Date().getTime()));
                            clientSystem.setUsers(Lists.newArrayList());
                            clientSystem.setDateCreated(new Timestamp(new java.util.Date().getTime()));
                            clientSystem.setLastUpdated(new Timestamp(new java.util.Date().getTime()));
                            clientSystem.setStatus(GenericStatusConstant.ACTIVE);
                            clientSystem.setJwtToken(jwtUtils.generateToken(clientSystem.getClientName(), true));

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
