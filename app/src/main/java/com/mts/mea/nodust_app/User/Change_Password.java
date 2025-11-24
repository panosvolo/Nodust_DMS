package com.mts.mea.nodust_app.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mts.mea.nodust_app.BackgroundServices.GetCurrentLocaion;
import com.mts.mea.nodust_app.Login.IContractLogin;
import com.mts.mea.nodust_app.Login.LoginPresenter;
import com.mts.mea.nodust_app.Login.loginActivity;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.common.Constants;
import com.mts.mea.nodust_app.common.GetAddress;
import com.mts.mea.nodust_app.orders.GetOrders.HomeWithMenuActivity;

import org.json.JSONException;

public class Change_Password extends AppCompatActivity implements IContractLogin.View {
    EditText UserName,oldPassword,newPassword,RePassword;
    android.support.v7.widget.AppCompatButton btn_change;
    User user;
    private ProgressDialog pbDailog;
    IContractLogin.Presenter mPresenter;
    private String UserName2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__password);
        INTUI();
    }
    private void INTUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getApplicationContext().getResources().getString(R.string.ChangePass));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        oldPassword=(EditText)findViewById(R.id.input_oldpassword);
        newPassword=(EditText)findViewById(R.id.input_newPassword);
        RePassword=(EditText)findViewById(R.id.input_RenewPassword);
        btn_change=(android.support.v7.widget.AppCompatButton)findViewById(R.id.btn_changePass);
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckValidation())
                {
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
                    if (sharedPreferences.contains("UserName"))
                    {
                        UserName2=sharedPreferences.getString("UserName", "");
                    }
                    String Address= GetAddress.GetAddress(getApplicationContext(), GetCurrentLocaion.CurrentLoc);
                    User user=new User(UserName2,"",GetCurrentLocaion.CurrentLoc,Address);
                    mPresenter=new LoginPresenter(Change_Password.this,user,Change_Password.this);
                    mPresenter.ChangePassword(newPassword.getText().toString(),oldPassword.getText().toString());
                }
            }
        });
    }
    private boolean CheckValidation() {
       
              if(oldPassword.getText().toString().isEmpty())
            {
                oldPassword.setError(Change_Password.this.getApplicationContext().getResources().getString(R.string.errEmptyField));
                return false;
            }
            else  if(newPassword.getText().toString().isEmpty())
            {
                newPassword.setError(Change_Password.this.getApplicationContext().getResources().getString(R.string.errEmptyField));
                return false;
            }
            else  if(RePassword.getText().toString().isEmpty())
            {
                RePassword.setError(Change_Password.this.getApplicationContext().getResources().getString(R.string.errEmptyField));
                return false;
            }
            else if(RePassword.getText().toString().compareTo(newPassword.getText().toString())!=0)
            {
                RePassword.setError(Change_Password.this.getApplicationContext().getResources().getString(R.string.passNotMatch));
                return false;
            }
            return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, HomeWithMenuActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }

   
    @Override
    public void showError(String errMsg) {
        Toast.makeText(getApplicationContext(),errMsg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goToHome(UserInfo userInfo) throws JSONException {
        
    }

    @Override
    public void ShowLoading() {
        pbDailog = new ProgressDialog(Change_Password.this);
        pbDailog.setMessage(getApplicationContext().getResources().getString(R.string.Loading));
        pbDailog.setCancelable(false);
        pbDailog.show();
    }

    @Override
    public void StopLoading() {
        if(pbDailog!=null && pbDailog.isShowing())
            pbDailog.dismiss();

    }

    @Override
    public void SuccessChangePassword() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_TASks, 0);
        sharedPreferences.edit().clear().commit();
        Intent i=new Intent(this,GetCurrentLocaion.class);
        GetCurrentLocaion.FlagGPS=false;
        stopService(i);
        i = new Intent(this, loginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);

    }

   

   
}
