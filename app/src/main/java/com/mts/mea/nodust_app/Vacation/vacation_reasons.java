package com.mts.mea.nodust_app.Vacation;

/**
 * Created by Mahmoud on 6/28/2018.
 */

public class vacation_reasons {
    private String ID;
    private String NAME_AR;
    private String NAME_EN;
    private String DEDUCTION;
    private int ALLOWED_DAY0;

    public int getALLOWED_DAY0() {
        return ALLOWED_DAY0;
    }

    public void setALLOWED_DAY0(int ALLOWED_DAY0) {
        this.ALLOWED_DAY0 = ALLOWED_DAY0;
    }

    public String getDEDUCTION() {
        return DEDUCTION;
    }

    public void setDEDUCTION(String DEDUCTION) {
        this.DEDUCTION = DEDUCTION;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNAME_AR() {
        return NAME_AR;
    }

    public void setNAME_AR(String NAME_AR) {
        this.NAME_AR = NAME_AR;
    }

    public String getNAME_EN() {
        return NAME_EN;
    }

    public void setNAME_EN(String NAME_EN) {
        this.NAME_EN = NAME_EN;
    }
}
