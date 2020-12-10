package de.viadee.ki.ipv.task;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import de.viadee.ki.ipv.model.Claim;
import de.viadee.ki.ipv.model.ProcessConstants;

@Component
public class TicketAusliefernDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(TicketAusliefernDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Claim claim = (Claim) execution.getVariable(ProcessConstants.INT_CLAIM);
        logger.info("Schaden ablehnen für Claim {}", claim);
    }
}