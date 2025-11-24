package com.mts.mea.nodust_app.orders.ShowDetailsOrder;

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
import com.mts.mea.nodust_app.ServerURI;
import com.mts.mea.nodust_app.User.User;
import com.mts.mea.nodust_app.VollyError.FailResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Map;

/**
 * Created by Mahmoud on 11/8/2017.
 */

public class OrderDetailsModel implements IContractOrderDetail.Model
{
    String TAG="UpdateTaskModel";
    private User CurrentUser;
    Context mContext;
    public OrderDetailsModel(Context context, User mUser)
    {
        RequestManager.newInstance(context.getApplicationContext()).getRequestQueue();

        CurrentUser=mUser;
        mContext=context;
    }

    @Override
    public void getTaskDetails(String TaskId, final IServerResponse iServerResponse) {
        String Url= ServerURI.ViewAssignmentDetails();
        Url+="?AssignmentId="+TaskId;
        RequestManager.getRequestQueue().getCache().remove(Url);
        final JsonObjectRequest jReqest=new JsonObjectRequest(Request.Method.POST, Url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response != null) {
                    Log.i(TAG, response.toString());
                    try {
                        if(response.has("state") & response.getString("state").equalsIgnoreCase("Ok")) {

                            if (response.has("currentTask") & response.getJSONObject("currentTask") != null) {

                                iServerResponse.success(response.getJSONObject("currentTask").toString());
                            }
                        }
                        else
                        {
                            String err=response.getString("message");
                            Log.i(TAG, "error " + err);
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

                return ServerManager.getHeadersMap2(CurrentUser);
            }
        };
        jReqest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestManager.getRequestQueue().add(jReqest);
    }
}
