package com.mts.mea.nodust_app.Application;

import android.support.multidex.MultiDexApplication;

import com.karumi.dexter.Dexter;

public class ApplicationControl extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Dexter.initialize(this);
    }
}
