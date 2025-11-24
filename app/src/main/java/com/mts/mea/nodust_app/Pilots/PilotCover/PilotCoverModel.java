package com.mts.mea.nodust_app.Pilots.PilotCover;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.mts.mea.nodust_app.Interfaces.IServerResponse;
import com.mts.mea.nodust_app.Interfaces.RequestManager;
import com.mts.mea.nodust_app.Interfaces.ServerManager;
import com.mts.mea.nodust_app.Pilots.Reconcilation;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.ServerURI;
import com.mts.mea.nodust_app.User.User;
import com.mts.mea.nodust_app.VollyError.FailResponseHandler;
import com.mts.mea.nodust_app.common.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
public class PilotCoverModel implements IPilotCover.Model {
    String TAG="UpdateTaskModel";
    private User CurrentUser;
    Context mContext;
    public PilotCoverModel(Context context, User mUser)
    {
        RequestManager.newInstance(context.getApplicationContext()).getRequestQueue();

        CurrentUser=mUser;
        mContext=context;
    }
    @Override
    public void AddCoverPilot(final String ProductsID, final String PilotID, final IServerResponse iServerResponse) {
        String URL= ServerURI.AddCoverToPilot();
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
                Map<String, String> headers= ServerManager.getHeadersMap(CurrentUser);
                headers.put("PILOTID",PilotID);
                headers.put("PRODUCTSID",ProductsID);
                SharedPreferences Area_name = mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0);
                headers.put("AREAID",Area_name.getString("AreaName", ""));

                Log.e("AREAID",Area_name.getString("AreaName", ""));
                Log.e("PILOTID",PilotID);
                Log.e("PRODUCTSID",ProductsID);
                return  headers;
            }
        };
        jReqest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestManager.getRequestQueue().add(jReqest);
    }

    @Override
    public void SetCoverAllPilot(final IServerResponse iServerResponse) {
        String url= ServerURI.GetCoverForAllPilots();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String currentDateandTime = sdf.format(new Date());
        url+="/CHECKTIME="+currentDateandTime;
        RequestManager.getRequestQueue().getCache().remove(url);
        JsonObjectRequest jReqest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response!=null)
                {
                    Log.i(TAG, response.toString());
                    try
                    {
                        if(response.has("state") & response.getString("state").equalsIgnoreCase("Ok")) {

                            if (response.has("Products") & response.getJSONArray("Products") != null) {

                                iServerResponse.success(response.getJSONArray("Products").toString());
                            }
                            else
                            {
                                String error=mContext.getResources().getString(R.string.NoDataFound);
                                iServerResponse.failed(error);
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
                    }
                    catch (JSONException ex)
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
                String err = FailResponseHandler.handle(error,mContext);
                iServerResponse.failed(err);            }
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
    public void SetReconcilationRequests(final IServerResponse iServerResponse) {
        String url= ServerURI.GetReconciliationRequest();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String currentDateandTime = sdf.format(new Date());
        url+="/CHECKTIME="+currentDateandTime;
        RequestManager.getRequestQueue().getCache().remove(url);
        JsonObjectRequest jReqest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response!=null)
                {
                    Log.i(TAG, response.toString());
                    try
                    {
                        if(response.has("state") & response.getString("state").equalsIgnoreCase("Ok")) {

                            if (response.has("Products") & response.getJSONArray("Products") != null) {

                                iServerResponse.success(response.getJSONArray("Products").toString());
                            }
                            else
                            {
                                String error=mContext.getResources().getString(R.string.NoDataFound);
                                iServerResponse.failed(error);
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
                    }
                    catch (JSONException ex)
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
                String err = FailResponseHandler.handle(error,mContext);
                iServerResponse.failed(err);            }
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
    public void SetCoverPilot(final IServerResponse iServerResponse) {
        String url= ServerURI.GetCoverForPilot();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String currentDateandTime = sdf.format(new Date());
        url+="/CHECKTIME="+currentDateandTime;
        RequestManager.getRequestQueue().getCache().remove(url);
        JsonObjectRequest jReqest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response!=null)
                {
                    Log.i(TAG, response.toString());
                    try
                    {
                        if(response.has("state") & response.getString("state").equalsIgnoreCase("Ok")) {

                            if (response.has("Products") & response.getJSONArray("Products") != null) {

                                iServerResponse.success(response.getJSONArray("Products").toString());
                            }
                            else
                            {
                                String error=mContext.getResources().getString(R.string.NoDataFound);
                                iServerResponse.failed(error);
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
                    }
                    catch (JSONException ex)
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
                String err = FailResponseHandler.handle(error,mContext);
                iServerResponse.failed(err);            }
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
    public void CheckProductsPilot(final IServerResponse iServerResponse) {

        String URL= ServerURI.CheckCoverPilotProduct();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String currentDateandTime = sdf.format(new Date());
        URL+="/CHECKTIME="+currentDateandTime;
        RequestManager.getRequestQueue().getCache().remove(URL);
        JsonObjectRequest jReqest=new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response!=null)
                {
                    Log.i(TAG, response.toString());
                    try
                    {
                        if(response.has("state") & response.getString("state").equalsIgnoreCase("Ok")) {

                            if (response.has("Products") & response.getJSONArray("Products") != null) {

                                iServerResponse.success(response.getJSONArray("Products").toString());
                            }
                            else
                            {
                                String error=mContext.getResources().getString(R.string.NoDataFound);
                                iServerResponse.failed(error);
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
                    }
                    catch (JSONException ex)
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
                String err = FailResponseHandler.handle(error,mContext);
                iServerResponse.failed(err);            }
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
    public void PilotAccept(final String AreaID, final IServerResponse iServerResponse) {
        String URL= ServerURI.PilotAcceptCover();
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

                Map<String, String> Headers= ServerManager.getHeadersMap(CurrentUser);
                Headers.put("AREAID",AreaID);
                return Headers;
            }
        };
        jReqest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestManager.getRequestQueue().add(jReqest);
    }

    @Override
    public void PilotRejectCover(final String AreaID, final IServerResponse iServerResponse) {
        String URL= ServerURI.PilotRejectCover();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String currentDateandTime = sdf.format(new Date());
        URL+="/CHECKTIME="+currentDateandTime;
        RequestManager.getRequestQueue().getCache().remove(URL);
        final JsonObjectRequest jReqest=new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response!=null)
                {
                    Log.i("Pilotreject",response.toString());
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

                Map<String,String> headers= ServerManager.getHeadersMap(CurrentUser);
                headers.put("AREAID",AreaID);
                return  headers;
            }
        };
        jReqest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestManager.getRequestQueue().add(jReqest);

    }

    @Override
    public void PilotReconcilation(final List<Pilot_Cover> products, final String Assign_ID, final String Pilot_Cash, final String ExpCach, final IServerResponse iServerResponse) throws JSONException {
        String URL= ServerURI.PilotReconcilation();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String query=ServerManager.serializeObjectToString(products);
        Gson gson = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(products).getAsJsonArray();

        JSONObject body=new JSONObject();
        body.put("PRODUCTS",(Object) myCustomArray);
       // JSONObject body=new JSONObject(query);
        Log.i("pilotRec",body.toString());
        final String currentDateandTime = sdf.format(new Date());
        URL+="/CHECKTIME="+currentDateandTime;
        RequestManager.getRequestQueue().getCache().remove(URL);

        final JsonObjectRequest jReqest=new JsonObjectRequest(Request.Method.POST, URL, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response!=null)
                {
                    Log.i("Pilotreject",response.toString());
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

                Map<String,String>headers= ServerManager.getHeadersMap(CurrentUser);
                headers.put("ASSIGNID",Assign_ID);
                headers.put("EXPCASH",ExpCach);
                headers.put("PILOTCASH",Pilot_Cash);
                headers.put("DRIVER",products.get(0).getDRIVER_ID());
                headers.put("PILOT",products.get(0).getPilot_ID());
                headers.put("AREA",products.get(0).getArea_ID());

                Log.i("ASSIGN_ID",Assign_ID);
                Log.i("EXPCASH",ExpCach);
                Log.i("PILOTCASH",Pilot_Cash);

                return  headers;
            }
        };
        jReqest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestManager.getRequestQueue().add(jReqest);

    }

    @Override
    public void DriverReconcilation(List<Reconcilation> products, final boolean Accept, final String Assign_ID, final String DRIVER_Cash, final String ExpCach, final IServerResponse iServerResponse) throws JSONException {
        String URL= ServerURI.DriverReconcilation();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String query=ServerManager.serializeObjectToString(products);
        Gson gson = new GsonBuilder().create();
        JsonArray myCustomArray = gson.toJsonTree(products).getAsJsonArray();

        JSONObject body=new JSONObject();
        body.put("PRODUCTS",(Object) myCustomArray);
        Log.i("pilotRec",body.toString());
        final String currentDateandTime = sdf.format(new Date());
        URL+="/CHECKTIME="+currentDateandTime;
        RequestManager.getRequestQueue().getCache().remove(URL);

        final JsonObjectRequest jReqest=new JsonObjectRequest(Request.Method.POST, URL, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response!=null)
                {
                    Log.i("Pilotreject",response.toString());
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
                            else if(response.has("code")& response.getString("code").equalsIgnoreCase("-3"))
                            {

                                err=mContext.getResources().getString(R.string.modifiedQTY);
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
                iServerResponse.failed(err);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> Headers=ServerManager.getHeadersMap(CurrentUser);
                Headers.put("DRIVERACCEPT", String.valueOf(Accept));
                Headers.put("ASSIGNID",Assign_ID);
                Headers.put("EXPCASH",ExpCach);
                Headers.put("DRIVERCASH",DRIVER_Cash);
                return Headers;
            }
        };
        jReqest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestManager.getRequestQueue().add(jReqest);
    }
}
