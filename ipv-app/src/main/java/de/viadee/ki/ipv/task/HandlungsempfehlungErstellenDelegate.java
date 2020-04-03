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

import de.viadee.ki.ipv.model.H2OModelWrapper;
import de.viadee.ki.ipv.model.MojoModelWrapper;
import de.viadee.ki.ipv.model.Person;
import de.viadee.ki.ipv.model.ProcessConstants;
import de.viadee.ki.ipv.model.TitanicDataset;
import de.viadee.xai.anchor.adapter.tabular.AnchorTabular;
import de.viadee.xai.anchor.adapter.tabular.TabularInstance;
import de.viadee.xai.anchor.algorithm.AnchorConstructionBuilder;
import de.viadee.xai.anchor.algorithm.AnchorResult;

@Component
public class HandlungsempfehlungErstellenDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(HandlungsempfehlungErstellenDelegate.class);

    @Autowired
    private MojoModelWrapper mojoModelWrapper;

    @Value("classpath:model/data.csv")
    private Resource dataResource;

    @Value("classpath:model/aie_titanic_rf.zip")
    private Resource kiModell;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        Person person = (Person) execution.getVariable(ProcessConstants.INT_PERSON);
        logger.info("Externe Prüfung für Person {}", person);

        String handlungsempfehlung = "Anomalie erkannt, Bitte prüfen Sie die Person.";
        if (execution.hasVariable(ProcessConstants.PV_KI_ENTSCHEIDUNG_VERKAUF_OK)) {
            logger.info("Handlungsempfehlung erzeugen");
            personZuDatenHinzufuegen(person);
            handlungsempfehlung = generateHandlungsempfehlung(execution);
        }

        execution.setVariable(ProcessConstants.PV_HANDLUNGSEMPFEHLUNG, handlungsempfehlung);
    }

    private String generateHandlungsempfehlung(DelegateExecution execution) throws Exception {

        final AnchorTabular anchorTabular = TitanicDataset.createTabularTrainingDefinition(dataResource.getInputStream());
        final H2OModelWrapper h2oModel = new H2OModelWrapper(kiModell.getURL());

        final int i = anchorTabular.getTabularInstances().length;
        final TabularInstance explainedInstance = anchorTabular.getTabularInstances()[i - 1];
        final AnchorConstructionBuilder<TabularInstance> h2oBuilder = anchorTabular
                .createDefaultBuilder(h2oModel::predict, explainedInstance);

        // Create local explanations
        return printLocalExplanationResult(explainedInstance, anchorTabular, h2oBuilder);
    }

    private void personZuDatenHinzufuegen(Person person) throws IOException {
        String age = mojoModelWrapper.wrapIntToString(person.getAge()) + ".0";
        String sex = mojoModelWrapper.getSex(person);
        String fare = mojoModelWrapper.wrapFare(person.getFare());
        String pclass = mojoModelWrapper.wrapPclass(person.getPclass());
        String sibSp = mojoModelWrapper.wrapSibSp(person.getSiblings());
        String parch = mojoModelWrapper.wrapParch(person.getParch());
        String embarked = mojoModelWrapper.wrapEmbarked(person.getEmabrked());
        String survided = mojoModelWrapper.wrapIntToString(person.getSurvived());
        String csvPersonLine = age + ",\"" + sex + "\"," + fare + ",\"" + pclass + "\"," + sibSp + "," + parch + ",\""
                + embarked + "\",\"" + survided + "\"" + System.lineSeparator();

        new FileWriter(dataResource.getFile(), true).append(csvPersonLine).close();
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