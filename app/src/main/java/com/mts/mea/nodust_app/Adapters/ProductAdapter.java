package com.mts.mea.nodust_app.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.common.DataBaseHelper;
import com.mts.mea.nodust_app.orders.GetOrders.OrdersFragment;
import com.mts.mea.nodust_app.products.Product;

import java.util.List;
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private  static List<Product> mProducts;
    Context mContext;
    DataBaseHelper DB;
    OrdersFragment mOrdersFragment;
    int count=0;
    public ProductAdapter(Context context, List<Product>products, OrdersFragment ordersFragment)
    {   DB=new DataBaseHelper(context);
        mContext=context;
        mProducts=products;
        mOrdersFragment=ordersFragment;
        String UserName="";
       /* SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.MY_PREFS_NAME, 0);
        if (sharedPreferences.contains("UserName"))
        {
            UserName=sharedPreferences.getString("UserName", "");
        }
        String Address= GetAddress.GetAddress(mContext, GetCurrentLocaion.CurrentLoc);
        User user=new User(UserName,"",GetCurrentLocaion.CurrentLoc,Address);*/

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {
        holder.tv_ProdName.setText(mProducts.get(position).getNAME());
        holder.tv_Quantity.setText(String.valueOf(mProducts.get(position).getQUNTITY()));
        holder.tv_Treatment.setText(String.valueOf(mProducts.get(position).getTreatment_description()));
    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ProdName,tv_Quantity,tv_Treatment;
        CheckBox found;
        public ViewHolder(final View itemView) {
            super(itemView);
            tv_ProdName=(TextView)itemView.findViewById(R.id.tv_prodName);
            tv_Quantity=(TextView)itemView.findViewById(R.id.tv_prodQuantity);
            found=(CheckBox)itemView.findViewById(R.id.ch_prodFound);
            tv_Treatment=(TextView)itemView.findViewById(R.id.tv_Treatment);
            found.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(mOrdersFragment!=null) {
                       // mOrdersFragment.btn_shift.setVisibility(View.GONE);
                        if (!isChecked) {
                            count++;
                        } else {
                            if (count > 0)
                                count--;
                        }
                        if (count == 0) {
                           // mOrdersFragment.btn_shift.setText(mContext.getResources().getString(R.string.Accept));
                        } else if (count > 0) {
                           // mOrdersFragment.btn_shift.setText(mContext.getResources().getString(R.string.Refuse));

                        }
                    }
                }
            });

        }


    }




}
