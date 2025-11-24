package com.mts.mea.nodust_app.orders.ShowDetailsOrder;

import com.mts.mea.nodust_app.Interfaces.IServerResponse;
import com.mts.mea.nodust_app.orders.Task;

import java.text.ParseException;

/**
 * Created by Mahmoud on 11/8/2017.
 */

public interface IContractOrderDetail {
    public interface View{
        public void setTaskdetails(Task task) throws ParseException;
        public void showError( String errMsg);
        public void showMsg( String errMsg);
        public void ShowLoading();
        public void StopLoading();
    };
    public interface Presenter{
        public void getTaskdetails(String TaskID);};
    public interface Model{
        public void getTaskDetails(String TaskId,IServerResponse iServerResponse);


    };
}
