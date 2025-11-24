package com.mts.mea.nodust_app.CloseCodes_Groups;

/**
 * Created by Mahmoud on 11/9/2017.
 */

public class CloseCode {
    private String closeCodeID;
    private String closeCodeReason;
    private String CLOSE_CODE_REASON_EN;

    public String getCLOSE_CODE_REASON_EN() {
        return CLOSE_CODE_REASON_EN;
    }

    public void setCLOSE_CODE_REASON_EN(String CLOSE_CODE_REASON_EN) {
        this.CLOSE_CODE_REASON_EN = CLOSE_CODE_REASON_EN;
    }

    public void setCloseCodeID(String closeCodeID) {
        this.closeCodeID = closeCodeID;
    }

    public String getCloseCodeID() {
        return closeCodeID;
    }
    public void setCloseCodeReason(String closeCodeReason) {
        this.closeCodeReason = closeCodeReason;
    }

    public String getCloseCodeReason() {
        return closeCodeReason;
    }
}
