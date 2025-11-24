package com.mts.mea.nodust_app.orders.GetOrders;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


public class GetOrdersModel implements IContractGetOrders.Model {
    User CurrentUser;
    String TAG="HomeModel";
    IContractGetOrders.Presenter mPresenter;
    Context mContext;
    public GetOrdersModel(User user, Context context)
    {
        RequestManager.newInstance(context.getApplicationContext()).getRequestQueue();
        CurrentUser=user;
    mContext=context;}
    @Override
    public void getTasks(final IServerResponse iServerResponse) {
        String url= ServerURI.ViewAssignedTasks();
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

                            if (response.has("tasks") & response.getJSONArray("tasks") != null) {

                                iServerResponse.success(response.getJSONArray("tasks").toString());
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
    public void getTasksNotAssign(final IServerResponse iServerResponse) {
        String url= ServerURI.GetTasksNotAssig();
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

                            if (response.has("tasks") & response.getJSONArray("tasks") != null) {

                                iServerResponse.success(response.getJSONArray("tasks").toString());
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
    public void getProducts(final IServerResponse iServerResponse) {
        String url= ServerURI.ViewProducts();
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
    public void getCoverProducts(final IServerResponse iServerResponse) {
        String url= ServerURI.ViewCoverProducts();
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
    public void getDriverProducts(final IServerResponse iServerResponse) {
        String url= ServerURI.ViewProductsDriver();
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
    public void getCoverPilots(final IServerResponse iServerResponse) {
        String url= ServerURI.PilotCover();
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

                            if (response.has("Pilots") & response.getJSONArray("Pilots") != null) {

                                iServerResponse.success(response.getJSONArray("Pilots").toString());
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
    public void getAmount(final IServerResponse iServerResponse) {
        String url= ServerURI.getTotalAmount();
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

                            if (response.has("Amount") & response.getString("Amount") != null) {

                                iServerResponse.success(response.getString("Amount"));
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
    public void getKPI(final IServerResponse iServerResponse) {
        String url= ServerURI.GetKPI();
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

                            if (response.has("KPI_list") & response.getJSONArray("KPI_list") != null) {

                                iServerResponse.success(response.getJSONArray("KPI_list").toString());
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
    public void getProductReference(final IServerResponse iServerResponse) {
        String url= ServerURI.GetProductsReference();
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
    public void getPackageInfo(final IServerResponse iServerResponse) {
        String url= ServerURI.GetPackagesDetails();
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
    public void GetCollectionPilot(final IServerResponse iServerResponse) {
        String url= ServerURI.GetCollectionPilot();
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
    public void GetCollectionDriver(final IServerResponse iServerResponse) {
        String url= ServerURI.GetCollectionDriver();
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


}

