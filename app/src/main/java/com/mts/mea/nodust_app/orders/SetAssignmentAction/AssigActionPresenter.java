package com.mts.mea.nodust_app.orders.SetAssignmentAction;

import android.content.Context;
import android.content.SharedPreferences;

import com.mts.mea.nodust_app.Application.ConnectivityUtil;
import com.mts.mea.nodust_app.CloseCodes_Groups.ActionObject;
import com.mts.mea.nodust_app.Interfaces.IServerResponse;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.User.User;
import com.mts.mea.nodust_app.common.Constants;

import org.json.JSONException;

import java.util.List;


public class AssigActionPresenter implements IContractAssignAction.Presenter {
    IContractAssignAction.View mView;
    IContractAssignAction.Model mModel;
    Context mcontext;

    public AssigActionPresenter(IContractAssignAction.View view, Context context, User user) {
        mView = view;
        mModel = new AssignActionModel(user, context);
        mcontext = context;
    }

    @Override
    public void setAssignmentAction(ActionObject obj) throws JSONException {
        if (ConnectivityUtil.isOnline(mcontext)) {
            // check queue cashimg first
           // if(mView.CheckCashing()) {
                mView.ShowLoading();
                mModel.setAssignmentAction(obj, new IServerResponse() {
                    @Override
                    public void success(String response) {
                        if (response != null && !response.equalsIgnoreCase("Fail")) {
                            mView.StopLoading();
                            mView.ShowMsg("ACTION", mcontext.getResources().getString(R.string.SaveChanges));
                            mView.SendSms();
                        } else if (response != null && response.equalsIgnoreCase("Fail")) {
                            mView.StopLoading();
                            mView.ShowMsg("ACTION", mcontext.getResources().getString(R.string.SaveChanges) + "fail");

                        }


                    }

                    @Override
                    public void failed(String errorText) {
                        mView.StopLoading();
                        mView.ShowMsg("ACTION", errorText);
                    }
                });
           // }
        }
        else {
            mView.ShowMsg("ACTION",mcontext.getString(R.string.no_internet));
        }
    }

    @Override
    public void AssignTask(String PilotID, final List<String> AssID) {
        if (ConnectivityUtil.isOnline(mcontext)) {
            mModel.AssignTask(PilotID,  AssID,new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        SharedPreferences sharedPreferences = mcontext.getSharedPreferences(Constants.MY_PREFS_TASks, 0);
                        mView.ShowMsg("ASSIGN",mcontext.getResources().getString(R.string.SaveChanges));
                        //mView.SendSms();
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.ShowMsg("ASSIGN",errorText);
                }
            });

        }
        else {
            mView.ShowMsg("ASSIGN",mcontext.getString(R.string.no_internet));
        }
    }

    @Override
    public void SendSMS(String Telno,String AID,String Products_Changed,String Products_NotChanged,String Products_Canceled) {
        if (ConnectivityUtil.isOnline(mcontext)) {
            mModel.SendSMS( Telno, AID, Products_Changed, Products_NotChanged,Products_Canceled,new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        //mView.ShowMsg(mcontext.getResources().getString(R.string.SaveChanges));
                        //mView.SendAction();
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.ShowMsg("SMS",errorText);
                }
            });

        }
        else {
            mView.ShowMsg("SMS",mcontext.getString(R.string.no_internet));
        }
    }


}
