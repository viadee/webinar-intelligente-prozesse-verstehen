package de.viadee.ki.ipv.task;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import de.viadee.ki.ipv.config.Config;
import de.viadee.ki.ipv.model.Claim;
import de.viadee.ki.ipv.model.ProcessConstants;

@Component
public class KaeuferInformationenVerarbeitenDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(KaeuferInformationenVerarbeitenDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        logger.info("initialize default values");

        // Default Werte setzen
        execution.setVariable(ProcessConstants.PV_ANOMALIE_WERT, 0);
        execution.setVariable(ProcessConstants.PV_ANOMALIE_SCHWELLWERT, Config.SCHWELLWERT_ANOMALIE);
        execution.setVariable(ProcessConstants.PV_KI_KONFIDENZ, 0);
        execution.setVariable(ProcessConstants.PV_KI_KONFIDENZSCHWELLWERT, Config.SCHWELLWERT_ML_KONFIDENZ);
        execution.setVariable(ProcessConstants.PV_INTERNE_ENTSCHEIDUNG, "freigabe|ablehnung|externe_pruefung");
        execution.setVariable(ProcessConstants.PV_EXTERNE_ENTSCHEIDUNG, "freigabe|ablehnung");
        execution.setVariable(ProcessConstants.PV_HANDLUNGSEMPFEHLUNG, "Anomalie erkannt, Bitte prüfen Sie den Schaden.");

        try {
 
            int id = (int) (execution.getVariable(ProcessConstants.INT_CLAIM_ID));
            int year = (int) execution.getVariable(ProcessConstants.INT_CLAIM_YEAR);
            String typeclass = String.valueOf(execution.getVariable(ProcessConstants.INT_CLAIM_TYPECLASS));
            double costs = (double) execution.getVariable(ProcessConstants.INT_CLAIM_COSTS);
            int doors = (int) execution.getVariable(ProcessConstants.INT_CLAIM_DOORS);
            int passengers = (int) execution.getVariable(ProcessConstants.INT_CLAIM_PASSENGERS);
            String rapairtype = String.valueOf(execution.getVariable(ProcessConstants.INT_CLAIM_REPAIRTYPE));
            int rejected = (int) execution.getVariable(ProcessConstants.INT_CLAIM_REJECTED);

            logger.info("Claim " + id + " wird erzeugt");
            //Person person = new Person(id, age, fare, sex, sibsp, pclass, ticket, cabin, embarked, parch, name, survived);
            //execution.setVariable(ProcessConstants.INT_PERSON, person);
            
            Claim claim = new Claim(id,  year,costs,typeclass, passengers,doors,rapairtype,rejected);
            execution.setVariable(ProcessConstants.INT_CLAIM, claim);

            logger.info("Claim wurde erzeugt " + claim.toString());
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }

    }

}