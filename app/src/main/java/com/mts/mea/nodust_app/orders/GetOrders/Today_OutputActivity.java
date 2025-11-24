package com.mts.mea.nodust_app.orders.GetOrders;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mts.mea.nodust_app.Adapters.PilotAdapter;
import com.mts.mea.nodust_app.BackgroundServices.GetCurrentLocaion;
import com.mts.mea.nodust_app.Evalution.KPI;
import com.mts.mea.nodust_app.Interfaces.ServerManager;
import com.mts.mea.nodust_app.Pilots.Pilot;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.User.User;
import com.mts.mea.nodust_app.common.Constants;
import com.mts.mea.nodust_app.common.GetAddress;
import com.mts.mea.nodust_app.products.Collection;
import com.mts.mea.nodust_app.products.Product;
import com.mts.mea.nodust_app.orders.Task;
import com.mts.mea.nodust_app.products.PackageInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Today_OutputActivity extends AppCompatActivity implements IContractGetOrders.View {

    private RecyclerView recyclerView;
    private PilotAdapter pilotAdapter;
    private String UserName;
    private IContractGetOrders.Presenter mPresnter;
    LinearLayout ll_driver;
    LinearLayout ll_pilot;
    TextView tv_RNo,tv_Amount,tv_Assign,tv_done,tv_notdone,tv_partial,tv_pending;
    Button btn_showMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today__output);
        IntiUI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void IntiUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getApplicationContext().getResources().getString(R.string.TodayOutput));
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        ll_driver=(LinearLayout)findViewById(R.id.ll_DriverDetails);
        ll_pilot=(LinearLayout)findViewById(R.id.ll_pilotDetails);
        tv_RNo=(TextView)findViewById(R.id.tv_RecNo);
        tv_Amount=(TextView)findViewById(R.id.tv_TotalAmount);
        tv_Assign=(TextView)findViewById(R.id.tv_assign);
        tv_pending=(TextView)findViewById(R.id.tv_pending) ;
        tv_done=(TextView)findViewById(R.id.tv_done);
        tv_notdone=(TextView)findViewById(R.id.tv_notDone) ;
        tv_partial=(TextView)findViewById(R.id.tv_partial) ;
        btn_showMore=(Button) findViewById(R.id.btn_showMore);
        btn_showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_driver.setVisibility(View.GONE);
                ll_pilot.setVisibility(View.VISIBLE);
                GetPilots();
            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.recView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(Today_OutputActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        if (sharedPreferences.contains("UserName")) {
            UserName = sharedPreferences.getString("UserName", "");
        }
        String Address = GetAddress.GetAddress(getApplicationContext(), GetCurrentLocaion.CurrentLoc);
        User user = new User(UserName, "", GetCurrentLocaion.CurrentLoc, Address);
        mPresnter = new GetOrdersPresenter(this, getApplicationContext(), user);
        GetTasks();
        //GetPilots();
    }

    private void GetTasks() {
        int No_Assigned=0;
        int No_parial=0;
        int No_done=0;
        int No_notDone=0;
        int pending=0;
        int No_pending=0;
        int No_RNo=0;

        // Get Tasks From Shared pref Driver_TASKS
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_TASks, 0);
        if (sharedPreferences.contains("Driver_TASKS")) {
            Task[] Tasks = ServerManager.deSerializeStringToObject(sharedPreferences.getString("Driver_TASKS",""), Task[].class);
            List<Task> AssignedTasks = new ArrayList<Task>(Tasks.length);
            AssignedTasks = Arrays.asList(Tasks);
            if(AssignedTasks.size()>0)
            {
                for(int i=0;i<AssignedTasks.size();i++)
                {
                    if(AssignedTasks.get(i).getPilotID()!=null)
                    {
                        No_Assigned++;
                    }
                    if(AssignedTasks.get(i).getStatus()==null)
                    {

                        No_pending++;
                    }
                    else if(AssignedTasks.get(i).getStatus().equalsIgnoreCase("Delivered"))
                    {
                        No_done++;
                    }
                    else if(AssignedTasks.get(i).getStatus().equalsIgnoreCase("Not Delivered"))
                    {
                        No_notDone++;
                    }
                    else if(AssignedTasks.get(i).getStatus().equalsIgnoreCase("Partially Delivered"))
                    {
                        No_parial++;
                    }

                }
                No_RNo=No_parial+No_done;
                No_pending=No_Assigned-(No_done+No_notDone+No_parial);
                tv_partial.setText(String.valueOf(No_parial));
                tv_done.setText(String.valueOf(No_done));
                tv_Assign.setText(String.valueOf(No_Assigned));
                tv_notdone.setText(String.valueOf(No_notDone));
                tv_RNo.setText(String.valueOf(No_RNo));
                tv_pending.setText(String.valueOf(No_pending));
                mPresnter.setAmount();

            }
        }
    }

    private void GetPilots()
    {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_TASks, 0);
        if(sharedPreferences.contains("Pilots"))
        {
            Pilot[] Tasks = ServerManager.deSerializeStringToObject(sharedPreferences.getString("Pilots",""), Pilot[].class);
            List<Pilot> AssignedTasks = new ArrayList<Pilot>(Tasks.length);
            AssignedTasks = Arrays.asList(Tasks);
            if(AssignedTasks!=null&&AssignedTasks.size()>0)
            {
                pilotAdapter = new PilotAdapter(null,null,getApplicationContext(), AssignedTasks,"TodayDtails",null);
                recyclerView.setAdapter(pilotAdapter);
            }
        }
        else
        {
            mPresnter.setPilots();
        }
    }

    @Override
    public void setTasks(List<Task> Tasks) {

    }

    @Override
    public void showError(String error) {
        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setProducts(List<Product> products) {

    }

    @Override
    public void setCoverProducts(List<Product> products) {

    }

    @Override
    public void setPilots(List<Pilot> Pilots) {
        if(Pilots!=null&&Pilots.size()>0)
        {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_TASks, 0);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            sharedPreferencesEditor.putString("Pilots",ServerManager.serializeObjectToString(Pilots));
            sharedPreferencesEditor.apply();
            pilotAdapter = new PilotAdapter(null,null,getApplicationContext(), Pilots,"TodayDtails",null);
            recyclerView.setAdapter(pilotAdapter);
        }
    }

    @Override
    public void setTotalAmount(String TotalAmount) {
        tv_Amount.setText(String.valueOf(TotalAmount));
    }

    @Override
    public void setKPI(List<KPI> kpis) {

    }

    @Override
    public void setProductReference(List<Product> products) {

    }

    @Override
    public void setPackageInfo(List<PackageInfo> packageInfoList) {

    }

    @Override
    public void ShowLoading() {

    }

    @Override
    public void StopLoading() {

    }

    @Override
    public void setCollection(List<Collection> products) {

    }
}
