package com.mts.mea.nodust_app.AREA;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.mts.mea.nodust_app.Interfaces.ServerManager;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.User.UserInfo;
import com.mts.mea.nodust_app.common.Constants;
import com.mts.mea.nodust_app.common.DataBaseHelper;
import com.mts.mea.nodust_app.orders.GetOrders.HomeWithMenuActivity;

import java.util.ArrayList;
import java.util.List;

public class CHOOSE_AREA extends AppCompatActivity {
    Spinner spinner;
    Button Save;
    DataBaseHelper DB;
    List<String> Areas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose__area);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getApplicationContext().getResources().getString(R.string.myAreas));
        spinner=(Spinner)findViewById(R.id.sp_AreaName);
        Save=(Button)findViewById(R.id.btn_update);
        DB=new DataBaseHelper(getApplicationContext());
        final SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        UserInfo userInfo= ServerManager.deSerializeStringToObject(sharedPreferences.getString("UserInfo",""),UserInfo.class);
        Areas=new ArrayList<>();
        if(userInfo.getUSERGROUP_ID()==2)
          Areas=DB.GetAreasPilots(sharedPreferences.getString("UserName", ""));
        else
        {
            Areas=DB.GetAreas(sharedPreferences.getString("UserName", ""));

        }

        ArrayAdapter adapter_areas=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, Areas);
        spinner.setAdapter(adapter_areas);
        if(Areas.size()>0)
        spinner.setSelection(0);
        if(sharedPreferences.contains("AreaName"))
        {
           int indx= Areas.indexOf(sharedPreferences.getString("AreaName",""));
            if(indx>=0)
            spinner.setSelection(indx);
        }
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Areas.size()>0)
                {
                    sharedPreferences.edit().putString("AreaName",spinner.getSelectedItem().toString()).commit();
                }
               HomePage();
            }
        });



    }

    private void HomePage() {
        Intent i=new Intent(this, HomeWithMenuActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(this, HomeWithMenuActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
        super.onBackPressed();
    }
}
