package de.viadee.ki.ipv.task;

import java.util.Random;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import de.viadee.ki.ipv.model.Person;
import de.viadee.ki.ipv.model.ProcessConstants;

@Component
public class ExternePruefungDurchfuehrenDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(ExternePruefungDurchfuehrenDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Person person = (Person) execution.getVariable(ProcessConstants.INT_PERSON);
        logger.info("Externe Prüfung für Person {}", person);
        String externeEntscheidung = "verkauf_ok";
        if (new Random().nextBoolean()) {
            externeEntscheidung = "verkauf_nicht_ok";
        }
        logger.info("Externe Prüfung für Person {} lautet {}", person, externeEntscheidung);
        execution.setVariable(ProcessConstants.PV_EXTERNE_ENTSCHEIDUNG, externeEntscheidung);
    }
}