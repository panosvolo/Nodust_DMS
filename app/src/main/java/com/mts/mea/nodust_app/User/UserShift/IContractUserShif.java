package com.mts.mea.nodust_app.User.UserShift;

import com.mts.mea.nodust_app.Evalution.KPI;
import com.mts.mea.nodust_app.Interfaces.IServerResponse;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Mahmoud on 11/19/2017.
 */

public interface IContractUserShif {
    public interface View{
        public void ShowMsg(String msg);
        public void Startbtn(String ShiftId);
        public void ShowLoading();
        public void HideLoading();
        public void Endbtn();
        public void disableCheck();
        public void disableCheckReturn();
    };
    public interface Presenter{
        public void StartShift();
        public void EndShift(String ShiftID);
        public void SetAttendance(String PilotID);
        public void SetReturnTime(String PilotID);
        public void SetEvalution(List<KPI> Evalution,String Notes);
        public void SetOutputDayDriver(String TotalPrice,String TotalRecNo,String AssignID);
        public void SetOutputDayPilot(String TotalPrice,String TotalRecNo,String AssignID);
    };
    public interface Model{
        public void StartShift(IServerResponse iServerResponse);
        public void EndShift(IServerResponse iServerResponse,String ShiftID);
        public void SetAttendance(IServerResponse iServerResponse,String PilotID);
        public void SetEvalution(IServerResponse iServerResponse,List<KPI> Evalution,String Notes) throws JSONException;
        public void SetReturnTime(IServerResponse iServerResponse,String PilotID);
        public void SetOutputDayDriver(IServerResponse iServerResponse,String TotalPrice,String TotalRecNo,String PilotID);
        public void SetOutputDayPilot(IServerResponse iServerResponse,String TotalPrice,String TotalRecNo,String PilotID);

    };
}
