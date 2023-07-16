package com.api.smsgateway.retrofit;

import android.app.Application;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitUtility extends Application {

    final String TAG = getClass().getSimpleName();
    private static RetrofitUtility minstance;
    private static Retrofit retrofit = null;
    private static String BASE_URL = "https://api.epaymentbd.com/";

    @Override
    public void onCreate() {
        super.onCreate();
        minstance = this;
    }
    public static synchronized RetrofitUtility getMinstance() {
        return minstance;
    }

    public static Retrofit getRetrofitClient() {

        if (retrofit == null){
           // OkHttpClient client = new OkHttpClient.Builder().build();

            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build();
        }

        return retrofit;
    }
}
