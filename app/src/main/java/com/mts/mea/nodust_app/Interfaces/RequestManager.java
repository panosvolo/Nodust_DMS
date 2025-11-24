package com.mts.mea.nodust_app.Interfaces;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Mahmoud on 9/7/2017.
 */

public class RequestManager {
    private static RequestQueue mRequetQueue;
    private static RequestManager instance;
    private static Context mContext;
    public RequestManager(Context context)
    {
        mContext=context;
        mRequetQueue=getRequestQueue();


    }
    public static synchronized RequestManager newInstance(Context context)
    {
      if(instance==null)
      {
          instance= new RequestManager(context);
      }
        return instance;
    }
    public static RequestQueue getRequestQueue() {
        if (mRequetQueue == null) {
            mRequetQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequetQueue;
    }

}
