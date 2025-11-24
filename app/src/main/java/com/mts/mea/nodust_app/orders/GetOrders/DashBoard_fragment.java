package com.mts.mea.nodust_app.orders.GetOrders;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mts.mea.nodust_app.Adapters.ExpandableListAdapter;
import com.mts.mea.nodust_app.BackgroundServices.GetCurrentLocaion;
import com.mts.mea.nodust_app.Pilots.Pilot;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.User.UserInfo;
import com.mts.mea.nodust_app.User.UserShift.IContractUserShif;
import com.mts.mea.nodust_app.common.Constants;
import com.mts.mea.nodust_app.common.DataBaseHelper;
import com.mts.mea.nodust_app.common.GetAddress;
import com.mts.mea.nodust_app.products.Product;
import com.mts.mea.nodust_app.orders.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashBoard_fragment extends Fragment {
    ExpandableListView expandableListView;
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    DataBaseHelper DB;
    TextView tv_noPilots,tv_myOrders,tv_PilotOrders;
    IContractUserShif.Presenter userShiftPresenter;
    UserInfo userInfo;
    LinearLayout ll_dash;
    public DashBoard_fragment() {
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
        View v= inflater.inflate(R.layout.fragment_dash_board_fragment, container, false);
        tv_myOrders=(TextView)v.findViewById(R.id.tv_myOrders);
        tv_noPilots=(TextView)v.findViewById(R.id.tv_noPilots);
        tv_PilotOrders=(TextView)v.findViewById(R.id.tv_pOrders);
        //  btn_shift=(Button)v.findViewById(R.id.btn_UserShift);
        expandableListView=(ExpandableListView)v.findViewById(R.id.exp_lst);
        ll_dash=(LinearLayout)v.findViewById(R.id.ll_dash);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        String UserName="";
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        if (sharedPreferences.contains("UserName")) {
            UserName = sharedPreferences.getString("UserName", "");
        }
        String Address = GetAddress.GetAddress(getContext(), GetCurrentLocaion.CurrentLoc);
        /*User user = new User(UserName, "", GetCurrentLocaion.CurrentLoc, Address);
         userInfo = ServerManager.deSerializeStringToObject(sharedPreferences.getString("UserInfo", ""), UserInfo.class);
        userShiftPresenter = new UserShiftPresenter(this, getContext(), user);*/

        InitUI();
    }

    private  void InitUI()
    {
        DB=new DataBaseHelper(getContext());
        prepareListData();
        //  dashBoard();
    }

    private void dashBoard()
    {
        String UserName="";
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        if (sharedPreferences.contains("UserName"))
        {
            UserName=sharedPreferences.getString("UserName", "");
        }
        int count_myOrders=0;
        int count_pilots_orders=0;
        int count_pilots=0;
        List<PilotInfo> delivered=new ArrayList<>();
        List<PilotInfo> Partial=new ArrayList<>();
        List<PilotInfo> Notdelivered=new ArrayList<>();
        List<Pilot> pilots=  DB.GetCoverPilots();
        for(int i=0;i<pilots.size();i++) {
            if(!pilots.get(i).getPilotID().equalsIgnoreCase(UserName))
            {
                count_pilots++;
            }
            List<Task>tasks= DB.GetALLTasksPilot(pilots.get(i).getPilotID());
            List<Product>products=DB.GetProductsPilot(pilots.get(i).getPilotID());
            PilotInfo pilot_deliver=new PilotInfo();
            PilotInfo pilot_notDeliver=new PilotInfo();
            PilotInfo pilot_partial=new PilotInfo();

            for(int j=0;j<tasks.size();j++)
            {
                if(tasks.get(j).getPilotID()!=null&&tasks.get(j).getPilotID().equalsIgnoreCase(tasks.get(j).getDriveID()))
                {
                    count_myOrders++;
                }
                else if(tasks.get(j).getPilotID()!=null&&!tasks.get(j).getPilotID().equalsIgnoreCase(tasks.get(j).getDriveID()))
                {
                    count_pilots_orders++;
                }

                if(tasks.get(j).getStatus()!=null&&tasks.get(j).getStatus().equalsIgnoreCase("Delivered"))
                {
                    for(int l=0;l<products.size();l++)
                    {
                        if(products.get(l).getAID().equalsIgnoreCase(tasks.get(j).getAID()))
                        {
                            if(products.get(l).getQUNTITY_REPLACED()!=null) {
                                if(products.get(l).getQUNTITY_REPLACED().equalsIgnoreCase("0"))
                                    products.get(l).setQUNTITY_REPLACED("1");
                                int Quantity_Replaced = Math.abs(Integer.valueOf(products.get(l).getQUNTITY_REPLACED()));
                                double Price = 0;
                                if (products.get(l).getBUY_PRICE() != null)
                                    Price = Double.valueOf(products.get(l).getBUY_PRICE().replace("'", ""));
                                pilot_deliver.Amount += (Price*Quantity_Replaced);
                            }
                        }
                    }

                    pilot_deliver.orders++;
                    if(j==tasks.size()-1)
                    {
                        pilot_deliver.ID=pilots.get(i).getPilotID();
                        delivered.add(pilot_deliver);
                    }

                }
                else if(tasks.get(j).getStatus()!=null&&tasks.get(j).getStatus().equalsIgnoreCase("Not Delivered"))
                {
                    for(int l=0;l<products.size();l++)
                    {
                        if(products.get(l).getAID().equalsIgnoreCase(tasks.get(j).getAID()))
                        {
                            if(products.get(l).getQUNTITY_REPLACED()!=null) {
                                if(products.get(l).getQUNTITY_REPLACED().equalsIgnoreCase("0"))
                                    products.get(l).setQUNTITY_REPLACED("1");
                                int Quantity_Replaced = Math.abs(Integer.valueOf(products.get(l).getQUNTITY_REPLACED()));
                                double Price = 0;
                                if (products.get(l).getBUY_PRICE() != null)
                                    Price = Double.valueOf(products.get(l).getBUY_PRICE().replace("'", ""));
                                pilot_notDeliver.Amount += (Price*Quantity_Replaced);
                            }
                        }
                    }

                    pilot_notDeliver.orders++;
                    if(j==tasks.size()-1)
                    {
                        pilot_notDeliver.ID=pilots.get(i).getPilotID();
                        Notdelivered.add(pilot_notDeliver);
                    }
                }
                else if(tasks.get(j).getStatus()!=null&&tasks.get(j).getStatus().equalsIgnoreCase("Partially Delivered"))
                {
                    for(int l=0;l<products.size();l++)
                    {
                        if(products.get(l).getAID().equalsIgnoreCase(tasks.get(j).getAID()))
                        {
                            if(products.get(l).getQUNTITY_REPLACED()!=null) {
                                if(products.get(l).getQUNTITY_REPLACED().equalsIgnoreCase("0"))
                                    products.get(l).setQUNTITY_REPLACED("1");
                                int Quantity_Replaced = Math.abs(Integer.valueOf(products.get(l).getQUNTITY_REPLACED()));
                                double Price = 0;
                                if (products.get(l).getBUY_PRICE() != null)
                                    Price = Double.valueOf(products.get(l).getBUY_PRICE().replace("'", ""));
                                pilot_partial.Amount += (Price*Quantity_Replaced);
                            }
                        }
                    }

                    pilot_partial.orders++;
                    if(j==tasks.size()-1)
                    {
                        pilot_partial.ID=pilots.get(i).getPilotID();
                        Partial.add(pilot_partial);
                    }
                }
            }
        }

        // list headers,items
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        listDataHeader.add(getContext().getResources().getString(R.string.Deliver)+"-->"+String.valueOf(delivered.size()));
        listDataHeader.add(getContext().getResources().getString(R.string.PartialDeliver)+"-->"+String.valueOf(Partial.size()));
        listDataHeader.add(getContext().getResources().getString(R.string.NotDeliver)+"-->"+String.valueOf(Notdelivered.size()));
        // list child
        List<String>data_delivered=new ArrayList<>();
        List<String>data_Notdelivered=new ArrayList<>();
        List<String>data_partial=new ArrayList<>();
        for(int i=0;i<delivered.size();i++)
            data_delivered.add(delivered.get(i).ID+"/"+delivered.get(i).orders+"/"+delivered.get(i).Amount);
        for(int i=0;i<Partial.size();i++)
            data_partial.add(Partial.get(i).ID+"/"+Partial.get(i).orders+"/"+Partial.get(i).Amount);
        for(int i=0;i<Notdelivered.size();i++)
            data_Notdelivered.add(Notdelivered.get(i).ID+"/"+Notdelivered.get(i).orders+"/"+Notdelivered.get(i).Amount);
        tv_myOrders.setText(String.valueOf(count_myOrders));
        tv_PilotOrders.setText(String.valueOf(count_pilots_orders));
        tv_noPilots.setText(String.valueOf(count_pilots));
        listDataChild.put(listDataHeader.get(0), data_delivered); // Header, Child data
        listDataChild.put(listDataHeader.get(1), data_partial);
        listDataChild.put(listDataHeader.get(2), data_Notdelivered);
        listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);

        // setting list adapter
        expandableListView.setAdapter(listAdapter);
    }

    class PilotInfo{
        public String ID;
        public  int orders;
        public double Amount;
        public double CreditAmount;
        public PilotInfo()
        {
            orders=0;
            Amount=0;
            CreditAmount=0;
        }
    };
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        List<Task> Tasks=DB.GetTasks();
        List<String> DeliverTask=new ArrayList<>();
        List<String> NotDeliver=new ArrayList<>();
        List<String> PartialDeliver=new ArrayList<>();
        List<PilotInfo> pilot_Deliver=new ArrayList<>();
        List<PilotInfo> pilot_NotDeliver=new ArrayList<>();
        List<PilotInfo> pilot_Partial=new ArrayList<>();
        int count_deliver=0;
        int count_notDeliver=0;
        int count_partial=0;
        int count_pilot_orders=0;
        int count_driver_orders=0;
        int noPilots=0;
        List<Pilot> pilots=DB.GetAllCoverPilots();
        if(pilots!=null)
            noPilots=pilots.size();
        if(Tasks!=null&&Tasks.size()>0)
        {
            for(int i=0;i<Tasks.size();i++)
            {
                if(Tasks.get(i).getPilotID()!=null&&Tasks.get(i).getPilotID().equalsIgnoreCase(Tasks.get(i).getDriveID()))
                {
                    count_driver_orders++;
                }
                else if(Tasks.get(i).getPilotID()!=null)
                {
                    count_pilot_orders++;
                }
                if(Tasks.get(i).getStatus()!=null&&Tasks.get(i).getStatus().equalsIgnoreCase("Partially Delivered"))
                {
                    count_partial++;

                    if(PartialDeliver.contains(Tasks.get(i).getPilotID())) {

                        int indx = PartialDeliver.indexOf(Tasks.get(i).getPilotID());
                        pilot_Partial.get(indx).CreditAmount+=0;
                        pilot_Partial.get(indx).Amount+=Tasks.get(i).getACTUAL_PAID();
                        pilot_Partial.get(indx).orders++;
                        Log.d("PartialAmount", String.valueOf(pilot_Partial.get(indx).Amount));
                        Log.d("Orders", String.valueOf( pilot_Partial.get(indx).orders));


                        // List<Product> p = DB.GetProduct(Tasks.get(i).getAID());
                      /*  for (int l = 0; l < p.size(); l++) {
                            if(p.get(l).getBUY_PRICE()==null)
                                p.get(l).setBUY_PRICE("0");
                            if(p.get(l).getType()!=null&&p.get(l).getType().equalsIgnoreCase("4"))
                            {
                                if (p.get(l).getUNIT_PRICE()!=null&&Double.valueOf(p.get(l).getUNIT_PRICE()) < 0) {
                                    pilot_Partial.get(indx).Amount += 0;
                                    if(p.get(l).getPackage_NO()!=null&&!p.get(l).getPackage_NO().equalsIgnoreCase("0"))
                                    {
                                        pilot_Partial.get(indx).CreditAmount += GetAmountPackage(p.get(l));

                                    }
                                    else {
                                        pilot_Partial.get(indx).CreditAmount += Math.abs(Double.valueOf(p.get(l).getUNIT_PRICE()));
                                    }
                                } else {
                                    if(p.get(l).getPackage_NO()!=null&&!p.get(l).getPackage_NO().equalsIgnoreCase("0"))
                                    {
                                        pilot_Partial.get(indx).Amount += GetAmountPackage(p.get(l));

                                    }
                                        else
                                    pilot_Partial.get(indx).Amount +=  Math.abs(Double.valueOf(p.get(l).getUNIT_PRICE()));
                                    pilot_Partial.get(indx).CreditAmount += 0;
                                }
                            }
                            else {

                                if (p.get(l).getQUNTITY_REPLACED() == null)
                                    p.get(l).setQUNTITY_REPLACED("0");
                                if (Double.valueOf(p.get(l).getBUY_PRICE()) < 0) {
                                    pilot_Partial.get(indx).Amount += 0;
                                    if (p.get(l).getPackage_NO() != null && !p.get(l).getPackage_NO().equalsIgnoreCase("0")) {
                                        pilot_Partial.get(indx).CreditAmount += GetAmountPackage(p.get(l));

                                    } else {
                                        if(p.get(l).getQUNTITY_REPLACED().equalsIgnoreCase("0"))
                                            p.get(l).setQUNTITY_REPLACED("1");
                                        pilot_Partial.get(indx).CreditAmount += (Math.abs(Double.valueOf(p.get(l).getQUNTITY_REPLACED())) * Double.valueOf(p.get(l).getBUY_PRICE()));
                                    }
                                }else {
                                    if(p.get(l).getPackage_NO()!=null&&!p.get(l).getPackage_NO().equalsIgnoreCase("0"))
                                    {
                                        pilot_Partial.get(indx).Amount += GetAmountPackage(p.get(l));

                                    }
                                    else {
                                        if(p.get(l).getQUNTITY_REPLACED().equalsIgnoreCase("0"))
                                            p.get(l).setQUNTITY_REPLACED("1");
                                        pilot_Partial.get(indx).Amount += (Math.abs(Double.valueOf(p.get(l).getQUNTITY_REPLACED())) * Double.valueOf(p.get(l).getBUY_PRICE()));

                                    }pilot_Partial.get(indx).CreditAmount += 0;
                                }

                                // }
                            }
                        }*/
                    }
                    else {
                        PartialDeliver.add(Tasks.get(i).getPilotID());
                        PilotInfo p=new PilotInfo();
                     /*   if(Tasks.get(i).getCONTRACT_TYPE()!=null&&Tasks.get(i).getCONTRACT_TYPE().equalsIgnoreCase("collection"))
                        {
                            if(Tasks.get(i).getFINANCIAL_AMOUNT()!=null)
                            {
                                if(Double.valueOf(Tasks.get(i).getFINANCIAL_AMOUNT())>0)
                                {
                                   p.Amount+=Double.valueOf(Tasks.get(i).getFINANCIAL_AMOUNT());
                                    p.CreditAmount +=0;
                                }
                                else
                                {
                                    p.CreditAmount+=Double.valueOf(Tasks.get(i).getFINANCIAL_AMOUNT());
                                    p.Amount +=0;
                                }
                            }
                        }
                        else {*/
                      /*  List<Product> product = DB.GetProduct(Tasks.get(i).getAID());
                        p.Amount = 0;
                        for (int l = 0; l < product.size(); l++) {

                            if(product.get(l).getBUY_PRICE()==null)
                                product.get(l).setBUY_PRICE("0");
                            if(product.get(l).getType()!=null&&product.get(l).getType().equalsIgnoreCase("4"))
                            {

                                if (Double.valueOf(product.get(l).getUNIT_PRICE()) > 0) {
                                    p.Amount +=  Double.valueOf(product.get(l).getUNIT_PRICE());
                                    p.CreditAmount += 0;
                                } else {
                                    p.Amount += 0;
                                    p.CreditAmount += Math.abs( Double.valueOf(product.get(l).getUNIT_PRICE()));
                                }
                            }
                            else {
                                if (product.get(l).getQUNTITY_REPLACED() == null)
                                    product.get(l).setQUNTITY_REPLACED("0");
                                   if (Double.valueOf(product.get(l).getBUY_PRICE()) > 0) {
                                    if(product.get(l).getPackage_NO()!=null&&!product.get(l).getPackage_NO().equalsIgnoreCase("0"))
                                    {
                                        p.Amount += GetAmountPackage(product.get(l));

                                    }
                                    else {
                                        if(product.get(l).getQUNTITY_REPLACED().equalsIgnoreCase("0"))
                                            product.get(l).setQUNTITY_REPLACED("1");
                                        p.Amount += (Math.abs(Double.valueOf(product.get(l).getQUNTITY_REPLACED())) * Double.valueOf(product.get(l).getBUY_PRICE()));

                                    }p.CreditAmount += 0;
                                } else {
                                       p.Amount += 0;
                                       if (product.get(l).getPackage_NO() != null && !product.get(l).getPackage_NO().equalsIgnoreCase("0")) {
                                           p.CreditAmount += GetAmountPackage(product.get(l));

                                       } else {
                                           if(product.get(l).getQUNTITY_REPLACED().equalsIgnoreCase("0"))
                                               product.get(l).setQUNTITY_REPLACED("1");
                                           p.CreditAmount += (Math.abs(Double.valueOf(product.get(l).getQUNTITY_REPLACED())) * Double.valueOf(product.get(l).getBUY_PRICE()));
                                       }
                                   }
                            }

                            //  }
                        }
                        */p.orders=1;
                        p.Amount=Tasks.get(i).getACTUAL_PAID();
                        p.CreditAmount+=0;
                        p.ID=Tasks.get(i).getPilotID();
                        Log.d("PartialAmountn", String.valueOf(p.Amount));
                        Log.d("Orders3", String.valueOf( p.orders));

                        pilot_Partial.add(p);

                    }


                }
                else if((Tasks.get(i).getStatus()!=null&&Tasks.get(i).getStatus().equalsIgnoreCase("Delivered")))
                {
                    count_deliver++;
                    if(DeliverTask.contains(Tasks.get(i).getPilotID()))
                    {
                        int indx=DeliverTask.indexOf(Tasks.get(i).getPilotID());
                        pilot_Deliver.get(indx).orders+=1;
                        pilot_Deliver.get(indx).CreditAmount+=0;
                        pilot_Deliver.get(indx).Amount+=Tasks.get(i).getACTUAL_PAID();
                    /*    if(Tasks.get(i).getCONTRACT_TYPE()!=null&&Tasks.get(i).getCONTRACT_TYPE().equalsIgnoreCase("collection"))
                        {
                            pilot_Deliver.get(indx).Amount+=Double.valueOf(Tasks.get(i).getFINANCIAL_AMOUNT());
                        }
                        else
                        {*/

                    /*    List<Product> p = DB.GetProduct(Tasks.get(i).getAID());
                        for (int l = 0; l < p.size(); l++) {
                            if(p.get(l).getBUY_PRICE()==null)
                                p.get(l).setBUY_PRICE("0");
                            if(p.get(l).getType()!=null&&p.get(l).getType().equalsIgnoreCase("4"))
                            {
                                if (p.get(l).getUNIT_PRICE()!=null&&Double.valueOf(p.get(l).getUNIT_PRICE()) < 0) {
                                    pilot_Deliver.get(indx).Amount += 0;
                                    pilot_Deliver.get(indx).CreditAmount += Math.abs(Double.valueOf(p.get(l).getUNIT_PRICE()));
                                } else {
                                    pilot_Deliver.get(indx).Amount +=  Math.abs(Double.valueOf(p.get(l).getUNIT_PRICE()));
                                    pilot_Deliver.get(indx).CreditAmount += 0;
                                }
                            }
                            else
                            {
                                if (p.get(l).getQUNTITY_REPLACED() == null)
                                    p.get(l).setQUNTITY_REPLACED("0");
                                if(Double.valueOf(p.get(l).getBUY_PRICE())>0)
                                {
                                    if(p.get(l).getPackage_NO()!=null&&!p.get(l).getPackage_NO().equalsIgnoreCase("0"))
                                    {
                                        pilot_Deliver.get(indx).Amount += GetAmountPackage(p.get(l));

                                    }
                                    else {
                                        if(p.get(l).getQUNTITY_REPLACED().equalsIgnoreCase("0"))
                                            p.get(l).setQUNTITY_REPLACED("1");
                                        pilot_Deliver.get(indx).Amount += (Math.abs(Double.valueOf(p.get(l).getQUNTITY_REPLACED())) * Double.valueOf(p.get(l).getBUY_PRICE()));

                                    } pilot_Deliver.get(indx).CreditAmount+=0;
                                }
                                else {
                                    if(p.get(l).getPackage_NO()!=null&&!p.get(l).getPackage_NO().equalsIgnoreCase("0"))
                                    {
                                        pilot_Deliver.get(indx).CreditAmount += GetAmountPackage(p.get(l));

                                    }

                                    else {
                                        if(p.get(l).getQUNTITY_REPLACED().equalsIgnoreCase("0"))
                                            p.get(l).setQUNTITY_REPLACED("1");
                                        pilot_Deliver.get(indx).CreditAmount += (Math.abs(Double.valueOf(p.get(l).getQUNTITY_REPLACED())) * Double.valueOf(p.get(l).getBUY_PRICE()));

                                    }pilot_Deliver.get(indx).Amount += 0;
                                }
                            }
                            //  }
                        }
*/
                    }
                    else {
                        DeliverTask.add(Tasks.get(i).getPilotID());
                        PilotInfo p=new PilotInfo();
                        p.Amount=Tasks.get(i).getACTUAL_PAID();
                        p.CreditAmount=0;
                    /*    if(Tasks.get(i).getCONTRACT_TYPE()!=null&&Tasks.get(i).getCONTRACT_TYPE().equalsIgnoreCase("collection"))
                        {
                            p.Amount+=Double.valueOf(Tasks.get(i).getFINANCIAL_AMOUNT());
                        }
                        else {*/
                     /*   List<Product> product = DB.GetProduct(Tasks.get(i).getAID());
                        p.Amount = 0;
                        for (int l = 0; l < product.size(); l++) {
                            if(product.get(l).getBUY_PRICE()==null)
                                product.get(l).setBUY_PRICE("0");
                            if(product.get(l).getType()!=null&&product.get(l).getType().equalsIgnoreCase("4"))
                            {

                                if (Double.valueOf(product.get(l).getUNIT_PRICE()) > 0) {
                                    p.Amount +=  Double.valueOf(product.get(l).getUNIT_PRICE());
                                    p.CreditAmount += 0;
                                } else {
                                    p.Amount += 0;
                                    p.CreditAmount += Math.abs( Double.valueOf(product.get(l).getUNIT_PRICE()));
                                }
                            }
                            else {
                                if (product.get(l).getQUNTITY_REPLACED() == null)
                                    product.get(l).setQUNTITY_REPLACED("0");
                                if (Double.valueOf(product.get(l).getBUY_PRICE()) > 0) {
                                    if(product.get(l).getPackage_NO()!=null&&!product.get(l).getPackage_NO().equalsIgnoreCase("0"))
                                    {
                                        p.Amount += GetAmountPackage(product.get(l));

                                    }

                                    else {
                                         if(product.get(l).getQUNTITY_REPLACED().equalsIgnoreCase("0"))
                                            product.get(l).setQUNTITY_REPLACED("1");
                                        p.Amount += (Math.abs(Double.valueOf(product.get(l).getQUNTITY_REPLACED())) * Double.valueOf(product.get(l).getBUY_PRICE()));

                                    }p.CreditAmount += 0;
                                } else {
                                    p.Amount += 0;
                                    if(product.get(l).getPackage_NO()!=null&&!product.get(l).getPackage_NO().equalsIgnoreCase("0"))
                                    {
                                        p.CreditAmount += GetAmountPackage(product.get(l));

                                    }
                                    else
                                    {
                                        if(product.get(l).getQUNTITY_REPLACED().equalsIgnoreCase("0"))
                                            product.get(l).setQUNTITY_REPLACED("1");
                                    p.CreditAmount += (Math.abs(Double.valueOf(product.get(l).getQUNTITY_REPLACED())) * Double.valueOf(product.get(l).getBUY_PRICE()));
                                }}
                            }
                            // }
                        }*/
                        p.orders=1;
                        p.ID=Tasks.get(i).getPilotID();
                        pilot_Deliver.add(p);

                    }
                }
                else if((Tasks.get(i).getStatus()!=null&&Tasks.get(i).getStatus().equalsIgnoreCase("Not Delivered")))
                {
                    count_notDeliver++;
                    if(NotDeliver.contains(Tasks.get(i).getPilotID())) {
                        int indx = NotDeliver.indexOf(Tasks.get(i).getPilotID());
                        pilot_NotDeliver.get(indx).orders += 1;
                        pilot_NotDeliver.get(indx).CreditAmount=0;
                        pilot_NotDeliver.get(indx).Amount+=Tasks.get(i).getACTUAL_PAID();
                     /*   List<Product> p = DB.GetProduct(Tasks.get(i).getAID());
                        for (int l = 0; l < p.size(); l++) {
                            if(p.get(l).getBUY_PRICE()==null)
                                p.get(l).setBUY_PRICE("0");
                            if (p.get(l).getType() != null && p.get(l).getType().equalsIgnoreCase("4")) {
                                if (p.get(l).getUNIT_PRICE() != null && Double.valueOf(p.get(l).getUNIT_PRICE()) < 0) {
                                    pilot_NotDeliver.get(indx).Amount += 0;
                                    pilot_NotDeliver.get(indx).CreditAmount += Math.abs(Double.valueOf(p.get(l).getUNIT_PRICE()));
                                } else {
                                    pilot_NotDeliver.get(indx).Amount += Math.abs(Double.valueOf(p.get(l).getUNIT_PRICE()));
                                    pilot_NotDeliver.get(indx).CreditAmount += 0;
                                }
                            } else {
                                if (p.get(l).getQUNTITY_REPLACED() == null)
                                    p.get(l).setQUNTITY_REPLACED("0");
                                if (Double.valueOf(p.get(l).getBUY_PRICE()) > 0) {
                                    if(p.get(l).getPackage_NO()!=null&&!p.get(l).getPackage_NO().equalsIgnoreCase("0"))
                                    {
                                        pilot_NotDeliver.get(indx).Amount += GetAmountPackage(p.get(l));

                                    }
                                    else {
                                        if(p.get(l).getQUNTITY_REPLACED().equalsIgnoreCase("0"))
                                            p.get(l).setQUNTITY_REPLACED("1");
                                        pilot_NotDeliver.get(indx).Amount += (Math.abs(Double.valueOf(p.get(l).getQUNTITY_REPLACED())) * Double.valueOf(p.get(l).getBUY_PRICE()));

                                    } pilot_NotDeliver.get(indx).CreditAmount += 0;
                                } else {
                                    if(p.get(l).getPackage_NO()!=null&&!p.get(l).getPackage_NO().equalsIgnoreCase("0"))
                                    {
                                        pilot_NotDeliver.get(indx).CreditAmount += GetAmountPackage(p.get(l));

                                    }
                                    else {
                                        if(p.get(l).getQUNTITY_REPLACED().equalsIgnoreCase("0"))
                                            p.get(l).setQUNTITY_REPLACED("1");
                                        pilot_NotDeliver.get(indx).CreditAmount += (Math.abs(Double.valueOf(p.get(l).getQUNTITY_REPLACED())) * Double.valueOf(p.get(l).getBUY_PRICE()));

                                    }   pilot_NotDeliver.get(indx).Amount += 0;
                                }
                            }
                        }*/

                    }
                    else {
                        NotDeliver.add(Tasks.get(i).getPilotID());
                        PilotInfo p=new PilotInfo();
                        p.Amount=Tasks.get(i).getACTUAL_PAID();
                        p.CreditAmount=0;
                      /*  List<Product> product=DB.GetProduct(Tasks.get(i).getAID());
                        p.Amount=0;
                        for(int l=0;l<product.size();l++) {
                            if(product.get(l).getBUY_PRICE()==null)
                                product.get(l).setBUY_PRICE("0");
                            if (product.get(l).getType() != null && product.get(l).getType().equalsIgnoreCase("4")) {

                                if (Double.valueOf(product.get(l).getUNIT_PRICE()) > 0) {
                                    p.Amount += Double.valueOf(product.get(l).getUNIT_PRICE());
                                    p.CreditAmount += 0;
                                } else {
                                    p.Amount += 0;
                                    p.CreditAmount += Math.abs(Double.valueOf(product.get(l).getUNIT_PRICE()));
                                }
                            } else {
                                if (product.get(l).getQUNTITY_REPLACED() == null)
                                    product.get(l).setQUNTITY_REPLACED("0");
                                if (Double.valueOf(product.get(l).getBUY_PRICE()) > 0) {
                                    if(product.get(l).getPackage_NO()!=null&&!product.get(l).getPackage_NO().equalsIgnoreCase("0"))
                                    {
                                        p.Amount += GetAmountPackage(product.get(l));

                                    }
                                    else {
                                        if(product.get(l).getQUNTITY_REPLACED().equalsIgnoreCase("0"))
                                            product.get(l).setQUNTITY_REPLACED("1");
                                        p.Amount += (Math.abs(Double.valueOf(product.get(l).getQUNTITY_REPLACED())) * Double.valueOf(product.get(l).getBUY_PRICE()));

                                    } p.CreditAmount += 0;
                                } else {
                                    if(product.get(l).getPackage_NO()!=null&&!product.get(l).getPackage_NO().equalsIgnoreCase("0"))
                                    {
                                        p.CreditAmount += GetAmountPackage(product.get(l));

                                    }
                                    else {
                                        if(product.get(l).getQUNTITY_REPLACED().equalsIgnoreCase("0"))
                                            product.get(l).setQUNTITY_REPLACED("1");
                                        p.CreditAmount += (Math.abs(Double.valueOf(product.get(l).getQUNTITY_REPLACED())) * Double.valueOf(product.get(l).getBUY_PRICE()));

                                    } p.Amount += 0;
                                }
                            }
                        }
                        */p.orders=1;
                        p.ID=Tasks.get(i).getPilotID();
                        pilot_NotDeliver.add(p);

                    }
                }
            }
        }
        // Adding child data

        listDataHeader.add(getContext().getResources().getString(R.string.Deliver)+"-->"+String.valueOf(count_deliver));
        listDataHeader.add(getContext().getResources().getString(R.string.PartialDeliver)+"-->"+String.valueOf(count_partial));
        listDataHeader.add(getContext().getResources().getString(R.string.NotDeliver)+"-->"+String.valueOf(count_notDeliver));
        DeliverTask=new ArrayList<>();
        PartialDeliver=new ArrayList<>();
        NotDeliver=new ArrayList<>();
        for(int i=0;i<pilot_Deliver.size();i++)
            DeliverTask.add(pilot_Deliver.get(i).ID+"/"+pilot_Deliver.get(i).orders+"/"+pilot_Deliver.get(i).Amount);
        for(int i=0;i<pilot_Partial.size();i++)
            PartialDeliver.add(pilot_Partial.get(i).ID+"/"+pilot_Partial.get(i).orders+"/"+pilot_Partial.get(i).Amount);
        for(int i=0;i<pilot_NotDeliver.size();i++)
            NotDeliver.add(pilot_NotDeliver.get(i).ID+"/"+pilot_NotDeliver.get(i).orders+"/"+pilot_NotDeliver.get(i).Amount);


        tv_myOrders.setText(String.valueOf(count_driver_orders));
        tv_PilotOrders.setText(String.valueOf(count_pilot_orders));
        tv_noPilots.setText(String.valueOf(noPilots));
        listDataChild.put(listDataHeader.get(0), DeliverTask); // Header, Child data
        listDataChild.put(listDataHeader.get(1), PartialDeliver);
        listDataChild.put(listDataHeader.get(2), NotDeliver);
        listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);

        // setting list adapter
        expandableListView.setAdapter(listAdapter);
    }
    private double GetAmountPackage(Product p)
    {
        double res=0;
        if(Integer.valueOf(p.getQUNTITY_REPLACED())==Integer.valueOf(p.getQUNTITY()))
        {
            res=Double.valueOf(p.getBUY_PRICE());
        }
        else if(Double.valueOf(p.getBUY_PRICE())==Double.valueOf(p.getUNIT_PRICE()))
        {
            res=Double.valueOf(p.getBUY_PRICE());
        }
        else
        {
            if(p.getQUNTITY_REPLACED().equalsIgnoreCase("0"))
                p.setQUNTITY_REPLACED("1");
            res=Math.abs(Double.valueOf(p.getQUNTITY_REPLACED())) * Double.valueOf(p.getBUY_PRICE());
        }
        return res;
    }


}
