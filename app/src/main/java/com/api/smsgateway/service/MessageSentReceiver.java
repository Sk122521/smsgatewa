package com.api.smsgateway.service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.api.smsgateway.model.ReceiveResponse;
import com.api.smsgateway.repo.ReceivesmsRepo;
import com.api.smsgateway.repo.checksentrepo;

public class MessageSentReceiver extends BroadcastReceiver implements LifecycleOwner {
   // checksentrepo  repo = new checksentrepo();
    @Override
    public void onReceive(Context context, Intent intent) {
      //  SharedPreferences sharedPreferences = context.getSharedPreferences("login_values", Context.MODE_PRIVATE);
       // String id = sharedPreferences.getString("deviceid", "121212");
       // String id = broadcastId.replace("SMS_SENT_", "");
       String id = intent.getStringExtra("id");
        Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                new checksentrepo().getmessages(id,"delivered",context);
                // Message sent successfully
//                repo.getmessages(deviceid,"delivered").observe(this, new Observer<String>() {
//                    @Override
//                    public void onChanged(String s) {
//                        if(s.equals("response got")){
//                            Toast.makeText(context, "message delivered", Toast.LENGTH_SHORT).show();
//                        }else{
//                            Toast.makeText(context, "message delivered but api not working ", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
              // Toast.makeText(context, "Message delivered successfully", Toast.LENGTH_SHORT).show();
                break;
            default:
                new checksentrepo().getmessages(id,"Failed",context);
                Toast.makeText(context, "Failed to send the message", Toast.LENGTH_SHORT).show();
//                showSnackBar(getString(R.string.no_service_sms_failed));
//                Analytics.track(AnalyticsEvents.SEND_REMINDER_SMS_APP_FAILED);
                break;
            // Handle other possible results as needed
        }
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return null;
    }

}
