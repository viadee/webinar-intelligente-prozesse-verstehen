package de.viadee.ki.ipv.task;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import de.viadee.ki.ipv.config.Config;
import de.viadee.ki.ipv.model.Person;
import de.viadee.ki.ipv.model.ProcessConstants;

@Component
public class KaeuferInformationenVerarbeitenDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(KaeuferInformationenVerarbeitenDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        logger.info("initialize default dalues");

        // Default Werte setzen
        execution.setVariable(ProcessConstants.PV_ANOMALIE_WERT, 0);
        execution.setVariable(ProcessConstants.PV_ANOMALIE_SCHWELLWERT, Config.SCHWELLWERT_ANOMALIE);
        execution.setVariable(ProcessConstants.PV_KI_KONFIDENZ, 0);
        execution.setVariable(ProcessConstants.PV_KI_KONFIDENZSCHWELLWERT, Config.SCHWELLWERT_ML_KONFIDENZ);
        execution.setVariable(ProcessConstants.PV_INTERNE_ENTSCHEIDUNG, "verkauf_ok|verkauf_nicht_ok|externe_pruefung");
        execution.setVariable(ProcessConstants.PV_EXTERNE_ENTSCHEIDUNG, "verkauf_ok|verkauf_nicht_ok");
        execution.setVariable(ProcessConstants.PV_HANDLUNGSEMPFEHLUNG, "Anomalie erkannt, Bitte prüfen Sie die Person.");

        //Person aus den Inputs als Objekt erstellen. Inputs zur Visualisierung behalten
        try {
            int id = (int) (execution.getVariable(ProcessConstants.INT_PERSON_ID));
            int age = (int) execution.getVariable(ProcessConstants.INT_PERSON_AGE);
            String sex = String.valueOf(execution.getVariable(ProcessConstants.INT_PERSON_SEX));
            int pclass = (int) execution.getVariable(ProcessConstants.INT_PERSON_PCLASS);
            int parch = (int) execution.getVariable(ProcessConstants.INT_PERSON_PARCH);
            int sibsp = (int) execution.getVariable(ProcessConstants.INT_PERSON_SIBSP);
            double fare = (double) execution.getVariable(ProcessConstants.INT_PERSON_FARE);
            String embarked = String.valueOf(execution.getVariable(ProcessConstants.INT_PERSON_EMBARKED));
            String ticket = String.valueOf(execution.getVariable(ProcessConstants.INT_PERSON_TICKET));
            String cabin = String.valueOf(execution.getVariable(ProcessConstants.INT_PERSON_CABIN));
            int survived = (int) execution.getVariable(ProcessConstants.INT_PERSON_SURVIVED);
            String name = String.valueOf(execution.getVariable(ProcessConstants.INT_PERSON_NAME));

            logger.info("Person " + name + " wird erzeugt");
            Person person = new Person(id, age, fare, sex, sibsp, pclass, ticket, cabin, embarked, parch, name,
                    survived);
            execution.setVariable(ProcessConstants.INT_PERSON, person);
            logger.info("Person wurde erzeugt " + person.toString());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }

    }

}