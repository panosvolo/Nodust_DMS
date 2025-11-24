package com.mts.mea.nodust_app.User;

import java.io.Serializable;

/**
 * Created by Mahmoud on 11/9/2017.
 */

public class UserInfo implements Serializable {
    private String nameAr;
    private String nameEn;
    private String shiftID;
    private String status;
    private int USERGROUP_ID;
    private String Evalution_time;
    private int Balance_Vacation_Day;

    public int getBalance_Vacation_Day() {
        return Balance_Vacation_Day;
    }

    public void setBalance_Vacation_Day(int balance_Vacation_Day) {
        Balance_Vacation_Day = balance_Vacation_Day;
    }

    public String getEvalution_time() {
        return Evalution_time;
    }

    public void setEvalution_time(String evalution_time) {
        Evalution_time = evalution_time;
    }

    public String getAss_ID() {
        return Ass_ID;
    }

    public void setAss_ID(String ass_ID) {
        Ass_ID = ass_ID;
    }

    private String Ass_ID;

    public int getUSERGROUP_ID() {
        return USERGROUP_ID;
    }

    public void setUSERGROUP_ID(int USERGROUP_ID) {
        this.USERGROUP_ID = USERGROUP_ID;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameAr() {
        return nameAr;
    }
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getShiftID() {
        return shiftID;
    }

    public void setShiftID(String shiftID) {
        this.shiftID = shiftID;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
