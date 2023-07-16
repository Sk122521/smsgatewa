package com.api.smsgateway.repo;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.api.smsgateway.model.ReceiveRequest;
import com.api.smsgateway.retrofit.ApiInterface;
import com.api.smsgateway.retrofit.RetrofitUtility;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class hearbeatrepo {

    ApiInterface apiInterface;

    public hearbeatrepo(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login_values", Context.MODE_PRIVATE);
        String callbackurl  = sharedPreferences.getString("callbackurl","");

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(callbackurl+"/")
                .build();
        apiInterface = retrofit.create(ApiInterface.class);
    }

    public LiveData<Map<String,Object>> checkheartbeat() {
        MutableLiveData<Map<String,Object>> Response = new MutableLiveData<>();

        Call<Void> call = apiInterface.checkHeartbeat();

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()){
                    int statusCode = response.code();

                    HashMap map = new HashMap();
                     map.put("result","yes");
                     map.put("statuscode",statusCode)   ;
                     Response.setValue(map);
                }else{
                    int statusCode = response.code();
                    HashMap map = new HashMap();
                    map.put("result","no");
                    map.put("statuscode",statusCode)   ;
                    Response.setValue(map);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });


   return  Response;
    }

}
