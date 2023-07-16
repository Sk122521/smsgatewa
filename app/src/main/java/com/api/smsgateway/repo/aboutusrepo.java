package com.api.smsgateway.repo;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.api.smsgateway.model.MessageResponse;
import com.api.smsgateway.retrofit.ApiInterface;
import com.api.smsgateway.retrofit.RetrofitUtility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class aboutusrepo {


    public MutableLiveData<String> aboutUs(){
        final MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

        ApiInterface apiInterface = RetrofitUtility.getRetrofitClient().create(ApiInterface.class);

        apiInterface.aboutUs().enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()){
                            mutableLiveData.setValue("fgfgfg");
                       //     Log.d("response",response.body());
                           // Toast.makeText(aboutusrepo.this.getClass(), "", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                          mutableLiveData.setValue(t.getLocalizedMessage());
                    }
                });



        return mutableLiveData;
    }

}
