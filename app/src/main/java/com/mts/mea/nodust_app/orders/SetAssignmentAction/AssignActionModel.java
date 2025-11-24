package com.mts.mea.nodust_app.orders.SetAssignmentAction;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.mts.mea.nodust_app.CloseCodes_Groups.ActionObject;
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
import java.util.List;
import java.util.Map;

public class AssignActionModel implements IContractAssignAction.Model {
    User CurrentUser;
    String TAG="HomeModel";
    Context mContext;
    public AssignActionModel(User user, Context context)
    {
        RequestManager.newInstance(context.getApplicationContext()).getRequestQueue();
        CurrentUser=user;
        mContext=context;
    }
    @Override
    public void setAssignmentAction(ActionObject obj, final IServerResponse iServerResponse) throws JSONException {
            String query=ServerManager.serializeObjectToString(obj);
        JSONObject body=new JSONObject(query);
        Log.i("AssAction",body.toString());
        String url= ServerURI.SetAssignmentAction();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String currentDateandTime = sdf.format(new Date());
        url+="/CHECKTIME="+currentDateandTime;

        // url+="?WorkOrder="+query;
        RequestManager.getRequestQueue().getCache().remove(url);
        final JsonObjectRequest jReqest=new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response != null) {
                    Log.i(TAG, response.toString());
                    try {
                        if(response.has("state") & response.getString("state").equalsIgnoreCase("Ok")) {
                            iServerResponse.success(response.getString("message"));
                        }
                        else if(response.has("state") & response.getString("state").equalsIgnoreCase("Fail")&response.has("message"))
                        {
                            String err="";
                            if(response.has("code")& response.getString("code").equalsIgnoreCase("-4"))
                            {
                                err=mContext.getResources().getString(R.string.erorrDB);
                            }
                            else if (response.has("code")& response.getString("code").equalsIgnoreCase("-9"))
                            {
                                err = response.getString("message");
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
                Log.i(TAG, "Volley Error " + error);
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
        jReqest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestManager.getRequestQueue().add(jReqest);


    }

    @Override
    public void SendSMS(String Telno, final String AID, final String Products_Changed, final String Products_NotChanged, final String Products_Canceled, final IServerResponse iServerResponse) {
        String url= ServerURI.sendSMS();
        url+="/Telno="+Telno;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String currentDateandTime = sdf.format(new Date());
        url+="/CHECKTIME="+currentDateandTime;
        RequestManager.getRequestQueue().getCache().remove(url);
        final JsonObjectRequest jReqest=new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response != null) {
                    Log.i(TAG, response.toString());
                    try {
                        if(response.has("state") & response.getString("state").equalsIgnoreCase("Ok")) {
                            iServerResponse.success("true");
                        }
                        else if(response.has("state") & response.getString("state").equalsIgnoreCase("Fail")&response.has("message"))
                        {
                            String err="";
                            if(response.has("code")& response.getString("code").equalsIgnoreCase("-8"))
                            {
                                err=mContext.getResources().getString(R.string.FailedSms);
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
                Log.i(TAG, "Volley Error " + error);
                error.printStackTrace();
                String err = FailResponseHandler.handle(error,mContext);
                iServerResponse.failed(err);

            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> tmp=ServerManager.getHeadersMap(CurrentUser);
                tmp.put("AID",AID);
                tmp.put("CHANGED",Products_Changed);
                tmp.put("NOTCHANGED",Products_NotChanged);
                tmp.put("CANCELED",Products_Canceled);
                Log.i("AID",AID);
                Log.i("CHANGED",Products_Changed);
                Log.i("NOTCHANGED",Products_NotChanged);
                Log.i("CANCELED",Products_Canceled);


                return tmp;
            }

        };
        jReqest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestManager.getRequestQueue().add(jReqest);

    }

    @Override
    public void AssignTask(String PilotID, final List<String> AssID, final IServerResponse iServerResponse) {

        String url= ServerURI.AssignTask();
        url+="/PilotId="+PilotID;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String currentDateandTime = sdf.format(new Date());
        url+="/CHECKTIME="+currentDateandTime;

        // url+="?WorkOrder="+query;
        RequestManager.getRequestQueue().getCache().remove(url);
        final JsonObjectRequest jReqest=new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response != null) {
                    Log.i(TAG, response.toString());
                    try {
                        if(response.has("state") & response.getString("state").equalsIgnoreCase("Ok")) {
                            iServerResponse.success("true");
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
                Log.i(TAG, "Volley Error " + error);
                error.printStackTrace();
                String err = FailResponseHandler.handle(error,mContext);
                iServerResponse.failed(err);

            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> lst=ServerManager.getHeadersMap(CurrentUser);
                String query=ServerManager.serializeObjectToString(AssID);
                lst.put("ASSIGNID",query);
                return lst;
            }

        };
        jReqest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestManager.getRequestQueue().add(jReqest);


    }
}
