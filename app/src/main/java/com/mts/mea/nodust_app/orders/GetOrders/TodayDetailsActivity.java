package com.mts.mea.nodust_app.orders.GetOrders;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.mts.mea.nodust_app.BackgroundServices.GetCurrentLocaion;
import com.mts.mea.nodust_app.Evalution.KPI;
import com.mts.mea.nodust_app.Pilots.Pilot;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.User.User;
import com.mts.mea.nodust_app.common.Constants;
import com.mts.mea.nodust_app.common.GetAddress;
import com.mts.mea.nodust_app.products.Collection;
import com.mts.mea.nodust_app.products.Product;
import com.mts.mea.nodust_app.orders.Task;
import com.mts.mea.nodust_app.products.PackageInfo;

import java.util.List;

public class TodayDetailsActivity extends AppCompatActivity implements IContractGetOrders.View {

    private Pilot mPilot;
    User user;
    TextView pilotName;
    TextView RNo;
    TextView Total;
    TableLayout tableLayout;
    LinearLayout ll_table;
    TextView tv_notFound;
    private IContractGetOrders.Presenter mPresnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_details);
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
        tv_notFound=(TextView)findViewById(R.id.tv_notFound);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mPilot=(Pilot)getIntent().getExtras().get("mPilot");
        tableLayout=(TableLayout)findViewById(R.id.tab_product) ;
        pilotName=(TextView)findViewById(R.id.tv_pilotName) ;
        ll_table=(LinearLayout)findViewById(R.id.ll_table);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Constants.My_Prefs_language_NoDust, 0);
        mPilot=(Pilot)getIntent().getExtras().get("mPilot");
        if (sharedPreferences.contains("SelectedLanguage")) {
            if(sharedPreferences.getString("SelectedLanguage","En").equalsIgnoreCase("English")) {

                pilotName.setText(mPilot.getNameEn());
            }
            else
            {
                pilotName.setText(mPilot.getNameAr());
            }
        }
        else
        {
            pilotName.setText(mPilot.getNameEn());
        }

        RNo=(TextView)findViewById(R.id.tv_RecNo) ;
        Total=(TextView)findViewById(R.id.tv_TotalAmount);
        String Address = GetAddress.GetAddress(getApplicationContext(), GetCurrentLocaion.CurrentLoc);
        user = new User(mPilot.getPilotID(), "", GetCurrentLocaion.CurrentLoc, Address);
        mPresnter = new GetOrdersPresenter(this, getApplicationContext(), user);
        mPresnter.setProducts();
    }

    @Override
    public void setTasks(List<Task> Tasks) {

    }

    @Override
    public void showError(String error) {
        if(error.equalsIgnoreCase(getApplicationContext().getResources().getString(R.string.NoDataFound)))
        {
            ll_table.setVisibility(View.INVISIBLE);
            tv_notFound.setVisibility(View.VISIBLE);
            Total.setText(String.valueOf(0) + " " + getApplicationContext().getResources().getString(R.string.EGP));
            RNo.setText(String.valueOf(0));
        }
        else
        Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setProducts(List<Product> products) {
        double Amount = 0;
        int N_RNO=0;
        if(products!=null&&products.size()>0) {
             Amount = 0;
             N_RNO = products.size();
            for (int i = 0; i < products.size(); i++) {
                Amount += (Double.valueOf(products.get(i).getQUNTITY_REPLACED() )*Double.valueOf( products.get(i).getUNIT_PRICE()));
                TableRow inflate = new TableRow(this);
                inflate = (TableRow) View.inflate(this, R.layout.item_today_details, null);
                tableLayout.addView(inflate);
                SetRow(i + 1, products.get(i));
                // add tabrow

            }

        }
        else
        {
            ll_table.setVisibility(View.INVISIBLE);
            tv_notFound.setVisibility(View.VISIBLE);

        }
        Total.setText(String.valueOf(Amount) + " " + getApplicationContext().getResources().getString(R.string.EGP));
        RNo.setText(String.valueOf(N_RNO));
    }

    @Override
    public void setCoverProducts(List<Product> products) {

    }

    private void SetRow(int i, Product product) {
        View child = tableLayout.getChildAt(i);
        if (child instanceof TableRow) {
            TableRow row = (TableRow) child;
            LinearLayout linearLayout_0 = (LinearLayout) row.getChildAt(0);
            TextView v=(TextView) linearLayout_0.getChildAt(0);
         //   v.setText(product.getRNo());
            v=(TextView) linearLayout_0.getChildAt(1);
            v.setText(String.valueOf(Double.valueOf(product.getQUNTITY_REPLACED())*Double.valueOf(product.getUNIT_PRICE())));
        }
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
    public void ShowLoading() {

    }

    @Override
    public void StopLoading() {

    }

    @Override
    public void setCollection(List<Collection> products) {

    }
}
