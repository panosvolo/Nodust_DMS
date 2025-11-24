package com.mts.mea.nodust_app.DashBoard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.mts.mea.nodust_app.Interfaces.ServerManager;
import com.mts.mea.nodust_app.Pilots.Pilot;
import com.mts.mea.nodust_app.Pilots.PilotCover.Pilot_Cover;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.User.UserInfo;
import com.mts.mea.nodust_app.common.Constants;
import com.mts.mea.nodust_app.common.DataBaseHelper;
import com.mts.mea.nodust_app.orders.Task;

import java.util.List;

public class new_DashBoard extends Fragment {
    // this dashboard to pilot
    TextView tv_namePilot,tv_NOAssignedContracts,tv_NoNotDone,tv_NoPending,tv_AmountMoney,tv_NODone,tv_NoPartial;
    TableLayout tab_product;
    Pilot mPilot;
    DataBaseHelper DB;
    double total_amount;
    LinearLayout ll_amount;
    double total_finacial_amount=0;
    public new_DashBoard() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_new__dash_board, container, false);
        tv_namePilot=(TextView)v.findViewById(R.id.tv_pilotName);
        tv_NOAssignedContracts=(TextView)v.findViewById(R.id.tv_noAssig);
        tv_NODone=(TextView)v.findViewById(R.id.tv_noDone);
        tv_NoNotDone=(TextView)v.findViewById(R.id.tv_noNotDone);
        tv_NoPending=(TextView)v.findViewById(R.id.tv_noPending);
        tv_NoPartial=(TextView)v.findViewById(R.id.tv_noPartial);
        tv_AmountMoney=(TextView)v.findViewById(R.id.tv_amountMoney);
        tab_product=(TableLayout)v.findViewById(R.id.tab_product);
        mPilot=(Pilot)getArguments().get("Pilot");
        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(Constants.My_Prefs_language_NoDust, 0);
        if (sharedPreferences.contains("SelectedLanguage")) {
            if(sharedPreferences.getString("SelectedLanguage","En").equalsIgnoreCase("English")) {

                tv_namePilot.setText(mPilot.getNameEn());
            }
            else
            {
                tv_namePilot.setText(mPilot.getNameAr());
            }
        }
        else
        {
            tv_namePilot.setText(mPilot.getNameEn());
        }
        ll_amount=(LinearLayout)v.findViewById(R.id.ll_amount);
        DB=new DataBaseHelper(getContext());
        INTUI();
        return v;
    }

    private void INTUI() {
        SharedPreferences sharedPreferences2=getActivity().getApplicationContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        UserInfo userInfo = ServerManager.deSerializeStringToObject(sharedPreferences2.getString("UserInfo", ""), UserInfo.class);
        // Get Tasks ,Get Products
        total_finacial_amount=0;
        GetPilotTasks();
        if(userInfo.getUSERGROUP_ID()==2)
        {
            ll_amount.setVisibility(View.VISIBLE);
            tab_product.setVisibility(View.VISIBLE);
            GetPilotProducts();
        }
        else
        {
            ll_amount.setVisibility(View.VISIBLE);
            tab_product.setVisibility(View.VISIBLE);
            GetPilotProducts();
        }
    }

    private void GetPilotProducts() {
        List<Pilot_Cover>Products=DB.GetCoverPilot(mPilot.getPilotID());
         total_amount=0;
        if(Products!=null&&Products.size()>0)
        {
            for(int i=0;i<Products.size();i++)
            {
                    AddRowProduct(Products.get(i));
            }

        }
        else
        {
            tab_product.setVisibility(View.GONE);
        }
        double final_total=DB.GetTotalPilot(mPilot.getPilotID());
        tv_AmountMoney.setText(String.valueOf(final_total)+" "+getActivity().getApplicationContext().getResources().getString(R.string.EGP));
    }

    private void AddRowProduct(Pilot_Cover product) {
        TableRow inflate = new TableRow(getActivity());
        inflate = (TableRow) View.inflate(getActivity(), R.layout.row_product_dashboard, null);
        tab_product.addView(inflate);
        LinearLayout linearLayout_0 = (LinearLayout) inflate.getChildAt(0);
        TextView ProductName=(TextView) linearLayout_0.getChildAt(0);
        TextView QuantityClean=(TextView) linearLayout_0.getChildAt(1);
        TextView QuatityDirty=(TextView)linearLayout_0.getChildAt(2);
        TextView Treatment=(TextView)linearLayout_0.getChildAt(3);
        Treatment.setText(product.getTreatment_Name());
        ProductName.setText(product.getName());
        int Quantity_prod=0;
        int Quantity_Replaced=0;
         Quantity_prod=Integer.valueOf(product.getQuantity());
            Quantity_Replaced=Integer.valueOf(product.getCurrent_Quantity());
        QuatityDirty.setText(String.valueOf(product.getDirty_Qty()));
        int QR=Quantity_Replaced;
        if(QR<0)
            QR=0;
        QuantityClean.setText(String.valueOf(QR));
        double Price=0;
     //   if(product.getBUY_PRICE()!=null)
           // Price=Double.valueOf(product.getBUY_PRICE().replace("'",""));
     //   total_amount+=(Math.abs(Quantity_Replaced)*Price);

    }

    private void GetPilotTasks() {
        List<Task> Tasks=DB.GetALLTasksPilot(mPilot.getPilotID());
        int NO_assignment=0;
        int No_Done=0;
        int No_Partial=0;
        int No_NotDone=0;
        int No_pending=0;
        if(Tasks!=null)
        {
            NO_assignment=Tasks.size();
            for(int i=0;i<Tasks.size();i++)
            {
                if(Tasks.get(i).getStatus()==null)
                {
                    No_pending++;
                }
                else
                {
                    if(Tasks.get(i).getStatus().equalsIgnoreCase("Delivered"))
                    {
                       /* if(Tasks.get(i).getCONTRACT_TYPE()!=null&&Tasks.get(i).getCONTRACT_TYPE().equalsIgnoreCase("collection"))
                        {
                            total_finacial_amount+=Double.valueOf(Tasks.get(i).getFINANCIAL_AMOUNT());
                        }*/
                        No_Done++;
                    }
                    else if(Tasks.get(i).getStatus().equalsIgnoreCase("Not Delivered"))
                    {
                        No_NotDone++;
                    }
                    else if(Tasks.get(i).getStatus().equalsIgnoreCase("Partially Delivered"))
                    {
                        No_Partial++;
                    }
                }

            }
        }
        tv_NOAssignedContracts.setText(String.valueOf(NO_assignment));
        tv_NoPending.setText(String.valueOf(No_pending));
        tv_NoNotDone.setText(String.valueOf(No_NotDone));
        tv_NODone.setText(String.valueOf(No_Done));
        tv_NoPartial.setText(String.valueOf(No_Partial));


    }


}
