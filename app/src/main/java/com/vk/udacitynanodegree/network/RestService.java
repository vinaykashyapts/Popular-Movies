package com.vk.udacitynanodegree.network;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.vk.udacitynanodegree.R;

/**
 * Created by Vinay on 4/11/15.
 */
public class RestService {

    public static final String TAG = RestService.class.getSimpleName();

    private static final int MAX_IMAGE_CACHE_ENTRIES = 100;

    private static RestService mInstance;
    private Context mContext;
    private RequestQueue mRequestQueue;

    private RestService(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized RestService getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RestService(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(mContext.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());
            mRequestQueue = new RequestQueue(cache, network);
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public String getApiUrl(int api, int movieId) {
        String server = RestAPI.URL_MOVIES_SERVER;
        switch (api) {
            case RestAPI.MOVIES_POPULAR:
                return server + RestAPI.API_MOVIE_POPULAR + RestAPI.API_KEY + mContext.getResources().getString(R.string.moviesDbApiKey);
            case RestAPI.MOVIES_TOP_RATED:
                return server + RestAPI.API_MOVIE_TOP_RATED + RestAPI.API_KEY + mContext.getResources().getString(R.string.moviesDbApiKey);
            case RestAPI.MOVIES_TRAILERS:
                return server + movieId + RestAPI.API_MOVIE_TRAILERS + RestAPI.API_KEY + mContext.getResources().getString(R.string.moviesDbApiKey);
            case RestAPI.MOVIES_REVIEWS:
                return server + movieId + RestAPI.API_MOVIE_REVIEWS + RestAPI.API_KEY + mContext.getResources().getString(R.string.moviesDbApiKey);
            default:
                return null;
        }
    }
}
