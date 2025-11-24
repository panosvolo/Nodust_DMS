package com.mts.mea.nodust_app.User.UserShift;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.mts.mea.nodust_app.Evalution.KPI;
import com.mts.mea.nodust_app.Interfaces.IServerResponse;
import com.mts.mea.nodust_app.Interfaces.RequestManager;
import com.mts.mea.nodust_app.Interfaces.ServerManager;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.ServerURI;
import com.mts.mea.nodust_app.User.User;
import com.mts.mea.nodust_app.VollyError.FailResponseHandler;
import com.mts.mea.nodust_app.orders.GetOrders.IContractGetOrders;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserShiftModel implements IContractUserShif.Model {
    User CurrentUser;
    IContractGetOrders.Presenter mPresenter;
    Context mContext;
    public UserShiftModel(User user, Context context)
    {
        RequestManager.newInstance(context.getApplicationContext()).getRequestQueue();
        CurrentUser=user;
        mContext=context;}
    @Override
    public void StartShift(final IServerResponse iServerResponse) {
        String URL= ServerURI.StartShift();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String currentDateandTime = sdf.format(new Date());
        URL+="/CHECKTIME="+currentDateandTime;
        RequestManager.getRequestQueue().getCache().remove(URL);
        final JsonObjectRequest jReqest=new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response!=null)
                {
                    Log.i("response",response.toString());
                    try {
                        if(response.has("state") & response.getString("state").equalsIgnoreCase("Ok"))
                        {
                            iServerResponse.success(response.getString("shiftId"));
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
    public void EndShift(final IServerResponse iServerResponse,String ShiftID) {
        String URL= ServerURI.EndShift();
        URL+="/ShiftId="+ShiftID;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String currentDateandTime = sdf.format(new Date());
        URL+="/CHECKTIME="+currentDateandTime;
        RequestManager.getRequestQueue().getCache().remove(URL);
        final JsonObjectRequest jReqest=new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response!=null)
                {
                    Log.i("response",response.toString());
                    try {
                        if(response.has("state") & response.getString("state").equalsIgnoreCase("Ok"))
                        {
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
    public void SetAttendance(final IServerResponse iServerResponse,String PilotID) {
        String URL= ServerURI.SetAttendance();
        URL+="/ASSIGN_PILOT_ID="+PilotID;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String currentDateandTime = sdf.format(new Date());
        URL+="/CHECKTIME="+currentDateandTime;
        RequestManager.getRequestQueue().getCache().remove(URL);
        final JsonObjectRequest jReqest=new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response!=null)
                {
                    Log.i("response",response.toString());
                    try {
                        if(response.has("state") & response.getString("state").equalsIgnoreCase("Ok"))
                        {
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
    public void SetEvalution(final IServerResponse iServerResponse, final List<KPI> Evalution, final String Notes) throws JSONException {
        String URL= ServerURI.SetEvalution();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String currentDateandTime = sdf.format(new Date());
        URL+="/CHECKTIME="+currentDateandTime;
       // URL+="/PilotId="+PilotID;
        String query=ServerManager.serializeObjectToString(Evalution.get(0));
        Gson gson = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(Evalution).getAsJsonArray();

        JSONObject body=new JSONObject();
        body.put("EVAlution",(Object) myCustomArray);
       // JSONObject body=new JSONObject();
      //body.put("EValution",query);
        final JsonObjectRequest jReqest=new JsonObjectRequest(Request.Method.POST, URL, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response!=null)
                {
                    Log.i("response",response.toString());
                    try {
                        if(response.has("state") & response.getString("state").equalsIgnoreCase("Ok"))
                        {
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
                Map<String, String> tmp=ServerManager.getHeadersMap(CurrentUser);
                String query=ServerManager.serializeObjectToString(Evalution);
               //  tmp.put("EVALUTION",query);
                tmp.put("NOTES",Notes);
              //  tmp.put("RATING",String.valueOf(Evalution));
                return tmp;
            }
        };
        jReqest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        String tmp=jReqest.toString();
        Log.i("EvalTest",tmp);
        RequestManager.getRequestQueue().add(jReqest);
    }

    @Override
    public void SetReturnTime(final IServerResponse iServerResponse, String PilotID) {
        String URL= ServerURI.SetReturnTimePilot();
        URL+="/ASSIGN_PILOT_ID="+PilotID;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String currentDateandTime = sdf.format(new Date());
        URL+="/CHECKTIME="+currentDateandTime;
        RequestManager.getRequestQueue().getCache().remove(URL);
        final JsonObjectRequest jReqest=new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response!=null)
                {
                    Log.i("response",response.toString());
                    try {
                        if(response.has("state") & response.getString("state").equalsIgnoreCase("Ok"))
                        {
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
    public void SetOutputDayDriver(final IServerResponse iServerResponse, final String TotalPrice, final String TotalRecNo, String PilotID) {
        String URL= ServerURI.TodayOutputDriver();
        URL+="/ASSIGN_PILOT_ID="+PilotID;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String currentDateandTime = sdf.format(new Date());
        URL+="/CHECKTIME="+currentDateandTime;
        RequestManager.getRequestQueue().getCache().remove(URL);
        final JsonObjectRequest jReqest=new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response!=null)
                {
                    Log.i("response",response.toString());
                    try {
                        if(response.has("state") & response.getString("state").equalsIgnoreCase("Ok"))
                        {
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

                 Map<String, String> headers=ServerManager.getHeadersMap(CurrentUser);
               /* headers.put("RECIEPTNO",TotalRecNo);
                headers.put("RECIEPTAMOUNT",TotalPrice);*/
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
    public void SetOutputDayPilot(final IServerResponse iServerResponse, final String TotalPrice, final String TotalRecNo, String PilotID) {
        String URL= ServerURI.TodayOutputPilot();
        URL+="/ASSIGN_PILOT_ID="+PilotID;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String currentDateandTime = sdf.format(new Date());
        URL+="/CHECKTIME="+currentDateandTime;
        RequestManager.getRequestQueue().getCache().remove(URL);
        final JsonObjectRequest jReqest=new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response!=null)
                {
                    Log.i("response",response.toString());
                    try {
                        if(response.has("state") & response.getString("state").equalsIgnoreCase("Ok"))
                        {
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

                Map<String, String> headers=ServerManager.getHeadersMap(CurrentUser);
                headers.put("RECIEPTNO",TotalRecNo);
                headers.put("RECIEPTAMOUNT",TotalPrice);
                return headers;
            }
        };
        jReqest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestManager.getRequestQueue().add(jReqest);
    }
}
