package com.mts.mea.nodust_app.CloseCodes_Groups;

import android.content.Context;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by Mahmoud on 11/9/2017.
 */

public class CloseCodeModel implements IContractCloseCode_Groups.Model{
    User CurrentUser;
    Context mContext;
    public CloseCodeModel(User user, Context context)
    {
        RequestManager.newInstance(context.getApplicationContext()).getRequestQueue();
        CurrentUser=user;
        mContext=context;}
    @Override
    public void GetCloseCodes(final IServerResponse iServerResponse, int GroupID) {
        String Url= ServerURI.GetCloseCodes();
        Url+="/GroupID="+GroupID;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String currentDateandTime = sdf.format(new Date());
        Url+="/CHECKTIME="+currentDateandTime;
        RequestManager.getRequestQueue().getCache().remove(Url);
        final JsonObjectRequest jReqest=new JsonObjectRequest(Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response != null) {
                    try {
                        if(response.has("state") & response.getString("state").equalsIgnoreCase("Ok")) {

                            if (response.has("codes") & response.getJSONArray("codes") != null) {

                                iServerResponse.success(response.getJSONArray("codes").toString());
                            }
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

                    }catch (JSONException ex)
                    {
                        ex.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    iServerResponse.failed("error");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                String err = FailResponseHandler.handle(error,mContext);
                iServerResponse.failed(err);

            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return ServerManager.getHeadersMap(CurrentUser);
            }
        };

        RequestManager.getRequestQueue().add(jReqest);
    }


    @Override
    public void GetcloseCodeGroups(final IServerResponse iServerResponse) {
        String Url= ServerURI.GetCloseCodesGroups();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String currentDateandTime = sdf.format(new Date());
        Url+="/CHECKTIME="+currentDateandTime;
        RequestManager.getRequestQueue().getCache().remove(Url);
        final JsonObjectRequest jReqest=new JsonObjectRequest(Request.Method.GET, Url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response != null) {
                    try {
                        if(response.has("state") & response.getString("state").equalsIgnoreCase("Ok")) {

                            if (response.has("cCgroups") & response.getJSONArray("cCgroups") != null) {

                                iServerResponse.success(response.getJSONArray("cCgroups").toString());
                            }
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

                    }catch (JSONException ex)
                    {
                        ex.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    iServerResponse.failed("error");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                String err = FailResponseHandler.handle(error,mContext);
                iServerResponse.failed(err);

            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return ServerManager.getHeadersMap(CurrentUser);
            }
        };

        RequestManager.getRequestQueue().add(jReqest);
    }
}
