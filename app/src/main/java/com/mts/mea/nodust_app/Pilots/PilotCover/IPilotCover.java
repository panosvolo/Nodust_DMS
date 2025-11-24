package com.mts.mea.nodust_app.Pilots.PilotCover;

import com.mts.mea.nodust_app.Interfaces.IServerResponse;
import com.mts.mea.nodust_app.Pilots.Reconcilation;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Mahmoud on 5/21/2018.
 */

public interface IPilotCover {
    public interface View{
        public void showMsg( String Msg);
        void ShowLoading();
        void StopLoading();
        public void SetCoverPilot(List<Pilot_Cover> lst_cover);
        public void SuccessPilotAccept();
        public void SetCheckCoverPilot(List<Pilot_Cover> lst_cover);
      public   void SuccessPilotReject();
        public void UpdateCoverAfterReconcilation();
        public void SetReconcilationRequests(List<Reconcilation> lst_reconcilation);
        public void RefreshQTYReconcilation();


    };
    public interface Presenter{
        public void AddCoverPilot(String ProductsID,String PilotID);
        public void GetCoverAllPilot();
        public void GetReconcilationRequest();
        public void GetcoverPilot();
        public void Pilotaccept(String AreaID);
        public void CheckProductsPilot();
        public void PilotRejectCover(String AreaID);
        public void PilotReconcilation(List<Pilot_Cover>lst_products,String Assign_ID,String Pilot_Cash,String ExpCach) throws JSONException;
        public void DriverReconcilation(List<Reconcilation>lst_products,boolean Accept,String assign_id,String Pilot_cash,String ExpCach) throws JSONException;

    };
    public interface Model{
        public void AddCoverPilot(String ProductsID,String PilotID,IServerResponse iServerResponse);
        public void SetCoverAllPilot(IServerResponse iServerResponse);
        public void SetReconcilationRequests(IServerResponse iServerResponse);
        public void SetCoverPilot(IServerResponse iServerResponse);
        public void CheckProductsPilot(IServerResponse iServerResponse);
        public void PilotAccept(String AreaID,IServerResponse iServerResponse);
        public void PilotRejectCover(String AreaID,IServerResponse iServerResponse);
        public void PilotReconcilation(List<Pilot_Cover>products,String Assign_ID,String Pilot_Cash,String ExpCach,IServerResponse iServerResponse) throws JSONException;
        public void DriverReconcilation(List<Reconcilation>products,boolean Accept,String Assign_ID,String Pilot_Cash,String ExpCach,IServerResponse iServerResponse) throws JSONException;

    };
}
