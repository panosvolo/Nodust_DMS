package com.mts.mea.nodust_app.Login;

import android.content.Context;
import android.content.pm.PackageManager;
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

public class LoginModel implements IContractLogin.Model{
    private User currentUser;
    String TAG ="LoginModel";
    Context mContext;
    public LoginModel(User user,Context context)
    {
        RequestManager.newInstance(context.getApplicationContext()).getRequestQueue();
        currentUser=user;
        mContext=context;
    }
    @Override
    public void validateAccount(final String IMIE, final IServerResponse iServerResponse) throws JSONException {
        String URL= ServerURI.getLoginURL();
         SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String currentDateandTime = sdf.format(new Date());

        android.content.pm.PackageInfo pInfo = null;
        try {
            pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        URL+="/CHECKTIME="+currentDateandTime+"/VERSION_NO="+pInfo.versionName;
        //URL+="/Password="+currentUser.getPassword();
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
                            Log.i(TAG, "Account Exists");
                            iServerResponse.success(response.getJSONObject("user").toString());
                        }
                        else if(response.has("state") & response.getString("state").equalsIgnoreCase("Fail")&response.has("message")){
                            String err="";
                            if(response.has("code")& response.getString("code").equalsIgnoreCase("-4"))
                            {
                                   err=mContext.getResources().getString(R.string.erorrDB);
                            }
                            else if(response.has("code")& response.getString("code").equalsIgnoreCase("-3"))
                            {
                               err=mContext.getResources().getString(R.string.errLogin);
                            }
                            else if(response.has("code")& response.getString("code").equalsIgnoreCase("-10"))
                            {
                                err=mContext.getResources().getString(R.string.errVersion);
                            }
                            else if(response.has("code")& response.getString("code").equalsIgnoreCase("-20"))
                            {
                                err=mContext.getResources().getString(R.string.errTablet);
                            }
                            else
                            {
                                 err = response.getString("message");
                            }
                            iServerResponse.failed(err);
                        }
                        else
                        {
                           String err=mContext.getResources().getString(R.string.erorrDB);
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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                 Map<String, String>Headers=ServerManager.getHeadersMap(currentUser);
                Headers.put("LOGINIMIE",IMIE);
                Log.i("LOGINIMIE",IMIE);
                Headers.put("PASSWORD",currentUser.getPassword());
                return Headers;
            }

         /*  @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap=new HashMap<>(1);
                hashMap.put("Password",currentUser.getPassword());
                return hashMap;
            }
*/

        };
        jReqest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestManager.getRequestQueue().add(jReqest);
    }

    @Override
    public void ChangePassword(String NewPass, String OldPass, final IServerResponse iServerResponse) {
        String URL= ServerURI.changePassword();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String currentDateandTime = sdf.format(new Date());
        URL+="/OldPassword="+OldPass+"/NewPassword="+NewPass+"/CHECKTIME="+currentDateandTime;
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
                            Log.i(TAG, "Account Exists");
                            iServerResponse.success("true");
                        }
                        else if(response.has("state") & response.getString("state").equalsIgnoreCase("Fail")&response.has("message")){
                            if(response.has("code")&response.getString("code").equalsIgnoreCase("-7")) {
                                String err = mContext.getApplicationContext().getResources().getString(R.string.errEnterCorrectPassord);
                                iServerResponse.failed(err);
                            }
                            else
                            {
                                iServerResponse.failed(response.getString("message"));

                            }
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
                Log.i(TAG, "Volley Error " + error);
                error.printStackTrace();
                String err = FailResponseHandler.handle(error,mContext);
                iServerResponse.failed(err);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                return ServerManager.getHeadersMap(currentUser);
            }

        };
        jReqest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestManager.getRequestQueue().add(jReqest);
    }


}
