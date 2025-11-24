package com.mts.mea.nodust_app.Login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mts.mea.nodust_app.BackgroundServices.GetCurrentLocaion;
import com.mts.mea.nodust_app.CloseCodes_Groups.CloseCode;
import com.mts.mea.nodust_app.CloseCodes_Groups.CloseCodePresenter;
import com.mts.mea.nodust_app.CloseCodes_Groups.CloseCodesGroup;
import com.mts.mea.nodust_app.CloseCodes_Groups.IContractCloseCode_Groups;
import com.mts.mea.nodust_app.Interfaces.ServerManager;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.User.User;
import com.mts.mea.nodust_app.User.UserInfo;
import com.mts.mea.nodust_app.common.Constants;
import com.mts.mea.nodust_app.common.DataBaseHelper;
import com.mts.mea.nodust_app.common.GetAddress;

import org.json.JSONException;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.mts.mea.nodust_app.common.GPS_Alert.displayPromptForEnablingGPS;

public class loginActivity extends AppCompatActivity implements IContractLogin.View,IContractCloseCode_Groups.View {
final static int MY_PERMISSIONS_REQUEST_LOCATION=1;
    final static int MY_PERMISSIONS_REQUEST_LOCATION_READSTATE=2;
    final static int MY_PERMISSIONS_REQUEST_READSTATE=3;
    EditText input_userName,input_password;
    IContractLogin.Presenter mPresenter;
    IContractCloseCode_Groups.Presenter closecodePresenter;
    android.support.v7.widget.AppCompatButton btn_login;
   UserInfo mUserInfo;
    Locale mlocal;
    private ProgressDialog pbDailog;
   DataBaseHelper DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.My_Prefs_language_NoDust, 0);
        if (sharedPreferences.contains("SelectedLanguage")) {
            if(sharedPreferences.getString("SelectedLanguage","En").equalsIgnoreCase("English"))
            {
                mlocal = Locale.ENGLISH;
            }
            else
            {
                mlocal=new Locale("ar");
            }
            Resources resources = getApplicationContext().getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            Configuration config = resources.getConfiguration();
            config.setLocale(mlocal);
            resources.updateConfiguration(config,dm);
        }
        else
        {
            mlocal = Locale.ENGLISH;
            Resources resources = getApplicationContext().getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            Configuration config = resources.getConfiguration();
            config.setLocale(mlocal);
            resources.updateConfiguration(config,dm);
        }
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckPermission();

    }

    private void CheckPermission() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_LOCATION_READSTATE);
        } else if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);

        }
        else if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READSTATE);
        }
        else
        {
            initUI();
           /* if(ConnectivityUtil.isOnline(getApplicationContext())) {
                initUI();
            }
            else
            {
                GPS_Alert.showAlertDialog(loginActivity.this,getApplicationContext().getResources().getString(R.string.InternetConnection),getApplicationContext().getResources().getString(R.string.NoConnectionError));
              // Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.NoConnectionError),Toast.LENGTH_SHORT).show();
            }*/
        }
    }

    private void initUI() {
        // start BackGroundServices
        Intent i=new Intent(this, GetCurrentLocaion.class);
        startService(i);
        DB=new DataBaseHelper(loginActivity.this);
        TextView tv_version=(TextView)findViewById(R.id.tv_version);
        android.content.pm.PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tv_version.setText(getApplicationContext().getResources().getString(R.string.VersionNo)+String.valueOf(pInfo.versionName));
        // save Currentdate

      /*  DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        Log.d("HigScreen",String.valueOf(height));
        Log.d("WidtScreen",String.valueOf(width));*/
        input_password=(EditText)findViewById(R.id.input_password);
        input_userName=(EditText)findViewById(R.id.input_email);
        btn_login=(android.support.v7.widget.AppCompatButton)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get imie
              if(input_userName.getText().toString().isEmpty()||input_password.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),R.string.errLogin,Toast.LENGTH_SHORT).show();
                }
                else {
                  TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String IMIE = String.valueOf(tel.getDeviceId());
                    Location CurrentLoc = GetCurrentLocaion.CurrentLoc;
                    if (CurrentLoc == null) {
                        displayPromptForEnablingGPS(loginActivity.this);

                    } else {
                        String Address = GetAddress.GetAddress(getApplicationContext(), CurrentLoc);
                        User user = new User(input_userName.getText().toString(), input_password.getText().toString(), CurrentLoc, Address);
                        mPresenter = new LoginPresenter(getApplicationContext(), user, loginActivity.this);
                        closecodePresenter = new CloseCodePresenter(loginActivity.this, getApplicationContext(), user);
                        try {
                            mPresenter.isExistUser(IMIE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }



                }
                 // Toast.makeText(getApplicationContext(),DB.CalculateDays(),Toast.LENGTH_SHORT).show();
            }
        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION_READSTATE :

                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                    {
                       // initUI();
                        Toast.makeText(getApplicationContext(),"Permission denied",Toast.LENGTH_SHORT).show();

                    }
                break;
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getApplicationContext(),"Permission denied",Toast.LENGTH_SHORT).show();
                }
                break;
            case MY_PERMISSIONS_REQUEST_READSTATE:
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(getApplicationContext(),"Permission denied",Toast.LENGTH_SHORT).show();
                }
                break;
                }
                return;


        }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(this,GetCurrentLocaion.class);
        GetCurrentLocaion.FlagGPS=false;
        stopService(i);
        SharedPreferences settings = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        settings.edit().remove("UserName").commit();
        settings.edit().remove("UserInfo").commit();
        finish();
        super.onBackPressed();
    }

    @Override
    public void showError(String errMsg) {
        Toast.makeText(this, errMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goToHome(UserInfo userInfo) throws JSONException {
        mUserInfo=userInfo;
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        SharedPreferences  name_shared=getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString("UserName", input_userName.getText().toString());
        sharedPreferencesEditor.putString("UserInfo",ServerManager.serializeObjectToString(mUserInfo));
        sharedPreferencesEditor.apply();
        if(mUserInfo.getUSERGROUP_ID()!=1) {
            closecodePresenter.GetCloseCodeGroups();
        }
        Calendar c=Calendar.getInstance();
        String CurrentDate=c.get(Calendar.DATE)+"/"+c.get(Calendar.MONTH)+"/"+(c.get(Calendar.YEAR));
         sharedPreferences = getApplicationContext().getSharedPreferences(Constants.My_Prefs_language_NoDust, 0);
        if(sharedPreferences.contains("loginToday"))
        {

            if(name_shared.contains("AreaName"))
            {
                name_shared.edit().remove("AreaName").commit();
            }

            if(!sharedPreferences.getString("loginToday","").equalsIgnoreCase(CurrentDate))
            {
                if(name_shared.contains("AreaName"))
                {
                    name_shared.edit().remove("AreaName").commit();
                }
                sharedPreferences.edit().putString("loginToday",CurrentDate).commit();
                DB.DeletAll(null);
                // DB.UpdateAllData(null,true);

            }
            else
            {

                      DB.UpdateAllData(null,true);
                       /* Intent i=new Intent(this, HomeWithMenuActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);*/
               // DB.DeletAll(null);
            }
        }
        else
        {
            sharedPreferences.edit().putString("loginToday",CurrentDate).commit();
            if(name_shared.contains("AreaName"))
            {
                name_shared.edit().remove("AreaName").commit();
            }
            DB.DeletAll(null);
        }

    }

    @Override
    public void ShowLoading() {
        pbDailog = new ProgressDialog(loginActivity.this);
        pbDailog.setMessage("جاري التحميل .....");
        pbDailog.setCancelable(false);
        pbDailog.show();

    }

    @Override
    public void StopLoading() {
    pbDailog.dismiss();
    }

    @Override
    public void SuccessChangePassword() {

    }

    @Override
    public void SaveCloseCode(int GId,List<CloseCode> lst_closeCode) {
        SharedPreferences sharedPreferences=null;
        String Key="";
        if(lst_closeCode!=null&&lst_closeCode.size()>0) {
            sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_closeCode, 0);
            if (GId == 2) { // deliver
                Key = Constants.Group1_deliver;
            } else if (GId == 3) { // not deliver
                Key = Constants.Group2_notDeliver;
            } else if (GId == 4) { //partial
                Key = Constants.Group3_partial;
            }else if (GId == 5) { //partial
                Key = Constants.Group4_Cancelation;
            }
            else {
                sharedPreferences = getApplicationContext().getSharedPreferences(Constants.GroupDefault, 0);
                Key = Constants.GroupDefault;
            }
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            sharedPreferencesEditor.putString(Key,String.valueOf(GId)+"*"+ ServerManager.serializeObjectToString(lst_closeCode));
            sharedPreferencesEditor.apply();
            sharedPreferencesEditor.commit();

            Log.d("CloseCodes", sharedPreferences.getString(Key,ServerManager.serializeObjectToString(lst_closeCode)));
        }
    }

    @Override
    public void SaveCloseCodeGroups(List<CloseCodesGroup> lst_Groups) {
        if(lst_Groups!=null)
        {
            Set<String> set = new HashSet<String>();
           // set.addAll(lst_Groups);
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_closeCodeGroup, 0);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            sharedPreferencesEditor.putString("CloseCodeGroups", ServerManager.serializeObjectToString(lst_Groups));
            Log.d("CloseCodeGroups",ServerManager.serializeObjectToString(lst_Groups));
            sharedPreferencesEditor.apply();
            for(int i=0;i<lst_Groups.size();i++)
            {
               closecodePresenter.GetCloseCodes(Integer.valueOf(lst_Groups.get(i).getGroupID()));
            }
        }
    }

    @Override
    public void ShowError(String error) {
     Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
    }
}

