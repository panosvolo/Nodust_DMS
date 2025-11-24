package com.mts.mea.nodust_app.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mts.mea.nodust_app.BackgroundServices.GetCurrentLocaion;
import com.mts.mea.nodust_app.Pilots.Pilot;
import com.mts.mea.nodust_app.Pilots.Pilot_Activity;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.User.User;
import com.mts.mea.nodust_app.User.UserShift.IContractUserShif;
import com.mts.mea.nodust_app.User.UserShift.UserShiftPresenter;
import com.mts.mea.nodust_app.common.Constants;
import com.mts.mea.nodust_app.common.DataBaseHelper;
import com.mts.mea.nodust_app.common.GetAddress;
import com.mts.mea.nodust_app.orders.GetOrders.TodayDetailsActivity;
import com.mts.mea.nodust_app.orders.SetAssignmentAction.AssigActionPresenter;
import com.mts.mea.nodust_app.orders.SetAssignmentAction.IContractAssignAction;

import java.io.Serializable;
import java.util.List;

public class PilotAdapter extends RecyclerView.Adapter<PilotAdapter.ViewHolder> implements IContractUserShif.View,IContractAssignAction.View {


    private  static List<Pilot> mPilots;
    Context mContext;
    DataBaseHelper DB;
    SharedPreferences sharedPreferences2;
    IContractUserShif.Presenter mPresenter;
    IContractAssignAction.Presenter presenter;
    CheckBox mfound;
    RatingBar mrating_pilot;
    int count=0;
    String mType;
    Dialog mdialog;
    OrderAdapter mOrderAdapter;
    Pilot mPilot;
    LinearLayout mll_check;
    List<String>SelectTasks;
    String UserName;
    public PilotAdapter(OrderAdapter orderAdapter,Dialog dialog,Context context, List<Pilot>Pilots, String Type,List<String>SelectedTasks)
    {   DB=new DataBaseHelper(context);
        mContext=context;
        mPilots=Pilots;
         UserName="";
        mType=Type;
        mdialog=dialog;
        SelectTasks=SelectedTasks;
        mOrderAdapter=orderAdapter;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        if (sharedPreferences.contains("UserName"))
        {
            UserName=sharedPreferences.getString("UserName", "");
        }
        sharedPreferences2= mContext.getSharedPreferences(Constants.My_Prefs_language_NoDust, 0);
        String Address= GetAddress.GetAddress(mContext, GetCurrentLocaion.CurrentLoc);
        User user=new User(UserName,"",GetCurrentLocaion.CurrentLoc,Address);
        mPresenter=new UserShiftPresenter(PilotAdapter.this,mContext,user);
        presenter=new AssigActionPresenter(this,mContext,user);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pilot,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.rating_pilot.setVisibility(View.GONE);
        if(mPilots.get(position).getPilotID().equalsIgnoreCase(UserName)) {
            holder.tv_lblName.setText(mContext.getResources().getString(R.string.driverName));
            holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.gold));
        }
        else
        {
            holder.tv_lblName.setText(mContext.getResources().getString(R.string.PilotName));
            holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.solid_white));


        }
        if (sharedPreferences2.contains("SelectedLanguage")) {
            if(sharedPreferences2.getString("SelectedLanguage","En").equalsIgnoreCase("English")) {

                holder.tv_PilotName.setText(mPilots.get(position).getPilotID()+" / "+mPilots.get(position).getNameEn());

            }
            else
            {
                holder.tv_PilotName.setText(mPilots.get(position).getPilotID()+" / "+mPilots.get(position).getNameAr());

            }
        }
        else
        {
            holder.tv_PilotName.setText(mPilots.get(position).getPilotID()+" / "+mPilots.get(position).getNameEn());

        }
        if(mType.equalsIgnoreCase("Assign"))
        {
            holder.ll_check.setVisibility(View.GONE);
        }
       /*  else if(mPilots.get(position).getAttendanceTime()!=null)
         {
             Date CurrentDate = new Date();
             DateFormat formatter ;
             Date date ;
             formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

             try {
                 date = (Date)formatter.parse(mPilots.get(position).getAttendanceTime());
                 Calendar cal = Calendar.getInstance();
                 Calendar cal2 = Calendar.getInstance();
                 cal.setTime(date);
                 cal2.setTime(CurrentDate);
                 cal.get(Calendar.DAY_OF_MONTH);
                 if( cal.get(Calendar.DAY_OF_MONTH)==cal2.get(Calendar.DAY_OF_MONTH)&&cal2.get(Calendar.YEAR)==cal.get(Calendar.YEAR)&&cal2.get(Calendar.MONTH)==cal.get(Calendar.MONTH))
                  {
                      holder.ll_check.setVisibility(View.VISIBLE);
                      holder.found.setChecked(true);
                  }

             } catch (ParseException e) {
                 e.printStackTrace();
             }
         }*/
        else {
            holder.ll_check.setVisibility(View.INVISIBLE);
        }
        if(mType.equalsIgnoreCase("TodayDtails"))
        {
            holder.ll_check.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mPilots.size();
    }

    @Override
    public void ShowMsg(String Type,String msg) {

            if(mOrderAdapter!=null) {
                mOrderAdapter.Clear(SelectTasks);
               // SelectTasks=null;
            }
        DB.UpdateAllData(null, false);

        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();

    }
    @Override
    public void ShowMsg(String msg) {

        if(mOrderAdapter!=null) {
            mOrderAdapter.Clear(SelectTasks);
            // SelectTasks=null;
        }
        DB.UpdateAllData(null, false);

        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void SendAction() {

    }

    @Override
    public void ShowLoading() {

    }

    @Override
    public void HideLoading() {

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

    @Override
    public void Startbtn(String ShiftId) {

    }

    @Override
    public void Endbtn() {

    }

    @Override
    public void disableCheck() {
        // set Attendance
        DB.setAttendance(mPilot.getPilotID());
        //mll_check.setVisibility(View.GONE);
        mrating_pilot.setVisibility(View.GONE);
       // show evalution popup
        final Dialog dialog = new Dialog(mContext);
        //set layout custom
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_evalution);
        final CheckBox ch_uniform,ch_hygiene,ch_presentaion,ch_apperance,ch_arrival;
        ch_uniform=(CheckBox)dialog.findViewById(R.id.ch_uniformCode);
        ch_hygiene=(CheckBox)dialog.findViewById(R.id.ch_Hygiene);
        ch_presentaion=(CheckBox)dialog.findViewById(R.id.ch_Presentation);
        ch_apperance=(CheckBox)dialog.findViewById(R.id.ch_Apperance);
        ch_arrival=(CheckBox)dialog.findViewById(R.id.ch_Arrival);
        Button btn_send=(Button)dialog.findViewById(R.id.btn_sendEvalution);
        TextView tv_pilot=(TextView)dialog.findViewById(R.id.tv_pilotName);
        if (sharedPreferences2.contains("SelectedLanguage")) {
            if(sharedPreferences2.getString("SelectedLanguage","En").equalsIgnoreCase("English")) {
                tv_pilot.setText(mContext.getResources().getString(R.string.Evalution)+" "+mPilot.getNameEn());
            }
            else
            {tv_pilot.setText(mContext.getResources().getString(R.string.Evalution)+" "+mPilot.getNameAr());
            }
        }
        else
        {
            tv_pilot.setText(mContext.getResources().getString(R.string.Evalution)+" "+mPilot.getNameEn());
        }
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                if(ch_apperance.isChecked())
                {
                    count++;
                }
                if(ch_arrival.isChecked())
                    count++;
                if(ch_hygiene.isChecked())
                    count++;
                if(ch_presentaion.isChecked())
                    count++;
                if(ch_uniform.isChecked())
                     count++;
               // mPresenter.SetEvalution(count,mPilot.getPilotID());
                dialog.dismiss();
            }
        });
        dialog.show();
        mrating_pilot.setVisibility(View.GONE);
        mrating_pilot.setRating(count);
    }

    @Override
    public void disableCheckReturn() {
        DB.setReturnTime(mPilot.getPilotID());
        mll_check.setVisibility(View.GONE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_PilotName;
        CheckBox found;
        RatingBar rating_pilot;
        CardView cardView;
        LinearLayout ll_check;
        TextView tv_lblName;
        public ViewHolder(final View itemView) {
            super(itemView);
            tv_PilotName = (TextView) itemView.findViewById(R.id.tv_pilotName);
            tv_lblName=(TextView)itemView.findViewById(R.id.lbl_ProdutName) ;
            found = (CheckBox) itemView.findViewById(R.id.ch_pilotFound);
            ll_check=(LinearLayout)itemView.findViewById(R.id.ll_check);

            cardView=(CardView)itemView.findViewById(R.id.card);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // go to list contain list of contract  and products
                    if(mType.equalsIgnoreCase("TodayDtails"))
                    {
                        // view details
                        Intent i = new Intent(mContext, TodayDetailsActivity.class);
                        i.putExtra("mPilot", (Serializable) mPilots.get(getAdapterPosition()));
                        v.getContext().startActivity(i);

                    }
                    else  if(mType.equalsIgnoreCase("Assign"))
                    {
                        if(mdialog!=null) {
                            mdialog.dismiss();
                        }
                        if(SelectTasks!=null&&SelectTasks.size()>0)
                        {
                            ShowAvailablePilots(mContext,SelectTasks,mPilots.get(getAdapterPosition()));
                        }
                    }
                    else {
                        Intent i = new Intent(mContext, Pilot_Activity.class);
                        i.putExtra("mPilot", (Serializable) mPilots.get(getAdapterPosition()));
                        v.getContext().startActivity(i);
                    }

                }
            });
            found.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mfound=found;
                    mPilot=mPilots.get(getAdapterPosition());
                    mrating_pilot=rating_pilot;
                    mll_check=ll_check;
                    if(isChecked) {
                        if(!mType.equalsIgnoreCase("Assign")) {
                            mPresenter.SetAttendance(mPilots.get(getAdapterPosition()).getASSIGN_PILOT_ID());
                        }
                        else
                        {
                            if(mdialog!=null) {
                                mdialog.dismiss();
                            }
                            if(SelectTasks!=null&&SelectTasks.size()>0)
                            {
                                ShowAvailablePilots(mContext,SelectTasks,mPilots.get(getAdapterPosition()));
                            }

                        }
                    }
                    else {
                        mPresenter.SetReturnTime(mPilots.get(getAdapterPosition()).getASSIGN_PILOT_ID());
                        rating_pilot.setVisibility(View.GONE);
                    }
                }
            });
            rating_pilot=(RatingBar)itemView.findViewById(R.id.rating_pilot);
            rating_pilot.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
               // mPresenter.SetEvalution(rating,mPilots.get(getAdapterPosition()).getPilotID());
                }
            });
        }
    }
    private void ShowAvailablePilots(Context context, final List<String> SelectedTasks, final Pilot mPilot) {
        final Dialog dialog = new Dialog(context);
        //set layout custom
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_confirm);
        TextView tv_pilotName=(TextView)dialog.findViewById(R.id.ed_totalAmount);
        TextView tv_NoOrders=(TextView)dialog.findViewById(R.id.ed_totalRecNo);
        Button yes=(Button)dialog.findViewById(R.id.btn_yes);
        Button No=(Button)dialog.findViewById(R.id.btn_no);
        tv_pilotName.setText(mPilot.getNameEn());
        if (sharedPreferences2.contains("SelectedLanguage")) {
            if(sharedPreferences2.getString("SelectedLanguage","En").equalsIgnoreCase("English")) {

                tv_pilotName.setText(mPilot.getNameEn());

            }
            else
            {
                tv_pilotName.setText(mPilot.getNameAr());

            }
        }
        else
        {
            tv_pilotName.setText(mPilot.getNameEn());

        }
        tv_NoOrders.setText(String.valueOf(SelectedTasks.size()));
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                presenter.AssignTask(mPilot.getPilotID(),SelectedTasks);
               // SelectTasks=null;
            }
        });
        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dialog.dismiss();
            }
        });
        dialog.show();

    }


}
