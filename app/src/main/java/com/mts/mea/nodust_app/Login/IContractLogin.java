package com.mts.mea.nodust_app.Login;

import com.mts.mea.nodust_app.Interfaces.IServerResponse;
import com.mts.mea.nodust_app.User.UserInfo;

import org.json.JSONException;

/**
 * Created by Mahmoud on 11/8/2017.
 */

public interface IContractLogin {
    public interface View{
        public void showError( String errMsg);
        public void goToHome(UserInfo userInfo) throws JSONException;
        public void ShowLoading();
        public void StopLoading();
        public void SuccessChangePassword();
    };
    public interface Model{
        public void validateAccount(String IMIE,IServerResponse iServerResponse) throws JSONException;
        public void ChangePassword(String new_Password,String old_Password,IServerResponse iServerResponse) ;

    };
    public interface Presenter{
        public void isExistUser(String IMIE) throws JSONException;
        public void ChangePassword(String new_Password,String old_Password) ;


    };
}
