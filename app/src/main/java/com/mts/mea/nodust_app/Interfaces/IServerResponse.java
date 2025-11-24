package com.mts.mea.nodust_app.Interfaces;

import org.json.JSONException;

import java.text.ParseException;

/**
 * Created by Mahmoud on 9/5/2017.
 */

public interface IServerResponse {
    public void success(String response) throws JSONException, ParseException;
    public void failed(String errorText);
}
