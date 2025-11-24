package com.mts.mea.nodust_app.Interfaces;

import java.io.Serializable;

/**
 * Created by Mahmoud on 9/7/2017.
 */

public class FailResponse implements Serializable {

    private String code;

    private String message;

    private String state;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
