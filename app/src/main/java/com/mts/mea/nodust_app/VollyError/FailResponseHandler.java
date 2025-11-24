package com.mts.mea.nodust_app.VollyError;

import android.content.Context;

import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ParseError;
import com.android.volley.error.ServerError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.common.Constants;
public class FailResponseHandler {
    public static String handle(VolleyError error, Context context){

        String errorMessage = "";

        if(error != null) {

            if (error.networkResponse!=null&&error.networkResponse.statusCode == Constants.UN_AUTHORIZED) {
                //can do some actions
                errorMessage = context.getString(R.string.AuthFailureError);

            } else if (error instanceof NetworkError) {
                errorMessage = context.getString(R.string.NetworkError);

            } else if (error instanceof ServerError) {
                errorMessage = context.getString(R.string.ServerError);

            } else if (error instanceof AuthFailureError) {
                errorMessage = context.getString(R.string.AuthFailureError);

            } else if (error instanceof ParseError) {
                errorMessage = context.getString(R.string.ParseError);

            } else if (error instanceof NoConnectionError) {
                errorMessage = context.getString(R.string.NoConnectionError);

            } else if (error instanceof TimeoutError) {
                errorMessage = context.getString(R.string.TimeoutError);

            } else {
                errorMessage = context.getString(R.string.errVersion);
            }
        }

        return errorMessage;
    }



}
