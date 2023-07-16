package com.api.smsgateway.repo;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.api.smsgateway.model.MessageResponse;
import com.api.smsgateway.retrofit.ApiInterface;
import com.api.smsgateway.retrofit.RetrofitUtility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmsRepo {
    SharedPreferences sharedPreferences;
    String deviceid;
    // private final String TAG = getClass().getSimpleName();
    public  MutableLiveData<MessageResponse>  getmessages(Context context){
      sharedPreferences = context.getSharedPreferences("login_values", Context.MODE_PRIVATE);
      deviceid =sharedPreferences.getString("deviceid", "121212");
      // Toast.makeText(context, deviceid, Toast.LENGTH_LONG).show();
        final MutableLiveData<MessageResponse> mutableLiveData = new MutableLiveData<>();

        ApiInterface apiInterface = RetrofitUtility.getRetrofitClient().create(ApiInterface.class);

        apiInterface.getMessages(deviceid).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    mutableLiveData.postValue(response.body());
                   // Toast.makeText(context, response.body().getError()+response.body().getSuccess(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        return mutableLiveData;
    }

}
