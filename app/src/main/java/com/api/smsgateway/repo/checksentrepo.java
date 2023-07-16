package com.api.smsgateway.repo;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.api.smsgateway.model.ChecksentRequest;
import com.api.smsgateway.model.MessageResponse;
import com.api.smsgateway.retrofit.ApiInterface;
import com.api.smsgateway.retrofit.RetrofitUtility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class checksentrepo {
    SharedPreferences sharedPreferences;
    String deviceid;
    public void getmessages(String id, String status, Context context) {
        // final MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        sharedPreferences = context.getSharedPreferences("login_values", Context.MODE_PRIVATE);
        deviceid =sharedPreferences.getString("deviceid", "121212");
        ChecksentRequest checksentRequest = new ChecksentRequest(id, status);
        ApiInterface apiInterface = RetrofitUtility.getRetrofitClient().create(ApiInterface.class);

        apiInterface.checksentstatus(checksentRequest,deviceid).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()&& response.body().getMessages() != null) {
                    //  mutableLiveData.setValue("response got");
                    Toast.makeText(context, "response got", Toast.LENGTH_SHORT).show();
                } else {
                    //  mutableLiveData.setValue("could not get response");
                      Toast.makeText(context, "response not getting", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                //  mutableLiveData.setValue("could not get response");
            }
        });

    }

}
