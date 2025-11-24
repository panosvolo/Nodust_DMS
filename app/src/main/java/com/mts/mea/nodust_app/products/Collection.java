package com.mts.mea.nodust_app.products;

/**
 * Created by Mahmoud on 9/25/2018.
 */

public class Collection {
    private String ASSIGN_ID;
    int AID;
    private String CARD_NO;
    private String INVOICE_NO;
    double AMOUNT;
    double PAID_AMOUNT;
   private String DATE1;

    public String getDATE1() {
        return DATE1;
    }

    public void setDATE1(String DATE) {
        this.DATE1 = DATE;
    }

    public int getAID() {
        return AID;
    }

    public void setAID(int AID) {
        this.AID = AID;
    }

    public double getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(double AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    public String getASSIGN_ID() {
        return ASSIGN_ID;
    }

    public void setASSIGN_ID(String ASSIGN_ID) {
        this.ASSIGN_ID = ASSIGN_ID;
    }

    public String getCARD_NO() {
        return CARD_NO;
    }

    public void setCARD_NO(String CARD_NO) {
        this.CARD_NO = CARD_NO;
    }

    public String getINVOICE_NO() {
        return INVOICE_NO;
    }

    public void setINVOICE_NO(String INVOICE_NO) {
        this.INVOICE_NO = INVOICE_NO;
    }

    public double getPAID_AMOUNT() {
        return PAID_AMOUNT;
    }

    public void setPAID_AMOUNT(double PAID_AMOUNT) {
        this.PAID_AMOUNT = PAID_AMOUNT;
    }
}
