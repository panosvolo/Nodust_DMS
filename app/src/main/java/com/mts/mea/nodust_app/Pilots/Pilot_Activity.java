package com.mts.mea.nodust_app.Pilots;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mts.mea.nodust_app.AREA.CHOOSE_AREA;
import com.mts.mea.nodust_app.Adapters.OrderAdapter;
import com.mts.mea.nodust_app.Adapters.PilotAdapter;
import com.mts.mea.nodust_app.BackgroundServices.GetCurrentLocaion;
import com.mts.mea.nodust_app.DashBoard.new_DashBoard;
import com.mts.mea.nodust_app.Evalution.KPI;
import com.mts.mea.nodust_app.Interfaces.ServerManager;
import com.mts.mea.nodust_app.Login.loginActivity;
import com.mts.mea.nodust_app.Pilots.PilotCover.IPilotCover;
import com.mts.mea.nodust_app.Pilots.PilotCover.PilotCoverPresenter;
import com.mts.mea.nodust_app.Pilots.PilotCover.Pilot_Cover;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.User.User;
import com.mts.mea.nodust_app.User.UserInfo;
import com.mts.mea.nodust_app.User.UserShift.IContractUserShif;
import com.mts.mea.nodust_app.User.UserShift.UserShiftPresenter;
import com.mts.mea.nodust_app.Vacation.Vacationlist_Activity;
import com.mts.mea.nodust_app.common.Constants;
import com.mts.mea.nodust_app.common.DataBaseHelper;
import com.mts.mea.nodust_app.common.GetAddress;
import com.mts.mea.nodust_app.common.langaugeActivity;
import com.mts.mea.nodust_app.orders.GetOrders.HomeWithMenuActivity;
import com.mts.mea.nodust_app.orders.GetOrders.ViewPagerAdapter;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Pilot_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener,IContractUserShif.View,IPilotCover.View {
    ViewPager viewPager;
    TabLayout tabLayout;
    private UserInfo userInfo;
    DataBaseHelper DB;
    private pilotFragment fragment_tasks;
    Pilot mPilot;
    IContractUserShif.Presenter mPresenter;
    IPilotCover.Presenter CoverPresenter;
    private String userName;
    Button btn_shift,btn_End,btn_evalution,btn_accept;
    private ProgressDialog pbDailog;
    static int SelectedPos;
   public static List<String>mselectedTasks;
   static OrderAdapter mOrderAdapter;
    private pilotFragment fragment_tasks2;
    LinearLayout ll_items;
    List<EditText>edit_lst_comments;
    List<CheckBox>check_list_kpi;
    new_DashBoard Dashboard;
    double Scale_value=0;
    private int postionTab2=0;
    TextView tv_areaName;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilot_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.My_Prefs_language_NoDust, 0);
        mPilot=(Pilot)getIntent().getExtras().get("mPilot");
        if (sharedPreferences.contains("SelectedLanguage")) {
            if(sharedPreferences.getString("SelectedLanguage","En").equalsIgnoreCase("English")) {

                toolbar.setTitle(mPilot.getPilotID()+"/"+mPilot.getNameEn());

            }
            else
            {
                toolbar.setTitle(mPilot.getPilotID()+"/"+mPilot.getNameAr());
            }
        }
        else
        {
            toolbar.setTitle(mPilot.getPilotID()+"/"+mPilot.getNameEn());
        }
        //  toolbar.setTitle(getApplicationContext().getResources().getString(R.string.Pilots));
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View v = navigationView.getHeaderView(0);
        TextView UserName = (TextView) v.findViewById(R.id.tv_userName);
        TextView  Version=(TextView)v.findViewById(R.id.welcome);
        try {
            android.content.pm.PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            Version.setText("Version Code: "+String.valueOf(pInfo.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        SharedPreferences sharedPreferences2=getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        userInfo= ServerManager.deSerializeStringToObject(sharedPreferences2.getString("UserInfo",""),UserInfo.class);
        mPilot=(Pilot)getIntent().getExtras().get("mPilot");
        if (sharedPreferences.contains("SelectedLanguage")) {
            if(sharedPreferences.getString("SelectedLanguage","En").equalsIgnoreCase("English")) {

                UserName.setText(userInfo.getNameEn());
                toolbar.setTitle(mPilot.getPilotID()+"/"+mPilot.getNameEn());

            }
            else
            {
                UserName.setText(userInfo.getNameAr());
                toolbar.setTitle(mPilot.getPilotID()+"/"+mPilot.getNameAr());
            }
        }
        else
        {
            UserName.setText(userInfo.getNameEn());
            toolbar.setTitle(mPilot.getPilotID()+"/"+mPilot.getNameEn());
        }
      tv_areaName=(TextView)findViewById(R.id.tv_areaName);
        SharedPreferences sharedPreferences_Area = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        tv_areaName.setText(sharedPreferences_Area.getString("AreaName",""));
       NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view2);
        navigationView1.setNavigationItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        DB=new DataBaseHelper(this);

         sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        if (sharedPreferences.contains("UserName"))
        {
            userName=sharedPreferences.getString("UserName", "");
        }
        String Address= GetAddress.GetAddress(getApplicationContext(), GetCurrentLocaion.CurrentLoc);
        User user=new User(userName,"",GetCurrentLocaion.CurrentLoc,Address);
        mPresenter=new UserShiftPresenter(Pilot_Activity.this,getApplicationContext(),user);
        CoverPresenter=new PilotCoverPresenter(Pilot_Activity.this,getApplicationContext(),user);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.My_Prefs_language_NoDust, 0);
        if(sharedPreferences.contains("loginToday")) {
            Calendar c=Calendar.getInstance();
            String CurrentDate=c.get(Calendar.DATE)+"/"+c.get(Calendar.MONTH)+"/"+(c.get(Calendar.YEAR));
            if(!sharedPreferences.getString("loginToday","").equalsIgnoreCase(CurrentDate)) {
                 sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_TASks, 0);
                sharedPreferences.edit().clear().commit();
                Intent i = new Intent(this, GetCurrentLocaion.class);
                GetCurrentLocaion.FlagGPS = false;
                stopService(i);
                i = new Intent(this, loginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
            else
            {
                try {
                    IntiUI();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            try {
                IntiUI();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_with_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final MenuItem AssignTask=menu.findItem(R.id.action_assign_task);
        final MenuItem Reload=menu.findItem(R.id.action_settings);
        final MenuItem action_all_assign_task=menu.findItem(R.id.action_all_assign_task);

        AssignTask.setVisible(false);
        searchItem.setVisible(false);
        Reload.setVisible(true);
        action_all_assign_task.setVisible(false);
        if(SelectedPos==1) {
            AssignTask.setVisible(true);
            action_all_assign_task.setVisible(true);
            searchItem.setVisible(true);

        }
        else if(SelectedPos==0)
        {
            AssignTask.setVisible(true);

        }
      /*  action_all_assign_task.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                fragment_tasks2.SetAllSelection();
                return true;
            }
        });*/
        if (searchItem != null) {

            android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);
            searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return true;
                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                   /* if(userInfo.getUSERGROUP_ID()==2) { // pilot
                        if(postionTab2==0)
                            fragment_tasks2.Search(query);
                    }
                    else
                    {
                        if(postionTab2==1)
                            fragment_tasks2.Search(query);
                    }*/
                    fragment_tasks2.Search(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    //  if(userInfo.getUSERGROUP_ID()==2)
                  /*  if(userInfo.getUSERGROUP_ID()==2) { // pilot
                        if (postionTab2 == 0)
                            fragment_tasks2.Search(newText);
                    }
                    else {
                        if (postionTab2 == 1)
                            fragment_tasks2.Search(newText);
                    }*/
                    fragment_tasks2.Search(newText);

                    return false;
                }
            });
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    return false;
                }
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_assign_task)
        {
            if(SelectedPos==1) {
                if (mselectedTasks != null && mselectedTasks.size() > 0) {
                    ShowAvailablePilots(Pilot_Activity.this, mselectedTasks);
                    //mselectedTasks=null;

                }
            }
            else if(SelectedPos==0)
            {
                if(btn_evalution.getVisibility()==View.GONE) {
                  //  fragment_tasks.CheckCoverAssignedToPilot();
                }
                else
                {

                     Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.EvaluationFirst),Toast.LENGTH_SHORT).show();

                }
               // Toast.makeText(getApplicationContext(),"click",Toast.LENGTH_SHORT).show();
            }
        }
        else if(item.getItemId()==R.id.action_all_assign_task)
        {
            fragment_tasks2.SetAllSelection();
        }
        else if (item.getItemId() == R.id.action_settings) {
           /* if(userInfo.getUSERGROUP_ID()==2)
            {
            if( fragment_tasks.mPresnter!=null &&!fragment_tasks.btn_shift.getText().toString().equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.StartShift)))
            fragment_tasks.mPresnter.setTasks();
            }
            else
            {
                DB.DeletAll();
              IntiUI();
            }*/
                DB.UpdateAllData(null,false);
        }
        return super.onOptionsItemSelected(item);
    }

    private void IntiUI() throws ParseException {
         btn_shift = (Button) findViewById(R.id.btn_UserShift);
        btn_End=(Button)findViewById(R.id.btn_EndShift);
        btn_evalution=(Button)findViewById(R.id.btn_evalution);
      //  btn_accept=(Button)findViewById(R.id.btn_accept);
//        btn_accept.setOnClickListener(this);
        btn_evalution.setOnClickListener(this);
        Date CurrentDate = new Date();
        DateFormat formatter ;
        Date date ;
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if(mPilot.getEVALUTION_DATE()!=null)
        {
            date = (Date) formatter.parse(mPilot.getEVALUTION_DATE());
            Calendar cal = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal.setTime(date);
            cal2.setTime(CurrentDate);
            cal.get(Calendar.DAY_OF_MONTH);
            if (cal.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH) && cal2.get(Calendar.YEAR) == cal.get(Calendar.YEAR) && cal2.get(Calendar.MONTH) == cal.get(Calendar.MONTH)) {
             btn_evalution.setVisibility(View.GONE);
             //   if(mPilot.getPilotID().equalsIgnoreCase(userName))
             //   btn_accept.setVisibility(View.VISIBLE);
            }
            else
            {
            btn_evalution.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            btn_evalution.setVisibility(View.VISIBLE);
        }


        try {
            if(mPilot.getAttendanceTime()!=null) {
                date = (Date) formatter.parse(mPilot.getAttendanceTime());
                Calendar cal = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();
                cal.setTime(date);
                cal2.setTime(CurrentDate);
                cal.get(Calendar.DAY_OF_MONTH);
                if (cal.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH) && cal2.get(Calendar.YEAR) == cal.get(Calendar.YEAR) && cal2.get(Calendar.MONTH) == cal.get(Calendar.MONTH)) {
                    btn_shift.setText(getApplicationContext().getResources().getString(R.string.ReturnTime));
                   /// new
                    btn_shift.setVisibility(View.GONE);
                    btn_End.setVisibility(View.VISIBLE);
                    btn_End.setText(getApplicationContext().getResources().getString(R.string.ReturnTime));
                    ///
                    if (mPilot.getRETURN_Time() != null) {
                      /*  Toast.makeText(getApplicationContext(),mPilot.getRETURN_Time(),Toast.LENGTH_SHORT).show();
                        date = (Date) formatter.parse(mPilot.getRETURN_Time());
                        cal = Calendar.getInstance();
                        cal2 = Calendar.getInstance();
                        cal.setTime(date);
                        if (cal.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH) && cal2.get(Calendar.YEAR) == cal.get(Calendar.YEAR) && cal2.get(Calendar.MONTH) == cal.get(Calendar.MONTH)) {
                            btn_shift.setVisibility(View.GONE);
                            btn_End.setVisibility(View.GONE);
                        } else {
                            btn_shift.setText(getApplicationContext().getResources().getString(R.string.ReturnTime));
                            btn_shift.setVisibility(View.GONE);
                            btn_End.setVisibility(View.VISIBLE);
                            btn_End.setText(getApplicationContext().getResources().getString(R.string.ReturnTime));
                        }*/
                        btn_shift.setVisibility(View.GONE);
                        btn_End.setVisibility(View.GONE);
                    } else {
                        btn_shift.setText(getApplicationContext().getResources().getString(R.string.ReturnTime));
                        btn_shift.setVisibility(View.GONE);
                        btn_End.setVisibility(View.VISIBLE);
                        btn_End.setText(getApplicationContext().getResources().getString(R.string.ReturnTime));
                    }
                }
                else {
                    btn_shift.setText(getApplicationContext().getResources().getString(R.string.TimeAttendance));
                    btn_End.setText(getApplicationContext().getResources().getString(R.string.TimeAttendance));
                    ///
                    /////
                    btn_shift.setVisibility(View.GONE);
                    // old   btn_End.setVisibility(View.GONE);


                    btn_End.setVisibility(View.GONE);
                }
            }
            else
            {
                btn_shift.setText(getApplicationContext().getResources().getString(R.string.TimeAttendance));
                btn_End.setText(getApplicationContext().getResources().getString(R.string.TimeAttendance));
                btn_shift.setVisibility(View.GONE);
               // old btn_End.setVisibility(View.VISIBLE);
                btn_End.setVisibility(View.GONE);

            }

            btn_shift.setOnClickListener(this);
            btn_End.setOnClickListener(this);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
        if(mPilot.getPilotID().equalsIgnoreCase(userName))
        {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

            tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.ic_product_white)); // products
            tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.ic_tasks_white)); // contracts
            tabLayout.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.ic_product_white)); // products

            tabLayout.getTabAt(3).setIcon(getApplicationContext().getResources().getDrawable(R.drawable.ic_tasks_white));
            tabLayout.getTabAt(4).setIcon(getApplicationContext().getResources().getDrawable(R.drawable.ic_tasks_white));
            tabLayout.getTabAt(5).setIcon(getApplicationContext().getResources().getDrawable(R.drawable.ic_tasks_white));


        }
        else {
            tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.ic_product_white)); // products
            tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.ic_tasks_white)); // contracts
            tabLayout.getTabAt(2).setIcon(getApplicationContext().getResources().getDrawable(R.drawable.ic_tasks_white));
            tabLayout.getTabAt(3).setIcon(getApplicationContext().getResources().getDrawable(R.drawable.ic_tasks_white));

        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                SelectedPos=tab.getPosition();
                postionTab2=tab.getPosition();

                invalidateOptionsMenu();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    private void setupViewPager(ViewPager viewPager) {
        // 2 tabs for products and contracts
        if(mPilot.getPilotID().equalsIgnoreCase(userName)) {

            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            fragment_tasks = new pilotFragment();
            Bundle fragmentType = new Bundle();
            fragmentType.putString("Type", "pilot_Product");
            fragmentType.putSerializable("pilot", mPilot);
            fragment_tasks.setArguments(fragmentType);
            adapter.addFragment(fragment_tasks, getApplicationContext().getResources().getString(R.string.Products));

            fragment_tasks2 = new pilotFragment();
            fragmentType = new Bundle();
            fragmentType.putString("Type", "pilot_tasks");
            fragmentType.putSerializable("pilot", mPilot);
            fragment_tasks2.setArguments(fragmentType);
            adapter.addFragment(fragment_tasks2, getApplicationContext().getResources().getString(R.string.action_Tasks));


            fragment_tasks = new pilotFragment();
             fragmentType = new Bundle();
            fragmentType.putString("Type", "Check_pilot_Product");
            fragmentType.putSerializable("pilot", mPilot);
            fragment_tasks.setArguments(fragmentType);
            adapter.addFragment(fragment_tasks, getApplicationContext().getResources().getString(R.string.PilotAccept));

            Dashboard = new new_DashBoard();
            fragmentType = new Bundle();
            fragmentType.putSerializable("Pilot", mPilot);
            Dashboard.setArguments(fragmentType);
            adapter.addFragment(Dashboard, getApplicationContext().getResources().getString(R.string.TodayOutput));

            fragment_tasks = new pilotFragment();
            fragmentType = new Bundle();
            fragmentType.putString("Type", "Pilot_Reconcilation");
            fragmentType.putSerializable("pilot", mPilot);
            fragment_tasks.setArguments(fragmentType);
            adapter.addFragment(fragment_tasks, getApplicationContext().getResources().getString(R.string.DriverReconciliation));


            fragment_tasks = new pilotFragment();
            fragmentType = new Bundle();
            fragmentType.putString("Type", "Driver_Reconcilation");
            fragmentType.putSerializable("pilot", mPilot);
            fragment_tasks.setArguments(fragmentType);
            adapter.addFragment(fragment_tasks, getApplicationContext().getResources().getString(R.string.PilotReconciliation));

            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(1);
            SelectedPos = 1;
            postionTab2 = 1;
        }
        else {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            fragment_tasks = new pilotFragment();
            Bundle fragmentType = new Bundle();
            fragmentType.putString("Type", "pilot_Product");
            fragmentType.putSerializable("pilot", mPilot);
            fragment_tasks.setArguments(fragmentType);
            adapter.addFragment(fragment_tasks, getApplicationContext().getResources().getString(R.string.Products));

            fragment_tasks2 = new pilotFragment();
            fragmentType = new Bundle();
            fragmentType.putString("Type", "pilot_tasks");
            fragmentType.putSerializable("pilot", mPilot);
            fragment_tasks2.setArguments(fragmentType);
            adapter.addFragment(fragment_tasks2, getApplicationContext().getResources().getString(R.string.action_Tasks));

            Dashboard = new new_DashBoard();
            fragmentType = new Bundle();
            fragmentType.putSerializable("Pilot", mPilot);
            Dashboard.setArguments(fragmentType);
            adapter.addFragment(Dashboard, getApplicationContext().getResources().getString(R.string.TodayOutput));

            fragment_tasks = new pilotFragment();
            fragmentType = new Bundle();
            fragmentType.putString("Type", "Pilot_Reconcilation");
            fragmentType.putSerializable("pilot", mPilot);
            fragment_tasks.setArguments(fragmentType);
            adapter.addFragment(fragment_tasks, getApplicationContext().getResources().getString(R.string.DriverReconciliation));

            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(1);
            SelectedPos = 1;
            postionTab2 = 1;
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_settings) {
            // Handle setting
            Intent i=new Intent(this,langaugeActivity.class);
            i.putExtra("page","pilot_activity");
            i.putExtra("mPilot", (Serializable) mPilot);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if(id==R.id.action_settle_output)
        {
            Intent i=new Intent(this,CHOOSE_AREA.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
        else if(id==R.id.action_vacation)
        {
            Intent i=new Intent(this, Vacationlist_Activity.class);
            startActivity(i);
        }
        else if (id == R.id.logout) {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_TASks, 0);
            sharedPreferences.edit().clear().commit();
            Intent i=new Intent(this,GetCurrentLocaion.class);
            GetCurrentLocaion.FlagGPS=false;
            stopService(i);
            i = new Intent(this, loginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        Button c=(Button)v;
        if(c.getText().toString().equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.TimeAttendance)))
        {
            mPresenter.SetAttendance(mPilot.getASSIGN_PILOT_ID());

        }
        else if(c.getText().toString().equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.ReturnTime)))
        {
            ShowEndDay();
        }
        else if (c.getText().toString().equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.Evalution2)))
        {
            ShowEvalution();

        }
        else if(c.getText().toString().equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.PilotAccept)))
        {
          //  CoverPresenter.Pilotaccept();
        }
    }

    @Override
    public void ShowMsg(String msg) {
        if(msg.equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.SaveChanges)+"Evalution"))
        {
            msg=getApplicationContext().getResources().getString(R.string.SaveChanges);
            DB.setEVALUTION_DATE(mPilot.getPilotID());
            btn_evalution.setVisibility(View.GONE);
         //   if(mPilot.getPilotID().equalsIgnoreCase(userName))
           // btn_accept.setVisibility(View.VISIBLE);
            Calendar c=Calendar.getInstance();
            String CurrentTime=c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DATE)+" "+c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
            mPilot.setEVALUTION_DATE(CurrentTime);


        }
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Startbtn(String ShiftId) {

    }

    @Override
    public void showMsg(String Msg) {
    Toast.makeText(getApplicationContext(),Msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ShowLoading() {
        pbDailog = new ProgressDialog(Pilot_Activity.this);
        pbDailog.setMessage("جاري التحميل .....");
        pbDailog.setCancelable(false);
        pbDailog.show();
    }

    @Override
    public void StopLoading() {
        if(pbDailog!=null)

            pbDailog.dismiss();
    }

    @Override
    public void SetCoverPilot(List<Pilot_Cover> lst_cover) {
        DB.AddCoverPilot(lst_cover,true);

    }

    @Override
    public void SuccessPilotAccept() {
        CoverPresenter.GetCoverAllPilot();

    }

    @Override
    public void SetCheckCoverPilot(List<Pilot_Cover> lst_cover) {
        DB.AddCoverPilot(lst_cover,false);

    }

    @Override
    public void SuccessPilotReject() {

    }

    @Override
    public void UpdateCoverAfterReconcilation() {
        CoverPresenter.GetCoverAllPilot();
    }

    @Override
    public void SetReconcilationRequests(List<Reconcilation> lst_reconcilation) {

    }

    @Override
    public void RefreshQTYReconcilation() {

    }

    @Override
    public void HideLoading() {
        if(pbDailog!=null)
            pbDailog.dismiss();
    }

    @Override
    public void Endbtn() {

    }

    @Override
    public void disableCheck() {
        DB.setAttendance(mPilot.getPilotID());
        Calendar c=Calendar.getInstance();
        String CurrentTime=c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DATE)+" "+c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
        mPilot.setAttendanceTime(CurrentTime);
        btn_shift.setText(getApplicationContext().getResources().getString(R.string.ReturnTime));
        btn_shift.setVisibility(View.GONE);
        btn_End.setVisibility(View.VISIBLE);
        btn_End.setText(getApplicationContext().getResources().getString(R.string.ReturnTime));

    }
    private  void ShowEvalution()
    {
        final Dialog dialog = new Dialog(Pilot_Activity.this);
        //set layout custom
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.new_popup_evalution);
        LinearLayout ll_dialog=(LinearLayout)dialog.findViewById(R.id.ll_eval) ;
        ll_items=(LinearLayout)dialog.findViewById(R.id.ll_items);
        final EditText notes=(EditText)dialog.findViewById(R.id.ed_notes);
        final List<KPI> kpis=DB.GetKPI();
        final List<EditText>edit_list=new ArrayList<>();
        edit_lst_comments=new ArrayList<>();
        for(int i=0;i<kpis.size();i++) {
            // add layout
            kpis.get(i).setKPI_VALUE("0");
            kpis.get(i).setComment("");
            addLayout_KPI(kpis.get(i));
          /*  EditText ed_kpi = new EditText(dialog.getContext());
            ed_kpi.setHint(kpis.get(i).getKPI_name());
            ll_dialog.addView(ed_kpi);
            edit_list.add(ed_kpi);*/
        }
        Button btn_send=(Button)dialog.findViewById(R.id.btn_sendEvalution);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                for(int i=0;i<kpis.size();i++)
                {
                    //  kpis.get(i).setKPI_VALUE(edit_list.get(i).getText().toString());
                    kpis.get(i).setID(mPilot.getASSIGN_PILOT_ID());
                    kpis.get(i).setComment(edit_lst_comments.get(i).getText().toString());
                }
                String query=ServerManager.serializeObjectToString(kpis);
                String Notes=notes.getText().toString();
                if(notes.getText().toString().equalsIgnoreCase(""))
                    Notes=" ";
                mPresenter.SetEvalution(kpis,Notes);
                dialog.dismiss();
            }
        });
        TextView tv_pilot=(TextView)dialog.findViewById(R.id.tv_pilotName);
        SharedPreferences sharedPreferences2 = getApplicationContext().getSharedPreferences(Constants.My_Prefs_language_NoDust, 0);

        if (sharedPreferences2.contains("SelectedLanguage")) {
            if(sharedPreferences2.getString("SelectedLanguage","En").equalsIgnoreCase("English")) {
                tv_pilot.setText(getApplicationContext().getResources().getString(R.string.Evalution)+" "+mPilot.getNameEn());
            }
            else
            {tv_pilot.setText(getApplicationContext().getResources().getString(R.string.Evalution)+" "+mPilot.getNameAr());
            }
        }
        else
        {
            tv_pilot.setText(getApplicationContext().getResources().getString(R.string.Evalution)+" "+mPilot.getNameEn());
        }

        dialog.show();
    }
    private void addLayout_KPI(final KPI kpi) {
        View add_layout= LayoutInflater.from(this).inflate(R.layout.item_evalution, ll_items, false);
        TextView kpi_name=(TextView)add_layout.findViewById(R.id.tx_kpiName);
        kpi_name.setText(kpi.getKPI_name());
        EditText comment_KPI=(EditText)add_layout.findViewById(R.id.ed_commentKPI) ;
        edit_lst_comments.add(comment_KPI);
//        edit_lst_comments.add(comment_KPI);
        LinearLayout ll_check=(LinearLayout)add_layout.findViewById(R.id.ll_check);
        CheckBox ch_kpi=(CheckBox)add_layout.findViewById(R.id.ch_KPIFound);
        ch_kpi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    kpi.setKPI_VALUE("1");
                else
                    kpi.setKPI_VALUE("0");
            }
        });
        //check_list_kpi.add(ch_kpi);
        LinearLayout ll_scale=(LinearLayout)add_layout.findViewById(R.id.ll_scale);
        if(kpi.getKPI_Type()!=null&&kpi.getKPI_Type().equalsIgnoreCase("scale"))
        {
            ll_check.setVisibility(View.GONE);
            ll_scale.setVisibility(View.VISIBLE);
        }
        else
        {
            ll_check.setVisibility(View.VISIBLE);
            ll_scale.setVisibility(View.GONE);
        }
        Button btn_increment=(Button)add_layout.findViewById(R.id.increment);
        Button btn_decrement=(Button)add_layout.findViewById(R.id.decrement);
        final TextView tv_number=(TextView)add_layout.findViewById(R.id.display);
        btn_increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double currentValue=Double.valueOf(tv_number.getText().toString());
                currentValue+=1;
                if(currentValue>3||currentValue<0)
                {
                    currentValue=0;
                }
                tv_number.setText(String.valueOf(currentValue));
                Scale_value=currentValue;
                kpi.setKPI_VALUE(tv_number.getText().toString());
            }
        });
        btn_decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double currentValue=Double.valueOf(tv_number.getText().toString());
                currentValue-=1;
                if(currentValue>3||currentValue<0)
                {
                    currentValue=0;
                }
                tv_number.setText(String.valueOf(currentValue));
                Scale_value=currentValue;
                kpi.setKPI_VALUE(tv_number.getText().toString());

            }
        });
        // check kpi if contain scale
        ll_items.addView(add_layout);
    }

    @Override
    public void disableCheckReturn() {
        DB.setReturnTime(mPilot.getPilotID());
        btn_shift.setVisibility(View.GONE);
        btn_End.setVisibility(View.GONE);
        Calendar c=Calendar.getInstance();
        String CurrentTime=c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DATE)+" "+c.get(Calendar.HOUR)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
        mPilot.setRETURN_Time(CurrentTime);
      //  ShowEndDay();

    }

    private void ShowEndDay() {
        mPresenter.SetReturnTime(mPilot.getASSIGN_PILOT_ID());
      /*  final Dialog dialog = new Dialog(Pilot_Activity.this);
        //set layout custom
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_total_rec_amount);
        final EditText ed_totalRecNo,ed_totalPrice;
        ed_totalRecNo=(EditText) dialog.findViewById(R.id.ed_totalRecNo);
        ed_totalPrice=(EditText) dialog.findViewById(R.id.ed_totalAmount);
        Button btn_send=(Button)dialog.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // send total amount,RecNo
                if(!ed_totalPrice.getText().toString().isEmpty()&&!ed_totalRecNo.getText().toString().isEmpty())
                {
                   // mPresenter.SetReturnTime(mPilot.getASSIGN_PILOT_ID());
                    mPresenter.SetOutputDayDriver(ed_totalPrice.getText().toString(),ed_totalRecNo.getText().toString(),mPilot.getASSIGN_PILOT_ID());
                    dialog.dismiss();
                }

            }
        });
        dialog.show();*/
    }
    private void ShowAvailablePilots(Context context, List<String> SelectedTasks) {
        final Dialog dialog = new Dialog(context);
        //set layout custom
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.lst_pilot_name);
        final RecyclerView recView_pilot = (RecyclerView) dialog.findViewById(R.id.recView_pilot);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recView_pilot.setLayoutManager(mLayoutManager);
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.MY_PREFS_TASks, 0);
        List<Pilot>mPilots=DB.GetCoverPilots();
        if(mPilots!=null&&mPilots.size()>0) {
            PilotAdapter pilotAdapter = new PilotAdapter(mOrderAdapter, dialog, context, mPilots, "Assign", SelectedTasks);
            recView_pilot.setAdapter(pilotAdapter);
            dialog.show();
        }
    }
    public static void  AssignTasks(List<String>mTasks,OrderAdapter orderAdapter)
    {
        if(mTasks!=null) {
             mselectedTasks= mTasks;
           mOrderAdapter =orderAdapter;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), HomeWithMenuActivity.class);
        //i.putExtra("mPilot", (Serializable) mPilots.get(getAdapterPosition()));
        startActivity(i);
    }
}
