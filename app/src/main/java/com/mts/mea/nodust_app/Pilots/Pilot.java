package com.mts.mea.nodust_app.Pilots;

import java.io.Serializable;

public class Pilot implements Serializable {
    private String  nameAr;
    private String  nameEn;
    private String  pilotID;
    private String DRIVER_ID;
    private String AttendanceTime;
    private String ASSIGN_PILOT_ID;
    private String RETURN_Time;
    private String EVALUTION_DATE;
    private String Area_Name;

    public String getArea_Name() {
        return Area_Name;
    }

    public void setArea_Name(String area_Name) {
        Area_Name = area_Name;
    }

    public String getEVALUTION_DATE() {
        return EVALUTION_DATE;
    }

    public void setEVALUTION_DATE(String EVALUTION_DATE) {
        this.EVALUTION_DATE = EVALUTION_DATE;
    }

    public String getRETURN_Time() {
        return RETURN_Time;
    }

    public void setRETURN_Time(String RETURN_Time) {
        this.RETURN_Time = RETURN_Time;
    }

    public void setDRIVER_ID(String DRIVER_ID) {
        this.DRIVER_ID = DRIVER_ID;
    }

    public String getDRIVER_ID() {
        return DRIVER_ID;
    }

    public String getASSIGN_PILOT_ID() {
        return ASSIGN_PILOT_ID;
    }

    public void setASSIGN_PILOT_ID(String ASSIGN_PILOT_ID) {
        this.ASSIGN_PILOT_ID = ASSIGN_PILOT_ID;
    }

    public String getAttendanceTime() {
        return AttendanceTime;
    }

    public void setAttendanceTime(String attendanceTime) {
        AttendanceTime = attendanceTime;
    }

    public String getNameAr() {
        return nameAr;
    }

    public String getNameEn() {
        return nameEn;
    }

    public String getPilotID() {
        return pilotID;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public void setPilotID(String pilotID) {
        this.pilotID = pilotID;
    }
}
