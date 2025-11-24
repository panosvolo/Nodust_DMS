package com.mts.mea.nodust_app.User.UserShift;

import android.content.Context;

import com.mts.mea.nodust_app.Application.ConnectivityUtil;
import com.mts.mea.nodust_app.Evalution.KPI;
import com.mts.mea.nodust_app.Interfaces.IServerResponse;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.User.User;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Mahmoud on 11/19/2017.
 */

public class UserShiftPresenter implements IContractUserShif.Presenter {
    IContractUserShif.View mView;
    IContractUserShif.Model mModel;
    Context mcontext;

    public UserShiftPresenter(IContractUserShif.View view, Context context, User user) {
        mView = view;
        mModel = new UserShiftModel(user, context);
        mcontext = context;
    }
    @Override
    public void StartShift() {
        if (ConnectivityUtil.isOnline(mcontext)) {
            mView.ShowLoading();
            mModel.StartShift(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        mView.HideLoading();
                       mView.Startbtn(response);

                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.HideLoading();
                    mView.ShowMsg(errorText);
                }
            });

        }
        else {
            mView.ShowMsg(mcontext.getString(R.string.no_internet));
        }

    }

    @Override
    public void EndShift(String ShiftID) {
        if (ConnectivityUtil.isOnline(mcontext)) {
            mView.ShowLoading();
            mModel.EndShift(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        mView.HideLoading();
                        mView.Endbtn();
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.HideLoading();
                    mView.ShowMsg(errorText);
                }
            },ShiftID);

        }
        else {
            mView.ShowMsg(mcontext.getString(R.string.no_internet));
        }

    }

    @Override
    public void SetAttendance(String PilotId) {
        if (ConnectivityUtil.isOnline(mcontext)) {
            mView.ShowLoading();
            mModel.SetAttendance(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        mView.HideLoading();
                        mView.disableCheck();
                    }
                }

                @Override
                public void failed(String errorText) {
                    mView.HideLoading();
                    mView.ShowMsg(errorText);
                }
            },PilotId);

        }
        else {
            mView.ShowMsg(mcontext.getString(R.string.no_internet));
        }
    }

    @Override
    public void SetReturnTime(String PilotID) {
        if (ConnectivityUtil.isOnline(mcontext)) {
            mView.ShowLoading();
            mModel.SetReturnTime(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        mView.HideLoading();
                        mView.disableCheckReturn();
                    }
                }

                @Override
                public void failed(String errorText) {
                    mView.HideLoading();
                    mView.ShowMsg(errorText);
                }
            },PilotID);

        }
        else {
            mView.ShowMsg(mcontext.getString(R.string.no_internet));
        }
    }

    @Override
    public void SetEvalution(List<KPI> Evalution,String Notes) {
        if (ConnectivityUtil.isOnline(mcontext)) {
            mView.ShowLoading();
            try {
                mModel.SetEvalution(new IServerResponse() {
                    @Override
                    public void success(String response) {
                        if (response != null) {
                            mView.HideLoading();
                           mView.ShowMsg(mcontext.getResources().getString(R.string.SaveChanges)+"Evalution");
                            mView.disableCheck();

                        }
                    }

                    @Override
                    public void failed(String errorText) {
                        mView.HideLoading();
                        mView.ShowMsg(errorText);
                    }
                },Evalution,Notes);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else {
            mView.ShowMsg(mcontext.getString(R.string.no_internet));
        }
    }

    @Override
    public void SetOutputDayDriver(String TotalPrice,String TotalRecNo,String AssignID) {
        if (ConnectivityUtil.isOnline(mcontext)) {
            mView.ShowLoading();
            mModel.SetOutputDayDriver(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        mView.HideLoading();
                        mView.disableCheckReturn();
                    }
                }

                @Override
                public void failed(String errorText) {
                    mView.HideLoading();
                    mView.ShowMsg(errorText);
                }
            },TotalPrice,TotalRecNo,AssignID);

        }
        else {
            mView.ShowMsg(mcontext.getString(R.string.no_internet));
        }
    }

    @Override
    public void SetOutputDayPilot(String TotalPrice, String TotalRecNo, String AssignID) {
        if (ConnectivityUtil.isOnline(mcontext)) {
            mView.ShowLoading();
            mModel.SetOutputDayPilot(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        mView.HideLoading();
                        mView.ShowMsg(mcontext.getResources().getString(R.string.SaveChanges)+"End");
                    }
                }

                @Override
                public void failed(String errorText) {
                    mView.HideLoading();
                    mView.ShowMsg(errorText);
                }
            },TotalPrice,TotalRecNo,AssignID);

        }
        else {
            mView.ShowMsg(mcontext.getString(R.string.no_internet));
        }
    }
}
