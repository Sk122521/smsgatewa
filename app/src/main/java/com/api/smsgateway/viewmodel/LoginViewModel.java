package com.api.smsgateway.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.api.smsgateway.model.ResponseModel;
import com.api.smsgateway.repo.LoginRepository;

public class LoginViewModel extends ViewModel {
    private LoginRepository repository;
    private String login;
    private String password;
    private String deviceId;

    private String devicename;
    private Context context;

    public LoginViewModel(String login, String password, String deviceId,String devicename, Context context) {
        repository = new LoginRepository();
        this.login = login;
        this.password = password;
        this.deviceId = deviceId;
        this.devicename = devicename;
        this.context = context;

    }

    public MutableLiveData<ResponseModel> performLogin() {
        return repository.performLogin(login, password,devicename, deviceId,context);
    }
}


