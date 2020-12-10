package de.viadee.ki.ipv.model;

import java.util.Objects;

import org.springframework.stereotype.Component;

import hex.genmodel.easy.prediction.BinomialModelPrediction;

@Component
public class MojoModelWrapper {

    public String getTypeClass(Claim claim) {
        return claim.getTypeclass();
    }

    public String wrapRejectedLabel(BinomialModelPrediction p) {
        return p.label;
    }

    public String wrapTypeClass(String typeclass) {
        return Objects.isNull(typeclass) ? "" : typeclass;
    }

    public String wrapDoors(int doors) {
        return String.valueOf(doors);
    }
    
    public String wrapYear(int year) {
        return String.valueOf(year);
    }

    public String wrapPassengers(int passengers) {
        return String.valueOf(passengers);
    }

    public String wrapCosts(double costs) {
        return String.valueOf(costs);
    }

    public String wrapIntToString(int value) {
        return String.valueOf(value);
    }

	public String wrapRepairType(String string) {
		return String.valueOf(string);
	}

}