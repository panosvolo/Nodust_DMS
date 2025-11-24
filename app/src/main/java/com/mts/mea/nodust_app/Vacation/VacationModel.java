package com.mts.mea.nodust_app.Vacation;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.mts.mea.nodust_app.Interfaces.IServerResponse;
import com.mts.mea.nodust_app.Interfaces.RequestManager;
import com.mts.mea.nodust_app.Interfaces.ServerManager;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.ServerURI;
import com.mts.mea.nodust_app.User.User;
import com.mts.mea.nodust_app.VollyError.FailResponseHandler;
import com.mts.mea.nodust_app.orders.GetOrders.IContractGetOrders;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Mahmoud on 4/25/2018.
 */

public class VacationModel implements IContractVacation.Model {
    IContractGetOrders.Presenter mPresenter;
    Context mContext;
    User CurrentUser;
    public VacationModel(User user, Context context)
    {
        RequestManager.newInstance(context.getApplicationContext()).getRequestQueue();
        CurrentUser=user;
        mContext=context;
    }
    @Override
    public void SaveVacation(final IServerResponse iServerResponse, VacationRequest obj) throws JSONException {
        String URL= ServerURI.SaveVacation();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String currentDateandTime = sdf.format(new Date());
        URL+="/CHECKTIME="+currentDateandTime;
        String query=ServerManager.serializeObjectToString(obj);
        JSONObject body=new JSONObject(query);
        RequestManager.getRequestQueue().getCache().remove(URL);
        Log.e("VacationRequest",query);
        final JsonObjectRequest jReqest=new JsonObjectRequest(Request.Method.POST, URL, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response!=null)
                {
                    Log.i("response",response.toString());
                    try {
                        if(response.has("state") & response.getString("state").equalsIgnoreCase("Ok"))
                        {
                            iServerResponse.success(response.getString("user"));
                        }
                        else if(response.has("state") & response.getString("state").equalsIgnoreCase("Fail")&response.has("message"))
                        {
                            String err="";
                            if(response.has("code")& response.getString("code").equalsIgnoreCase("-4"))
                            {
                                err=mContext.getResources().getString(R.string.erorrDB);
                            }
                            else
                            {
                                err = response.getString("message");
                            }
                            iServerResponse.failed(err);
                        }

                    }
                    catch (JSONException ex){
                        ex.printStackTrace();} catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    iServerResponse.failed("Error");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("UserShift", "Volley Error " + error);
                error.printStackTrace();
                String err = FailResponseHandler.handle(error,mContext);
                iServerResponse.failed(err);            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return ServerManager.getHeadersMap(CurrentUser);
            }
        };
        jReqest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestManager.getRequestQueue().add(jReqest);
    }

    @Override
    public void GetVacations(final IServerResponse iServerResponse) throws JSONException {
        String URL= ServerURI.GetVacations();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String currentDateandTime = sdf.format(new Date());
        URL+="/CHECKTIME="+currentDateandTime;
        RequestManager.getRequestQueue().getCache().remove(URL);
        final JsonObjectRequest jReqest=new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response!=null)
                {
                    Log.i("response",response.toString());
                    try {
                        if(response.has("state") & response.getString("state").equalsIgnoreCase("Ok"))
                        {
                            iServerResponse.success(response.getString("tasks"));
                        }
                        else if(response.has("state") & response.getString("state").equalsIgnoreCase("Fail")&response.has("message"))
                        {
                            String err="";
                            if(response.has("code")& response.getString("code").equalsIgnoreCase("-4"))
                            {
                                err=mContext.getResources().getString(R.string.erorrDB);
                            }
                            else
                            {
                                err = response.getString("message");
                            }
                            iServerResponse.failed(err);
                        }

                    }
                    catch (JSONException ex){
                        ex.printStackTrace();} catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    iServerResponse.failed("Error");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("UserShift", "Volley Error " + error);
                error.printStackTrace();
                String err = FailResponseHandler.handle(error,mContext);
                iServerResponse.failed(err);            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return ServerManager.getHeadersMap(CurrentUser);
            }
        };
        jReqest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestManager.getRequestQueue().add(jReqest);
    }

    @Override
    public void GetVacationReasons(final IServerResponse iServerResponse) throws JSONException {
        String URL= ServerURI.GetVacationReasons();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String currentDateandTime = sdf.format(new Date());
        URL+="/CHECKTIME="+currentDateandTime;
        RequestManager.getRequestQueue().getCache().remove(URL);
        final JsonObjectRequest jReqest=new JsonObjectRequest(Request.Method.POST, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response!=null)
                {
                    Log.i("response",response.toString());
                    try {
                        if(response.has("state") & response.getString("state").equalsIgnoreCase("Ok"))
                        {
                            iServerResponse.success(response.getString("tasks"));
                        }
                        else if(response.has("state") & response.getString("state").equalsIgnoreCase("Fail")&response.has("message"))
                        {
                            String err="";
                            if(response.has("code")& response.getString("code").equalsIgnoreCase("-4"))
                            {
                                err=mContext.getResources().getString(R.string.erorrDB);
                            }
                            else
                            {
                                err = response.getString("message");
                            }
                            iServerResponse.failed(err);
                        }

                    }
                    catch (JSONException ex){
                        ex.printStackTrace();} catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    iServerResponse.failed("Error");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("UserShift", "Volley Error " + error);
                error.printStackTrace();
                String err = FailResponseHandler.handle(error,mContext);
                iServerResponse.failed(err);            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return ServerManager.getHeadersMap(CurrentUser);
            }
        };
        jReqest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestManager.getRequestQueue().add(jReqest);
    }
}
