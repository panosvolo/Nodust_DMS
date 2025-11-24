package com.mts.mea.nodust_app.Application;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityUtil {

    public static final String WIFI = "WIFI Network";
    public static final String MOBILE = "MOBILE Network";

    /**
     * This method is to check the internet connctivity
     *
     *
     * @author  Mahmoud
     * @version 1.0
     * @since   10/11/2015
     *
     */

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    /**
     * This method is to get the network type (WIFI or Data)
     *
     *
     * @author  Mahmoud
     * @version 1.0
     * @since   10/11/2015
     *
     */

    public static String getNetWorkType(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info != null && info.isConnected()) {

            switch (info.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    return WIFI;
                case ConnectivityManager.TYPE_MOBILE:
                    return MOBILE;
            }
        }

        return "Unknown";
    }
}
