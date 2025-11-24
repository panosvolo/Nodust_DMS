package com.mts.mea.nodust_app.CloseCodes_Groups;

import com.mts.mea.nodust_app.Interfaces.IServerResponse;

import java.util.List;

/**
 * Created by Mahmoud on 11/9/2017.
 */

public interface IContractCloseCode_Groups {
    public interface View{
        public void SaveCloseCode(int GroupID,List<CloseCode>lst_closeCode);
        public void SaveCloseCodeGroups(List<CloseCodesGroup>lst_Groups);
        public void ShowError(String error);
    };
    public interface Presenter{
        public void GetCloseCodes(int GroupID);
        public void GetCloseCodeGroups();
    };
    public interface Model{
        public void GetCloseCodes(IServerResponse iServerResponse,int GroupID);
        public void GetcloseCodeGroups(IServerResponse iServerResponse);
    };
}
