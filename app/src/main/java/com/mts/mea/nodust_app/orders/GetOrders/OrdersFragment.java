package com.mts.mea.nodust_app.orders.GetOrders;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.mts.mea.nodust_app.Adapters.CloseCodeAdapter;
import com.mts.mea.nodust_app.Adapters.OrderAdapter;
import com.mts.mea.nodust_app.Adapters.PilotAdapter;
import com.mts.mea.nodust_app.Adapters.ProductAdapter;
import com.mts.mea.nodust_app.BackgroundServices.GetCurrentLocaion;
import com.mts.mea.nodust_app.Evalution.KPI;
import com.mts.mea.nodust_app.Interfaces.ServerManager;
import com.mts.mea.nodust_app.OFFLINE.IContractOffline;
import com.mts.mea.nodust_app.OFFLINE.OFFLINE;
import com.mts.mea.nodust_app.OFFLINE.OfflinePresenter;
import com.mts.mea.nodust_app.Pilots.Pilot;
import com.mts.mea.nodust_app.Pilots.PilotCover.IPilotCover;
import com.mts.mea.nodust_app.Pilots.PilotCover.PilotCoverPresenter;
import com.mts.mea.nodust_app.Pilots.PilotCover.Pilot_Cover;
import com.mts.mea.nodust_app.Pilots.Pilot_Activity;
import com.mts.mea.nodust_app.Pilots.Reconcilation;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.Street;
import com.mts.mea.nodust_app.User.User;
import com.mts.mea.nodust_app.User.UserInfo;
import com.mts.mea.nodust_app.User.UserShift.IContractUserShif;
import com.mts.mea.nodust_app.common.Constants;
import com.mts.mea.nodust_app.common.DataBaseHelper;
import com.mts.mea.nodust_app.common.GetAddress;
import com.mts.mea.nodust_app.orders.SetAssignmentAction.AssigActionPresenter;
import com.mts.mea.nodust_app.orders.SetAssignmentAction.IContractAssignAction;
import com.mts.mea.nodust_app.orders.Task;
import com.mts.mea.nodust_app.products.Collection;
import com.mts.mea.nodust_app.products.PackageInfo;
import com.mts.mea.nodust_app.products.Product;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment implements IContractGetOrders.View,IContractAssignAction.View,View.OnClickListener,IPilotCover.View, IContractOffline.View {
    RecyclerView recyclerView;
    // SwipeRefreshLayout swipRefresh;
    List<Task>mTasks;
    String UserName;
    IContractAssignAction.Presenter mpresnter;
    IContractUserShif.Presenter userShiftPresenter;
    private ProgressDialog pbDailog;
    //public Button btn_shift;
    IContractGetOrders.Presenter mPresnter;
    UserInfo userInfo;
    boolean flag=false;
    OrderAdapter orderAdapter;
    ProductAdapter productAdapter;
    PilotAdapter pilotAdapter;
    DataBaseHelper DB;
    String Type="";
    Pilot mPilot;
    TableLayout tab_product;
    ScrollView scr_product;
    Button btn_accept,btn_reject,btn_reconcilation;
    IPilotCover.Presenter pilotCoverPrsenter;
    LinearLayout ll_actions2;
    EditText ed_ExpCash,ed_PilotCash,ed_DriverCash;
    int i=0;
    List<Pilot_Cover>lst_Cover;
    LinearLayout ll_street;
    android.support.v7.widget.AppCompatSpinner sp_street;
    private List<Street> lst_streets;
    String filterName="";
    private SharedPreferences sharedPreferences_streetName;

    public OrdersFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            HomeWithMenuActivity.SelectedTasks=null;
            Pilot_Activity.mselectedTasks=null;
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_orders, container, false);
        tab_product=(TableLayout)view.findViewById(R.id.tab_product);
        recyclerView=(RecyclerView)view.findViewById(R.id.recView);
        scr_product=(ScrollView)view.findViewById(R.id.scr_product);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        btn_accept=(Button)view.findViewById(R.id.btn_accept);
        btn_reject=(Button)view.findViewById(R.id.btn_reject);
        btn_reconcilation=(Button)view.findViewById(R.id.btn_reconcilation);
        ll_actions2=(LinearLayout)view.findViewById(R.id.ll_actions_2);
        ed_ExpCash=(EditText)view.findViewById(R.id.expCash);
        ed_DriverCash=(EditText)view.findViewById(R.id.driverCash);
        ed_PilotCash=(EditText)view.findViewById(R.id.pilotcash);
        btn_reject.setOnClickListener(this);
        btn_accept.setOnClickListener(this);
        btn_reconcilation.setOnClickListener(this);
        ll_street=(LinearLayout)view.findViewById(R.id.ll_street);
        sp_street=(android.support.v7.widget.AppCompatSpinner)view.findViewById(R.id.sp_streetname);

        //  swipRefresh=(android.support.v4.widget.SwipeRefreshLayout)view.findViewById(R.id.ll_swip);
        // swipRefresh.setRefreshing(false);
        //btn_shift=(Button)view.findViewById(R.id.btn_UserShift);
        DB=new DataBaseHelper(getContext());
        Type=getArguments().getString("Type");
        return view;
    }

    private void InitUI() {
        tab_product.setVisibility(View.GONE);
        scr_product.setVisibility(View.GONE);
        btn_accept.setVisibility(View.GONE);
        btn_reject.setVisibility(View.GONE);
        btn_reconcilation.setVisibility(View.GONE);
        ll_actions2.setVisibility(View.GONE);
        // btn_shift.setOnClickListener(this);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        if (sharedPreferences.contains("UserName")) {
            UserName = sharedPreferences.getString("UserName", "");
        }
        String Address = GetAddress.GetAddress(getContext(), GetCurrentLocaion.CurrentLoc);
        User user = new User(UserName, "", GetCurrentLocaion.CurrentLoc, Address);
        mpresnter = new AssigActionPresenter(this, getContext(), user);
        mPresnter = new GetOrdersPresenter(this, getContext(), user);
        pilotCoverPrsenter=new PilotCoverPresenter(this,getActivity(),user);
        // userShiftPresenter = new UserShiftPresenter(this, getContext(), user);
        SharedPreferences sharedPreferences2 = getContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        userInfo = ServerManager.deSerializeStringToObject(sharedPreferences2.getString("UserInfo", ""), UserInfo.class);

        if (Type.equalsIgnoreCase("Product")) {
            GetProducts();
        } else if (Type.equalsIgnoreCase("Pilot")) {
            GetPilots();


        } else if(Type.equalsIgnoreCase("pilot_Product")){
            // products of contracts
            // btn_shift.setVisibility(View.GONE);
            mPilot=(Pilot)getArguments().get("pilot");
            GetProductsPilot(mPilot.getPilotID());

        }
        else if(Type.equalsIgnoreCase("pilot_tasks"))
        {
            mPilot=(Pilot)getArguments().get("pilot");
            filterName=mPilot.getPilotID();
            GetPilotTasks(mPilot.getPilotID());
        }
        else if(Type.equalsIgnoreCase("driver_tasks"))
        {
            GetNotAssignTask();

        }
        else if (Type.equalsIgnoreCase("CoverProduct_Pilot"))
        {
            GetCoverProductsPilot();
        }
        else if(Type.equalsIgnoreCase("CheckCoverProduct_Pilot"))
        {
            CheckCoverProductsPilot();
        }
        else if(Type.equalsIgnoreCase("ReconcilationProduct_Pilot"))
        {
            CheckReconcilation();
        }
        else {
            GetTasks();
        }
        sp_street.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    if (lst_streets != null && lst_streets.size() > position-1)
                        //Toast.makeText(getContext().getApplicationContext(), lst_streets.get(position-1).getStreetName(), Toast.LENGTH_SHORT).show();
                        //Refresh
                        orderAdapter = new OrderAdapter(getContext(), DB.GetTasksPilot_street(filterName, lst_streets.get(position-1).getStreetName()), mpresnter, Type);
                    recyclerView.setAdapter(orderAdapter);
                    sharedPreferences_streetName.edit().putString("StreetName",lst_streets.get(position-1).getStreetName()).commit();



                }
                else
                {
                    orderAdapter = new OrderAdapter(getContext(), DB.GetTasksPilot(filterName), mpresnter, Type);
                    recyclerView.setAdapter(orderAdapter);
                    sharedPreferences_streetName.edit().putString("StreetName","no").commit();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void CheckReconcilation() {
        List<Pilot_Cover>lst=DB.checkreconcilation(UserName);
        if(lst!=null&&lst.size()>0) {
            //  btn_reject.setVisibility(View.VISIBLE);
            if(lst.get(i).getDriver_Accept()==null||!lst.get(i).getDriver_Accept().equalsIgnoreCase("true"))
                btn_reconcilation.setVisibility(View.VISIBLE);
            tab_product.setVisibility(View.VISIBLE);
            scr_product.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            ll_actions2.setVisibility(View.VISIBLE);
            ed_PilotCash.setEnabled(true);
            ed_ExpCash.setEnabled(false);
            ed_DriverCash.setEnabled(false);
            if(lst.get(i).getTOTAL_RECIEPT_AMOUNT_PILOT()==null||lst.get(i).getTOTAL_RECIEPT_AMOUNT_PILOT().isEmpty())
                ed_PilotCash.setText("0");
            else {
                //ed_PilotCash.setText(String.valueOf(lst.get(i).getTOTAL_RECIEPT_AMOUNT_PILOT()));
                ed_PilotCash.setText(String.valueOf("0"));

            }
            if(lst.get(i).getTOTAL_RECIEPT_AMOUNT_DRIVER()==null||lst.get(i).getTOTAL_RECIEPT_AMOUNT_DRIVER().isEmpty())
                ed_DriverCash.setText("0");
            else
                ed_DriverCash.setText(String.valueOf(lst.get(i).getTOTAL_RECIEPT_AMOUNT_DRIVER()));
            String ExpCash=String.valueOf(  DB.GetTotalPilot(UserName));
            ed_ExpCash.setText(ExpCash);
            AddreconcilationCoverProducts(lst);
        }
    }


    private void CheckCoverProductsPilot() {
        List<Pilot_Cover>lst=DB.CheckAcceptenceCoverPilot(UserName);
        lst_Cover=lst;
        if(lst!=null&&lst.size()>0) {
            btn_reject.setVisibility(View.VISIBLE);
            btn_accept.setVisibility(View.VISIBLE);
            tab_product.setVisibility(View.VISIBLE);
            scr_product.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            AddCoverPilotProducts(lst);
        }

    }

    private void GetCoverProductsPilot() {
        List<Pilot_Cover>lst=DB.GetCoverPilot(UserName);
        if(lst!=null&&lst.size()>0) {
            tab_product.setVisibility(View.VISIBLE);
            scr_product.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            AddCoverPilotProducts(lst);
        }

    }

    private void GetPilotTasks(String pilotID) {
        filterName=pilotID;
        List<Task>Contracts=DB.GetTasksPilot(pilotID);
        if(Contracts!=null) {
            mTasks=Contracts;
            ll_street.setVisibility(View.VISIBLE);
            lst_streets=DB.GetStreetsPilot(pilotID);
            GetStreets(lst_streets);
            orderAdapter = new OrderAdapter(getContext(), Contracts, mpresnter, Type);
            recyclerView.setAdapter(orderAdapter);
            // Toast.makeText(getActivity().getApplicationContext(),DB.GetALLTasksPilot2(pilotID),Toast.LENGTH_SHORT).show();
        }
    }
    public  void GetStreets(List<Street>lst_streets)
    {
        ArrayList<String> data_spiner = new ArrayList<>();
        if (lst_streets != null) {
            data_spiner.add(getContext().getResources().getString(R.string.ChooseStreet));
            for (int j = 0; j<lst_streets.size(); j++) {
                //  if(isProbablyArabic(lst_streets.get(j).getStreetName()))
                data_spiner.add(String.valueOf(lst_streets.get(j).getStreetName()+" ==> ("+String.valueOf(lst_streets.get(j).getCount())+')'));
                //else
                // data_spiner.add(String.valueOf(lst_streets.get(j).getCount()+" & "+String.valueOf(lst_streets.get(j).getStreetName())));

                // data_spiner.add(String.valueOf(lst_streets.get(j).getStreetName()));

            }
        }
        CloseCodeAdapter adapter=new CloseCodeAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, data_spiner);
        sp_street.setAdapter(adapter);
        sharedPreferences_streetName = getContext().getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        if(sharedPreferences_streetName.contains("StreetName")&& !sharedPreferences_streetName.getString("StreetName","").equalsIgnoreCase("no"))
        {
            for(int i=0;i<lst_streets.size();i++)
            {
                if(lst_streets.get(i).getStreetName()!=null&&lst_streets.get(i).getStreetName().equalsIgnoreCase(sharedPreferences_streetName.getString("StreetName","")))
                {
                    sp_street.setSelection(i+1);
                    break;
                }
            }
        }
    }
    public static boolean isProbablyArabic(String s) {
        s=s.replace(" ","");
        // for (int i = 0; i < s.length();) {
        int c = s.codePointAt(0);
        if (c >= 0x0600 && c <= 0x06E0)
            return true;
        //    i += Character.charCount(c);
        // }
        return false;
    }

    private void GetProductsPilot(String pilotID) {
        List<Task>Contracts=DB.GetTasksPilot(pilotID);
        String []Contract_id=new String[Contracts.size()];
        for (int i=0;i<Contracts.size();i++)
        {
            Contract_id[i]=Contracts.get(i).getAID();
        }
        List<Product>Products=DB.GetCoverProduct(Contract_id);
        if(Products!=null) {
            tab_product.setVisibility(View.VISIBLE);
            scr_product.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            AddProducts(Products);
            //    productAdapter = new ProductAdapter(getContext(), Products, this);
            //  recyclerView.setAdapter(productAdapter);
        }
    }

    private void AddProducts(List<Product> products) {
        tab_product.removeAllViews();
        TableRow inflate = new TableRow(getActivity());
        inflate = (TableRow) View.inflate(getActivity(), R.layout.row_product_header, null);
        tab_product.addView(inflate);
        for(int i=0;i<products.size();i++)
            AddRowProduct(products.get(i));

    }
    private void AddRowProduct(Product product) {
        TableRow inflate = new TableRow(getActivity());
        inflate = (TableRow) View.inflate(getActivity(), R.layout.row_product, null);
        tab_product.addView(inflate);
        LinearLayout linearLayout_0 = (LinearLayout) inflate.getChildAt(0);
        TextView ProductName=(TextView) linearLayout_0.getChildAt(0);
        TextView QuantityClean=(TextView) linearLayout_0.getChildAt(2);
        TextView Treatment=(TextView)linearLayout_0.getChildAt(1);
        TextView CurrentQuantity=(TextView)linearLayout_0.getChildAt(3) ;
        TextView DirtyQuantity=(TextView)linearLayout_0.getChildAt(4) ;
        Treatment.setText(product.getTreatment_description());
        ProductName.setText(product.getNAME());
        QuantityClean.setText(product.getQUNTITY());
        CurrentQuantity.setText(String.valueOf(product.getCurrentQuantity()));
        DirtyQuantity.setText(String.valueOf(product.getDirtyQTY()));
    }
    private void AddCoverPilotProducts(List<Pilot_Cover> products) {
        tab_product.removeAllViews();
        TableRow inflate = new TableRow(getActivity());
        inflate = (TableRow) View.inflate(getActivity(), R.layout.row_product_header, null);
        tab_product.addView(inflate);
        for(int i=0;i<products.size();i++)
            AddRowCoverProduct(products.get(i));

    }
    private void AddreconcilationCoverProducts(List<Pilot_Cover> products) {
        tab_product.removeAllViews();
        TableRow inflate = new TableRow(getActivity());
        inflate = (TableRow) View.inflate(getActivity(), R.layout.row_header_product_reconcilation_pilot, null);
        LinearLayout linearLayout_0 = (LinearLayout) inflate.getChildAt(0);
        TextView ProductName=(TextView) linearLayout_0.getChildAt(0);
        TextView ActualClean=(TextView) linearLayout_0.getChildAt(4);
        TextView Treatment=(TextView)linearLayout_0.getChildAt(1);
        TextView ExpClean=(TextView)linearLayout_0.getChildAt(2);
        TextView expDirty=(TextView)linearLayout_0.getChildAt(3);
        TextView ActualDirty=(TextView) linearLayout_0.getChildAt(5) ;
        TextView ApprovedDirty=(TextView) linearLayout_0.getChildAt(7) ;
        TextView ApprovedClean=(TextView) linearLayout_0.getChildAt(6) ;
        ActualClean.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.solid_black));
        ActualDirty.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.solid_black));

        tab_product.addView(inflate);
        for(int i=0;i<products.size();i++)
            AddRowReconcilation(products.get(i));

    }

    private void AddRowReconcilation(Pilot_Cover product) {
        TableRow inflate = new TableRow(getActivity());
        inflate = (TableRow) View.inflate(getActivity(), R.layout.row_product_reconcliation, null);
        tab_product.addView(inflate);
        LinearLayout linearLayout_0 = (LinearLayout) inflate.getChildAt(0);
        TextView ProductName=(TextView) linearLayout_0.getChildAt(0);
        EditText ActualClean=(EditText) linearLayout_0.getChildAt(4);
        TextView Treatment=(TextView)linearLayout_0.getChildAt(1);
        TextView ExpClean=(TextView)linearLayout_0.getChildAt(2);
        TextView expDirty=(TextView)linearLayout_0.getChildAt(3);
        final EditText ActualDirty=(EditText) linearLayout_0.getChildAt(5) ;
        EditText ApprovedDirty=(EditText) linearLayout_0.getChildAt(7) ;
        EditText ApprovedClean=(EditText) linearLayout_0.getChildAt(6) ;
        Treatment.setText(product.getTreatment_Name());
        ProductName.setText(product.getName());
        if(product.getActual_Clean()!=null&&!product.getActual_Clean().isEmpty()) {
          //  ActualClean.setText(product.getActual_Clean());
            ActualClean.setText("0");

        }
        else
            ActualClean.setText(String.valueOf(product.getCurrent_Quantity()));
        if(product.getActual_Dirty()!=null&&!product.getActual_Dirty().isEmpty()) {
           // ActualDirty.setText(product.getActual_Dirty());
            ActualDirty.setText("0");

        }
        else
            ActualDirty.setText(String.valueOf(product.getDirty_Qty()));

        ApprovedClean.setText(product.getApproved_clean());
        ExpClean.setText(String.valueOf(product.getCurrent_Quantity()));
        expDirty.setText(String.valueOf(product.getDirty_Qty()));
        ApprovedDirty.setText(product.getApproved_Dirty());
        ApprovedClean.setEnabled(false);
        ApprovedDirty.setEnabled(false);
        ActualClean.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.solid_black));
        ActualDirty.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.solid_black));
        final TableRow finalInflate = inflate;
        ActualDirty.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO||actionId == EditorInfo.IME_ACTION_NEXT) {
                    // ApprovedDirty.requestFocus();
                    int rowIndex = tab_product.indexOfChild(finalInflate)+1;
                    Log.d("rowIndexDirty", String.valueOf(rowIndex));
                    if(rowIndex<tab_product.getChildCount()) {
                        TableRow row1 = (TableRow) tab_product.getChildAt(rowIndex);
                        if (row1 != null) {
                            EditText et = (EditText) ((LinearLayout) row1.getChildAt(0)).getChildAt(4);
                            Log.d("rowIndexDirty", et.getText().toString());
                            et.requestFocus();
                            return true;

                            //   Toast.makeText(getContext().getApplicationContext(),et.getText().toString(),Toast.LENGTH_SHORT).show();

                        }
                    }
                    else
                    {
                        if(ed_PilotCash!=null) {
                            ed_PilotCash.requestFocus();
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        ActualClean.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    ActualDirty.requestFocus();
                }
                return false;
            }
        });

    }

    private void AddRowCoverProduct(Pilot_Cover product) {
        TableRow inflate = new TableRow(getActivity());
        inflate = (TableRow) View.inflate(getActivity(), R.layout.row_product, null);
        tab_product.addView(inflate);
        LinearLayout linearLayout_0 = (LinearLayout) inflate.getChildAt(0);
        TextView ProductName=(TextView) linearLayout_0.getChildAt(0);
        TextView QuantityClean=(TextView) linearLayout_0.getChildAt(2);
        TextView Treatment=(TextView)linearLayout_0.getChildAt(1);
        TextView CurrentQuantity=(TextView)linearLayout_0.getChildAt(3) ;
        TextView DirtyQTY=(TextView)linearLayout_0.getChildAt(4) ;
        Treatment.setText(product.getTreatment_Name());
        ProductName.setText(product.getName());
        QuantityClean.setText(String.valueOf(product.getQuantity()));
        CurrentQuantity.setText(String.valueOf(product.getCurrent_Quantity()));
        DirtyQTY.setText(String.valueOf(product.getDirty_Qty()));
    }
    @Override
    public void onResume() {
        super.onResume();
        InitUI();

    }
    private void GetNotAssignTask()
    {
  /*      SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.MY_PREFS_TASks, 0);
        if(sharedPreferences.contains("Driver_TASKS"))
        {
            Task[] Tasks = ServerManager.deSerializeStringToObject(sharedPreferences.getString("Driver_TASKS",""), Task[].class);
            List<Task> AssignedTasks = new ArrayList<Task>(Tasks.length);
            AssignedTasks = Arrays.asList(Tasks);
            if(AssignedTasks!=null&&AssignedTasks.size()>0)
            {
                mTasks=new ArrayList<>();
                for(int i=0;i<AssignedTasks.size();i++)
                {
                    if(AssignedTasks.get(i).getPilotID()==null)
                    {
                        mTasks.add(AssignedTasks.get(i));
                    }
                }

                orderAdapter = new OrderAdapter(getContext(), mTasks,mpresnter,Type);
                recyclerView.setAdapter(orderAdapter);
            }
        }
        else
        {
            mPresnter.setTasksNotAssign();
        }*/

        //List<Task>Contracts=DB.GetTasksNotAssign(UserName);
        List<Task>Contracts=DB.GetTasksPilot(UserName);
        filterName=UserName;
        if(Contracts!=null&&Contracts.size()>0)
        {
            //  Toast.makeText(getContext(),Contracts.size()+"k",Toast.LENGTH_SHORT).show();
            mTasks=Contracts;
            ll_street.setVisibility(View.VISIBLE);
            lst_streets=DB.GetStreetsPilot(UserName);
            GetStreets(lst_streets);
            orderAdapter = new OrderAdapter(getContext(), Contracts,mpresnter,Type);
            recyclerView.setAdapter(orderAdapter);}
    }
    private void GetPilots()
    {
        List<Pilot>Pilots=DB.GetCoverPilots();
        if(Pilots!=null&&Pilots.size()>0) {
            for(int i=0;i<Pilots.size();i++)
            {
                if(Pilots.get(i).getPilotID().equalsIgnoreCase(UserName))
                {
                    Pilot tmp=Pilots.get(i);
                    Pilots.remove(i);
                    Pilots.add(0,tmp);
                    break;
                }
            }
            pilotAdapter = new PilotAdapter(null, null, getContext(), Pilots, " ", null);
            recyclerView.setAdapter(pilotAdapter);
        }

    }
    private void GetProducts()
    {

        List<Product>Products=DB.GetCoverProducts();


        if(Products!=null) {
            tab_product.setVisibility(View.VISIBLE);
            scr_product.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            AddProducts(Products);

        }


    }
    private void GetTasks() {
        List<Task>Tasks=DB.GetTasksPilot(UserName);
        filterName=UserName;
        if(Tasks!=null) {
            mTasks=Tasks;
            ll_street.setVisibility(View.VISIBLE);
            lst_streets=DB.GetStreetsPilot(UserName);
            GetStreets(lst_streets);
            orderAdapter = new OrderAdapter(getContext(), Tasks, mpresnter, Type);
            recyclerView.setAdapter(orderAdapter);
        }


    }
    public  void RefreshData()
    {

        mPresnter.setTasks();
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void setTasks(List<Task> Tasks) {

    }

    @Override
    public void showError(String error) {
        recyclerView.removeAllViewsInLayout();
        //swipRefresh.setRefreshing(false);
        Toast.makeText(getContext(),error,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void setProducts(List<Product> products) {

        /*if(products!=null&&products.size()>0) {
            if(Type.equalsIgnoreCase("tasks"))
            {
                if(Type.equalsIgnoreCase("tasks"))
                {
                    DB.AddProducts(products,UserName);
                }
            }
            else if(Type.equalsIgnoreCase("pilot_Product"))
            {
                DB.AddProducts(products,mPilot.getPilotID());
                products= DB.GetCoverProductsPilots(mPilot.getPilotID());
                productAdapter = new ProductAdapter(getContext(), products, this);
                recyclerView.setAdapter(productAdapter);
            }
            else if(Type.equalsIgnoreCase("Product"))
            {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.MY_PREFS_TASks, 0);
                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                sharedPreferencesEditor.putString("driver_Products",ServerManager.serializeObjectToString(products));
                sharedPreferencesEditor.apply();
                DB.AddProducts(products,UserName);
                products= DB.GetCoverProductsPilots(UserName);
                productAdapter = new ProductAdapter(getContext(), products, this);
                recyclerView.setAdapter(productAdapter);
            }
            else
            {

                productAdapter = new ProductAdapter(getContext(), products, this);
                recyclerView.setAdapter(productAdapter);
            }

        }*/
    }

    @Override
    public void setCoverProducts(List<Product> products) {

    }

    @Override
    public void setPilots(List<Pilot> Pilots) {
     /*   if(Pilots!=null&&Pilots.size()>0) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.MY_PREFS_TASks, 0);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            sharedPreferencesEditor.putString("Pilots",ServerManager.serializeObjectToString(Pilots));
            sharedPreferencesEditor.apply();
            pilotAdapter = new PilotAdapter(null,null,getContext(), Pilots," "," ");
            recyclerView.setAdapter(pilotAdapter);
           // DB.AddProducts(products);

        }*/
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
        Toast.makeText(getActivity(),Msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ShowLoading() {
        pbDailog = new ProgressDialog(getContext());
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
        InitUI();



    }

    @Override
    public void SuccessPilotAccept() {
       /* pilotCoverPrsenter.GetcoverPilot();
        pilotCoverPrsenter.CheckProductsPilot();*/
        CheckCashing2();


    }
    public boolean CheckCashing2() {

        List<OFFLINE> lst_off=DB.GetCashingRequest();
        String Address= GetAddress.GetAddress(getActivity().getApplicationContext(), GetCurrentLocaion.CurrentLoc);
        User user=new User(getActivity().getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", ""),"",GetCurrentLocaion.CurrentLoc,Address);
        IContractOffline.Presenter offlinePresnter=new OfflinePresenter(OrdersFragment.this, getActivity().getApplicationContext(), user);
        if(lst_off.size()>0) {
            ShowLoading();
            Log.d("test2",String.valueOf(lst_off.size()));
            // for (int i = 0; i < lst_off.size(); i++) {
            if (lst_off.get(0).getTYPE().equalsIgnoreCase("SMS")) {
                offlinePresnter.SendSMS(lst_off.get(0));
            } else if (lst_off.get(0).getTYPE().equalsIgnoreCase("ACTION")) {
                try {
                    offlinePresnter.setAssignmentAction(lst_off.get(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            // }
        }
        else
        {
            pilotCoverPrsenter.GetcoverPilot();
            pilotCoverPrsenter.CheckProductsPilot();

        }
        return  true;
    }

    @Override
    public void SetCheckCoverPilot(List<Pilot_Cover> lst_cover) {
        DB.AddCoverPilot(lst_cover,false);
        InitUI();
    }

    @Override
    public void SuccessPilotReject() {
       // pilotCoverPrsenter.CheckProductsPilot();
        CheckCashing2();
    }

    @Override
    public void UpdateCoverAfterReconcilation() {

        //pilotCoverPrsenter.GetcoverPilot();
        CheckCashing2();
    }

    @Override
    public void SetReconcilationRequests(List<Reconcilation> lst_reconcilation) {

    }

    @Override
    public void RefreshQTYReconcilation() {

    }


    @Override
    public void ShowMsg(String Type, String msg) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void SendAction() {

    }

    public void Search_cardNo(String Query)
    {
        if(orderAdapter!=null&&mTasks!=null)
        {
            orderAdapter.Search_cardNo(Query,mTasks);
        }
    }
    public void Search_custName(String Query)
    {
        if(orderAdapter!=null&&mTasks!=null)
        {
            orderAdapter.Search_CustName(Query,mTasks);
        }
    }
    public void Search_CustAdd(String Query)
    {
        if(orderAdapter!=null&&mTasks!=null)
        {
            orderAdapter.Search_Address(Query,mTasks);
        }
    }
    public void Search_comments(String Query)
    {
        if(orderAdapter!=null&&mTasks!=null)
        {
            orderAdapter.Search_Comments(Query,mTasks);
        }
    }
    public void Search(String Query)
    {
        if(orderAdapter!=null)
            orderAdapter.Search(Query,mTasks);
    }
    public void Reset()
    {
        if(orderAdapter!=null&&mTasks!=null)
        {
            orderAdapter.Reset(mTasks);
        }
    }
    public void SetAllSelection()
    {
        //Toast.makeText(getActivity().getApplicationContext(),"click2",Toast.LENGTH_SHORT).show();

        if(orderAdapter!=null)
        {
            // Toast.makeText(getActivity().getApplicationContext(),"click",Toast.LENGTH_SHORT).show();
            orderAdapter.SetAllSelection();
        }
    }

    @Override
    public void onClick(View v) {
        Button b=(Button)v;
        if(b.getText().toString().equalsIgnoreCase(getActivity().getApplicationContext().getResources().getString(R.string.PilotAccept)))
        {
            pilotCoverPrsenter.Pilotaccept(lst_Cover.get(0).getArea_ID());

        }
        else if(b.getText().toString().equalsIgnoreCase(getActivity().getApplicationContext().getResources().getString(R.string.Reject)))
        {
            pilotCoverPrsenter.PilotRejectCover(lst_Cover.get(0).getArea_ID());
        }
        else if(b.getText().toString().equalsIgnoreCase(getActivity().getApplicationContext().getResources().getString(R.string.send)))
        {
            CheckReconcilationLst();
        }
    }

    private void CheckReconcilationLst() {
        List<Pilot_Cover> lst= DB.checkreconcilation(UserName);
        boolean flag_QTy=true;
        for (int i = 1; i < tab_product.getChildCount(); i++) {
            flag_QTy=true;
            View child = tab_product.getChildAt(i);
            TableRow row = (TableRow) child;
            LinearLayout linearLayout_0 = (LinearLayout) row.getChildAt(0);
            TextView ProductName=(TextView) linearLayout_0.getChildAt(0);
            EditText ActualClean=(EditText) linearLayout_0.getChildAt(4);
            TextView Treatment=(TextView)linearLayout_0.getChildAt(1);
            TextView ExpClean=(TextView)linearLayout_0.getChildAt(2);
            TextView expDirty=(TextView)linearLayout_0.getChildAt(3);
            EditText ActualDirty=(EditText) linearLayout_0.getChildAt(5) ;
            EditText ApprovedDirty=(EditText) linearLayout_0.getChildAt(7) ;
            EditText ApprovedClean=(EditText) linearLayout_0.getChildAt(6) ;
            if(!ActualClean.getText().toString().isEmpty()&&!ActualDirty.getText().toString().isEmpty()){
                   // &&Integer.valueOf(ActualDirty.getText().toString())<=Integer.valueOf(expDirty.getText().toString())
                 //   &&Integer.valueOf(ActualClean.getText().toString())<=Integer.valueOf(ExpClean.getText().toString())) {
                lst.get(i-1).setActual_Dirty(ActualDirty.getText().toString());
                lst.get(i-1).setActual_Clean(ActualClean.getText().toString());
            }
            else
            {
                flag_QTy=false;
                if(ActualClean.getText().toString().isEmpty())
                        //||Integer.valueOf(ActualDirty.getText().toString())<=Integer.valueOf(expDirty.getText().toString()))
                    ActualClean.setError(getActivity().getApplicationContext().getResources().getString(R.string.errorQty));
                else
                    ActualDirty.setError(getActivity().getApplicationContext().getResources().getString(R.string.errorQty));
                break;
            }

        }
        if(flag_QTy)
        {
            // send Recociliation
            if(!ed_PilotCash.getText().toString().isEmpty()) {
                try {
                    pilotCoverPrsenter.PilotReconcilation(lst, UserName, ed_PilotCash.getText().toString(), ed_ExpCash.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                ed_PilotCash.requestFocus();
                ed_PilotCash.setError(getActivity().getResources().getString(R.string.errfillPilotAmount));
            }

        }
    }

    @Override
    public void Failed(OFFLINE offline) {

    }

    @Override
    public void Success(OFFLINE offline) {

    }

    @Override
    public void SuccessALLRequests() {
        StopLoading();
        pilotCoverPrsenter.GetcoverPilot();
        pilotCoverPrsenter.CheckProductsPilot();
    }

    @Override
    public void Fail() {
        StopLoading();
    }
}
