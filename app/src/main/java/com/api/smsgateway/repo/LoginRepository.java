package com.api.smsgateway.repo;



import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.api.smsgateway.R;
import com.api.smsgateway.model.RequestModel;
import com.api.smsgateway.model.ResponseModel;
import com.api.smsgateway.retrofit.ApiInterface;
import com.api.smsgateway.retrofit.RetrofitUtility;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepository {

    ApiInterface apiInterface;

    public LoginRepository() {
         apiInterface = RetrofitUtility.getRetrofitClient().create(ApiInterface.class);
    }

    public MutableLiveData<ResponseModel> performLogin(String login, String password, String deviceId,String devicename, Context context) {
       MutableLiveData<ResponseModel> loginResponse = new MutableLiveData<>();

        RequestModel request = new RequestModel(login, password, deviceId,devicename);

        Call<ResponseModel> call = apiInterface.performLogin(request);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    ResponseModel data = response.body();
                    loginResponse.setValue(data);
                } else {
                    ResponseModel data = response.body();
                    loginResponse.setValue(data);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                if (t instanceof IOException) {
                    Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    // Network or connectivity error occurred
                    // Handle the error appropriately (e.g., show a message to the user)
                } else {
                    Toast.makeText(context, t.getMessage().toString(), Toast.LENGTH_LONG).show();
                    // Non-network related error occurred
                    // Log the error or handle it based on your application's requirements
                }
                loginResponse.setValue(null);
            }
        });

        return loginResponse;
    }
}

