package com.mts.mea.nodust_app.orders.ShowDetailsOrder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.mts.mea.nodust_app.Adapters.CloseCodeAdapter;
import com.mts.mea.nodust_app.Adapters.PilotAdapter;
import com.mts.mea.nodust_app.BackgroundServices.GetCurrentLocaion;
import com.mts.mea.nodust_app.CloseCodes_Groups.ActionObject;
import com.mts.mea.nodust_app.CloseCodes_Groups.CloseCode;
import com.mts.mea.nodust_app.Interfaces.ServerManager;
import com.mts.mea.nodust_app.Login.loginActivity;
import com.mts.mea.nodust_app.OFFLINE.IContractOffline;
import com.mts.mea.nodust_app.OFFLINE.OFFLINE;
import com.mts.mea.nodust_app.OFFLINE.OfflinePresenter;
import com.mts.mea.nodust_app.Pilots.Pilot;
import com.mts.mea.nodust_app.Pilots.PilotCover.Pilot_Cover;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.ServerURI;
import com.mts.mea.nodust_app.User.User;
import com.mts.mea.nodust_app.common.Constants;
import com.mts.mea.nodust_app.common.DataBaseHelper;
import com.mts.mea.nodust_app.common.GetAddress;
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

public class OrderDetailsActivity extends AppCompatActivity implements IContractOrderDetail.View,
        IContractAssignAction.View,View.OnClickListener, IContractOffline.View {
    IContractOrderDetail.Presenter mPresent;
    LinearLayout ll_group1, ll_group2, ll_RNo;
    Button btn_partial, btn_deliver, btn_notDeliver,btn_cancelation;
    TextView tv_driveID, tv_cardNo, tv_areaName, tv_showMore, tv_custName, tv_operationComment, tv_dataComment,tv_FUNote;
    TextView tv_flatNo,tv_floorNo,tv_homeNo,tv_remarks,tv_area2name,tv_streetName;
    private SharedPreferences sharedPreferences;
    IContractAssignAction.Presenter presenter;
    ProgressDialog pbDailog;
    EditText b1, b2, b3, b4, b5, b6, b7, b8;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        task = (Task) getIntent().getExtras().get("Task");
        DB = new DataBaseHelper(this);
        table = (TableLayout) findViewById(R.id.tab_product);
        tab_layoutPenality=(TableLayout) findViewById(R.id.tab_PenaltyProduct);
        card_prod=(android.support.v7.widget.CardView)findViewById(R.id.car_products);
        tv_totalPrice=(TextView)findViewById(R.id.tv_totalPrice);
        ll_penalty=(LinearLayout)findViewById(R.id.ll_penalty);
        btn_cancelation=(Button)findViewById(R.id.btn_cancelation);
        txt_msg="";
        InitUI();
    }

    @Override
    protected void onResume() {
        if (!GPS_FLAG) {
            finish();
        }
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
        task = (Task) getIntent().getExtras().get("Task");
        if(task.getRecieptNo()==null)
            task.setRecieptNo("");
        if(task.getDuration()==null)
            task.setDuration("0"); // spider
        if(task.getRegion_name()==null)
        {
            task.setRegion_name("");
        }
        if(task.getAreaName()==null)
        {
            task.setAreaName("");
        }
        if(task.getRemarks()==null)
            task.setRemarks("");
        if(task.getFlat_no()==null)
            task.setFlat_no("");
        if(task.getFloor_no()==null)
            task.setFloor_no("");
        if(task.getStreet_name()==null)
            task.setStreet_name("");
        if(task.getPAY_CREDIT()==null)
            task.setPAY_CREDIT("false");
        if(task.getPAY_CREDIT().equalsIgnoreCase("f"))
            task.setPAY_CREDIT("false");
        if(task.getPAY_CREDIT().equalsIgnoreCase("t"))
            task.setPAY_CREDIT("true");
        if(task.getCUSTOMER_TYPE()==null)
            task.setCUSTOMER_TYPE("null");
        Type=getIntent().getExtras().getString("Type");

        ll_group1 = (LinearLayout) findViewById(R.id.ll_group1);
        ll_group2 = (LinearLayout) findViewById(R.id.ll_group2);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        //getting GPS status
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        btn_partial = (Button) findViewById(R.id.btn_partial);
        btn_partial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contract_status = "Partially Delivered";
                flag = false;
                mCloseCodes = null;
                GetCloseCode(Constants.Group3_partial, true);

                /*if(CheckProductsPilots())
                GetCloseCode(Constants.Group3_partial, true);
                else
                Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.enoughProduct),Toast.LENGTH_SHORT).show();*/

            }
        });
        btn_deliver = (Button) findViewById(R.id.btn_deliver);
        btn_deliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contract_status = "Delivered";
                flag = false;
                mCloseCodes = null;
                GetCloseCode(Constants.Group1_deliver, true);

                /*if(CheckProductsPilots())
                GetCloseCode(Constants.Group1_deliver, true);
                else
                    Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.enoughProduct),Toast.LENGTH_SHORT).show();*/


            }
        });
        btn_notDeliver = (Button) findViewById(R.id.btn_notDeliver);
        btn_notDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               flag = false;
                Contract_status = "Not Delivered";
                mCloseCodes = null;
                GetCloseCode(Constants.Group2_notDeliver, false);

              /*  if(CheckProductsPilots())
                GetCloseCode(Constants.Group2_notDeliver, false);
                else
                    Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.enoughProduct),Toast.LENGTH_SHORT).show();*/

            }
        });
        btn_cancelation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;
                Contract_status = "Cancelation";
                mCloseCodes = null;
                GetCloseCode(Constants.Group4_Cancelation, false);

              /*  if(CheckProductsPilots())
                    GetCloseCode(Constants.Group4_Cancelation, false);
                else
                    Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.enoughProduct),Toast.LENGTH_SHORT).show();*/

            }
        });
       if (!UserName.equalsIgnoreCase(task.getPilotID()))
        {
            btn_partial.setVisibility(View.GONE);
            btn_deliver.setVisibility(View.GONE);
            btn_notDeliver.setVisibility(View.GONE);
            card_prod.setVisibility(View.INVISIBLE);

        }
        tv_driveID = (TextView) findViewById(R.id.tv_driveID);
        tv_cardNo = (TextView) findViewById(R.id.tv_cardNo);
        tv_areaName = (TextView) findViewById(R.id.tv_areaName);
        tv_showMore = (TextView) findViewById(R.id.tv_showMore);
        tv_custName = (TextView) findViewById(R.id.tv_custName);
        tv_operationComment = (TextView) findViewById(R.id.tv_operationComment);
        tv_dataComment = (TextView) findViewById(R.id.tv_dataComment);
        tv_flatNo=(TextView)findViewById(R.id.tv_FlatNo);
        tv_floorNo=(TextView)findViewById(R.id.tv_FloorNo);
        tv_homeNo=(TextView)findViewById(R.id.tv_HomeNo);
        tv_remarks=(TextView)findViewById(R.id.tv_Remarks);
        tv_area2name=(TextView)findViewById(R.id.tv_area2Name);
        tv_streetName=(TextView)findViewById(R.id.tv_StreetName);
        tv_FUNote=(TextView)findViewById(R.id.tv_FUNote);
        tv_RecNo=(TextView)findViewById(R.id.tv_RecNo);


        sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);

        if (sharedPreferences.contains("UserName")) {
            UserName = sharedPreferences.getString("UserName", "");
        }

        if (!isGPSEnabled || GetCurrentLocaion.CurrentLoc == null) {
            ShowGPSAlert();
        } else {
            String Address = GetAddress.GetAddress(getApplicationContext(), GetCurrentLocaion.CurrentLoc);
            user = new User(UserName, "", GetCurrentLocaion.CurrentLoc, Address);
            mPresent = new OrderDetailsPresenter(this, getApplicationContext(), user);
            presenter = new AssigActionPresenter(this, getApplicationContext(), user);
            // mPresent.getTaskdetails(task.getAssignmentId());
            try {
                setTaskdetails(task);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(UserName.equalsIgnoreCase(task.getPilotID()))
        {
            ProductLst = DB.GetProduct(task.getAID());
            Replaced_product=new ArrayList<>();
            NotReplaced_product=new ArrayList<>();
            HandlesProducts=new ArrayList<>();
            CancelationProducts=new ArrayList<>();
           // penalties_products =new ArrayList<>();
            int count=0;
            for(int i=0;i<ProductLst.size();i++)
            {
                Product p1 = new Product();
                Product p2 = new Product();
                Product p3=new Product();
                Product p4=new Product();

                p1.setQUNTITY_REPLACED(ProductLst.get(i).getQUNTITY_REPLACED());
                p2.setQUNTITY_REPLACED(ProductLst.get(i).getQUNTITY_REPLACED());
                p3.setQUNTITY_REPLACED(ProductLst.get(i).getQUNTITY());
                p4.setQUNTITY_REPLACED(ProductLst.get(i).getQUNTITY_REPLACED());

                p1.setQUNTITY(ProductLst.get(i).getQUNTITY());
                p2.setQUNTITY(ProductLst.get(i).getQUNTITY());
                p3.setQUNTITY(ProductLst.get(i).getQUNTITY());
                p4.setQUNTITY(ProductLst.get(i).getQUNTITY());

                p1.setBUY_PRICE(ProductLst.get(i).getBUY_PRICE());
                p2.setBUY_PRICE(ProductLst.get(i).getBUY_PRICE());
                p3.setBUY_PRICE(ProductLst.get(i).getBUY_PRICE());
                p4.setBUY_PRICE(ProductLst.get(i).getBUY_PRICE());

                p1.setHANDLE(ProductLst.get(i).getHANDLE());
                p2.setHANDLE(ProductLst.get(i).getHANDLE());
                p3.setHANDLE(ProductLst.get(i).getHANDLE());
                p4.setHANDLE(ProductLst.get(i).getHANDLE());


                p1.setUNIT_PRICE(ProductLst.get(i).getUNIT_PRICE());
                p2.setUNIT_PRICE(ProductLst.get(i).getUNIT_PRICE());
                p3.setUNIT_PRICE(ProductLst.get(i).getUNIT_PRICE());
                p4.setUNIT_PRICE(ProductLst.get(i).getUNIT_PRICE());

                p1.setAID(ProductLst.get(i).getAID());
                p2.setAID(ProductLst.get(i).getAID());
                p3.setAID(ProductLst.get(i).getAID());
                p4.setAID(ProductLst.get(i).getAID());

                p1.setCARD_NO(ProductLst.get(i).getCARD_NO());
                p2.setCARD_NO(ProductLst.get(i).getCARD_NO());
                p3.setCARD_NO(ProductLst.get(i).getCARD_NO());
                p4.setCARD_NO(ProductLst.get(i).getCARD_NO());


                p1.setNAME(ProductLst.get(i).getNAME());
                p2.setNAME(ProductLst.get(i).getNAME());
                p3.setNAME(ProductLst.get(i).getNAME());
                p4.setNAME(ProductLst.get(i).getNAME());

                p1.setPRODUCT_ID(ProductLst.get(i).getPRODUCT_ID());
                p2.setPRODUCT_ID(ProductLst.get(i).getPRODUCT_ID());
                p3.setPRODUCT_ID(ProductLst.get(i).getPRODUCT_ID());
                p4.setPRODUCT_ID(ProductLst.get(i).getPRODUCT_ID());

                p1.setPackage_id(ProductLst.get(i).getPackage_id());
                p2.setPackage_id(ProductLst.get(i).getPackage_id());
                p3.setPackage_id(ProductLst.get(i).getPackage_id());
                p4.setPackage_id(ProductLst.get(i).getPackage_id());

                p1.setPackage_NO(ProductLst.get(i).getPackage_NO());
                p2.setPackage_NO(ProductLst.get(i).getPackage_NO());
                p3.setPackage_NO(ProductLst.get(i).getPackage_NO());
                p4.setPackage_NO(ProductLst.get(i).getPackage_NO());

                p1.setTreatment_code(ProductLst.get(i).getTreatment_code());
                p2.setTreatment_code(ProductLst.get(i).getTreatment_code());
                p3.setTreatment_code(ProductLst.get(i).getTreatment_code());
                p4.setTreatment_code(ProductLst.get(i).getTreatment_code());

                p1.setType(ProductLst.get(i).getType());
                p2.setType(ProductLst.get(i).getType());
                p3.setType(ProductLst.get(i).getType());
                p4.setType(ProductLst.get(i).getType());

                p1.setDescription(ProductLst.get(i).getDescription());
                p2.setDescription(ProductLst.get(i).getDescription());
                p3.setDescription(ProductLst.get(i).getDescription());
                p4.setDescription(ProductLst.get(i).getDescription());

                p1.setTreatment_description(ProductLst.get(i).getTreatment_description());
                p2.setTreatment_description(ProductLst.get(i).getTreatment_description());
                p3.setTreatment_description(ProductLst.get(i).getTreatment_description());
                p4.setTreatment_description(ProductLst.get(i).getTreatment_description());

                p1.setKIND(ProductLst.get(i).getKIND());
                p2.setKIND(ProductLst.get(i).getKIND());
                p3.setKIND(ProductLst.get(i).getKIND());
                p4.setKIND(ProductLst.get(i).getKIND());



                p1.setDELIVERY_TYPE(ProductLst.get(i).getDELIVERY_TYPE());
                p2.setDELIVERY_TYPE(ProductLst.get(i).getDELIVERY_TYPE());
                p3.setDELIVERY_TYPE(ProductLst.get(i).getDELIVERY_TYPE());
                p4.setDELIVERY_TYPE(ProductLst.get(i).getDELIVERY_TYPE());

                // search if PNo found before
                if(p1.getPackage_NO()!=null&&!p1.getPackage_NO().equalsIgnoreCase("0")) {
                    if (Search(p1.getPackage_NO())) {
                        p1.setUNIT_PRICE("0");
                        p2.setUNIT_PRICE("0");
                        ProductLst.get(i).setUNIT_PRICE("0");
                    }
                }
               // if(ProductLst.get(i).getType()!=null&&!ProductLst.get(i).getType().equalsIgnoreCase("penalty")) {
                    Replaced_product.add(p1);
                    NotReplaced_product.add(p2);
                    HandlesProducts.add(p3);
                    CancelationProducts.add(p4);

               // }
                /*else if(ProductLst.get(i).getType()!=null)
                {
                    penalties_products.add(p1);
                    count++;
                }*/
            }
            if(count==0)
                tab_layoutPenality.setVisibility(View.GONE);
           //     ll_penalty.setVisibility(View.GONE);
             /*for (int i = 0; i < ProductLst.size(); i++) {
                    TableRow inflate = new TableRow(this);
                    if(ProductLst.get(i).getType()!=null&&ProductLst.get(i).getType().equalsIgnoreCase("Penalty"))
                    {
                        inflate = (TableRow) View.inflate(this, R.layout.item_product_penalty, null);
                        tab_layoutPenality.addView(inflate);
                        GetRows_colPenality(tab_layoutPenality.getChildCount()-1,ProductLst.get(i));
                    }
                    else
                    {
                        inflate = (TableRow) View.inflate(this, R.layout.attrib_row, null);
                        table.addView(inflate);
                        GetRows_col(table.getChildCount()-1, ProductLst.get(i));
                    }
                }
                TableRow inflate = new TableRow(this);
                inflate = (TableRow) View.inflate(this, R.layout.last_row, null);
                table.addView(inflate);
                if (ProductLst == null || ProductLst.size() == 0)
                    table.setVisibility(View.GONE);*/
           /* TableRow inflate = new TableRow(this);
            inflate = (TableRow) View.inflate(this, R.layout.last_row, null);
            table.addView(inflate);*/
            if (ProductLst == null && ProductLst.size() == 0)
                table.setVisibility(View.GONE);
              /*  if(penalties_products!=null) {
                    for (int i = 0; i < penalties_products.size(); i++) {
                        TableRow inflate = new TableRow(this);
                        inflate = (TableRow) View.inflate(this, R.layout.item_product_penalty, null);
                        tab_layoutPenality.addView(inflate);
                        GetRows_colPenality(i+1, penalties_products.get(i));

                    }
                }*/
                if(Replaced_product!=null)
                {
                    if(Replaced_product.size()==0)
                    {
                        table.setVisibility(View.GONE);
                    }
                    for(int i=0;i<Replaced_product.size();i++) {
                            TableRow inflate = new TableRow(this);
                            inflate = (TableRow) View.inflate(this, R.layout.attrib_row, null);
                            table.addView(inflate);
                            GetRows_col(i + 1, Replaced_product.get(i));
                          if(i==Replaced_product.size()-1) {
                            inflate = new TableRow(this);
                            inflate = (TableRow) View.inflate(this, R.layout.last_row, null);
                            table.addView(inflate);
                        }

                    }

                }
            }

    /*    if(task.getCONTRACT_TYPE()!=null&&task.getCONTRACT_TYPE().equalsIgnoreCase("collection"))
        {
            table.setVisibility(View.GONE);
            tv_totalPrice.setText(getApplicationContext().getResources().getString(R.string.Total)+" "+task.getFINANCIAL_AMOUNT()+" "+getApplicationContext().getResources().getString(R.string.EGP));
            btn_deliver.setVisibility(View.VISIBLE);
            btn_notDeliver.setVisibility(View.GONE);
            btn_partial.setVisibility(View.GONE);
        }*/
 }

    private boolean Search(String package_no) {
        if(Replaced_product!=null)
        {
            for(int i=0;i<Replaced_product.size();i++)
            {
               if( Replaced_product.get(i).getPackage_NO().equalsIgnoreCase(package_no))
               {
                   return true;
               }
            }
            return false;
        }
        return false;
    }
    private double GetActualPriceProduct(String ProductID,String PackageID,String Treatment,int DeliveryType,String PNO)
    {
        for(int i=0;i<ProductLst.size();i++)
        {
            if(ProductLst.get(i).getPackage_id().equalsIgnoreCase(PackageID)
                    &&(ProductLst.get(i).getPRODUCT_ID().equalsIgnoreCase(ProductID))&&
                    (ProductLst.get(i).getTreatment_code().equalsIgnoreCase(Treatment))&&
                    ProductLst.get(i).getDELIVERY_TYPE()==DeliveryType&&ProductLst.get(i).getPackage_NO()==PNO)
              return Double.valueOf(ProductLst.get(i).getUNIT_PRICE());
        }
        return 0;
    }
    private double GetReplacedPriceProduct(String ProductID,String PackageID,String Treatment,int DeliveryType,String PNO)
    {
        for(int i=0;i<Replaced_product.size();i++)
        {
            if(Replaced_product.get(i).getPackage_id().equalsIgnoreCase(PackageID)&&
                    (Replaced_product.get(i).getPRODUCT_ID().equalsIgnoreCase(ProductID))&&
                    (Replaced_product.get(i).getTreatment_code().equalsIgnoreCase(Treatment))
                    &&ProductLst.get(i).getDELIVERY_TYPE()==DeliveryType&&ProductLst.get(i).getPackage_NO()==PNO)
                return Double.valueOf(Replaced_product.get(i).getUNIT_PRICE());
        }
        return 0;
    }

    private void GetRows_colPenality(int i, final Product product) {
        final ArrayList<String> data_spiner = new ArrayList<>();
        if (product != null) {
            if (product.getQUNTITY() != null) {

                for (int j = 0; j <= Integer.valueOf(product.getQUNTITY()); j++) {
                    data_spiner.add(String.valueOf(j));
                }
            }
        }
       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_spiner);
        CloseCodeAdapter adapter=new CloseCodeAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, data_spiner);

        View child = tab_layoutPenality.getChildAt(i);
        final double[] price_val = {0};
        if (child instanceof TableRow) {
            TableRow row = (TableRow) child;
            LinearLayout linearLayout_0 = (LinearLayout) row.getChildAt(0);
            TextView v = (TextView) linearLayout_0.getChildAt(0);
            v.setText(product.getNAME());
            LinearLayout ll = (LinearLayout) linearLayout_0.getChildAt(1);
            final Spinner RS = (Spinner) ll.getChildAt(0);
            RS.setAdapter(adapter);
             final TextView price=(TextView) linearLayout_0.getChildAt(2);
              price_val[0] =Double.valueOf(product.getUNIT_PRICE())*Double.valueOf(product.getQUNTITY());
            price.setText(String.valueOf(price_val[0])+" "+getApplicationContext().getResources().getString(R.string.EGP));
            RS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    price_val[0] =Double.valueOf(data_spiner.get(position))*Double.valueOf(product.getUNIT_PRICE());
                    price.setText(String.valueOf(price_val[0])+" "+getApplicationContext().getResources().getString(R.string.EGP));
                    SetTotalPrice();
                    ShowBtns();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            RS.setSelection(data_spiner.size()-1);
        }
    }

    @Override
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

    }

    @Override
    public void showError(String errMsg) {
        Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMsg(String errMsg) {

        Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void ShowLoading() {
        pbDailog = new ProgressDialog(OrderDetailsActivity.this);
        pbDailog.setMessage("جاري التحميل .....");
        pbDailog.setCancelable(false);
        pbDailog.show();
    }

    @Override
    public void StopLoading() {
        if(pbDailog!=null && pbDailog.isShowing())
        pbDailog.dismiss();

    }

    @Override
    public void SendSms() {

    }

    @Override
    public boolean CheckCashing() {

        List<OFFLINE> lst_off=DB.GetCashingRequest();
        user = new User(UserName, "", GetCurrentLocaion.CurrentLoc, " ");
        IContractOffline.Presenter offlinePresnter=new OfflinePresenter(OrderDetailsActivity.this, getApplicationContext(), user);
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
            OrderDetailsActivity.this.StopLoading();
            try {
                presenter.setAssignmentAction(TmpActionObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  true;
    }

    public void GetCloseCode(String Key, boolean status) {

        if(CheckProductsPilots()) {
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
        else
        {
            Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.enoughProduct),Toast.LENGTH_SHORT).show();

        }
    }
    public List<CloseCode> GetCloseCodeCancelation()
    {
        CloseCodeCancelation=new ArrayList<>();
        final SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_closeCode, 0);
        if (sharedPreferences.contains(Constants.Group4_Cancelation)) {
            String[] tmp = sharedPreferences.getString(Constants.Group4_Cancelation, "").split("\\*");
            CloseCode[] arr = ServerManager.deSerializeStringToObject(tmp[1], CloseCode[].class);
            CloseCodeCancelation = Arrays.asList(arr);

        }
        return CloseCodeCancelation;

    }

    public void ShowPoPUp(final List<CloseCode> lst_closeCode, final String GID, final boolean status) {
        Flag_notDeliver=!status;
        List<String> lst_clos = new ArrayList<>();
        final List<String> lst_clos_cancel = new ArrayList<>();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.My_Prefs_language_NoDust, 0);
        if (sharedPreferences.contains("SelectedLanguage")) {
            if(sharedPreferences.getString("SelectedLanguage","En").equalsIgnoreCase("English")) {
                if (mCloseCodes != null && mCloseCodes.size() > 0) {
                    for (int i = 0; i < lst_closeCode.size(); i++) {
                        lst_clos.add(lst_closeCode.get(i).getCLOSE_CODE_REASON_EN());
                    }
                }
            }
            else
            {
                if (mCloseCodes != null && mCloseCodes.size() > 0) {
                    for (int i = 0; i < lst_closeCode.size(); i++) {
                        lst_clos.add(lst_closeCode.get(i).getCloseCodeReason());
                    }
                }
            }
            }
        else {

            if (mCloseCodes != null && mCloseCodes.size() > 0) {
                for (int i = 0; i < lst_closeCode.size(); i++) {
                    lst_clos.add(lst_closeCode.get(i).getCLOSE_CODE_REASON_EN());
                }
            }
        }
        final Dialog dialog = new Dialog(OrderDetailsActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_close_code);
        final Spinner spinner = (Spinner) dialog.findViewById(R.id.sp_closeCode);
        final EditText Comments = (EditText) dialog.findViewById(R.id.ed_comments);
        final LinearLayout ly_CancelCloseCode=(LinearLayout)dialog.findViewById(R.id.ll_closeCancel);
        final Spinner spinner_Cancel = (Spinner) dialog.findViewById(R.id.sp_CancelcloseCode);

        // final EditText RNO=(EditText)dialog.findViewById(R.id.ed_RNO);
        final TextView tv_RNO = (TextView) dialog.findViewById(R.id.tv_RNO);
        final LinearLayout ll_RNO = (LinearLayout) dialog.findViewById(R.id.ly_RNO);
        final LinearLayout ll_check=(LinearLayout)dialog.findViewById(R.id.ll_checks);
        final CheckBox ch_fullpaiment=(CheckBox) dialog.findViewById(R.id.ch_fullpayment);
        final CheckBox ch_NoMomey=(CheckBox)dialog.findViewById(R.id.ch_NoMoney);
        final CheckBox ch_payCash=(CheckBox)dialog.findViewById(R.id.ch_payCash);
        final TextView tv_TotalPrice=(TextView)dialog.findViewById(R.id.tv_TotalPrice);
        LinearLayout ly_handels=(LinearLayout)dialog.findViewById(R.id.ly_handles);
        TextView tv_handles=(TextView)dialog.findViewById(R.id.tv_handles);
        TableLayout tb_product=(TableLayout)dialog.findViewById(R.id.tb_product);
        tv_handles.setVisibility(View.GONE);
        tb_product.setVisibility(View.GONE);
        if((Contract_status.equalsIgnoreCase("Cancelation")||GetCancelationProducts().size()>0)&& !Contract_status.equalsIgnoreCase("Not Delivered"))
        {
            if(!(Contract_status.equalsIgnoreCase("Cancelation"))) // case partial
            {
                ly_CancelCloseCode.setVisibility(View.VISIBLE);
                CloseCodeCancelation= GetCloseCodeCancelation();
                if (sharedPreferences.contains("SelectedLanguage")) {
                    if (sharedPreferences.getString("SelectedLanguage", "En").equalsIgnoreCase("English")) {
                        if (CloseCodeCancelation != null && CloseCodeCancelation.size() > 0) {
                            for (int i = 0; i < CloseCodeCancelation.size(); i++) {
                                lst_clos_cancel.add(CloseCodeCancelation.get(i).getCLOSE_CODE_REASON_EN());
                            }
                        } else {
                            if (CloseCodeCancelation != null && CloseCodeCancelation.size() > 0) {
                                for (int i = 0; i < CloseCodeCancelation.size(); i++) {
                                    lst_clos_cancel.add(CloseCodeCancelation.get(i).getCloseCodeReason());
                                }
                            }
                        }
                    }
                }
                else {
                    if (CloseCodeCancelation != null && CloseCodeCancelation.size() > 0) {
                        for (int i = 0; i < CloseCodeCancelation.size(); i++) {
                            lst_clos_cancel.add(CloseCodeCancelation.get(i).getCLOSE_CODE_REASON_EN());
                        }
                    }
                }


                CloseCodeAdapter Adapter = new CloseCodeAdapter(getApplicationContext(),
                        android.R.layout.simple_spinner_dropdown_item, lst_clos_cancel);
                spinner_Cancel.setAdapter(Adapter);
                spinner_Cancel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // closeComments.setText(lst_closeCode.get(position).getCloseCodeReason());
                        CancelcloseCode = CloseCodeCancelation.get(position).getCloseCodeID();
                        //flag = true;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
            List<Product> CheckReplacedProducts;
            if(Contract_status.equalsIgnoreCase("Cancelation"))
            CheckReplacedProducts=HandlesProducts;
            else
            CheckReplacedProducts= GetCancelationProducts();

            lst_Spinner=new ArrayList<>();

            for(int i=0;i<CheckReplacedProducts.size();i++)
            {
                if(CheckReplacedProducts.get(i).getHANDLE()>0)
                {
                    tv_handles.setVisibility(View.VISIBLE);
                    tb_product.setVisibility(View.VISIBLE);
                    TableRow inflate = new TableRow(OrderDetailsActivity.this);
                    inflate = (TableRow) View.inflate(OrderDetailsActivity.this, R.layout.layout_handles, null);
                    tb_product.addView(inflate);
                  //  View add_layout= LayoutInflater.from(dialog.getContext()).inflate(R.layout.layout_handles, ly_handels, false);
                    TextView tx_name=(TextView)inflate.findViewById(R.id.tv_lblProductName);
                    TextView tx_Tret=(TextView)inflate.findViewById(R.id.tv_lblTratmentName);
                    tx_Tret.setText(CheckReplacedProducts.get(i).getTreatment_description());
                    tx_name.setText(CheckReplacedProducts.get(i).getNAME());
                    Spinner sp=(Spinner)inflate.findViewById(R.id.sp_QHandles);
                    final ArrayList<String> data_spiner = new ArrayList<>();
                            for (int j = 0; j <= Integer.valueOf(CheckReplacedProducts.get(i).getQUNTITY()); j++) {
                                data_spiner.add(String.valueOf(j));
                    }
                    CloseCodeAdapter adapter=new CloseCodeAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, data_spiner);
                    sp.setAdapter(adapter);
                    sp.setSelection(data_spiner.size()-1);
                    lst_Spinner.add(sp);
                    //ly_handels.addView(add_layout);

                }
            }
        }
        tv_TotalPrice.setVisibility(View.GONE);
        ch_payCash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    ch_NoMomey.setChecked(false);
                    ch_fullpaiment.setChecked(false);
                }
            }
        });
        ch_NoMomey.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    tv_TotalPrice.setVisibility(View.VISIBLE);
                   tv_TotalPrice.setText(getApplicationContext().getResources().getString(R.string.Totalamount)+" 0 "+getApplicationContext().getResources().getString(R.string.EGP));
                    ch_payCash.setChecked(true);
                }
                else
                {
                    tv_TotalPrice.setVisibility(View.GONE);
                }
            }
        });
        ch_fullpaiment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    ch_payCash.setChecked(false);
                    List<Product>lst_products=GetReplacedProducts(true);
                    double price=0;
                    for(int i=0;i<lst_products.size();i++)
                    {
                        int QR=Integer.valueOf(lst_products.get(i).getQUNTITY_REPLACED());
                        if(Integer.valueOf(lst_products.get(i).getQUNTITY_REPLACED())==0)
                            QR=1;

                        price += QR * Double.valueOf(lst_products.get(i).getBUY_PRICE());
                    }
                    tv_TotalPrice.setVisibility(View.VISIBLE);
                    tv_TotalPrice.setText(getApplicationContext().getResources().getString(R.string.Totalamount)+" "+price+" "+getApplicationContext().getResources().getString(R.string.EGP));

                }
                else
                {
                    tv_TotalPrice.setVisibility(View.GONE);
                }
            }
        });
       ///// check user type
        if((task.getCUSTOMER_TYPE()!=null&&(task.getCUSTOMER_TYPE().equalsIgnoreCase("01"))||(task.getPRIORITY()!=null&&task.getPRIORITY().equalsIgnoreCase("8")))&&task.getALLOW_CREDIT()==1)
        {
            ch_payCash.setVisibility(View.VISIBLE);
            ch_payCash.setChecked(true);
            //ch_payCash.setVisibility(View.GONE);
        }
        else
        {
            ch_payCash.setVisibility(View.GONE);
        }

        if(Contract_status.equalsIgnoreCase("Partially Delivered")&&GetCancelationProducts().size()<=0)
        {
            ch_fullpaiment.setVisibility(View.VISIBLE);
        }
        else if(Contract_status.equalsIgnoreCase("Delivered")&&((task.getCUSTOMER_TYPE()!=null&&task.getCUSTOMER_TYPE().equalsIgnoreCase("01"))||(task.getPRIORITY()!=null&&task.getPRIORITY().equalsIgnoreCase("8"))))
        {
            ch_fullpaiment.setVisibility(View.GONE);
          //  ch_NoMomey.setVisibility(View.VISIBLE);
        }
        if(Contract_status.equalsIgnoreCase("Not Delivered"))
            ch_payCash.setVisibility(View.GONE);

        b1 = (EditText) dialog.findViewById(R.id.btn_button1);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        final int tmp = year - 2000;
        b1.setText(String.valueOf(tmp));
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                PopupMenu popupMenu = new PopupMenu(context, b1);
                popupMenu.inflate(R.menu.menu_r_no);

                popupMenu.getMenu().getItem(0).setTitle(String.valueOf(tmp));
                popupMenu.getMenu().getItem(1).setTitle(String.valueOf(tmp + 1));
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        b1.setText(item.getTitle().toString());
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        b2 = (EditText) dialog.findViewById(R.id.btn_button2);
        b3 = (EditText) dialog.findViewById(R.id.btn_button3);
        b3.addTextChangedListener(new Textwatcher(b3));
       // b3.requestFocus();
        b4 = (EditText) dialog.findViewById(R.id.btn_button4);
        b4.addTextChangedListener(new Textwatcher(b4));
        b5 = (EditText) dialog.findViewById(R.id.btn_button5);
        b5.addTextChangedListener(new Textwatcher(b5));
        b6 = (EditText) dialog.findViewById(R.id.btn_button6);
        b6.addTextChangedListener(new Textwatcher(b6));
        b7 = (EditText) dialog.findViewById(R.id.btn_button7);
        b7.addTextChangedListener(new Textwatcher(b7));
        b8 = (EditText) dialog.findViewById(R.id.btn_button8);
        b8.addTextChangedListener(new Textwatcher(b8));

        tv_RNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_RNO.setError(null);
                tv_RNO.clearFocus();
            }
        });
        /*if (!status) {

            ll_RNO.setVisibility(View.GONE);
        } else {
            ll_RNO.setVisibility(View.VISIBLE);
            // RNO.setText(tmp);
            //   tv_RNO.setHint(getApplicationContext().getResources().getString(R.string.D_r_no)+tmp+"-");
        }*/
        /// new
        ll_RNO.setVisibility(View.GONE);
        CloseCodeAdapter Adapter = new CloseCodeAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, lst_clos);
        spinner.setAdapter(Adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // closeComments.setText(lst_closeCode.get(position).getCloseCodeReason());
                closeCode = lst_closeCode.get(position).getCloseCodeID();
                flag = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Button send = (Button) dialog.findViewById(R.id.btn_ok);
        //  final int finalTmp = tmp;
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean Tmpflag = true;
               // hideKeyboard(OrderDetailsActivity.this);

                if (flag && mCloseCodes != null) {
                    String AssignmentID = task.getAID();
                    String CloseCode = closeCode;
                    String CloseCodeReason = spinner.getSelectedItem().toString();
                    String CloseCodeCancel="";
                    if(spinner_Cancel.getAdapter()!=null&&spinner_Cancel.getAdapter().getCount()>0&&ly_CancelCloseCode.getVisibility()==View.VISIBLE)
                    {
                        CloseCodeCancel=spinner_Cancel.getSelectedItem().toString();
                    }

                    String comments = Comments.getText().toString();
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    String ActionDate = df.format(new Date());
                    ActionObject actionObject=new ActionObject();
                    actionObject.setNoMoney(false);
                    actionObject.setActionDate(ActionDate);
                    actionObject.setTOTAL_PRICE(0.0);
                    actionObject.setActual_paid(0.0);
                    actionObject.setAssignmentID(AssignmentID);
                    actionObject.setCloseCode(CloseCode);
                    actionObject.setCloseCodeReason(CloseCodeReason);
                    actionObject.setCollection(new ArrayList<Collection>());
                    if(Contract_status.equalsIgnoreCase("Cancelation"))
                    {
                        actionObject.setCancelcloseCode(CloseCode);
                        actionObject.setCancelcloseCodeReason(CloseCodeReason);
                    }
                    else {
                        actionObject.setCancelcloseCode(CancelcloseCode);
                        actionObject.setCancelcloseCodeReason(CloseCodeCancel);
                    }

                    actionObject.setComments(comments);
                    actionObject.setLatitude(GetCurrentLocaion.CurrentLoc.getLatitude());
                    actionObject.setLongitude(GetCurrentLocaion.CurrentLoc.getLongitude());
                    actionObject.setHandles(new ArrayList<Product>());
                    if(Contract_status.equalsIgnoreCase("Cancelation"))
                    actionObject.setContractStatus("5");
                    else
                        actionObject.setContractStatus(GID);
                    actionObject.setDRNo(task.getRecieptNo());
                   // actionObject.setTOTAL_PRICE(0.0);
                    actionObject.setCARD_NO(task.getCardNo());
                    actionObject.setDeliveryMan(UserName);
                    actionObject.setDRNo(task.getRecieptNo());
                   // actionObject.setTOTAL_PRICE(Double.valueOf(GetTotalPrice()));


                   List<Product> ReplacedProducts=new ArrayList<Product>();
                    if(Contract_status.equalsIgnoreCase("Partially Delivered")) {
                        ReplacedProducts=GetReplacedProducts(ch_fullpaiment.isChecked());
                        List<Product> ReminderProducts = GetNotReplacedProducts();
                        if (ch_fullpaiment.isChecked()) {
                            // insert new contract to product_contract with cost equal zero and Quantity NReplaced -QR
                            for (int i = 0; i < ReminderProducts.size(); i++) {
                                ReminderProducts.get(i).setBUY_PRICE("0");
                            }

                        }
                        Products_NotChanged="";
                        for (int i = 0; i < ReminderProducts.size(); i++) {
                            if(i==ReminderProducts.size()-1)
                            Products_NotChanged+=ReminderProducts.get(i).getNAME()+" X "+ReminderProducts.get(i).getQUNTITY_REPLACED();
                            else
                                Products_NotChanged+=ReminderProducts.get(i).getNAME()+" X "+ReminderProducts.get(i).getQUNTITY_REPLACED()+" , ";
                        }
                       actionObject.setNotReplacedProducts(ReminderProducts);
                    }
                    else
                    {
                        ReplacedProducts=GetReplacedProducts(false);
                    }
                   actionObject.setReplacedProducts(ReplacedProducts);
                    if(task.getOperationComment()==null)
                        task.setOperationComment("");
                    if(task.getDataComment()==null)
                        task.setDataComment("");
                    actionObject.setCurrentTask(task);
                    TmpActionObject=actionObject;
                    // send data
                    if (status) {
                         txt_msg = "";

                        txt_msg = task.getRecieptNo() + "Your Products are ";
                        double price = 0;
                        for (int i = 0; i < actionObject.getReplacedProducts().size(); i++) {
                            int QR = Integer.valueOf(actionObject.getReplacedProducts().get(i).getQUNTITY_REPLACED());

                            if(actionObject.getReplacedProducts().get(i).getPackage_NO()!=null&&!actionObject.getReplacedProducts().get(i).getPackage_NO().equalsIgnoreCase("0"))
                            {
                              //  Toast.makeText(getApplicationContext(),actionObject.getReplacedProducts().get(i).getPackage_NO(),Toast.LENGTH_SHORT).show();
                                if(Integer.valueOf(actionObject.getReplacedProducts().get(i).getQUNTITY())==QR)
                                {
                                    //Toast.makeText(getApplicationContext(),actionObject.getReplacedProducts().get(i).getPackage_NO(),Toast.LENGTH_SHORT).show();


                                    price += Math.abs(Double.valueOf(actionObject.getReplacedProducts().get(i).getBUY_PRICE()));
                                }
                                else if(Math.abs(Double.valueOf(actionObject.getReplacedProducts().get(i).getBUY_PRICE()))==Math.abs(Double.valueOf(actionObject.getReplacedProducts().get(i).getUNIT_PRICE())))
                                {

                                        price += Math.abs(Double.valueOf(actionObject.getReplacedProducts().get(i).getBUY_PRICE()));
                                }
                                else
                                {
                                    if(QR==0)
                                        QR=1;
                                    price += QR * Double.valueOf(actionObject.getReplacedProducts().get(i).getBUY_PRICE());

                                }
                            }
                            else {
                                if (QR == 0)
                                    QR = 1;
                                price += QR * Double.valueOf(actionObject.getReplacedProducts().get(i).getBUY_PRICE());

                            }
                            txt_msg += "Name:" + actionObject.getReplacedProducts().get(i).getNAME() + " " + "Quantity:" + actionObject.getReplacedProducts().get(i).getQUNTITY_REPLACED() + ",";
                        }
                        txt_msg += " Total Price is " + String.valueOf(price);
                        // check Company
                        actionObject.setTOTAL_PRICE(price);
                        actionObject.setActual_paid(price);

                      /*  if (task.getPRIORITY() != null && task.getPRIORITY().equalsIgnoreCase("9")) { // new customer
                            for (int i = 0; i < actionObject.getReplacedProducts().size(); i++) {
                                actionObject.getReplacedProducts().get(i).setQUNTITY_REPLACED("-" + actionObject.getReplacedProducts().get(i).getQUNTITY_REPLACED());
                            }

                        }*/
                        if (Contract_status.equalsIgnoreCase("Delivered") && ((task.getCUSTOMER_TYPE() != null && task.getCUSTOMER_TYPE().equalsIgnoreCase("01"))||(task.getPRIORITY()!=null&&task.getPRIORITY().equalsIgnoreCase("8")))) {
                            if (ch_NoMomey.isChecked()&&ch_NoMomey.getVisibility()==View.VISIBLE) {
                                actionObject.setNoMoney(true);
                                double Total_Financial = 0;
                                int QR=0;
                                for (int i = 0; i < actionObject.getReplacedProducts().size(); i++) {
                                    if (actionObject.getReplacedProducts().get(i).getPackage_NO() != null && !actionObject.getReplacedProducts().get(i).getPackage_NO().equalsIgnoreCase("0")) {
                                        QR = Integer.valueOf(actionObject.getReplacedProducts().get(i).getQUNTITY_REPLACED());
                                        if (Integer.valueOf(actionObject.getReplacedProducts().get(i).getQUNTITY()) == QR) {

                                            Total_Financial += Math.abs(Double.valueOf(actionObject.getReplacedProducts().get(i).getBUY_PRICE()));
                                        } else if (Math.abs(Double.valueOf(actionObject.getReplacedProducts().get(i).getBUY_PRICE())) == Math.abs(Double.valueOf(actionObject.getReplacedProducts().get(i).getUNIT_PRICE()))) {

                                            Total_Financial += Math.abs(Double.valueOf(actionObject.getReplacedProducts().get(i).getBUY_PRICE()));
                                        } else {
                                            if (QR == 0)
                                                QR = 1;
                                            Total_Financial += QR * Double.valueOf(actionObject.getReplacedProducts().get(i).getBUY_PRICE());

                                        }
                                        actionObject.getReplacedProducts().get(i).setBUY_PRICE("0"); // no money

                                    }
                                    else {
                                        Total_Financial += Integer.valueOf(actionObject.getReplacedProducts().get(i).getQUNTITY_REPLACED()) * Double.valueOf(actionObject.getReplacedProducts().get(i).getBUY_PRICE());
                                        actionObject.getReplacedProducts().get(i).setBUY_PRICE("0"); // no money
                                    }
                                }
                                actionObject.setTOTAL_PRICE(Total_Financial);
                                // actionObject.setReplacedProducts(ReplacedProducts);
                            }
                        }

                        if (((task.getCUSTOMER_TYPE() != null && task.getCUSTOMER_TYPE().equalsIgnoreCase("01"))||(task.getPRIORITY()!=null&&task.getPRIORITY().equalsIgnoreCase("8")))&& (ch_payCash.getVisibility()==View.VISIBLE&&ch_payCash.isChecked())) // pay credit
                        {
                             /*   if (Contract_status.equalsIgnoreCase("Delivered")) {
                                    actionObject.setTOTAL_PRICE(-1*actionObject.getTOTAL_PRICE());
                                } else {*/
                           /* for (int i = 0; i < actionObject.getReplacedProducts().size(); i++) {
                                actionObject.getReplacedProducts().get(i).setBUY_PRICE("-" + actionObject.getReplacedProducts().get(i).getBUY_PRICE());
                            }*/
                            actionObject.setActual_paid(0);
                            // actionObject.setReplacedProducts(ReplacedProducts);


                            }
                            if(Contract_status.equalsIgnoreCase("Not Delivered"))
                            {
                                actionObject.setTOTAL_PRICE(0);
                                actionObject.setActual_paid(0);
                                List<Product>tmp=new ArrayList<Product>();
                                for(int i=0;i<actionObject.getReplacedProducts().size();i++)
                                {
                                   /*.add( actionObject.getReplacedProducts().get(i));
                                    tmp.get(i).setBUY_PRICE("0");
                                    tmp.get(i).setQUNTITY_REPLACED("0");*/
                                    actionObject.getReplacedProducts().get(i).setBUY_PRICE("0");
                                    actionObject.getReplacedProducts().get(i).setQUNTITY_REPLACED("0");
                                }
                                actionObject.setHandles(new ArrayList<Product>());
                                actionObject.setReplacedProducts(new ArrayList<Product>());
                            //    actionObject.setReplacedProducts(tmp);
                            }
                             if(Contract_status.equalsIgnoreCase("Cancelation")|| GetCancelationProducts().size()>0)
                            {
                                if(Contract_status.equalsIgnoreCase("Cancelation")) {
                                    actionObject.setHandles(HandlesProducts);

                                }
                                else
                                    actionObject.setHandles(GetCancelationProducts());

                                int indx=0;
                                for(int i=0;i<actionObject.getHandles().size();i++)
                                {
                                    if(actionObject.getHandles().get(i).getHANDLE()>0)
                                    {
                                      //  Toast.makeText(getApplicationContext(),"f",Toast.LENGTH_SHORT).show();
                                        Spinner s=lst_Spinner.get(indx);
                                        actionObject.getHandles().get(i).setHANDLE(Integer.valueOf(s.getSelectedItem().toString()));
                                        if(Contract_status.equalsIgnoreCase("Cancelation")) {
                                           // actionObject.getHandles().get(i).setQUNTITY_REPLACED("0");

                                        }
                                        //  actionObject.getReplacedProducts().get(i).setHANDLE(Integer.valueOf(s.getSelectedItem().toString()));
                                        indx++;
                                    }
                                }

                            }
                        if(Contract_status.equalsIgnoreCase("Cancelation"))
                        {
                            for(int i=0;i<actionObject.getReplacedProducts().size();i++)
                            {
                                actionObject.getReplacedProducts().get(i).setBUY_PRICE("0");
                                actionObject.getReplacedProducts().get(i).setQUNTITY_REPLACED("0");
                            }
                            actionObject.setTOTAL_PRICE(0);
                            actionObject.setActual_paid(0);
                        }
                        //}
                        if(task.getTelNo()!=null) {
                            // presenter.SendSMS(task.getTelNo(), txt_msg);
                            if (Contract_status.equalsIgnoreCase("Not Delivered")) {
                                actionObject.setHandles(new ArrayList<Product>());
                                actionObject.setReplacedProducts(new ArrayList<Product>());

                            }
                            if( CheckCancelediProductsInCover(actionObject.getReplacedProducts())) {
                                TmpActionObject=actionObject;
                              /*  if(CheckCashing())
                                presenter.setAssignmentAction(actionObject);*/
                              /*  try {
                                    new CheckCashing().execute().get();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }*/
                                CheckCashing();
                            }
                            else
                                Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.enoughProduct),Toast.LENGTH_SHORT).show();

                        }
                        else {
                            if(Contract_status.equalsIgnoreCase("Not Delivered"))
                            {
                                actionObject.setTOTAL_PRICE(0);
                                actionObject.setActual_paid(0);
                                List<Product>tmp=new ArrayList<Product>();
                                for(int i=0;i<actionObject.getReplacedProducts().size();i++)
                                {
                               /*.add( actionObject.getReplacedProducts().get(i));
                                tmp.get(i).setBUY_PRICE("0");
                                tmp.get(i).setQUNTITY_REPLACED("0");*/
                                    actionObject.getReplacedProducts().get(i).setBUY_PRICE("0");
                                    actionObject.getReplacedProducts().get(i).setQUNTITY_REPLACED("0");
                                }
                                actionObject.setHandles(new ArrayList<Product>());
                                actionObject.setReplacedProducts(new ArrayList<Product>());

                                //actionObject.setReplacedProducts(tmp);
                            }
                            if( CheckCancelediProductsInCover(actionObject.getReplacedProducts())) {
                                TmpActionObject=actionObject;
                                CheckCashing();
                              /*  if(CheckCashing())
                                presenter.setAssignmentAction(actionObject);*/
                               /* try {
                                    new CheckCashing().execute().get();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }*/
                            }
                            else
                                Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.enoughProduct),Toast.LENGTH_SHORT).show();

                        }


                    }
                    else
                    {
                        if(Contract_status.equalsIgnoreCase("Not Delivered"))
                        {
                            actionObject.setTOTAL_PRICE(0);
                            actionObject.setActual_paid(0);
                            List<Product>tmp=new ArrayList<Product>();
                            for(int i=0;i<actionObject.getReplacedProducts().size();i++)
                            {
                                   /*.add( actionObject.getReplacedProducts().get(i));
                                    tmp.get(i).setBUY_PRICE("0");
                                    tmp.get(i).setQUNTITY_REPLACED("0");*/
                                actionObject.getReplacedProducts().get(i).setBUY_PRICE("0");
                                actionObject.getReplacedProducts().get(i).setQUNTITY_REPLACED("0");
                            }
                            actionObject.setHandles(new ArrayList<Product>());
                            actionObject.setReplacedProducts(new ArrayList<Product>());

                            //actionObject.setReplacedProducts(tmp);
                        }
                         if(Contract_status.equalsIgnoreCase("Cancelation")|| GetCancelationProducts().size()>0)
                        {
                            if(Contract_status.equalsIgnoreCase("Cancelation")) {
                                actionObject.setHandles(HandlesProducts);

                            }
                            else
                             actionObject.setHandles(GetCancelationProducts());

                            int indx=0;
                                for(int i=0;i<actionObject.getHandles().size();i++)
                                {
                                    if(actionObject.getHandles().get(i).getHANDLE()>0)
                                    {
                                        Spinner s=lst_Spinner.get(indx);
                                        actionObject.getHandles().get(i).setHANDLE(Integer.valueOf(s.getSelectedItem().toString()));
                                        if(Contract_status.equalsIgnoreCase("Cancelation")) {
                                          //  actionObject.getHandles().get(i).setQUNTITY_REPLACED("0");
                                        }
                                      //  actionObject.getReplacedProducts().get(i).setHANDLE(Integer.valueOf(s.getSelectedItem().toString()));
                                        indx++;
                                    }
                                }

                        }
                        if(Contract_status.equalsIgnoreCase("Cancelation"))
                        {
                            for(int i=0;i<actionObject.getReplacedProducts().size();i++)
                            {
                                actionObject.getReplacedProducts().get(i).setBUY_PRICE("0");
                                actionObject.getReplacedProducts().get(i).setQUNTITY_REPLACED("0");
                            }
                            actionObject.setTOTAL_PRICE(0);
                            actionObject.setActual_paid(0);
                        }
                        if(Contract_status.equalsIgnoreCase("Not Delivered"))
                        {
                            actionObject.setHandles(new ArrayList<Product>());
                            actionObject.setReplacedProducts(new ArrayList<Product>());

                        }
                        if( CheckCancelediProductsInCover(actionObject.getReplacedProducts())) {
                            TmpActionObject=actionObject;
                            CheckCashing();
                           // if(CheckCashing())
                           // presenter.setAssignmentAction(actionObject);
                           /* try {
                                new CheckCashing().execute().get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }*/
                        }
                         else
                            Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.enoughProduct),Toast.LENGTH_SHORT).show();


                    }

                  /*   else {
                        tv_RNO.requestFocus();
                        tv_RNO.setError(getApplicationContext().getResources().getString(R.string.errRNo));
                    }*/

                } else {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.msgCloseReason), Toast.LENGTH_SHORT).show();
                }
                if (Tmpflag) {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }
    @Override
    public void ShowMsg(String Type,String msg) {
        if (msg.equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.SaveChanges))) {
            for(int i=0;i<TmpActionObject.getReplacedProducts().size();i++)
            {
                if (i==TmpActionObject.getReplacedProducts().size()-1)
                Products_Changed+=TmpActionObject.getReplacedProducts().get(i).getNAME()+" X "+TmpActionObject.getReplacedProducts().get(i).getQUNTITY_REPLACED();
                else
                    Products_Changed+=TmpActionObject.getReplacedProducts().get(i).getNAME()+" X "+TmpActionObject.getReplacedProducts().get(i).getQUNTITY_REPLACED()+" , ";

            }
            for(int i=0;i<TmpActionObject.getHandles().size();i++)
            {
                if (i==TmpActionObject.getHandles().size()-1)
                    Products_Canceled+=TmpActionObject.getHandles().get(i).getNAME()+" X "+TmpActionObject.getHandles().get(i).getQUNTITY_REPLACED();
                else
                    Products_Canceled+=TmpActionObject.getHandles().get(i).getNAME()+" X "+TmpActionObject.getHandles().get(i).getQUNTITY_REPLACED()+" , ";

            }
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
            DB.UpdateReplacedAmount(TmpActionObject.getReplacedProducts());
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
                DB.UpdateReplacedAmount(TmpActionObject.getReplacedProducts());
                DB.UpdateCoverPilot(TmpActionObject.getReplacedProducts(),UserName,task,TmpActionObject.getHandles());
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
        DB.AddCashingRequests(offline);
    }

    @Override
    public void SendAction() {
        try {
            presenter.setAssignmentAction(TmpActionObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void ShowGPSAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderDetailsActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing the Settings button.
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                OrderDetailsActivity.this.startActivity(intent);
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

    @Override
    public void onClick(View v) {


    }

    @Override
    public void Failed(OFFLINE offline) {

    }

    @Override
    public void Success(OFFLINE offline) {
        DB.DeleteCashingRequests(offline);

    }

    @Override
    public void SuccessALLRequests() {
        StopLoading();
        OrderDetailsActivity.this.StopLoading();
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
            DB.UpdateReplacedAmount(TmpActionObject.getReplacedProducts());
            DB.UpdateCoverPilot(TmpActionObject.getReplacedProducts(),UserName,task,TmpActionObject.getHandles());
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

    private class Textwatcher implements TextWatcher {
        EditText myedit;

        public Textwatcher(EditText editText) {
            myedit = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().equalsIgnoreCase("")) {
                if (myedit.getId() == R.id.btn_button3) {
                    myedit.clearFocus();
                   // b4.requestFocus();

                } else if (myedit.getId() == R.id.btn_button4) {
                    myedit.clearFocus();
                  //  b5.requestFocus();
                } else if (myedit.getId() == R.id.btn_button5) {
                    myedit.clearFocus();
                   // b6.requestFocus();

                } else if (myedit.getId() == R.id.btn_button6) {
                    myedit.clearFocus();
                    //b7.requestFocus();
                } else if (myedit.getId() == R.id.btn_button7) {
                    myedit.clearFocus();
                   // b8.requestFocus();

                }
            } else {
                if (myedit.getId() == R.id.btn_button4) {
                    myedit.clearFocus();
                  //  b3.requestFocus();

                } else if (myedit.getId() == R.id.btn_button5) {
                    myedit.clearFocus();
                  //  b4.requestFocus();
                } else if (myedit.getId() == R.id.btn_button6) {
                    myedit.clearFocus();
                   // b5.requestFocus();

                } else if (myedit.getId() == R.id.btn_button7) {
                    myedit.clearFocus();
                  //  b6.requestFocus();
                } else if (myedit.getId() == R.id.btn_button8) {
                    myedit.clearFocus();
                  //  b7.requestFocus();

                }

            }
        }
    }

    private void GetRows_col(int index_row, final Product p) {
        final ArrayList<String> data_spiner = new ArrayList<>();
        if (p != null) {
            if (p.getQUNTITY() != null) {
                if (task.getPRIORITY() != null && task.getPRIORITY().equalsIgnoreCase("9")) { // new customer
                    // data_spiner.add(p.getQUNTITY());
                    for (int i = 0; i <= Integer.valueOf(p.getQUNTITY()); i++) {
                        data_spiner.add(String.valueOf(i));
                    }
                } else {
                    for (int i = 0; i <= Integer.valueOf(p.getQUNTITY()); i++) {
                        data_spiner.add(String.valueOf(i));
                    }
                }
            }

          /*  ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item,data_spiner);*/
            CloseCodeAdapter adapter2;
            CloseCodeAdapter adapter = new CloseCodeAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, data_spiner);


            // for (int i = 1; i < table.getChildCount(); i++) {
            View child = table.getChildAt(index_row);
            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;
                LinearLayout linearLayout_0 = (LinearLayout) row.getChildAt(0);
                TextView v = (TextView) linearLayout_0.getChildAt(0);
                v.setText(p.getNAME());
                LinearLayout ll = (LinearLayout) linearLayout_0.getChildAt(1);
                final Spinner RS = (Spinner) ll.getChildAt(0);
                RS.setAdapter(adapter);
                final Spinner NotRepresent_Spinner = (Spinner) ((LinearLayout) linearLayout_0.getChildAt(2)).getChildAt(0);
                final Spinner cancel_spinner = (Spinner) ((LinearLayout) linearLayout_0.getChildAt(3)).getChildAt(0);
                cancel_spinner.setAdapter(adapter);
                if (task.getPRIORITY() != null && task.getPRIORITY().equalsIgnoreCase("9")) {
                   /* final ArrayList<String>data_spiner2=new ArrayList<>();
                  adapter2= new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item,data_spiner2);
                    NotRepresent_Spinner.setAdapter(adapter2);*/
                    NotRepresent_Spinner.setAdapter(adapter);
                } else {
                    NotRepresent_Spinner.setAdapter(adapter);
                }
                if (p.getType() != null && p.getType().equalsIgnoreCase("4")) /// case collection
                {
                    final ArrayList<String> data_spiner2 = new ArrayList<>();
                    adapter2 = new CloseCodeAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, data_spiner2);

                    NotRepresent_Spinner.setAdapter(adapter2);
                    RS.setAdapter(adapter2);
                }
                final TextView price = (TextView) linearLayout_0.getChildAt(4);
                final TextView Treatment = (TextView) linearLayout_0.getChildAt(6);
                Treatment.setText(p.getTreatment_description());
                final TextView PNo = (TextView) linearLayout_0.getChildAt(7);
                PNo.setText(p.getPackage_NO());
                final Switch Replaced = (Switch) linearLayout_0.getChildAt(5);
                double price_val;
                if (p.getType() != null && p.getType().equalsIgnoreCase("4")) {
                    price_val = Double.valueOf(p.getUNIT_PRICE());
                } else
                    price_val = Double.valueOf(p.getUNIT_PRICE()) * Double.valueOf(p.getQUNTITY());

                if (p.getPackage_NO() != null && !p.getPackage_NO().equalsIgnoreCase("0")) {
                    price.setText(String.valueOf(p.getUNIT_PRICE()) + " " + getApplicationContext().getResources().getString(R.string.EGP));

                } else {
                    price.setText(String.valueOf(price_val) + " " + getApplicationContext().getResources().getString(R.string.EGP));
                }
               /*  if(UserName.equalsIgnoreCase(task.getPilotID())) {
                     Replaced.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                         @Override
                         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                             if (!isChecked)
                                 NotRepresent_Spinner.setSelection(data_spiner.size() - 1);
                             else
                                 NotRepresent_Spinner.setSelection(0);

                         }
                     });
                 }*/
                if (UserName.equalsIgnoreCase(task.getPilotID())) {
                    NotRepresent_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            int value = Integer.valueOf(NotRepresent_Spinner.getSelectedItem().toString());
                            int Cancelvalue = Integer.valueOf(cancel_spinner.getSelectedItem().toString());
                             if(value+Cancelvalue>Integer.valueOf(p.getQUNTITY()))
                             {
                                 value=Integer.valueOf(p.getQUNTITY())-Cancelvalue;
                             }
                            NotRepresent_Spinner.setSelection(value);
                           // Toast.makeText(getApplicationContext(),String.valueOf(value),Toast.LENGTH_SHORT).show();
                            int QR=Integer.valueOf(p.getQUNTITY())-(Cancelvalue+value);
                            RS.setSelection(QR);
                          /*  if (task.getPRIORITY() == null || !task.getPRIORITY().equalsIgnoreCase("9")) {
                                int tmp = Integer.valueOf(p.getQUNTITY()) - (value + Cancelvalue);
                                int cancel_new = Integer.valueOf(p.getQUNTITY()) - (value + tmp);

                                for (int k = 0; k < data_spiner.size(); k++) {
                                    if (String.valueOf(tmp).equalsIgnoreCase(data_spiner.get(k))) {
                                        RS.setSelection(k);
                                        break;
                                    }
                                }
                                for (int k = 0; k < data_spiner.size(); k++) {
                                    if (String.valueOf(cancel_new).equalsIgnoreCase(data_spiner.get(k))) {
                                        cancel_spinner.setSelection(k);
                                        break;
                                    }
                                }

                                double new_price = (((Double.valueOf(p.getUNIT_PRICE()) * Double.valueOf(p.getQUNTITY())) * tmp)) / Integer.valueOf(p.getQUNTITY());
                                if (p.getPackage_NO() != null && !p.getPackage_NO().equalsIgnoreCase("0")) {
                                    //  price.setText(String.valueOf(p.getUNIT_PRICE()) + " " + getApplicationContext().getResources().getString(R.string.EGP));

                                } else
                                    price.setText(String.valueOf(new_price) + " " + getApplicationContext().getResources().getString(R.string.EGP));
                                SetTotalPrice();
                                ShowBtns();


                            } else {*/
                                /*double new_price = Double.valueOf(p.getUNIT_PRICE())*Double.valueOf(value);
                                price.setText(String.valueOf(new_price) + " " + getApplicationContext().getResources().getString(R.string.EGP));*/
                             /*   int tmp = Integer.valueOf(p.getQUNTITY()) - (value + Cancelvalue);
                                int cancel_new = Integer.valueOf(p.getQUNTITY()) - (value + tmp);

                                for (int k = 0; k < data_spiner.size(); k++) {
                                    if (String.valueOf(tmp).equalsIgnoreCase(data_spiner.get(k))) {
                                        RS.setSelection(k);
                                        break;
                                    }
                                }
                                for (int k = 0; k < data_spiner.size(); k++) {
                                    if (String.valueOf(cancel_new).equalsIgnoreCase(data_spiner.get(k))) {
                                        cancel_spinner.setSelection(k);
                                        break;
                                    }
                                }*/


                            /*    double new_price = (((Double.valueOf(p.getUNIT_PRICE()) * Double.valueOf(p.getQUNTITY())) * tmp)) / Integer.valueOf(p.getQUNTITY());
                                if (p.getPackage_NO() != null && !p.getPackage_NO().equalsIgnoreCase("0")) {
                                    // price.setText(String.valueOf(p.getUNIT_PRICE()) + " " + getApplicationContext().getResources().getString(R.string.EGP));

                                } else
                                    price.setText(String.valueOf(new_price) + " " + getApplicationContext().getResources().getString(R.string.EGP));*/
                                SetTotalPrice();
                                ShowBtns();


                            //}
                        }


                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
                if (UserName.equalsIgnoreCase(task.getPilotID())) {
                    RS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            int value = Integer.valueOf(RS.getSelectedItem().toString());
                            int Cancelvalue = Integer.valueOf(cancel_spinner.getSelectedItem().toString());
                            // if (task.getPRIORITY() == null || !task.getPRIORITY().equalsIgnoreCase("9")) {
                            if((value+Cancelvalue)>Integer.valueOf(p.getQUNTITY()))
                            {
                                value=Integer.valueOf(p.getQUNTITY())-Cancelvalue;
                            }
                            RS.setSelection(value);
                            int QNR=Integer.valueOf(p.getQUNTITY())-(value+Cancelvalue);
                            //Toast.makeText(getApplicationContext(),"ff"+QNR,Toast.LENGTH_SHORT).show();
                            NotRepresent_Spinner.setSelection(QNR);
                         /*   int tmp = Integer.valueOf(p.getQUNTITY()) - (value + Cancelvalue);
                            int cancel_new = Integer.valueOf(p.getQUNTITY()) - (value + tmp);
                            for (int k = 0; k < data_spiner.size(); k++) {
                                if (String.valueOf(tmp).equalsIgnoreCase(data_spiner.get(k))) {
                                    NotRepresent_Spinner.setSelection(k);
                                    break;
                                }
                            }
                            for (int k = 0; k < data_spiner.size(); k++) {
                                if (String.valueOf(cancel_new).equalsIgnoreCase(data_spiner.get(k))) {
                                    cancel_spinner.setSelection(k);
                                    break;
                                }
                            }*/
                          /*  if (value == 0) {
                                Replaced.setChecked(true);
                                Replaced.setTrackDrawable(getApplicationContext().getResources().getDrawable(R.drawable.switch_track_on_not_deliver));
                            } else if (value != Integer.valueOf(data_spiner.get(data_spiner.size() - 1))) {
                                Replaced.setTrackDrawable(getApplicationContext().getResources().getDrawable(R.drawable.switch_track_on_partial));

                            } else {
                                Replaced.setTrackDrawable(getApplicationContext().getResources().getDrawable(R.drawable.switch_track_on_deliver));
                            }*/
                            if (value != Integer.valueOf(p.getQUNTITY())) {
                                if (p.getPackage_NO() != null && !p.getPackage_NO().equalsIgnoreCase("0")) {
                                    int Duration = 1;
                                    if (task.getDuration() != null) {
                                        Duration = Integer.valueOf(task.getDuration());
                                    }

                                    p.setUNIT_PRICE(String.valueOf(DB.GetPriceReference(p.getPRODUCT_ID(), Duration)));
                                    UpdatePackagesProducts(p.getPackage_NO());
                                }
                            } else if (p.getPackage_NO() != null && !p.getPackage_NO().equalsIgnoreCase("0")) {
                                // check if All Product Not Partial in Package
                                if (CheckQuantityProductsPackages(p.getPackage_NO())) {
                                    //PackageInfo packageInfo=DB.GetPackage(Integer.valueOf(p.getPackage_id()),Integer.valueOf(p.getPRODUCT_ID()));
                                    p.setUNIT_PRICE(String.valueOf(GetActualPriceProduct(p.getPRODUCT_ID(), p.getPackage_id(), p.getTreatment_code(),p.getDELIVERY_TYPE(),p.getPackage_NO())));
                                    UpdateActualPackagesProducts(p.getPackage_NO());
                                } else {

                                    p.setUNIT_PRICE(String.valueOf(DB.GetPriceReference(p.getPRODUCT_ID(), Integer.valueOf(task.getDuration()))));
                                    UpdatePackagesProducts(p.getPackage_NO());
                                }
                            }
                            if (p.getPackage_NO() != null && p.getPackage_NO().equalsIgnoreCase("0")) {
                                double new_price = (((Double.valueOf(p.getUNIT_PRICE()) * Double.valueOf(p.getQUNTITY())) * value)) / Integer.valueOf(p.getQUNTITY());
                                price.setText(String.valueOf(new_price) + " " + getApplicationContext().getResources().getString(R.string.EGP));
                            }
                            // }
                    /*    else
                        {
                            double new_price = Double.valueOf(p.getUNIT_PRICE())*value;
                            price.setText(String.valueOf(new_price) + " " + getApplicationContext().getResources().getString(R.string.EGP));

                        }*/
                            SetTotalPrice();
                            ShowBtns();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    if (UserName.equalsIgnoreCase(task.getPilotID())) {
                        cancel_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                int value = Integer.valueOf(cancel_spinner.getSelectedItem().toString());
                                int QNR = Integer.valueOf(NotRepresent_Spinner.getSelectedItem().toString());
                                int QR = Integer.valueOf(RS.getSelectedItem().toString());
                                if((value+QR)>Integer.valueOf(p.getQUNTITY()))
                                {
                                    QR=Integer.valueOf(p.getQUNTITY())-value;
                                }
                                QR=Integer.valueOf(p.getQUNTITY())-value;
                                RS.setSelection(Integer.valueOf(p.getQUNTITY())-value);
                                NotRepresent_Spinner.setSelection(Integer.valueOf(p.getQUNTITY())-(value+QR));

                                // if (task.getPRIORITY() == null || !task.getPRIORITY().equalsIgnoreCase("9")) {

                             /*   int tmp = Integer.valueOf(p.getQUNTITY()) - (value );
                               // int cancel_new = Integer.valueOf(p.getQUNTITY()) - (value + tmp);
                                for (int k = 0; k < data_spiner.size(); k++) {
                                    if (String.valueOf(tmp).equalsIgnoreCase(data_spiner.get(k))) {
                                        RS.setSelection(k);
                                        break;
                                    }
                                }
                                /*for (int k = 0; k < data_spiner.size(); k++) {
                                    if (String.valueOf(cancel_new).equalsIgnoreCase(data_spiner.get(k))) {
                                        cancel_spinner.setSelection(k);
                                        break;
                                    }
                                }*/
                             /*   if (value == 0) {
                                    Replaced.setChecked(true);
                                    Replaced.setTrackDrawable(getApplicationContext().getResources().getDrawable(R.drawable.switch_track_on_not_deliver));
                                } else if (value != Integer.valueOf(data_spiner.get(data_spiner.size() - 1))) {
                                    Replaced.setTrackDrawable(getApplicationContext().getResources().getDrawable(R.drawable.switch_track_on_partial));

                                } else {
                                    Replaced.setTrackDrawable(getApplicationContext().getResources().getDrawable(R.drawable.switch_track_on_deliver));
                                }
                                if (value != Integer.valueOf(p.getQUNTITY())) {
                                    if (p.getPackage_NO() != null && !p.getPackage_NO().equalsIgnoreCase("0")) {
                                        int Duration = 1;
                                        if (task.getDuration() != null) {
                                            Duration = Integer.valueOf(task.getDuration());
                                        }

                                        p.setUNIT_PRICE(String.valueOf(DB.GetPriceReference(p.getPRODUCT_ID(), Duration)));
                                        UpdatePackagesProducts(p.getPackage_NO());
                                    }
                                } else if (p.getPackage_NO() != null && !p.getPackage_NO().equalsIgnoreCase("0")) {
                                    // check if All Product Not Partial in Package
                                    if (CheckQuantityProductsPackages(p.getPackage_NO())) {
                                        //PackageInfo packageInfo=DB.GetPackage(Integer.valueOf(p.getPackage_id()),Integer.valueOf(p.getPRODUCT_ID()));
                                        p.setUNIT_PRICE(String.valueOf(GetActualPriceProduct(p.getPRODUCT_ID(), p.getPackage_id(), p.getTreatment_code())));
                                        UpdateActualPackagesProducts(p.getPackage_NO());
                                    } else {

                                        p.setUNIT_PRICE(String.valueOf(DB.GetPriceReference(p.getPRODUCT_ID(), Integer.valueOf(task.getDuration()))));
                                        UpdatePackagesProducts(p.getPackage_NO());
                                    }
                                }
                                if (p.getPackage_NO() != null && p.getPackage_NO().equalsIgnoreCase("0")) {
                                    double new_price = (((Double.valueOf(p.getUNIT_PRICE()) * Double.valueOf(p.getQUNTITY())) * value)) / Integer.valueOf(p.getQUNTITY());
                                    price.setText(String.valueOf(new_price) + " " + getApplicationContext().getResources().getString(R.string.EGP));
                                }
                                // }
                    /*    else
                        {
                            double new_price = Double.valueOf(p.getUNIT_PRICE())*value;
                            price.setText(String.valueOf(new_price) + " " + getApplicationContext().getResources().getString(R.string.EGP));

                        }*/
                                /*SetTotalPrice();
                                ShowBtns();*/
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                        NotRepresent_Spinner.setSelection(0);
                        RS.setSelection(data_spiner.size() - 1);
                        if(p.getDELIVERY_TYPE()==6)
                            cancel_spinner.setSelection(data_spiner.size() - 1);
                    }
                }
            }
        }
    }

    private void UpdateActualPackagesProducts(String package_no) {
        for (int i = 1; i < table.getChildCount(); i++) {

            TableRow row = (TableRow) table.getChildAt(i);
            if (row.getId() != R.id.last_row) {
                LinearLayout linearLayout_0 = (LinearLayout) row.getChildAt(0);
                TextView Name=(TextView) linearLayout_0.getChildAt(0);
                final TextView price = (TextView) linearLayout_0.getChildAt(4);
                final TextView PNo=(TextView)linearLayout_0.getChildAt(7);
                final TextView Treatment=(TextView) linearLayout_0.getChildAt(6);
                LinearLayout ll=(LinearLayout)linearLayout_0.getChildAt(1);
                final Spinner RS=(Spinner)ll.getChildAt(0);
                if(PNo.getText().toString().equalsIgnoreCase(package_no)) {
                    double RefPrice=GetActualPriceProduct(Replaced_product.get(i-1).getPRODUCT_ID(),Replaced_product.get(i-1).getPackage_id(),Replaced_product.get(i-1).getTreatment_code(),Replaced_product.get(i-1).getDELIVERY_TYPE(),Replaced_product.get(i-1).getPackage_NO());
                    double FinalPrice= Integer.valueOf(RS.getSelectedItem().toString())*RefPrice;

                    price.setText(String.valueOf(RefPrice) + " " + getApplicationContext().getResources().getString(R.string.EGP));
                    // update price in list
                    UpdateReplacedProducts(package_no,Name.getText().toString(),Treatment.getText().toString(),RefPrice);

                }

            }

        }
    }

    private boolean CheckQuantityProductsPackages(String package_no) {
        for (int i = 1; i < table.getChildCount(); i++) {

            TableRow row = (TableRow) table.getChildAt(i);
            if (row.getId() != R.id.last_row) {
                LinearLayout linearLayout_0 = (LinearLayout) row.getChildAt(0);
                TextView Name=(TextView) linearLayout_0.getChildAt(0);
                final TextView price = (TextView) linearLayout_0.getChildAt(4);
                final TextView PNo=(TextView)linearLayout_0.getChildAt(7);
                final TextView Treatment=(TextView) linearLayout_0.getChildAt(6);
                LinearLayout ll=(LinearLayout)linearLayout_0.getChildAt(1);
                final Spinner RS=(Spinner)ll.getChildAt(0);
                if(PNo.getText().toString().equalsIgnoreCase(package_no)) {
                    for (int j = 0; j < Replaced_product.size(); j++) {
                        if (Replaced_product.get(j).getPackage_NO().equalsIgnoreCase(package_no) && Replaced_product.get(j).getNAME().equalsIgnoreCase(Name.getText().toString()) && Replaced_product.get(j).getTreatment_description().equalsIgnoreCase(Treatment.getText().toString())) {
                            if (!Replaced_product.get(j).getQUNTITY().equalsIgnoreCase(RS.getSelectedItem().toString()))

                      //          Toast.makeText(getApplicationContext(),i+","+ RS.getSelectedItem().toString() + "," + Replaced_product.get(j).getQUNTITY(), Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
                }


            }

        }
        return  true;

    }

    private void UpdatePackagesProducts(String package_no) {
        for (int i = 1; i < table.getChildCount(); i++) {

            TableRow row = (TableRow) table.getChildAt(i);
            if (row.getId() != R.id.last_row) {
                LinearLayout linearLayout_0 = (LinearLayout) row.getChildAt(0);
                TextView Name=(TextView) linearLayout_0.getChildAt(0);
                final TextView price = (TextView) linearLayout_0.getChildAt(4);
                final TextView PNo=(TextView)linearLayout_0.getChildAt(7);
                final TextView Treatment=(TextView) linearLayout_0.getChildAt(6);
                LinearLayout ll=(LinearLayout)linearLayout_0.getChildAt(1);
                final Spinner RS=(Spinner)ll.getChildAt(0);
                 if(PNo.getText().toString().equalsIgnoreCase(package_no)) {
                     double RefPrice=DB.GetPriceReferenceByName(Name.getText().toString(),Integer.valueOf(task.getDuration()));
                      double FinalPrice= Integer.valueOf(RS.getSelectedItem().toString())*RefPrice;
                      price.setText(String.valueOf(FinalPrice) + " " + getApplicationContext().getResources().getString(R.string.EGP));
                      // update price in list
                     UpdateReplacedProducts(package_no,Name.getText().toString(),Treatment.getText().toString(),RefPrice);

                 }

            }

        }
    }

    private void UpdateReplacedProducts(String package_no, String Name, String Treat, double refPrice) {
        for(int i=0;i<Replaced_product.size();i++)
        {
            if(Replaced_product.get(i).getPackage_NO().equalsIgnoreCase(package_no)&&Replaced_product.get(i).getNAME().equalsIgnoreCase(Name)&&Replaced_product.get(i).getTreatment_description().equalsIgnoreCase(Treat))
            {
                Replaced_product.get(i).setUNIT_PRICE(String.valueOf(refPrice));
            }
        }
    }


    private void SetTotalPrice() {
        double total_price = 0.0;
        for (int i = 1; i < table.getChildCount(); i++) {

            TableRow row = (TableRow) table.getChildAt(i);
            if (row.getId() != R.id.last_row) {
                LinearLayout linearLayout_0 = (LinearLayout) row.getChildAt(0);
                LinearLayout ll=(LinearLayout)linearLayout_0.getChildAt(1);
                final Spinner RS=(Spinner)ll.getChildAt(0);
                final TextView price = (TextView) linearLayout_0.getChildAt(4);
                String tmp = price.getText().toString().split(" ")[0];
                total_price += Double.valueOf(tmp);
            }

        }
      /*  for (int i = 1; i < tab_layoutPenality.getChildCount(); i++) {

            TableRow row = (TableRow) tab_layoutPenality.getChildAt(i);
            if (row.getId() != R.id.last_row) {
                LinearLayout linearLayout_0 = (LinearLayout) row.getChildAt(0);
                final TextView price = (TextView) linearLayout_0.getChildAt(2);
                String tmp = price.getText().toString().split(" ")[0];
                total_price += Double.valueOf(tmp);
            }

        }*/

        TableRow row = (TableRow) table.getChildAt(table.getChildCount() - 1);
        LinearLayout linearLayout_0 = (LinearLayout) row.getChildAt(0);
        if (row.getId() == R.id.last_row) {
            final TextView price = (TextView) linearLayout_0.getChildAt(0);
            price.setText(getApplicationContext().getResources().getString(R.string.Total) + "=" + String.valueOf(total_price) +" "+ getApplicationContext().getResources().getString(R.string.EGP));
        }
    }
    private String GetTotalPrice()
    {
        TableRow row = (TableRow) table.getChildAt(table.getChildCount() - 1);
        LinearLayout linearLayout_0 = (LinearLayout) row.getChildAt(0);
        if (row.getId() == R.id.last_row) {
            final TextView price = (TextView) linearLayout_0.getChildAt(0);
            String Price_val=price.getText().toString().split("=")[1].split(" ")[0];
            return Price_val;
        }
        return null;
    }
    private void ShowBtns()
    {

        int count =0;
        int count2=0;
        int count_cancel=0;
        int count_cancel2=0;

        if(ProductLst!=null)
        {
            for(int i=1;i<table.getChildCount()-1;i++)
            {   View child=table.getChildAt(i);
                TableRow row = (TableRow) child;
                LinearLayout linearLayout_0 = (LinearLayout) row.getChildAt(0);
                LinearLayout ll=(LinearLayout)linearLayout_0.getChildAt(1);
                final Spinner RS=(Spinner)ll.getChildAt(0);
                final Spinner cancel_spinner = (Spinner) ((LinearLayout) linearLayout_0.getChildAt(3)).getChildAt(0);


                if(RS.getAdapter().getCount()!=0&&RS.getSelectedItemPosition()!=RS.getAdapter().getCount()-1)
                {
                    count++;
                }
                if(RS.getSelectedItem().toString().equalsIgnoreCase("0"))
                {
                    count2++;
                }
                if(cancel_spinner.getSelectedItemPosition()==cancel_spinner.getAdapter().getCount()-1)
                {
                    count_cancel++;
                }
                if(!cancel_spinner.getSelectedItem().toString().equalsIgnoreCase("0"))
                {
                    count_cancel2++;
                }

            }
          /*  for(int i=1;i<tab_layoutPenality.getChildCount();i++)
            {   View child=tab_layoutPenality.getChildAt(i);
                TableRow row = (TableRow) child;
                LinearLayout linearLayout_0 = (LinearLayout) row.getChildAt(0);
                LinearLayout ll=(LinearLayout)linearLayout_0.getChildAt(1);
                final Spinner RS=(Spinner)ll.getChildAt(0);
                if(RS.getSelectedItemPosition()!=RS.getAdapter().getCount()-1)
                {
                    count++;
                }
                if(RS.getSelectedItem().toString().equalsIgnoreCase("0"))
                {
                    count2++;
                }
            }*/

        }

        int z=table.getChildCount()-2;
        if(count2>0&&(count2==(table.getChildCount()-2)))
        {
            btn_deliver.setVisibility(View.GONE);
            btn_partial.setVisibility(View.GONE);
            btn_notDeliver.setVisibility(View.VISIBLE);
            btn_cancelation.setVisibility(View.GONE);
        }
        else if(count>0)
        {
            btn_deliver.setVisibility(View.GONE);
           // btn_notDeliver.setVisibility(View.GONE);
            btn_partial.setVisibility(View.VISIBLE);
            btn_notDeliver.setVisibility(View.VISIBLE);
            btn_cancelation.setVisibility(View.GONE);
        }
        else {
            btn_deliver.setVisibility(View.VISIBLE);
          //  btn_notDeliver.setVisibility(View.GONE);
            btn_partial.setVisibility(View.GONE);
            btn_notDeliver.setVisibility(View.VISIBLE);
            btn_cancelation.setVisibility(View.GONE);

          }
        if(count_cancel==(table.getChildCount()-2))
        {
            btn_cancelation.setVisibility(View.VISIBLE);
            btn_deliver.setVisibility(View.GONE);
            btn_notDeliver.setVisibility(View.GONE);
            btn_partial.setVisibility(View.GONE);
        }
        else if(count_cancel2>0&&count2>0)
        {
            btn_deliver.setVisibility(View.GONE);
            // btn_notDeliver.setVisibility(View.GONE);
            btn_partial.setVisibility(View.VISIBLE);
            btn_notDeliver.setVisibility(View.VISIBLE);
            btn_cancelation.setVisibility(View.GONE);

        }
    }
    private List<Product> GetReplacedProducts(boolean check) {
        List<Product> products = new ArrayList<>();

        for (int i = 1; i < table.getChildCount() - 1; i++) {
            View child = table.getChildAt(i);
            TableRow row = (TableRow) child;
            LinearLayout linearLayout_0 = (LinearLayout) row.getChildAt(0);
            LinearLayout ll = (LinearLayout) linearLayout_0.getChildAt(1);
            final Spinner RS = (Spinner) ll.getChildAt(0);
            TextView price = (TextView) linearLayout_0.getChildAt(4);
            final Spinner cancel_spinner = (Spinner) ((LinearLayout) linearLayout_0.getChildAt(3)).getChildAt(0);

            Product p = new Product();
            p = Replaced_product.get(i - 1);

            if (RS.getAdapter().getCount() == 0) // collection
            {
                p.setBUY_PRICE(p.getUNIT_PRICE());
                products.add(p);
            } else if (!RS.getSelectedItem().toString().equalsIgnoreCase("0")) {

                p.setAID(task.getAID());
                p.setQUNTITY_REPLACED(String.valueOf(Integer.valueOf(RS.getSelectedItem().toString())));
                // p.setUNIT_PRICE(String.valueOf(Double.valueOf(price.getText().toString().split("\\ ")[0])));

                double RefPrice=GetReplacedPriceProduct(p.getPRODUCT_ID(),p.getPackage_id(),p.getTreatment_code(),p.getDELIVERY_TYPE(),p.getPackage_NO());
                double TotalPrice = (Integer.valueOf(p.getQUNTITY())-Integer.valueOf(cancel_spinner.getSelectedItem().toString())) * Double.valueOf(RefPrice);
                double CurrentPrice = TotalPrice / Integer.valueOf(p.getQUNTITY_REPLACED());
                if (!check)
                    p.setBUY_PRICE(String.valueOf(Double.valueOf(price.getText().toString().split("\\ ")[0]) / Integer.valueOf(RS.getSelectedItem().toString())));
                else
                    p.setBUY_PRICE(String.valueOf(CurrentPrice));

                if(p.getPackage_NO()!=null&&!p.getPackage_NO().equalsIgnoreCase("0")) // package
                {
                    // check if select all Quantity
                    if(RS.getSelectedItem().toString().equalsIgnoreCase(p.getQUNTITY()))  // check done or partial
                    {

                        if(CheckQuantityProductsPackages(p.getPackage_NO()))
                        {
                            RefPrice=GetActualPriceProduct(p.getPRODUCT_ID(),p.getPackage_id(),p.getTreatment_code(),p.getDELIVERY_TYPE(),p.getPackage_NO());
                            p.setBUY_PRICE(String.valueOf(RefPrice));

                        }
                        else
                        {
                            if(!check) // no full Payment
                            {
                                p.setBUY_PRICE(String.valueOf(Double.valueOf(price.getText().toString().split("\\ ")[0]))) ;
                            }
                            else
                            {
                                RefPrice=GetActualPriceProduct(p.getPRODUCT_ID(),p.getPackage_id(),p.getTreatment_code(),p.getDELIVERY_TYPE(),p.getPackage_NO());
                                p.setBUY_PRICE(String.valueOf(RefPrice));
                            }
                        }
                       // p.setBUY_PRICE(String.valueOf(RefPrice));
                    }
                    else
                    {

                        // partial
                        if(!check) // no full Payment
                        {
                            p.setBUY_PRICE(String.valueOf(Double.valueOf(price.getText().toString().split("\\ ")[0]) / Integer.valueOf(RS.getSelectedItem().toString())));
                        }
                        else
                        {
                             RefPrice=GetActualPriceProduct(p.getPRODUCT_ID(),p.getPackage_id(),p.getTreatment_code(),p.getDELIVERY_TYPE(),p.getPackage_NO());
                            p.setBUY_PRICE(String.valueOf(RefPrice));
                        }

                    }

                }
                products.add(p);
            } else if (RS.getSelectedItem().toString().equalsIgnoreCase("0") && check) {
                p.setAID(task.getAID());
                p.setQUNTITY_REPLACED(String.valueOf("0"));
                // p.setUNIT_PRICE(String.valueOf(Double.valueOf(price.getText().toString().split("\\ ")[0])));
                double RefPrice=GetActualPriceProduct(p.getPRODUCT_ID(),p.getPackage_id(),p.getTreatment_code(),p.getDELIVERY_TYPE(),p.getPackage_NO());
                double TotalPrice = (Integer.valueOf(p.getQUNTITY())-Integer.valueOf(cancel_spinner.getSelectedItem().toString()))* Double.valueOf(RefPrice);
             //   double TotalPrice=Integer.valueOf(p.getQUNTITY())*Double.valueOf(p.getUNIT_PRICE());
                double CurrentPrice=TotalPrice;
                p.setBUY_PRICE(String.valueOf(CurrentPrice));
                products.add(p);
            }


        }
        //////
      /*  for(int i=1;i<tab_layoutPenality.getChildCount();i++)
        {   View child=tab_layoutPenality.getChildAt(i);
            TableRow row = (TableRow) child;
            LinearLayout linearLayout_0 = (LinearLayout) row.getChildAt(0);
            LinearLayout ll=(LinearLayout)linearLayout_0.getChildAt(1);
            final Spinner RS=(Spinner)ll.getChildAt(0);
            TextView price=(TextView) linearLayout_0.getChildAt(2);
            if(!RS.getSelectedItem().toString().equalsIgnoreCase("0"))
            {
                Product p=new Product();
                p=penalties_products.get(i-1);
                p.setAID(task.getAID());
                p.setQUNTITY_REPLACED(String.valueOf(Integer.valueOf(RS.getSelectedItem().toString())));
                // p.setUNIT_PRICE(String.valueOf(Double.valueOf(price.getText().toString().split("\\ ")[0])));
                double TotalPrice=Integer.valueOf(p.getQUNTITY())*Double.valueOf(p.getUNIT_PRICE());
                double CurrentPrice=TotalPrice/Integer.valueOf(RS.getSelectedItem().toString());
                if(!check)
                    p.setBUY_PRICE(String.valueOf(Double.valueOf(price.getText().toString().split("\\ ")[0])/Integer.valueOf(RS.getSelectedItem().toString())));
                else
                    p.setBUY_PRICE(String.valueOf(CurrentPrice));
                products.add(p);
            }
        }*/
        return products;
    }

    private List<Product> GetCancelationProducts() {
        List<Product> products = new ArrayList<>();
        for (int i = 1; i < table.getChildCount() - 1; i++) {
            View child = table.getChildAt(i);
            TableRow row = (TableRow) child;
            LinearLayout linearLayout_0 = (LinearLayout) row.getChildAt(0);
            LinearLayout ll = (LinearLayout) linearLayout_0.getChildAt(1);
            final Spinner RS = (Spinner) ll.getChildAt(0);
            final Spinner cancel_spinner = (Spinner) ((LinearLayout) linearLayout_0.getChildAt(3)).getChildAt(0);
            Product p = new Product();
            p = CancelationProducts.get(i - 1);
            if(!cancel_spinner.getSelectedItem().toString().equalsIgnoreCase("0"))
            {

                if(p.getHANDLE()>0)
                p.setHANDLE(Integer.valueOf(cancel_spinner.getSelectedItem().toString()));
                p.setQUNTITY_REPLACED(cancel_spinner.getSelectedItem().toString());
                p.setQUNTITY(cancel_spinner.getSelectedItem().toString());
                products.add(p);
            }
        }
           return products;

        }



    private List<Product> GetNotReplacedProducts()
    {
        List<Product> products=new ArrayList<>();
        for(int i=1;i<table.getChildCount()-1;i++)
        {   View child=table.getChildAt(i);
            TableRow row = (TableRow) child;
            LinearLayout linearLayout_0 = (LinearLayout) row.getChildAt(0);
            final Spinner NotRepresent_Spinner=(Spinner)((LinearLayout)linearLayout_0.getChildAt(2)).getChildAt(0);
        /*  LinearLayout ll=(LinearLayout)linearLayout_0.getChildAt(1);
            final Spinner RS=(Spinner)ll.getChildAt(0);*/
            TextView price=(TextView) linearLayout_0.getChildAt(4);
            if(NotRepresent_Spinner.getAdapter().getCount()>0&&!NotRepresent_Spinner.getSelectedItem().toString().equalsIgnoreCase("0"))
            {
                Product p=new Product();
                p=NotReplaced_product.get(i-1);
                p.setAID(task.getAID());
                p.setBUY_PRICE(String.valueOf(Double.valueOf(price.getText().toString().split("\\ ")[0])));
                p.setQUNTITY_REPLACED(String.valueOf(Integer.valueOf(NotRepresent_Spinner.getSelectedItem().toString())));
                products.add(p);
            }
        }
        return products;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
     /*   if(Type.equalsIgnoreCase("driver_tasks"))
        getMenuInflater().inflate(R.menu.activity_details_menu,menu);*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_assign_task)
        {
            final Dialog dialog = new Dialog(OrderDetailsActivity.this);
            //set layout custom
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.lst_pilot_name);
            final RecyclerView recView_pilot = (RecyclerView) dialog.findViewById(R.id.recView_pilot);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recView_pilot.setLayoutManager(mLayoutManager);
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MY_PREFS_TASks, 0);
            List<Pilot>mPilot=new ArrayList<>();
            if(sharedPreferences.contains("Pilots"))
            {
                Pilot[] pilots = ServerManager.deSerializeStringToObject(sharedPreferences.getString("Pilots",""), Pilot[].class);
                mPilot = Arrays.asList(pilots);
            }
            PilotAdapter pilotAdapter=new PilotAdapter(null,dialog,getApplicationContext(),mPilot,"Assign",null);
            recView_pilot.setAdapter(pilotAdapter);
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
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
            DB.UpdateReplacedAmount(TmpActionObject.getReplacedProducts());
            DB.UpdateAfterAction(null,false);
          //  DB.UpdateCoverPilot(TmpActionObject.getReplacedProducts(),UserName);

                if(task.getTelNo()!=null)
                presenter.SendSMS(task.getTelNo(), TmpActionObject.getAssignmentID(),Products_Changed,Products_NotChanged,Products_Canceled);

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

    class CheckCashing extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            OrderDetailsActivity.this.StopLoading();
            try {
                presenter.setAssignmentAction(TmpActionObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           OrderDetailsActivity.this.ShowLoading();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }

        @Override
        protected String doInBackground(String... params) {
            List<OFFLINE> lst_off=DB.GetCashingRequest();
            user = new User(UserName, "", GetCurrentLocaion.CurrentLoc, " ");
            IContractOffline.Presenter offlinePresnter=new OfflinePresenter(OrderDetailsActivity.this, getApplicationContext(), user);
            if(lst_off.size()>0) {
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
            return "true";
        }
    }
   /* public boolean CheckProductsPilots()  // old
    {
        boolean flag=false;
        List<Pilot_Cover>lst_pilotCover=DB.GetCoverPilot(UserName);
        for(int i=0;i<ProductLst.size();i++)
        {
            if(ProductLst.get(i).getDELIVERY_TYPE()!=5&&ProductLst.get(i).getDELIVERY_TYPE()!=6) {
                flag = false;
                for (int j = 0; j < lst_pilotCover.size(); j++) {
                    if (ProductLst.get(i).getPRODUCT_ID().equalsIgnoreCase(lst_pilotCover.get(j).getProduct_ID()) && ProductLst.get(i).getTreatment_code().equalsIgnoreCase(lst_pilotCover.get(j).getTreatment_ID()) && Integer.valueOf(GetProdQTY(ProductLst.get(i))) <= (lst_pilotCover.get(j).getCurrent_Quantity())) {
                        flag = true;

                    }
                }
                if (flag == false)
                    return false;
            }
        }
        return true;
    }*/
   public boolean CheckProductsPilots()
   {
       boolean flag=false;
       List<Pilot_Cover>lst_pilotCover=DB.GetCoverPilot(UserName);
       List<Product>products_clean= GetReplacedProducts(false);
       if(Contract_status.equalsIgnoreCase("Not Delivered"))
       {
           products_clean=new ArrayList<>();
       }
       for(int i=0;i<products_clean.size();i++)
       {
           if(products_clean.get(i).getDELIVERY_TYPE()!=5) {
               flag = false;
               for (int j = 0; j < lst_pilotCover.size(); j++) {
                   if (products_clean.get(i).getPRODUCT_ID().equalsIgnoreCase(lst_pilotCover.get(j).getProduct_ID()) &&
                           products_clean.get(i).getTreatment_code().equalsIgnoreCase(lst_pilotCover.get(j).getTreatment_ID())
                           && Integer.valueOf(GetProdQTY(products_clean.get(i),products_clean,true)) <= (lst_pilotCover.get(j).getCurrent_Quantity())) {
                       flag = true;

                   }
               }
               if (flag == false)
                   return false;
           }
       }
       return true;
   }
    private int GetProdQTY(Product product,List<Product>lst_prod,boolean flag) {
        int qty=0;


                if (product.getDELIVERY_TYPE() != 5) {
                    for (int j = 0; j < lst_prod.size(); j++) {

                        if (product.getPRODUCT_ID().equalsIgnoreCase(lst_prod.get(j).getPRODUCT_ID())
                                && product.getTreatment_code().equalsIgnoreCase(lst_prod.get(j).getTreatment_code())
                                ) {
                            if (flag) {
                                qty += Integer.valueOf(lst_prod.get(j).getQUNTITY_REPLACED());
                            }

                        }
                    }

                }


        return qty;

    }


    public boolean CheckCancelediProductsInCover(List<Product>replaced_products)
    {
        if(replaced_products!=null&&replaced_products.size()>0) {
            boolean flag = true;
            List<Pilot_Cover> lst_pilotCover = DB.GetCoverPilot(UserName);
            for (int i = 0; i < replaced_products.size(); i++) {
                if (replaced_products.get(i).getDELIVERY_TYPE() == 6) {
                    flag = false;
                    for (int j = 0; j < lst_pilotCover.size(); j++) {
                        if (replaced_products.get(i).getPRODUCT_ID().equalsIgnoreCase(lst_pilotCover.get(j).getProduct_ID()) &&
                                replaced_products.get(i).getTreatment_code().equalsIgnoreCase(lst_pilotCover.get(j).getTreatment_ID())
                                && Integer.valueOf(GetRep_canceledProdQTY(replaced_products.get(i),replaced_products)) <= (lst_pilotCover.get(j).getCurrent_Quantity())) {
                            flag = true;

                        }
                    }
                    if (flag == false)
                        return false;
                }
            }

            return true;
        }
        else
        {
            return true;
        }
    }
    private int GetRep_canceledProdQTY(Product product,List<Product>Replaced) {
        int qty=0;



            for (int j = 0; j < Replaced.size(); j++) {
                if (product.getPRODUCT_ID().equalsIgnoreCase(Replaced.get(j).getPRODUCT_ID()) && product.getTreatment_code()
                        .equalsIgnoreCase(Replaced.get(j).getTreatment_code())
                        ) {
                    qty += Integer.valueOf(Replaced.get(j).getQUNTITY_REPLACED());

                }


        }


        return qty;

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
