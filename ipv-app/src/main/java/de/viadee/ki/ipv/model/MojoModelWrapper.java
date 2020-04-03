package de.viadee.ki.ipv.model;

import java.util.Objects;

import org.springframework.stereotype.Component;

import hex.genmodel.easy.prediction.BinomialModelPrediction;

@Component
public class MojoModelWrapper {

    public String getSex(Person person) {
        return person.getSex();
    }

    public String wrapSurvivedLabel(BinomialModelPrediction p) {
        return p.label;
    }

    public String wrapEmbarked(String embarked) {
        return Objects.isNull(embarked) ? "" : embarked;
    }

    public String wrapPclass(int pclass) {
        return String.valueOf(pclass);
    }

    public String wrapParch(int parch) {
        return String.valueOf(parch);
    }

    public String wrapSibSp(int siblings) {
        return String.valueOf(siblings);
    }

    public String wrapIntToString(int value) {
        return String.valueOf(value);
    }

	public String wrapFare(double fare) {
		return String.valueOf(fare);
	}

}