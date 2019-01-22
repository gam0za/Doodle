package com.gamza.jinyoungkim.doodle.util;

import android.app.Application;
import android.widget.Toast;

import com.gamza.jinyoungkim.doodle.network.NetworkService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GlobalApplication extends Application {
    private static GlobalApplication mInstance;
    private static String baseUrl = "http://52.78.193.204:3000/api/";
    private NetworkService networkService;

    public NetworkService getNetworkService() {
        return networkService;
    }

    public static GlobalApplication getGlobalApplicationContext() {
        return mInstance;
    }

    @Override
    public void onCreate () {
        super.onCreate();
        mInstance = this;
        buildService();
    }

    public void buildService () {
        Retrofit.Builder builder = new Retrofit.Builder();
        Retrofit retrofit = builder
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        networkService = retrofit.create(NetworkService.class);
    }
    public void makeToast (String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
