package com.mts.mea.nodust_app.Vacation;

import java.io.Serializable;


public class VacationRequest implements Serializable
{
    private String RequestDate;
    private String RequesterID;
    private String Reason_AR;
    private  String Reason_EN;
    private String Notes;
    private String Vacation_from_date;
    private String Vacation_to_date;
    private int  No_days;
    private String approval_status_EN;
    private String approval_status_AR;
    private double ded_day;
    private double ded_payment;
    private int Request_state;
    private String Performer;
    private String PerformerDate;
    private String SupervisorComment;
    private String UserName;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }



    public double getDed_day() {
        return ded_day;
    }

    public String getApproval_status_AR() {
        return approval_status_AR;
    }

    public void setApproval_status_AR(String approval_status_AR) {
        this.approval_status_AR = approval_status_AR;
    }

    public String getApproval_status_EN() {
        return approval_status_EN;
    }

    public void setApproval_status_EN(String approval_status_EN) {
        this.approval_status_EN = approval_status_EN;
    }

    public String getReason_AR() {
        return Reason_AR;
    }

    public void setReason_AR(String reason_AR) {
        Reason_AR = reason_AR;
    }

    public String getReason_EN() {
        return Reason_EN;
    }

    public void setReason_EN(String reason_EN) {
        Reason_EN = reason_EN;
    }

    public void setDed_day(double ded_day) {
        this.ded_day = ded_day;
    }

    public double getDed_payment() {
        return ded_payment;
    }

    public void setDed_payment(double ded_payment) {
        this.ded_payment = ded_payment;
    }

    public int getNo_days() {
        return No_days;
    }

    public void setNo_days(int no_days) {
        No_days = no_days;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getPerformer() {
        return Performer;
    }

    public void setPerformer(String performer) {
        Performer = performer;
    }

    public String getPerformerDate() {
        return PerformerDate;
    }

    public void setPerformerDate(String performerDate) {
        PerformerDate = performerDate;
    }

    public int getRequest_state() {
        return Request_state;
    }

    public void setRequest_state(int request_state) {
        Request_state = request_state;
    }

    public String getRequestDate() {
        return RequestDate;
    }

    public void setRequestDate(String requestDate) {
        RequestDate = requestDate;
    }

    public String getRequesterID() {
        return RequesterID;
    }

    public void setRequesterID(String requesterID) {
        RequesterID = requesterID;
    }

    public String getSupervisorComment() {
        return SupervisorComment;
    }

    public void setSupervisorComment(String supervisorComment) {
        SupervisorComment = supervisorComment;
    }

    public String getVacation_from_date() {
        return Vacation_from_date;
    }

    public void setVacation_from_date(String vacation_from_date) {
        Vacation_from_date = vacation_from_date;
    }

    public String getVacation_to_date() {
        return Vacation_to_date;
    }

    public void setVacation_to_date(String vacation_to_date) {
        Vacation_to_date = vacation_to_date;
    }
}
