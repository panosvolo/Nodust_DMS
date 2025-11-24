package com.mts.mea.nodust_app.User;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mts.mea.nodust_app.Application.ConnectivityUtil;
import com.mts.mea.nodust_app.Interfaces.ServerManager;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.common.Constants;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class User_Profile extends AppCompatActivity {

    private UserInfo userInfo;
    String userName = "";
    TextView tv_UserName,tv_Name;
   // de.hdodenhof.circleimageview.CircleImageView img_user;
    ImageView img_user;
    private ProgressDialog pbDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__profile);
        INTIUI();
    }

    private void INTIUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getApplicationContext().getResources().getString(R.string.Profile));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        img_user=(ImageView)findViewById(R.id.profile_image);
        tv_Name = (TextView) findViewById(R.id.tv_Name);
        tv_UserName = (TextView) findViewById(R.id.tv_userName);

        SharedPreferences sharedPreferences2 = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.My_Prefs_language_NoDust, 0);

        userInfo = ServerManager.deSerializeStringToObject(sharedPreferences2.getString("UserInfo", ""), UserInfo.class);
        userName = "";
        if (sharedPreferences2.contains("UserName")) {
            userName = sharedPreferences2.getString("UserName", "");
        }
        if (sharedPreferences.contains("SelectedLanguage")) {
            if (sharedPreferences.getString("SelectedLanguage", "En").equalsIgnoreCase("English")) {

                tv_Name.setText(userInfo.getNameEn() );
            } else {
                tv_Name.setText(userInfo.getNameAr() );
            }
        } else {
            tv_Name.setText(userInfo.getNameEn() );
        }
        tv_UserName.setText(userName);
        new LoadImagefromUrl( ).execute( img_user, "http://gdms.nodust-eg.com/Workers Images/user_" +userName+".png");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
    private class LoadImagefromUrl extends AsyncTask< Object, Void, Bitmap> {
        ImageView  ivPreview = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ShowLoading();
        }

        @Override
        protected Bitmap doInBackground( Object... params ) {
            this.ivPreview = (ImageView) params[0];
            String url = (String) params[1];
            System.out.println(url);
            return loadBitmap( url );
        }

        @Override
        protected void onPostExecute( Bitmap result ) {
            super.onPostExecute( result );
            if (ConnectivityUtil.isOnline(User_Profile.this)) {
                EndLoading();
                if(result!=null)
                ivPreview.setImageBitmap(result);
            }
            else
            {
                EndLoading();
                Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.no_internet),Toast.LENGTH_SHORT).show();
            }

        }
    }
    public void ShowLoading() {
        pbDailog = new ProgressDialog(User_Profile.this);
        pbDailog.setMessage(getApplicationContext().getResources().getString(R.string.Loading));
        pbDailog.setCancelable(false);
        pbDailog.show();
    }

    public void EndLoading() {
        if(pbDailog!=null && pbDailog.isShowing())
            pbDailog.dismiss();

    }

    public Bitmap loadBitmap( String url ) {
        URL newurl = null;
        Bitmap bitmap = null;
        if (ConnectivityUtil.isOnline(User_Profile.this)) {
            try {
                newurl = new URL(url);
                bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
            return bitmap;
        }
        else
        {

            return null;
        }

    }

}
