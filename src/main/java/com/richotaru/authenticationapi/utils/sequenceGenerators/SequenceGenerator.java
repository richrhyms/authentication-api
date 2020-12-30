package com.richotaru.authenticationapi.utils.sequenceGenerators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.math.BigInteger;

/**
 * @author Otaru Richard <richotaru@gmail.com>
 */
public abstract class SequenceGenerator {

//    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final EntityManager entityManager;
    private final String sequenceName;
    private final TransactionTemplate transactionTemplate;
    @Value("${SEQUENCE_DEFINITION_SQL_QUERY}")
    private String sequenceDefinition;

    public SequenceGenerator(EntityManager entityManager, TransactionTemplate transactionTemplate, String sequenceTableName) {
        this.entityManager = entityManager;
        this.transactionTemplate = transactionTemplate;
        this.sequenceName = sequenceTableName.toLowerCase() + "_sequence";
    }

    @PostConstruct
    public void init() {
        transactionTemplate.execute(tx -> {
//            logger.info(sequenceName);
            this.entityManager.createNativeQuery(String.format(sequenceDefinition, sequenceName))
                    .executeUpdate();
            return null;
        });
    }

    @Transactional
    public Long getNextLong() {
        return ((BigInteger) this.entityManager.createNativeQuery(String.format("select nextval ('%s')", sequenceName)).getSingleResult()).longValue();
    }

    @Transactional
    public String getNext() {
        return String.valueOf(getNextLong());
    }
}
