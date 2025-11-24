package com.mts.mea.nodust_app.orders.SetAssignmentAction;

import com.mts.mea.nodust_app.CloseCodes_Groups.ActionObject;
import com.mts.mea.nodust_app.Interfaces.IServerResponse;

import org.json.JSONException;

import java.util.List;

public interface IContractAssignAction {
    public interface View{
      public void ShowMsg(String Type,String msg);
        public void SendAction();
        public void ShowLoading();
        public  void StopLoading();
        void SendSms();

       public boolean CheckCashing();
    };
    public interface Presenter{
        public void setAssignmentAction(ActionObject obj) throws JSONException;
        public void AssignTask(String PilotID,List<String > AssID);
        public void SendSMS(String Telno,String AID,String Products_Changed,String Products_NotChanged,String Products_Canceled);
    };
    public interface Model{
    public void setAssignmentAction(ActionObject obj, IServerResponse iServerResponse) throws JSONException;
     public void SendSMS(String Telno,String AID,String Products_Changed,String Products_NotChanged,String Products_Canceled,IServerResponse iServerResponse) ;
        public void AssignTask(String PilotID,List<String>AssID,IServerResponse iServerResponse);
    };
}
