package com.mts.mea.nodust_app.Pilots.PilotCover;

/**
 * Created by Mahmoud on 5/22/2018.
 */

public class Pilot_Cover {
    private String ID;
    private String Pilot_ID;
    private String Cover_Driver_ID;
    private String Assign_Date;
    private int Quantity;
    private int Current_Quantity;
    private int Dirty_Qty;
    private String Product_ID;
    private String name;
    private String Treatment_ID;
    private String Treatment_Name;
    private String DRIVER_ID;
    private String Pilot_Accept;
    private  String Actual_Clean;
    private   String   Actual_Dirty;
    private  String Approved_clean;
    private  String Approved_Dirty;
    private  String Driver_Accept;
    private String TOTAL_RECIEPT_AMOUNT_PILOT;
    private String  TOTAL_RECIEPT_AMOUNT_DRIVER;
    private   String Area_ID;
    private  String Area_Name;

    public String getArea_ID() {
        return Area_ID;
    }

    public void setArea_ID(String area_ID) {
        Area_ID = area_ID;
    }

    public String getArea_Name() {
        return Area_Name;
    }

    public void setArea_Name(String area_Name) {
        Area_Name = area_Name;
    }

    public String getTOTAL_RECIEPT_AMOUNT_DRIVER() {
        return TOTAL_RECIEPT_AMOUNT_DRIVER;
    }

    public void setTOTAL_RECIEPT_AMOUNT_DRIVER(String TOTAL_RECIEPT_AMOUNT_DRIVER) {
        this.TOTAL_RECIEPT_AMOUNT_DRIVER = TOTAL_RECIEPT_AMOUNT_DRIVER;
    }

    public String getTOTAL_RECIEPT_AMOUNT_PILOT() {
        return TOTAL_RECIEPT_AMOUNT_PILOT;
    }

    public void setTOTAL_RECIEPT_AMOUNT_PILOT(String TOTAL_RECIEPT_AMOUNT_PILOT) {
        this.TOTAL_RECIEPT_AMOUNT_PILOT = TOTAL_RECIEPT_AMOUNT_PILOT;
    }

    public String getActual_Clean() {
        return Actual_Clean;
    }

    public void setActual_Clean(String actual_Clean) {
        Actual_Clean = actual_Clean;
    }

    public String getActual_Dirty() {
        return Actual_Dirty;
    }

    public void setActual_Dirty(String actual_Dirty) {
        Actual_Dirty = actual_Dirty;
    }

    public String getApproved_clean() {
        return Approved_clean;
    }

    public void setApproved_clean(String approved_clean) {
        Approved_clean = approved_clean;
    }

    public String getApproved_Dirty() {
        return Approved_Dirty;
    }

    public void setApproved_Dirty(String approved_Dirty) {
        Approved_Dirty = approved_Dirty;
    }

    public String getDriver_Accept() {
        return Driver_Accept;
    }

    public void setDriver_Accept(String driver_Accept) {
        Driver_Accept = driver_Accept;
    }

    public String getPilot_Accept() {
        return Pilot_Accept;
    }

    public int getDirty_Qty() {
        return Dirty_Qty;
    }

    public void setDirty_Qty(int dirty_Qty) {
        Dirty_Qty = dirty_Qty;
    }

    public void setPilot_Accept(String pilot_Accept) {
        Pilot_Accept = pilot_Accept;
    }

    public String getAssign_Date() {
        return Assign_Date;
    }

    public void setAssign_Date(String assign_Date) {
        Assign_Date = assign_Date;
    }

    public String getCover_Driver_ID() {
        return Cover_Driver_ID;
    }

    public void setCover_Driver_ID(String cover_Driver_ID) {
        Cover_Driver_ID = cover_Driver_ID;
    }

    public int getCurrent_Quantity() {
        return Current_Quantity;
    }

    public void setCurrent_Quantity(int current_Quantity) {
        Current_Quantity = current_Quantity;
    }

    public String getDRIVER_ID() {
        return DRIVER_ID;
    }

    public void setDRIVER_ID(String DRIVER_ID) {
        this.DRIVER_ID = DRIVER_ID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPilot_ID() {
        return Pilot_ID;
    }

    public void setPilot_ID(String pilot_ID) {
        Pilot_ID = pilot_ID;
    }

    public String getProduct_ID() {
        return Product_ID;
    }

    public void setProduct_ID(String product_ID) {
        Product_ID = product_ID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getTreatment_ID() {
        return Treatment_ID;
    }

    public void setTreatment_ID(String treatment_ID) {
        Treatment_ID = treatment_ID;
    }

    public String getTreatment_Name() {
        return Treatment_Name;
    }

    public void setTreatment_Name(String treatment_Name) {
        Treatment_Name = treatment_Name;
    }
}
