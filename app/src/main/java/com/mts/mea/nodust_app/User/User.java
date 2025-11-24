package com.mts.mea.nodust_app.User;

import android.location.Location;

import java.io.Serializable;

/**
 * Created by Mahmoud on 11/8/2017.
 */

public class User implements Serializable {
    public static String UserName;
    public static String Password;
    public static String Description;
    public static Location CurrentLocation;

    public  void setDescription(String description) {
        Description = description;
    }

    public  String getDescription() {
        return Description;
    }



    public User(String userName, String password, Location currentLocation, String description) {
        UserName = userName;
        Password = password;
        CurrentLocation = currentLocation;
        Description=description;

    }

    public String getUserName() {
        return UserName;
    }

    public String getPassword() {
        return Password;
    }

    public Location getCurrentLocation() {
        return CurrentLocation;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setCurrentLocation(Location currentLocation) {
        CurrentLocation = currentLocation;
    }
    @Override
    public String toString()
    {
        return "User [UserName=" + getUserName() + "Password="+getPassword()+"CurrentLocation="+getCurrentLocation()+"Description="+Description+"]";
    }
}
