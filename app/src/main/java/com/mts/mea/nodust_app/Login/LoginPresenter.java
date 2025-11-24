package com.mts.mea.nodust_app.Login;

import android.content.Context;

import com.mts.mea.nodust_app.Application.ConnectivityUtil;
import com.mts.mea.nodust_app.Interfaces.IServerResponse;
import com.mts.mea.nodust_app.Interfaces.ServerManager;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.User.User;
import com.mts.mea.nodust_app.User.UserInfo;

import org.json.JSONException;

/**
 * Created by Mahmoud on 11/8/2017.
 */

public class LoginPresenter implements IContractLogin.Presenter {
    IContractLogin.Model mModel;
    IContractLogin.View mView;
    Context mContext;
    public LoginPresenter(Context context, User user, IContractLogin.View view)
    {
        mModel=new LoginModel(user,context);
        mView=view;
        mContext=context;

    }

    @Override
    public void isExistUser(String IMIE) throws JSONException {
        if (ConnectivityUtil.isOnline(mContext)) {
            mView.ShowLoading();
            mModel.validateAccount(IMIE,new IServerResponse() {
                @Override
                public void success(String response)  {
                    try {
                        UserInfo userInfo= ServerManager.deSerializeStringToObject(response,UserInfo.class);
                        mView.StopLoading();
                        mView.goToHome(userInfo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        mView.StopLoading();
                    }
                }

                @Override
                public void failed(String errorText) {
                    mView.StopLoading();
                    mView.showError(errorText);
                }
            });

        }
        else
        {
            mView.showError(mContext.getString(R.string.no_internet));
        }

    }

    @Override
    public void ChangePassword(String NewPass, String OldPass) {
        if (ConnectivityUtil.isOnline(mContext)) {
            mView.ShowLoading();
            mModel.ChangePassword(NewPass,OldPass,new IServerResponse() {
                @Override
                public void success(String response) throws JSONException {
                    mView.SuccessChangePassword();
                    mView.StopLoading();
                }

                @Override
                public void failed(String errorText) {

                    mView.showError(errorText);
                    mView.StopLoading();
                }
            });

        }
        else
        {
            mView.showError(mContext.getString(R.string.no_internet));
        }
    }


}
