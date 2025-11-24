package com.mts.mea.nodust_app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.Vacation.ShowVacationActivity;
import com.mts.mea.nodust_app.Vacation.VacationRequest;
import com.mts.mea.nodust_app.common.Constants;
import com.mts.mea.nodust_app.common.DataBaseHelper;
import com.mts.mea.nodust_app.orders.GetOrders.OrdersFragment;

import java.io.Serializable;
import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.ViewHolder> {
    private  static List<VacationRequest> Vacations;
    Context mContext;
    DataBaseHelper DB;
    OrdersFragment mOrdersFragment;
    int count=0;
    public VacationAdapter(Context context, List<VacationRequest>products)
    {   DB=new DataBaseHelper(context);
        mContext=context;
        Vacations=products;
      //  mOrdersFragment=ordersFragment;
        String UserName="";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vacation,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VacationAdapter.ViewHolder holder, int position) {
        holder.tv_From.setText(Vacations.get(position).getVacation_from_date());
        holder.tv_to.setText(String.valueOf(Vacations.get(position).getVacation_to_date()));
        final SharedPreferences sharedPreferences_lang = mContext.getSharedPreferences(Constants.My_Prefs_language_NoDust, 0);
        if (sharedPreferences_lang.contains("SelectedLanguage")) {
            if(sharedPreferences_lang.getString("SelectedLanguage","En").equalsIgnoreCase("English"))
            {
                holder.tv_Reason.setText(String.valueOf(Vacations.get(position).getReason_EN()));

            }
            else
            {
                holder.tv_Reason.setText(String.valueOf(Vacations.get(position).getReason_AR()));

            }
        }
        else {
            holder.tv_Reason.setText(String.valueOf(Vacations.get(position).getReason_EN()));

        }
    }

    @Override
    public int getItemCount() {
        return Vacations.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_From,tv_to,tv_Reason;
        CardView cardView;
        public ViewHolder(final View itemView) {
            super(itemView);
            cardView=(CardView)itemView.findViewById(R.id.card) ;
            tv_From=(TextView)itemView.findViewById(R.id.tv_FromDate);
            tv_to=(TextView)itemView.findViewById(R.id.tv_ToDate);
            tv_Reason=(TextView)itemView.findViewById(R.id.tv_Reason);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    int pos = getAdapterPosition();
                    Intent intent = new Intent(context, ShowVacationActivity.class);
                    intent.putExtra("Vacation", (Serializable) Vacations.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });

        }


    }




}

