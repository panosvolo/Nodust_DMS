package com.mts.mea.nodust_app.products;

public class Product {
    private String PRODUCT_ID;
    private String NAME;
    private String AID;
    private String QUNTITY;
    private String QUNTITY_REPLACED;
    private int neededPilot;
    private int currentQuantityPilot;
    private int HANDLE;
    private String DirtyQTY;
    private int DELIVERY_TYPE;

    public int getDELIVERY_TYPE() {
        return DELIVERY_TYPE;
    }

    public void setDELIVERY_TYPE(int DELIVERY_TYPE) {
        this.DELIVERY_TYPE = DELIVERY_TYPE;
    }

    public String getDirtyQTY() {
        return DirtyQTY;
    }

    public void setDirtyQTY(String dirtyQTY) {
        DirtyQTY = dirtyQTY;
    }

    public int getHANDLE() {
        return HANDLE;
    }

    public void setHANDLE(int HANDLE) {
        this.HANDLE = HANDLE;
    }

    public int getCurrentQuantityPilot() {
        return currentQuantityPilot;
    }

    public void setCurrentQuantityPilot(int currentQuantityPilot) {
        this.currentQuantityPilot = currentQuantityPilot;
    }

    public int getNeededPilot() {
        return neededPilot;
    }

    public void setNeededPilot(int neededPilot) {
        this.neededPilot = neededPilot;
    }

    private String UNIT_PRICE;
    private String CARD_NO;
    private String BUY_PRICE;
    private String ASSIGN_ID;
    private String Type;
    private String Treatment_code;
    private String Treatment_description;
    private String Package_id;
    private String Package_NO;
    private String Description;
    private String KIND;
    private String Price1W;
    private String Price2W;
    private String Price4W;
    private String SellingPrice;
    private String B_price;
    private int CurrentQuantity;
    private String Area_id;
    private String Area_name;
    private String ID;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getArea_id() {
        return Area_id;
    }

    public void setArea_id(String area_id) {
        Area_id = area_id;
    }

    public String getArea_name() {
        return Area_name;
    }

    public void setArea_name(String area_name) {
        Area_name = area_name;
    }

    public int getCurrentQuantity() {
        return CurrentQuantity;
    }

    public void setCurrentQuantity(int currentQuantity) {
        CurrentQuantity = currentQuantity;
    }

    public String getB_price() {
        return B_price;
    }

    public void setB_price(String b_price) {
        B_price = b_price;
    }

    public String getPrice1W() {
        return Price1W;
    }

    public void setPrice1W(String price1W) {
        Price1W = price1W;
    }

    public String getPrice2W() {
        return Price2W;
    }

    public void setPrice2W(String price2W) {
        Price2W = price2W;
    }

    public String getPrice4W() {
        return Price4W;
    }

    public void setPrice4W(String price4W) {
        Price4W = price4W;
    }

    public String getSellingPrice() {
        return SellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        SellingPrice = sellingPrice;
    }

    public String getKIND() {
        return KIND;
    }

    public void setKIND(String KIND) {
        this.KIND = KIND;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setPackage_id(String package_id) {
        Package_id = package_id;
    }

    public void setPackage_NO(String package_NO) {
        Package_NO = package_NO;
    }

    public void setTreatment_code(String treatment_code) {
        Treatment_code = treatment_code;
    }

    public void setTreatment_description(String treatment_description) {
        Treatment_description = treatment_description;
    }

    public String getPackage_id() {
        return Package_id;
    }

    public String getPackage_NO() {
        return Package_NO;
    }

    public String getTreatment_code() {
        return Treatment_code;
    }

    public String getTreatment_description() {
        return Treatment_description;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getASSIGN_ID() {
        return ASSIGN_ID;
    }

    public void setASSIGN_ID(String ASSIGN_ID) {
        this.ASSIGN_ID = ASSIGN_ID;
    }

    public String getBUY_PRICE() {
        return BUY_PRICE;
    }

    public void setBUY_PRICE(String BUY_PRICE) {
        this.BUY_PRICE = BUY_PRICE;
    }

    public void setPRODUCT_ID(String PRODUCT_ID) {
        this.PRODUCT_ID = PRODUCT_ID;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public void setAID(String AID) {
        this.AID = AID;
    }

    public void setQUNTITY(String QUNTITY) {
        this.QUNTITY = QUNTITY;
    }

    public void setQUNTITY_REPLACED(String QUNTITY_REPLACED) {
        this.QUNTITY_REPLACED = QUNTITY_REPLACED;
    }

    public void setUNIT_PRICE(String UNIT_PRICE) {
        this.UNIT_PRICE = UNIT_PRICE;
    }

    public void setCARD_NO(String CARD_NO) {
        this.CARD_NO = CARD_NO;
    }

    public String getPRODUCT_ID() {
        return PRODUCT_ID;
    }

    public String getNAME() {
        return NAME;
    }

    public String getAID() {
        return AID;
    }

    public String getQUNTITY() {
        return QUNTITY;
    }

    public String getQUNTITY_REPLACED() {
        return QUNTITY_REPLACED;
    }

    public String getUNIT_PRICE() {
        return UNIT_PRICE;
    }

    public String getCARD_NO() {
        return CARD_NO;
    }
}
