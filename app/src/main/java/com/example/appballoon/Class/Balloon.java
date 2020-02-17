package com.example.appballoon.Class;

public class Balloon {
    private String license,cda,cdm,insurance,rcda;
    //private boolean cda,cdm;
    //private Timestamp insurance;



    public Balloon() {
    }

    public Balloon(String license, String cda, String cdm, String insurance, String rcda) {
        this.license = license;
        this.cda = cda;
        this.cdm = cdm;
        this.insurance = insurance;
        this.rcda=rcda;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getCda() {
        return cda;
    }

    public void setCda(String cda) {
        this.cda = cda;
    }

    public String getCdm() {
        return cdm;
    }

    public void setCdm(String cdm) {
        this.cdm = cdm;
    }


    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getRcda() {
        return rcda;
    }

    public void setRcda(String rcda) {
        this.rcda = rcda;
    }
}