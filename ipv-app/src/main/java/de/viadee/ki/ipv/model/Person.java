package de.viadee.ki.ipv.model;

import java.io.Serializable;

public class Person implements Serializable {

    private static final long serialVersionUID = 4636630587983644817L;

    private int id;
    private int age;
    private double fare;
    private String sex;
    private int siblings;
    private int pclass;
    private String ticket;
    private String cabin;
    private String emabrked;
    private int parch;
    private String name;
    private int survived;

    public Person(int id, int age, double fare, String sex, int siblings, int pclass, String ticket, String cabin,
            String emabrked, int parch, String name, int survived) {
        this.id = id;
        this.age = age;
        this.fare = fare;
        this.sex = sex;
        this.siblings = siblings;
        this.pclass = pclass;
        this.ticket = ticket;
        this.cabin = cabin;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getSiblings() {
        return siblings;
    }

    public void setSiblings(int siblings) {
        this.siblings = siblings;
    }

    public int getPclass() {
        return pclass;
    }

    public void setPclass(int pclass) {
        this.pclass = pclass;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getCabin() {
        return cabin;
    }

    public void setCabin(String cabin) {
        this.cabin = cabin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmabrked() {
        return emabrked;
    }

    public void setEmabrked(String emabrked) {
        this.emabrked = emabrked;
    }

    public int getParch() {
        return parch;
    }

    public void setParch(int parch) {
        this.parch = parch;
    }

    public int getSurvived() {
        return survived;
    }

    public void setSurvived(int survived) {
        this.survived = survived;
    }

    @Override
    public String toString() {
        return "Person [age=" + age + ", cabin=" + cabin + ", emabrked=" + emabrked + ", fare=" + fare + ", id=" + id
                + ", name=" + name + ", parch=" + parch + ", pclass=" + pclass + ", sex=" + sex + ", siblings="
                + siblings + ", survived=" + survived + ", ticket=" + ticket + "]";
    }

}