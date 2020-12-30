package com.richotaru.authenticationapi.utils.sequenceGenerators;

import com.richotaru.authenticationapi.utils.sequenceGenerators.qualifiers.ClientSystemCodeSequence;
import org.springframework.transaction.support.TransactionTemplate;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author RIchard Otaru <rotaru@byteworks.com.ng>
 */

@ClientSystemCodeSequence
@Named
public class ClientSystemSequenceGenerator extends SequenceGenerator {

    @Inject
    public ClientSystemSequenceGenerator(EntityManager entityManager, TransactionTemplate transactionTemplate) {
        super(entityManager, transactionTemplate, "client_system_code");
    }

    @Override
    public String getNext() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        return String.format(Locale.ENGLISH, "Rich_"+year+"_CSC%07d", getNextLong());
    }
}
