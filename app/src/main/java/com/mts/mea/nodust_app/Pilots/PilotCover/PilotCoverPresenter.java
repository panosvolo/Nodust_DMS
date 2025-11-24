package com.mts.mea.nodust_app.Pilots.PilotCover;

import android.content.Context;
import android.util.Log;

import com.mts.mea.nodust_app.Application.ConnectivityUtil;
import com.mts.mea.nodust_app.Interfaces.IServerResponse;
import com.mts.mea.nodust_app.Interfaces.ServerManager;
import com.mts.mea.nodust_app.Pilots.Reconcilation;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.User.User;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mahmoud on 5/21/2018.
 */

public class PilotCoverPresenter  implements IPilotCover.Presenter{
    IPilotCover.View mView;
    IPilotCover.Model mModel;
    Context mContext;
    String TAG="UpdateTaskPresenter";
    public PilotCoverPresenter(IPilotCover.View view,Context context,User muser)
    {
        mView=view;
        mModel=new PilotCoverModel(context,muser);
        mContext=context;

    }
    @Override
    public void AddCoverPilot(String ProductsID, String PilotID) {
        if(ConnectivityUtil.isOnline(mContext))
        {
            mView.ShowLoading();
            mModel.AddCoverPilot(ProductsID,PilotID, new IServerResponse() {
                @Override
                public void success(String response) throws ParseException {
                   // Task task= ServerManager.deSerializeStringToObject(response,Task.class);

                    if(response!=null)
                        mView.StopLoading();

                    mView.showMsg(mContext.getResources().getString(R.string.SaveChanges)+"SuccessfulCover");
                 //   mView.StopLoading();
                }

                @Override
                public void failed(String errorText) {
                    mView.showMsg(errorText);
                    mView.StopLoading();
                }
            });
        }
        else
        {
            mView.showMsg(mContext.getString(R.string.no_internet));
            mView.StopLoading();

        }
    }

    @Override
    public void GetCoverAllPilot() {
        if (ConnectivityUtil.isOnline(mContext)) {
            //   mView.ShowLoading();
            mModel.SetCoverAllPilot(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        Pilot_Cover[] Tasks = ServerManager.deSerializeStringToObject(response, Pilot_Cover[].class);
                        List<Pilot_Cover> AssignedTasks = new ArrayList<Pilot_Cover>(Tasks.length);
                        AssignedTasks = Arrays.asList(Tasks);
                        if (AssignedTasks != null && AssignedTasks.size() > 0) {
                            mView.SetCoverPilot(AssignedTasks);
                        } else {
                            mView.SetCoverPilot(new ArrayList<Pilot_Cover>());
                        }
                        mView.StopLoading();
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.showMsg(errorText);
                    mView.StopLoading();
                }
            });

        } else {
            mView.showMsg(mContext.getString(R.string.no_internet));
            mView.StopLoading();
        }
    }

    @Override
    public void GetReconcilationRequest() {
        if (ConnectivityUtil.isOnline(mContext)) {
            //   mView.ShowLoading();
            mModel.SetReconcilationRequests(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        Reconcilation[] Tasks = ServerManager.deSerializeStringToObject(response, Reconcilation[].class);
                        List<Reconcilation> AssignedTasks = new ArrayList<Reconcilation>(Tasks.length);
                        AssignedTasks = Arrays.asList(Tasks);
                        if (AssignedTasks != null && AssignedTasks.size() > 0) {
                            mView.SetReconcilationRequests(AssignedTasks);
                        } else {
                            mView.SetReconcilationRequests(new ArrayList<Reconcilation>());
                        }
                        mView.StopLoading();
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.showMsg(errorText);
                    mView.StopLoading();
                }
            });

        } else {
            mView.showMsg(mContext.getString(R.string.no_internet));
            mView.StopLoading();
        }
    }

    @Override
    public void GetcoverPilot() {
        if (ConnectivityUtil.isOnline(mContext)) {
            //   mView.ShowLoading();
            mModel.SetCoverPilot(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        Pilot_Cover[] Tasks = ServerManager.deSerializeStringToObject(response, Pilot_Cover[].class);
                        List<Pilot_Cover> AssignedTasks = new ArrayList<Pilot_Cover>(Tasks.length);
                        AssignedTasks = Arrays.asList(Tasks);
                        if (AssignedTasks != null && AssignedTasks.size() > 0) {
                            mView.SetCoverPilot(AssignedTasks);
                        } else {
                            mView.SetCoverPilot(new ArrayList<Pilot_Cover>());
                        }
                        mView.StopLoading();
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.showMsg(errorText);
                    mView.StopLoading();
                }
            });

        } else {
            mView.showMsg(mContext.getString(R.string.no_internet));
            mView.StopLoading();
        }
    }

    @Override
    public void Pilotaccept(String AreaID) {
        if (ConnectivityUtil.isOnline(mContext)) {
            mView.ShowLoading();
            mModel.PilotAccept(AreaID,new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        mView.StopLoading();
                        mView.SuccessPilotAccept();
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.StopLoading();
                    mView.showMsg(errorText);
                }
            });

        }
        else {
            mView.showMsg(mContext.getString(R.string.no_internet));
        }
    }

    @Override
    public void CheckProductsPilot() {
        if (ConnectivityUtil.isOnline(mContext)) {
            //   mView.ShowLoading();
            mModel.CheckProductsPilot(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        Log.e("pilotcover",response);
                        Pilot_Cover[] Tasks = ServerManager.deSerializeStringToObject(response, Pilot_Cover[].class);
                        List<Pilot_Cover> AssignedTasks = new ArrayList<Pilot_Cover>(Tasks.length);
                        AssignedTasks = Arrays.asList(Tasks);
                        if (AssignedTasks != null && AssignedTasks.size() > 0) {
                            mView.SetCheckCoverPilot(AssignedTasks);
                        } else {
                            mView.SetCheckCoverPilot(new ArrayList<Pilot_Cover>());
                        }
                        mView.StopLoading();
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.showMsg(errorText);
                    mView.StopLoading();
                }
            });

        } else {
            mView.showMsg(mContext.getString(R.string.no_internet));
            mView.StopLoading();
        }
    }

    @Override
    public void PilotRejectCover(String Area_id) {
        if (ConnectivityUtil.isOnline(mContext)) {
            mView.ShowLoading();
            mModel.PilotRejectCover(Area_id,new IServerResponse() {
                @Override
                public void success(String response) {
                    Log.e("rejectPilot",response);
                    if (response != null) {
                        mView.StopLoading();
                        mView.SuccessPilotReject();
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.StopLoading();
                    mView.showMsg(errorText);
                }
            });

        }
        else {
            mView.showMsg(mContext.getString(R.string.no_internet));
        }

    }

    @Override
    public void PilotReconcilation(List<Pilot_Cover> lst_products,String Assign_ID,String Pilot_Cash,String ExpCach) throws JSONException {
        if (ConnectivityUtil.isOnline(mContext)) {
            mView.ShowLoading();
            mModel.PilotReconcilation(lst_products, Assign_ID, Pilot_Cash, ExpCach,new IServerResponse() {
                @Override
                public void success(String response) {
                    Log.e("rejectPilot",response);
                    if (response != null) {
                        mView.StopLoading();
                        mView.UpdateCoverAfterReconcilation();
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.StopLoading();
                    mView.showMsg(errorText);
                }
            });

        }
        else {
            mView.showMsg(mContext.getString(R.string.no_internet));
        }

    }

    @Override
    public void DriverReconcilation(List<Reconcilation> lst_products,boolean Accept,String Assign_ID,String Pilot_Cash,String ExpCach ) throws JSONException {

        if (ConnectivityUtil.isOnline(mContext)) {
            mView.ShowLoading();
            mModel.DriverReconcilation(lst_products, Accept, Assign_ID, Pilot_Cash , ExpCach,new IServerResponse() {
                @Override
                public void success(String response) {
                    Log.e("rejectPilot", response);
                    if (response != null) {
                        mView.StopLoading();
                        mView.UpdateCoverAfterReconcilation();
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.StopLoading();
                    if(errorText.equalsIgnoreCase(mContext.getResources().getString(R.string.modifiedQTY)))
                    {
                        // need to refresh qty
                        mView.RefreshQTYReconcilation();
                    }
                    mView.showMsg(errorText);
                }
            });

        } else {
            mView.showMsg(mContext.getString(R.string.no_internet));
        }

    }

}
