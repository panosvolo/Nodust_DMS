package com.mts.mea.nodust_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mts.mea.nodust_app.R;

import java.util.List;

/**
 * Created by Mahmoud on 11/9/2017.
 */

public class CloseCodeAdapter extends ArrayAdapter {
    private Context context;
    private List<String> dropModelList;
    public CloseCodeAdapter(Context context, int resource, List <String>dropModelList) {
        super(context, resource);
        this.dropModelList = dropModelList;
        this.context = context;
    }
    @Override
    public int getCount() {
        return dropModelList.size();
    }
    @Override
    public String getItem(int position) {
        return dropModelList.get(position);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.drop_list_item, parent, false);
        TextView label = (TextView) convertView.findViewById(R.id.tv_lblcloseCode);
        label.setText(dropModelList.get(position));
        label.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
        return convertView;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.drop_list_item, parent, false);

        TextView label = (TextView) convertView.findViewById(R.id.tv_lblcloseCode);
        label.setTextColor(context.getResources().getColor(R.color.colorAccent));
        label.setText(dropModelList.get(position));

        return convertView;
    }
}
