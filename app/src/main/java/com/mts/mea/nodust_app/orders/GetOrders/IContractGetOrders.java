package com.mts.mea.nodust_app.orders.GetOrders;

import com.mts.mea.nodust_app.Evalution.KPI;
import com.mts.mea.nodust_app.Interfaces.IServerResponse;
import com.mts.mea.nodust_app.Pilots.Pilot;
import com.mts.mea.nodust_app.products.Collection;
import com.mts.mea.nodust_app.products.Product;
import com.mts.mea.nodust_app.orders.Task;
import com.mts.mea.nodust_app.products.PackageInfo;

import java.util.List;

/**
 * Created by Mahmoud on 11/8/2017.
 */

public interface IContractGetOrders  {
    public interface View{
        public void setTasks(List<Task> Tasks);
        public void showError(String error);
        public void setProducts(List<Product>products);
        public void setCoverProducts(List<Product>products);
        public void setPilots(List<Pilot>Pilots);
        public void setTotalAmount(String TotalAmount);
        public void setKPI(List<KPI>kpis);
        public void setProductReference(List<Product>products);
        public void setPackageInfo(List<PackageInfo>packageInfoList);
        public void ShowLoading();
        public void StopLoading();
        public void setCollection(List<Collection>products);

    };
    public interface Presenter{
        public void setTasks();
        public void setTasksNotAssign();
        public void setProducts();
        public void setCoverProducts();
        public void setDriverProducts();
        public void setPilots();
        public void setAmount();
        public void GetKPI();
        public void GetProductReference();
        public void GetPackageInfo();
        public void GetCollectionDriver();
        public void GetCollectionPilot();

    };
    public interface Model{
        public void getTasks(IServerResponse iServerResponse);
        public void getTasksNotAssign(IServerResponse iServerResponse);
        public void getProducts(IServerResponse iServerResponse);
        public void getCoverProducts(IServerResponse iServerResponse);
        public void getDriverProducts(IServerResponse iServerResponse);
        public void getCoverPilots(IServerResponse iServerResponse);
        public void getAmount(IServerResponse iServerResponse);
        public void getKPI(IServerResponse iServerResponse);
        public void getProductReference(IServerResponse iServerResponse);
        public void getPackageInfo(IServerResponse iServerResponse);
        public void GetCollectionPilot(IServerResponse iServerResponse);
        public void GetCollectionDriver(IServerResponse iServerResponse);


    };
}
