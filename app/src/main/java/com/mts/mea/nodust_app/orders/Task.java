package com.mts.mea.nodust_app.orders;


import java.io.Serializable;


public class Task implements Serializable {

    private String assignmentId;
    private String driveID;
    private String cardNo;
    private String areaId;
    private String areaName;
    private String clientName;
    private String city_name;
    private String region_name;
    private String street_name;
    private String home_no;
    private String floor_no;
    private String flat_no;
    private String remarks;
    // public  $customerAddress;
    private String operationComment;
    private String dataComment;
    private String Priority;
    private String changeDate;
    private double X;
    private double Y;
    private String RecieptNo;
    private String TelNo;
    private String Status;
    private String PilotID;
    private String AID;
    private String ASSIGN_DATE;
    private String LAST_DOWNLOAD;
    private String PRIORITY;
    private String ORDER_DELIVERY;
    private String CREATED_STATE;
    private String CHANGED_STATE;
    private String DELIVERY_STATE;
    private String CUSTOMER_TYPE;
    private String AREA2_NAME;
    private String PAY_CREDIT;
    private String ADDRESS;
    private String Duration;
    private String FU_Note;
    private double ACTUAL_PAID;
    private double TOTAL_PRICE_PAID;
    private int ALLOW_CREDIT;
    private double bonus;
    private String From_time;
    private String To_time;
    private int ASSIGNMENTS_TYPE;

    public int getLOC_CONFIRMED() {
        return LOC_CONFIRMED;
    }

    public void setLOC_CONFIRMED(int LOC_CONFIRMED) {
        this.LOC_CONFIRMED = LOC_CONFIRMED;
    }

    private int LOC_CONFIRMED;


    public int getASSIGNMENTS_TYPE() {
        return ASSIGNMENTS_TYPE;
    }

    public void setASSIGNMENTS_TYPE(int ASSIGNMENTS_TYPE) {
        this.ASSIGNMENTS_TYPE = ASSIGNMENTS_TYPE;
    }

    public String getFrom_time() {
        return From_time;
    }

    public void setFrom_time(String from_time) {
        From_time = from_time;
    }

    public String getTo_time() {
        return To_time;
    }

    public void setTo_time(String to_time) {
        To_time = to_time;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public double getACTUAL_PAID() {
        return ACTUAL_PAID;
    }

    public void setACTUAL_PAID(double ACTUAL_PAID) {
        this.ACTUAL_PAID = ACTUAL_PAID;
    }

    public int getALLOW_CREDIT() {
        return ALLOW_CREDIT;
    }

    public void setALLOW_CREDIT(int ALLOW_CREDIT) {
        this.ALLOW_CREDIT = ALLOW_CREDIT;
    }

    public double getTOTAL_PRICE_PAID() {
        return TOTAL_PRICE_PAID;
    }

    public void setTOTAL_PRICE_PAID(double TOTAL_PRICE_PAID) {
        this.TOTAL_PRICE_PAID = TOTAL_PRICE_PAID;
    }

    public String getFU_Note() {
        return FU_Note;
    }

    public void setFU_Note(String FU_Note) {
        this.FU_Note = FU_Note;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getPAY_CREDIT() {
        return PAY_CREDIT;
    }

    public void setPAY_CREDIT(String PAY_CREDIT) {
        this.PAY_CREDIT = PAY_CREDIT;
    }

    public void setAREA2_NAME(String AREA2_NAME) {
        this.AREA2_NAME = AREA2_NAME;
    }

    public String getAREA2_NAME() {
        return AREA2_NAME;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }


    public void setFlat_no(String flat_no) {
        this.flat_no = flat_no;
    }

    public void setFloor_no(String floor_no) {
        this.floor_no = floor_no;
    }

    public void setHome_no(String home_no) {
        this.home_no = home_no;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setStreet_name(String street_name) {
        this.street_name = street_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public String getRegion_name() {
        return region_name;
    }


    public String getStreet_name() {
        return street_name;
    }

    public String getHome_no() {
        return home_no;
    }

    public String getFloor_no() {
        return floor_no;
    }

    public String getFlat_no() {
        return flat_no;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getCUSTOMER_TYPE() {
        return CUSTOMER_TYPE;
    }

    public void setCUSTOMER_TYPE(String CUSTOMER_TYPE) {
        this.CUSTOMER_TYPE = CUSTOMER_TYPE;
    }

    public void setAID(String AID) {
        this.AID = AID;
    }

    public void setASSIGN_DATE(String ASSIGN_DATE) {
        this.ASSIGN_DATE = ASSIGN_DATE;
    }

    public void setLAST_DOWNLOAD(String LAST_DOWNLOAD) {
        this.LAST_DOWNLOAD = LAST_DOWNLOAD;
    }

    public void setPRIORITY(String PRIORITY) {
        this.PRIORITY = PRIORITY;
    }

    public void setORDER_DELIVERY(String ORDER_DELIVERY) {
        this.ORDER_DELIVERY = ORDER_DELIVERY;
    }

    public void setCREATED_STATE(String CREATED_STATE) {
        this.CREATED_STATE = CREATED_STATE;
    }

    public void setCHANGED_STATE(String CHANGED_STATE) {
        this.CHANGED_STATE = CHANGED_STATE;
    }

    public void setDELIVERY_STATE(String DELIVERY_STATE) {
        this.DELIVERY_STATE = DELIVERY_STATE;
    }

    public String getAID() {
        return AID;
    }

    public String getASSIGN_DATE() {
        return ASSIGN_DATE;
    }

    public String getLAST_DOWNLOAD() {
        return LAST_DOWNLOAD;
    }

    public String getPRIORITY() {
        return PRIORITY;
    }

    public String getORDER_DELIVERY() {
        return ORDER_DELIVERY;
    }

    public String getCREATED_STATE() {
        return CREATED_STATE;
    }

    public String getCHANGED_STATE() {
        return CHANGED_STATE;
    }

    public String getDELIVERY_STATE() {
        return DELIVERY_STATE;
    }



    public String getPilotID() {
        return PilotID;
    }

    public void setPilotID(String pilotID) {
        PilotID = pilotID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setRecieptNo(String recieptNo) {
        RecieptNo = recieptNo;
    }

    public void setTelNo(String telNo) {
        TelNo = telNo;
    }

    public String getRecieptNo() {
        return RecieptNo;
    }

    public String getTelNo() {
        return TelNo;
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public void setX(double x) {
        X = x;
    }

    public void setY(double y) {
        Y = y;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }

    public String getChangeDate() {
        return changeDate;
    }

    public void setPriority(String priority) {
        Priority = priority;
    }

    public String getPriority() {
        return Priority;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getAssignmentId() {
        return assignmentId;
    }
    public void setDriveID(String driveID) {
        this.driveID = driveID;
    }

    public String getDriveID() {
        return driveID;
    }


    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaId() {
        return areaId;
    }
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaName() {
        return areaName;
    }
    public void setOperationComment(String operationComment) {
        this.operationComment = operationComment;
    }

    public String getOperationComment() {
        return operationComment;
    }


    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardNo() {
        return cardNo;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setDataComment(String dataComment) {
        this.dataComment = dataComment;
    }

    public String getDataComment() {
        return dataComment;
    }
}