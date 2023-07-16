package com.api.smsgateway.service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.api.smsgateway.repo.checksentrepo;

public class DeliveredBroadcastReceiver extends BroadcastReceiver {
    checksentrepo repo = new checksentrepo();
    @Override
    public void onReceive(Context context, Intent intent) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences("login_values", Context.MODE_PRIVATE);
//        String deviceid = sharedPreferences.getString("deviceid", "121212");
        String id = intent.getStringExtra("id");
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                new checksentrepo().getmessages(id,"sent",context);
            //    showSnackBar(getString(R.string.sms_sent));
                //Analytics.track(AnalyticsEvents.SEND_REMINDER_SMS_APP_SUCCESS);
                Toast.makeText(context, "Message sent successfully", Toast.LENGTH_SHORT).show();
                break;
            default:
                new checksentrepo().getmessages(id,"Failed",context);
                Toast.makeText(context, "Failed to send the message", Toast.LENGTH_SHORT).show();
//                showSnackBar(getString(R.string.no_service_sms_failed));
//                Analytics.track(AnalyticsEvents.SEND_REMINDER_SMS_APP_FAILED);
                break;
        }
    }
}
