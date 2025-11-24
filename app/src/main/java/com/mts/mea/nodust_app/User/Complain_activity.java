package com.mts.mea.nodust_app.User;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;

import com.mts.mea.nodust_app.Interfaces.ServerManager;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.common.Constants;

public class Complain_activity extends AppCompatActivity {

    private String lang;
    private String UserName2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complain_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getApplicationContext().getResources().getString(R.string.Complains));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setDomStorageEnabled(true);
        myWebView.getSettings().setAppCacheEnabled(false);
        myWebView.clearCache(true);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        if (sharedPreferences.contains("UserName")) {
            UserName2 = sharedPreferences.getString("UserName", "");
        }
        SharedPreferences sharedPreferences2 = getApplicationContext().getSharedPreferences(Constants.My_Prefs_language_NoDust, 0);
        if (sharedPreferences2.contains("SelectedLanguage")) {
            if (sharedPreferences2.getString("SelectedLanguage", "En").equalsIgnoreCase("English")) {

                lang = "EN";
            } else

            {
                lang = "AR";

            }
        } else {
            lang = "EN";

        }
        UserInfo userInfo = ServerManager.deSerializeStringToObject(sharedPreferences.getString("UserInfo", ""), UserInfo.class);

      int Dep_Id=userInfo.getUSERGROUP_ID();
        myWebView.loadUrl("http://gdms.nodust-eg.com/Pages_mobile/Request_form.html?UserID=" + UserName2 + "&Language=" + lang+"&DEP_ID="+Dep_Id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}