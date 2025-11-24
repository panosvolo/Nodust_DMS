package com.mts.mea.nodust_app.OFFLINE;

import android.content.Context;

import com.mts.mea.nodust_app.Application.ConnectivityUtil;
import com.mts.mea.nodust_app.Interfaces.IServerResponse;
import com.mts.mea.nodust_app.User.User;
import com.mts.mea.nodust_app.common.DataBaseHelper;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Mahmoud on 9/5/2018.
 */

public class OfflinePresenter implements IContractOffline.Presenter {

    IContractOffline.View mView;
    IContractOffline.Model mModel;
    Context mcontext;
    DataBaseHelper DB;
    List<OFFLINE> mlst_off;
    int index;
    public OfflinePresenter(IContractOffline.View view, Context context, User user) {
        mView = view;
        mModel = new OfflineModel(user, context);
        mcontext = context;
        DB=new DataBaseHelper(mcontext);
        mlst_off=DB.GetCashingRequest();
        index=0;
    }

    @Override
    public void setAssignmentAction(final OFFLINE obj) throws JSONException {
        if (ConnectivityUtil.isOnline(mcontext)) {
            mModel.setAssignmentAction(obj, new IServerResponse() {
                    @Override
                    public void success(String response) {
                        if (response != null)
                        {
                            DB.DeleteCashingRequests(obj);
                            index++;
                            // take second request
                            if(mlst_off!=null &&index< mlst_off.size())
                            {
                                if(mlst_off.get(index).getTYPE().equalsIgnoreCase("SMS"))
                                {
                                    SendSMS(mlst_off.get(index));

                                }
                                else if(mlst_off.get(index).getTYPE().equalsIgnoreCase("ACTION"))
                                {
                                    try {
                                        setAssignmentAction(mlst_off.get(index));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            else if(mlst_off.size()-1<=index)
                            {
                                mView.SuccessALLRequests();
                            }
                        }
                  //     mView.Success(obj);


                    }

                    @Override
                    public void failed(String errorText) {
                        mView.Fail();
                    }
                });
            }

        else {
            mView.Fail();
        }
    }

    @Override
    public void SendSMS(final OFFLINE obj) {
        if (ConnectivityUtil.isOnline(mcontext)) {
            mModel.SendSMS( obj,new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        //mView.ShowMsg(mcontext.getResources().getString(R.string.SaveChanges));
                        //mView.SendAction();
                       // mView.Success(obj);
                        DB.DeleteCashingRequests(obj);
                        index++;
                        // take second request
                        if(mlst_off!=null &&index< mlst_off.size())
                        {
                            if(mlst_off.get(index).getTYPE().equalsIgnoreCase("SMS"))
                            {
                                SendSMS(mlst_off.get(index));

                            }
                            else if(mlst_off.get(index).getTYPE().equalsIgnoreCase("ACTION"))
                            {
                                try {
                                    setAssignmentAction(mlst_off.get(index));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else if(mlst_off.size()-1<=index)
                        {
                            mView.SuccessALLRequests();
                        }
                    }


                }

                @Override
                public void failed(String errorText) {

                   // mView.Failed(obj);
                    index++;
                    // take second request
                    if(mlst_off!=null &&index< mlst_off.size())
                    {
                        if(mlst_off.get(index).getTYPE().equalsIgnoreCase("SMS"))
                        {
                            SendSMS(mlst_off.get(index));

                        }
                        else if(mlst_off.get(index).getTYPE().equalsIgnoreCase("ACTION"))
                        {
                            try {
                                setAssignmentAction(mlst_off.get(index));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else if(mlst_off.size()-1<=index)
                    {
                        mView.SuccessALLRequests();
                    }

                }
            });

        }
        else {
         //   mView.Failed(obj);
            mView.Fail();
        }

    }
}
