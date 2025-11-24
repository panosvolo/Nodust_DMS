package com.mts.mea.nodust_app.common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.mts.mea.nodust_app.Pilots.Pilot;
import com.mts.mea.nodust_app.Pilots.Pilot_Activity;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.orders.GetOrders.HomeWithMenuActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class langaugeActivity extends AppCompatActivity {
    Spinner spinner;
    Button update;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_change_languages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getApplicationContext().getResources().getString(R.string.action_settings));
        spinner=(Spinner)findViewById(R.id.sp_lan);
        update=(Button)findViewById(R.id.btn_lan);
        textView=(TextView)findViewById(R.id.tv_lng);
        List<String> lst=new ArrayList<>();

        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.My_Prefs_language_NoDust, 0);
        if (sharedPreferences.contains("SelectedLanguage"))
        {
            if(sharedPreferences.getString("SelectedLanguage"," ").equalsIgnoreCase("English"))
            {
                update.setText("Update Language");
                textView.setText("Choose Your Language");
                lst.add("English");
                lst.add("العربيه");
            }
            else
            {
                update.setText("تغير اللغه");
                textView.setText("اختر لغتك");
                lst.add("العربيه");
                lst.add("English");

            }

        }
        else
        {
            lst.add("English");
            lst.add("العربيه");
            update.setText("Update Language");
            textView.setText("Choose Your Language");
        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HandleChangeLanguage();
            }
        });
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.drop_list_item,lst);
        arrayAdapter.setDropDownViewResource(R.layout.item_in_spinner);
        spinner.setAdapter(arrayAdapter);
        final int[] check = {0};
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (++check[0] > 1) {
                    if (spinner.getSelectedItem().toString().equalsIgnoreCase("English")) {
                        update.setText("Update Language");
                        textView.setText("Choose Your Language");
                    } else {
                        update.setText("تغير اللغه");
                        textView.setText("اختر لغتك");
                     }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void HandleChangeLanguage() {
        Locale mLocal = null;
        String lan="";
        if (spinner.getSelectedItem().toString().equalsIgnoreCase("English"))
        {
            mLocal = Locale.ENGLISH;
            lan="English";
        }
        else
        {
            mLocal = new Locale("ar");
            lan="ar";
        }
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.My_Prefs_language_NoDust, 0);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString("SelectedLanguage", lan);
        sharedPreferencesEditor.apply();
        ChangeLanguageType(getApplicationContext(), mLocal);

    }
    public  void ChangeLanguageType(Context context, Locale localelanguage) {
        Resources resources = context.getResources();

        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
       // config.setLocale(localelanguage);
       // resources.updateConfiguration(config, dm);
        Locale locale = localelanguage;
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
        context.getApplicationContext().getResources().updateConfiguration(config,
                context.getApplicationContext().getResources().getDisplayMetrics());

        resources.updateConfiguration(configuration, dm);

        Intent i = null;
        if (getIntent().getExtras().get("page").toString().equalsIgnoreCase("home"))
        {
            i = new Intent(this, HomeWithMenuActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
    }
        else {
            i = new Intent(this, Pilot_Activity.class);
            Pilot  mPilot=(Pilot)getIntent().getExtras().get("mPilot");
            i.putExtra("mPilot", (Serializable) mPilot);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();;
        }

    }

    @Override
    public void onBackPressed() {
        Intent i=null;
        if(getIntent().getExtras().get("page").toString().equalsIgnoreCase("home")) {
            i = new Intent(this, HomeWithMenuActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        else {
            i = new Intent(this, Pilot_Activity.class);
            Pilot  mPilot=(Pilot)getIntent().getExtras().get("mPilot");
            i.putExtra("mPilot", (Serializable) mPilot);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        }
        //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
        super.onBackPressed();
    }
}
