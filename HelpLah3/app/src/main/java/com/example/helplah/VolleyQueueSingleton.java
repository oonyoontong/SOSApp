package com.example.helplah;


import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyQueueSingleton{
    public static final String TAG = VolleyQueueSingleton.class
            .getSimpleName();
    private RequestQueue mRequestQueue;
//    private ImageLoader mImageLoader;
    private static VolleyQueueSingleton mvolleyQueueSingletonInstance;
    private Context mcontext;

    private VolleyQueueSingleton(Context context){
        mcontext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleyQueueSingleton getInstance(Context context) {
        if(mvolleyQueueSingletonInstance == null){
            mvolleyQueueSingletonInstance = new VolleyQueueSingleton(context);
        }
        return mvolleyQueueSingletonInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mcontext.getApplicationContext());
        }
        return mRequestQueue;
    }

//    public ImageLoader getImageLoader() {
//        getRequestQueue();
//        if (mImageLoader == null) {
//            mImageLoader = new ImageLoader(this.mRequestQueue,
//                    new LruBitmapCache());
//        }
//        return this.mImageLoader;
//    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
