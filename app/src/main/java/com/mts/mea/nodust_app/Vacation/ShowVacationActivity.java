package com.mts.mea.nodust_app.Vacation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.common.Constants;

public class ShowVacationActivity extends AppCompatActivity {
TextView FromDate,ToDate,Reason,Comments,Status,tv_supComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_vacation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getApplicationContext().getResources().getString(R.string.VacationDetails));
        setSupportActionBar(toolbar);
        IntiUI();
    }

    private void IntiUI() {
        FromDate=(TextView)findViewById(R.id.tv_FromDate);
        ToDate=(TextView)findViewById(R.id.tv_toDate);
        Reason=(TextView)findViewById(R.id.tv_Reason);
        Comments=(TextView)findViewById(R.id.tv_Comments);
        Status=(TextView)findViewById(R.id.tv_Status);
        tv_supComment=(TextView)findViewById(R.id.tv_supComment);
        VacationRequest vacationRequest=(VacationRequest)getIntent().getExtras().get("Vacation");
        FromDate.setText(vacationRequest.getVacation_from_date());
        ToDate.setText(vacationRequest.getVacation_to_date());
        final SharedPreferences sharedPreferences_lang = getApplicationContext().getSharedPreferences(Constants.My_Prefs_language_NoDust, 0);
        if (sharedPreferences_lang.contains("SelectedLanguage")) {
            if(sharedPreferences_lang.getString("SelectedLanguage","En").equalsIgnoreCase("English"))
            {
                Reason.setText(vacationRequest.getReason_EN());
                Status.setText(vacationRequest.getApproval_status_EN());


            }
            else
            {
                Reason.setText(vacationRequest.getReason_AR());
                Status.setText(vacationRequest.getApproval_status_AR());




            }
        }
        else {
            Reason.setText(vacationRequest.getReason_EN());
            Status.setText(vacationRequest.getApproval_status_EN());



        }
        Comments.setText(vacationRequest.getNotes());
        tv_supComment.setText(vacationRequest.getSupervisorComment());





    }
}
