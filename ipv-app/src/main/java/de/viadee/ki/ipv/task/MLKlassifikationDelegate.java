package de.viadee.ki.ipv.task;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import de.viadee.ki.ipv.model.Claim;
import de.viadee.ki.ipv.model.MojoModelWrapper;
import de.viadee.ki.ipv.model.ProcessConstants;
import hex.genmodel.MojoModel;
import hex.genmodel.easy.EasyPredictModelWrapper;
import hex.genmodel.easy.RowData;
import hex.genmodel.easy.prediction.BinomialModelPrediction;

@Component
public class MLKlassifikationDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(MLKlassifikationDelegate.class);

    @Value("classpath:model/DRF_1_AutoML_20201210_010425.zip")
    private Resource kiModell;

    @Autowired
    private MojoModelWrapper mojoModelWrapper;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Claim claim = (Claim) execution.getVariable(ProcessConstants.INT_CLAIM);
        logger.info("prediction for Claim " + claim.toString());

        EasyPredictModelWrapper model = new EasyPredictModelWrapper(MojoModel.load(kiModell.getFile().getAbsolutePath()));

        RowData row = new RowData();
        row.put("Year", mojoModelWrapper.wrapIntToString(claim.getYear()));
        row.put("TypeClass", mojoModelWrapper.getTypeClass(claim));
        row.put("Costs", mojoModelWrapper.wrapCosts(claim.getCosts()));
        row.put("Doors", mojoModelWrapper.wrapDoors(claim.getDoors()));
        row.put("Passengers", mojoModelWrapper.wrapPassengers(claim.getPassengers()));
        row.put("RepairType", mojoModelWrapper.wrapRepairType(claim.getRepairtype()));
        logger.info("claim as Row " + row.toString());

        BinomialModelPrediction p = model.predictBinomial(row);

        logger.info("claim has rejected (0=No,1=Yes): " + p.label);
        StringBuilder infoOut = new StringBuilder("Class probabilities: ");
        double konfidenz = 0;
        for (int i = 0; i < p.classProbabilities.length; i++) {
            if (i > 0) {
                infoOut.append(",");
            }
            infoOut.append(p.classProbabilities[i]);
            konfidenz = StrictMath.max(konfidenz, p.classProbabilities[i]);
        }
        infoOut.append("");
        logger.info(infoOut.toString());

        execution.setVariable(ProcessConstants.INT_CLAIM_SURVIVED_PREDICTED, mojoModelWrapper.wrapRejectedLabel(p));
        execution.setVariable(ProcessConstants.PV_KI_KONFIDENZ, konfidenz);
        execution.setVariable(ProcessConstants.PV_KI_ENTSCHEIDUNG_FREIGABE, p.label.equals("1"));

    }

}