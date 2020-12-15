package com.group4.softwareanalytics.metrics;

public class ProjectMetric {
    private float LOC;
    private float CBO;
    private float WMC;
    private float LCOM;

    private float parentLOC;
    private float parentCBO;
    private float parentWMC;
    private float parentLCOM;

    private float deltaLOC;
    private float deltaCBO;
    private float deltaWMC;
    private float deltaLCOM;

    public ProjectMetric( float CBO, float WMC, float LOC, float LCOM, float parentCBO, float parentWMC, float parentLOC, float parentLCOM ) {
        this.LOC = LOC;
        this.CBO = CBO;
        this.WMC = WMC;
        this.LCOM = LCOM;

        this.parentLOC = parentLOC;
        this.parentCBO = parentCBO;
        this.parentWMC = parentWMC;
        this.parentLCOM = parentLCOM;

        this.deltaCBO = parentCBO - CBO;
        this.deltaLCOM = parentLCOM - LCOM;
        this.deltaLOC = parentLOC - LOC;
        this.deltaWMC = parentWMC - WMC;
    }

    public float getLOC() {
        return LOC;
    }

    public void setLOC(float LOC) {
        this.LOC = LOC;
        this.deltaLOC = this.parentLOC - LOC;
    }

    public float getCBO() {
        return CBO;
    }

    public void setCBO(float CBO) {
        this.CBO = CBO;
        this.deltaCBO = this.parentCBO - CBO;
    }

    public float getWMC() {
        return WMC;
    }

    public void setWMC(float WMC) {
        this.WMC = WMC;
        this.deltaWMC = this.parentWMC - WMC;
    }

    public float getLCOM() {
        return LCOM;
    }

    public void setLCOM(float LCOM) {
        this.LCOM = LCOM;
        this.deltaLCOM = this.parentLCOM - LCOM;
    }

    public float getParentLOC() {
        return parentLOC;
    }

    public void setParentLOC(float parentLOC) {
        this.parentLOC = parentLOC;
        this.deltaLOC = parentLOC - this.LOC;
    }

    public float getParentCBO() {
        return parentCBO;
    }

    public void setParentCBO(float parentCBO) {
        this.parentCBO = parentCBO;
        this.deltaCBO = parentCBO - this.CBO;
    }

    public float getParentWMC() {
        return parentWMC;
    }

    public void setParentWMC(float parentWMC) {
        this.parentWMC = parentWMC;
        this.deltaWMC = parentWMC - this.WMC;
    }

    public float getParentLCOM() {
        return parentLCOM;
    }

    public void setParentLCOM(float parentLCOM) {
        this.parentLCOM = parentLCOM;
        this.deltaLCOM = parentLCOM - this.LCOM;
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
