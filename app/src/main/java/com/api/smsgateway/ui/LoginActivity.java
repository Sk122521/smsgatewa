package com.api.smsgateway.ui;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BadParcelableException;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.api.smsgateway.R;
import com.api.smsgateway.model.ResponseModel;
import com.api.smsgateway.viewmodel.LoginViewModel;
import com.api.smsgateway.databinding.ActivityLoginBinding;
import com.api.smsgateway.viewmodel.LoginViewModelFactory;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("login_values", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

       if(!sharedPreferences.getBoolean("logout",true)){
           Intent intent =  new Intent(LoginActivity.this, ChatActivity.class);
           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
           startActivity(intent);
       }


        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        saveLogsToFile();


        // Get the login, password, and deviceId from somewhere
        String deviceId =  Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
      //  Toast.makeText(this, deviceId, Toast.LENGTH_SHORT).show();
       String devicename = getDeviceName();
        Toast.makeText(this, devicename+"  ::  "+deviceId, Toast.LENGTH_SHORT).show();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (login.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Enter both fields", Toast.LENGTH_SHORT).show();
                }else{
                    initialiseViewmodel(login,password,deviceId,devicename);
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    loginButton.setVisibility(View.GONE);
                    loginViewModel.performLogin().observe(LoginActivity.this, new Observer<ResponseModel>() {
                        @Override
                        public void onChanged(ResponseModel responseModel) {
                            if (responseModel != null) {
                                boolean success = responseModel.isSuccess();
                                boolean error = responseModel.isError();
                                // Handle success and error flags
                                if(success && !error){

                                    String callbackurl = responseModel.getCallbackUrl();
                                    String username = responseModel.getUserName();
                                    //     Toast.makeText(LoginActivity.this, Boolean.toString(success)+ Boolean.toString(error), Toast.LENGTH_SHORT).show();
                                    loadingProgressBar.setVisibility(View.GONE);
                                    SharedPreferences sharedPreferences = getSharedPreferences("login_values", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    editor.putString("callbackurl", callbackurl);
                                    editor.putString("username", username);
                                    editor.putString("deviceid",deviceId);
                                    editor.putString("devicename",devicename);
                                    editor.putBoolean("logout",false);

                                    editor.apply();

                                    Intent intent =  new Intent(LoginActivity.this, ChatActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }else{
                                    showCustomDialog("Login Failed", "Enter right Id password");
                                }

                            } else {
                                loadingProgressBar.setVisibility(View.GONE);
                                showCustomDialog("Login Failed", "Network related issue");

                                Toast.makeText(LoginActivity.this, "unable to login", Toast.LENGTH_SHORT).show();
                                // Handle null response
                            }
                        }
                    });
                }
            }
        });
    }

    private void initialiseViewmodel(String login , String password, String deviceId,String devicename) {
        LoginViewModelFactory factory = new LoginViewModelFactory(login, password, deviceId,devicename,this);
        loginViewModel = new ViewModelProvider(this, factory).get(LoginViewModel.class);
    }
    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (TextUtils.isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == ',') {
                found = false;
            }
        }
        return String.valueOf(chars);
    }
//    private void displayLogs() {
//        try {
//            Process process = Runtime.getRuntime().exec("logcat -d");
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            StringBuilder log = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                log.append(line).append("\n");
//            }
//            Toast.makeText(this, log.toString(), Toast.LENGTH_LONG).show();
//           // logTextView.setText(log.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    private void saveLogsToFile() {
        String logFileName = "log_file.txt";
        String logFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + logFileName;

        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder log = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                log.append(line).append("\n");
            }

            // Save the log to a file
            FileWriter writer = new FileWriter(logFilePath);
            writer.write(log.toString());
            writer.close();

            Toast.makeText(this, "Logs saved to file: " + logFilePath, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showCustomDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate the custom layout
        View dialogView = getLayoutInflater().inflate(R.layout.custom_dilog, null);

        // Set the title and message
        TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
        dialogTitle.setText(title);

        TextView dialogMessage = dialogView.findViewById(R.id.dialog_message);
        dialogMessage.setText(message);

        // Set the custom view to the builder
        builder.setView(dialogView);

        // Create and show the dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

}
