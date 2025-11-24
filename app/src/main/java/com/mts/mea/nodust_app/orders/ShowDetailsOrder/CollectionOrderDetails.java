package com.mts.mea.nodust_app.orders.ShowDetailsOrder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.mts.mea.nodust_app.Adapters.CloseCodeAdapter;
import com.mts.mea.nodust_app.BackgroundServices.GetCurrentLocaion;
import com.mts.mea.nodust_app.CloseCodes_Groups.ActionObject;
import com.mts.mea.nodust_app.CloseCodes_Groups.CloseCode;
import com.mts.mea.nodust_app.Interfaces.ServerManager;
import com.mts.mea.nodust_app.Login.loginActivity;
import com.mts.mea.nodust_app.OFFLINE.IContractOffline;
import com.mts.mea.nodust_app.OFFLINE.OFFLINE;
import com.mts.mea.nodust_app.OFFLINE.OfflinePresenter;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.ServerURI;
import com.mts.mea.nodust_app.User.User;
import com.mts.mea.nodust_app.common.Constants;
import com.mts.mea.nodust_app.common.DataBaseHelper;
import com.mts.mea.nodust_app.common.GetAddress;
import com.mts.mea.nodust_app.common.InputFilterMinMax;
import com.mts.mea.nodust_app.orders.SetAssignmentAction.AssigActionPresenter;
import com.mts.mea.nodust_app.orders.SetAssignmentAction.IContractAssignAction;
import com.mts.mea.nodust_app.orders.Task;
import com.mts.mea.nodust_app.products.Collection;
import com.mts.mea.nodust_app.products.Product;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.mts.mea.nodust_app.User.User.UserName;

public class CollectionOrderDetails extends AppCompatActivity implements IContractOffline.View, IContractAssignAction.View {
    IContractOrderDetail.Presenter mPresent;
    LinearLayout ll_group1, ll_group2, ll_RNo;
    Button btn_partial, btn_deliver, btn_notDeliver,btn_cancelation;
    TextView tv_driveID, tv_cardNo, tv_areaName, tv_showMore, tv_custName, tv_operationComment, tv_dataComment,tv_FUNote;
    TextView tv_flatNo,tv_floorNo,tv_homeNo,tv_remarks,tv_area2name,tv_streetName;
    private SharedPreferences sharedPreferences;
    IContractAssignAction.Presenter presenter;
    ProgressDialog pbDailog;
    DataBaseHelper DB;
    TableLayout table;
    Task task;
    boolean flag = false;
    List<CloseCode> mCloseCodes,CloseCodeCancelation;
    String Contract_status;
    User user;
    String closeCode = "";
    String CancelcloseCode = "";
    boolean GPS_FLAG = true;
    LocationManager locationManager;
    boolean isGPSEnabled;
    boolean Flag_notDeliver=false;
    List<Product> ProductLst;
    ActionObject TmpActionObject;
    String Type;
    android.support.v7.widget.CardView card_prod;
    List<Product> Replaced_product,NotReplaced_product,HandlesProducts,CancelationProducts;
    TextView tv_totalPrice,tv_RecNo;
    TableLayout tab_layoutPenality;
    LinearLayout ll_penalty;
    List<Product>penalties_products;
    private ArrayList<Spinner> lst_Spinner;
    private String txt_msg;
    private String Products_NotChanged="";
    private String Products_Changed="";
    private String Products_Canceled="";
    private List<Collection> lst_coll;
    private InputFilter filter;
    private  String AcceptCharacterSet;
    private double Actual_paid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_order_details);
        task = (Task) getIntent().getExtras().get("Task");
        DB = new DataBaseHelper(this);
        table = (TableLayout) findViewById(R.id.tab_product);
        tab_layoutPenality=(TableLayout) findViewById(R.id.tab_PenaltyProduct);
        card_prod=(android.support.v7.widget.CardView)findViewById(R.id.car_products);
        tv_totalPrice=(TextView)findViewById(R.id.tv_totalPrice);
        ll_penalty=(LinearLayout)findViewById(R.id.ll_penalty);
        btn_deliver = (Button) findViewById(R.id.btn_deliver);
        btn_notDeliver = (Button) findViewById(R.id.btn_notDeliver);
        btn_partial = (Button) findViewById(R.id.btn_partial);


        txt_msg="";

        InitUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

        }
    }

    private void InitUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getApplicationContext().getResources().getString(R.string.DetailsActivity));
        setSupportActionBar(toolbar);
        /// handle actions
        btn_partial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contract_status = "Partially Delivered";
                GetCloseCode(Constants.Group3_partial, true);

            }
        });
        btn_notDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contract_status = "Not Delivered";
                GetCloseCode(Constants.Group2_notDeliver, false);


            }
        });
        btn_deliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contract_status = "Delivered";
                GetCloseCode(Constants.Group1_deliver, true);


            }
        });
        ////
        task = (Task) getIntent().getExtras().get("Task");
        if (task.getRecieptNo() == null)
            task.setRecieptNo("");
        if (task.getDuration() == null)
            task.setDuration("0"); // spider
        if (task.getRegion_name() == null) {
            task.setRegion_name("");
        }
        if (task.getAreaName() == null) {
            task.setAreaName("");
        }
        if (task.getRemarks() == null)
            task.setRemarks("");
        if (task.getFlat_no() == null)
            task.setFlat_no("");
        if (task.getFloor_no() == null)
            task.setFloor_no("");
        if (task.getStreet_name() == null)
            task.setStreet_name("");
        if (task.getPAY_CREDIT() == null)
            task.setPAY_CREDIT("false");
        if (task.getPAY_CREDIT().equalsIgnoreCase("f"))
            task.setPAY_CREDIT("false");
        if (task.getPAY_CREDIT().equalsIgnoreCase("t"))
            task.setPAY_CREDIT("true");
        if (task.getCUSTOMER_TYPE() == null)
            task.setCUSTOMER_TYPE("null");
        Type = getIntent().getExtras().getString("Type");

        ll_group1 = (LinearLayout) findViewById(R.id.ll_group1);
        ll_group2 = (LinearLayout) findViewById(R.id.ll_group2);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        //getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!UserName.equalsIgnoreCase(task.getPilotID())) {
            btn_partial.setVisibility(View.GONE);
            btn_deliver.setVisibility(View.GONE);
            btn_notDeliver.setVisibility(View.GONE);
            table.setVisibility(View.INVISIBLE);

        }
        tv_driveID = (TextView) findViewById(R.id.tv_driveID);
        tv_cardNo = (TextView) findViewById(R.id.tv_cardNo);
        tv_areaName = (TextView) findViewById(R.id.tv_areaName);
        tv_showMore = (TextView) findViewById(R.id.tv_showMore);
        tv_custName = (TextView) findViewById(R.id.tv_custName);
        tv_operationComment = (TextView) findViewById(R.id.tv_operationComment);
        tv_dataComment = (TextView) findViewById(R.id.tv_dataComment);
        tv_flatNo = (TextView) findViewById(R.id.tv_FlatNo);
        tv_floorNo = (TextView) findViewById(R.id.tv_FloorNo);
        tv_homeNo = (TextView) findViewById(R.id.tv_HomeNo);
        tv_remarks = (TextView) findViewById(R.id.tv_Remarks);
        tv_area2name = (TextView) findViewById(R.id.tv_area2Name);
        tv_streetName = (TextView) findViewById(R.id.tv_StreetName);
        tv_FUNote = (TextView) findViewById(R.id.tv_FUNote);
        tv_RecNo = (TextView) findViewById(R.id.tv_RecNo);


        sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);

        if (sharedPreferences.contains("UserName")) {
            UserName = sharedPreferences.getString("UserName", "");
        }

        if (!isGPSEnabled || GetCurrentLocaion.CurrentLoc == null) {
            ShowGPSAlert();
        } else {
            String Address = GetAddress.GetAddress(getApplicationContext(), GetCurrentLocaion.CurrentLoc);
            user = new User(UserName, "", GetCurrentLocaion.CurrentLoc, Address);

            try {
                setTaskdetails(task);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    public void ShowGPSAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CollectionOrderDetails.this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                CollectionOrderDetails.this.startActivity(intent);
                finish();
            }
        });

        // On pressing the cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
    public void setTaskdetails(Task task) throws ParseException {
        tv_driveID.setText(task.getDriveID());
        tv_cardNo.setText(task.getCardNo());
        tv_areaName.setText(task.getAreaName());
        tv_custName.setText(task.getClientName());
        //  tv_custAddress.setText(task.getCustomerAddress());
        tv_operationComment.setText(task.getOperationComment());
        tv_dataComment.setText(task.getDataComment());
        tv_flatNo.setText(task.getFlat_no());
        tv_floorNo.setText(task.getFloor_no());
        tv_homeNo.setText(task.getHome_no());
        tv_streetName.setText(task.getStreet_name());
        tv_area2name.setText(task.getADDRESS());
        tv_remarks.setText(task.getRemarks());
        tv_RecNo.setText(String.valueOf(task.getRecieptNo()));
        if(task.getFU_Note()!=null) {
            String text=task.getFU_Note()+"\n";
            if(task.getFrom_time()!=null)
            {
                text+=getApplicationContext().getResources().getString(R.string.FromTime)+" "+task.getFrom_time();
            }
            if(task.getTo_time()!=null)
            {
                text+=" ";

                text+=getApplicationContext().getResources().getString(R.string.ToTime)+" "+task.getTo_time();

            }
            tv_FUNote.setText(text);
        }
        else
        {
            String text=" ";
            if(task.getFrom_time()!=null)
            {
                text+=getApplicationContext().getResources().getString(R.string.FromTime)+" "+task.getFrom_time();
            }
            if(task.getTo_time()!=null)
            {
                text+=" ";

                text+=getApplicationContext().getResources().getString(R.string.ToTime)+" "+task.getTo_time();

            }
            tv_FUNote.setText(text);
        }
        // PRODUCTS
        GetCollection();


    }

    private void GetCollection() {
        lst_coll = DB.GetCollection(Integer.parseInt(task.getAID()));
        if(lst_coll==null || lst_coll.size()==0)
        {
            table.setVisibility(View.GONE);
            btn_deliver.setVisibility(View.GONE);
            btn_notDeliver.setVisibility(View.GONE);
            btn_partial.setVisibility(View.GONE);
        }
        else {
            double Total_amount=0;
            for (int i = 0; i < lst_coll.size(); i++) {
                Total_amount+=lst_coll.get(i).getAMOUNT();

                TableRow inflate = new TableRow(this);
                inflate = (TableRow) View.inflate(this, R.layout.collection_row, null);
                table.addView(inflate);
                GetRows_col(i + 1, lst_coll.get(i));
                if(i==lst_coll.size()-1)
                {
                    inflate = new TableRow(this);
                    inflate = (TableRow) View.inflate(this, R.layout.last_row, null);
                    table.addView(inflate);
                    LinearLayout linearLayout_0 = (LinearLayout) inflate.getChildAt(0);
                    if (inflate.getId() == R.id.last_row) {
                        final TextView price = (TextView) linearLayout_0.getChildAt(0);
                        price.setText(getApplicationContext().getResources().getString(R.string.Total) + "=" + String.valueOf(Total_amount) +" "+ getApplicationContext().getResources().getString(R.string.EGP));
                    }
                }

            }
            Actual_paid=Total_amount;
        }
    }

    private void GetRows_col(int index_row, final Collection collection) {

        View child = table.getChildAt(index_row);
        if (child instanceof TableRow) {
            TableRow row = (TableRow) child;
            LinearLayout linearLayout_0 = (LinearLayout) row.getChildAt(0);
            TextView invoice_NO = (TextView) linearLayout_0.getChildAt(0);
            invoice_NO.setText(collection.getINVOICE_NO());
            TextView date = (TextView) linearLayout_0.getChildAt(1);
            if(collection.getDATE1()!=null)
            date.setText(String.valueOf(collection.getDATE1()));
            else
                date.setText(String.valueOf(""));

            TextView amount = (TextView) linearLayout_0.getChildAt(2);
            amount.setText(String.valueOf(collection.getAMOUNT()));
            EditText paid = (EditText) linearLayout_0.getChildAt(3);
            paid.setText(String.valueOf(collection.getAMOUNT()));
            paid.setFilters(new InputFilter[]{ new InputFilterMinMax("0", String.valueOf(collection.getAMOUNT()))});
            collection.setPAID_AMOUNT(Double.valueOf(String.valueOf(collection.getAMOUNT())));

            //  paid.setFilters(new InputFilter[] { filter });
            paid.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(!s.toString().isEmpty()&&!s.toString().equalsIgnoreCase("."))
                    collection.setPAID_AMOUNT(Double.valueOf(s.toString()));
                    else
                        collection.setPAID_AMOUNT(Double.valueOf("0"));

                    // update total price and buttons
                    UpdatePrice_btns();
                }
            });
        }
    }

    private void UpdatePrice_btns() {
        double total_paid=0;
         Actual_paid=0;
        for(int i=0;i<lst_coll.size();i++) {
            Actual_paid += lst_coll.get(i).getPAID_AMOUNT();
            total_paid += lst_coll.get(i).getAMOUNT();
        }

        if(Actual_paid==0)
        {
            // not done
            btn_notDeliver.setVisibility(View.VISIBLE);
            btn_deliver.setVisibility(View.GONE);
            btn_partial.setVisibility(View.GONE);
        }
        else if(total_paid>Actual_paid)
        {
            btn_notDeliver.setVisibility(View.VISIBLE);
            btn_deliver.setVisibility(View.GONE);
            btn_partial.setVisibility(View.VISIBLE);
        }
        else
        {
            btn_notDeliver.setVisibility(View.VISIBLE);
            btn_deliver.setVisibility(View.VISIBLE);
            btn_partial.setVisibility(View.GONE);
        }
        TableRow row = (TableRow) table.getChildAt(table.getChildCount() - 1);
        LinearLayout linearLayout_0 = (LinearLayout) row.getChildAt(0);
        if (row.getId() == R.id.last_row) {
            final TextView price = (TextView) linearLayout_0.getChildAt(0);
            price.setText(getApplicationContext().getResources().getString(R.string.Total) + "=" + String.valueOf(Actual_paid) +" "+ getApplicationContext().getResources().getString(R.string.EGP));
        }

    }

    public void GetCloseCode(String Key, boolean status) {

        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isGPSEnabled) {
            String GID = null;
            final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_closeCode, 0);
            if (sharedPreferences.contains(Key)) {
                String[] tmp = sharedPreferences.getString(Key, "").split("\\*");
                CloseCode[] arr = ServerManager.deSerializeStringToObject(tmp[1], CloseCode[].class);
                mCloseCodes = Arrays.asList(arr);
                GID = tmp[0];
            }
            ShowPoPUp(mCloseCodes, GID, status);
        } else
            ShowGPSAlert();
    }

    public void ShowPoPUp(final List<CloseCode> lst_closeCode, final String GID, final boolean status) {
        Flag_notDeliver = !status;
        List<String> lst_clos = new ArrayList<>();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.My_Prefs_language_NoDust, 0);
        if (sharedPreferences.contains("SelectedLanguage")) {
            if (sharedPreferences.getString("SelectedLanguage", "En").equalsIgnoreCase("English")) {
                if (mCloseCodes != null && mCloseCodes.size() > 0) {
                    for (int i = 0; i < lst_closeCode.size(); i++) {
                        lst_clos.add(lst_closeCode.get(i).getCLOSE_CODE_REASON_EN());
                    }
                }
            } else {
                if (mCloseCodes != null && mCloseCodes.size() > 0) {
                    for (int i = 0; i < lst_closeCode.size(); i++) {
                        lst_clos.add(lst_closeCode.get(i).getCloseCodeReason());
                    }
                }
            }
        } else {

            if (mCloseCodes != null && mCloseCodes.size() > 0) {
                for (int i = 0; i < lst_closeCode.size(); i++) {
                    lst_clos.add(lst_closeCode.get(i).getCLOSE_CODE_REASON_EN());
                }
            }
        }
        final Dialog dialog = new Dialog(CollectionOrderDetails.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_close_code);
        final Spinner spinner = (Spinner) dialog.findViewById(R.id.sp_closeCode);
        final EditText Comments = (EditText) dialog.findViewById(R.id.ed_comments);
        final CheckBox ch_fullpaiment=(CheckBox) dialog.findViewById(R.id.ch_fullpayment);
        final CheckBox ch_NoMomey=(CheckBox)dialog.findViewById(R.id.ch_NoMoney);
        final CheckBox ch_payCash=(CheckBox)dialog.findViewById(R.id.ch_payCash);
        ch_fullpaiment.setVisibility(View.GONE);
        ch_NoMomey.setVisibility(View.GONE);
        ch_payCash.setVisibility(View.GONE);
        CloseCodeAdapter Adapter = new CloseCodeAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, lst_clos);
        spinner.setAdapter(Adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                closeCode = lst_closeCode.get(position).getCloseCodeID();
                flag = true;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
        Button send = (Button) dialog.findViewById(R.id.btn_ok);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean Tmpflag = true;

                if (flag && mCloseCodes != null) {
                    String AssignmentID = task.getAID();
                    String CloseCode = closeCode;
                    String CloseCodeReason = spinner.getSelectedItem().toString();
                    String CloseCodeCancel = "";

                    String comments = Comments.getText().toString();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    String ActionDate = df.format(new Date());
                    ActionObject actionObject = new ActionObject();
                    actionObject.setNoMoney(false);
                    actionObject.setActionDate(ActionDate);
                    actionObject.setAssignmentID(AssignmentID);
                    actionObject.setCloseCode(CloseCode);
                    actionObject.setCloseCodeReason(CloseCodeReason);
                    actionObject.setCancelcloseCodeReason(CloseCodeCancel);
                    actionObject.setCancelcloseCode(CancelcloseCode);
                    actionObject.setComments(comments);
                    actionObject.setLatitude(GetCurrentLocaion.CurrentLoc.getLatitude());
                    actionObject.setLongitude(GetCurrentLocaion.CurrentLoc.getLongitude());
                    actionObject.setHandles(new ArrayList<Product>());
                    actionObject.setContractStatus(GID);
                    actionObject.setDRNo(task.getRecieptNo());
                    actionObject.setCARD_NO(task.getCardNo());
                    actionObject.setDeliveryMan(UserName);
                    actionObject.setCurrentTask(task);
                    actionObject.setTOTAL_PRICE(Actual_paid);
                    actionObject.setActual_paid(Actual_paid);
                    actionObject.setReplacedProducts(new ArrayList<Product>());
                    actionObject.setHandles(new ArrayList<Product>());
                    if(Contract_status.equalsIgnoreCase("Not Delivered"))
                    {
                        actionObject.setTOTAL_PRICE(0);
                        actionObject.setActual_paid(0);
                        actionObject.setCollection(new ArrayList<Collection>());
                    }
                    else
                    {
                        actionObject.setCollection(lst_coll);

                    }
                    TmpActionObject=actionObject;
                    CheckCashing();

                }
                if (Tmpflag) {
                    dialog.dismiss();
                }
            }
        });
   dialog.show();

    }
    public boolean CheckCashing() {

        List<OFFLINE> lst_off=DB.GetCashingRequest();
        user = new User(UserName, "", GetCurrentLocaion.CurrentLoc, " ");
        IContractOffline.Presenter offlinePresnter=new OfflinePresenter(CollectionOrderDetails.this, getApplicationContext(), user);
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
       CollectionOrderDetails.this.StopLoading();
            String Address = GetAddress.GetAddress(getApplicationContext(), GetCurrentLocaion.CurrentLoc);
            user = new User(UserName, "", GetCurrentLocaion.CurrentLoc, Address);
            presenter = new AssigActionPresenter(this, getApplicationContext(), user);

            try {
                presenter.setAssignmentAction(TmpActionObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  true;
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
        String Address = GetAddress.GetAddress(getApplicationContext(), GetCurrentLocaion.CurrentLoc);
        user = new User(UserName, "", GetCurrentLocaion.CurrentLoc, Address);
        presenter = new AssigActionPresenter(this, getApplicationContext(), user);
        CollectionOrderDetails.this.StopLoading();
        try {
            presenter.setAssignmentAction(TmpActionObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void Fail() {
        StopLoading();
        Type="ACTION";
        if(Type.equalsIgnoreCase("ACTION"))
        {
            DB.UpdateStatus(task.getAID(),Contract_status,TmpActionObject.getActual_paid());
            OFFLINE offline=new OFFLINE();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            String ActionDate = df.format(new Date());
            offline.setACTIONDate(ActionDate);
            offline.setAID(task.getAID());
            offline.setUSERNAME(user.getUserName());
            offline.setDESCRIPTION(user.getDescription());
            offline.setLATITUDE(String.valueOf(user.getCurrentLocation().getLatitude()));
            offline.setLONGITUDE(String.valueOf(user.getCurrentLocation().getLongitude()));
            String query=ServerManager.serializeObjectToString(TmpActionObject);
            offline.setBODY(query);
            String url= ServerURI.SetAssignmentAction();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String currentDateandTime = sdf.format(new Date());
            url+="/CHECKTIME="+currentDateandTime;
            offline.setURL(url);
            offline.setTYPE("ACTION");
            DB.AddCashingRequests(offline);

            // Add Sms
            AddSmsINCashing();

        }
        else if(Type.equalsIgnoreCase("SMS"))
        {
            AddSmsINCashing();
        }
        finish();

    }
    public void  AddSmsINCashing()
    {
        OFFLINE offline=new OFFLINE();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String ActionDate = df.format(new Date());
        offline.setACTIONDate(ActionDate);
        offline.setAID(task.getAID());
        offline.setUSERNAME(user.getUserName());
        offline.setDESCRIPTION(user.getDescription());
        offline.setLATITUDE(String.valueOf(user.getCurrentLocation().getLatitude()));
        offline.setLONGITUDE(String.valueOf(user.getCurrentLocation().getLongitude()));
        offline.setBODY("");
        String url= ServerURI.sendSMS();
        url+="/Telno="+task.getTelNo();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        final String currentDateandTime = sdf.format(new Date());
        url+="/CHECKTIME="+currentDateandTime;
        offline.setURL(url);
        offline.setTYPE("SMS");
      //  DB.AddCashingRequests(offline);
    }
    public void StopLoading() {
        if(pbDailog!=null && pbDailog.isShowing())
            pbDailog.dismiss();

    }

    @Override
    public void SendSms() {

    }

    @Override
    public void ShowMsg(String Type, String msg) {
        if (msg.equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.SaveChanges))) {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            sharedPreferencesEditor.putString("TASKID", task.getAssignmentId());
            sharedPreferencesEditor.apply();
            new CloseActivityAfterUpdate().execute();
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }
        else if(msg.equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.SaveChanges) + "fail"))
        {
            DB.UpdateStatus(task.getAID(),Contract_status,TmpActionObject.getActual_paid());
            //  DB.UpdateAllData(null,false);
            //  finish();
            new CloseActivityAfterUpdate2().execute();
        }
        else
        {
            // Update Status action
            // update cover action
            if(Type.equalsIgnoreCase("ACTION"))
            {
                DB.UpdateStatus(task.getAID(),Contract_status,TmpActionObject.getActual_paid());
                     OFFLINE offline=new OFFLINE();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                String ActionDate = df.format(new Date());
                offline.setACTIONDate(ActionDate);
                offline.setAID(task.getAID());
                offline.setUSERNAME(user.getUserName());
                offline.setDESCRIPTION(user.getDescription());
                offline.setLATITUDE(String.valueOf(user.getCurrentLocation().getLatitude()));
                offline.setLONGITUDE(String.valueOf(user.getCurrentLocation().getLongitude()));
                String query=ServerManager.serializeObjectToString(TmpActionObject);
                offline.setBODY(query);
                String url= ServerURI.SetAssignmentAction();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                String currentDateandTime = sdf.format(new Date());
                url+="/CHECKTIME="+currentDateandTime;
                offline.setURL(url);
                offline.setTYPE("ACTION");
                DB.AddCashingRequests(offline);

                // Add Sms
                AddSmsINCashing();

            }
            else if(Type.equalsIgnoreCase("SMS"))
            {
                AddSmsINCashing();
            }
            finish();
            // Add Cashing Requests

        }


    }

    @Override
    public void SendAction() {

    }

    public void ShowLoading() {
        pbDailog = new ProgressDialog(CollectionOrderDetails.this);
        pbDailog.setMessage(getApplicationContext().getResources().getString(R.string.Loading));
        pbDailog.setCancelable(false);
        pbDailog.show();
    }
    class CloseActivityAfterUpdate2 extends AsyncTask<String, Void, Void>
    {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            DB.UpdateAfterAction(null,false);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected Void doInBackground(String... params) {

            return null;
        }
    }
    class CloseActivityAfterUpdate extends AsyncTask<String, Void, Void>
    {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            finish();
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            DB.UpdateStatus(task.getAID(),Contract_status,TmpActionObject.getActual_paid());
            DB.UpdateAfterAction(null,false);

        //    if(task.getTelNo()!=null)
          //      presenter.SendSMS(task.getTelNo(), TmpActionObject.getAssignmentID()," "," "," ");

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected Void doInBackground(String... params) {

            return null;
        }
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
