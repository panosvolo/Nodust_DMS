package com.mts.mea.nodust_app.orders.ShowDetailsOrder;

import android.content.Context;

import com.mts.mea.nodust_app.Application.ConnectivityUtil;
import com.mts.mea.nodust_app.Interfaces.IServerResponse;
import com.mts.mea.nodust_app.Interfaces.ServerManager;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.User.User;
import com.mts.mea.nodust_app.orders.Task;

import java.text.ParseException;

/**
 * Created by Mahmoud on 11/8/2017.
 */

public class OrderDetailsPresenter implements IContractOrderDetail.Presenter {
    IContractOrderDetail.View mView;
    IContractOrderDetail.Model mModel;
    Context mContext;
    String TAG="UpdateTaskPresenter";
    public OrderDetailsPresenter(IContractOrderDetail.View view,Context context,User muser)
    {
        mView=view;
        mModel=new OrderDetailsModel(context,muser);
        mContext=context;

    }

    @Override
    public void getTaskdetails(String TaskID) {
        if(ConnectivityUtil.isOnline(mContext))
        {
            mView.ShowLoading();
            mModel.getTaskDetails(TaskID, new IServerResponse() {
                @Override
                public void success(String response) throws ParseException {
                    Task task= ServerManager.deSerializeStringToObject(response,Task.class);

                    if(task!=null)
                        mView.setTaskdetails(task);
                 mView.StopLoading();
                }

                @Override
                public void failed(String errorText) {
                    mView.showMsg(errorText);
                    mView.StopLoading();
                }
            });
        }
        else
        {
            mView.showError(mContext.getString(R.string.no_internet));
            mView.StopLoading();

        }
    }
}
