package com.richotaru.authenticationapi.utils.sequenceGenerators;

import com.richotaru.authenticationapi.utils.sequenceGenerators.qualifiers.WorkSpaceUserCodeSequence;
import org.springframework.transaction.support.TransactionTemplate;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author RIchard Otaru <rotaru@byteworks.com.ng>
 */

@WorkSpaceUserCodeSequence
@Named
public class WorkSpaceUserSequenceGenerator extends SequenceGenerator {

    @Inject
    public WorkSpaceUserSequenceGenerator(EntityManager entityManager, TransactionTemplate transactionTemplate) {
        super(entityManager, transactionTemplate, "work_space_user_code");
    }

    @Override
    public String getNext() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        return String.format(Locale.ENGLISH, "WSU_"+year+"_PA%07d", getNextLong());
    }
}
