package com.mts.mea.nodust_app.BackgroundServices;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.mts.mea.nodust_app.common.AlertActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Timer;
import java.util.TimerTask;

public class GetCurrentLocaion extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    GoogleApiClient mGoogleApiClient;
    public static Location CurrentLoc;
    final static int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    public static boolean FlagGPS=true;
    private Handler mHandler = new Handler();   //run on another Thread to avoid crash
    private Timer mTimer = null;
   public static boolean flag=true;

    public GetCurrentLocaion() {
    }
    public class TimeDisplay extends TimerTask {
        public  void stop()
        {
            stopSelf();
            this.cancel();
        }
        @Override
        public void run() {

            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                       if(FlagGPS)
                       {
                           LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                           //getting GPS status
                           boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                           if(!isGPSEnabled&&flag) {
                               ShowAlert();
                           }
                       }
                        else {
                            stopSelf();
                            stop();
                        }


                }

            });
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
            mTimer = new Timer();   //recreate new
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, 1000);

        buildGoogleApiClient();

        return super.onStartCommand(intent, flags, startId);
    }
public void ShowAlert()
{
    flag=false;
    Intent i=new Intent(this, AlertActivity.class);
    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(i);
}

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mGoogleApiClient!=null)
        {
            RemoveUpdate();

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(GetCurrentLocaion.this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        if (!(mGoogleApiClient.isConnected()))
            mGoogleApiClient.connect();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        //getting GPS status
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
       // if(isGPSEnabled)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            CurrentLoc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        StartUpdateLoc();
    }
    public void StartUpdateLoc()
    {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            return;
        }
        if(mGoogleApiClient!=null&&mGoogleApiClient.isConnected())
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        flag=true;
        if (location != null) {
            CurrentLoc = location;
        }
    }
    public void RemoveUpdate()
    {
        if(mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
        }
    }


}
