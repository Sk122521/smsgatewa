package com.api.smsgateway.repo;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.api.smsgateway.model.ReceiveRequest;
import com.api.smsgateway.model.ReceiveResponse;
import com.api.smsgateway.model.RequestModel;
import com.api.smsgateway.model.ResponseModel;
import com.api.smsgateway.retrofit.ApiInterface;
import com.api.smsgateway.retrofit.RetrofitUtility;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReceivesmsRepo {

    ApiInterface apiInterface;
    private String x;

    private Context context;

    public ReceivesmsRepo(Context context) {
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences("login_values", Context.MODE_PRIVATE);
        String callbackurl  = sharedPreferences.getString("callbackurl","");
       Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(callbackurl+"/")
                .build();
        apiInterface = retrofit.create(ApiInterface.class);
    }

    public String submitmsg(ReceiveRequest request) {


     //   ReceiveRequest request = new ReceiveRequest(message, phoneno , time);

        Call<Void> call = apiInterface.submitMessage(request);

        call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                        if (response.isSuccessful()){
                          x =  "Message received in API with status code "+ Integer.toString(response.code());
                            Toast.makeText(context, x, Toast.LENGTH_SHORT).show();
                        }else{
                        x =  "status code"+Integer.toString(response.code())+"Message Failed to receive ";
                            Toast.makeText(context, x, Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        x = "Message Failed to receive ";
                        Toast.makeText(context, x, Toast.LENGTH_SHORT).show();

                    }
                });

        return x;
    }

}
