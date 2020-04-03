package de.viadee.ki.ipv.model;

import hex.genmodel.ModelMojoReader;
import hex.genmodel.MojoModel;
import hex.genmodel.MojoReaderBackend;
import hex.genmodel.MojoReaderBackendFactory;
import hex.genmodel.easy.EasyPredictModelWrapper;
import hex.genmodel.easy.RowData;
import hex.genmodel.easy.exception.PredictException;
import hex.genmodel.easy.prediction.BinomialModelPrediction;

import java.io.IOException;
import java.net.URL;

import de.viadee.xai.anchor.adapter.tabular.TabularInstance;

public class H2OModelWrapper {
    private final EasyPredictModelWrapper model;

    public H2OModelWrapper(URL urlToMojoZip) throws IOException {
        final MojoReaderBackend reader = MojoReaderBackendFactory.createReaderBackend(urlToMojoZip, MojoReaderBackendFactory.CachingStrategy.MEMORY);
        final MojoModel mojoModel = ModelMojoReader.readFrom(reader);
        model = new EasyPredictModelWrapper(mojoModel);
    }

    public int predict(TabularInstance instance) {
        final RowData row = new RowData();

        row.put("Age", instance.getTransformedValue("Age"));
        row.put("Sex", instance.getTransformedValue("Sex"));
        row.put("Fare", instance.getTransformedValue("Fare"));
        row.put("Pclass", instance.getTransformedValue("Pclass"));
        row.put("SibSp", ((Integer) instance.getTransformedValue("SibSp")).doubleValue());
        row.put("Parch", ((Integer) instance.getTransformedValue("Parch")).doubleValue());
        row.put("Embarked", instance.getTransformedValue("Embarked"));

        try {
            return ((BinomialModelPrediction) model.predict(row)).labelIndex;
        } catch (PredictException e) {
            throw new RuntimeException(e);
        }
    }
}
