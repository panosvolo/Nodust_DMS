package com.mts.mea.nodust_app.orders.GetOrders;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import com.mts.mea.nodust_app.Pilots.Pilot;
import com.mts.mea.nodust_app.Pilots.PilotCover.IPilotCover;
import com.mts.mea.nodust_app.Pilots.PilotCover.PilotCoverPresenter;
import com.mts.mea.nodust_app.Pilots.PilotCover.Pilot_Cover;
import com.mts.mea.nodust_app.Pilots.Reconcilation;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.User.Change_Password;
import com.mts.mea.nodust_app.User.Complain_activity;
import com.mts.mea.nodust_app.User.Payroll_activity;
import com.mts.mea.nodust_app.User.User;
import com.mts.mea.nodust_app.User.UserInfo;
import com.mts.mea.nodust_app.User.UserShift.IContractUserShif;
import com.mts.mea.nodust_app.User.UserShift.UserShiftPresenter;
import com.mts.mea.nodust_app.User.User_Profile;
import com.mts.mea.nodust_app.Vacation.Vacationlist_Activity;
import com.mts.mea.nodust_app.common.Constants;
import com.mts.mea.nodust_app.common.DataBaseHelper;
import com.mts.mea.nodust_app.common.GetAddress;
import com.mts.mea.nodust_app.common.langaugeActivity;
import com.mts.mea.nodust_app.orders.SetAssignmentAction.AssigActionPresenter;
import com.mts.mea.nodust_app.orders.SetAssignmentAction.IContractAssignAction;
import com.mts.mea.nodust_app.orders.Task;
import com.mts.mea.nodust_app.products.Collection;
import com.mts.mea.nodust_app.products.PackageInfo;
import com.mts.mea.nodust_app.products.Product;

import java.util.Calendar;
import java.util.List;

public class HomeWithMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IContractGetOrders.View ,View.OnClickListener,IContractUserShif.View, IContractAssignAction.View,IPilotCover.View{
    ViewPager viewPager;
    TabLayout tabLayout;
    List<Task> mTasks;
    IContractGetOrders.Presenter mPresnter;
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferences_Area;
    String UserName;
    UserInfo userInfo;
    private ProgressDialog pbDailog;
    OrdersFragment fragment_tasks;
    OrdersFragment TasksFragment;
    DataBaseHelper DB;
    public Button btn_shift,btn_accept;
    IContractUserShif.Presenter userShiftPresenter;
    IPilotCover.Presenter CoverPresenter;
    public static List<String>SelectedTasks;
    static int postionTab;
    static int postionTab2;
    static  OrderAdapter mOrderAdapter;
    String userName;
    IContractAssignAction.Presenter AssignAction;
    TextView tv_areaName;
    int NoBack=0;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_with_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       tv_areaName=(TextView)findViewById(R.id.tv_areaName);
        toolbar.setTitle(getApplicationContext().getResources().getString(R.string.Home));
        DB=new DataBaseHelper(this);
        btn_shift=(Button)findViewById(R.id.btn_UserShift);
     //  btn_accept=(Button)findViewById(R.id.btn_accept);
        btn_shift.setOnClickListener(this);
       //btn_accept.setOnClickListener(this);
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
           Version.setText(getApplicationContext().getResources().getString(R.string.VersionNo)+":"+String.valueOf(pInfo.versionName));
       } catch (PackageManager.NameNotFoundException e) {
           e.printStackTrace();
       }
       SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.My_Prefs_language_NoDust, 0);
       SharedPreferences sharedPreferences2=getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
         userInfo= ServerManager.deSerializeStringToObject(sharedPreferences2.getString("UserInfo",""),UserInfo.class);
          userName="";
        if (sharedPreferences.contains("SelectedLanguage")) {
            if(sharedPreferences.getString("SelectedLanguage","En").equalsIgnoreCase("English")) {

                UserName.setText(userInfo.getNameEn());
            }
            else
            {
                UserName.setText(userInfo.getNameAr());
            }
        }
        else
        {
            UserName.setText(userInfo.getNameEn());
        }
        if (sharedPreferences2.contains("UserName")) {
            userName = sharedPreferences2.getString("UserName", "");
        }
        NavigationView navigationView1 = (NavigationView) findViewById(R.id.nav_view2);
        navigationView1.setNavigationItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        if (userInfo.getShiftID() == null && userInfo.getStatus() == null) {
            btn_shift.setText(getApplicationContext().getResources().getString(R.string.StartShift));
        } else if (userInfo.getStatus().equalsIgnoreCase("Started")) {
            btn_shift.setText(getApplicationContext().getResources().getString(R.string.EndShift));
            IntiUI ();
        }
        else
        {
            btn_shift.setVisibility(View.GONE);
            IntiUI ();
        }
        String Address = GetAddress.GetAddress(getApplicationContext(), GetCurrentLocaion.CurrentLoc);
        User user = new User(userName, "", GetCurrentLocaion.CurrentLoc, Address);
        userShiftPresenter = new UserShiftPresenter(this, getApplicationContext(), user);

        if(userInfo.getUSERGROUP_ID()==1||userInfo.getUSERGROUP_ID()==3)
        {
            navigationView.getMenu().clear(); //clear old inflated items.
            navigationView.inflateMenu(R.menu.home_menu_driver); //inflate new items.
        }
       else
        {
            if(userInfo.getStatus()!=null)
           // btn_accept.setVisibility(View.VISIBLE);
            navigationView.getMenu().clear(); //clear old inflated items.
            navigationView.inflateMenu(R.menu.home_menu_driver); //inflate new items.
        }

    }

    @Override
    protected void onResume() {

        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (searchView != null && !searchView.isIconified()) {
            searchView.setIconified(true);
            searchView.clearFocus();
            searchView.onActionViewCollapsed();
        }
        NoBack=0;
        sharedPreferences = getApplicationContext().getSharedPreferences(Constants.My_Prefs_language_NoDust, 0);
        if(sharedPreferences.contains("loginToday")) {
            Calendar c=Calendar.getInstance();
            String CurrentDate=c.get(Calendar.DATE)+"/"+c.get(Calendar.MONTH)+"/"+(c.get(Calendar.YEAR));
            if(!sharedPreferences.getString("loginToday","").equalsIgnoreCase(CurrentDate)) {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_TASks, 0);
                sharedPreferences.edit().clear().commit();
                Intent i = new Intent(this, GetCurrentLocaion.class);
                GetCurrentLocaion.FlagGPS = false;
                stopService(i);
                i = new Intent(this, loginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        }
    }

    public void IntiUI() {
        DeleteSharedPrefeence();
        sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        if (sharedPreferences.contains("UserName"))
        {
            UserName=sharedPreferences.getString("UserName", "");
        }
        String Address= GetAddress.GetAddress(getApplicationContext(), GetCurrentLocaion.CurrentLoc);
        User user=new User(UserName,"",GetCurrentLocaion.CurrentLoc,Address);
        mPresnter=new GetOrdersPresenter(this,getApplicationContext(),user);
        CoverPresenter=new PilotCoverPresenter(this,getApplicationContext(),user);
        AssignAction=new AssigActionPresenter(this,getApplicationContext(),user);
        // check user shift if started show tasks else show button start
         sharedPreferences_Area = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);

        if(userInfo.getUSERGROUP_ID()!=2)
        {
            if(!sharedPreferences_Area.contains("AreaName")) {
                List<String> Areas = DB.GetAreas(userName);
                if (Areas.size() > 1) {
                    final Dialog dialog = new Dialog(this);
                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.popup_areas);
                    final Spinner spinner = (Spinner) dialog.findViewById(R.id.sp_areaName);
                    Button saveArea = (Button) dialog.findViewById(R.id.btn_ok);
                    ArrayAdapter AreaAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, Areas);
                    spinner.setAdapter(AreaAdapter);
                    spinner.setSelection(0);
                    saveArea.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sharedPreferences_Area.edit().putString("AreaName",spinner.getSelectedItem().toString()).commit();
                            tv_areaName.setText(sharedPreferences_Area.getString("AreaName",""));
                            // sharedPreferences2.edit().putString("AreaName",spinner.getSelectedItem().toString());
                            dialog.setCancelable(true);
                            dialog.dismiss();
                      //      AddTabs();
                        }
                    });
                    dialog.setCancelable(false);
                    dialog.show();
                } else if (Areas.size() == 1) {
                    sharedPreferences_Area.edit().putString("AreaName",Areas.get(0)).commit();
                    tv_areaName.setText(sharedPreferences_Area.getString("AreaName",""));

                   // AddTabs();
                }
            }
            else
            {
                tv_areaName.setText(sharedPreferences_Area.getString("AreaName",""));

              //  AddTabs();
            }
        }
        else {
           // AddTabs();
            if(!sharedPreferences_Area.contains("AreaName")) {
                List<String> Areas = DB.GetAreasPilots(userName);
                if (Areas.size() > 1) {
                    final Dialog dialog = new Dialog(this);
                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.popup_areas);
                    final Spinner spinner = (Spinner) dialog.findViewById(R.id.sp_areaName);
                    Button saveArea = (Button) dialog.findViewById(R.id.btn_ok);
                    ArrayAdapter AreaAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, Areas);
                    spinner.setAdapter(AreaAdapter);
                    spinner.setSelection(0);
                    saveArea.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sharedPreferences_Area.edit().putString("AreaName",spinner.getSelectedItem().toString()).commit();
                            tv_areaName.setText(sharedPreferences_Area.getString("AreaName",""));
                            // sharedPreferences2.edit().putString("AreaName",spinner.getSelectedItem().toString());
                            dialog.setCancelable(true);
                            dialog.dismiss();
                           // AddTabs();
                        }
                    });
                    dialog.setCancelable(false);
                    dialog.show();
                } else if (Areas.size() == 1) {
                    sharedPreferences_Area.edit().putString("AreaName",Areas.get(0)).commit();
                    tv_areaName.setText(sharedPreferences_Area.getString("AreaName",""));

                  //  AddTabs();
                }
            }
            else
            {
                tv_areaName.setText(sharedPreferences_Area.getString("AreaName",""));

               // AddTabs();
            }
        }
        AddTabs();
      /*  mPresnter.setTasks();*/
       // AddTabs();
    }

    @Override
    public void onBackPressed() {
        if(NoBack>=1) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }
            DeleteSharedPrefeence();

            //   DB.DeletAll();
            Intent i = new Intent(this, loginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
        else
        {
            Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.PressAgain),Toast.LENGTH_SHORT).show();
            NoBack++;
        }
    }

    private void DeleteSharedPrefeence() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_TASks, 0);
        if(sharedPreferences.contains("TASKS"))
        {
            sharedPreferences.edit().remove("TASKS").commit();
        }
        if(sharedPreferences.contains("Driver_TASKS"))
        {
            sharedPreferences.edit().remove("Driver_TASKS").commit();
        }
        if(sharedPreferences.contains("Pilots"))
        {
            sharedPreferences.edit().remove("Pilots").commit();
        }
        if(sharedPreferences.contains("driver_Products"))
        {
            sharedPreferences.edit().remove("driver_Products").commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //   Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_with_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final MenuItem AssignTask=menu.findItem(R.id.action_assign_task);
        final MenuItem action_all_assign_task=menu.findItem(R.id.action_all_assign_task);


        if(userInfo.getUSERGROUP_ID()==2)
        {
            AssignTask.setVisible(false);
            action_all_assign_task.setVisible(false);
        }

        if(postionTab==1)
        {
            AssignTask.setVisible(true);
           postionTab=0;
        }
      /*  action_all_assign_task.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                TasksFragment.SetAllSelection();
                return false;
            }
        });*/
        if (searchItem != null) {

             searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);
            searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return true;
                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    /*
                   if(userInfo.getUSERGROUP_ID()==2) { // pilot
                       if(postionTab2==0)
                       TasksFragment.Search(query);
                   }
                    else
                   {
                       if(postionTab2==1)
                           TasksFragment.Search(query);
                   }*/
                    TasksFragment.Search(query);

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                  //  if(userInfo.getUSERGROUP_ID()==2)
                   /* if(userInfo.getUSERGROUP_ID()==2) { // pilot
                        if (postionTab2 == 0)
                            TasksFragment.Search(newText);
                    }
                    else {
                        if (postionTab2 == 1)
                            TasksFragment.Search(newText);
                    }
*/
                    TasksFragment.Search(newText);
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
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
           if(userInfo.getStatus()!=null)
            DB.UpdateAllData(this,true);
        }
        else if(id==R.id.action_vacation)
        {
            Intent i=new Intent(this, Vacationlist_Activity.class);
            startActivity(i);
        }
        else if(id==R.id.action_assign_task)
        {
            if(SelectedTasks!=null)
            {
                ShowAvailablePilots(HomeWithMenuActivity.this,SelectedTasks);
               // SelectedTasks=null;
            }
        }
        else if(item.getItemId()==R.id.action_all_assign_task)
        {
            TasksFragment.SetAllSelection();
        }

      /*  else if(id==R.id.src_cardNum)
        {
            srcView.setFocusableInTouchMode(true);
            srcView.setVisibility(View.VISIBLE);
            srcView.setQuery("",false);
            srcView.requestFocus();
            srcView.setQueryHint(getApplicationContext().getResources().getString(R.string.EnterCardNum));
            SearchKey="CardNo";
        }
        else if(id==R.id.src_comments)
        {
            srcView.setQuery("",false);
            srcView.setFocusableInTouchMode(true);
            srcView.setVisibility(View.VISIBLE);
            srcView.requestFocus();
            srcView.setQueryHint(getApplicationContext().getResources().getString(R.string.EnterComments));
            SearchKey="Comments";
        }
        else if(id==R.id.src_custAddress)
        {
            srcView.setFocusableInTouchMode(true);
            srcView.setVisibility(View.VISIBLE);
            srcView.requestFocus();
            srcView.setQuery("",false);
            srcView.setQueryHint(getApplicationContext().getResources().getString(R.string.EnterCustAddr));
            SearchKey="CustAddress";
        }
        else if(id==R.id.src_custName)
        {
            srcView.setFocusableInTouchMode(true);
            srcView.setVisibility(View.VISIBLE);
            srcView.requestFocus();
            srcView.setQuery("",false);
            srcView.setQueryHint(getApplicationContext().getResources().getString(R.string.EnterCustName));
            SearchKey="CustName";
        }*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_settings) {
            // Handle setting
            Intent i=new Intent(this,langaugeActivity.class);
            i.putExtra("page","home");
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
        else if(id==R.id.action_Profile)
        {
            Intent i=new Intent(this, User_Profile.class);
            startActivity(i);
        }
        else if(id==R.id.PayRoll)
        {
            Intent i=new Intent(this, Payroll_activity.class);
            startActivity(i);
        }

        else if(id==R.id.action_complain)
        {
            Intent i=new Intent(this, Complain_activity.class);
            startActivity(i);
        }
        else if (id == R.id.logout) {
           /* SharedPreferences settings = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
            settings.edit().clear().commit();
            settings=getApplicationContext().getSharedPreferences(Constants.My_Prefs_language_NoDust,0);
            settings.edit().clear().commit();
            settings=getApplicationContext().getSharedPreferences(Constants.MY_PREFS_closeCode,0);
            settings.edit().clear().commit();
            settings=getApplicationContext().getSharedPreferences(Constants.MY_PREFS_closeCodeGroup,0);
            settings.edit().clear().commit();
            settings=getApplicationContext().getSharedPreferences(Constants.GroupDefault,0);
            settings.edit().clear().commit();*/
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_TASks, 0);
            sharedPreferences.edit().clear().commit();
            Intent i=new Intent(this,GetCurrentLocaion.class);
            GetCurrentLocaion.FlagGPS=false;
            stopService(i);
             i = new Intent(this, loginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
        else if(id==R.id.ChangePassword)
        {
            Intent i = new Intent(this, Change_Password.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

        }
        else if(id==R.id.action_Refresh)
        {
            if(userInfo.getStatus()!=null)

                DB.UpdateAllData(this,true);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void AddTabs() {
       if(userInfo.getUSERGROUP_ID()==1||userInfo.getUSERGROUP_ID()==3)
       {
           // driver
           viewPager = (ViewPager) findViewById(R.id.viewpager);
           setupViewPager(viewPager);
           tabLayout = (TabLayout) findViewById(R.id.tabs);
           tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
           tabLayout.setupWithViewPager(viewPager);
           tabLayout.getTabAt(0).setIcon(getApplicationContext().getResources().getDrawable(R.drawable.ic_new_pilot)); // pilots

           /*ImageView imageView = new ImageView(getApplicationContext());
           imageView.setImageResource(R.drawable.ic_pilot_white);
           imageView.setMinimumWidth(60);*/
           //tabLayout.getTabAt(1).setCustomView(imageView); // pilots
           tabLayout.getTabAt(1).setIcon(getApplicationContext().getResources().getDrawable(R.drawable.ic_tasks_white)); // pilots

           tabLayout.getTabAt(2).setIcon(getApplicationContext().getResources().getDrawable(R.drawable.ic_product_white)); // products
           tabLayout.getTabAt(3).setIcon(getApplicationContext().getResources().getDrawable(R.drawable.ic_tasks_white));
           tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
               @Override
               public void onTabSelected(TabLayout.Tab tab) {
                   postionTab=tab.getPosition();
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

        else {

           viewPager = (ViewPager) findViewById(R.id.viewpager);
           setupViewPager(viewPager);
           tabLayout = (TabLayout) findViewById(R.id.tabs);
          // tabLayout.setTabMode(TabLayout.MODE_FIXED);
           tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
           tabLayout.setupWithViewPager(viewPager);
           tabLayout.getTabAt(0).setIcon(getApplicationContext().getResources().getDrawable(R.drawable.ic_tasks_white));
           tabLayout.getTabAt(1).setIcon(getApplicationContext().getResources().getDrawable(R.drawable.ic_product_white)); // products
           tabLayout.getTabAt(2).setIcon(getApplicationContext().getResources().getDrawable(R.drawable.ic_product_white)); // products
           tabLayout.getTabAt(3).setIcon(getApplicationContext().getResources().getDrawable(R.drawable.ic_tasks_white));
           tabLayout.getTabAt(4).setIcon(getApplicationContext().getResources().getDrawable(R.drawable.ic_tasks_white));

           tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
               @Override
               public void onTabSelected(TabLayout.Tab tab) {
                   postionTab=tab.getPosition();
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
    }

    private void setupViewPager(ViewPager viewPager) {
        if(userInfo.getUSERGROUP_ID()==1||userInfo.getUSERGROUP_ID()==3)
        {    String Type="";
            Bundle fragmentType=new Bundle();

            // 2 tabs for products and pilots
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            fragment_tasks = new OrdersFragment();
            fragmentType=new Bundle();
            fragmentType.putString("Type","Pilot");
            fragment_tasks.setArguments(fragmentType);
            adapter.addFragment(fragment_tasks, getApplicationContext().getResources().getString(R.string.Pilots));


            fragment_tasks = new OrdersFragment();
            fragmentType=new Bundle();
            fragmentType.putString("Type","driver_tasks");
            fragment_tasks.setArguments(fragmentType);
            adapter.addFragment(fragment_tasks, getApplicationContext().getResources().getString(R.string.action_Tasks));
            TasksFragment=fragment_tasks;

            fragment_tasks = new OrdersFragment();
            fragmentType=new Bundle();
            fragmentType.putString("Type","Product");
            fragment_tasks.setArguments(fragmentType);
            adapter.addFragment(fragment_tasks, getApplicationContext().getResources().getString(R.string.Products));

            DashBoard_fragment dashBoard_fragment=new DashBoard_fragment();

            adapter.addFragment(dashBoard_fragment, getApplicationContext().getResources().getString(R.string.TodayOutput));

            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(1);
            postionTab2=1;

        }
        else
        {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            fragment_tasks = new OrdersFragment();
            Bundle fragmentType=new Bundle();
            fragmentType.putString("Type","tasks");
            fragment_tasks.setArguments(fragmentType);
            adapter.addFragment(fragment_tasks, getApplicationContext().getResources().getString(R.string.action_Tasks));
            TasksFragment=fragment_tasks;
            Pilot mpilot=new Pilot();
            mpilot.setASSIGN_PILOT_ID(userInfo.getAss_ID());
            mpilot.setNameEn(userInfo.getNameEn());
            mpilot.setNameAr(userInfo.getNameAr());
            mpilot.setPilotID(userName);

            OrdersFragment products_pilot=new OrdersFragment();
            fragmentType=new Bundle();
            fragmentType.putString("Type","CoverProduct_Pilot");
            products_pilot.setArguments(fragmentType);
            adapter.addFragment(products_pilot, getApplicationContext().getResources().getString(R.string.Products));
            OrdersFragment products_pilot2=new OrdersFragment();
            fragmentType=new Bundle();
            fragmentType.putString("Type","CheckCoverProduct_Pilot");
            products_pilot2.setArguments(fragmentType);
            adapter.addFragment(products_pilot2, getApplicationContext().getResources().getString(R.string.PilotAccept));

            new_DashBoard Dashboard = new new_DashBoard();
            fragmentType=new Bundle();
            fragmentType.putSerializable("Pilot",mpilot);
            Dashboard.setArguments(fragmentType);
            adapter.addFragment(Dashboard,getApplicationContext().getResources().getString(R.string.TodayOutput));

            OrdersFragment products_pilot3=new OrdersFragment();
            fragmentType=new Bundle();
            fragmentType.putString("Type","ReconcilationProduct_Pilot");
            products_pilot3.setArguments(fragmentType);
            adapter.addFragment(products_pilot3, getApplicationContext().getResources().getString(R.string.Reconcilation));
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(0);
            postionTab2=0;
        }
    }

    @Override
    public void setTasks(List<Task> Tasks) {
       /* if(Tasks!=null)
        {
            mTasks=Tasks;
            // save Tasks
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_TASks, 0);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            sharedPreferencesEditor.putString("TASKS",ServerManager.serializeObjectToString(Tasks));
            sharedPreferencesEditor.apply();
            AddTabs();
            DB.AddContract(Tasks);
            mPresnter.setProducts();
        }*/


    }

    @Override
    public void showError(String error) {
        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void setProducts(List<Product> products) {

        //DB.AddProducts(products,UserName);
    }

    @Override
    public void setCoverProducts(List<Product> products) {

    }

    @Override
    public void setPilots(List<Pilot> Pilots) {

    }

    @Override
    public void setTotalAmount(String TotalAmount) {

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
    public void showMsg(String Msg) {
  Toast.makeText(getApplicationContext(),Msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ShowLoading() {
        pbDailog = new ProgressDialog(HomeWithMenuActivity.this);
        pbDailog.setMessage("جاري التحميل .....");
        pbDailog.setCancelable(false);
        pbDailog.show();
    }

    @Override
    public void HideLoading() {
        if(pbDailog!=null)
            pbDailog.dismiss();
    }

    @Override
    public void StopLoading() {
        if(pbDailog!=null)
            pbDailog.dismiss();    }

    @Override
    public void setCollection(List<Collection> products) {

    }

    @Override
    public void SendSms() {

    }

    @Override
    public boolean CheckCashing() {
        return false;
    }

    @Override
    public void SetCoverPilot(List<Pilot_Cover> lst_cover) {
        DB.AddCoverPilot(lst_cover,true);
            IntiUI();

    }

    @Override
    public void SuccessPilotAccept() {
        CoverPresenter.GetcoverPilot();
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
        CoverPresenter.GetcoverPilot();
    }

    @Override
    public void SetReconcilationRequests(List<Reconcilation> lst_reconcilation) {

    }

    @Override
    public void RefreshQTYReconcilation() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        Button c=(Button)v;
        if(c.getText().toString().equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.StartShift)))
        {
            userShiftPresenter.StartShift();

        }
        else if(c.getText().toString().equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.EndShift)))
        {
           // userShiftPresenter.EndShift(userInfo.getShiftID());
           /* SharedPreferences sharedPreferences2=getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
            userInfo.setStatus("Ended");
            sharedPreferences2.edit().putString("UserInfo",ServerManager.serializeObjectToString(userInfo)).apply();
            if(userInfo.getUSERGROUP_ID()==2)
            {
                final Dialog dialog = new Dialog(HomeWithMenuActivity.this);
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
                            userShiftPresenter.SetOutputDayPilot(ed_totalPrice.getText().toString(),ed_totalRecNo.getText().toString(),userInfo.getAss_ID());
                            dialog.dismiss();
                        }

                    }
                });
                if(userInfo.getAss_ID()!=null&!userInfo.getAss_ID().isEmpty())
                    dialog.show();
            }*/
            userShiftPresenter.EndShift(userInfo.getShiftID());
        }
        else if(c.getText().toString().equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.PilotAccept)))
        {
           // CoverPresenter.Pilotaccept();
        }
    }

    @Override
    public void ShowMsg(String type,String msg) {
        if(msg.equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.SaveChanges)+"End"))
        {
            userShiftPresenter.EndShift(userInfo.getShiftID());
            msg=getApplicationContext().getResources().getString(R.string.SaveChanges);
        }
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void ShowMsg(String msg) {
        if(msg.equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.SaveChanges)+"End"))
        {
            userShiftPresenter.EndShift(userInfo.getShiftID());
            msg=getApplicationContext().getResources().getString(R.string.SaveChanges);
        }
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void SendAction() {

    }

    @Override
    public void Startbtn(String ShiftId) {
        btn_shift.setText(getApplicationContext().getResources().getString(R.string.EndShift));
    //    btn_accept.setVisibility(View.VISIBLE);
        final SharedPreferences sharedPreferences2=getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        userInfo.setStatus("Started");
        userInfo.setShiftID(ShiftId);
        sharedPreferences2.edit().putString("UserInfo",ServerManager.serializeObjectToString(userInfo)).apply();
        // CHECK IF DRIVER OR PILOT DRIVER FIRST SELECT AREA FIRST IF HE HAS MULTIPLE AREA
         IntiUI();
        //mPresnter.setTasks();

    }

    @Override
    public void Endbtn() {
        btn_shift.setVisibility(View.GONE);
        SharedPreferences sharedPreferences2=getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        userInfo.setStatus("Ended");
        sharedPreferences2.edit().putString("UserInfo",ServerManager.serializeObjectToString(userInfo)).apply();

    }

    @Override
    public void disableCheck() {

    }

    @Override
    public void disableCheckReturn() {

    }
    public static void AssignTasks(List<String>mTasks,OrderAdapter orderAdapter)
    {
        if(mTasks!=null) {
            SelectedTasks = mTasks;
            mOrderAdapter=orderAdapter;
        }


    }
    private void ShowAvailablePilots(  Context context, List<String> SelectedTasks) {
         Dialog dialog = new Dialog(context);
        //set layout custom
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.lst_pilot_name);
        final RecyclerView recView_pilot = (RecyclerView) dialog.findViewById(R.id.recView_pilot);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recView_pilot.setLayoutManager(mLayoutManager);
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.MY_PREFS_TASks, 0);
        List<Pilot>mPilot=DB.GetCoverPilots();
        if(mPilot!=null&&mPilot.size()>0) {
            PilotAdapter pilotAdapter = new PilotAdapter(mOrderAdapter, dialog, context, mPilot, "Assign", SelectedTasks);
            recView_pilot.setAdapter(pilotAdapter);
            dialog.show();
        }
    }


}
