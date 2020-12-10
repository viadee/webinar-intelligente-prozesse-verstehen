package de.viadee.ki.ipv.model;

import java.io.Serializable;

public class Claim implements Serializable {

    private static final long serialVersionUID = 4636630587483644817L;

    private int id;
    private int year;
    private double costs;
    private String typeclass;
    private int passengers;
    private int doors;
    private String repairtype;
    private int rejected;

    public Claim(int id, int year, double costs, String typeclass, int passengers, int doors, String repairtype,
            int rejected) {
        this.id = id;
        this.setYear(year);
        this.setCosts(costs);
        this.setTypeclass(typeclass);
        this.setPassengers(passengers);
        this.setDoors(doors);
        this.setRepairtype(repairtype);
        this.setRejected(rejected);
    }

    public int getRejected() {
        return rejected;
    }

    public void setRejected(int rejected) {
        this.rejected = rejected;
    }

    public String getRepairtype() {
        return repairtype;
    }

    public void setRepairtype(String repairtype) {
        this.repairtype = repairtype;
    }

    public int getDoors() {
        return doors;
    }

    public void setDoors(int doors) {
        this.doors = doors;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public String getTypeclass() {
        return typeclass;
    }

    public void setTypeclass(String typeclass) {
        this.typeclass = typeclass;
    }

    public double getCosts() {
        return costs;
    }

    public void setCosts(double costs) {
        this.costs = costs;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return "Claim[id="+id+",rejected="+rejected+"]";
    }

}