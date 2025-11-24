package com.mts.mea.nodust_app.CloseCodes_Groups;

import android.content.Context;

import com.mts.mea.nodust_app.Application.ConnectivityUtil;
import com.mts.mea.nodust_app.Interfaces.IServerResponse;
import com.mts.mea.nodust_app.Interfaces.ServerManager;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.User.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mahmoud on 11/9/2017.
 */

public class CloseCodePresenter implements IContractCloseCode_Groups.Presenter {
    IContractCloseCode_Groups.View mView;
    IContractCloseCode_Groups.Model mModel;
    Context mcontext;

    public CloseCodePresenter(IContractCloseCode_Groups.View view, Context context, User user) {
        mView = view;
        mModel = new CloseCodeModel(user, context);
        mcontext = context;
    }
    @Override
    public void GetCloseCodes(final int GroupID) {
        if (ConnectivityUtil.isOnline(mcontext)) {
            mModel.GetCloseCodes(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        CloseCode[] lst_closeCodes = ServerManager.deSerializeStringToObject(response, CloseCode[].class);
                        List<CloseCode> Ass_codes = new ArrayList<CloseCode>(lst_closeCodes.length);
                        Ass_codes = Arrays.asList(lst_closeCodes);
                        if (Ass_codes!=null) {
                            mView.SaveCloseCode(GroupID,Ass_codes);
                        }
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.ShowError(errorText);
                }
            },GroupID);

        }
        else {
            mView.ShowError(mcontext.getString(R.string.no_internet));
        }
    }



    @Override
    public void GetCloseCodeGroups() {
        if (ConnectivityUtil.isOnline(mcontext)) {
            mModel.GetcloseCodeGroups(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        CloseCodesGroup[] lst_closeCodes = ServerManager.deSerializeStringToObject(response, CloseCodesGroup[].class);
                        List<CloseCodesGroup> Ass_codes = new ArrayList<CloseCodesGroup>(lst_closeCodes.length);
                        Ass_codes = Arrays.asList(lst_closeCodes);
                        if (Ass_codes!=null) {
                            mView.SaveCloseCodeGroups(Ass_codes);
                        }
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.ShowError(errorText);
                }
            });

        }
        else {
            mView.ShowError(mcontext.getString(R.string.no_internet));
        }

    }
}
