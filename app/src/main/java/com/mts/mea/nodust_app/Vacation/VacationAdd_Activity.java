package com.mts.mea.nodust_app.Vacation;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mts.mea.nodust_app.Adapters.CloseCodeAdapter;
import com.mts.mea.nodust_app.BackgroundServices.GetCurrentLocaion;
import com.mts.mea.nodust_app.Interfaces.ServerManager;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.User.User;
import com.mts.mea.nodust_app.User.UserInfo;
import com.mts.mea.nodust_app.common.Constants;
import com.mts.mea.nodust_app.common.DataBaseHelper;
import com.mts.mea.nodust_app.common.GetAddress;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationAdd_Activity extends AppCompatActivity  implements View.OnClickListener,IContractVacation.View{
EditText FromDate,ToDate,Comments;
    TextView tv_balance;
    android.support.v7.widget.AppCompatSpinner Reason;
    Button btn_save;
    Calendar myCalendar ;
    VacationRequest vacationRequest;
    String UserName;
    IContractVacation.Presenter myPresenter;
    DataBaseHelper DB;
    ProgressDialog pbDailog;
    private List<vacation_reasons> vacation_reasonses;
    private List<String> data_spiner;
    private UserInfo userInfo;
    private vacation_reasons SelectedReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_add_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getApplicationContext().getResources().getString(R.string.RequestVacation));
        setSupportActionBar(toolbar);
        intiUI();

    }

    private void intiUI() {
         DB=new DataBaseHelper(this);
        String Address= GetAddress.GetAddress(getApplicationContext(), GetCurrentLocaion.CurrentLoc);
        vacation_reasonses=DB.GetVacationReasons();
        data_spiner=new ArrayList<>();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        final SharedPreferences sharedPreferences_lang = getApplicationContext().getSharedPreferences(Constants.My_Prefs_language_NoDust, 0);
        if (sharedPreferences_lang.contains("SelectedLanguage")) {
            if(sharedPreferences_lang.getString("SelectedLanguage","En").equalsIgnoreCase("English"))
            {
                if(vacation_reasonses!=null)

                    for(int i=0;i<vacation_reasonses.size();i++)
                {
                    data_spiner.add(vacation_reasonses.get(i).getNAME_EN());

                }
            }
            else
            {
                if(vacation_reasonses!=null)

                    for(int i=0;i<vacation_reasonses.size();i++)
                {
                    data_spiner.add(vacation_reasonses.get(i).getNAME_AR());

                }
            }
        }
        else
        {
            if(vacation_reasonses!=null)
            for(int i=0;i<vacation_reasonses.size();i++)
            {
                data_spiner.add(vacation_reasonses.get(i).getNAME_EN());

            }
        }
        Log.d("sizeVacations", String.valueOf(data_spiner.size()));
        CloseCodeAdapter adapter=new CloseCodeAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, data_spiner);

        if (sharedPreferences.contains("UserName"))
        {
            UserName=sharedPreferences.getString("UserName", "");
        }
        User user=new User(UserName,"",GetCurrentLocaion.CurrentLoc,Address);
        myPresenter=new VacationPresenter(this,this,user);
        myCalendar = Calendar.getInstance();
        FromDate=(EditText)findViewById(R.id.ed_fromdate);
        ToDate=(EditText)findViewById(R.id.ed_todate);
        Reason=(android.support.v7.widget.AppCompatSpinner)findViewById(R.id.ed_Reason);
        Reason.setAdapter(adapter);
        Comments=(EditText)findViewById(R.id.ed_comments);
        btn_save=(Button)findViewById(R.id.btn_save);
        tv_balance=(TextView)findViewById(R.id.tv_balance);
        SharedPreferences sharedPreferences2=getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
         userInfo = ServerManager.deSerializeStringToObject(sharedPreferences2.getString("UserInfo", ""), UserInfo.class);
        tv_balance.setText(String.valueOf(userInfo.getBalance_Vacation_Day()));
        FromDate();
        ToDate();
        btn_save.setOnClickListener(this);
    }

    private  void FromDate()
    {
    //    final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateFromDate();
            }

        };

        FromDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(VacationAdd_Activity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateFromDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        FromDate.setText(sdf.format(myCalendar.getTime()));
    }
    private void ToDate()
    {
        //    final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateToDate();
            }

        };

        ToDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(VacationAdd_Activity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateToDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        ToDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View v) {
        FromDate.setError(null);
        ToDate.setError(null);
       // Reason.setError(null);
        Comments.setError(null);
        if(FromDate.getText().toString().isEmpty())
        {
            String err=getApplicationContext().getResources().getString(R.string.FillValue);
            err+=" "+getApplicationContext().getResources().getString(R.string.FromDate);
            FromDate.setError(err);

        }
        else if(ToDate.getText().toString().isEmpty())
        {

            String err=getApplicationContext().getResources().getString(R.string.FillValue);
            err+=" "+getApplicationContext().getResources().getString(R.string.ToDate);
            ToDate.setError(err);
        }
      /*  else if(Reason.getText().toString().isEmpty())
        {
            String err=getApplicationContext().getResources().getString(R.string.FillValue);
            err+=" "+getApplicationContext().getResources().getString(R.string.Reason);
            Reason.setError(err);
        }*/
        else if(Comments.getText().toString().isEmpty())
        {
            String err=getApplicationContext().getResources().getString(R.string.FillValue);
            err+=" "+getApplicationContext().getResources().getString(R.string.Comments);
            Comments.setError(err);
        }
        else
        {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            Date d_from= null;
            Date d_to=null;
            Date currentdaten=new Date();
            try {
                d_from = formatter.parse(FromDate.getText().toString());
                 d_to=formatter.parse(ToDate.getText().toString());
           //     Toast.makeText(getApplicationContext(),formatter.format(currentdaten).toString(),Toast.LENGTH_SHORT).show();
                currentdaten=formatter.parse(formatter.format(currentdaten).toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

           // if(d_from.compareTo(d_from))
         //   Toast.makeText(getApplicationContext(),","+d_from.compareTo(currentdaten),Toast.LENGTH_SHORT).show();

            if(d_to.compareTo(d_from)>0&&d_from.compareTo(currentdaten)>=0) {
               vacationRequest=new VacationRequest();
                vacationRequest.setNo_days((int) printDifference(d_from,d_to));
                vacationRequest.setRequesterID(UserName);
                vacationRequest.setVacation_from_date(FromDate.getText().toString());
                vacationRequest.setVacation_to_date(ToDate.getText().toString());
                vacationRequest.setNotes(Comments.getText().toString());
                vacationRequest.setReason_AR(vacation_reasonses.get(Reason.getSelectedItemPosition()).getID());
                SelectedReason=new vacation_reasons();
                    // Check Available Balance
                    if(CheckAvailableBalance(vacationRequest,vacation_reasonses.get(Reason.getSelectedItemPosition())))
                    {
                        SelectedReason=vacation_reasonses.get(Reason.getSelectedItemPosition());
                        try {
                            myPresenter.SaveVacation(vacationRequest);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                        Toast.makeText(getApplicationContext(),getApplicationContext()
                        .getResources().getString(R.string.noVacation),Toast.LENGTH_SHORT).show();

            }
            else
            {
                Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.errorDate),Toast.LENGTH_SHORT).show();
                ToDate.setError(getApplicationContext().getResources().getString(R.string.errorDate));
                FromDate.setError(getApplicationContext().getResources().getString(R.string.errorDate));
            }
        }
    }

    private boolean CheckAvailableBalance(VacationRequest vacationRequest, vacation_reasons vacation_reasons) {
      //  Toast.makeText(getApplicationContext(),String.valueOf(userInfo.getBalance_Vacation_Day()-vacationRequest.getNo_days()),Toast.LENGTH_SHORT).show();
        if(userInfo.getBalance_Vacation_Day()-vacationRequest.getNo_days()>=0)
        {
            return true;
        }
        else
        {
            if(vacation_reasons.getALLOWED_DAY0()==1)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    @Override
    public void ShowMsg(String msg) {
     Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
        if(msg.equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.SaveChanges)))
        {
            VacationAdd_Activity.this.finish();
        }
    }

    @Override
    public void InsertVacation(VacationRequest obj) {
        if(SelectedReason!=null) {
            obj.setReason_AR(SelectedReason.getNAME_AR());
            obj.setReason_EN(SelectedReason.getNAME_EN());
        }
      if( DB.InsertVacations(obj))
      {
          ShowMsg(getApplicationContext().getResources().getString(R.string.SaveChanges));
      }
    }

    @Override
    public void InsertVaction(List<VacationRequest> lst) {

    }

    @Override
    public void InsertVactionReason(List<vacation_reasons> lst) {

    }

    @Override
    public void ShowDialog() {
        pbDailog = new ProgressDialog(VacationAdd_Activity.this);
        pbDailog.setMessage("جاري التحميل .....");
        pbDailog.setCancelable(false);
        pbDailog.show();
    }

    @Override
    public void HideDialog() {
        if(pbDailog!=null && pbDailog.isShowing())
            pbDailog.dismiss();
    }
    public long printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
     //   different = different % daysInMilli;
        return elapsedDays;
    }
}
