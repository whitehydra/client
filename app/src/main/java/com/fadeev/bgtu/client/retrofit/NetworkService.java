package com.fadeev.bgtu.client.retrofit;

import android.util.Log;

import com.fadeev.bgtu.client.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;
    private Retrofit mRetrofit;
    private String TAG = "Retrofit";

    private NetworkService(){
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.d(TAG, "Build");
    }


    public static NetworkService getInstance(){
        if(mInstance == null){
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public JSONPlaceHolderApi getJSONApi(){
        return mRetrofit.create(JSONPlaceHolderApi.class);
    }



}
