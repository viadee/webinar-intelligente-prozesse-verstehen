package de.viadee.ki.ipv.task;

import java.io.FileWriter;
import java.io.IOException;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import de.viadee.ki.ipv.model.Claim;
import de.viadee.ki.ipv.model.ClaimDataset;
import de.viadee.ki.ipv.model.H2OModelWrapper;
import de.viadee.ki.ipv.model.MojoModelWrapper;
import de.viadee.ki.ipv.model.ProcessConstants;
import de.viadee.xai.anchor.adapter.tabular.AnchorTabular;
import de.viadee.xai.anchor.adapter.tabular.TabularInstance;
import de.viadee.xai.anchor.algorithm.AnchorConstructionBuilder;
import de.viadee.xai.anchor.algorithm.AnchorResult;

@Component
public class HandlungsempfehlungErstellenDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(HandlungsempfehlungErstellenDelegate.class);

    @Autowired
    private MojoModelWrapper mojoModelWrapper;

    @Value("classpath:model/claimdata.csv")
    private Resource dataResource;

    @Value("classpath:model/DRF_1_AutoML_20201210_010425.zip")
    private Resource kiModell;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Claim claim = (Claim) execution.getVariable(ProcessConstants.INT_CLAIM);
        logger.info("Externe Prüfung für Person {}", claim);

        String handlungsempfehlung = "Anomalie erkannt, Bitte prüfen Sie den Schaden.";
        if (execution.hasVariable(ProcessConstants.PV_KI_ENTSCHEIDUNG_FREIGABE)) {
            logger.info("Handlungsempfehlung erzeugen");
            personZuDatenHinzufuegen(claim);
            handlungsempfehlung = generateHandlungsempfehlung(execution);
            handlungsempfehlung = handlungsempfehlung.replaceAll("PREDICT \\[1\\]", "CHOOSE [ablehnen]");
            handlungsempfehlung = handlungsempfehlung.replaceAll("PREDICT \\[0\\]", "CHOOSE [freigeben]");
            handlungsempfehlung = handlungsempfehlung.replaceAll("CAUTION", "                                                                                                                                                                                      ");
        }

        execution.setVariable(ProcessConstants.PV_HANDLUNGSEMPFEHLUNG, handlungsempfehlung);
    }

    private String generateHandlungsempfehlung(DelegateExecution execution) throws Exception {

        final AnchorTabular anchorTabular = ClaimDataset.createTabularTrainingDefinition(dataResource.getInputStream());
        final H2OModelWrapper h2oModel = new H2OModelWrapper(kiModell.getURL());

        final int i = anchorTabular.getTabularInstances().length;
        final TabularInstance explainedInstance = anchorTabular.getTabularInstances()[i - 1];
        final AnchorConstructionBuilder<TabularInstance> h2oBuilder = anchorTabular
                .createDefaultBuilder(h2oModel::predict, explainedInstance);

        // Create local explanations
        return printLocalExplanationResult(explainedInstance, anchorTabular, h2oBuilder);
    }

    private void personZuDatenHinzufuegen(Claim claim) throws IOException {

        String id = mojoModelWrapper.wrapIntToString(claim.getId()) ;
        String typeclass = mojoModelWrapper.wrapTypeClass(claim.getTypeclass()) ;
        String year = mojoModelWrapper.wrapYear(claim.getYear());
        String doors = mojoModelWrapper.wrapDoors(claim.getDoors());
        String passengers = mojoModelWrapper.wrapPassengers(claim.getPassengers());
        String costs = mojoModelWrapper.wrapCosts(claim.getCosts());
        String repairtype = mojoModelWrapper.wrapRepairType(claim.getRepairtype());
        String rejected = mojoModelWrapper.wrapIntToString(claim.getRejected());

        //"ClaimId","Rejected","TypeClass","Year","Doors","Passangers","Costs","RepairType"

        String csvClaimLine = id+","+rejected+",\""+typeclass + "\"," + year + "," + doors + "," + passengers + "," + costs + ",\"" + repairtype+ "\"" + System.lineSeparator();
    
        new FileWriter(dataResource.getFile(), true).append(csvClaimLine).close();
    }

    private String printLocalExplanationResult(TabularInstance instance, AnchorTabular tabular,
            AnchorConstructionBuilder<TabularInstance> builder) {
        final AnchorResult<TabularInstance> anchor = builder.build().constructAnchor();

        System.out.println("====Explained instance====" + System.lineSeparator()
                + tabular.getVisualizer().visualizeInstance(anchor.getInstance()));

        String explanationResult = tabular.getVisualizer().visualizeResult(anchor);
        System.out.println("====Result====" + System.lineSeparator() + explanationResult);
        logger.info(explanationResult);
        return explanationResult;
    }
}