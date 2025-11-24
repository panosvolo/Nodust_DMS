package com.mts.mea.nodust_app.Vacation;

import android.content.Context;
import android.util.Log;

import com.mts.mea.nodust_app.Application.ConnectivityUtil;
import com.mts.mea.nodust_app.Interfaces.IServerResponse;
import com.mts.mea.nodust_app.Interfaces.ServerManager;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.User.User;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mahmoud on 4/25/2018.
 */

public class VacationPresenter implements IContractVacation.Presenter {
    IContractVacation.View mView;
    IContractVacation.Model mModel;
    Context mcontext;

    public VacationPresenter(IContractVacation.View view, Context context, User user) {
        mView = view;
        mModel = new VacationModel(user, context);
        mcontext = context;
    }
    @Override
    public void SaveVacation(VacationRequest obj) throws JSONException {

        if (ConnectivityUtil.isOnline(mcontext)) {
            mView.ShowDialog();
            mModel.SaveVacation(new IServerResponse() {
                @Override
                public void success(String response) {
                    mView.HideDialog();
                    if (response != null) {
                        Log.e("VacID",response);

                        VacationRequest vacationRequest= ServerManager.deSerializeStringToObject(response,VacationRequest.class);
                        Log.e("VacID",vacationRequest.getRequesterID());
                        mView.InsertVacation(vacationRequest);
                        // mView.Startbtn(response);
                    }

                }

                @Override
                public void failed(String errorText)
                {
                    mView.HideDialog();
                    mView.ShowMsg(errorText);
                }
            },obj);

        }
        else {
            mView.ShowMsg(mcontext.getString(R.string.no_internet));
        }

    }

    @Override
    public void GetVactions() throws JSONException {
        if (ConnectivityUtil.isOnline(mcontext)) {
            mView.ShowDialog();
            Log.d("Vacation","CallVacation");
            mModel.GetVacations(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        Log.e("VacID",response);

                        VacationRequest[] vacationRequest= ServerManager.deSerializeStringToObject(response,VacationRequest[].class);
                        if(vacationRequest!=null&&vacationRequest.length>=0) {
                            List<VacationRequest> AssignedTasks = new ArrayList<VacationRequest>(vacationRequest.length);
                            AssignedTasks = Arrays.asList(vacationRequest);
                            mView.HideDialog();
                            mView.InsertVaction(AssignedTasks);

                        }

                    }


                }

                @Override
                public void failed(String errorText)
                {
                    mView.HideDialog();
                    mView.ShowMsg(errorText);
                }
            });

        }
        else {
            mView.ShowMsg(mcontext.getString(R.string.no_internet));
        }

    }

    @Override
    public void GetVacationReason() throws JSONException {
        if (ConnectivityUtil.isOnline(mcontext)) {
           // mView.ShowDialog();
            mModel.GetVacationReasons(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        Log.e("VacID",response);

                        vacation_reasons[] vacationRequest= ServerManager.deSerializeStringToObject(response,vacation_reasons[].class);
                        if(vacationRequest!=null&&vacationRequest.length>=0) {
                            List<vacation_reasons> AssignedTasks = new ArrayList<vacation_reasons>(vacationRequest.length);
                            AssignedTasks = Arrays.asList(vacationRequest);
                           // mView.HideDialog();
                            mView.InsertVactionReason(AssignedTasks);

                        }

                    }


                }

                @Override
                public void failed(String errorText)
                {
                //    mView.HideDialog();
                    mView.ShowMsg(errorText);
                }
            });

        }
        else {
            mView.ShowMsg(mcontext.getString(R.string.no_internet));
        }

    }
}
