package com.mts.mea.nodust_app.Vacation;

import com.mts.mea.nodust_app.Interfaces.IServerResponse;

import org.json.JSONException;

import java.util.List;


public interface IContractVacation {
    public interface View{
        public void ShowMsg(String msg);
        public void InsertVacation(VacationRequest obj);
        public  void InsertVaction(List<VacationRequest>lst);
        public  void InsertVactionReason(List<vacation_reasons>lst);
        public void ShowDialog();
        public void HideDialog();
    };
    public interface Presenter{
        public void SaveVacation(VacationRequest obj) throws JSONException;
        public void GetVactions() throws JSONException;
        public void GetVacationReason()throws JSONException;

    };
    public interface Model{
        public void SaveVacation(IServerResponse iServerResponse,VacationRequest obj) throws JSONException;
        public void GetVacations(IServerResponse iServerResponse)throws JSONException;
        public void GetVacationReasons(IServerResponse iServerResponse)throws JSONException;
    };
}
