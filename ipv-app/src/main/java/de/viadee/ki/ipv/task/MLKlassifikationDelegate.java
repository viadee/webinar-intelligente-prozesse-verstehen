package de.viadee.ki.ipv.task;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import de.viadee.ki.ipv.model.MojoModelWrapper;
import de.viadee.ki.ipv.model.Person;
import de.viadee.ki.ipv.model.ProcessConstants;
import hex.genmodel.MojoModel;
import hex.genmodel.easy.EasyPredictModelWrapper;
import hex.genmodel.easy.RowData;
import hex.genmodel.easy.prediction.BinomialModelPrediction;

@Component
public class MLKlassifikationDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(MLKlassifikationDelegate.class);

    @Value("classpath:model/aie_titanic_rf.zip")
    private Resource kiModell;

    @Autowired
    private MojoModelWrapper mojoModelWrapper;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Person person = (Person) execution.getVariable(ProcessConstants.INT_PERSON);
        logger.info("prediction for Person " + person.toString());

        EasyPredictModelWrapper model = new EasyPredictModelWrapper(MojoModel.load(kiModell.getFile().getAbsolutePath()));

        RowData row = new RowData();
        row.put("Age", mojoModelWrapper.wrapIntToString(person.getAge()));
        row.put("Sex", mojoModelWrapper.getSex(person));
        row.put("Fare", mojoModelWrapper.wrapFare(person.getFare()));
        row.put("Pclass", mojoModelWrapper.wrapPclass(person.getPclass()));
        row.put("SibSp", mojoModelWrapper.wrapSibSp(person.getSiblings()));
        row.put("Parch", mojoModelWrapper.wrapParch(person.getParch()));
        row.put("Embarked", mojoModelWrapper.wrapEmbarked(person.getEmabrked()));
        logger.info("Person as Row " + row.toString());

        BinomialModelPrediction p = model.predictBinomial(row);

        logger.info("Person has survived (0=No,1=Yes): " + p.label);
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

        execution.setVariable(ProcessConstants.INT_PERSON_SURVIVED_PREDICTED, mojoModelWrapper.wrapSurvivedLabel(p));
        execution.setVariable(ProcessConstants.PV_KI_KONFIDENZ, konfidenz);
        execution.setVariable(ProcessConstants.PV_KI_ENTSCHEIDUNG_VERKAUF_OK, p.label.equals("1"));

    }

}