package com.mts.mea.nodust_app.OFFLINE;

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
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.User.User;
import com.mts.mea.nodust_app.VollyError.FailResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mahmoud on 9/5/2018.
 */

public class OfflineModel implements IContractOffline.Model {
    User CurrentUser;
    String TAG="HomeModel";
    Context mContext;
    public OfflineModel(User user, Context context)
    {
        RequestManager.newInstance(context.getApplicationContext()).getRequestQueue();
        CurrentUser=user;
        mContext=context;
    }
    @Override
    public void setAssignmentAction(final OFFLINE obj, final IServerResponse iServerResponse) throws JSONException {
        String query= obj.getBODY();
                //ServerManager.serializeObjectToString(obj.getBODY());
        Log.d("AssAction",query);
        JSONObject body=new JSONObject(query);
        String url= obj.getURL();
        Log.d("testAction",url);

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

                HashMap<String,String> headers=new HashMap<String, String>(5);
                headers.put("UserName", obj.getUSERNAME());
                headers.put("Longitude",obj.getLONGITUDE());
                headers.put("Latitude",obj.getLATITUDE());
                headers.put("Description",obj.getDESCRIPTION());
                return headers;
            }

        };
        jReqest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestManager.getRequestQueue().add(jReqest);


    }

    @Override
    public void SendSMS(final OFFLINE obj, final IServerResponse iServerResponse) {
        String url=obj.getURL();
        Log.d("testsms",url);
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
                HashMap<String,String> headers=new HashMap<String, String>(5);
                headers.put("UserName", obj.getUSERNAME());
                headers.put("Longitude",obj.getLONGITUDE());
                headers.put("Latitude",obj.getLATITUDE());
                headers.put("Description",obj.getDESCRIPTION());
                Map<String, String> tmp=headers;
                tmp.put("AID",obj.getAID());
                tmp.put("CHANGED"," ");
                tmp.put("NOTCHANGED"," ");
                tmp.put("CANCELED"," ");
                return tmp;
            }

        };
        jReqest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestManager.getRequestQueue().add(jReqest);

    }
}
