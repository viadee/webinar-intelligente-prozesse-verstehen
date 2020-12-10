package de.viadee.ki.ipv.task;

import java.util.Random;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import de.viadee.ki.ipv.model.Claim;
import de.viadee.ki.ipv.model.ProcessConstants;

@Component
public class ExternePruefungDurchfuehrenDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(ExternePruefungDurchfuehrenDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Claim claim = (Claim) execution.getVariable(ProcessConstants.INT_CLAIM);
        logger.info("Externe Prüfung für Claim {}", claim);
        String externeEntscheidung = "freigabe";
        if (new Random().nextBoolean()) {
            externeEntscheidung = "ablehnung";
        }
        logger.info("Externe Prüfung für Claim {} lautet {}", claim, externeEntscheidung);
        execution.setVariable(ProcessConstants.PV_EXTERNE_ENTSCHEIDUNG, externeEntscheidung);
    }
}