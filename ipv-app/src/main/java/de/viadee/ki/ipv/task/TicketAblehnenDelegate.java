package de.viadee.ki.ipv.task;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import de.viadee.ki.ipv.model.Person;
import de.viadee.ki.ipv.model.ProcessConstants;

@Component
public class TicketAblehnenDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(TicketAblehnenDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Person person = (Person) execution.getVariable(ProcessConstants.INT_PERSON);
        logger.info("Ticken ablehnen für Person {}", person);
    }
}