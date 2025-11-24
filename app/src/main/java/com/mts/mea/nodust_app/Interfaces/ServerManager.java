package com.mts.mea.nodust_app.Interfaces;

import android.location.Location;
import android.util.Log;

import com.mts.mea.nodust_app.User.User;
import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by Mahmoud on 9/7/2017.
 */

public class ServerManager {

    public static HashMap<String,String> getHeadersMap(User user)
    {
        HashMap<String,String> headers=new HashMap<String, String>(5);
        headers.put("UserName", user.UserName);
        headers.put("Longitude",String.valueOf(user.getCurrentLocation().getLongitude()));
        headers.put("Latitude",String.valueOf(user.getCurrentLocation().getLatitude()));
        headers.put("Description",user.getDescription());
        return headers;
    }
    public static HashMap<String,String> getHeadersMap2(User user)
    {
        HashMap<String,String> headers=new HashMap<String, String>(5);
        headers.put("UserName", user.UserName);
        // double lon= User.CurrentLocation.getLongitude();
        headers.put("Longitude",String.valueOf(user.getCurrentLocation().getLongitude()));
        headers.put("Latitude",String.valueOf(user.getCurrentLocation().getLatitude()));
        headers.put("LocDescription",user.getDescription());
        headers.put("Content-Type", "application/json");


        return headers;
    }
    public static HashMap<String,String> getHeadersMap3(User user)
    {
        HashMap<String,String> headers=new HashMap<String, String>(5);
        headers.put("UserName", user.UserName);
        // double lon= User.CurrentLocation.getLongitude();
        Location loc=user.getCurrentLocation();
        headers.put("Longitude",String.valueOf(user.getCurrentLocation().getLongitude()));
        headers.put("Latitude",String.valueOf(user.getCurrentLocation().getLatitude()));
        headers.put("Description",user.getDescription());
        headers.put("Content-Type", "application/json; charset=utf-8");
    return headers;}
    public static <T> T deSerializeStringToObject(String responseStr, Class<T> classOfT) {
        Gson gson = new Gson();
        T serviceResponse = null;
        try {
            serviceResponse = gson.fromJson(responseStr, classOfT);
        } catch (Exception e) {
            Log.e("Test", e.toString());
        }
        return serviceResponse;
    }

    public static String serializeObjectToString(Object Requestobj) {
        Gson gson = new Gson();
        String requestString = gson.toJson(Requestobj);
        return requestString;
    }
}
