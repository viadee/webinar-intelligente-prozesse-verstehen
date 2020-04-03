package de.viadee.ki.ipv.model;

import java.io.IOException;
import java.io.InputStream;

import de.viadee.xai.anchor.adapter.tabular.AnchorTabular;
import de.viadee.xai.anchor.adapter.tabular.builder.AnchorTabularBuilderSequential;
import de.viadee.xai.anchor.adapter.tabular.column.DoubleColumn;
import de.viadee.xai.anchor.adapter.tabular.column.IntegerColumn;
import de.viadee.xai.anchor.adapter.tabular.column.StringColumn;

public class TitanicDataset {

    public static AnchorTabular createTabularTrainingDefinition(String pathTrainingdata) {
        InputStream trainingDataStream = ClassLoader.getSystemResourceAsStream(pathTrainingdata);

        return createTabularTrainingDefinition(trainingDataStream);
    }

    public static AnchorTabular createTabularTrainingDefinition(InputStream trainingDataStream) {
        if (trainingDataStream == null)
            throw new RuntimeException("Could not load data");
        try {
            return new AnchorTabularBuilderSequential().setDoBalance(false)
                    .addColumn(DoubleColumn.fromStringInput("Age", -1, 5))
                    .addColumn(new StringColumn("Sex"))
                    .addColumn(DoubleColumn.fromStringInput("Fare", -1, 6))
                    .addColumn(new StringColumn("Pclass"))
                    .addColumn(IntegerColumn.fromStringInput("SibSp"))
                    .addColumn(IntegerColumn.fromStringInput("Parch"))
                    .addColumn(new StringColumn("Embarked"))
                    .addTargetColumn(IntegerColumn.fromStringInput("Survived"))
                    .build(trainingDataStream, true, false);
        } catch (IOException e) {
            throw new RuntimeException("Could not read data");
        }
    }
}
