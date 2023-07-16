package com.api.smsgateway.viewmodel;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class LoginViewModelFactory implements ViewModelProvider.Factory {
    private String login;
    private String password;
    private String deviceId;

    private String devicename;

    private Context context;

    public LoginViewModelFactory(String login, String password, String deviceId, String devicename, Context context) {
        this.login = login;
        this.password = password;
        this.deviceId = deviceId;
        this.context = context;
        this.devicename = devicename;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(login, password, deviceId,devicename,context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
