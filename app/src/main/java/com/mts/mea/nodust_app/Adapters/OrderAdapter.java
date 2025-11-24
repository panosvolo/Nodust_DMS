package com.mts.mea.nodust_app.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mts.mea.nodust_app.BackgroundServices.GetCurrentLocaion;
import com.mts.mea.nodust_app.CloseCodes_Groups.ActionObject;
import com.mts.mea.nodust_app.CloseCodes_Groups.CloseCode;
import com.mts.mea.nodust_app.Interfaces.ServerManager;
import com.mts.mea.nodust_app.Pilots.Pilot;
import com.mts.mea.nodust_app.Pilots.Pilot_Activity;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.User.User;
import com.mts.mea.nodust_app.common.Constants;
import com.mts.mea.nodust_app.common.DataBaseHelper;
import com.mts.mea.nodust_app.common.GetAddress;
import com.mts.mea.nodust_app.orders.GetOrders.HomeWithMenuActivity;
import com.mts.mea.nodust_app.orders.ShowDetailsOrder.CollectionOrderDetails;
import com.mts.mea.nodust_app.orders.ShowDetailsOrder.Routing_map;
import com.mts.mea.nodust_app.products.Product;
import com.mts.mea.nodust_app.orders.SetAssignmentAction.AssigActionPresenter;
import com.mts.mea.nodust_app.orders.SetAssignmentAction.IContractAssignAction;
import com.mts.mea.nodust_app.orders.ShowDetailsOrder.OrderDetailsActivity;
import com.mts.mea.nodust_app.orders.Task;

import org.json.JSONException;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>implements IContractAssignAction.View{
    private  static List<Task> mTasks;
    Context mContext;
    Context context;
    IContractAssignAction.Presenter mPresenter;
    List<CloseCode>mCloseCodes;
    String Contract_status;
    String closeCode;
    EditText b1,b2,b3,b4,b5,b6,b7,b8;
    String AssID="";
    DataBaseHelper DB;
    String mType;
    String UserName="";
    List<String>SelectedTasks;
    boolean flag=false;
    public OrderAdapter(Context context, List<Task>Tasks, IContractAssignAction.Presenter mpresenter,String Type)
    {   DB=new DataBaseHelper(context);
        mContext=context;
        mTasks=Tasks;
        mType=Type;
        UserName="";
        SelectedTasks=new ArrayList<>();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        if (sharedPreferences.contains("UserName"))
        {
            UserName=sharedPreferences.getString("UserName", "");
        }
        String Address= GetAddress.GetAddress(mContext, GetCurrentLocaion.CurrentLoc);
        User user=new User(UserName,"",GetCurrentLocaion.CurrentLoc,Address);
        mPresenter=new AssigActionPresenter(this,mContext,user);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_in_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mTasks.get(position).getLOC_CONFIRMED()!=1)
        {
            holder.btn_routing.setVisibility(View.GONE);
        }
        else
        {
            holder.btn_routing.setVisibility(View.VISIBLE);

        }
        int rep=DB.GetRepetedCard(mTasks.get(position).getCardNo());
        if(rep>1)
        {
            holder.ly_repCard.setVisibility(View.VISIBLE);
            if(mTasks.get(position).getASSIGNMENTS_TYPE()==4)
            {
               holder.tv_textrep.setText(mContext.getResources().getString(R.string.CollectionCard));
            }
            else
            {
                holder.tv_textrep.setText(mContext.getResources().getString(R.string.ProductCard));

            }
            //holder.tx_count.setText(String.valueOf(rep));
        }
        else
        {
            holder.ly_repCard.setVisibility(View.GONE);
        }
        holder.tv_cardno.setText(mTasks.get(position).getCardNo());
        holder.tv_custName.setText(mTasks.get(position).getClientName());
        if(mTasks.get(position).getPRIORITY()!=null)
            holder.tv_priority.setText(mTasks.get(position).getPRIORITY());
        else
            holder.tv_priority.setText("0");

         if(mTasks.get(position).getBonus()!=0)
         {
             holder.lbl_bouns.setVisibility(View.VISIBLE);
             holder.lbl_bouns.setText(String.valueOf(mTasks.get(position).getBonus()+"$"));
         }
        else
         {
             holder.lbl_bouns.setVisibility(View.GONE);
           //  holder.lbl_bouns.setText(String.valueOf(mTasks.get(position).getBonus()+"$"));
         }


        if(mTasks.get(position).getStreet_name()!=null&&mTasks.get(position).getHome_no()!=null)
            holder.tv_streetname.setText(mTasks.get(position).getStreet_name() + " "
                    + mContext.getApplicationContext().getResources().getString(R.string.HOME_NO) + " " + mTasks.get(position).getHome_no());
        else if(mTasks.get(position).getStreet_name()!=null)
            holder.tv_streetname.setText(mTasks.get(position).getStreet_name()) ;
        else if(mTasks.get(position).getHome_no()!=null)
            holder.tv_streetname.setText(mContext.getApplicationContext().getResources().getString(R.string.HOME_NO) + " " + mTasks.get(position).getHome_no());
        else
            holder.tv_streetname.setText(" ");
        if(mTasks.get(position).getFU_Note()!=null) {
         String text=mTasks.get(position).getFU_Note()+"\n";
            if(mTasks.get(position).getFrom_time()!=null)
            {
                text+=mContext.getApplicationContext().getResources().getString(R.string.FromTime)+" "+mTasks.get(position).getFrom_time();
            }
             if(mTasks.get(position).getTo_time()!=null)
            {
                text+=" ";
                text+=mContext.getApplicationContext().getResources().getString(R.string.ToTime)+" "+mTasks.get(position).getTo_time();

            }

            holder.tv_custAdd.setText(text);
        }
        else {
            String text=" ";
            if(mTasks.get(position).getFrom_time()!=null)
            {
                text+=mContext.getApplicationContext().getResources().getString(R.string.FromTime)+" "+mTasks.get(position).getFrom_time();
            }
            if(mTasks.get(position).getTo_time()!=null)
            {
                text+=" ";

                text+=mContext.getApplicationContext().getResources().getString(R.string.ToTime)+" "+mTasks.get(position).getTo_time();

            }

            holder.tv_custAdd.setText(text);
        }

        if(mTasks.get(position).getStatus()!=null)
        {
            holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.TaskNotDeliver));

        }
        else
        {
            holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.solid_white));

        }
        if(mTasks.get(position).getPRIORITY()!=null&&mTasks.get(position).getPRIORITY().equalsIgnoreCase("99"))
        {

            holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.HighestPriority));
        }



        if (!mType.equalsIgnoreCase("tasks")) { // case assign
            if (SelectedTasks.contains(mTasks.get(position).getAID())) {
                AddSelectedTask(mTasks.get(position));
                holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.gold));
                //holder.ll_check.setVisibility(View.VISIBLE);
                //holder.checkBox.setChecked(true);
            } else {
                if(mTasks.get(position).getStatus()!=null)
                {
                    RemoveSelectedTask(mTasks.get(position));
                    holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.TaskNotDeliver));

                }
                else if(mTasks.get(position).getPRIORITY()!=null&&mTasks.get(position).getPRIORITY().equalsIgnoreCase("99"))
                {
                    RemoveSelectedTask(mTasks.get(position));
                    holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.HighestPriority));
                }
                else {
                    RemoveSelectedTask(mTasks.get(position));
                    holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.solid_white));
                    // holder.ll_check.setVisibility(View.GONE);
                }
            }
        }

        if(mType.equalsIgnoreCase("driver_tasks"))
        {
            //  holder.btn_assign.setVisibility(View.VISIBLE);
            holder.btn_deliver.setVisibility(View.GONE);
            holder.btn_notDeliver.setVisibility(View.GONE);
            holder.btn_partial.setVisibility(View.GONE);
        }
        if(!mType.equalsIgnoreCase("tasks"))
        {
            holder.btn_assign.setVisibility(View.GONE);
            // holder.ll_check.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(false);
            holder.btn_deliver.setVisibility(View.GONE);
            holder.btn_notDeliver.setVisibility(View.GONE);
            holder.btn_partial.setVisibility(View.GONE);
        }
        if(UserName.equalsIgnoreCase(mTasks.get(position).getPilotID()))
        {
            holder.btn_deliver.setVisibility(View.GONE);
            holder.btn_notDeliver.setVisibility(View.GONE);
            holder.btn_partial.setVisibility(View.GONE);
        }
        //  holder.tv_AssDate.setText(mTasks.get(position).d());
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }



    @Override
    public void ShowMsg(String Type, String msg) {
        if(!mType.equalsIgnoreCase("driver_tasks"))
        {
            if(!msg.equalsIgnoreCase(mContext.getResources().getString(R.string.erorrDB))) {
                DB.UpdateStatus(AssID,"Not Delivered",0);
                List<String>tmp=new ArrayList<>();
                tmp.add(AssID);
                Clear(tmp);
            }
        }
        else
        {
            if(msg.equalsIgnoreCase(mContext.getResources().getString(R.string.SaveChanges)))
            {
                DB.UpdateStatus(AssID,"Not Delivered",0);
                List<String>tmp=new ArrayList<>();
                tmp.add(AssID);
                Clear(tmp);

            }

        }
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void SendAction() {

    }

    @Override
    public void ShowLoading() {

    }

    @Override
    public void StopLoading() {

    }

    @Override
    public void SendSms() {

    }

    @Override
    public boolean CheckCashing() {
        return false;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // TextView tv_ContractCode,tv_AssDate,tv_assID,tv_areaName,tv_custName;
        TextView tv_cardno,tv_custName,tv_streetname,tv_custAdd,tv_priority,tv_textrep;
        FloatingActionButton btn_partial,btn_deliver,btn_notDeliver,btn_assign,btn_routing;
        CardView cardView;
        LinearLayout ll_check,ly_repCard;
        CheckBox checkBox;
        Button lbl_bouns;
        public ViewHolder(final View itemView) {
            super(itemView);
            tv_cardno=(TextView)itemView.findViewById(R.id.tv_cardno);
            tv_textrep=(TextView)itemView.findViewById(R.id.tv_textrep);
            tv_custName=(TextView)itemView.findViewById(R.id.tv_custName);
            tv_streetname=(TextView)itemView.findViewById(R.id.tv_streetname);
            tv_custAdd=(TextView)itemView.findViewById(R.id.tv_custAdd);
            tv_priority=(TextView)itemView.findViewById(R.id.tv_priority) ;
            btn_deliver=(FloatingActionButton)itemView.findViewById(R.id.btn_deliver);
            btn_notDeliver=(FloatingActionButton)itemView.findViewById(R.id.btn_notDeliver);
            btn_partial=(FloatingActionButton)itemView.findViewById(R.id.btn_partial);
            btn_routing=(FloatingActionButton)itemView.findViewById(R.id.btn_routing);
            btn_assign=(FloatingActionButton)itemView.findViewById(R.id.btn_assign);
            ll_check=(LinearLayout)itemView.findViewById(R.id.ll_check);
            ly_repCard=(LinearLayout)itemView.findViewById(R.id.ly_repCard);

            checkBox=(CheckBox)itemView.findViewById(R.id.ch_pilotFound);
            lbl_bouns=(Button)itemView.findViewById(R.id.lbl_bouns);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked)
                    {
                        AddSelectedTask(mTasks.get(getAdapterPosition()));
                    }
                    else
                    {
                        RemoveSelectedTask(mTasks.get(getAdapterPosition()));
                    }
                }
            });
            btn_assign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // show lst _pilot
                    ShowAvailablePilots(v.getContext(),mTasks.get(getAdapterPosition()).getAssignmentId());
                }
            });
            cardView=(CardView)itemView.findViewById(R.id.card);
            cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    cardView.setCardBackgroundColor(v.getContext().getResources().getColor(R.color.colorAccent));
                    context = v.getContext();
                    int pos = getAdapterPosition();
                   // Toast.makeText(context,""+mTasks.get(getAdapterPosition()).getASSIGNMENTS_TYPE(),Toast.LENGTH_SHORT).show();
                    if(mTasks.get(getAdapterPosition()).getASSIGNMENTS_TYPE()!=4 ) {
                        Intent intent = new Intent(context, OrderDetailsActivity.class);
                        intent.putExtra("Task", (Serializable) mTasks.get(getAdapterPosition()));
                        intent.putExtra("Type", mType);
                        context.startActivity(intent);
                    }
                    else
                    {
                        // collection
                        Intent intent = new Intent(context, CollectionOrderDetails.class);
                        intent.putExtra("Task", (Serializable) mTasks.get(getAdapterPosition()));
                        intent.putExtra("Type", mType);
                        context.startActivity(intent);
                    }
                    return false;
                }
            });
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!mType.equalsIgnoreCase("tasks"))
                    {
                        if(SelectedTasks.contains(mTasks.get(getAdapterPosition()).getAID()))
                        {
                            // checkBox.setChecked(false);
                            RemoveSelectedTask(mTasks.get(getAdapterPosition()));
                            ll_check.setVisibility(View.GONE);
                            cardView.setCardBackgroundColor(v.getContext().getResources().getColor(R.color.solid_white));

                        }
                        else
                        {
                            cardView.setCardBackgroundColor(v.getContext().getResources().getColor(R.color.gold));
                            AddSelectedTask(mTasks.get(getAdapterPosition()));
                            //  ll_check.setVisibility(View.VISIBLE);
                            // checkBox.setChecked(true);
                        }
                        //if(cardView.getCardBackgroundColor().equals(v.getContext().getResources().getColor(R.color.colorAccent)))

                    }
                  /*  context = v.getContext();
                    int pos = getAdapterPosition();
                    Intent intent = new Intent(context, OrderDetailsActivity.class);
                    intent.putExtra("Task", (Serializable) mTasks.get(getAdapterPosition()));
                    intent.putExtra("Type",mType);
                    context.startActivity(intent);*/
                }
            });
            btn_deliver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  Contract_status="Deliver";
                    mCloseCodes=null;
                    GetCloseCode(Constants.Group1_deliver,v.getContext(),mTasks.get(getAdapterPosition()),true);
                    */
                    if(UserName.equalsIgnoreCase(mTasks.get(getAdapterPosition()).getPilotID())) {
                        context = v.getContext();
                        int pos = getAdapterPosition();
                        Intent intent = new Intent(context, OrderDetailsActivity.class);
                        intent.putExtra("Task", (Serializable) mTasks.get(getAdapterPosition()));
                        intent.putExtra("Type", "");

                        context.startActivity(intent);
                    }
                }
            });
            btn_routing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context = v.getContext();
                    int pos = getAdapterPosition();
                    Intent intent = new Intent(context, Routing_map.class);
                    intent.putExtra("Task", (Serializable) mTasks.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
            btn_partial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  Contract_status="PartialDeliver";
                    mCloseCodes=null;
                    GetCloseCode(Constants.Group3_partial,v.getContext(),mTasks.get(getAdapterPosition()),true);*/
                    if(UserName.equalsIgnoreCase(mTasks.get(getAdapterPosition()).getPilotID()))
                    {
                        context = v.getContext();
                        int pos = getAdapterPosition();
                        Intent intent = new Intent(context, OrderDetailsActivity.class);
                        intent.putExtra("Task", (Serializable) mTasks.get(getAdapterPosition()));
                        intent.putExtra("Type", "");
                        context.startActivity(intent);
                    }
                }
            });
            btn_notDeliver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (UserName.equalsIgnoreCase(mTasks.get(getAdapterPosition()).getPilotID())) {
                        context = v.getContext();
                        int pos = getAdapterPosition();
                        Intent intent = new Intent(context, OrderDetailsActivity.class);
                        intent.putExtra("Task", (Serializable) mTasks.get(getAdapterPosition()));
                        intent.putExtra("Type", "");
                        context.startActivity(intent);
                    }
                }
            });


        }
        public void GetCloseCode(String Key,Context context,Task task,boolean status) {
            if(UserName.equalsIgnoreCase(mTasks.get(getAdapterPosition()).getPilotID()))
            {
                String GID=null;
                final SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.MY_PREFS_closeCode, 0);
                if (sharedPreferences.contains(Key)) {
                    String[]tmp=sharedPreferences.getString(Key, "").split("\\*");
                    CloseCode[] arr = ServerManager.deSerializeStringToObject(tmp[1], CloseCode[].class);
                    mCloseCodes = Arrays.asList(arr);
                    GID=tmp[0];
                }
                ShowPoPUp(mCloseCodes,context,task,GID,status);
            }
        }

        public void ShowPoPUp(final List<CloseCode> lst_closeCode, final Context context, final Task task, final String GID, final boolean status) {
            List<String> lst_clos = new ArrayList<>();
            if (mCloseCodes != null && mCloseCodes.size() > 0) {
                for (int i = 0; i < lst_closeCode.size(); i++) {
                    lst_clos.add(lst_closeCode.get(i).getCloseCodeReason());
                }
            }
            final Dialog dialog=new Dialog(context);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.popup_close_code);
            final Spinner spinner = (Spinner) dialog.findViewById(R.id.sp_closeCode);
            // final TextView closeComments = (TextView) dialog.findViewById(R.id.ed_CloseReason);
            final EditText Comments = (EditText) dialog.findViewById(R.id.ed_comments);
            final TextView tv_RNO=(TextView) dialog.findViewById(R.id.tv_RNO);
            final LinearLayout ll_RNO=(LinearLayout)dialog.findViewById(R.id.ly_RNO);
            int year= Calendar.getInstance().get(Calendar.YEAR);
            final int tmp=year-2000;
            b1=(EditText)dialog.findViewById(R.id.btn_button1);
            b1.setText(String.valueOf(tmp));
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context=v.getContext();
                    PopupMenu popupMenu=new PopupMenu(context,b1);
                    popupMenu.inflate(R.menu.menu_r_no);
                    popupMenu.getMenu().getItem(0).setTitle(String.valueOf(tmp));
                    popupMenu.getMenu().getItem(1).setTitle(String.valueOf(tmp+1));
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
            b2=(EditText)dialog.findViewById(R.id.btn_button2);
            b3=(EditText)dialog.findViewById(R.id.btn_button3);
            b3.addTextChangedListener(new Textwatcher(b3));
            b3.requestFocus();
            b4=(EditText)dialog.findViewById(R.id.btn_button4);
            b4.addTextChangedListener(new Textwatcher(b4));
            b5=(EditText)dialog.findViewById(R.id.btn_button5);
            b5.addTextChangedListener(new Textwatcher(b5));
            b6=(EditText)dialog.findViewById(R.id.btn_button6);
            b6.addTextChangedListener(new Textwatcher(b6));
            b7=(EditText)dialog.findViewById(R.id.btn_button7);
            b7.addTextChangedListener(new Textwatcher(b7));
            b8=(EditText)dialog.findViewById(R.id.btn_button8);
            b8.addTextChangedListener(new Textwatcher(b8));

            if(!status)
            {

                ll_RNO.setVisibility(View.GONE);
            }
            else
            {
                ll_RNO.setVisibility(View.VISIBLE);
                tv_RNO.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tv_RNO.setError(null);
                        tv_RNO.clearFocus();
                    }
                });
                // RNO.setText(tmp);
                //   tv_RNO.setHint(getApplicationContext().getResources().getString(R.string.D_r_no)+tmp+"-");
            }
            CloseCodeAdapter Adapter = new CloseCodeAdapter(context, android.R.layout.simple_spinner_dropdown_item, lst_clos);
            spinner.setAdapter(Adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // closeComments.setText(lst_closeCode.get(position).getCloseCodeReason());
                    closeCode=lst_closeCode.get(position).getCloseCodeID();
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
                    boolean Tmpflag=true;
                    if (flag&&mCloseCodes!=null) {
                        String AssignmentID = task.getAID();
                        String CloseCode = closeCode;
                        String CloseCodeReason = spinner.getSelectedItem().toString();
                        String comments = Comments.getText().toString();
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                        String ActionDate = df.format(new Date());
                        ActionObject actionObject=new ActionObject();
                        actionObject.setActionDate(ActionDate);
                        actionObject.setAssignmentID(AssignmentID);
                        actionObject.setCloseCode(CloseCode);
                        actionObject.setCloseCodeReason(CloseCodeReason);
                        actionObject.setComments(comments);
                        actionObject.setLatitude(GetCurrentLocaion.CurrentLoc.getLatitude());
                        actionObject.setLongitude(GetCurrentLocaion.CurrentLoc.getLongitude());
                        actionObject.setContractStatus(GID);
                        actionObject.setDRNo(task.getRecieptNo());
                        actionObject.setTOTAL_PRICE(0.0);
                        actionObject.setCARD_NO(task.getCardNo());
                        actionObject.setDeliveryMan(UserName);
                        String RNO_val="";
                        if(status)
                        {
                            RNO_val=b1.getText().toString()+b2.getText().toString()+b3.getText().toString()+b4.getText().toString()+b5.getText().toString()+b6.getText().toString()+b7.getText().toString()+b8.getText().toString();
                            if(RNO_val.length()!=9)
                            {
                                Tmpflag=false;
                            }
                            else
                            {
                                tv_RNO.setError(null);
                                tv_RNO.clearFocus();
                            }
                        }
                        else
                        {
                            RNO_val="";
                        }
                        actionObject.setDRNo(task.getRecieptNo());
                        actionObject.setTOTAL_PRICE(0.0);
                        actionObject.setReplacedProducts(new ArrayList<Product>());
                        if(Tmpflag) {
                            try {
                                AssID=actionObject.getAssignmentID();
                                mPresenter.setAssignmentAction(actionObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            tv_RNO.requestFocus();
                            tv_RNO.setError(context.getResources().getString(R.string.errRNo));
                        }

                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.msgCloseReason), Toast.LENGTH_SHORT).show();
                    }
                    if(Tmpflag) {
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();

        }
    }

    private void ShowAvailablePilots(Context context,String assignmentId) {
        AssID=assignmentId;
        final Dialog dialog = new Dialog(context);
        //set layout custom
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.lst_pilot_name);
        final RecyclerView recView_pilot = (RecyclerView) dialog.findViewById(R.id.recView_pilot);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recView_pilot.setLayoutManager(mLayoutManager);
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.MY_PREFS_TASks, 0);
        List<Pilot>mPilot=DB.GetCoverPilots();
        if(mPilot!=null&&mPilot.size()>0) {
            PilotAdapter pilotAdapter = new PilotAdapter(this, dialog, context, mPilot, "Assign", null);
            recView_pilot.setAdapter(pilotAdapter);
            dialog.show();
        }
    }
    public  void Clear(List<String> AssID)
    {
        List<Task>NewList=new ArrayList<>();
        if(mTasks!=null&&AssID!=null) {
            for (int i = 0; i < mTasks.size(); i++) {
                if (!AssID.contains(mTasks.get(i).getAID())) {
                    NewList.add(mTasks.get(i));

                }
          /*  if(!mTasks.get(i).getAID().equalsIgnoreCase(AssID))
            {
                NewList.add(mTasks.get(i));
            }*/
            }
            mTasks = NewList;
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.MY_PREFS_TASks, 0);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            if (!mType.equalsIgnoreCase("driver_tasks")) {
                sharedPreferencesEditor.putString("TASKS", ServerManager.serializeObjectToString(mTasks));
            } else
                sharedPreferencesEditor.putString("Driver_TASKS", ServerManager.serializeObjectToString(mTasks));

            sharedPreferencesEditor.apply();

            this.notifyDataSetChanged();
        }
    }
    private  class Textwatcher implements TextWatcher {
        EditText myedit;
        public  Textwatcher(EditText editText)
        {
            myedit=editText;
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
                    b4.requestFocus();

                } else if (myedit.getId() == R.id.btn_button4) {
                    myedit.clearFocus();
                    b5.requestFocus();
                } else if (myedit.getId() == R.id.btn_button5) {
                    myedit.clearFocus();
                    b6.requestFocus();

                } else if (myedit.getId() == R.id.btn_button6) {
                    myedit.clearFocus();
                    b7.requestFocus();
                } else if (myedit.getId() == R.id.btn_button7) {
                    myedit.clearFocus();
                    b8.requestFocus();

                }
            }
            else
            {
                if (myedit.getId() == R.id.btn_button4) {
                    myedit.clearFocus();
                    b3.requestFocus();

                } else if (myedit.getId() == R.id.btn_button5) {
                    myedit.clearFocus();
                    b4.requestFocus();
                } else if (myedit.getId() == R.id.btn_button6) {
                    myedit.clearFocus();
                    b5.requestFocus();

                } else if (myedit.getId() == R.id.btn_button7) {
                    myedit.clearFocus();
                    b6.requestFocus();
                } else if (myedit.getId() == R.id.btn_button8) {
                    myedit.clearFocus();
                    b7.requestFocus();

                }

            }

        }
    }

    public void Search_cardNo(String Query,List<Task>backTasks)
    {
        List<Task>NewTasks=new ArrayList<>();
        for(int i=0;i<backTasks.size();i++)
        {
            if(backTasks.get(i).getCardNo().contains(Query))
            {
                NewTasks.add(backTasks.get(i));
            }
        }
        mTasks=NewTasks;
        this.notifyDataSetChanged();
    }
    public void Search_Address(String Query,List<Task>backTasks)
    {
        List<Task>NewTasks=new ArrayList<>();
        for(int i=0;i<backTasks.size();i++)
        {
          /*  if(backTasks.get(i).getCustomerAddress().contains(Query))
            {
                NewTasks.add(backTasks.get(i));
            }*/
        }
        mTasks=NewTasks;
        this.notifyDataSetChanged();
    }
    public void Search_CustName(String Query,List<Task>backTasks)
    {
        List<Task>NewTasks=new ArrayList<>();
        for(int i=0;i<backTasks.size();i++)
        {
            if(backTasks.get(i).getClientName().contains(Query))
            {
                NewTasks.add(backTasks.get(i));
            }
        }
        mTasks=NewTasks;
        this.notifyDataSetChanged();
    }
    public void Search_Comments(String Query,List<Task>backLst)
    {
        List<Task>NewTasks=new ArrayList<>();
        for(int i=0;i<backLst.size();i++)
        {
            if(backLst.get(i).getOperationComment()!=null&&backLst.get(i).getOperationComment().contains(Query))
            {
                NewTasks.add(backLst.get(i));
            }
        }
        mTasks=NewTasks;
        this.notifyDataSetChanged();
    }
    public void Search(String Query,List<Task>Tasks)
    {
        mTasks=DB.Search(Query.toUpperCase(),Tasks);
        this.notifyDataSetChanged();
    }
    public void SetAllSelection()
    {
        if(flag==false) {
            for (int i = 0; i < mTasks.size(); i++)
                AddSelectedTask(mTasks.get(i));
            flag=true;
        }
        else
        {
            for (int i = 0; i < mTasks.size(); i++)
                RemoveSelectedTask(mTasks.get(i));
            flag=false;
        }

        this.notifyDataSetChanged();
    }
    public void Reset(List<Task>Newtasks)
    {
        mTasks=Newtasks;
        this.notifyDataSetChanged();
    }
    public void AddSelectedTask(Task t) {
        if (!SelectedTasks.contains(t.getAID())) {
            SelectedTasks.add(t.getAID());
            if (mType.equalsIgnoreCase("pilot_tasks")) {
                Pilot_Activity.AssignTasks(SelectedTasks, this);
            } else
                HomeWithMenuActivity.AssignTasks(SelectedTasks, this);

        }
    }
    public void RemoveSelectedTask(Task t)
    {
        if(SelectedTasks.contains(t.getAID()))
        {
            SelectedTasks.remove(t.getAID());
            if(mType.equalsIgnoreCase("pilot_tasks"))
            {
                Pilot_Activity.AssignTasks(SelectedTasks,this);}
            else
                HomeWithMenuActivity.AssignTasks(SelectedTasks,this);

        }
    }

}

