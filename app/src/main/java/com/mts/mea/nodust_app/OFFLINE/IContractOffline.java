package com.mts.mea.nodust_app.OFFLINE;

import com.mts.mea.nodust_app.Interfaces.IServerResponse;

import org.json.JSONException;

/**
 * Created by Mahmoud on 9/5/2018.
 */

public interface IContractOffline {
    public interface View{
        public void Failed(OFFLINE offline);
        public void Success(OFFLINE offline);
        public void SuccessALLRequests();
        public void Fail();


    };
    public interface Presenter{
        public void setAssignmentAction(OFFLINE obj) throws JSONException;
        public void SendSMS(OFFLINE obj);
    };
    public interface Model{
        public void setAssignmentAction(OFFLINE obj, IServerResponse iServerResponse) throws JSONException;
        public void SendSMS(OFFLINE obj,IServerResponse iServerResponse) ;
    };
}
