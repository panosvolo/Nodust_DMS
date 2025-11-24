package com.mts.mea.nodust_app.Pilots;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import com.mts.mea.nodust_app.Pilots.PilotCover.IPilotCover;
import com.mts.mea.nodust_app.Pilots.PilotCover.PilotCoverPresenter;
import com.mts.mea.nodust_app.Pilots.PilotCover.Pilot_Cover;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.Street;
import com.mts.mea.nodust_app.User.User;
import com.mts.mea.nodust_app.User.UserInfo;
import com.mts.mea.nodust_app.User.UserShift.IContractUserShif;
import com.mts.mea.nodust_app.common.Constants;
import com.mts.mea.nodust_app.common.DataBaseHelper;
import com.mts.mea.nodust_app.common.GetAddress;
import com.mts.mea.nodust_app.orders.GetOrders.GetOrdersPresenter;
import com.mts.mea.nodust_app.orders.GetOrders.HomeWithMenuActivity;
import com.mts.mea.nodust_app.orders.GetOrders.IContractGetOrders;
import com.mts.mea.nodust_app.orders.SetAssignmentAction.IContractAssignAction;
import com.mts.mea.nodust_app.orders.Task;
import com.mts.mea.nodust_app.products.Collection;
import com.mts.mea.nodust_app.products.PackageInfo;
import com.mts.mea.nodust_app.products.Product;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class pilotFragment extends Fragment implements IContractGetOrders.View,IPilotCover.View,View.OnClickListener,IContractOffline.View {
    RecyclerView recyclerView;
    // SwipeRefreshLayout swipRefresh;
    List<Task> mTasks;
    String UserName;
    IContractAssignAction.Presenter mpresnter;
    IContractUserShif.Presenter userShiftPresenter;
    private ProgressDialog pbDailog;
    public Button btn_shift;
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
    List<Product> lst_cover_products;
    IPilotCover.Presenter PilotCoverPresenter;
    private String send_ProductIDs;
    Button btn_AddProduct,btn_reject,btn_accept;
    LinearLayout ll_actions2;
    EditText ed_ExpCash,ed_PilotCash,ed_DriverCash;
    List<Pilot_Cover>lst_cover;
    LinearLayout ll_street;
    android.support.v7.widget.AppCompatSpinner sp_street;
    private List<Street> lst_streets;
    private String filterNvame="";
    private SharedPreferences sharedPreferences_streetName;

    public pilotFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_pilot, container, false);
        recyclerView=(RecyclerView)view.findViewById(R.id.recView);
        tab_product=(TableLayout)view.findViewById(R.id.tab_product);
        scr_product=(ScrollView)view.findViewById(R.id.scr_product);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        // swipRefresh=(android.support.v4.widget.SwipeRefreshLayout)view.findViewById(R.id.ll_swip);
        btn_shift=(Button)view.findViewById(R.id.btn_UserShift);
        DB=new DataBaseHelper(getActivity());
        Type=getArguments().getString("Type");
        btn_AddProduct=(Button)view.findViewById(R.id.btn_AddProduct);
        btn_accept=(Button)view.findViewById(R.id.btn_accept);
        btn_reject=(Button)view.findViewById(R.id.btn_reject);
        ll_actions2=(LinearLayout)view.findViewById(R.id.ll_actions_2);
        ed_ExpCash=(EditText)view.findViewById(R.id.expCash);
        ed_DriverCash=(EditText)view.findViewById(R.id.driverCash);
        ed_PilotCash=(EditText)view.findViewById(R.id.pilotcash);
        btn_AddProduct.setOnClickListener(this);
        btn_reject.setOnClickListener(this);
        btn_accept.setOnClickListener(this);
        ll_street=(LinearLayout)view.findViewById(R.id.ll_street);
        sp_street=(android.support.v7.widget.AppCompatSpinner)view.findViewById(R.id.sp_streetname);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntUI();

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

    public void IntUI() {
        scr_product.setVisibility(View.GONE);
        tab_product.setVisibility(View.GONE);
        ll_actions2.setVisibility(View.GONE);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        if (sharedPreferences.contains("UserName")) {
            UserName = sharedPreferences.getString("UserName", "");
        }
        String Address = GetAddress.GetAddress(getActivity(), GetCurrentLocaion.CurrentLoc);
        User user = new User(UserName, "", GetCurrentLocaion.CurrentLoc, Address);
        mPresnter = new GetOrdersPresenter(pilotFragment.this,getActivity().getApplicationContext(),user);
        if(Type.equalsIgnoreCase("pilot_Product")){
            // products of contracts
            mPilot=(Pilot)getArguments().get("pilot");
            user = new User(mPilot.getPilotID(), "", GetCurrentLocaion.CurrentLoc, Address);
           /* mPresnter = new GetOrdersPresenter(this, getContext(), user);
            mPresnter.setProducts();*/
            btn_AddProduct.setVisibility(View.VISIBLE);
            GetProducts(mPilot.getPilotID());
        }
        else if(Type.equalsIgnoreCase("pilot_tasks"))
        {
            mPilot=(Pilot)getArguments().get("pilot");
            filterNvame=mPilot.getPilotID();
            user = new User(mPilot.getPilotID(), "", GetCurrentLocaion.CurrentLoc, Address);
          /*  mPresnter = new GetOrdersPresenter(this, getContext(), user);
            mPresnter.setTasks();*/
            GetTasks(mPilot.getPilotID());
            btn_AddProduct.setVisibility(View.GONE);
        }
        else if(Type.equalsIgnoreCase("Check_pilot_Product"))
        {


            mPilot=(Pilot)getArguments().get("pilot");
            filterNvame=mPilot.getPilotID();
            CheckCoverProductsPilot();
        }
        else if(Type.equalsIgnoreCase("Pilot_Reconcilation"))
        {


            mPilot=(Pilot)getArguments().get("pilot");
            filterNvame=mPilot.getPilotID();
            CheckReconcilation();
        }
        else if(Type.equalsIgnoreCase("Driver_Reconcilation"))
        {

            mPilot=(Pilot)getArguments().get("pilot");
            filterNvame=mPilot.getPilotID();
            CheckReconcilation();
        }
        sp_street.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    if (lst_streets != null && lst_streets.size() > position-1)
                        //Toast.makeText(getContext().getApplicationContext(), lst_streets.get(position-1).getStreetName(), Toast.LENGTH_SHORT).show();
                        //Refresh
                        orderAdapter = new OrderAdapter(getContext(), DB.GetTasksPilot_street(filterNvame, lst_streets.get(position-1).getStreetName()), mpresnter, Type);
                    recyclerView.setAdapter(orderAdapter);
                    if(filterNvame.equalsIgnoreCase(UserName))
                    {
                        sharedPreferences_streetName.edit().putString("StreetName",lst_streets.get(position-1).getStreetName()).commit();

                    }

                }
                else
                {
                    orderAdapter = new OrderAdapter(getContext(), DB.GetTasksPilot(filterNvame), mpresnter, Type);
                    recyclerView.setAdapter(orderAdapter);
                    if(filterNvame.equalsIgnoreCase(UserName))
                        sharedPreferences_streetName.edit().putString("StreetName","no").commit();


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    private void CheckReconcilation() {
        if(Type.equalsIgnoreCase("Driver_Reconcilation"))
        {
        List<Pilot_Cover>lst=DB.checkreconcilation(mPilot.getPilotID());
        if(lst!=null&&lst.size()>0) {
            ed_ExpCash.setEnabled(false);
            //  btn_reject.setVisibility(View.VISIBLE);
            String Expcash = String.valueOf(DB.GetTotalPilot(mPilot.getPilotID()));
            ed_ExpCash.setText(Expcash);
            if (Type.equalsIgnoreCase("Driver_Reconcilation")) {
                btn_AddProduct.setVisibility(View.VISIBLE);
                ed_PilotCash.setEnabled(true);
                ed_DriverCash.setEnabled(false);
            } else {
                btn_accept.setVisibility(View.VISIBLE);
                btn_reject.setVisibility(View.VISIBLE);
                ed_DriverCash.setEnabled(true);
                ed_PilotCash.setEnabled(false);
            }
            tab_product.setVisibility(View.VISIBLE);
            scr_product.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            ll_actions2.setVisibility(View.VISIBLE);

            AddreconcilationCoverProducts(lst);
        }
        }
        else if(Type.equalsIgnoreCase("Pilot_Reconcilation"))
        {
            List<Reconcilation>lst=DB.GetRquestReconcilation(mPilot.getPilotID());
            if(lst!=null&&lst.size()>0) {
                ed_ExpCash.setEnabled(false);
                //  btn_reject.setVisibility(View.VISIBLE);
                String Expcash = String.valueOf(DB.GetTotalPilot(mPilot.getPilotID()));
                ed_ExpCash.setText(Expcash);

                    btn_accept.setVisibility(View.VISIBLE);
                    btn_reject.setVisibility(View.VISIBLE);
                    ed_DriverCash.setEnabled(true);
                    ed_PilotCash.setEnabled(false);
                tab_product.setVisibility(View.VISIBLE);
                scr_product.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                ll_actions2.setVisibility(View.VISIBLE);

                AddRequstReconcilation(lst);
            }
            else
            {
                btn_accept.setVisibility(View.GONE);
                btn_reject.setVisibility(View.GONE);
            }
        }

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
        if(Type.equalsIgnoreCase("Driver_Reconcilation"))
        {
            ApprovedClean.setEnabled(false);
            ApprovedDirty.setEnabled(false);
            ActualClean.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.solid_black));
            ActualDirty.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.solid_black));

        }
        else
        {
            ActualClean.setEnabled(false);
            ActualDirty.setEnabled(false);
            ApprovedClean.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.solid_black));
            ApprovedDirty.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.solid_black));

        }
        tab_product.addView(inflate);
        if(products.get(0).getTOTAL_RECIEPT_AMOUNT_DRIVER()==null||products.get(0).getTOTAL_RECIEPT_AMOUNT_DRIVER().isEmpty())
            ed_DriverCash.setText("0");
        else
            ed_DriverCash.setText(String.valueOf(products.get(0).getTOTAL_RECIEPT_AMOUNT_DRIVER()));
        if(products.get(0).getTOTAL_RECIEPT_AMOUNT_PILOT()==null||products.get(0).getTOTAL_RECIEPT_AMOUNT_PILOT().isEmpty())
            ed_PilotCash.setText("0");
        else {
           // ed_PilotCash.setText(String.valueOf(products.get(0).getTOTAL_RECIEPT_AMOUNT_PILOT()));
            ed_PilotCash.setText(String.valueOf(0));

        }
        for(int i=0;i<products.size();i++)
            AddRowReconcilation(products.get(i));

    }
    private void AddRequstReconcilation(List<Reconcilation> products) {
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
        if(Type.equalsIgnoreCase("Driver_Reconcilation"))
        {
            ApprovedClean.setEnabled(false);
            ApprovedDirty.setEnabled(false);
            ActualClean.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.solid_black));
            ActualDirty.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.solid_black));

        }
        else
        {
            ActualClean.setEnabled(false);
            ActualDirty.setEnabled(false);
            ApprovedClean.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.solid_black));
            ApprovedDirty.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.solid_black));

        }
        tab_product.addView(inflate);
        if(products.get(0).getAPPROVED_MONEY()==null||products.get(0).getAPPROVED_MONEY().isEmpty())
            ed_DriverCash.setText("0");
        else
            ed_DriverCash.setText(String.valueOf(products.get(0).getAPPROVED_MONEY()));
        if(products.get(0).getACTUAL_MONEY()==null||products.get(0).getACTUAL_MONEY().isEmpty())
            ed_PilotCash.setText("0");
        else
            ed_PilotCash.setText(String.valueOf(products.get(0).getACTUAL_MONEY()));
        for(int i=0;i<products.size();i++)
            AddRequestReconcilation(products.get(i));

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
        final EditText ApprovedDirty=(EditText) linearLayout_0.getChildAt(7) ;
        EditText ApprovedClean=(EditText) linearLayout_0.getChildAt(6) ;
        Treatment.setText(product.getTreatment_Name());
        ProductName.setText(product.getName());
        ApprovedClean.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    ApprovedDirty.requestFocus();
                }
                return false;
            }
        });
        final TableRow finalInflate = inflate;
        ApprovedDirty.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO||actionId == EditorInfo.IME_ACTION_NEXT) {
                   // ApprovedDirty.requestFocus();
                    int rowIndex = tab_product.indexOfChild(finalInflate)+1;
                    Log.d("rowIndexDirty", String.valueOf(rowIndex));
                    if(rowIndex<tab_product.getChildCount()) {
                        TableRow row1 = (TableRow) tab_product.getChildAt(rowIndex);
                        if (row1 != null) {
                            EditText et = (EditText) ((LinearLayout) row1.getChildAt(0)).getChildAt(6);
                            Log.d("rowIndexDirty", et.getText().toString());
                            et.requestFocus();
                            return true;

                         //   Toast.makeText(getContext().getApplicationContext(),et.getText().toString(),Toast.LENGTH_SHORT).show();

                        }
                    }
                    else
                    {
                        if(ed_DriverCash!=null) {
                            ed_DriverCash.requestFocus();
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
        if(product.getActual_Clean()!=null&&!product.getActual_Clean().isEmpty())
            ActualClean.setText("0");
        else {
            if(Type.equalsIgnoreCase("Driver_Reconcilation"))
            ActualClean.setText(String.valueOf(product.getCurrent_Quantity()));
            else
                ActualClean.setText("0");
        }
        if(product.getActual_Dirty()!=null&&!product.getActual_Dirty().isEmpty())
            ActualDirty.setText("0");
        else {
            if(Type.equalsIgnoreCase("Driver_Reconcilation"))
            ActualDirty.setText(String.valueOf(product.getDirty_Qty()));
            else
                ActualDirty.setText("0");

        }
        if (product.getApproved_clean()!=null&&!product.getApproved_clean().isEmpty())
            ApprovedClean.setText(product.getApproved_clean());
        else {
            if(!Type.equalsIgnoreCase("Driver_Reconcilation"))
            ApprovedClean.setText(String.valueOf(product.getCurrent_Quantity()));
            else
                ApprovedClean.setText("0");
        }
        if(product.getApproved_Dirty()!=null&&!product.getApproved_Dirty().isEmpty())
            ApprovedDirty.setText(product.getApproved_Dirty());
        else {
            if(!Type.equalsIgnoreCase("Driver_Reconcilation"))
            ApprovedDirty.setText(String.valueOf(product.getDirty_Qty()));
            else
                ApprovedDirty.setText("0");
        }
        ExpClean.setText(String.valueOf(product.getCurrent_Quantity()));
        expDirty.setText(String.valueOf(product.getDirty_Qty()));
        if(Type.equalsIgnoreCase("Driver_Reconcilation"))
        {
            ApprovedClean.setEnabled(false);
            ApprovedDirty.setEnabled(false);
            ActualClean.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.solid_black));
            ActualDirty.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.solid_black));

        }
        else
        {
            ActualClean.setEnabled(false);
            ActualDirty.setEnabled(false);
            ApprovedClean.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.solid_black));
            ApprovedDirty.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.solid_black));

        }
    }
    private void AddRequestReconcilation(Reconcilation product) {
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
        final EditText ApprovedDirty=(EditText) linearLayout_0.getChildAt(7) ;
        EditText ApprovedClean=(EditText) linearLayout_0.getChildAt(6) ;
        Treatment.setText(product.getTRE_NAME());
        ProductName.setText(product.getPRODUCT_NAME());
        ApprovedClean.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    ApprovedDirty.requestFocus();
                }
                return false;
            }
        });
        final TableRow finalInflate = inflate;
        ApprovedDirty.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO||actionId == EditorInfo.IME_ACTION_NEXT) {
                    // ApprovedDirty.requestFocus();
                    int rowIndex = tab_product.indexOfChild(finalInflate)+1;
                    Log.d("rowIndexDirty", String.valueOf(rowIndex));
                    if(rowIndex<tab_product.getChildCount()) {
                        TableRow row1 = (TableRow) tab_product.getChildAt(rowIndex);
                        if (row1 != null) {
                            EditText et = (EditText) ((LinearLayout) row1.getChildAt(0)).getChildAt(6);
                            Log.d("rowIndexDirty", et.getText().toString());
                            et.requestFocus();
                            return true;

                            //   Toast.makeText(getContext().getApplicationContext(),et.getText().toString(),Toast.LENGTH_SHORT).show();

                        }
                    }
                    else
                    {
                        if(ed_DriverCash!=null) {
                            ed_DriverCash.requestFocus();
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
        if(product.getACTUAL_CLEAN()!=null&&!product.getACTUAL_CLEAN().isEmpty())
            ActualClean.setText(product.getACTUAL_CLEAN());
        else {
            if(Type.equalsIgnoreCase("Driver_Reconcilation"))
                ActualClean.setText(String.valueOf(product.getACTUAL_CLEAN()));
            else
                ActualClean.setText("0");
        }
        if(product.getACTUAL_DIRTY()!=null&&!product.getACTUAL_DIRTY().isEmpty())
            ActualDirty.setText(product.getACTUAL_DIRTY());
        else {
            if(Type.equalsIgnoreCase("Driver_Reconcilation"))
                ActualDirty.setText(String.valueOf(product.getACTUAL_DIRTY()));
            else
                ActualDirty.setText("0");

        }
        if (product.getAPPROVED_CLEAN()!=null&&!product.getAPPROVED_CLEAN().isEmpty()&&
                !product.getAPPROVED_CLEAN().equalsIgnoreCase("null"))
            ApprovedClean.setText(product.getAPPROVED_CLEAN());
        else {
            if(!Type.equalsIgnoreCase("Driver_Reconcilation"))
                ApprovedClean.setText(String.valueOf(product.getEXP_CLEAN()));
            else
                ApprovedClean.setText("0");
        }
        if(product.getAPPROVED_DIRTY()!=null&&!product.getAPPROVED_DIRTY().isEmpty()&&
                !product.getAPPROVED_DIRTY().equalsIgnoreCase("null"))
            ApprovedDirty.setText(product.getAPPROVED_DIRTY());
        else {
            if(!Type.equalsIgnoreCase("Driver_Reconcilation"))
                ApprovedDirty.setText(String.valueOf(product.getEXP_DIRTY()));
            else
                ApprovedDirty.setText("0");
        }
        ExpClean.setText(String.valueOf(product.getEXP_CLEAN()));
        expDirty.setText(String.valueOf(product.getEXP_DIRTY()));
        if(Type.equalsIgnoreCase("Driver_Reconcilation"))
        {
            ApprovedClean.setEnabled(false);
            ApprovedDirty.setEnabled(false);
            ActualClean.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.solid_black));
            ActualDirty.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.solid_black));

        }
        else
        {
            ActualClean.setEnabled(false);
            ActualDirty.setEnabled(false);
            ApprovedClean.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.solid_black));
            ApprovedDirty.setTextColor(getActivity().getApplicationContext().getResources().getColor(R.color.solid_black));

        }
    }
    private void CheckCoverProductsPilot() {
        List<Pilot_Cover>lst=DB.CheckAcceptenceCoverPilot(mPilot.getPilotID());
        lst_cover=lst;
        if(lst!=null&&lst.size()>0) {
            btn_reject.setVisibility(View.VISIBLE);
            btn_accept.setVisibility(View.VISIBLE);
            tab_product.setVisibility(View.VISIBLE);
            scr_product.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            AddCoverPilotProducts(lst);
        }

    }
    private void AddCoverPilotProducts(List<Pilot_Cover> products) {
        tab_product.removeAllViews();
        TableRow inflate = new TableRow(getActivity());
        inflate = (TableRow) View.inflate(getActivity(), R.layout.row_product_header, null);
        tab_product.addView(inflate);
        for(int i=0;i<products.size();i++)
            AddRowCoverProduct(products.get(i));

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
        TextView DirtyQuantity=(TextView)linearLayout_0.getChildAt(4) ;

        Treatment.setText(product.getTreatment_Name());
        ProductName.setText(product.getName());
        QuantityClean.setText(String.valueOf(product.getQuantity()));
        CurrentQuantity.setText(String.valueOf(product.getCurrent_Quantity()));
        DirtyQuantity.setText(String.valueOf(product.getDirty_Qty()));
    }

    private void GetTasks(String pilotID) {
        List<Task>Tasks=DB.GetTasksPilot(pilotID);
        mTasks=Tasks;
        if(Tasks.size()>0) {
            ll_street.setVisibility(View.VISIBLE);
            lst_streets=DB.GetStreetsPilot(pilotID);
            GetStreets(lst_streets);
            orderAdapter = new OrderAdapter(getActivity(), Tasks, mpresnter, Type);
            recyclerView.setAdapter(orderAdapter);
        }
    }
    public  void GetStreets(List<Street>lst_streets)
    {
        ArrayList<String> data_spiner = new ArrayList<>();
        if (lst_streets != null) {
            data_spiner.add(getContext().getResources().getString(R.string.ChooseStreet));
            for (int j = 0; j<lst_streets.size(); j++) {
                data_spiner.add(String.valueOf(lst_streets.get(j).getStreetName()+" ==> ("+String.valueOf(lst_streets.get(j).getCount())+')'));
            }
        }
        CloseCodeAdapter adapter=new CloseCodeAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, data_spiner);
        sp_street.setAdapter(adapter);
        if(filterNvame.equalsIgnoreCase(UserName)) {
            sharedPreferences_streetName = getContext().getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
            if (sharedPreferences_streetName.contains("StreetName") && !sharedPreferences_streetName.getString("StreetName", "").equalsIgnoreCase("no")) {
                for (int i = 0; i < lst_streets.size(); i++) {
                    if (lst_streets.get(i).getStreetName().equalsIgnoreCase(sharedPreferences_streetName.getString("StreetName", ""))) {
                        sp_street.setSelection(i + 1);
                        break;
                    }
                }
            }
        }
    }

    private void GetProducts(String pilotID) {
        List<Task>Contracts=DB.GetPendingTasksPilot(pilotID);
        String []Contract_id=new String[Contracts.size()];
        for (int i=0;i<Contracts.size();i++)
        {
            Contract_id[i]=Contracts.get(i).getAID();
        }
        List<Product> lst_tasks_prod=DB.GetCoverProductWithoutCanceled(Contract_id);
        // List<Product> lst_Tasks=DB.GetCoverProducts();
        lst_cover_products=DB.GetCoverProducts();
        List<Pilot_Cover> lst_current_pilot=DB.GetCoverPilot(pilotID);
        for(int i=0;i<lst_cover_products.size();i++)
        {
            for(int j=0;j<lst_tasks_prod.size();j++)
            {

                if(lst_cover_products.get(i).getPRODUCT_ID().equalsIgnoreCase(lst_tasks_prod.get(j).getPRODUCT_ID())&&lst_cover_products.get(i).getTreatment_code().equalsIgnoreCase(lst_tasks_prod.get(j).getTreatment_code()))
                {
                    lst_cover_products.get(i).setNeededPilot(lst_cover_products.get(i).getNeededPilot()+Integer.valueOf(lst_tasks_prod.get(j).getQUNTITY()));
                }
            }
            for(int j=0;j<lst_current_pilot.size();j++)
            {

                if(lst_cover_products.get(i).getPRODUCT_ID().equalsIgnoreCase(lst_current_pilot.get(j).getProduct_ID())&&lst_cover_products.get(i).getTreatment_code().equalsIgnoreCase(lst_current_pilot.get(j).getTreatment_ID()))
                {
                    lst_cover_products.get(i).setCurrentQuantityPilot(lst_cover_products.get(i).getCurrentQuantityPilot()+(lst_current_pilot.get(j).getCurrent_Quantity()));
                }

            }

        }
        // neglate that needQTY equal zero
        List<Product> lst=new ArrayList<>();

        if(lst_cover_products!=null&&lst_cover_products.size()>0) {
            for(int i=0;i<lst_cover_products.size();i++)
            {
                if(lst_cover_products.get(i).getNeededPilot()!=0)
                    lst.add(lst_cover_products.get(i));
            }
            lst_cover_products=new ArrayList<>();
            lst_cover_products=lst;
            if(lst!=null&&lst.size()>0) {
                scr_product.setVisibility(View.VISIBLE);
                tab_product.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                btn_AddProduct.setVisibility(View.VISIBLE);

                AddProducts(lst_cover_products);
            }
            else
                btn_AddProduct.setVisibility(View.GONE);

            //productAdapter = new ProductAdapter(getActivity(), lst_Tasks, null);

            // recyclerView.setAdapter(productAdapter);
        }

    }

    @Override
    public void setTasks(List<Task> Tasks) {
        if(Tasks!=null)
        {
            mTasks = Tasks;
            // update Changes
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.MY_PREFS_TASks, 0);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            sharedPreferencesEditor.putString("TASKS", ServerManager.serializeObjectToString(Tasks));
            sharedPreferencesEditor.apply();
            if(Tasks.size()>0) {
                ll_street.setVisibility(View.VISIBLE);

                orderAdapter = new OrderAdapter(getActivity(), Tasks, mpresnter, Type);
                recyclerView.setAdapter(orderAdapter);
            }
        }
    }

    @Override
    public void showError(String error) {
        recyclerView.removeAllViewsInLayout();
        // swipRefresh.setRefreshing(false);
        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setProducts(List<Product> products) {

        if(products!=null&&products.size()>0) {
            if(Type.equalsIgnoreCase("pilot_Product"))
            {
                //  DB.AddProducts(products,mPilot.getPilotID());
                //   products= DB.GetCoverProductsPilots(mPilot.getPilotID());
                //  productAdapter = new ProductAdapter(getActivity(), products, null);
                // recyclerView.setAdapter(productAdapter);
                scr_product.setVisibility(View.VISIBLE);
                tab_product.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                AddProducts(products);
            }
        }
    }
    public void Search(String Query)
    {
        if(orderAdapter!=null)
            orderAdapter.Search(Query,mTasks);
    }

    @Override
    public void setCoverProducts(List<Product> products) {
        // Toast.makeText(getActivity(),"ff",Toast.LENGTH_SHORT).show();
        // ShowLoading();
        DB.UpdateCoverProductDriver(products);
        IntUI();

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
        if(Msg.equalsIgnoreCase(getActivity().getResources().getString(R.string.SaveChanges)+"SuccessfulCover"))
        {
            Msg=getActivity().getResources().getString(R.string.SaveChanges);
            //ShowLoading();
            mPresnter.setCoverProducts();
           // PilotCoverPresenter.CheckProductsPilot();
            CheckCashing();

        }

        Toast.makeText(getActivity(),Msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ShowLoading() {
        pbDailog = new ProgressDialog(getActivity());
        pbDailog.setMessage("جاري التحميل .....");
        pbDailog.setCancelable(false);
        pbDailog.show();
    }

    @Override
    public void StopLoading() {
        if(pbDailog!=null) {
            pbDailog.hide();

        }
    }

    @Override
    public void setCollection(List<Collection> products) {

    }

    @Override
    public void SetCoverPilot(List<Pilot_Cover> lst_cover) {
        DB.AddCoverPilot(lst_cover,true);
        IntUI();
    }
    @Override
    public void SuccessPilotAccept() {
        btn_accept.setVisibility(View.GONE);
        btn_reject.setVisibility(View.GONE);
        String Address= GetAddress.GetAddress(getActivity().getApplicationContext(), GetCurrentLocaion.CurrentLoc);
        User user=new User(getActivity().getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", ""),"",GetCurrentLocaion.CurrentLoc,Address);
        PilotCoverPresenter=new PilotCoverPresenter(pilotFragment.this,getActivity().getApplicationContext(),user);

        CheckCashing();

    }

    @Override
    public void SetCheckCoverPilot(List<Pilot_Cover> lst_cover) {
        DB.AddCoverPilot(lst_cover,false);
        IntUI();
    }

    @Override
    public void SuccessPilotReject() {

        // I NEED TO CHECK CASHING FIRST
        String Address= GetAddress.GetAddress(getActivity().getApplicationContext(), GetCurrentLocaion.CurrentLoc);
        User user=new User(getActivity().getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", ""),"",GetCurrentLocaion.CurrentLoc,Address);
        PilotCoverPresenter=new PilotCoverPresenter(pilotFragment.this,getActivity().getApplicationContext(),user);
        //PilotCoverPresenter.CheckProductsPilot();
        mPresnter.setCoverProducts();
        CheckCashing();
       /* Intent i=new Intent(getActivity(),Pilot_Activity.class);
        i.putExtra("mPilot", (Serializable) mPilot);
        getActivity().startActivity(i);*/

    }

    @Override
    public void UpdateCoverAfterReconcilation() {
        String Address= GetAddress.GetAddress(getActivity().getApplicationContext(), GetCurrentLocaion.CurrentLoc);
        User user=new User(getActivity().getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", ""),"",GetCurrentLocaion.CurrentLoc,Address);
        PilotCoverPresenter=new PilotCoverPresenter(pilotFragment.this,getActivity().getApplicationContext(),user);
        CheckCashing();
      //  PilotCoverPresenter.GetCoverAllPilot();
        mPresnter.setCoverProducts();


    }

    @Override
    public void SetReconcilationRequests(List<Reconcilation> lst_reconcilation) {
        if(lst_reconcilation!=null)
            DB.SetReconcilationRequests(lst_reconcilation);
        IntUI();

    }

    @Override
    public void RefreshQTYReconcilation() {
        String Address= GetAddress.GetAddress(getActivity().getApplicationContext(), GetCurrentLocaion.CurrentLoc);
        User user=new User(getActivity().getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", ""),"",GetCurrentLocaion.CurrentLoc,Address);
        PilotCoverPresenter=new PilotCoverPresenter(pilotFragment.this,getActivity().getApplicationContext(),user);
        PilotCoverPresenter.GetReconcilationRequest();


    }

    private void AddProducts(List<Product> products) {
        tab_product.removeAllViews();
        TableRow inflate = new TableRow(getActivity());
        inflate = (TableRow) View.inflate(getActivity(), R.layout.row_product_header_pilot, null);
        tab_product.addView(inflate);
        for(int i=0;i<products.size();i++)
            AddRowProduct(products.get(i));

    }
    private void AddRowProduct(Product product) {
        TableRow inflate = new TableRow(getActivity());
        inflate = (TableRow) View.inflate(getActivity(), R.layout.row_product_pilot, null);
        tab_product.addView(inflate);
        LinearLayout linearLayout_0 = (LinearLayout) inflate.getChildAt(0);
        TextView ProductName=(TextView) linearLayout_0.getChildAt(0);
        TextView QuantityClean=(TextView) linearLayout_0.getChildAt(2);
        TextView Treatment=(TextView)linearLayout_0.getChildAt(1);
        TextView CurrentQuantity=(TextView)linearLayout_0.getChildAt(3);
        TextView CleanQty=(TextView)linearLayout_0.getChildAt(4);
        TextView NeedQty=(TextView)linearLayout_0.getChildAt(5);

        EditText PilotQuantity=(EditText) linearLayout_0.getChildAt(6);

        Treatment.setText(product.getTreatment_description());
        ProductName.setText(product.getNAME());
        QuantityClean.setText(product.getQUNTITY());
        CleanQty.setText(String.valueOf(product.getCurrentQuantityPilot()));
        CurrentQuantity.setText(String.valueOf(product.getCurrentQuantity()));
        NeedQty.setText(String.valueOf(product.getNeededPilot()));
    }
    public void CheckCoverAssignedToPilot()
    {
        List<Product>send_prod=new ArrayList<>();
        String ProductsID="";
        boolean flag=false;
       // lst_cover_products=DB.GetCoverProducts();
        if(lst_cover_products!=null&&lst_cover_products.size()>0) {
            for (int i = 1; i < tab_product.getChildCount(); i++) {
                View child = tab_product.getChildAt(i);
                TableRow row = (TableRow) child;
                LinearLayout linearLayout_0 = (LinearLayout) row.getChildAt(0);
                TextView CurrentQuantity=(TextView)linearLayout_0.getChildAt(3);
                EditText PilotQuantity=(EditText) linearLayout_0.getChildAt(6);
                TextView CleanQty=(TextView)linearLayout_0.getChildAt(4);
                TextView NeedQty=(TextView)linearLayout_0.getChildAt(5);
                if(!PilotQuantity.getText().toString().isEmpty()&&!PilotQuantity.getText().toString().equalsIgnoreCase("0")) {
                    Product p = new Product();
                    p.setID(lst_cover_products.get(i - 1).getID());
                    if (Integer.valueOf(CurrentQuantity.getText().toString()) >= Integer.valueOf(PilotQuantity.getText().toString())) {
                        p.setQUNTITY(PilotQuantity.getText().toString());
                        send_prod.add(p);
                    } else {
                        PilotQuantity.setError(getActivity().getResources().getString(R.string.AssignedCoverErr));
                        flag = true;
                        break;
                    }
                }

            }
            if(!flag)
            {
                // call api
                if(send_prod!=null&&send_prod.size()>0)
                    ShowConfirmMessage(send_prod);
                else

                    Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.enterQuantity),Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            // show message
            Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.enoughProduct),Toast.LENGTH_SHORT).show();
        }
    }

    private void ShowConfirmMessage(List<Product> Send_Product) {
        send_ProductIDs="";
        for(int i=0;i<Send_Product.size();i++)
        {
            send_ProductIDs+=Send_Product.get(i).getID()+","+Send_Product.get(i).getQUNTITY();
            if(i!=Send_Product.size()-1)
            {
                send_ProductIDs+='&';
            }
        }
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getContext());
        } else {
            builder = new AlertDialog.Builder(getContext());
        }
        builder.setTitle(getContext().getResources().getString(R.string.addProducts))
                .setMessage(getContext().getResources().getString(R.string.ConfirmAdd))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        String Address= GetAddress.GetAddress(getActivity().getApplicationContext(), GetCurrentLocaion.CurrentLoc);
                        User user=new User(getActivity().getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", ""),"",GetCurrentLocaion.CurrentLoc,Address);
                        PilotCoverPresenter=new PilotCoverPresenter(pilotFragment.this,getActivity().getApplicationContext(),user);
                      // CHECK CASHING FIRST


                        PilotCoverPresenter.AddCoverPilot(send_ProductIDs,mPilot.getPilotID());

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void SetAllSelection()
    {
        if(orderAdapter!=null)
        {
            orderAdapter.SetAllSelection();
        }
    }

    @Override
    public void onClick(View v) {
        Button b=(Button)v;
        if(b.getText().toString().equalsIgnoreCase(getActivity().getApplicationContext().getResources().getString(R.string.AddProducts)))
        {
            if(Type.equalsIgnoreCase("Driver_Reconcilation"))
            {
                CheckReconcilationDriver(true);
            }
            else
            {
                Button eval = (Button) getActivity().findViewById(R.id.btn_evalution);
                if (eval.getVisibility() == View.GONE) {
                    CheckCoverAssignedToPilot();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), getActivity().getApplicationContext().getResources().getString(R.string.EvaluationFirst), Toast.LENGTH_SHORT).show();

                }
            }

        }
        else if(b.getText().toString().equalsIgnoreCase(getActivity().getApplicationContext().getResources().getString(R.string.PilotAccept)))
        {
            String Address = GetAddress.GetAddress(getActivity().getApplicationContext(), GetCurrentLocaion.CurrentLoc);
            User user = new User(getActivity().getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", ""), "", GetCurrentLocaion.CurrentLoc, Address);
            PilotCoverPresenter = new PilotCoverPresenter(pilotFragment.this, getActivity().getApplicationContext(), user);

            if(Type.equalsIgnoreCase("Pilot_Reconcilation"))
            {
                ActionOnReconcilationRequest(true);
            }
            else {
                if(lst_cover!=null&&lst_cover.size()>0)

                {
                    // CHECK CASHING FIRST
                    PilotCoverPresenter.Pilotaccept(lst_cover.get(0).getArea_ID());
                }
            }
        }
        else if(b.getText().toString().equalsIgnoreCase(getActivity().getApplicationContext().getResources().getString(R.string.Reject)))
        {
            String Address = GetAddress.GetAddress(getActivity().getApplicationContext(), GetCurrentLocaion.CurrentLoc);
            User user = new User(getActivity().getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", ""), "", GetCurrentLocaion.CurrentLoc, Address);
            PilotCoverPresenter = new PilotCoverPresenter(pilotFragment.this, getActivity().getApplicationContext(), user);

            if(Type.equalsIgnoreCase("Pilot_Reconcilation"))
            {
                ActionOnReconcilationRequest(false);


            }
            else {


                PilotCoverPresenter.PilotRejectCover(lst_cover.get(0).getArea_ID());
            }
        }
    }

    private void CheckReconcilationDriver(boolean b) {

        //List<Pilot_Cover> lst= DB.checkreconcilation(UserName);
        List<Pilot_Cover>lst=DB.checkreconcilation(mPilot.getPilotID());

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
            if(Type.equalsIgnoreCase("Driver_Reconcilation"))
            {
                if(!ActualClean.getText().toString().isEmpty()
                        &&!ActualDirty.getText().toString().isEmpty())
                      //  &&Integer.valueOf(ActualDirty.getText().toString())<=Integer.valueOf(expDirty.getText().toString())
                      //  &&Integer.valueOf(ActualClean.getText().toString())<=Integer.valueOf(ExpClean.getText().toString())) {
                {
                    lst.get(i-1).setActual_Dirty(ActualDirty.getText().toString());
                    lst.get(i-1).setActual_Clean(ActualClean.getText().toString());
                }

                else
                {
                    flag_QTy=false;
                    if(ActualClean.getText().toString().isEmpty())
                            //||
                           // Integer.valueOf(ActualDirty.getText().toString())<=Integer.valueOf(expDirty.getText().toString()))
                        ActualClean.setError(getActivity().getApplicationContext().getResources().getString(R.string.errorQty));
                    else
                        ActualDirty.setError(getActivity().getApplicationContext().getResources().getString(R.string.errorQty));
                    break;
                }



            }
            else {
                if (!ApprovedDirty.getText().toString().isEmpty() &&
                        !ApprovedClean.getText().toString().isEmpty()&&lst.get(i - 1).getActual_Dirty()!=null
                        &&lst.get(i - 1).getActual_Clean()!=null)
                        //&&
                       // Integer.valueOf(ApprovedDirty.getText().toString())<=Integer.valueOf(expDirty.getText().toString())
                       // &&Integer.valueOf(ApprovedClean.getText().toString())<=Integer.valueOf(ExpClean.getText().toString())) {
                {
                    lst.get(i - 1).setApproved_Dirty(ApprovedDirty.getText().toString());
                    lst.get(i - 1).setApproved_clean(ApprovedClean.getText().toString());
                } else {
                    flag_QTy = false;
                    if(lst.get(i - 1).getActual_Dirty()==null)
                    {
                        ApprovedDirty.setError(getActivity().getApplicationContext().getResources().getString(R.string.pilotQty));


                    }
                    else if(lst.get(i - 1).getActual_Clean()==null)
                    {
                        ApprovedClean.setError(getActivity().getApplicationContext().getResources().getString(R.string.pilotQty));

                    }
                    else if (ApprovedClean.getText().toString().isEmpty())
                            //||Integer.valueOf(ApprovedDirty.getText().toString())<=Integer.valueOf(expDirty.getText().toString()))
                        ApprovedClean.setError(getActivity().getApplicationContext().getResources().getString(R.string.errorQty));
                    else
                        ApprovedDirty.setError(getActivity().getApplicationContext().getResources().getString(R.string.errorQty));
                    break;
                }
            }
        }
        if(flag_QTy)
        {


            // send Recociliation
            String Address = GetAddress.GetAddress(getActivity().getApplicationContext(), GetCurrentLocaion.CurrentLoc);
            User user = new User(getActivity().getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", ""), "", GetCurrentLocaion.CurrentLoc, Address);
            PilotCoverPresenter = new PilotCoverPresenter(pilotFragment.this, getActivity().getApplicationContext(), user);
            // check driver,pilot amount
            try {
                if(Type.equalsIgnoreCase("Driver_Reconcilation"))
                {
                    if(!ed_PilotCash.getText().toString().isEmpty()) {
                        PilotCoverPresenter.PilotReconcilation(lst,UserName,ed_PilotCash.getText().toString(),ed_ExpCash.getText().toString());
                    }
                    else {
                        ed_PilotCash.requestFocus();
                        ed_PilotCash.setError(getActivity().getResources().getString(R.string.errfillPilotAmount));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    private void ActionOnReconcilationRequest(boolean b) {

        //List<Pilot_Cover> lst= DB.checkreconcilation(UserName);
        List<Reconcilation>lst=DB.GetRquestReconcilation(mPilot.getPilotID());

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
                if (!ApprovedDirty.getText().toString().isEmpty() &&
                        !ApprovedClean.getText().toString().isEmpty()&&lst.get(i - 1).getACTUAL_DIRTY()!=null
                        &&lst.get(i - 1).getACTUAL_DIRTY()!=null)
                //&&
                // Integer.valueOf(ApprovedDirty.getText().toString())<=Integer.valueOf(expDirty.getText().toString())
                // &&Integer.valueOf(ApprovedClean.getText().toString())<=Integer.valueOf(ExpClean.getText().toString())) {
                {
                    lst.get(i - 1).setAPPROVED_DIRTY(ApprovedDirty.getText().toString());
                    lst.get(i - 1).setAPPROVED_CLEAN(ApprovedClean.getText().toString());
                } else {
                    flag_QTy = false;
                    if(lst.get(i - 1).getACTUAL_DIRTY()==null)
                    {
                        ApprovedDirty.setError(getActivity().getApplicationContext().getResources().getString(R.string.pilotQty));


                    }
                    else if(lst.get(i - 1).getACTUAL_CLEAN()==null)
                    {
                        ApprovedClean.setError(getActivity().getApplicationContext().getResources().getString(R.string.pilotQty));

                    }
                    else if (ApprovedClean.getText().toString().isEmpty())
                        //||Integer.valueOf(ApprovedDirty.getText().toString())<=Integer.valueOf(expDirty.getText().toString()))
                        ApprovedClean.setError(getActivity().getApplicationContext().getResources().getString(R.string.errorQty));
                    else
                        ApprovedDirty.setError(getActivity().getApplicationContext().getResources().getString(R.string.errorQty));
                    break;
                }

        }
        if(flag_QTy)
        {


            // send Recociliation
            String Address = GetAddress.GetAddress(getActivity().getApplicationContext(), GetCurrentLocaion.CurrentLoc);
            User user = new User(getActivity().getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", ""), "", GetCurrentLocaion.CurrentLoc, Address);
            PilotCoverPresenter = new PilotCoverPresenter(pilotFragment.this, getActivity().getApplicationContext(), user);
            // check driver,pilot amount
            try {

                    if(!ed_DriverCash.getText().toString().isEmpty())

                        PilotCoverPresenter.DriverReconcilation(lst, b,mPilot.getASSIGN_PILOT_ID(),ed_DriverCash.getText().toString(),ed_ExpCash.getText().toString());
                    else
                    {
                        ed_DriverCash.requestFocus();
                        ed_DriverCash.setError(getActivity().getResources().getString(R.string.errfillDriverAmount));
                    }

            } catch (JSONException e) {
                e.printStackTrace();
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

        String Address= GetAddress.GetAddress(getActivity().getApplicationContext(), GetCurrentLocaion.CurrentLoc);
        User user=new User(getActivity().getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", ""),"",GetCurrentLocaion.CurrentLoc,Address);
        PilotCoverPresenter=new PilotCoverPresenter(pilotFragment.this,getActivity().getApplicationContext(),user);

      // CheckCashing();

    }

    @Override
    public void Fail() {
   StopLoading();
    }
    public boolean CheckCashing() {

        List<OFFLINE> lst_off=DB.GetCashingRequest();
        String Address= GetAddress.GetAddress(getActivity().getApplicationContext(), GetCurrentLocaion.CurrentLoc);
        User user=new User(getActivity().getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", ""),"",GetCurrentLocaion.CurrentLoc,Address);
        IContractOffline.Presenter offlinePresnter=new OfflinePresenter(pilotFragment.this, getActivity().getApplicationContext(), user);
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
            PilotCoverPresenter=new PilotCoverPresenter(pilotFragment.this,getActivity().getApplicationContext(),user);
            PilotCoverPresenter.GetReconcilationRequest();
            PilotCoverPresenter.GetcoverPilot();
            PilotCoverPresenter.CheckProductsPilot();
            PilotCoverPresenter.GetCoverAllPilot();

        }
        return  true;
    }
}

