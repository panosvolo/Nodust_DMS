package com.mts.mea.nodust_app.orders.GetOrders;

import android.content.Context;

import com.mts.mea.nodust_app.Application.ConnectivityUtil;
import com.mts.mea.nodust_app.Evalution.KPI;
import com.mts.mea.nodust_app.Interfaces.IServerResponse;
import com.mts.mea.nodust_app.Interfaces.ServerManager;
import com.mts.mea.nodust_app.Pilots.Pilot;
import com.mts.mea.nodust_app.R;
import com.mts.mea.nodust_app.User.User;
import com.mts.mea.nodust_app.products.Collection;
import com.mts.mea.nodust_app.products.Product;
import com.mts.mea.nodust_app.orders.Task;
import com.mts.mea.nodust_app.products.PackageInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mahmoud on 11/8/2017.
 */

public class GetOrdersPresenter implements IContractGetOrders.Presenter {
    IContractGetOrders.View mView;
    IContractGetOrders.Model mModel;
    Context mcontext;

    public GetOrdersPresenter(IContractGetOrders.View view, Context context, User user) {
        mView = view;
        mModel = new GetOrdersModel(user, context);
        mcontext = context;
    }

    @Override
    public void setTasks() {
        if (ConnectivityUtil.isOnline(mcontext)) {
            mView.ShowLoading();
            mModel.getTasks(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        Task[] Tasks = ServerManager.deSerializeStringToObject(response, Task[].class);
                        List<Task> AssignedTasks = new ArrayList<Task>(Tasks.length);
                        AssignedTasks = Arrays.asList(Tasks);
                        if (AssignedTasks != null && AssignedTasks.size() > 0) {
                            mView.setTasks(AssignedTasks);
                        } else {
                            mView.setTasks(new ArrayList<Task>());
                        }
                        mView.StopLoading();
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.showError(errorText);
                    mView.StopLoading();
                }
            });

        } else {
            mView.showError(mcontext.getString(R.string.no_internet));
            mView.StopLoading();
        }
    }

    @Override
    public void setTasksNotAssign() {
        if (ConnectivityUtil.isOnline(mcontext)) {
           // mView.ShowLoading();
            mModel.getTasksNotAssign(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        Task[] Tasks = ServerManager.deSerializeStringToObject(response, Task[].class);
                        List<Task> AssignedTasks = new ArrayList<Task>(Tasks.length);
                        AssignedTasks = Arrays.asList(Tasks);
                        if (AssignedTasks != null && AssignedTasks.size() > 0) {
                            mView.setTasks(AssignedTasks);
                        } else {
                            mView.setTasks(new ArrayList<Task>());
                           // mView.showError(mcontext.getResources().getString(R.string.NoDataFound));
                        }
                        mView.StopLoading();
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.showError(errorText);
                    mView.StopLoading();
                }
            });

        } else {
            mView.showError(mcontext.getString(R.string.no_internet));
            mView.StopLoading();
        }
    }

    @Override
    public void setProducts() {
        if (ConnectivityUtil.isOnline(mcontext)) {
            //   mView.ShowLoading();
            mModel.getProducts(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        Product[] Tasks = ServerManager.deSerializeStringToObject(response, Product[].class);
                        List<Product> AssignedTasks = new ArrayList<Product>(Tasks.length);
                        AssignedTasks = Arrays.asList(Tasks);
                        if (AssignedTasks != null && AssignedTasks.size() > 0) {
                            mView.setProducts(AssignedTasks);
                        } else {
                            mView.setProducts(new ArrayList<Product>());
                        }
                        mView.StopLoading();
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.showError(errorText);
                    mView.StopLoading();
                }
            });

        } else {
            mView.showError(mcontext.getString(R.string.no_internet));
            mView.StopLoading();
        }

    }

    @Override
    public void setCoverProducts() {
        if (ConnectivityUtil.isOnline(mcontext)) {
           mView.ShowLoading();
            mModel.getCoverProducts(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        Product[] Tasks = ServerManager.deSerializeStringToObject(response, Product[].class);
                        List<Product> AssignedTasks = new ArrayList<Product>(Tasks.length);
                        AssignedTasks = Arrays.asList(Tasks);
                        if (AssignedTasks != null && AssignedTasks.size() > 0) {

                            mView.setCoverProducts(AssignedTasks);
                        } else {
                            mView.setCoverProducts(new ArrayList<Product>());
                        }
                        mView.StopLoading();
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.showError(errorText);
                    mView.StopLoading();
                }
            });

        } else {
            mView.showError(mcontext.getString(R.string.no_internet));
            mView.StopLoading();
        }

    }

    @Override
    public void setDriverProducts() {
        if (ConnectivityUtil.isOnline(mcontext)) {
            //   mView.ShowLoading();
            mModel.getDriverProducts(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        Product[] Tasks = ServerManager.deSerializeStringToObject(response, Product[].class);
                        List<Product> AssignedTasks = new ArrayList<Product>(Tasks.length);
                        AssignedTasks = Arrays.asList(Tasks);
                        if (AssignedTasks != null && AssignedTasks.size() > 0) {
                            mView.setProducts(AssignedTasks);
                        } else {
                            mView.setProducts(new ArrayList<Product>());
                        }
                        mView.StopLoading();
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.showError(errorText);
                    mView.StopLoading();
                }
            });

        } else {
            mView.showError(mcontext.getString(R.string.no_internet));
            mView.StopLoading();
        }
    }

    @Override
    public void setPilots() {
        if (ConnectivityUtil.isOnline(mcontext)) {
            //   mView.ShowLoading();
            mModel.getCoverPilots(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        Pilot[] Tasks = ServerManager.deSerializeStringToObject(response, Pilot[].class);
                        List<Pilot> AssignedTasks = new ArrayList<Pilot>(Tasks.length);
                        AssignedTasks = Arrays.asList(Tasks);
                        if (AssignedTasks != null && AssignedTasks.size() > 0) {
                            mView.setPilots(AssignedTasks);
                        } else {
                            mView.setPilots(new ArrayList<Pilot>());
                            //mView.showError(mcontext.getResources().getString(R.string.NoDataFound));
                        }
                        mView.StopLoading();
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.showError(errorText);
                    mView.StopLoading();
                }
            });

        } else {
            mView.showError(mcontext.getString(R.string.no_internet));
            mView.StopLoading();
        }

    }

    @Override
    public void setAmount() {
        if (ConnectivityUtil.isOnline(mcontext)) {
            //   mView.ShowLoading();
            mModel.getAmount(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                       mView.setTotalAmount(response);
                        mView.StopLoading();
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.showError(errorText);
                    mView.StopLoading();
                }
            });

        } else {
            mView.showError(mcontext.getString(R.string.no_internet));
            mView.StopLoading();
        }

    }

    @Override
    public void GetKPI() {
        if (ConnectivityUtil.isOnline(mcontext)) {
            //   mView.ShowLoading();
            mModel.getKPI(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        KPI[] Tasks = ServerManager.deSerializeStringToObject(response, KPI[].class);
                        List<KPI> AssignedTasks = new ArrayList<KPI>(Tasks.length);
                        AssignedTasks = Arrays.asList(Tasks);
                        if (AssignedTasks != null && AssignedTasks.size() > 0) {
                            mView.setKPI(AssignedTasks);
                        } else {
                            mView.setKPI(new ArrayList<KPI>());
                            //mView.showError(mcontext.getResources().getString(R.string.NoDataFound));
                        }
                        mView.StopLoading();
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.showError(errorText);
                    mView.StopLoading();
                }
            });

        } else {
            mView.showError(mcontext.getString(R.string.no_internet));
            mView.StopLoading();
        }

    }

    @Override
    public void GetProductReference() {
        if (ConnectivityUtil.isOnline(mcontext)) {
            //   mView.ShowLoading();
            mModel.getProductReference(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        Product[] Tasks = ServerManager.deSerializeStringToObject(response, Product[].class);
                        List<Product> AssignedTasks = new ArrayList<Product>(Tasks.length);
                        AssignedTasks = Arrays.asList(Tasks);
                        if (AssignedTasks != null && AssignedTasks.size() > 0) {
                            mView.setProductReference(AssignedTasks);
                        } else {
                            mView.setProductReference(new ArrayList<Product>());
                            //mView.showError(mcontext.getResources().getString(R.string.NoDataFound));
                        }
                        mView.StopLoading();
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.showError(errorText);
                    mView.StopLoading();
                }
            });

        } else {
            mView.showError(mcontext.getString(R.string.no_internet));
            mView.StopLoading();
        }
    }

    @Override
    public void GetPackageInfo() {
        if (ConnectivityUtil.isOnline(mcontext)) {
            //   mView.ShowLoading();
            mModel.getPackageInfo(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        PackageInfo[] Tasks = ServerManager.deSerializeStringToObject(response, PackageInfo[].class);
                        List<PackageInfo> AssignedTasks = new ArrayList<PackageInfo>(Tasks.length);
                        AssignedTasks = Arrays.asList(Tasks);
                        if (AssignedTasks != null && AssignedTasks.size() > 0) {
                            mView.setPackageInfo(AssignedTasks);
                        } else {
                            mView.setPackageInfo(new ArrayList<PackageInfo>());
                            //mView.showError(mcontext.getResources().getString(R.string.NoDataFound));
                        }
                        mView.StopLoading();
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.showError(errorText);
                    mView.StopLoading();
                }
            });

        } else {
            mView.showError(mcontext.getString(R.string.no_internet));
            mView.StopLoading();
        }
    }

    @Override
    public void GetCollectionDriver() {
        if (ConnectivityUtil.isOnline(mcontext)) {
            //   mView.ShowLoading();
            mModel.GetCollectionDriver(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        Collection[] Tasks = ServerManager.deSerializeStringToObject(response, Collection[].class);
                        List<Collection> AssignedTasks = new ArrayList<Collection>(Tasks.length);
                        AssignedTasks = Arrays.asList(Tasks);
                        if (AssignedTasks != null && AssignedTasks.size() > 0) {
                            mView.setCollection(AssignedTasks);
                        } else {
                            mView.setCollection(new ArrayList<Collection>());
                        }
                        mView.StopLoading();
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.showError(errorText);
                    mView.StopLoading();
                }
            });

        } else {
            mView.showError(mcontext.getString(R.string.no_internet));
            mView.StopLoading();
        }
    }

    @Override
    public void GetCollectionPilot() {
        if (ConnectivityUtil.isOnline(mcontext)) {
            //   mView.ShowLoading();
            mModel.GetCollectionPilot(new IServerResponse() {
                @Override
                public void success(String response) {
                    if (response != null) {
                        Collection[] Tasks = ServerManager.deSerializeStringToObject(response, Collection[].class);
                        List<Collection> AssignedTasks = new ArrayList<Collection>(Tasks.length);
                        AssignedTasks = Arrays.asList(Tasks);
                        if (AssignedTasks != null && AssignedTasks.size() > 0) {
                            mView.setCollection(AssignedTasks);
                        } else {
                            mView.setCollection(new ArrayList<Collection>());
                        }
                        mView.StopLoading();
                    }


                }

                @Override
                public void failed(String errorText) {
                    mView.showError(errorText);
                    mView.StopLoading();
                }
            });

        } else {
            mView.showError(mcontext.getString(R.string.no_internet));
            mView.StopLoading();
        }
    }


}
