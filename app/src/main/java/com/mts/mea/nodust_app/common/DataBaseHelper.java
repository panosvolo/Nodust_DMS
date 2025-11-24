package com.mts.mea.nodust_app.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDiskIOException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.widget.Toast;

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
import com.mts.mea.nodust_app.Pilots.Reconcilation;
import com.mts.mea.nodust_app.Street;
import com.mts.mea.nodust_app.User.User;
import com.mts.mea.nodust_app.User.UserInfo;
import com.mts.mea.nodust_app.Vacation.IContractVacation;
import com.mts.mea.nodust_app.Vacation.VacationPresenter;
import com.mts.mea.nodust_app.Vacation.VacationRequest;
import com.mts.mea.nodust_app.Vacation.vacation_reasons;
import com.mts.mea.nodust_app.orders.GetOrders.GetOrdersPresenter;
import com.mts.mea.nodust_app.orders.GetOrders.HomeWithMenuActivity;
import com.mts.mea.nodust_app.orders.GetOrders.IContractGetOrders;
import com.mts.mea.nodust_app.orders.Task;
import com.mts.mea.nodust_app.products.Collection;
import com.mts.mea.nodust_app.products.PackageInfo;
import com.mts.mea.nodust_app.products.Product;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.mts.mea.nodust_app.User.User.UserName;


public class DataBaseHelper extends SQLiteOpenHelper implements IContractGetOrders.View,IContractVacation.View,IPilotCover.View,IContractOffline.View {
    private static final int DATABASE_VERSION = 32;
    private Context mContext;
    ProgressDialog pbDailog;
    HomeWithMenuActivity mHomeWithMenuActivity;
    UserInfo userInfo;
    IContractGetOrders.Presenter mPresnter;
    boolean NextActivity;
    // Database Name
    private static final String DATABASE_NAME = "NODustManager2";

    // Contacts table name
    private static final String TABLE_COVER_PRODUCT = "PRODUCT_COVER";
    private static final String TABLE_CONTRACTS = "ASSIGN_WORK_X_Y";
    private static final String TABLE_Pilot = "PILOT_DRIVER";
    private static final String TABLE_PROD_CONTRACT = "PRODUCT_CONTRACT";
    private static final String TABLE_KPI = "KPI";
    private static final String TABLE_PACKAGES = "PACKAGES";
    private static final String TABLE_PRODUCTS = "PRODUCTS";
    private static final String TABLE_VACATION="VACATIONS";
    private static final String TABLE_VACATION_REASONS="VACATION_REASONS";

    private static final String TABLE_PILOT_COVER="PILOT_COVER";
    private static final String TABLE_CASHIG_REQUESTS="CASHIG_REQUESTS";
    private static final String TABLE_COLLECTION="COLLECTION";
    private static final String TABLE_RECONCILATION="Reconcillation";



    private static final String CREATE_TABLE_COLLECTION = "CREATE TABLE "
            + TABLE_COLLECTION + "(" + "ASSIGN_ID" + " TEXT," + "AID" + " INTEGER," + "CARD_NO" + " TEXT," + "INVOICE_NO"
            + " TEXT," +  "AMOUNT" + " REAL," + "PAID_AMOUNT" + " REAL,DATE TEXT"+")";

    private static final String CREATE_TABLE_CASHIG_REQUESTS = "CREATE TABLE "
            + TABLE_CASHIG_REQUESTS + "(" + "ACTIONDATE" + " TEXT," + "ID" + " INTEGER," + "TYPE" + " TEXT," + "BODY"
            + " TEXT," +  "USERNAME" + " TEXT," + "LONGITUDE" + " TEXT," +
            "LATITUDE Text, " + "DESCRIPTION TEXT, " + "AID TEXT,URL Text)";


    private static final String CREATE_TABLE_CONTRACT = "CREATE TABLE "
            + TABLE_CONTRACTS + "(" + "ASSIGN_ID" + " TEXT," + "DRIV_ID" + " INTEGER," + "CARD_NO" + " TEXT," + "CLIENT_NAME"
            + " TEXT," + "AREA_ID" + " INTEGER," + "AREA_NAME" + " TEXT," + "city_name" + " TEXT," +
            "region_name Text, " + "street_name TEXT, " + "home_no TEXT, " + " floor_no TEXT, " + "flat_no TEXT," +
            "remarks TEXT, " + "DATA_COMMENT" + " TEXT," + "OPERATION_COMMENT" + " TEXT," + "CHANGE_DATE" + " DATE,"
            + "X" + " REAL," + "Y" + " REAL," + "PRIORITY" + " INTEGER," + "TelNo" + " TEXT," + "RecieptNo" + " TEXT," +
            "Status" + " TEXT ," + "PILOT_ID" + " TEXT," + "AID" + " TEXT," + "ASSIGN_DATE DATE," + "LAST_DOWNLOAD DATE," +
            "ORDER_DELIVERY INTEGER," + "CREAED_STATE INTEGER," + "CHANGED_STATE INTEGER," + "DELIVERY_STATE INTEGER," +
            "CUSTOMER_TYPE TEXT," + "CONTRACT_TYPE TEXT," + "ADDRESS Text," + "AREA2_NAME Text," + "PAY_CREDIT TEXT," +
            "Duration Text,FU_Note TEXT," +" ACTUAL_PAID real,TOTAL_PRICE_PAID real,ALLOW_CREDIT integer,bonus real,FROM_TIME TEXT,TO_TIME TEXT,ASSIGNMENT_TYPE INTEGER,LOC_CONFIRMED INTEGER"+ ")";
    private static final String CREATE_TABLE_PRODUCT_CONTRACT = "CREATE TABLE " + TABLE_PROD_CONTRACT +
            "(" + "PRODUCT_ID" + " Text," + "AID" + " TEXT," + "QUANTITY" + " INTEGER," + "QUANTITY_REPLACED" +
            " INTEGER," + "UNIT_PRICE" + " TEXT," + "CARD_NO" + " TEXT," + "NAME TEXT," + "BUY_PRICE TEXT," + "ASSIGN_ID TEXT," +
            "Treatment_code text,Treatment_description text,Package_id text,Package_NO integer," + "Description TEXT," + "Type Text," +
            "KIND Text," +"HANDLE integer, DELIVERY_TYPE integer"+ ")";
    private static final String CREATE_TABLE_PILOT = "CREATE TABLE " + TABLE_Pilot + "(" + "PILOT_ID" + " INTEGER," + "FULL_NAME_AR" + " TEXT," + "FULL_NAME_EN" + " TEXT," + "ATTENDANCE_TIME_PILOT" + " TEXT," + "DRIVER_ID INTEGER," + "ASSIGN_ID TEXT," + "RETURN_TIME_PILOT TEXT," + "EVALUTION_DATE TEXT,AREA_NAME TEXT" + ")";
    private static final String CREATE_TABLE_KPI = "CREATE TABLE " + TABLE_KPI + "(" + "ID INTEGER,NAME TEXT,KPI_Type TEXT)";
    private static final String CREATE_COVER_PRODUCT = "CREATE TABLE " + TABLE_COVER_PRODUCT + "(" + "id text,"+ "PRODUCT_ID text,NAME TEXT,QUANTITY INTEGER,Treatment_ID INTEGER,Treatment_Description TEXT,Driver_id INTEGER,CurrentQuantity integer,area_id text,area_name text,DirtyQTY integer )";
    // private static final String CREATE_TABLE_PACKAGES="CREATE TABLE "+TABLE_PACKAGES+"("+"Package_id INTEGER,Product_id INTEGER,Quantity INTEGER,Price real )";
    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE " + TABLE_PRODUCTS + "(" + "PRODUCT_ID Text,NAME TEXT,PRICE_1W TEXT,PRICE_2W TEXT,PRICE_4W TEXT,SELLING_PRICE TEXT,B_PRICE TEXT,TYPE TEXT,KIND TEXT )";
    private  static final String CREATE_TABLE_VACATION="CREATE TABLE "+TABLE_VACATION+
            "(REQUESTERID TEXT,RequestDate TEXT,Reason_AR TEXT,Reason_EN TEXT,Notes TEXT," +
            "Vacation_from_date TEXT,Vacation_to_date TEXT,No_days INTEGER,approval_status_EN TEXT,approval_status_AR TEXT," +
            "ded_day TEXT,ded_payment TEXT,Request_state INTEGER,Performer TEXT,PerformerDate TEXT,SupervisorComment TEXT,userName text)";
    private static final String CREATE_TABLE_PILOT_COVER="CREATE TABLE "+TABLE_PILOT_COVER+"(ID text,Pilot_ID text,Cover_Driver_ID text,Assign_Date text,Quantity integer,Current_Quantity integer,Product_ID text,name text,Treatment_ID text,Treatment_Name text,DRIVER_ID text,Pilot_Accept Text,Dirty_Qty integer,Actual_Clean text,Actual_Dirty text,Approved_clean text,Approved_Dirty text,Driver_Accept text,TOTAL_RECIEPT_AMOUNT_PILOT text,TOTAL_RECIEPT_AMOUNT_DRIVER text,Area_ID text ,Area_Name text)";
    private  static final String CREATE_TABLE_VACATION_REASONS="CREATE TABLE "+TABLE_VACATION_REASONS+"(ID TEXT,NAME_AR TEXT,NAME_EN TEXT,DEDUCTION TEXT,ALLOWED_DAY0 integer)";
    private static final String CREATE_TABLE_Reconcilation="CREATE TABLE "+TABLE_RECONCILATION+"(ID text,MID text," +
            "PRODUCT text,TREATMENT text,EXP_CLEAN text,EXP_DIRTY text,ACTUAL_CLEAN text,ACTUAL_DIRTY text," +
            "APPROVED_CLEAN text,APPROVED_DIRTY text,TIME text,DATE Text,AREA text,AREA_NAME text,PILOT text," +
            "DRIVER text,TRE_NAME text,PRODUCT_NAME TEXT,EXPECTED_MONEY text,ACTUAL_MONEY text,APPROVED_MONEY text" +
            ")";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONTRACT);
        // db.execSQL(CREATE_TABLE_PRODUCT);
        db.execSQL(CREATE_TABLE_PRODUCT_CONTRACT);
        db.execSQL(CREATE_TABLE_PILOT);
        db.execSQL(CREATE_TABLE_KPI);
        db.execSQL(CREATE_COVER_PRODUCT);
         db.execSQL(CREATE_TABLE_COLLECTION);
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_VACATION);
        db.execSQL(CREATE_TABLE_PILOT_COVER);
        db.execSQL(CREATE_TABLE_VACATION_REASONS);
        db.execSQL(CREATE_TABLE_CASHIG_REQUESTS);
        db.execSQL(CREATE_TABLE_Reconcilation);

    }

    @Override
    public void ShowMsg(String msg) {

    }

    @Override
    public void InsertVacation(VacationRequest obj) {

    }
    public  void AddVacationReasons(List<vacation_reasons>lst)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        //SQLiteDatabase dbReader=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        for(int i=0;i<lst.size();i++) {
            vacation_reasons vacationReason = lst.get(i);
            values.put("ID", vacationReason.getID());
            values.put("NAME_AR", vacationReason.getNAME_AR());
            values.put("NAME_EN", vacationReason.getNAME_EN());
            values.put("DEDUCTION", vacationReason.getDEDUCTION());
            values.put("ALLOWED_DAY0",vacationReason.getALLOWED_DAY0());
            db.insert(TABLE_VACATION_REASONS, null, values);
        }
        db.close();


    }
    public List<vacation_reasons> GetVacationReasons()
    {

        List<vacation_reasons> Tasks = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_VACATION_REASONS ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                vacation_reasons v = new vacation_reasons();
                v.setID(cursor.getString(cursor.getColumnIndex("ID")));
                v.setNAME_AR(cursor.getString(cursor.getColumnIndex("NAME_AR")));
                v.setNAME_EN(cursor.getString(cursor.getColumnIndex("NAME_EN")));
                v.setDEDUCTION(cursor.getString(cursor.getColumnIndex("DEDUCTION")));
                v.setALLOWED_DAY0(cursor.getInt(cursor.getColumnIndex("ALLOWED_DAY0")));
                Tasks.add(v);
            } while (cursor.moveToNext());
        }
        db.close();
        return Tasks;

    }
    public boolean InsertVacations(VacationRequest vacationRequest)
     {
         SQLiteDatabase db = this.getWritableDatabase();
         ContentValues values=new ContentValues();
         values.put("REQUESTERID",vacationRequest.getRequesterID());
         values.put("RequestDate",vacationRequest.getRequestDate());
         values.put("Reason_AR",vacationRequest.getReason_AR());
         values.put("Reason_EN",vacationRequest.getReason_EN());

         values.put("Notes",vacationRequest.getNotes());
         values.put("Vacation_from_date",vacationRequest.getVacation_from_date());
         values.put("Vacation_to_date",vacationRequest.getVacation_to_date());
         values.put("No_days",vacationRequest.getNo_days());
         values.put("approval_status_AR",vacationRequest.getApproval_status_AR());
         values.put("approval_status_EN",vacationRequest.getApproval_status_EN());

         values.put("ded_day",vacationRequest.getDed_day());
         values.put("ded_payment",vacationRequest.getDed_payment());
         values.put("Performer",vacationRequest.getPerformer());
         values.put("PerformerDate",vacationRequest.getPerformerDate());
         values.put("SupervisorComment",vacationRequest.getSupervisorComment());
         values.put("userName",vacationRequest.getUserName());
         db.insert(TABLE_VACATION,null,values);
         db.close();
         return true;

     }

    @Override
    public void InsertVaction(List<VacationRequest> lst) {
        if(lst!=null)
            InsertALLVacation(lst);

    }

    @Override
    public void InsertVactionReason(List<vacation_reasons> lst) {
        if(lst!=null)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_VACATION_REASONS, null, null);
            db.close();
            AddVacationReasons(lst);
    }}

    @Override
    public void ShowDialog() {

    }

    @Override
    public void HideDialog() {

    }

    public boolean InsertALLVacation(List<VacationRequest> lstvacationRequest)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase dbReader=this.getReadableDatabase();
        ContentValues values=new ContentValues();
        for(int i=0;i<lstvacationRequest.size();i++) {
            VacationRequest vacationRequest=lstvacationRequest.get(i);
            values.put("REQUESTERID", vacationRequest.getRequesterID());
            values.put("RequestDate", vacationRequest.getRequestDate());
            values.put("Reason_AR", vacationRequest.getReason_AR());
            values.put("Reason_EN", vacationRequest.getReason_EN());
            values.put("Notes", vacationRequest.getNotes());
            values.put("Vacation_from_date", vacationRequest.getVacation_from_date());
            values.put("Vacation_to_date", vacationRequest.getVacation_to_date());
            values.put("No_days", vacationRequest.getNo_days());
            values.put("approval_status_AR", vacationRequest.getApproval_status_AR());
            values.put("approval_status_EN", vacationRequest.getApproval_status_EN());
            values.put("ded_day", vacationRequest.getDed_day());
            values.put("ded_payment", vacationRequest.getDed_payment());
            values.put("Performer", vacationRequest.getPerformer());
            values.put("PerformerDate", vacationRequest.getPerformerDate());
            values.put("SupervisorComment", vacationRequest.getSupervisorComment());
            values.put("userName",vacationRequest.getUserName());
            String selectQuery = "SELECT * FROM " + TABLE_VACATION + " where REQUESTERID='" + lstvacationRequest.get(i).getRequesterID() + "'";

            Cursor cursor = dbReader.rawQuery(selectQuery, null);
            if (cursor.getCount() <= 0) {
                db.insert(TABLE_VACATION, null, values);
            }
            else
            {

                db.update(TABLE_VACATION,values,"REQUESTERID='" + lstvacationRequest.get(i).getRequesterID() + "'",null);
            }
            cursor.close();
        }
        db.close();
        dbReader.close();
        return true;

    }
    public List<VacationRequest> GetVacations()
    {
        SharedPreferences Area_name = mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        List<VacationRequest> Tasks = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_VACATION +" where userName='"+ Area_name.getString("UserName", "")+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                VacationRequest v = new VacationRequest();
                v.setRequesterID(cursor.getString(cursor.getColumnIndex("REQUESTERID")));
                v.setRequestDate(cursor.getString(cursor.getColumnIndex("RequestDate")));
                v.setReason_AR(cursor.getString(cursor.getColumnIndex("Reason_AR")));
                v.setReason_EN(cursor.getString(cursor.getColumnIndex("Reason_EN")));
                v.setNotes(cursor.getString(cursor.getColumnIndex("Notes")));
                v.setVacation_from_date(cursor.getString(cursor.getColumnIndex("Vacation_from_date")));
                v.setVacation_to_date(cursor.getString(cursor.getColumnIndex("Vacation_to_date")));
                v.setApproval_status_AR(cursor.getString(cursor.getColumnIndex("approval_status_AR")));
                v.setApproval_status_EN(cursor.getString(cursor.getColumnIndex("approval_status_EN")));
                v.setSupervisorComment(cursor.getString(cursor.getColumnIndex("SupervisorComment")));
                Tasks.add(v);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return Tasks;
    }

    public int GetRepetedCard(String CardNo)
    {

        int tmp=0;
        String selectQuery = "SELECT count(*) from "+TABLE_CONTRACTS+" WHERE CARD_NO='"+CardNo+"'";
        //selectQuery = "SELECT date('2018-03-27 13:47:11')";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                tmp=cursor.getInt(0);

            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return tmp;

    }

    public void UpdateReplacedAmount(List<Product> products) {
        double TotalAmount = 0;
        if (products != null) {
            SQLiteDatabase db = this.getWritableDatabase();

            for (int i = 0; i < products.size(); i++) {
                TotalAmount += Double.valueOf(products.get(i).getUNIT_PRICE());
                ContentValues values = new ContentValues();
                values.put("QUANTITY_REPLACED", String.valueOf(products.get(i).getQUNTITY_REPLACED()));
                values.put("BUY_PRICE", products.get(i).getBUY_PRICE());
                db.update(TABLE_PROD_CONTRACT, values, "PRODUCT_ID ='" + products.get(i).getPRODUCT_ID() + "' and AID= " + "'" + products.get(i).getAID() + "'" + "and Treatment_code='" + products.get(i).getTreatment_code() + "'" + "and Package_id='" + products.get(i).getPackage_id() + "' and DELIVERY_TYPE="+products.get(i).getDELIVERY_TYPE()+" and Package_NO='"+products.get(i).getPackage_NO()+"'", null);
               /* if(i==products.size()-1)
                {
                    ContentValues val=new ContentValues();
                    Log.d("TotalAmount",String.valueOf(TotalAmount));
                    val.put("TotalPrice","'"+String.valueOf(TotalAmount)+"'");
                    db.update(TABLE_CONTRACTS,val,"AID =" + "'" + products.get(0).getAID() + "'",null);

                }*/
            }


            db.close();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROD_CONTRACT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTRACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Pilot);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KPI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COVER_PRODUCT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VACATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PILOT_COVER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VACATION_REASONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CASHIG_REQUESTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLLECTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECONCILATION);



      //  db.execSQL("DROP TABLE IF EXISTS "+TABLE_PACKAGES);



        //  db.execSQL("DROP TABLE IF EXISTS " +TABLE_PACKAGES);
        // Create tables again
        onCreate(db);
    }

    public void SetPackages(List<PackageInfo> packages) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PACKAGES, null, null);
        ContentValues values = new ContentValues();
        if (packages != null) {
            for (int i = 0; i < packages.size(); i++) {
                values = new ContentValues();
                values.put("Package_id", packages.get(i).getPackage_id());
                values.put("Product_id", packages.get(i).getProduct_id());
                values.put("Quantity", packages.get(i).getQuantity());
                values.put("Price", packages.get(i).getPrice());

                db.insert(TABLE_PACKAGES, null, values);
            }
        }
        db.close();

    }

    public PackageInfo GetPackage(int packageID, int ProductID) {
        PackageInfo packageInfo = null;
        String SelectQuery = "Select Package_id,Product_id,Quantity,Price from " + TABLE_PACKAGES + " where Package_id=" + packageID + " and Product_id=" + ProductID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SelectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                packageInfo = new PackageInfo();
                packageInfo.setPackage_id(cursor.getInt(cursor.getColumnIndex("Package_id")));
                packageInfo.setProduct_id(cursor.getInt(cursor.getColumnIndex("Product_id")));
                packageInfo.setPrice(cursor.getDouble(cursor.getColumnIndex("Price")));
                packageInfo.setQuantity(cursor.getInt(cursor.getColumnIndex("Quantity")));
            } while (cursor.moveToNext());
        }
        db.close();
        return packageInfo;
    }

    public double GetPriceReference(String productID, int duration) {
        String Price_duration = "SELLING_PRICE";
        double Price = 0;
        if (duration == 0) {
            Price_duration = "SELLING_PRICE";
        } else if (duration == 1) {
            Price_duration = "PRICE_1W";
        } else if (duration == 2) {
            Price_duration = "PRICE_2W";
        } else {
            Price_duration = "PRICE_4W";
        }
        String SelectQuery = "Select " + Price_duration + " from " + TABLE_PRODUCTS + " where PRODUCT_ID='" + productID + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SelectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Price = Double.valueOf(cursor.getString(0));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return Price;
    }

    public double GetPriceReferenceByName(String Name, int duration) {
        String Price_duration = "PRICE_1W";
        double Price = 0;
        if (duration == 1) {
            Price_duration = "PRICE_1W";
        } else if (duration == 2) {
            Price_duration = "PRICE_2W";
        } else {
            Price_duration = "PRICE_4W";
        }
        String SelectQuery = "Select " + Price_duration + " from " + TABLE_PRODUCTS + " where NAME='" + Name + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SelectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Price = Double.valueOf(cursor.getString(0));
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return Price;
    }

    public void SetProductsReference(List<Product> products) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, null, null);
        ContentValues values = new ContentValues();
        if (products != null) {
            for (int i = 0; i < products.size(); i++) {
                values = new ContentValues();
                values.put("PRODUCT_ID", products.get(i).getPRODUCT_ID());
                values.put("NAME", products.get(i).getNAME());
                values.put("PRICE_1W", products.get(i).getPrice1W());
                values.put("PRICE_2W", products.get(i).getPrice2W());
                values.put("PRICE_4W", products.get(i).getPrice4W());
                values.put("SELLING_PRICE", products.get(i).getSellingPrice());
                values.put("B_PRICE", products.get(i).getB_price());
                values.put("TYPE", products.get(i).getType());
                values.put("KIND", products.get(i).getKIND());
                db.insertOrThrow(TABLE_PRODUCTS, null, values);
            }
        }
        db.close();

    }

    public void SetKPI(List<KPI> kpis) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_KPI, null, null);
        ContentValues values = new ContentValues();
        if (kpis != null) {
            for (int i = 0; i < kpis.size(); i++) {
                values = new ContentValues();
                values.put("ID", kpis.get(i).getKPI_ID());
                values.put("NAME", kpis.get(i).getKPI_name());
                values.put("KPI_Type", kpis.get(i).getKPI_Type());
                db.insert(TABLE_KPI, null, values);
            }
        }
        db.close();

    }

    public List<KPI> GetKPI() {
        List<KPI> kpis = new ArrayList<>();
        String query = "select * from " + TABLE_KPI;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                KPI kpi = new KPI();
                kpi.setKPI_ID(String.valueOf(cursor.getInt(cursor.getColumnIndex("ID"))));
                kpi.setKPI_name(cursor.getString(cursor.getColumnIndex("NAME")));
                kpi.setKPI_Type(cursor.getString(cursor.getColumnIndex("KPI_Type")));
                kpis.add(kpi);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return kpis;
    }

    public void AddContract(List<Task> tasks) {
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase dbReader = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        if (tasks != null) {
            for (int i = 0; i < tasks.size(); i++) {
                values = new ContentValues();
                values.put("ASSIGN_ID", tasks.get(i).getAssignmentId());
                values.put("DRIV_ID", tasks.get(i).getDriveID());
                values.put("CARD_NO", tasks.get(i).getCardNo());
                values.put("CLIENT_NAME", tasks.get(i).getClientName());
                values.put("AREA_ID", tasks.get(i).getAreaId());
                values.put("AREA_NAME", tasks.get(i).getAreaName());
                values.put("city_name", tasks.get(i).getCity_name());
                values.put("street_name", tasks.get(i).getStreet_name());
                values.put("home_no", tasks.get(i).getHome_no());
                values.put("floor_no", tasks.get(i).getFloor_no());
                values.put("flat_no", tasks.get(i).getFlat_no());
                values.put("remarks", tasks.get(i).getRemarks());
                values.put("region_name", tasks.get(i).getRegion_name());
                values.put("DATA_COMMENT", tasks.get(i).getDataComment());
                values.put("OPERATION_COMMENT", tasks.get(i).getOperationComment());
                values.put("CHANGE_DATE", tasks.get(i).getChangeDate());
                values.put("PRIORITY", tasks.get(i).getPRIORITY());
                values.put("X", tasks.get(i).getX());
                values.put("Y", tasks.get(i).getY());
                values.put("TelNo", tasks.get(i).getTelNo());
                values.put("RecieptNo", tasks.get(i).getRecieptNo());
                values.put("Status", tasks.get(i).getStatus());
                values.put("PILOT_ID", tasks.get(i).getPilotID());
                values.put("AID", tasks.get(i).getAID());
                values.put("ASSIGN_DATE", tasks.get(i).getASSIGN_DATE());
                values.put("LAST_DOWNLOAD", tasks.get(i).getLAST_DOWNLOAD());
                values.put("ORDER_DELIVERY", tasks.get(i).getORDER_DELIVERY());
                values.put("CREAED_STATE", tasks.get(i).getCREATED_STATE());
                values.put("CHANGED_STATE", tasks.get(i).getCHANGED_STATE());
                values.put("DELIVERY_STATE", tasks.get(i).getDELIVERY_STATE());
                values.put("CUSTOMER_TYPE", tasks.get(i).getCUSTOMER_TYPE());
                values.put("AREA2_NAME", tasks.get(i).getAREA2_NAME());
                values.put("PAY_CREDIT", tasks.get(i).getPAY_CREDIT());
                values.put("Duration", tasks.get(i).getDuration());
                values.put("ADDRESS", tasks.get(i).getADDRESS());
                values.put("FU_Note",tasks.get(i).getFU_Note());
                values.put("TOTAL_PRICE_PAID",tasks.get(i).getTOTAL_PRICE_PAID());
                values.put("ACTUAL_PAID",tasks.get(i).getACTUAL_PAID());
                values.put("ALLOW_CREDIT",tasks.get(i).getALLOW_CREDIT());
                values.put("bonus",tasks.get(i).getBonus());

                values.put("FROM_TIME",tasks.get(i).getFrom_time());

                values.put("TO_TIME",tasks.get(i).getTo_time());

                values.put("ASSIGNMENT_TYPE",tasks.get(i).getASSIGNMENTS_TYPE());

                values.put("LOC_CONFIRMED",tasks.get(i).getLOC_CONFIRMED());




                String selectQuery = "SELECT * FROM " + TABLE_CONTRACTS + " where AID='" + tasks.get(i).getAID() + "'";
                Cursor cursor = dbReader.rawQuery(selectQuery, null);
                if (cursor.getCount() <= 0) {
                    //db.insertOrThrow(TABLE_CONTRACTS,null,values);
                    if (tasks.get(i).getCHANGED_STATE() != null && tasks.get(i).getCHANGED_STATE().equalsIgnoreCase("2") || tasks.get(i).getDELIVERY_STATE().equalsIgnoreCase("33")) {
                        // not save
                    } else {
                        db.insertOrThrow(TABLE_CONTRACTS, null, values);
                    }
                } else {
                    if (tasks.get(i).getCHANGED_STATE() != null && tasks.get(i).getCHANGED_STATE().equalsIgnoreCase("2") || tasks.get(i).getDELIVERY_STATE().equalsIgnoreCase("33")) {
                        db.delete(TABLE_CONTRACTS, "AID='" + tasks.get(i).getAID() + "'", null);
                       // db.delete(TABLE_PROD_CONTRACT,"AID='" + tasks.get(i).getAID() + "'",null);
                    } else {
                        db.update(TABLE_CONTRACTS, values, "AID='" + tasks.get(i).getAID() + "'", null);
                    }
                }
                cursor.close();
            }
        }
        db.close();
        dbReader.close();
        if (userInfo.getUSERGROUP_ID() == 2) {
            mPresnter.setProducts();
            mPresnter.GetCollectionPilot();

        }
    }

    public void AddProducts(List<Product> Products) {
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase dbReader = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        if (Products != null) {
            for (int i = 0; i < Products.size(); i++) {
                values = new ContentValues();
                values.put("PRODUCT_ID", Products.get(i).getPRODUCT_ID());
                values.put("AID", Products.get(i).getAID());
                values.put("QUANTITY", Products.get(i).getQUNTITY());
                values.put("QUANTITY_REPLACED", Products.get(i).getQUNTITY_REPLACED());
                values.put("UNIT_PRICE", Products.get(i).getUNIT_PRICE());
                values.put("CARD_NO", Products.get(i).getCARD_NO());
                values.put("NAME", Products.get(i).getNAME());
                values.put("BUY_PRICE", Products.get(i).getBUY_PRICE());
                values.put("ASSIGN_ID", Products.get(i).getASSIGN_ID());
                values.put("Treatment_code", Products.get(i).getTreatment_code());
                values.put("Treatment_description", Products.get(i).getTreatment_description());
                values.put("Package_id", Products.get(i).getPackage_id());
                values.put("Package_NO", Products.get(i).getPackage_NO());
                values.put("Description", Products.get(i).getDescription());
                values.put("Type", Products.get(i).getType());
                values.put("KIND", Products.get(i).getKIND());
                values.put("HANDLE", Products.get(i).getHANDLE());
                values.put("DELIVERY_TYPE", Products.get(i).getDELIVERY_TYPE());
                String selectQuery = "SELECT * FROM " + TABLE_PROD_CONTRACT + " where PRODUCT_ID='" + Products.get(i).getPRODUCT_ID() + "' and AID='" + Products.get(i).getAID() + "' " + " and Package_id='" + Products.get(i).getPackage_id() + "' and Treatment_code='" + Products.get(i).getTreatment_code() + "'";
                selectQuery = "Select * from " + TABLE_PROD_CONTRACT +
                        " where PRODUCT_ID='" + Products.get(i).getPRODUCT_ID() + "'" + " And AID ='" +
                        Products.get(i).getAID() + "'" + " and Package_id='" + Products.get(i).getPackage_id() + "'"
                        + " and Treatment_code='" + Products.get(i).getTreatment_code() + "' and DELIVERY_TYPE=" + Products.get(i).getDELIVERY_TYPE() + " and Package_NO='" + Products.get(i).getPackage_NO() + "'";
              //  Log.i("QueryInsett", selectQuery);

                Cursor cursor = dbReader.rawQuery(selectQuery, null);
                if (cursor.getCount() <= 0) {
                    db.insertOrThrow(TABLE_PROD_CONTRACT, null, values);
                } else {
                    try {
                        db.update(TABLE_PROD_CONTRACT, values, "PRODUCT_ID='" + Products.get(i).getPRODUCT_ID() +
                                "' and AID='" + Products.get(i).getAID() + "'" + " and Package_id='" +
                                Products.get(i).getPackage_id() + "'" + " and Treatment_code='" +
                                Products.get(i).getTreatment_code() + "' and DELIVERY_TYPE=" + Products.get(i).getDELIVERY_TYPE() + " and Package_NO='" + Products.get(i).getPackage_NO() + "'", null);
                    } catch (SQLiteDiskIOException e) {
                        e.printStackTrace();
                    }
                }
                cursor.close();
            }

        }
        db.close();
        dbReader.close();
        if (pbDailog != null && pbDailog.isShowing()) {
            pbDailog.hide();
        }
        if (NextActivity) {
            if (mHomeWithMenuActivity == null) {
                Intent i = new Intent(mContext, HomeWithMenuActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
            } else {
                mHomeWithMenuActivity.IntiUI();
            }
        }

    }

    public void AddPilots(List<Pilot> pilots) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Pilot, null, null);
        ContentValues values = new ContentValues();
        if (pilots != null) {
            for (int i = 0; i < pilots.size(); i++) {
                values = new ContentValues();
                values.put("PILOT_ID", pilots.get(i).getPilotID());
                values.put("FULL_NAME_AR", pilots.get(i).getNameAr());
                values.put("FULL_NAME_EN", pilots.get(i).getNameEn());
                values.put("ATTENDANCE_TIME_PILOT", pilots.get(i).getAttendanceTime());
                values.put("DRIVER_ID", pilots.get(i).getDRIVER_ID());
                values.put("ASSIGN_ID", pilots.get(i).getASSIGN_PILOT_ID());
                values.put("EVALUTION_DATE", pilots.get(i).getEVALUTION_DATE());
                values.put("AREA_NAME", pilots.get(i).getArea_Name());
                values.put("RETURN_TIME_PILOT",pilots.get(i).getRETURN_Time());
                db.insert(TABLE_Pilot, null, values);
            }
        }
        db.close();
        mPresnter.setCoverProducts();
    }

    public List<Task> GetTasksNotAssign(String DriverID) {
        List<Task> Tasks = new ArrayList<>();
        SharedPreferences Area_name = mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        String selectQuery = "SELECT * FROM " + TABLE_CONTRACTS + " where DRIV_ID='" + DriverID + "' and PILOT_ID is null and Area_Name ='" + Area_name.getString("AreaName", "") + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Task t = new Task();
                t.setAssignmentId(cursor.getString(cursor.getColumnIndex("ASSIGN_ID")));
                t.setDriveID(cursor.getString(cursor.getColumnIndex("DRIV_ID")));
                t.setCardNo(cursor.getString(cursor.getColumnIndex("CARD_NO")));
                t.setClientName(cursor.getString(cursor.getColumnIndex("CLIENT_NAME")));
                t.setAreaId(cursor.getString(cursor.getColumnIndex("AREA_ID")));
                t.setAreaName(cursor.getString(cursor.getColumnIndex("AREA_NAME")));
                t.setCity_name(cursor.getString(cursor.getColumnIndex("city_name")));
                t.setStreet_name(cursor.getString(cursor.getColumnIndex("street_name")));
                t.setHome_no(cursor.getString(cursor.getColumnIndex("home_no")));
                t.setFloor_no(cursor.getString(cursor.getColumnIndex("floor_no")));
                t.setFlat_no(cursor.getString(cursor.getColumnIndex("flat_no")));
                t.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
                t.setRegion_name(cursor.getString(cursor.getColumnIndex("region_name")));
                t.setPRIORITY(cursor.getString(cursor.getColumnIndex("PRIORITY")));
                t.setDataComment(cursor.getString(cursor.getColumnIndex("DATA_COMMENT")));
                t.setOperationComment(cursor.getString(cursor.getColumnIndex("OPERATION_COMMENT")));
                t.setChangeDate(cursor.getString(cursor.getColumnIndex("CHANGE_DATE")));
                t.setX(cursor.getDouble(cursor.getColumnIndex("X")));
                t.setY(cursor.getDouble(cursor.getColumnIndex("Y")));
                t.setTelNo(cursor.getString(cursor.getColumnIndex("TelNo")));
                t.setRecieptNo(cursor.getString(cursor.getColumnIndex("RecieptNo")));
                t.setStatus(cursor.getString(cursor.getColumnIndex("Status")));
                t.setPilotID(cursor.getString(cursor.getColumnIndex("PILOT_ID")));
                t.setAID(cursor.getString(cursor.getColumnIndex("AID")));
                t.setASSIGN_DATE(cursor.getString(cursor.getColumnIndex("ASSIGN_DATE")));
                t.setLAST_DOWNLOAD(cursor.getString(cursor.getColumnIndex("LAST_DOWNLOAD")));
                t.setORDER_DELIVERY(cursor.getString(cursor.getColumnIndex("ORDER_DELIVERY")));
                t.setCREATED_STATE(cursor.getString(cursor.getColumnIndex("CREAED_STATE")));
                t.setCHANGED_STATE(cursor.getString(cursor.getColumnIndex("CHANGED_STATE")));
                t.setDELIVERY_STATE(cursor.getString(cursor.getColumnIndex("DELIVERY_STATE")));
                t.setCUSTOMER_TYPE(cursor.getString(cursor.getColumnIndex("CUSTOMER_TYPE")));
                t.setAREA2_NAME(cursor.getString(cursor.getColumnIndex("AREA2_NAME")));
                t.setPAY_CREDIT(cursor.getString(cursor.getColumnIndex("PAY_CREDIT")));
                t.setDuration(cursor.getString(cursor.getColumnIndex("Duration")));
                t.setADDRESS(cursor.getString(cursor.getColumnIndex("ADDRESS")));
                t.setFU_Note(cursor.getString(cursor.getColumnIndex("FU_Note")));
                Tasks.add(t);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return Tasks;
    }

    public List<Product> GetCoverProduct(String[] Contracts) {
        List<Product> p = new ArrayList<>();
        if (Contracts != null && Contracts.length > 0) {
            String incluse = "(";
            for (int i = 0; i < Contracts.length; i++) {
                incluse += "'" + Contracts[i] + "'";
                if (i != Contracts.length - 1)
                    incluse += ',';
            }
            incluse += ")";
            String selectQuery = "SELECT UNIT_PRICE,AID,PRODUCT_ID,NAME,SUM(QUANTITY)as TOTAL_AMOUNT,ASSIGN_ID,Treatment_description,Treatment_code,min(DELIVERY_TYPE) as DELIVERY_TYPE FROM " + TABLE_PROD_CONTRACT + " WHERE AID in " + incluse + " group by PRODUCT_ID, NAME,Treatment_description";
      /*  String selectQuery = "SELECT  * FROM " + TABLE_PROD_CONTRACT + " WHERE "
                + KEY_ID + "= " + ContractID;*/

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            p = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    Product product = new Product();
                    product.setPRODUCT_ID(cursor.getString(cursor.getColumnIndex("PRODUCT_ID")));
                    product.setAID(String.valueOf(cursor.getString(cursor.getColumnIndex("AID"))));
                    product.setQUNTITY(String.valueOf(cursor.getInt(cursor.getColumnIndex("TOTAL_AMOUNT"))));
                    product.setNAME(cursor.getString(cursor.getColumnIndex("NAME")));
                    product.setUNIT_PRICE(String.valueOf(cursor.getDouble(cursor.getColumnIndex("UNIT_PRICE"))));
                   /* product.setDESCRIPTION(cursor.getString(cursor.getColumnIndex("DESCRIPTION")));
                    product.setTYPE(cursor.getString(cursor.getColumnIndex("TYPE")));*/
                    product.setASSIGN_ID(String.valueOf(cursor.getString(cursor.getColumnIndex("ASSIGN_ID"))));
                    product.setTreatment_description(cursor.getString((cursor.getColumnIndex("Treatment_description"))));
                    product.setTreatment_code(cursor.getString(cursor.getColumnIndex("Treatment_code")));
                    product.setDELIVERY_TYPE(cursor.getInt(cursor.getColumnIndex("DELIVERY_TYPE")));
                    p.add(product);
                } while (cursor.moveToNext());
                cursor.close();
            }
            db.close();

        }
        return p;
    }

    public List<Product> GetCoverProductWithoutCanceled(String[] Contracts) {
        List<Product> p = new ArrayList<>();
        if (Contracts != null && Contracts.length > 0) {
            String incluse = "(";
            for (int i = 0; i < Contracts.length; i++) {
                incluse += "'" + Contracts[i] + "'";
                if (i != Contracts.length - 1)
                    incluse += ',';
            }
            incluse += ")";
            String selectQuery = "SELECT UNIT_PRICE,AID,PRODUCT_ID,NAME,SUM(QUANTITY)as TOTAL_AMOUNT,ASSIGN_ID," +
                    "Treatment_description,Treatment_code,min(DELIVERY_TYPE) as DELIVERY_TYPE FROM " + TABLE_PROD_CONTRACT +
                    " WHERE AID in " + incluse + " and DELIVERY_TYPE !=6 group by PRODUCT_ID, NAME," +
                    "Treatment_description";
      /*  String selectQuery = "SELECT  * FROM " + TABLE_PROD_CONTRACT + " WHERE "
                + KEY_ID + "= " + ContractID;*/

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            p = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    Product product = new Product();
                    product.setPRODUCT_ID(cursor.getString(cursor.getColumnIndex("PRODUCT_ID")));
                    product.setAID(String.valueOf(cursor.getString(cursor.getColumnIndex("AID"))));
                    product.setQUNTITY(String.valueOf(cursor.getInt(cursor.getColumnIndex("TOTAL_AMOUNT"))));
                    product.setNAME(cursor.getString(cursor.getColumnIndex("NAME")));
                    product.setUNIT_PRICE(String.valueOf(cursor.getDouble(cursor.getColumnIndex("UNIT_PRICE"))));
                   /* product.setDESCRIPTION(cursor.getString(cursor.getColumnIndex("DESCRIPTION")));
                    product.setTYPE(cursor.getString(cursor.getColumnIndex("TYPE")));*/
                    product.setASSIGN_ID(String.valueOf(cursor.getString(cursor.getColumnIndex("ASSIGN_ID"))));
                    product.setTreatment_description(cursor.getString((cursor.getColumnIndex("Treatment_description"))));
                    product.setTreatment_code(cursor.getString(cursor.getColumnIndex("Treatment_code")));
                    product.setDELIVERY_TYPE(cursor.getInt(cursor.getColumnIndex("DELIVERY_TYPE")));
                    p.add(product);
                } while (cursor.moveToNext());
                cursor.close();
            }
            db.close();

        }
        return p;
    }

    public List<Pilot> GetCoverPilots() {
        SharedPreferences Area_name = mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        List<Pilot> pilots = new ArrayList<>();
        String SelectQuery = "Select * from " + TABLE_Pilot + " where AREA_NAME ='" + Area_name.getString("AreaName", "") + "' AND DRIVER_ID=" + Area_name.getString("UserName", "");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SelectQuery, null);
        if (cursor.moveToFirst()) {
            pilots = new ArrayList<>();
            do {
                Pilot p = new Pilot();
                p.setPilotID(cursor.getString(cursor.getColumnIndex("PILOT_ID")));
                p.setAttendanceTime(cursor.getString(cursor.getColumnIndex("ATTENDANCE_TIME_PILOT")));
                p.setNameAr(cursor.getString(cursor.getColumnIndex("FULL_NAME_AR")));
                p.setNameEn(cursor.getString(cursor.getColumnIndex("FULL_NAME_EN")));
                p.setASSIGN_PILOT_ID(cursor.getString(cursor.getColumnIndex("ASSIGN_ID")));
                p.setRETURN_Time(cursor.getString(cursor.getColumnIndex("RETURN_TIME_PILOT")));
                p.setEVALUTION_DATE(cursor.getString(cursor.getColumnIndex("EVALUTION_DATE")));
                p.setArea_Name(cursor.getString(cursor.getColumnIndex("AREA_NAME")));
                pilots.add(p);
            } while (cursor.moveToNext());
            cursor.close();

        }
        db.close();
        return pilots;
    }

    public List<Pilot> GetAllCoverPilots() {
        SharedPreferences Area_name = mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        List<Pilot> pilots = new ArrayList<>();
        String SelectQuery = "Select * from " + TABLE_Pilot + " where DRIVER_ID=" + Area_name.getString("UserName", "")+" AND AREA_NAME='"+Area_name.getString("AreaName", "") +"'  group by PILOT_ID";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SelectQuery, null);
        if (cursor.moveToFirst()) {
            pilots = new ArrayList<>();
            do {
                Pilot p = new Pilot();
                p.setPilotID(cursor.getString(cursor.getColumnIndex("PILOT_ID")));
                p.setAttendanceTime(cursor.getString(cursor.getColumnIndex("ATTENDANCE_TIME_PILOT")));
                p.setNameAr(cursor.getString(cursor.getColumnIndex("FULL_NAME_AR")));
                p.setNameEn(cursor.getString(cursor.getColumnIndex("FULL_NAME_EN")));
                p.setASSIGN_PILOT_ID(cursor.getString(cursor.getColumnIndex("ASSIGN_ID")));
                p.setRETURN_Time(cursor.getString(cursor.getColumnIndex("RETURN_TIME_PILOT")));
                p.setEVALUTION_DATE(cursor.getString(cursor.getColumnIndex("EVALUTION_DATE")));
                p.setArea_Name(cursor.getString(cursor.getColumnIndex("AREA_NAME")));
                pilots.add(p);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return pilots;
    }

    public List<Task> GetTasksPilot(String PilotID) {
        String selectQuery="";
        userInfo= ServerManager.deSerializeStringToObject(mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserInfo",""),UserInfo.class);

        if(PilotID.equalsIgnoreCase(mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", ""))&&userInfo.getUSERGROUP_ID()==2)
        {
            selectQuery = "SELECT * FROM " + TABLE_CONTRACTS + " where PILOT_ID='" + PilotID + "'" +" AND AREA_NAME='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"' ORDER BY  home_no asc,CARD_NO";
        }
        else
        {
            selectQuery = "SELECT * FROM " + TABLE_CONTRACTS + " where PILOT_ID='" + PilotID + "' and DRIV_ID='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", "")+"'"+" AND AREA_NAME='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"'"+" ORDER BY home_no asc,CARD_NO";
        // Log.i("quer",selectQuery);

        }
        List<Task> Tasks = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Task t = new Task();
                t.setAssignmentId(cursor.getString(cursor.getColumnIndex("ASSIGN_ID")));
                t.setDriveID(cursor.getString(cursor.getColumnIndex("DRIV_ID")));
                t.setCardNo(cursor.getString(cursor.getColumnIndex("CARD_NO")));
                t.setClientName(cursor.getString(cursor.getColumnIndex("CLIENT_NAME")));
                t.setAreaId(cursor.getString(cursor.getColumnIndex("AREA_ID")));
                t.setAreaName(cursor.getString(cursor.getColumnIndex("AREA_NAME")));
                t.setCity_name(cursor.getString(cursor.getColumnIndex("city_name")));
                t.setStreet_name(cursor.getString(cursor.getColumnIndex("street_name")));
                t.setHome_no(cursor.getString(cursor.getColumnIndex("home_no")));
                t.setFloor_no(cursor.getString(cursor.getColumnIndex("floor_no")));
                t.setFlat_no(cursor.getString(cursor.getColumnIndex("flat_no")));
                t.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
                t.setRegion_name(cursor.getString(cursor.getColumnIndex("region_name")));
                t.setPRIORITY(cursor.getString(cursor.getColumnIndex("PRIORITY")));
                t.setDataComment(cursor.getString(cursor.getColumnIndex("DATA_COMMENT")));
                t.setOperationComment(cursor.getString(cursor.getColumnIndex("OPERATION_COMMENT")));
                t.setChangeDate(cursor.getString(cursor.getColumnIndex("CHANGE_DATE")));
                t.setX(cursor.getDouble(cursor.getColumnIndex("X")));
                t.setY(cursor.getDouble(cursor.getColumnIndex("Y")));
                t.setTelNo(cursor.getString(cursor.getColumnIndex("TelNo")));
                t.setRecieptNo(cursor.getString(cursor.getColumnIndex("RecieptNo")));
                t.setStatus(cursor.getString(cursor.getColumnIndex("Status")));
                t.setPilotID(cursor.getString(cursor.getColumnIndex("PILOT_ID")));
                t.setAID(cursor.getString(cursor.getColumnIndex("AID")));
                t.setASSIGN_DATE(cursor.getString(cursor.getColumnIndex("ASSIGN_DATE")));
                t.setLAST_DOWNLOAD(cursor.getString(cursor.getColumnIndex("LAST_DOWNLOAD")));
                t.setORDER_DELIVERY(cursor.getString(cursor.getColumnIndex("ORDER_DELIVERY")));
                t.setCREATED_STATE(cursor.getString(cursor.getColumnIndex("CREAED_STATE")));
                t.setCHANGED_STATE(cursor.getString(cursor.getColumnIndex("CHANGED_STATE")));
                t.setDELIVERY_STATE(cursor.getString(cursor.getColumnIndex("DELIVERY_STATE")));
                t.setCUSTOMER_TYPE(cursor.getString(cursor.getColumnIndex("CUSTOMER_TYPE")));
                t.setAREA2_NAME(cursor.getString(cursor.getColumnIndex("AREA2_NAME")));
                t.setPAY_CREDIT(cursor.getString(cursor.getColumnIndex("PAY_CREDIT")));
                t.setDuration(cursor.getString(cursor.getColumnIndex("Duration")));
                t.setADDRESS(cursor.getString(cursor.getColumnIndex("ADDRESS")));
                t.setFU_Note(cursor.getString(cursor.getColumnIndex("FU_Note")));
                t.setTOTAL_PRICE_PAID(cursor.getDouble(cursor.getColumnIndex("TOTAL_PRICE_PAID")));
                t.setACTUAL_PAID(cursor.getDouble(cursor.getColumnIndex("ACTUAL_PAID")));
                t.setALLOW_CREDIT(cursor.getInt(cursor.getColumnIndex("ALLOW_CREDIT")));
                t.setBonus(cursor.getDouble(cursor.getColumnIndex("bonus")));
                t.setFrom_time(cursor.getString(cursor.getColumnIndex("FROM_TIME")));
                t.setTo_time(cursor.getString(cursor.getColumnIndex("TO_TIME")));
                t.setASSIGNMENTS_TYPE(cursor.getInt(cursor.getColumnIndex("ASSIGNMENT_TYPE")));

                        t.setLOC_CONFIRMED(cursor.getInt(cursor.getColumnIndex("LOC_CONFIRMED")));
                if (t.getStatus() == null || t.getStatus().equalsIgnoreCase("Not Delivered"))
                    Tasks.add(t);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return Tasks;
    }

    public List<Task> GetALLTasksPilot(String PilotID) {
        SharedPreferences Area_name = mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        List<Task> Tasks = new ArrayList<>();
        String selectQuery="";
        userInfo= ServerManager.deSerializeStringToObject(mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserInfo",""),UserInfo.class);

        if(PilotID.equalsIgnoreCase(Area_name.getString("UserName", ""))&&userInfo.getUSERGROUP_ID()==2)
         selectQuery = "SELECT * FROM " + TABLE_CONTRACTS + " where PILOT_ID='" + PilotID + "'"+" AND AREA_NAME='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"'"+" ORDER BY PRIORITY desc";
        else
            selectQuery = "SELECT * FROM " + TABLE_CONTRACTS + " where PILOT_ID='" + PilotID + "' and DRIV_ID='"+Area_name.getString("UserName", "")+"'"+" AND AREA_NAME='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"'"+" ORDER BY PRIORITY desc";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Task t = new Task();
                t.setAssignmentId(cursor.getString(cursor.getColumnIndex("ASSIGN_ID")));
                t.setDriveID(cursor.getString(cursor.getColumnIndex("DRIV_ID")));
                t.setCardNo(cursor.getString(cursor.getColumnIndex("CARD_NO")));
                t.setClientName(cursor.getString(cursor.getColumnIndex("CLIENT_NAME")));
                t.setAreaId(cursor.getString(cursor.getColumnIndex("AREA_ID")));
                t.setAreaName(cursor.getString(cursor.getColumnIndex("AREA_NAME")));
                t.setCity_name(cursor.getString(cursor.getColumnIndex("city_name")));
                t.setStreet_name(cursor.getString(cursor.getColumnIndex("street_name")));
                t.setHome_no(cursor.getString(cursor.getColumnIndex("home_no")));
                t.setFloor_no(cursor.getString(cursor.getColumnIndex("floor_no")));
                t.setFlat_no(cursor.getString(cursor.getColumnIndex("flat_no")));
                t.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
                t.setRegion_name(cursor.getString(cursor.getColumnIndex("region_name")));
                t.setPRIORITY(cursor.getString(cursor.getColumnIndex("PRIORITY")));
                t.setDataComment(cursor.getString(cursor.getColumnIndex("DATA_COMMENT")));
                t.setOperationComment(cursor.getString(cursor.getColumnIndex("OPERATION_COMMENT")));
                t.setChangeDate(cursor.getString(cursor.getColumnIndex("CHANGE_DATE")));
                t.setX(cursor.getDouble(cursor.getColumnIndex("X")));
                t.setY(cursor.getDouble(cursor.getColumnIndex("Y")));
                t.setTelNo(cursor.getString(cursor.getColumnIndex("TelNo")));
                t.setRecieptNo(cursor.getString(cursor.getColumnIndex("RecieptNo")));
                t.setStatus(cursor.getString(cursor.getColumnIndex("Status")));
                t.setPilotID(cursor.getString(cursor.getColumnIndex("PILOT_ID")));
                t.setAID(cursor.getString(cursor.getColumnIndex("AID")));
                t.setASSIGN_DATE(cursor.getString(cursor.getColumnIndex("ASSIGN_DATE")));
                t.setLAST_DOWNLOAD(cursor.getString(cursor.getColumnIndex("LAST_DOWNLOAD")));
                t.setORDER_DELIVERY(cursor.getString(cursor.getColumnIndex("ORDER_DELIVERY")));
                t.setCREATED_STATE(cursor.getString(cursor.getColumnIndex("CREAED_STATE")));
                t.setCHANGED_STATE(cursor.getString(cursor.getColumnIndex("CHANGED_STATE")));
                t.setDELIVERY_STATE(cursor.getString(cursor.getColumnIndex("DELIVERY_STATE")));
                t.setCUSTOMER_TYPE(cursor.getString(cursor.getColumnIndex("CUSTOMER_TYPE")));
                t.setAREA2_NAME(cursor.getString(cursor.getColumnIndex("AREA2_NAME")));
                t.setPAY_CREDIT(cursor.getString(cursor.getColumnIndex("PAY_CREDIT")));
                t.setDuration(cursor.getString(cursor.getColumnIndex("Duration")));
                t.setADDRESS(cursor.getString(cursor.getColumnIndex("ADDRESS")));
                t.setFU_Note(cursor.getString(cursor.getColumnIndex("FU_Note")));
                t.setTOTAL_PRICE_PAID(cursor.getDouble(cursor.getColumnIndex("TOTAL_PRICE_PAID")));
                t.setACTUAL_PAID(cursor.getDouble(cursor.getColumnIndex("ACTUAL_PAID")));
                t.setALLOW_CREDIT(cursor.getInt(cursor.getColumnIndex("ALLOW_CREDIT")));
                t.setBonus(cursor.getDouble(cursor.getColumnIndex("bonus")));
                t.setFrom_time(cursor.getString(cursor.getColumnIndex("FROM_TIME")));
                t.setTo_time(cursor.getString(cursor.getColumnIndex("TO_TIME")));
                t.setASSIGNMENTS_TYPE(cursor.getInt(cursor.getColumnIndex("ASSIGNMENT_TYPE")));
                t.setLOC_CONFIRMED(cursor.getInt(cursor.getColumnIndex("LOC_CONFIRMED")));

                Tasks.add(t);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return Tasks;
    }
    public List<Task> GetPendingTasksPilot(String PilotID) {
        SharedPreferences Area_name = mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        List<Task> Tasks = new ArrayList<>();
        String selectQuery="";
        userInfo= ServerManager.deSerializeStringToObject(mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserInfo",""),UserInfo.class);

        if(PilotID.equalsIgnoreCase(Area_name.getString("UserName", ""))&&userInfo.getUSERGROUP_ID()==2)
            selectQuery = "SELECT * FROM " + TABLE_CONTRACTS + " where PILOT_ID='" + PilotID + "'" +" AND AREA_NAME='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"'"+" ORDER BY  home_no asc,CARD_NO";
        else
            selectQuery = "SELECT * FROM " + TABLE_CONTRACTS + " where PILOT_ID='" + PilotID + "' and DRIV_ID='"+Area_name.getString("UserName", "")+"'"+" AND AREA_NAME='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"'" +" ORDER BY home_no asc,CARD_NO";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Task t = new Task();
                t.setAssignmentId(cursor.getString(cursor.getColumnIndex("ASSIGN_ID")));
                t.setDriveID(cursor.getString(cursor.getColumnIndex("DRIV_ID")));
                t.setCardNo(cursor.getString(cursor.getColumnIndex("CARD_NO")));
                t.setClientName(cursor.getString(cursor.getColumnIndex("CLIENT_NAME")));
                t.setAreaId(cursor.getString(cursor.getColumnIndex("AREA_ID")));
                t.setAreaName(cursor.getString(cursor.getColumnIndex("AREA_NAME")));
                t.setCity_name(cursor.getString(cursor.getColumnIndex("city_name")));
                t.setStreet_name(cursor.getString(cursor.getColumnIndex("street_name")));
                t.setHome_no(cursor.getString(cursor.getColumnIndex("home_no")));
                t.setFloor_no(cursor.getString(cursor.getColumnIndex("floor_no")));
                t.setFlat_no(cursor.getString(cursor.getColumnIndex("flat_no")));
                t.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
                t.setRegion_name(cursor.getString(cursor.getColumnIndex("region_name")));
                t.setPRIORITY(cursor.getString(cursor.getColumnIndex("PRIORITY")));
                t.setDataComment(cursor.getString(cursor.getColumnIndex("DATA_COMMENT")));
                t.setOperationComment(cursor.getString(cursor.getColumnIndex("OPERATION_COMMENT")));
                t.setChangeDate(cursor.getString(cursor.getColumnIndex("CHANGE_DATE")));
                t.setX(cursor.getDouble(cursor.getColumnIndex("X")));
                t.setY(cursor.getDouble(cursor.getColumnIndex("Y")));
                t.setTelNo(cursor.getString(cursor.getColumnIndex("TelNo")));
                t.setRecieptNo(cursor.getString(cursor.getColumnIndex("RecieptNo")));
                t.setStatus(cursor.getString(cursor.getColumnIndex("Status")));
                t.setPilotID(cursor.getString(cursor.getColumnIndex("PILOT_ID")));
                t.setAID(cursor.getString(cursor.getColumnIndex("AID")));
                t.setASSIGN_DATE(cursor.getString(cursor.getColumnIndex("ASSIGN_DATE")));
                t.setLAST_DOWNLOAD(cursor.getString(cursor.getColumnIndex("LAST_DOWNLOAD")));
                t.setORDER_DELIVERY(cursor.getString(cursor.getColumnIndex("ORDER_DELIVERY")));
                t.setCREATED_STATE(cursor.getString(cursor.getColumnIndex("CREAED_STATE")));
                t.setCHANGED_STATE(cursor.getString(cursor.getColumnIndex("CHANGED_STATE")));
                t.setDELIVERY_STATE(cursor.getString(cursor.getColumnIndex("DELIVERY_STATE")));
                t.setCUSTOMER_TYPE(cursor.getString(cursor.getColumnIndex("CUSTOMER_TYPE")));
                t.setAREA2_NAME(cursor.getString(cursor.getColumnIndex("AREA2_NAME")));
                t.setPAY_CREDIT(cursor.getString(cursor.getColumnIndex("PAY_CREDIT")));
                t.setDuration(cursor.getString(cursor.getColumnIndex("Duration")));
                t.setADDRESS(cursor.getString(cursor.getColumnIndex("ADDRESS")));
                t.setFU_Note(cursor.getString(cursor.getColumnIndex("FU_Note")));
                t.setTOTAL_PRICE_PAID(cursor.getDouble(cursor.getColumnIndex("TOTAL_PRICE_PAID")));
                t.setACTUAL_PAID(cursor.getDouble(cursor.getColumnIndex("ACTUAL_PAID")));
                t.setALLOW_CREDIT(cursor.getInt(cursor.getColumnIndex("ALLOW_CREDIT")));
                t.setBonus(cursor.getDouble(cursor.getColumnIndex("bonus")));
                t.setFrom_time(cursor.getString(cursor.getColumnIndex("FROM_TIME")));
                t.setTo_time(cursor.getString(cursor.getColumnIndex("TO_TIME")));
                t.setASSIGNMENTS_TYPE(cursor.getInt(cursor.getColumnIndex("ASSIGNMENT_TYPE")));
                t.setLOC_CONFIRMED(cursor.getInt(cursor.getColumnIndex("LOC_CONFIRMED")));

                if(t.getStatus()==null||t.getStatus().isEmpty()||t.getStatus().equalsIgnoreCase("Not Delivered"))
                Tasks.add(t);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return Tasks;
    }

    public List<Task> GetTasks() {
        List<Task> Tasks = new ArrayList<>();
        SharedPreferences Area_name = mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        String SelectQuery_p = "Select PILOT_ID from " + TABLE_Pilot + " where DRIVER_ID=" + Area_name.getString("UserName", "")+" AND AREA_NAME='"+Area_name.getString("AreaName", "") +"'" +" group by PILOT_ID";

        String selectQuery = "SELECT * FROM " + TABLE_CONTRACTS+" where DRIV_ID='"+Area_name.getString("UserName", "")+"' AND AREA_NAME='"+Area_name.getString("AreaName", "")+"' and PILOT_ID in ("+SelectQuery_p+")"+" ORDER BY PRIORITY desc";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Task t = new Task();
                t.setAssignmentId(cursor.getString(cursor.getColumnIndex("ASSIGN_ID")));
                t.setDriveID(cursor.getString(cursor.getColumnIndex("DRIV_ID")));
                t.setCardNo(cursor.getString(cursor.getColumnIndex("CARD_NO")));
                t.setClientName(cursor.getString(cursor.getColumnIndex("CLIENT_NAME")));
                t.setAreaId(cursor.getString(cursor.getColumnIndex("AREA_ID")));
                t.setAreaName(cursor.getString(cursor.getColumnIndex("AREA_NAME")));
                t.setCity_name(cursor.getString(cursor.getColumnIndex("city_name")));
                t.setStreet_name(cursor.getString(cursor.getColumnIndex("street_name")));
                t.setHome_no(cursor.getString(cursor.getColumnIndex("home_no")));
                t.setFloor_no(cursor.getString(cursor.getColumnIndex("floor_no")));
                t.setFlat_no(cursor.getString(cursor.getColumnIndex("flat_no")));
                t.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
                t.setRegion_name(cursor.getString(cursor.getColumnIndex("region_name")));
                t.setPRIORITY(cursor.getString(cursor.getColumnIndex("PRIORITY")));
                t.setDataComment(cursor.getString(cursor.getColumnIndex("DATA_COMMENT")));
                t.setOperationComment(cursor.getString(cursor.getColumnIndex("OPERATION_COMMENT")));
                t.setChangeDate(cursor.getString(cursor.getColumnIndex("CHANGE_DATE")));
                t.setX(cursor.getDouble(cursor.getColumnIndex("X")));
                t.setY(cursor.getDouble(cursor.getColumnIndex("Y")));
                t.setTelNo(cursor.getString(cursor.getColumnIndex("TelNo")));
                t.setRecieptNo(cursor.getString(cursor.getColumnIndex("RecieptNo")));
                t.setStatus(cursor.getString(cursor.getColumnIndex("Status")));
                t.setPilotID(cursor.getString(cursor.getColumnIndex("PILOT_ID")));
                t.setAID(cursor.getString(cursor.getColumnIndex("AID")));
                t.setASSIGN_DATE(cursor.getString(cursor.getColumnIndex("ASSIGN_DATE")));
                t.setLAST_DOWNLOAD(cursor.getString(cursor.getColumnIndex("LAST_DOWNLOAD")));
                t.setORDER_DELIVERY(cursor.getString(cursor.getColumnIndex("ORDER_DELIVERY")));
                t.setCREATED_STATE(cursor.getString(cursor.getColumnIndex("CREAED_STATE")));
                t.setCHANGED_STATE(cursor.getString(cursor.getColumnIndex("CHANGED_STATE")));
                t.setDELIVERY_STATE(cursor.getString(cursor.getColumnIndex("DELIVERY_STATE")));
                t.setCUSTOMER_TYPE(cursor.getString(cursor.getColumnIndex("CUSTOMER_TYPE")));
                t.setAREA2_NAME(cursor.getString(cursor.getColumnIndex("AREA2_NAME")));
                t.setPAY_CREDIT(cursor.getString(cursor.getColumnIndex("PAY_CREDIT")));
                t.setDuration(cursor.getString(cursor.getColumnIndex("Duration")));
                t.setADDRESS(cursor.getString(cursor.getColumnIndex("ADDRESS")));
                t.setFU_Note(cursor.getString(cursor.getColumnIndex("FU_Note")));
                t.setTOTAL_PRICE_PAID(cursor.getDouble(cursor.getColumnIndex("TOTAL_PRICE_PAID")));
                t.setACTUAL_PAID(cursor.getDouble(cursor.getColumnIndex("ACTUAL_PAID")));
                t.setALLOW_CREDIT(cursor.getInt(cursor.getColumnIndex("ALLOW_CREDIT")));
                t.setBonus(cursor.getDouble(cursor.getColumnIndex("bonus")));
                t.setFrom_time(cursor.getString(cursor.getColumnIndex("FROM_TIME")));
                t.setTo_time(cursor.getString(cursor.getColumnIndex("TO_TIME")));
                t.setASSIGNMENTS_TYPE(cursor.getInt(cursor.getColumnIndex("ASSIGNMENT_TYPE")));
                t.setLOC_CONFIRMED(cursor.getInt(cursor.getColumnIndex("LOC_CONFIRMED")));

                Tasks.add(t);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return Tasks;
    }

    public void UpdateStatus(String AssID, String Status,double Actual_paid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Status", Status);
        values.put("ACTUAL_PAID", Actual_paid);
        db.update(TABLE_CONTRACTS, values, "AID =" + "'" + AssID + "'", null);
        db.close();
    }

    public void AssignTask(String PilotId, List<String> AssID) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < AssID.size(); i++) {
            ContentValues values = new ContentValues();
            values.put("PILOT_ID", PilotId);
            db.update(TABLE_CONTRACTS, values, "AID =" + "'" + AssID.get(i) + "'", null);
        }
        db.close();
    }

    public void setEVALUTION_DATE(String pilotID) {
        //EVALUTION_DATE
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Calendar c = Calendar.getInstance();
        String CurrentTime = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE) + " " + c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
        values.put("EVALUTION_DATE", CurrentTime);
        db.update(TABLE_Pilot, values, "PILOT_ID =" + "'" + pilotID + "'", null);
        db.close();

    }

    public void setAttendance(String PilotID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Calendar c = Calendar.getInstance();
        String CurrentTime = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE) + " " + c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
        values.put("ATTENDANCE_TIME_PILOT", CurrentTime);
        db.update(TABLE_Pilot, values, "PILOT_ID =" + "'" + PilotID + "'", null);
        db.close();
    }

    public void setReturnTime(String PilotID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Calendar c = Calendar.getInstance();
        String CurrentTime = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE) + " " + c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
        values.put("RETURN_TIME_PILOT", CurrentTime);
        db.update(TABLE_Pilot, values, "PILOT_ID =" + "'" + PilotID + "'", null);
        db.close();
    }

    public List<Product> GetProduct(String ContractID) {
        List<Product> p = new ArrayList<>();
        String KEY_ID = "CONTRACT_ID";
        String selectQuery = "SELECT  * FROM " + TABLE_PROD_CONTRACT + " WHERE AID=" + "'" + ContractID + "' order by Package_NO ";
      /*  String selectQuery = "SELECT  * FROM " + TABLE_PROD_CONTRACT + " WHERE "
                + KEY_ID + "= " + ContractID;*/

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        p = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setPRODUCT_ID(cursor.getString(cursor.getColumnIndex("PRODUCT_ID")));
                product.setAID(String.valueOf(cursor.getString(cursor.getColumnIndex("AID"))));
                product.setQUNTITY(String.valueOf(cursor.getInt(cursor.getColumnIndex("QUANTITY"))));
                product.setNAME(cursor.getString(cursor.getColumnIndex("NAME")));
                product.setUNIT_PRICE(String.valueOf(cursor.getDouble(cursor.getColumnIndex("UNIT_PRICE"))));
                product.setQUNTITY_REPLACED(String.valueOf(cursor.getInt(cursor.getColumnIndex("QUANTITY_REPLACED"))));
                product.setBUY_PRICE(cursor.getString(cursor.getColumnIndex("BUY_PRICE")));
                product.setASSIGN_ID(cursor.getString(cursor.getColumnIndex("ASSIGN_ID")));
                product.setTreatment_code(cursor.getString(cursor.getColumnIndex("Treatment_code")));
                product.setTreatment_description(cursor.getString(cursor.getColumnIndex("Treatment_description")));
                product.setPackage_id(cursor.getString(cursor.getColumnIndex("Package_id")));
                product.setPackage_NO(cursor.getString(cursor.getColumnIndex("Package_NO")));
                product.setDescription(cursor.getString(cursor.getColumnIndex("Description")));
                product.setType(cursor.getString(cursor.getColumnIndex("Type")));
                product.setKIND(cursor.getString(cursor.getColumnIndex("KIND")));
                product.setHANDLE(cursor.getInt(cursor.getColumnIndex("HANDLE")));
                product.setDELIVERY_TYPE(cursor.getInt(cursor.getColumnIndex("DELIVERY_TYPE")));
                p.add(product);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return p;
    }

    public double GetTotalPilot(String PilotID) {
        double totalAmount = 0;
        String INQuery="";
        userInfo= ServerManager.deSerializeStringToObject(mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserInfo",""),UserInfo.class);

        if(PilotID.equalsIgnoreCase(mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", ""))&&userInfo.getUSERGROUP_ID()==2)
         INQuery = "SELECT AID FROM " + TABLE_CONTRACTS + " where PILOT_ID='" + PilotID + "' and Status is not null "+" AND AREA_NAME='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"'"+" ORDER BY home_no asc";
        else
            INQuery = "SELECT AID FROM " + TABLE_CONTRACTS + " where PILOT_ID='" + PilotID + "' and Status is not null and DRIV_ID='"+ mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", "")+"'"+" AND AREA_NAME='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"'"+" ORDER BY home_no asc";

        String selectQuery = "SELECT sum(ACTUAL_PAID) as ACTUAL_PAID FROM " + TABLE_CONTRACTS + " WHERE AID in(" + INQuery + ")";


        //String selectQuery = "SELECT QUANTITY_REPLACED,BUY_PRICE,Type,Package_NO,QUANTITY,UNIT_PRICE FROM " + TABLE_PROD_CONTRACT + " WHERE AID in(" + INQuery + ")";
      /*  String selectQuery = "SELECT  * FROM " + TABLE_PROD_CONTRACT + " WHERE "
                + KEY_ID + "=
                " + ContractID;*/

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                totalAmount=cursor.getDouble(cursor.getColumnIndex("ACTUAL_PAID"));
               /* int QR = cursor.getInt(cursor.getColumnIndex("QUANTITY_REPLACED"));
                int Q=cursor.getInt(cursor.getColumnIndex("QUANTITY"));
                int PNO=cursor.getInt(cursor.getColumnIndex("Package_NO"));
                double price = cursor.getDouble(cursor.getColumnIndex("BUY_PRICE"));
                double unitPrice=cursor.getDouble(cursor.getColumnIndex("UNIT_PRICE"));
                if(PNO!=0)
                {
                    // package
                    if(Q==QR)
                    {
                        if(price>=0)
                        totalAmount += Math.abs(price);
                    }
                    else if(Math.abs(unitPrice)==Math.abs(price))
                    {
                        if(price>=0)
                        totalAmount += Math.abs(price);
                    }
                    else
                    {
                        if(QR==0)
                            QR=1;
                        if (price < 0)
                            price = 0;
                        totalAmount += (Math.abs(QR) * price);
                    }
                }
                else {

                    if (cursor.getString(cursor.getColumnIndex("Type")) != null && cursor.getString(cursor.getColumnIndex("Type")).equalsIgnoreCase("4")) {
                        totalAmount += Math.abs(price);
                    } else {

                        if (price < 0)
                            price = 0;
                        if(QR==0)
                            QR=1;
                        totalAmount += (Math.abs(QR) * price);

                    }
                }
*/
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return totalAmount;
    }

    public List<Product> GetProductsPilot(String PilotID) {

        userInfo= ServerManager.deSerializeStringToObject(mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserInfo",""),UserInfo.class);

        List<Product> p = new ArrayList<>();
        String INQuery="";
        if(PilotID.equalsIgnoreCase(mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", ""))&&userInfo.getUSERGROUP_ID()==2)
         INQuery = "SELECT AID FROM " + TABLE_CONTRACTS + " where PILOT_ID='" + PilotID + "'"+" ORDER BY PRIORITY desc";
        else
            INQuery = "SELECT AID FROM " + TABLE_CONTRACTS + " where PILOT_ID='" + PilotID + "' and DRIV_ID='"+ mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", "")+"'"+" AND AREA_NAME='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"'"+" ORDER BY PRIORITY desc";

        String selectQuery = "SELECT PRODUCT_ID,NAME,SUM(QUANTITY) AS QUANTITY,SUM(QUANTITY_REPLACED) AS QUANTITY_REPLACED,MIN(UNIT_PRICE) AS UNIT_PRICE ,MIN(ASSIGN_ID) AS ASSIGN_ID,Treatment_description ,min(DELIVERY_TYPE) as DELIVERY_TYPE FROM " + TABLE_PROD_CONTRACT + " WHERE AID in(" + INQuery + ")" + "GROUP BY PRODUCT_ID,Treatment_code,NAME";
      /*  String selectQuery = "SELECT  * FROM " + TABLE_PROD_CONTRACT + " WHERE "
                + KEY_ID + "= " + ContractID;*/

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        p = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setPRODUCT_ID(cursor.getString(cursor.getColumnIndex("PRODUCT_ID")));
                //product.setAID(String.valueOf(cursor.getString(cursor.getColumnIndex("AID"))));
                product.setQUNTITY(String.valueOf(cursor.getInt(cursor.getColumnIndex("QUANTITY"))));
                product.setQUNTITY_REPLACED(String.valueOf(cursor.getInt(cursor.getColumnIndex("QUANTITY_REPLACED"))));
                product.setNAME(cursor.getString(cursor.getColumnIndex("NAME")));
                product.setUNIT_PRICE(String.valueOf(cursor.getDouble(cursor.getColumnIndex("UNIT_PRICE"))));
                product.setASSIGN_ID(String.valueOf(cursor.getString(cursor.getColumnIndex("ASSIGN_ID"))));
                product.setTreatment_description(cursor.getString(cursor.getColumnIndex("Treatment_description")));
                product.setDELIVERY_TYPE(cursor.getInt(cursor.getColumnIndex("DELIVERY_TYPE")));
                //  product.setDescription(cursor.getString(cursor.getColumnIndex("Description")));
                //product.setType(cursor.getString(cursor.getColumnIndex("Type")));
                p.add(product);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return p;
    }

    public List<Product> GetCoverProducts() {
        List<Product> p = new ArrayList<>();
     //   String selectQuery = "SELECT PRODUCT_ID,NAME, sum(QUANTITY) as QUANTITY ,Treatment_ID ,Treatment_Description,sum(CurrentQuantity) as CurrentQuantity ,area_id,area_name,id FROM "
       //         + TABLE_COVER_PRODUCT + " where Driver_id =" + mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", "")+" and area_name='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("AreaName", "") + "' GROUP BY Treatment_ID,PRODUCT_ID order by NAME,Treatment_Description";
        String selectQuery = "SELECT PRODUCT_ID,NAME, sum(QUANTITY) as QUANTITY ,Treatment_ID ,Treatment_Description,sum(CurrentQuantity) as CurrentQuantity ,area_id,area_name,id,sum(DirtyQTY) as DirtyQTY FROM "
                + TABLE_COVER_PRODUCT + " where Driver_id =" + mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", "")+ " GROUP BY Treatment_ID,PRODUCT_ID order by NAME,Treatment_Description";


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        p = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setPRODUCT_ID(cursor.getString(cursor.getColumnIndex("PRODUCT_ID")));
                product.setQUNTITY(String.valueOf(cursor.getInt(cursor.getColumnIndex("QUANTITY"))));
                product.setNAME(cursor.getString(cursor.getColumnIndex("NAME")));
                product.setTreatment_code(cursor.getString(cursor.getColumnIndex("Treatment_ID")));
                product.setTreatment_description(cursor.getString(cursor.getColumnIndex("Treatment_Description")));
                product.setCurrentQuantity(cursor.getInt(cursor.getColumnIndex("CurrentQuantity")));
                product.setID(cursor.getString(cursor.getColumnIndex("id")));
                product.setArea_id(cursor.getString(cursor.getColumnIndex("area_id")));
                product.setArea_name(cursor.getString(cursor.getColumnIndex("area_name")));
                product.setDirtyQTY(String.valueOf(cursor.getInt(cursor.getColumnIndex("DirtyQTY"))));
            //   Log.e("CurrentQuantity",String.valueOf(product.getCurrentQuantity()));

                p.add(product);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return p;
    }

public List<Task> testTask()
{
    List<Task> Tasks = new ArrayList<>();
    String selectQuery = "SELECT * FROM " + TABLE_CONTRACTS + " where cast (( julianday(date('now')) - julianday(date(ASSIGN_DATE) )) as integer) !=0";
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    if(cursor.moveToFirst())

    {
        do {

            String t=cursor.getString(cursor.getColumnIndex("ASSIGN_DATE"));
            t=t+"";
        } while (cursor.moveToNext());
        cursor.close();
    }

    db.close();
    ////////
     selectQuery = "SELECT ASSIGN_Date  FROM " + TABLE_CONTRACTS ;
     db = this.getReadableDatabase();
     cursor = db.rawQuery(selectQuery, null);
    if(cursor.moveToFirst())

    {
        do {
            String t=cursor.getString(cursor.getColumnIndex("ASSIGN_DATE"));

        } while (cursor.moveToNext());
        cursor.close();
    }

    db.close();
    return Tasks;
}
    public List<Product> GetProductsDB() {
        List<Product> p = new ArrayList<>();
        String KEY_ID = "CONTRACT_ID";
        String selectQuery = "SELECT  * FROM " + TABLE_PROD_CONTRACT ;
      /*  String selectQuery = "SELECT  * FROM " + TABLE_PROD_CONTRACT + " WHERE "
                + KEY_ID + "= " + ContractID;*/

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        p = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Product product = new Product();
                product.setPRODUCT_ID(cursor.getString(cursor.getColumnIndex("PRODUCT_ID")));
                product.setAID(String.valueOf(cursor.getString(cursor.getColumnIndex("AID"))));
                product.setQUNTITY(String.valueOf(cursor.getInt(cursor.getColumnIndex("QUANTITY"))));
                product.setNAME(cursor.getString(cursor.getColumnIndex("NAME")));
                product.setUNIT_PRICE(String.valueOf(cursor.getDouble(cursor.getColumnIndex("UNIT_PRICE"))));
                product.setBUY_PRICE(cursor.getString(cursor.getColumnIndex("BUY_PRICE")));
                product.setASSIGN_ID(cursor.getString(cursor.getColumnIndex("ASSIGN_ID")));
                product.setTreatment_code(cursor.getString(cursor.getColumnIndex("Treatment_code")));
                product.setTreatment_description(cursor.getString(cursor.getColumnIndex("Treatment_description")));
                product.setPackage_id(cursor.getString(cursor.getColumnIndex("Package_id")));
                product.setPackage_NO(cursor.getString(cursor.getColumnIndex("Package_NO")));
               product.setDescription(cursor.getString(cursor.getColumnIndex("Description")));
                product.setType(cursor.getString(cursor.getColumnIndex("Type")));
                p.add(product);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return p;
    }

    public List<Task> SearchCardNo(String CardNo) {
        List<Task> tasks = new ArrayList<>();
        String selectQuery = "select * from " + TABLE_CONTRACTS + " where CARD_NO=" + CardNo;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Task t = new Task();

                t.setAssignmentId(cursor.getString(cursor.getColumnIndex("ASSIGN_ID")));
                t.setDriveID(cursor.getString(cursor.getColumnIndex("DRIV_ID")));
                t.setCardNo(cursor.getString(cursor.getColumnIndex("CARD_NO")));
                t.setClientName(cursor.getString(cursor.getColumnIndex("CLIENT_NAME")));
                t.setAreaId(cursor.getString(cursor.getColumnIndex("AREA_ID")));
                t.setAreaName(cursor.getString(cursor.getColumnIndex("AREA_NAME")));
              //  t.setCustomerAddress(cursor.getString(cursor.getColumnIndex("ADDRESS")));
                t.setDataComment(cursor.getString(cursor.getColumnIndex("DATA_COMMENT")));
                t.setOperationComment(cursor.getString(cursor.getColumnIndex("OPERATION_COMMENT")));
                t.setChangeDate(cursor.getString(cursor.getColumnIndex("CHANGE_DATE")));
                t.setX(cursor.getDouble(cursor.getColumnIndex("X")));
                t.setY(cursor.getDouble(cursor.getColumnIndex("Y")));
                t.setTelNo(cursor.getString(cursor.getColumnIndex("TelNo")));
                t.setRecieptNo(cursor.getString(cursor.getColumnIndex("RecieptNo")));
                tasks.add(t);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return tasks;
    }

    public List<Task> SearchCustomerName(String CustName) {
        List<Task> tasks = new ArrayList<>();
        String selectQuery = "select * from " + TABLE_CONTRACTS + " where CLIENT_NAME='" + CustName + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Task t = new Task();

                t.setAssignmentId(cursor.getString(cursor.getColumnIndex("ASSIGN_ID")));
                t.setDriveID(cursor.getString(cursor.getColumnIndex("DRIV_ID")));
                t.setCardNo(cursor.getString(cursor.getColumnIndex("CARD_NO")));
                t.setClientName(cursor.getString(cursor.getColumnIndex("CLIENT_NAME")));
                t.setAreaId(cursor.getString(cursor.getColumnIndex("AREA_ID")));
                t.setAreaName(cursor.getString(cursor.getColumnIndex("AREA_NAME")));
             //   t.setCustomerAddress(cursor.getString(cursor.getColumnIndex("ADDRESS")));
                t.setDataComment(cursor.getString(cursor.getColumnIndex("DATA_COMMENT")));
                t.setOperationComment(cursor.getString(cursor.getColumnIndex("OPERATION_COMMENT")));
                t.setChangeDate(cursor.getString(cursor.getColumnIndex("CHANGE_DATE")));
                t.setX(cursor.getDouble(cursor.getColumnIndex("X")));
                t.setY(cursor.getDouble(cursor.getColumnIndex("Y")));
                t.setTelNo(cursor.getString(cursor.getColumnIndex("TelNo")));
                t.setRecieptNo(cursor.getString(cursor.getColumnIndex("RecieptNo")));
                tasks.add(t);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return tasks;
    }

    public List<Task> SearchAddress(String Address) {
        List<Task> tasks = new ArrayList<>();
        String selectQuery = "select * from " + TABLE_CONTRACTS + " where ADDRESS='" + Address + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Task t = new Task();

                t.setAssignmentId(cursor.getString(cursor.getColumnIndex("ASSIGN_ID")));
                t.setDriveID(cursor.getString(cursor.getColumnIndex("DRIV_ID")));
                t.setCardNo(cursor.getString(cursor.getColumnIndex("CARD_NO")));
                t.setClientName(cursor.getString(cursor.getColumnIndex("CLIENT_NAME")));
                t.setAreaId(cursor.getString(cursor.getColumnIndex("AREA_ID")));
                t.setAreaName(cursor.getString(cursor.getColumnIndex("AREA_NAME")));
               // t.setCustomerAddress(cursor.getString(cursor.getColumnIndex("ADDRESS")));
                t.setDataComment(cursor.getString(cursor.getColumnIndex("DATA_COMMENT")));
                t.setOperationComment(cursor.getString(cursor.getColumnIndex("OPERATION_COMMENT")));
                t.setChangeDate(cursor.getString(cursor.getColumnIndex("CHANGE_DATE")));
                t.setX(cursor.getDouble(cursor.getColumnIndex("X")));
                t.setY(cursor.getDouble(cursor.getColumnIndex("Y")));
                t.setTelNo(cursor.getString(cursor.getColumnIndex("TelNo")));
                t.setRecieptNo(cursor.getString(cursor.getColumnIndex("RecieptNo")));
                tasks.add(t);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return tasks;
    }

    public List<Task> SearchComments(String Comments) {
        List<Task> tasks = new ArrayList<>();
        String selectQuery = "select * from " + TABLE_CONTRACTS + " where OPERATION_COMMENT='" + Comments + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Task t = new Task();

                t.setAssignmentId(cursor.getString(cursor.getColumnIndex("ASSIGN_ID")));
                t.setDriveID(cursor.getString(cursor.getColumnIndex("DRIV_ID")));
                t.setCardNo(cursor.getString(cursor.getColumnIndex("CARD_NO")));
                t.setClientName(cursor.getString(cursor.getColumnIndex("CLIENT_NAME")));
                t.setAreaId(cursor.getString(cursor.getColumnIndex("AREA_ID")));
                t.setAreaName(cursor.getString(cursor.getColumnIndex("AREA_NAME")));
               // t.setCustomerAddress(cursor.getString(cursor.getColumnIndex("ADDRESS")));
                t.setDataComment(cursor.getString(cursor.getColumnIndex("DATA_COMMENT")));
                t.setOperationComment(cursor.getString(cursor.getColumnIndex("OPERATION_COMMENT")));
                t.setChangeDate(cursor.getString(cursor.getColumnIndex("CHANGE_DATE")));
                t.setX(cursor.getDouble(cursor.getColumnIndex("X")));
                t.setY(cursor.getDouble(cursor.getColumnIndex("Y")));
                t.setTelNo(cursor.getString(cursor.getColumnIndex("TelNo")));
                t.setRecieptNo(cursor.getString(cursor.getColumnIndex("RecieptNo")));
                tasks.add(t);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return tasks;
    }

    public List<Task> Search(String Query,List<Task>mTasks) {
        String incluse = "(";

        if (mTasks != null && mTasks.size() > 0) {
            for (int i = 0; i < mTasks.size(); i++) {
                incluse += "'" + mTasks.get(i).getAID() + "'";
                if (i != mTasks.size() - 1)
                    incluse += ',';
            }
            incluse += ")";
        }
            List<Task> tasks = new ArrayList<>();
            String selectQuery = "select * from " + TABLE_CONTRACTS + " where AID in " + incluse + " and ( UPPER(street_name)  LIKE '%" + Query + "%' OR UPPER(CARD_NO)  LIKE '%" + Query + "%' OR UPPER(ADDRESS) LIKE '%" + Query + "%' OR UPPER(CLIENT_NAME) LIKE '%" + Query + "%')"+" ORDER BY PRIORITY desc";;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
             if (cursor.moveToFirst()) {
            do {
                Task t = new Task();
                t.setAssignmentId(cursor.getString(cursor.getColumnIndex("ASSIGN_ID")));
                t.setDriveID(cursor.getString(cursor.getColumnIndex("DRIV_ID")));
                t.setCardNo(cursor.getString(cursor.getColumnIndex("CARD_NO")));
                t.setClientName(cursor.getString(cursor.getColumnIndex("CLIENT_NAME")));
                t.setAreaId(cursor.getString(cursor.getColumnIndex("AREA_ID")));
                t.setAreaName(cursor.getString(cursor.getColumnIndex("AREA_NAME")));
                t.setCity_name(cursor.getString(cursor.getColumnIndex("city_name")));
                t.setStreet_name(cursor.getString(cursor.getColumnIndex("street_name")));
                t.setHome_no(cursor.getString(cursor.getColumnIndex("home_no")));
                t.setFloor_no(cursor.getString(cursor.getColumnIndex("floor_no")));
                t.setFlat_no(cursor.getString(cursor.getColumnIndex("flat_no")));
                t.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
                t.setRegion_name(cursor.getString(cursor.getColumnIndex("region_name")));
                t.setPRIORITY(cursor.getString(cursor.getColumnIndex("PRIORITY")));
                t.setDataComment(cursor.getString(cursor.getColumnIndex("DATA_COMMENT")));
                t.setOperationComment(cursor.getString(cursor.getColumnIndex("OPERATION_COMMENT")));
                t.setChangeDate(cursor.getString(cursor.getColumnIndex("CHANGE_DATE")));
                t.setX(cursor.getDouble(cursor.getColumnIndex("X")));
                t.setY(cursor.getDouble(cursor.getColumnIndex("Y")));
                t.setTelNo(cursor.getString(cursor.getColumnIndex("TelNo")));
                t.setRecieptNo(cursor.getString(cursor.getColumnIndex("RecieptNo")));
                t.setStatus(cursor.getString(cursor.getColumnIndex("Status")));
                t.setPilotID(cursor.getString(cursor.getColumnIndex("PILOT_ID")));
                t.setAID(cursor.getString(cursor.getColumnIndex("AID")));
                t.setASSIGN_DATE(cursor.getString(cursor.getColumnIndex("ASSIGN_DATE")));
                t.setLAST_DOWNLOAD(cursor.getString(cursor.getColumnIndex("LAST_DOWNLOAD")));
                t.setORDER_DELIVERY(cursor.getString(cursor.getColumnIndex("ORDER_DELIVERY")));
                t.setCREATED_STATE(cursor.getString(cursor.getColumnIndex("CREAED_STATE")));
                t.setCHANGED_STATE(cursor.getString(cursor.getColumnIndex("CHANGED_STATE")));
                t.setDELIVERY_STATE(cursor.getString(cursor.getColumnIndex("DELIVERY_STATE")));
                t.setCUSTOMER_TYPE(cursor.getString(cursor.getColumnIndex("CUSTOMER_TYPE")));
                t.setAREA2_NAME(cursor.getString(cursor.getColumnIndex("AREA2_NAME")));
                t.setPAY_CREDIT(cursor.getString(cursor.getColumnIndex("PAY_CREDIT")));
                t.setDuration(cursor.getString(cursor.getColumnIndex("Duration")));
                t.setADDRESS(cursor.getString(cursor.getColumnIndex("ADDRESS")));
                t.setFU_Note(cursor.getString(cursor.getColumnIndex("FU_Note")));
                t.setTOTAL_PRICE_PAID(cursor.getDouble(cursor.getColumnIndex("TOTAL_PRICE_PAID")));
                t.setACTUAL_PAID(cursor.getDouble(cursor.getColumnIndex("ACTUAL_PAID")));
                t.setALLOW_CREDIT(cursor.getInt(cursor.getColumnIndex("ALLOW_CREDIT")));
                t.setBonus(cursor.getDouble(cursor.getColumnIndex("bonus")));
                t.setFrom_time(cursor.getString(cursor.getColumnIndex("FROM_TIME")));
                t.setTo_time(cursor.getString(cursor.getColumnIndex("TO_TIME")));
                t.setASSIGNMENTS_TYPE(cursor.getInt(cursor.getColumnIndex("ASSIGNMENT_TYPE")));
                t.setLOC_CONFIRMED(cursor.getInt(cursor.getColumnIndex("LOC_CONFIRMED")));

                tasks.add(t);
            } while (cursor.moveToNext());
                 cursor.close();
        }
        db.close();
        return tasks;
    }

    public void DeletAll(HomeWithMenuActivity homeWithMenuActivity) {
       // String query = "delete from " + TABLE_CONTRACTS + " where 1=1";
        //String query2 = "delete from " + TABLE_PROD_CONTRACT + " where 1=1";
        String where_vacaion=" cast ((SELECT julianday(date('now')) - julianday(date(Vacation_to_date) ) from VACATIONS) as integer) <0";
        String where_contract=" cast ((SELECT julianday(date('now')) - julianday(date(ASSIGN_DATE) ) from ASSIGN_WORK_X_Y) as integer) >=3";
        String where_Product="AID in(select AID from ASSIGN_WORK_X_Y where cast ((SELECT julianday(date('now')) - julianday(date(ASSIGN_DATE) ) from ASSIGN_WORK_X_Y) as integer) >=3 )";
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PROD_CONTRACT, null, null);
        db.delete(TABLE_CONTRACTS, null, null);
        db.delete(TABLE_VACATION,where_vacaion,null);
        db.delete(TABLE_Pilot,null,null);
        db.delete(TABLE_KPI,null,null);
        db.delete(TABLE_COVER_PRODUCT,null,null);
        db.delete(TABLE_PILOT_COVER,null,null);
        db.delete(TABLE_VACATION,null,null);
        db.delete(TABLE_VACATION_REASONS,null,null);
        db.delete(TABLE_COLLECTION,null,null);
        db.delete(TABLE_RECONCILATION,null,null);

        // db.delete(TABLE_PACKAGES,null,null);



        db.close();
      UpdateAllData(homeWithMenuActivity,true);


    }


    public void UpdateAllData(HomeWithMenuActivity homeWithMenuActivity,boolean flag) {
        mHomeWithMenuActivity=homeWithMenuActivity;
         NextActivity=flag;
      CheckCashing(homeWithMenuActivity,flag);
    }

    @Override
    public void setTasks(List<Task> Tasks) {
    if(Tasks!=null)
    {
        AddContract(Tasks);
    }
    }

    @Override
    public void showError(String error) {
        Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
       /* if(error.equalsIgnoreCase(mContext.getResources().getString(R.string.NoDataFound)))
        {
            if(pbDailog!=null&&pbDailog.isShowing())
            {
                pbDailog.hide();
            }
        }*/
        if(pbDailog!=null&&pbDailog.isShowing())
        {
            pbDailog.hide();
        }

    }

    @Override
    public void setProducts(List<Product> products) {
      if(products!=null)
          AddProducts(products);

    }

    @Override
    public void setCoverProducts(List<Product> products) {
        // insert to cover_products
        if(products!=null)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_COVER_PRODUCT,null,null);
            ContentValues values = new ContentValues();
            if (products != null) {
                for (int i = 0; i < products.size(); i++) {
                    values = new ContentValues();
                    values.put("PRODUCT_ID", products.get(i).getPRODUCT_ID());
                    values.put("QUANTITY", products.get(i).getQUNTITY());
                    values.put("NAME", products.get(i).getNAME());
                    values.put("Treatment_ID",products.get(i).getTreatment_code());
                    values.put("Treatment_description",products.get(i).getTreatment_description());
                    values.put("Driver_id",mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName",""));
                    values.put("CurrentQuantity",products.get(i).getCurrentQuantity());
                    values.put("id",products.get(i).getID());
                    values.put("area_id",products.get(i).getArea_id());
                    values.put("area_name",products.get(i).getArea_name());
                    values.put("DirtyQty",products.get(i).getDirtyQTY());
                    db.insertOrThrow(TABLE_COVER_PRODUCT,null,values);
                }
            }
            db.close();
        }
        mPresnter.setDriverProducts();
        mPresnter.GetCollectionDriver();


    }
    public boolean UpdateCoverProductDriver(List<Product> products)
    {
        if(products!=null)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_COVER_PRODUCT,null,null);
            ContentValues values = new ContentValues();
            if (products != null) {
                for (int i = 0; i < products.size(); i++) {
                    values = new ContentValues();
                    values.put("PRODUCT_ID", products.get(i).getPRODUCT_ID());
                    values.put("QUANTITY", products.get(i).getQUNTITY());
                    values.put("NAME", products.get(i).getNAME());
                    values.put("Treatment_ID",products.get(i).getTreatment_code());
                    values.put("Treatment_description",products.get(i).getTreatment_description());
                    values.put("Driver_id",mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName",""));
                    values.put("CurrentQuantity",products.get(i).getCurrentQuantity());
                    values.put("id",products.get(i).getID());
                    values.put("area_id",products.get(i).getArea_id());
                    values.put("area_name",products.get(i).getArea_name());
                    values.put("DirtyQty",products.get(i).getDirtyQTY());
                    db.insertOrThrow(TABLE_COVER_PRODUCT,null,values);
                }
            }
            db.close();
        }
        return true;
    }

    @Override
    public void setPilots(List<Pilot> Pilots) {
     if(Pilots!=null)
     {
         AddPilots(Pilots);
     }


    }

    @Override
    public void setTotalAmount(String TotalAmount) {

    }

    @Override
    public void setKPI(List<KPI> kpis) {
        if(kpis!=null)
            SetKPI(kpis);
    }

    @Override
    public void setProductReference(List<Product> products) {
     if(products!=null)
         SetProductsReference(products);
    }

    @Override
    public void setPackageInfo(List<PackageInfo> packageInfoList) {
        if(packageInfoList!=null)
            SetPackages(packageInfoList);

    }

    @Override
    public void showMsg(String Msg) {

    }

    @Override
    public void ShowLoading() {

    }

    @Override
    public void StopLoading() {

    }

    @Override
    public void setCollection(List<Collection> lst_coll) {
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase dbReader = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        if (lst_coll != null) {
            for (int i = 0; i < lst_coll.size(); i++) {
                values = new ContentValues();
                values.put("ASSIGN_ID", lst_coll.get(i).getASSIGN_ID());
                values.put("AID", lst_coll.get(i).getAID());
                values.put("CARD_NO", lst_coll.get(i).getCARD_NO());
                values.put("INVOICE_NO", lst_coll.get(i).getINVOICE_NO());
                values.put("AMOUNT", lst_coll.get(i).getAMOUNT());
                values.put("PAID_AMOUNT", lst_coll.get(i).getPAID_AMOUNT());
                values.put("DATE",lst_coll.get(i).getDATE1());

                String selectQuery = "SELECT * FROM " + TABLE_COLLECTION + " where AID=" + lst_coll.get(i).getAID() + " " +
                        "and INVOICE_NO='" + lst_coll.get(i).getINVOICE_NO() + "'" ;

                Cursor cursor = dbReader.rawQuery(selectQuery, null);
                if (cursor.getCount() <= 0)
                    db.insertOrThrow(TABLE_COLLECTION, null, values);
                else
                    db.update(TABLE_COLLECTION, values, "INVOICE_NO='" + lst_coll.get(i).getINVOICE_NO() +
                            "' and AID=" + lst_coll.get(i).getAID() , null);
                cursor.close();
            }
        }
        db.close();
        dbReader.close();

    }

    @Override
    public void SetCoverPilot(List<Pilot_Cover> lst_cover) {
        AddCoverPilot(lst_cover,true);
    }

    @Override
    public void SuccessPilotAccept() {

    }

    @Override
    public void SetCheckCoverPilot(List<Pilot_Cover> lst_cover) {
        AddCoverPilot(lst_cover,false);

    }

    @Override
    public void SuccessPilotReject() {

    }

    @Override
    public void UpdateCoverAfterReconcilation() {

    }

    @Override
    public void SetReconcilationRequests(List<Reconcilation> lst_reconcilation) {
        if(lst_reconcilation!=null)
        {
            SQLiteDatabase db = this.getWritableDatabase();
                db.delete(TABLE_RECONCILATION, null, null);
            SQLiteDatabase dbReader = this.getReadableDatabase();
            ContentValues values = new ContentValues();
                for (int i = 0; i < lst_reconcilation.size(); i++) {
                    values = new ContentValues();
                    values.put("ID", lst_reconcilation.get(i).getID());
                    values.put("MID", lst_reconcilation.get(i).getMID());
                    values.put("ACTUAL_CLEAN", lst_reconcilation.get(i).getACTUAL_CLEAN());
                    values.put("ACTUAL_DIRTY", lst_reconcilation.get(i).getACTUAL_DIRTY());
                    values.put("APPROVED_CLEAN", lst_reconcilation.get(i).getAPPROVED_CLEAN());
                    values.put("APPROVED_DIRTY", lst_reconcilation.get(i).getAPPROVED_DIRTY());
                    values.put("EXP_CLEAN", lst_reconcilation.get(i).getEXP_CLEAN());
                    values.put("EXP_DIRTY", lst_reconcilation.get(i).getEXP_DIRTY());
                    values.put("AREA", lst_reconcilation.get(i).getAREA());
                    values.put("AREA_NAME", lst_reconcilation.get(i).getAREA_NAME());
                    values.put("TREATMENT", lst_reconcilation.get(i).getTREATMENT());
                    values.put("TRE_NAME", lst_reconcilation.get(i).getTRE_NAME());
                    values.put("TIME",lst_reconcilation.get(i).getLAST_TIME());
                    values.put("DATE",lst_reconcilation.get(i).getLAST_DATE());
                    values.put("DRIVER",lst_reconcilation.get(i).getDRIVER());
                    values.put("PILOT",lst_reconcilation.get(i).getPILOT());
                    values.put("PRODUCT",lst_reconcilation.get(i).getPRODUCT());
                    values.put("PRODUCT_NAME",lst_reconcilation.get(i).getPRODUCT_NAME());
                    values.put("EXPECTED_MONEY",lst_reconcilation.get(i).getEXPECTED_MONEY());
                    values.put("ACTUAL_MONEY",lst_reconcilation.get(i).getACTUAL_MONEY());
                    values.put("APPROVED_MONEY",lst_reconcilation.get(i).getAPPROVED_MONEY());

                    db.insertOrThrow(TABLE_RECONCILATION, null, values);
                }
                db.close();


            }

    }

    @Override
    public void RefreshQTYReconcilation() {

    }

    public List<String> GetAreas(String driverID)
    {
        List<String>Areas=new ArrayList<>();
        String Query="Select distinct(AREA_NAME) as Area_Name From "+TABLE_CONTRACTS+" where DRIV_ID="+driverID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            do {
                Areas.add(cursor.getString(cursor.getColumnIndex("Area_Name")));

            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return Areas;
    }
    public List<String> GetAreasPilots(String PilotID)
    {
        List<String>Areas=new ArrayList<>();
        String Query="Select distinct(AREA_NAME) as Area_Name From "+TABLE_CONTRACTS+" where PILOT_ID="+PilotID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            do {
                Areas.add(cursor.getString(cursor.getColumnIndex("Area_Name")));

            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return Areas;
    }
    public List<Pilot_Cover>GetAllCoverPilots(String driverID,String PilotID)
    {
        List<Pilot_Cover>lst_pilot=new ArrayList<>();
        String Query="Select ID,Pilot_ID,Quantity,Current_Quantity,name,Treatment_Name,DRIVER_ID From "+TABLE_PILOT_COVER+" where DRIVER_ID='"+driverID+"' and Pilot_ID='"+PilotID+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            do {
                Pilot_Cover p=new Pilot_Cover();
                p.setID(cursor.getString(cursor.getColumnIndex("ID")));
                p.setPilot_ID(cursor.getString(cursor.getColumnIndex("Pilot_ID")));
                p.setQuantity(cursor.getInt(cursor.getColumnIndex("Quantity")));
                p.setCurrent_Quantity(cursor.getInt(cursor.getColumnIndex("Current_Quantity")));
                p.setName(cursor.getString(cursor.getColumnIndex("name")));
                p.setTreatment_Name(cursor.getString(cursor.getColumnIndex("Treatment_Name")));
                lst_pilot.add(p);


            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return lst_pilot;

    }
    public List<Pilot_Cover>GetCoverPilot(String PilotID)
    {
        userInfo= ServerManager.deSerializeStringToObject(mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserInfo",""),UserInfo.class);
        String Query="";
        if(PilotID.equalsIgnoreCase(mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", ""))&&userInfo.getUSERGROUP_ID()==2)
        {
             Query="Select * From "+TABLE_PILOT_COVER+" where Pilot_ID='"+PilotID+"' and Pilot_Accept='true' and Area_Name='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"' order by name,Treatment_Name";

        }
        else
        {
          // Query="Select * From "+TABLE_PILOT_COVER;
            Query="Select * From "+TABLE_PILOT_COVER+" where Pilot_ID='"+PilotID+"' and DRIVER_ID='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", "")+"' and Pilot_Accept='true' and Area_Name='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"' order by name,Treatment_Name";
          // Log.i("queyCover",Query);
        }
            List<Pilot_Cover>lst_pilot=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            do {
                Pilot_Cover p=new Pilot_Cover();
                p.setID(cursor.getString(cursor.getColumnIndex("ID")));
                p.setPilot_ID(cursor.getString(cursor.getColumnIndex("Pilot_ID")));
                p.setQuantity(cursor.getInt(cursor.getColumnIndex("Quantity")));
                p.setCurrent_Quantity(cursor.getInt(cursor.getColumnIndex("Current_Quantity")));
                p.setName(cursor.getString(cursor.getColumnIndex("name")));
                p.setTreatment_Name(cursor.getString(cursor.getColumnIndex("Treatment_Name")));
                p.setTreatment_ID(cursor.getString(cursor.getColumnIndex("Treatment_ID")));
                p.setProduct_ID(cursor.getString(cursor.getColumnIndex("Product_ID")));
                p.setDirty_Qty(cursor.getInt(cursor.getColumnIndex("Dirty_Qty")));
                p.setActual_Clean(cursor.getString(cursor.getColumnIndex("Actual_Clean")));
                p.setActual_Dirty(cursor.getString(cursor.getColumnIndex("Actual_Dirty")));
                p.setApproved_clean(cursor.getString(cursor.getColumnIndex("Approved_clean")));
                p.setApproved_Dirty(cursor.getString(cursor.getColumnIndex("Approved_Dirty")));
                p.setDriver_Accept(cursor.getString(cursor.getColumnIndex("Driver_Accept")));
                p.setTOTAL_RECIEPT_AMOUNT_DRIVER(cursor.getString(cursor.getColumnIndex("TOTAL_RECIEPT_AMOUNT_DRIVER")));
                p.setTOTAL_RECIEPT_AMOUNT_PILOT(cursor.getString(cursor.getColumnIndex("TOTAL_RECIEPT_AMOUNT_PILOT")));
                p.setArea_ID(cursor.getString(cursor.getColumnIndex("Area_ID")));
                p.setArea_Name(cursor.getString(cursor.getColumnIndex("Area_Name")));
                lst_pilot.add(p);


            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
      //  Log.i("queyCoverc", String.valueOf(lst_pilot.size()));
        return lst_pilot;

    }

   public void UpdateCoverPilot(List<Product>ReplacedProducts,String PilotID,Task task,List<Product>CanceledProduct)
   {
       String Date=task.getASSIGN_DATE();
       userInfo= ServerManager.deSerializeStringToObject(mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserInfo",""),UserInfo.class);
       SQLiteDatabase db = this.getWritableDatabase();

       for (int i = 0; i < ReplacedProducts.size(); i++) {
           String strSQL="";
           if(PilotID.equalsIgnoreCase(mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", ""))&&userInfo.getUSERGROUP_ID()==2)
           {
               if(ReplacedProducts.get(i).getDELIVERY_TYPE()==3 || ReplacedProducts.get(i).getDELIVERY_TYPE()==4 || ReplacedProducts.get(i).getDELIVERY_TYPE()==2)
                strSQL = "UPDATE "+TABLE_PILOT_COVER+" SET Current_Quantity = Current_Quantity-"+ReplacedProducts.get(i).getQUNTITY_REPLACED()+
                       "WHERE Product_ID = '"+ ReplacedProducts.get(i).getPRODUCT_ID()+"' and Treatment_ID='"+
                       ReplacedProducts.get(i).getTreatment_code()+"'"+ " and Pilot_ID='"+PilotID+"' and Pilot_Accept='true' and Area_Name='"+mContext.getSharedPreferences
                       (Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"' and Assign_Date='"+Date+"'";
               else
                  strSQL = "UPDATE "+TABLE_PILOT_COVER+" SET Current_Quantity = Current_Quantity-"+ReplacedProducts.get(i).getQUNTITY_REPLACED()
                          +" ,Dirty_Qty = Dirty_Qty+"+ReplacedProducts.get(i).getQUNTITY_REPLACED()+
                          " WHERE Product_ID = '"+ ReplacedProducts.get(i).getPRODUCT_ID()+"' and Treatment_ID='"+
                          ReplacedProducts.get(i).getTreatment_code()+"'"+ " and Pilot_ID='"+PilotID+"' and Pilot_Accept='true' and Area_Name='"+mContext.getSharedPreferences
                          (Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"' and Assign_Date='"+Date+"'";

           }
           else
           {
               if(ReplacedProducts.get(i).getDELIVERY_TYPE()==3 || ReplacedProducts.get(i).getDELIVERY_TYPE()==4 || ReplacedProducts.get(i).getDELIVERY_TYPE()==2)

                   // Query="Select * From "+TABLE_PILOT_COVER;
                strSQL = "UPDATE "+TABLE_PILOT_COVER+" SET Current_Quantity = Current_Quantity-"+ReplacedProducts.get(i).getQUNTITY_REPLACED()+
                       " WHERE Product_ID = '"+ ReplacedProducts.get(i).getPRODUCT_ID()+"' and Treatment_ID='"+
                       ReplacedProducts.get(i).getTreatment_code()+"'"+ " and Pilot_ID='"+PilotID+"' and DRIVER_ID='"
                       +mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", "")
                       +"' and Pilot_Accept='true' and Area_Name='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString
                       ("AreaName", "")+"' and Assign_Date='"+Date+"'";

               else
                   strSQL = "UPDATE "+TABLE_PILOT_COVER+" SET Current_Quantity = Current_Quantity-"+ReplacedProducts.get(i).getQUNTITY_REPLACED()+
                           " ,Dirty_Qty = Dirty_Qty+"+ReplacedProducts.get(i).getQUNTITY_REPLACED()+
                           " WHERE Product_ID = '"+ ReplacedProducts.get(i).getPRODUCT_ID()+"' and Treatment_ID='"+
                           ReplacedProducts.get(i).getTreatment_code()+"'"+ " and Pilot_ID='"+PilotID+"' and DRIVER_ID='"
                           +mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", "")
                           +"' and Pilot_Accept='true' and Area_Name='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString
                           ("AreaName", "")+"' and Assign_Date='"+Date+"'";

           }
            db.execSQL(strSQL);

    }
       for (int i = 0; i < CanceledProduct.size(); i++) {
           String strSQL="";
           if(PilotID.equalsIgnoreCase(mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", ""))&&userInfo.getUSERGROUP_ID()==2)
           {
               if(CanceledProduct.get(i).getDELIVERY_TYPE()!=3 && CanceledProduct.get(i).getDELIVERY_TYPE()!=4 && CanceledProduct.get(i).getDELIVERY_TYPE()!=2)
                   strSQL = "UPDATE "+TABLE_PILOT_COVER+" SET Current_Quantity = Current_Quantity-"+CanceledProduct.get(i).getQUNTITY_REPLACED()+
                           " WHERE Product_ID = '"+ CanceledProduct.get(i).getPRODUCT_ID()+"' and Treatment_ID='"+
                           CanceledProduct.get(i).getTreatment_code()+"'"+ " and Pilot_ID='"+PilotID+"' and Pilot_Accept='true' and Area_Name='"+mContext.getSharedPreferences
                           (Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"' and Assign_Date='"+Date+"'";
               else
                   strSQL = "UPDATE "+TABLE_PILOT_COVER+" SET Current_Quantity = Current_Quantity-"+CanceledProduct.get(i).getQUNTITY_REPLACED()
                           +" ,Dirty_Qty = Dirty_Qty+"+CanceledProduct.get(i).getQUNTITY_REPLACED()+
                           " WHERE Product_ID = '"+ CanceledProduct.get(i).getPRODUCT_ID()+"' and Treatment_ID='"+
                           CanceledProduct.get(i).getTreatment_code()+"'"+ " and Pilot_ID='"+PilotID+"' and Pilot_Accept='true' and Area_Name='"+mContext.getSharedPreferences
                           (Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"' and Assign_Date='"+Date+"'";

           }
           else
           {
               if(CanceledProduct.get(i).getDELIVERY_TYPE()!=3 && CanceledProduct.get(i).getDELIVERY_TYPE()!=4 && CanceledProduct.get(i).getDELIVERY_TYPE()!=2)

                   // Query="Select * From "+TABLE_PILOT_COVER;
                   strSQL = "UPDATE "+TABLE_PILOT_COVER+" SET Current_Quantity = Current_Quantity-"+CanceledProduct.get(i).getQUNTITY_REPLACED()+
                           " WHERE Product_ID = '"+ CanceledProduct.get(i).getPRODUCT_ID()+"' and Treatment_ID='"+
                           CanceledProduct.get(i).getTreatment_code()+"'"+ " and Pilot_ID='"+PilotID+"' and DRIVER_ID='"
                           +mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", "")
                           +"' and Pilot_Accept='true' and Area_Name='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString
                           ("AreaName", "")+"' and Assign_Date='"+Date+"'";

               else
                   strSQL = "UPDATE "+TABLE_PILOT_COVER+" SET Current_Quantity = Current_Quantity-"+CanceledProduct.get(i).getQUNTITY_REPLACED()+
                           " ,Dirty_Qty = Dirty_Qty+"+CanceledProduct.get(i).getQUNTITY_REPLACED()+
                           " WHERE Product_ID = '"+ CanceledProduct.get(i).getPRODUCT_ID()+"' and Treatment_ID='"+
                           CanceledProduct.get(i).getTreatment_code()+"'"+ " and Pilot_ID='"+PilotID+"' and DRIVER_ID='"
                           +mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", "")
                           +"' and Pilot_Accept='true' and Area_Name='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString
                           ("AreaName", "")+"' and Assign_Date='"+Date+"'";

           }
           db.execSQL(strSQL);

       }
       db.close();

       }

    public List<Pilot_Cover>CheckAcceptenceCoverPilot(String PilotID)
    {
        userInfo= ServerManager.deSerializeStringToObject(mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserInfo",""),UserInfo.class);
        String Query="";
        if(PilotID.equalsIgnoreCase(mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", ""))&&userInfo.getUSERGROUP_ID()==2)
        {
            Query="Select * From "+TABLE_PILOT_COVER+" where Pilot_ID='"+PilotID+"' and Pilot_Accept='false' and Area_Name='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"'order by name,Treatment_Name";

        }
        else
        {
            Query="Select * From "+TABLE_PILOT_COVER+" where Pilot_ID='"+PilotID+"' and DRIVER_ID='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", "")+"' and Pilot_Accept='false' and Area_Name='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"' order by name,Treatment_Name";

        }
        List<Pilot_Cover>lst_pilot=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            do {
                Pilot_Cover p=new Pilot_Cover();
                p.setID(cursor.getString(cursor.getColumnIndex("ID")));
                p.setPilot_ID(cursor.getString(cursor.getColumnIndex("Pilot_ID")));
                p.setQuantity(cursor.getInt(cursor.getColumnIndex("Quantity")));
                p.setCurrent_Quantity(cursor.getInt(cursor.getColumnIndex("Current_Quantity")));
                p.setName(cursor.getString(cursor.getColumnIndex("name")));
                p.setTreatment_Name(cursor.getString(cursor.getColumnIndex("Treatment_Name")));
                p.setTreatment_ID(cursor.getString(cursor.getColumnIndex("Treatment_ID")));
                p.setProduct_ID(cursor.getString(cursor.getColumnIndex("Product_ID")));
                p.setActual_Clean(cursor.getString(cursor.getColumnIndex("Actual_Clean")));
                p.setActual_Dirty(cursor.getString(cursor.getColumnIndex("Actual_Dirty")));
                p.setApproved_clean(cursor.getString(cursor.getColumnIndex("Approved_clean")));
                p.setApproved_Dirty(cursor.getString(cursor.getColumnIndex("Approved_Dirty")));
                p.setDriver_Accept(cursor.getString(cursor.getColumnIndex("Driver_Accept")));
                p.setDirty_Qty(cursor.getInt(cursor.getColumnIndex("Dirty_Qty")));
                p.setTOTAL_RECIEPT_AMOUNT_DRIVER(cursor.getString(cursor.getColumnIndex("TOTAL_RECIEPT_AMOUNT_DRIVER")));
                p.setTOTAL_RECIEPT_AMOUNT_PILOT(cursor.getString(cursor.getColumnIndex("TOTAL_RECIEPT_AMOUNT_PILOT")));
                p.setArea_ID(cursor.getString(cursor.getColumnIndex("Area_ID")));
                p.setArea_Name(cursor.getString(cursor.getColumnIndex("Area_Name")));

                lst_pilot.add(p);


            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return lst_pilot;

    }
    public List<Pilot_Cover>checkreconcilation(String PilotID)
    {

        userInfo= ServerManager.deSerializeStringToObject(mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserInfo",""),UserInfo.class);
        String Query="";
        if(PilotID.equalsIgnoreCase(mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", ""))&&userInfo.getUSERGROUP_ID()==2)
        {
            //    '"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"'"
            Query="Select * From "+TABLE_PILOT_COVER+" where Pilot_ID='"+PilotID+"' and Pilot_Accept='true' and Area_Name='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"' order by name,Treatment_Name";

        }
        else
        {
            Query="Select * From "+TABLE_PILOT_COVER+" where Pilot_ID='"+PilotID+"' and Pilot_Accept='true' and Area_Name='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"' and DRIVER_ID='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", "")+"' order by name,Treatment_Name";
        }
        List<Pilot_Cover>lst_pilot=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            do {
                Pilot_Cover p=new Pilot_Cover();
                p.setID(cursor.getString(cursor.getColumnIndex("ID")));
                p.setPilot_ID(cursor.getString(cursor.getColumnIndex("Pilot_ID")));
                p.setQuantity(cursor.getInt(cursor.getColumnIndex("Quantity")));
                p.setCurrent_Quantity(cursor.getInt(cursor.getColumnIndex("Current_Quantity")));
                p.setName(cursor.getString(cursor.getColumnIndex("name")));
                p.setTreatment_Name(cursor.getString(cursor.getColumnIndex("Treatment_Name")));
                p.setTreatment_ID(cursor.getString(cursor.getColumnIndex("Treatment_ID")));
                p.setProduct_ID(cursor.getString(cursor.getColumnIndex("Product_ID")));
                p.setActual_Clean(cursor.getString(cursor.getColumnIndex("Actual_Clean")));
                p.setActual_Dirty(cursor.getString(cursor.getColumnIndex("Actual_Dirty")));
                p.setApproved_clean(cursor.getString(cursor.getColumnIndex("Approved_clean")));
                p.setApproved_Dirty(cursor.getString(cursor.getColumnIndex("Approved_Dirty")));
                p.setDriver_Accept(cursor.getString(cursor.getColumnIndex("Driver_Accept")));
                p.setDirty_Qty(cursor.getInt(cursor.getColumnIndex("Dirty_Qty")));
                p.setTOTAL_RECIEPT_AMOUNT_DRIVER(cursor.getString(cursor.getColumnIndex("TOTAL_RECIEPT_AMOUNT_DRIVER")));
                p.setTOTAL_RECIEPT_AMOUNT_PILOT(cursor.getString(cursor.getColumnIndex("TOTAL_RECIEPT_AMOUNT_PILOT")));
                p.setArea_ID(cursor.getString(cursor.getColumnIndex("Area_ID")));
                p.setArea_Name(cursor.getString(cursor.getColumnIndex("Area_Name")));
                p.setDRIVER_ID(cursor.getString(cursor.getColumnIndex("DRIVER_ID")));

                lst_pilot.add(p);


            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return lst_pilot;

    }
    public void AddCoverPilot(List<Pilot_Cover>lst_cover,boolean flag) {
        SQLiteDatabase db = this.getWritableDatabase();
        if(flag)
        db.delete(TABLE_PILOT_COVER, "Pilot_Accept='true'", null);
        else
            db.delete(TABLE_PILOT_COVER, "Pilot_Accept='false'", null);

    //    SQLiteDatabase dbReader = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        if (lst_cover != null) {
            for (int i = 0; i < lst_cover.size(); i++) {
                values = new ContentValues();
                values.put("ID", lst_cover.get(i).getID());
                values.put("Pilot_ID", lst_cover.get(i).getPilot_ID());
                values.put("Cover_Driver_ID", lst_cover.get(i).getCover_Driver_ID());
                values.put("Assign_Date", lst_cover.get(i).getAssign_Date());
                values.put("Quantity", lst_cover.get(i).getQuantity());
                values.put("Current_Quantity", lst_cover.get(i).getCurrent_Quantity());
                values.put("Product_ID", lst_cover.get(i).getProduct_ID());
                values.put("name", lst_cover.get(i).getName());
                values.put("Treatment_ID", lst_cover.get(i).getTreatment_ID());
                values.put("Treatment_Name", lst_cover.get(i).getTreatment_Name());
                values.put("DRIVER_ID", lst_cover.get(i).getDRIVER_ID());
                values.put("Pilot_Accept", lst_cover.get(i).getPilot_Accept());
                values.put("Dirty_Qty",lst_cover.get(i).getDirty_Qty());
                values.put("Actual_Clean",lst_cover.get(i).getActual_Clean());
                values.put("Actual_Dirty",lst_cover.get(i).getActual_Dirty());
                values.put("Approved_clean",lst_cover.get(i).getApproved_clean());
                values.put("Approved_Dirty",lst_cover.get(i).getApproved_Dirty());
                values.put("Driver_Accept",lst_cover.get(i).getDriver_Accept());
                values.put("TOTAL_RECIEPT_AMOUNT_DRIVER",lst_cover.get(i).getTOTAL_RECIEPT_AMOUNT_DRIVER());
                values.put("TOTAL_RECIEPT_AMOUNT_PILOT",lst_cover.get(i).getTOTAL_RECIEPT_AMOUNT_PILOT());
                values.put("Area_ID",lst_cover.get(i).getArea_ID());
                values.put("Area_Name",lst_cover.get(i).getArea_Name());


                db.insertOrThrow(TABLE_PILOT_COVER, null, values);
            }
            db.close();


        }
    }
    public boolean UpdateCoverDriver(String ProductsID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
         ContentValues values = new ContentValues();
        String[] Products=ProductsID.split("&");
        for(int i=0;i<Products.length;i++)
        {
            String[]details=Products[i].split(",");
            values.put("CurrentQuantity", String.valueOf(details[1]));
            db.update(TABLE_COVER_PRODUCT, values, "id ='"+details[0]+"'",null);


        }
        db.close();
        return true;
    }
    public String CalculateDays()
    {
        String selectQuery = "SELECT  cast ((julianday(date('now')) - julianday(date('2018-04-30'))) as integer)";
        //selectQuery = "SELECT date('2018-03-27 13:47:11')";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String tmp="";
        if (cursor.moveToFirst()) {
            do {
                tmp=cursor.getString(0);

            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return tmp;
    }
    public int getCountCashing()
    {
        int tmp=0;
        String selectQuery = "SELECT max(ID) from "+TABLE_CASHIG_REQUESTS;
        //selectQuery = "SELECT date('2018-03-27 13:47:11')";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                tmp=cursor.getInt(0);

            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return tmp;
    }
    public void AddCashingRequests(OFFLINE offline)
    {
        offline.setID(getCountCashing()+1);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (offline != null) {

                values = new ContentValues();
                values.put("ACTIONDate", offline.getACTIONDate());
                values.put("ID", offline.getID());
                values.put("BODY", offline.getBODY());
                values.put("TYPE", offline.getTYPE());
                values.put("AID", offline.getAID());
                values.put("DESCRIPTION", offline.getDESCRIPTION());
                values.put("LATITUDE", offline.getLATITUDE());
                values.put("LONGITUDE", offline.getLONGITUDE());
                values.put("USERNAME", offline.getUSERNAME());
            values.put("URL",offline.getURL());
                db.insertOrThrow(TABLE_CASHIG_REQUESTS, null, values);

        }
        db.close();

    }
    public List<OFFLINE> GetCashingRequest()
    {

        userInfo= ServerManager.deSerializeStringToObject(mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserInfo",""),UserInfo.class);
        String  Query="Select * From "+TABLE_CASHIG_REQUESTS+" order by ID asc";
        List<OFFLINE>lst=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            do {

                OFFLINE p=new OFFLINE();
                p.setID(cursor.getInt(cursor.getColumnIndex("ID")));
                p.setACTIONDate(cursor.getString(cursor.getColumnIndex("ACTIONDATE")));
                p.setAID(cursor.getString(cursor.getColumnIndex("AID")));
                p.setBODY(cursor.getString(cursor.getColumnIndex("BODY")));
                p.setDESCRIPTION(cursor.getString(cursor.getColumnIndex("DESCRIPTION")));
                p.setTYPE(cursor.getString(cursor.getColumnIndex("TYPE")));
                p.setLATITUDE(cursor.getString(cursor.getColumnIndex("LATITUDE")));
                p.setLONGITUDE(cursor.getString(cursor.getColumnIndex("LONGITUDE")));
                p.setUSERNAME(cursor.getString(cursor.getColumnIndex("USERNAME")));
                p.setURL(cursor.getString(cursor.getColumnIndex("URL")));
                lst.add(p);


            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return lst;

    }
    public  void DeleteCashingRequests(OFFLINE offline)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CASHIG_REQUESTS, "ID = "+offline.getID(), null);
        db.close();
    }
    class CheckCashing extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
         /*   if(pbDailog!=null&&pbDailog.isShowing())
            {
                pbDailog.hide();
            }*/

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
         /*   if(pbDailog!=null)
            {
                pbDailog.dismiss();
                pbDailog = null;
            }
            ///
            pbDailog = new ProgressDialog(mContext);
            pbDailog.setMessage("جاري التحميل .....");
            pbDailog.setCancelable(false);
            if(pbDailog.isShowing())
            {
                pbDailog.hide();
            }
            if(mContext.getApplicationContext()!=null)
                pbDailog.show();
*/
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);


        }

        @Override
        protected String doInBackground(String... params) {
            List<OFFLINE> lst_off=GetCashingRequest();
          User  user = new User(UserName, "", GetCurrentLocaion.CurrentLoc, " ");
            IContractOffline.Presenter offlinePresnter=new OfflinePresenter(DataBaseHelper.this, mContext, user);
            if(lst_off.size()>0) {
            //    Log.d("test2",String.valueOf(lst_off.size()));
                for (int i = 0; i < lst_off.size(); i++) {
                    if (lst_off.get(i).getTYPE().equalsIgnoreCase("SMS")) {
                        offlinePresnter.SendSMS(lst_off.get(i));
                    } else if (lst_off.get(i).getTYPE().equalsIgnoreCase("ACTION")) {
                        try {
                            offlinePresnter.setAssignmentAction(lst_off.get(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return "true";
        }
    }


    public boolean CheckCashing(HomeWithMenuActivity mHomeWithMenuActivity2,boolean flag) {

        List<OFFLINE> lst_off=GetCashingRequest();
       User user = new User(UserName, "", GetCurrentLocaion.CurrentLoc, " ");
        IContractOffline.Presenter offlinePresnter=new OfflinePresenter(this, mContext, user);
        if(lst_off.size()>0) {
            if(pbDailog!=null)
            {
                pbDailog.dismiss();
                pbDailog = null;
            }
            ///


            pbDailog = new ProgressDialog(mContext);
            pbDailog.setMessage("جاري التحميل .....");
            pbDailog.setCancelable(false);
            if(pbDailog.isShowing())
            {
                pbDailog.hide();
            }
            if(mContext.getApplicationContext()!=null)
                pbDailog.show();

          //  Log.d("test2",String.valueOf(lst_off.size()));
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
         // UpdateAllData(mHomeWithMenuActivity2,flag);
            UpdateAfterCashing();
        }
        return  true;
    }
    public void UpdateAfterCashing()
    {
        Activity activity = (Activity) mContext;
        if(!activity.isFinishing()&&pbDailog!=null)
        {
            pbDailog.dismiss();
            pbDailog = null;
        }
        ///


        pbDailog = new ProgressDialog(mContext);
        pbDailog.setMessage("جاري التحميل .....");
        pbDailog.setCancelable(false);
        if(pbDailog.isShowing())
        {
            pbDailog.hide();
        }
        if(!activity.isFinishing()&&mContext.getApplicationContext()!=null)
            pbDailog.show();


        // update data

        String UserName="";
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        if (sharedPreferences.contains("UserName")) {
            UserName = sharedPreferences.getString("UserName", "");
        }
        String Address = GetAddress.GetAddress(mContext, GetCurrentLocaion.CurrentLoc);
        User user = new User(UserName, "", GetCurrentLocaion.CurrentLoc, Address);
        mPresnter = new GetOrdersPresenter(this, mContext, user);
      userInfo = ServerManager.deSerializeStringToObject(sharedPreferences.getString("UserInfo", ""), UserInfo.class);
        boolean tmp;
        IPilotCover.Presenter presenter_pilotCover=new PilotCoverPresenter(this,mContext,user);
        IContractVacation.Presenter presenter_vacation=new VacationPresenter(this, mContext, user);
        try {
            presenter_vacation.GetVactions();
            presenter_vacation.GetVacationReason();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //  mPresnter.GetPackageInfo();
        mPresnter.GetProductReference();
        presenter_pilotCover.CheckProductsPilot();

        if(userInfo.getUSERGROUP_ID()!=2)
        {
            presenter_pilotCover.GetCoverAllPilot();
            presenter_pilotCover.GetReconcilationRequest();
            mPresnter.setTasksNotAssign();
            mPresnter.setPilots();
            mPresnter.GetKPI();


        }
        else
        {
            presenter_pilotCover.GetcoverPilot();
            mPresnter.setTasks();

        }


    }
    public void UpdateAfterAction(HomeWithMenuActivity mHomeWithMenuActivity2,boolean flag)
    {
        mHomeWithMenuActivity=mHomeWithMenuActivity2;
        NextActivity=flag;
        Activity activity = (Activity) mContext;
        if(!activity.isFinishing()&&pbDailog!=null)
        {
            pbDailog.dismiss();
            pbDailog = null;
        }
        ///


        pbDailog = new ProgressDialog(mContext);
        pbDailog.setMessage("جاري التحميل .....");
        pbDailog.setCancelable(false);
        if(pbDailog.isShowing())
        {
            pbDailog.hide();
        }
        if(!activity.isFinishing()&&mContext.getApplicationContext()!=null)
            pbDailog.show();


        // update data

        String UserName="";
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        if (sharedPreferences.contains("UserName")) {
            UserName = sharedPreferences.getString("UserName", "");
        }
        String Address = GetAddress.GetAddress(mContext, GetCurrentLocaion.CurrentLoc);
        User user = new User(UserName, "", GetCurrentLocaion.CurrentLoc, Address);
        mPresnter = new GetOrdersPresenter(this, mContext, user);
        userInfo = ServerManager.deSerializeStringToObject(sharedPreferences.getString("UserInfo", ""), UserInfo.class);
        boolean tmp;
        IPilotCover.Presenter presenter_pilotCover=new PilotCoverPresenter(this,mContext,user);
        IContractVacation.Presenter presenter_vacation=new VacationPresenter(this, mContext, user);
        try {
            presenter_vacation.GetVactions();
            presenter_vacation.GetVacationReason();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //  mPresnter.GetPackageInfo();
        mPresnter.GetProductReference();
        presenter_pilotCover.CheckProductsPilot();

        if(userInfo.getUSERGROUP_ID()!=2)
        {
            presenter_pilotCover.GetCoverAllPilot();
            mPresnter.setTasksNotAssign();
            mPresnter.setPilots();
            mPresnter.GetKPI();


        }
        else
        {
            presenter_pilotCover.GetcoverPilot();
            mPresnter.setTasks();

        }


    }


    @Override
    public void Failed(OFFLINE offline) {

    }

    @Override
    public void Success(OFFLINE offline) {
        DeleteCashingRequests(offline);

    }

    @Override
    public void SuccessALLRequests() {
        if (pbDailog != null && pbDailog.isShowing()) {
            pbDailog.hide();
        }
        UpdateAfterCashing();
    }

    @Override
    public void Fail() {
            if (pbDailog != null && pbDailog.isShowing()) {
                pbDailog.hide();
            }
    }

    public List<Task> GetTasksPilot_street(String PilotID,String Street) {
        String selectQuery="";
        userInfo= ServerManager.deSerializeStringToObject(mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserInfo",""),UserInfo.class);

        if(PilotID.equalsIgnoreCase(mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", ""))&&userInfo.getUSERGROUP_ID()==2)
        {
            selectQuery = "SELECT * FROM " + TABLE_CONTRACTS + " where PILOT_ID='" + PilotID + "'" +" AND AREA_NAME='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"' and street_name='"+Street+"' ORDER BY home_no ASC,CARD_NO";
        }
        else
        {
            selectQuery = "SELECT * FROM " + TABLE_CONTRACTS + " where PILOT_ID='" + PilotID + "' and DRIV_ID='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", "")+"'"+" AND AREA_NAME='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"'"+" and street_name='"+Street+"' ORDER BY home_no ASC,CARD_NO";
        //    Log.i("quer",selectQuery);

        }
        List<Task> Tasks = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Task t = new Task();
                t.setAssignmentId(cursor.getString(cursor.getColumnIndex("ASSIGN_ID")));
                t.setDriveID(cursor.getString(cursor.getColumnIndex("DRIV_ID")));
                t.setCardNo(cursor.getString(cursor.getColumnIndex("CARD_NO")));
                t.setClientName(cursor.getString(cursor.getColumnIndex("CLIENT_NAME")));
                t.setAreaId(cursor.getString(cursor.getColumnIndex("AREA_ID")));
                t.setAreaName(cursor.getString(cursor.getColumnIndex("AREA_NAME")));
                t.setCity_name(cursor.getString(cursor.getColumnIndex("city_name")));
                t.setStreet_name(cursor.getString(cursor.getColumnIndex("street_name")));
                t.setHome_no(cursor.getString(cursor.getColumnIndex("home_no")));
                t.setFloor_no(cursor.getString(cursor.getColumnIndex("floor_no")));
                t.setFlat_no(cursor.getString(cursor.getColumnIndex("flat_no")));
                t.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
                t.setRegion_name(cursor.getString(cursor.getColumnIndex("region_name")));
                t.setPRIORITY(cursor.getString(cursor.getColumnIndex("PRIORITY")));
                t.setDataComment(cursor.getString(cursor.getColumnIndex("DATA_COMMENT")));
                t.setOperationComment(cursor.getString(cursor.getColumnIndex("OPERATION_COMMENT")));
                t.setChangeDate(cursor.getString(cursor.getColumnIndex("CHANGE_DATE")));
                t.setX(cursor.getDouble(cursor.getColumnIndex("X")));
                t.setY(cursor.getDouble(cursor.getColumnIndex("Y")));
                t.setTelNo(cursor.getString(cursor.getColumnIndex("TelNo")));
                t.setRecieptNo(cursor.getString(cursor.getColumnIndex("RecieptNo")));
                t.setStatus(cursor.getString(cursor.getColumnIndex("Status")));
                t.setPilotID(cursor.getString(cursor.getColumnIndex("PILOT_ID")));
                t.setAID(cursor.getString(cursor.getColumnIndex("AID")));
                t.setASSIGN_DATE(cursor.getString(cursor.getColumnIndex("ASSIGN_DATE")));
                t.setLAST_DOWNLOAD(cursor.getString(cursor.getColumnIndex("LAST_DOWNLOAD")));
                t.setORDER_DELIVERY(cursor.getString(cursor.getColumnIndex("ORDER_DELIVERY")));
                t.setCREATED_STATE(cursor.getString(cursor.getColumnIndex("CREAED_STATE")));
                t.setCHANGED_STATE(cursor.getString(cursor.getColumnIndex("CHANGED_STATE")));
                t.setDELIVERY_STATE(cursor.getString(cursor.getColumnIndex("DELIVERY_STATE")));
                t.setCUSTOMER_TYPE(cursor.getString(cursor.getColumnIndex("CUSTOMER_TYPE")));
                t.setAREA2_NAME(cursor.getString(cursor.getColumnIndex("AREA2_NAME")));
                t.setPAY_CREDIT(cursor.getString(cursor.getColumnIndex("PAY_CREDIT")));
                t.setDuration(cursor.getString(cursor.getColumnIndex("Duration")));
                t.setADDRESS(cursor.getString(cursor.getColumnIndex("ADDRESS")));
                t.setFU_Note(cursor.getString(cursor.getColumnIndex("FU_Note")));
                t.setTOTAL_PRICE_PAID(cursor.getDouble(cursor.getColumnIndex("TOTAL_PRICE_PAID")));
                t.setACTUAL_PAID(cursor.getDouble(cursor.getColumnIndex("ACTUAL_PAID")));
                t.setALLOW_CREDIT(cursor.getInt(cursor.getColumnIndex("ALLOW_CREDIT")));
                t.setASSIGNMENTS_TYPE(cursor.getInt(cursor.getColumnIndex("ASSIGNMENT_TYPE")));
                t.setLOC_CONFIRMED(cursor.getInt(cursor.getColumnIndex("LOC_CONFIRMED")));

                t.setBonus(cursor.getDouble(cursor.getColumnIndex("bonus")));

                if (t.getStatus() == null || t.getStatus().equalsIgnoreCase("Not Delivered"))
                    Tasks.add(t);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return Tasks;
    }


    public List<Street> GetStreetsPilot(String PilotID) {
        String selectQuery="";
        userInfo= ServerManager.deSerializeStringToObject(mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserInfo",""),UserInfo.class);

        if(PilotID.equalsIgnoreCase(mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", ""))&&userInfo.getUSERGROUP_ID()==2)
        {
            selectQuery = "SELECT count(*) as count ,street_name  FROM " + TABLE_CONTRACTS + " where PILOT_ID='" + PilotID + "'" +" AND AREA_NAME='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"' and (Status is null or Status='Not Delivered') group by street_name ORDER BY street_name asc";
        }
        else
        {
            selectQuery = "SELECT count(*) as count ,street_name FROM " + TABLE_CONTRACTS + " where PILOT_ID='" + PilotID + "' and DRIV_ID='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", "")+"'"+" AND AREA_NAME='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"'"+" AND (Status is null or Status='Not Delivered') group by street_name ORDER BY street_name asc";
          //  Log.i("quer",selectQuery);

        }
       // Log.d("test2",selectQuery);
        List<Street> Tasks = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Street t = new Street();
                t.setCount(cursor.getInt(cursor.getColumnIndex("count")));
                t.setStreetName(cursor.getString(cursor.getColumnIndex("street_name")));
                Tasks.add(t);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
      //  Log.d("lenT", String.valueOf(Tasks.size()));
        return Tasks;
    }
 public List<Collection> GetCollection (int AID)
 {
     List<Collection> p = new ArrayList<>();
     String KEY_ID = "CONTRACT_ID";
     String selectQuery = "SELECT  * FROM " + TABLE_COLLECTION+" where AID="+AID ;
      /*  String selectQuery = "SELECT  * FROM " + TABLE_PROD_CONTRACT + " WHERE "
                + KEY_ID + "= " + ContractID;*/

     SQLiteDatabase db = this.getReadableDatabase();
     Cursor cursor = db.rawQuery(selectQuery, null);
     p = new ArrayList<>();
     if (cursor.moveToFirst()) {
         do {
             Collection product = new Collection();
             product.setAID(cursor.getInt(cursor.getColumnIndex("AID")));
             product.setASSIGN_ID(String.valueOf(cursor.getString(cursor.getColumnIndex("ASSIGN_ID"))));
             product.setCARD_NO(String.valueOf(cursor.getInt(cursor.getColumnIndex("CARD_NO"))));
             product.setAMOUNT(cursor.getDouble(cursor.getColumnIndex("AMOUNT")));
             product.setPAID_AMOUNT(cursor.getDouble(cursor.getColumnIndex("PAID_AMOUNT")));
             product.setINVOICE_NO(cursor.getString(cursor.getColumnIndex("INVOICE_NO")));
             product.setDATE1(cursor.getString(cursor.getColumnIndex("DATE")));
             p.add(product);
         } while (cursor.moveToNext());
         cursor.close();
     }
     db.close();
     return p;
 }

    public List<Reconcilation>GetRquestReconcilation(String PilotID)
    {

        userInfo= ServerManager.deSerializeStringToObject(mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserInfo",""),UserInfo.class);
        String Query="";

        Query="Select * From "+TABLE_RECONCILATION+" where PILOT='"+PilotID+"' and AREA_NAME='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("AreaName", "")+"' " +
                "and DRIVER='"+mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0).getString("UserName", "")+"'" +
                " order by PRODUCT_NAME,TRE_NAME";
        List<Reconcilation>lst_pilot=new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            do {
                Reconcilation p=new Reconcilation();
                p.setID(cursor.getString(cursor.getColumnIndex("ID")));
                p.setMID(cursor.getString(cursor.getColumnIndex("MID")));
                p.setPRODUCT_NAME(cursor.getString(cursor.getColumnIndex("PRODUCT_NAME")));
                p.setPRODUCT(cursor.getString(cursor.getColumnIndex("PRODUCT")));
                p.setTRE_NAME(cursor.getString(cursor.getColumnIndex("TRE_NAME")));
                p.setTREATMENT(cursor.getString(cursor.getColumnIndex("TREATMENT")));
                p.setACTUAL_CLEAN(cursor.getString(cursor.getColumnIndex("ACTUAL_CLEAN")));
                p.setACTUAL_DIRTY(cursor.getString(cursor.getColumnIndex("ACTUAL_DIRTY")));
                p.setEXP_CLEAN(cursor.getString(cursor.getColumnIndex("EXP_CLEAN")));
                p.setEXP_DIRTY(cursor.getString(cursor.getColumnIndex("EXP_DIRTY")));
                p.setAPPROVED_CLEAN(cursor.getString(cursor.getColumnIndex("APPROVED_CLEAN")));
                p.setAPPROVED_DIRTY(cursor.getString(cursor.getColumnIndex("APPROVED_DIRTY")));
                p.setAREA(cursor.getString(cursor.getColumnIndex("AREA")));
                p.setAREA_NAME(cursor.getString(cursor.getColumnIndex("AREA_NAME")));
                p.setDRIVER(cursor.getString(cursor.getColumnIndex("DRIVER")));
                p.setPILOT(cursor.getString(cursor.getColumnIndex("PILOT")));
                p.setLAST_DATE(cursor.getString(cursor.getColumnIndex("DATE")));
                p.setLAST_TIME(cursor.getString(cursor.getColumnIndex("TIME")));
                p.setAPPROVED_MONEY(cursor.getString(cursor.getColumnIndex("APPROVED_MONEY")));
                p.setEXPECTED_MONEY(cursor.getString(cursor.getColumnIndex("EXPECTED_MONEY")));
                p.setACTUAL_MONEY(cursor.getString(cursor.getColumnIndex("ACTUAL_MONEY")));

                lst_pilot.add(p);


            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return lst_pilot;

    }
}

