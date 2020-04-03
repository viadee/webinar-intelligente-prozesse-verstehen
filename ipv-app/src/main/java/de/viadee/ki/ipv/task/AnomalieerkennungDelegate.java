package de.viadee.ki.ipv.task;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.viadee.ki.ipv.model.MojoModelWrapper;
import de.viadee.ki.ipv.model.Person;
import de.viadee.ki.ipv.model.ProcessConstants;
import hex.genmodel.MojoModel;
import hex.genmodel.easy.EasyPredictModelWrapper;
import hex.genmodel.easy.RowData;
import hex.genmodel.easy.prediction.AutoEncoderModelPrediction;

@Component
public class AnomalieerkennungDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(AnomalieerkennungDelegate.class);
    private static final String PATH_MODEL_ZIP = "src/main/resources/model/aie_titanic_autoencoder.zip";

    @Autowired
    private MojoModelWrapper mojoModelWrapper;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        try {
            Person person = (Person) execution.getVariable(ProcessConstants.INT_PERSON);
            logger.info("anomalie detection Person " + person.toString());

            EasyPredictModelWrapper model = new EasyPredictModelWrapper(MojoModel.load(PATH_MODEL_ZIP));

            RowData row = new RowData();
            row.put("Age", mojoModelWrapper.wrapIntToString(person.getAge()));
            row.put("Sex", mojoModelWrapper.getSex(person));
            row.put("Fare", mojoModelWrapper.wrapFare(person.getFare()));
            row.put("Pclass", mojoModelWrapper.wrapPclass(person.getPclass()));
            row.put("SibSp", mojoModelWrapper.wrapSibSp(person.getSiblings()));
            row.put("Parch", mojoModelWrapper.wrapParch(person.getParch()));
            row.put("Embarked", mojoModelWrapper.wrapEmbarked(person.getEmabrked()));

            logger.info("Person as Row " + row.toString());

            AutoEncoderModelPrediction p = model.predictAutoEncoder(row);
            double anomaliewert = p.mse;
            logger.info("Person can be reconstructed by autoencoder with MSE: " + p.mse);

            StringBuilder infoOut = new StringBuilder("Feature MSEs: ");

            for (int i = 0; i < p.reconstructed.length; i++) {
                if (i > 0) {
                    infoOut.append(",");
                }
                infoOut.append(p.reconstructed[i]);
            }
            logger.info(infoOut.toString());

            execution.setVariable(ProcessConstants.PV_ANOMALIE_WERT, anomaliewert);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }

    }
}