package com.group4.softwareanalytics.metrics;

public class ProjectMetric {
    private float loc;
    private float cbo;
    private float wmc;
    private float lcom;

    private float parentLOC;
    private float parentCBO;
    private float parentWMC;
    private float parentLCOM;

    private float deltaLOC;
    private float deltaCBO;
    private float deltaWMC;
    private float deltaLCOM;

    public ProjectMetric(float cbo, float wmc, float loc, float lcom, float parentCBO, float parentWMC, float parentLOC, float parentLCOM ) {
        this.loc = loc;
        this.cbo = cbo;
        this.wmc = wmc;
        this.lcom = lcom;

        this.parentLOC = parentLOC;
        this.parentCBO = parentCBO;
        this.parentWMC = parentWMC;
        this.parentLCOM = parentLCOM;

        this.deltaCBO = parentCBO - cbo;
        this.deltaLCOM = parentLCOM - lcom;
        this.deltaLOC = parentLOC - loc;
        this.deltaWMC = parentWMC - wmc;
    }

    public float getLoc() {
        return loc;
    }

    public void setLoc(float loc) {
        this.loc = loc;
        this.deltaLOC = this.parentLOC - loc;
    }

    public float getCbo() {
        return cbo;
    }

    public void setCbo(float cbo) {
        this.cbo = cbo;
        this.deltaCBO = this.parentCBO - cbo;
    }

    public float getWmc() {
        return wmc;
    }

    public void setWmc(float wmc) {
        this.wmc = wmc;
        this.deltaWMC = this.parentWMC - wmc;
    }

    public float getLcom() {
        return lcom;
    }

    public void setLcom(float lcom) {
        this.lcom = lcom;
        this.deltaLCOM = this.parentLCOM - lcom;
    }

    public float getParentLOC() {
        return parentLOC;
    }

    public void setParentLOC(float parentLOC) {
        this.parentLOC = parentLOC;
        this.deltaLOC = parentLOC - this.loc;
    }

    public float getParentCBO() {
        return parentCBO;
    }

    public void setParentCBO(float parentCBO) {
        this.parentCBO = parentCBO;
        this.deltaCBO = parentCBO - this.cbo;
    }

    public float getParentWMC() {
        return parentWMC;
    }

    public void setParentWMC(float parentWMC) {
        this.parentWMC = parentWMC;
        this.deltaWMC = parentWMC - this.wmc;
    }

    public float getParentLCOM() {
        return parentLCOM;
    }

    public void setParentLCOM(float parentLCOM) {
        this.parentLCOM = parentLCOM;
        this.deltaLCOM = parentLCOM - this.lcom;
    }

    public float getDeltaLOC() {
        return deltaLOC;
    }

    public void setDeltaLOC(float deltaLOC) {
        this.deltaLOC = deltaLOC;
    }

    public float getDeltaCBO() {
        return deltaCBO;
    }

    public void setDeltaCBO(float deltaCBO) {
        this.deltaCBO = deltaCBO;
    }

    public float getDeltaWMC() {
        return deltaWMC;
    }

    public void setDeltaWMC(float deltaWMC) {
        this.deltaWMC = deltaWMC;
    }

    public float getDeltaLCOM() {
        return deltaLCOM;
    }

    public void setDeltaLCOM(float deltaLCOM) {
        this.deltaLCOM = deltaLCOM;
    }
}
