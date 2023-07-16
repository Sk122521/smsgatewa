package com.api.smsgateway.service;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.api.smsgateway.model.ReceiveRequest;
import com.api.smsgateway.repo.ReceivesmsRepo;

public class SmsReceiver extends BroadcastReceiver implements LifecycleOwner {
    private static final String TAG =
            SmsReceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";
    private Object lock = new Object();
    private boolean running = true;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

        // Get the SMS message.
        SharedPreferences sharedPreferences = context.getSharedPreferences("login_values", Context.MODE_PRIVATE);
        String deviceid = sharedPreferences.getString("deviceid", "121212");
        boolean receiverEnabled = sharedPreferences.getBoolean("receiver_enabled", true);
       // Toast.makeText(context, Boolean.toString(receiverEnabled), Toast.LENGTH_SHORT).show();
        if(receiverEnabled){
        //    Toast.makeText(context, Boolean.toString(receiverEnabled), Toast.LENGTH_SHORT).show();
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs;
            String strMessage = "";
            String format = bundle.getString("format");
            // Retrieve the SMS message received.
            Object[] pdus = (Object[]) bundle.get(pdu_type);
            if (pdus != null) {
                // Check the Android version.
                boolean isVersionM =
                        (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
                // Fill the msgs array.
                msgs = new SmsMessage[pdus.length];
                for (int i = 0; i < msgs.length; i++) {
                    // Check Android version and use appropriate createFromPdu.
                    if (isVersionM) {
                        // If Android version M or newer:
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                    } else {
                        // If Android version L or older:
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                    ReceivesmsRepo repository = new ReceivesmsRepo(context);

                    String result =  repository.submitmsg(new ReceiveRequest(msgs[i].getOriginatingAddress(), "", msgs[i].getMessageBody(), deviceid));

                    //  Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                    // Build the message to show.
                    strMessage += "SMS from " + msgs[i].getOriginatingAddress();
                    strMessage += " :" + msgs[i].getMessageBody() + "\n"+  msgs[i].getUserData().toString();
                    // Log and display the SMS message.
                    Log.d(TAG, "onReceive: " + strMessage);
                    //  Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
                }
            }
        }else{

        }



//        Toast.makeText(context, "hshhshsh", Toast.LENGTH_SHORT).show();
//        Log.i("mobile", "hgtyuy");
//            Bundle bundle = intent.getExtras();
//
//
//            String receiverPhoneNumber = getReceiverPhoneNumber(context);
//            if (bundle != null) {
//                Object[] pdus = (Object[]) bundle.get(EXTRA_PDUS);
//                if (pdus != null) {
//                    for (Object pdu : pdus) {
//                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
//                        String senderPhoneNumber = smsMessage.getOriginatingAddress();
//                        Log.d("mobile", senderPhoneNumber);
//                        if (senderPhoneNumber != null) {
//                            String messageBody = smsMessage.getMessageBody();
//                            // Do something with the received message
////                            ReceivesmsRepo repository = new ReceivesmsRepo(context);
////                            repository.submitmsg(new ReceiveRequest(senderPhoneNumber, receiverPhoneNumber, messageBody, "")).observe(this, new Observer<String>() {
////                                @Override
////                                public void onChanged(String s) {
////                                    Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
////                                }
////                            });
//                           // showNotification(context, messageBody);
//                        }
//                    }
//                }
//            }

    }

//    private void showNotification(Context context, String messageBody) {
//        Intent intent = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("New Message")
//                .setContentText(messageBody)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true);
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//        notificationManager.notify(0, builder.build());
//    }

    private String getReceiverPhoneNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
              //  return TODO;
                return telephonyManager.getLine1Number();
            }
        }
        return null;
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return null;
    }
}
