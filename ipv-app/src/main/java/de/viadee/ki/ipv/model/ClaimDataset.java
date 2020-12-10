package de.viadee.ki.ipv.model;

import java.io.IOException;
import java.io.InputStream;

import de.viadee.xai.anchor.adapter.tabular.AnchorTabular;
import de.viadee.xai.anchor.adapter.tabular.builder.AnchorTabularBuilderSequential;
import de.viadee.xai.anchor.adapter.tabular.column.DoubleColumn;
import de.viadee.xai.anchor.adapter.tabular.column.IntegerColumn;
import de.viadee.xai.anchor.adapter.tabular.column.StringColumn;

public class ClaimDataset {

    public static AnchorTabular createTabularTrainingDefinition(String pathTrainingdata) {
        InputStream trainingDataStream = ClassLoader.getSystemResourceAsStream(pathTrainingdata);

        return createTabularTrainingDefinition(trainingDataStream);
    }

    public static AnchorTabular createTabularTrainingDefinition(InputStream trainingDataStream) {
        if (trainingDataStream == null)
            throw new RuntimeException("Could not load data");
        try {
            //"ClaimId","Rejected","TypeClass","Year","Doors","Passangers","Costs","RepairType"

            return new AnchorTabularBuilderSequential().setDoBalance(false)
                    .addColumn(IntegerColumn.fromStringInput("ClaimId"))
                    .addTargetColumn(IntegerColumn.fromStringInput("Rejected"))
                    .addColumn(new StringColumn("TypeClass"))
                    .addColumn(IntegerColumn.fromStringInput("Year"))
                    .addColumn(IntegerColumn.fromStringInput("Doors"))
                    .addColumn(IntegerColumn.fromStringInput("Passengers"))
                    .addColumn(DoubleColumn.fromStringInput("Costs",-1,5))
                    .addColumn(new StringColumn("RepairType"))
                    .build(trainingDataStream, true, false);
        } catch (IOException e) {
            throw new RuntimeException("Could not read data");
        }
    }
}
