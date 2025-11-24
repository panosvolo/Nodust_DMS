package com.mts.mea.nodust_app.CloseCodes_Groups;

import com.mts.mea.nodust_app.products.Collection;
import com.mts.mea.nodust_app.products.Product;
import com.mts.mea.nodust_app.orders.Task;

import java.util.List;

/**
 * Created by Mahmoud on 11/9/2017.
 */

public class ActionObject {
    private String assignmentID;
    private String contractStatus;
    private String closeCode;
    private String closeCodeReason;
    private String CancelcloseCode;
    private String CancelcloseCodeReason;
    private Double longitude;
    private Double latitude;
    private String comments;
    private String actionDate;
    private String DRNo;
    private double TOTAL_PRICE;
    private List<Product>ReplacedProducts;
    private List<Product>NotReplacedProducts;
    private List<Product>Handles;
    private double Actual_paid;
    private List<Collection>Collection;

    public List<com.mts.mea.nodust_app.products.Collection> getCollection() {
        return Collection;
    }

    public void setCollection(List<com.mts.mea.nodust_app.products.Collection> collection) {
        Collection = collection;
    }

    public double getActual_paid() {
        return Actual_paid;
    }

    public void setActual_paid(double actual_paid) {
        Actual_paid = actual_paid;
    }

    public String getCancelcloseCode() {
        return CancelcloseCode;
    }

    public void setCancelcloseCode(String cancelcloseCode) {
        CancelcloseCode = cancelcloseCode;
    }

    public String getCancelcloseCodeReason() {
        return CancelcloseCodeReason;
    }

    public void setCancelcloseCodeReason(String cancelcloseCodeReason) {
        CancelcloseCodeReason = cancelcloseCodeReason;
    }

    public void setHandles(List<Product> handles) {
        Handles = handles;
    }

    public List<Product> getHandles() {
        return Handles;
    }

    private String CARD_NO;
    private String deliveryMan;
    private Task currentTask;
    private boolean NoMoney;

    public void setNoMoney(boolean noMoney) {
        NoMoney = noMoney;
    }

    public boolean isNoMoney() {
        return NoMoney;
    }

    public List<Product> getNotReplacedProducts() {
        return NotReplacedProducts;
    }

    public void setNotReplacedProducts(List<Product> notReplacedProducts) {
        NotReplacedProducts = notReplacedProducts;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public String getCARD_NO() {
        return CARD_NO;
    }

    public String getDeliveryMan() {
        return deliveryMan;
    }

    public void setCARD_NO(String CARD_NO) {
        this.CARD_NO = CARD_NO;
    }

    public void setDeliveryMan(String deliveryMan) {
        this.deliveryMan = deliveryMan;
    }

    public void setReplacedProducts(List<Product> replacedProducts) {
        ReplacedProducts = replacedProducts;
    }

    public List<Product> getReplacedProducts() {
        return ReplacedProducts;
    }

    public void setTOTAL_PRICE(double TOTAL_PRICE) {
        this.TOTAL_PRICE = TOTAL_PRICE;
    }

    public double getTOTAL_PRICE() {
        return TOTAL_PRICE;
    }

    public void setDRNo(String DRNo) {
        this.DRNo = DRNo;
    }

    public String getDRNo() {
        return DRNo;
    }

    public void setAssignmentID(String assignmentID) {
        this.assignmentID = assignmentID;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public void setCloseCode(String closeCode) {
        this.closeCode = closeCode;
    }

    public void setCloseCodeReason(String closeCodeReason) {
        this.closeCodeReason = closeCodeReason;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setActionDate(String actionDate) {
        this.actionDate = actionDate;
    }

    public String getAssignmentID() {
        return assignmentID;
    }

    public String getCloseCode() {
        return closeCode;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public String getCloseCodeReason() {
        return closeCodeReason;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getComments() {
        return comments;
    }

    public String getActionDate() {
        return actionDate;
    }
}
