package com.api.smsgateway.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.api.smsgateway.model.ResponseModel;
import com.api.smsgateway.repo.LoginRepository;
import com.api.smsgateway.repo.hearbeatrepo;

import java.util.Map;

public class HeartbeatModel extends ViewModel {
    private hearbeatrepo repository;
    private Context context;

    public HeartbeatModel(Context context) {
       this.context = context;
       repository = new hearbeatrepo(context);
    }

    public LiveData<Map<String,Object>> checkhearbeat() {
        return repository.checkheartbeat();
    }
}
