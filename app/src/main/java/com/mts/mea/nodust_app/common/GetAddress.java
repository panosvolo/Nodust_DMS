package com.mts.mea.nodust_app.common;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.util.List;
import java.util.Locale;

/**
 * Created by Mahmoud on 11/8/2017.
 */

public class GetAddress {
    public static String GetAddress(Context context, Location location)
    {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;

    }
}
