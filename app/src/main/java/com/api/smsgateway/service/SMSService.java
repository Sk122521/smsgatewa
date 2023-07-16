package com.api.smsgateway.service;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.Observer;

import com.api.smsgateway.model.Message;
import com.api.smsgateway.model.MessageResponse;
import com.api.smsgateway.repo.SmsRepo;
import com.api.smsgateway.utils.SimUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SMSService extends LifecycleService {
    private boolean isSendingMessages = false;
    private List<Message> messages;
    private static final String SENT = "SMS_SENT";
    MessageSentReceiver messageSentReceiver;
    DeliveredBroadcastReceiver deliveredBroadcastReceiver;

    Handler handler;
    Runnable runnable;

    //private ApiService apiService;
   // private Handler handler;

    // Replace with the desired phone number
    public class MyBinder extends Binder {
        public SMSService getService() {
            return SMSService.this;
        }
    }
    SmsRepo smsRepo = new SmsRepo();

    @Override
    public IBinder onBind(Intent intent) {
        //   return binder;
        super.onBind(intent);
        return new MyBinder();
    }

    // private final IBinder binder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void getmessages() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // Place your code here
               fetchData();

                // Schedule the next execution after X seconds
                handler.postDelayed(this,  5000); // X seconds * 1000 milliseconds
            }
        };
        handler.post(runnable);

    }

    private void fetchData() {
      //  Toast.makeText(this, "djdjjdj", Toast.LENGTH_SHORT).show();
        smsRepo.getmessages(this).observe((LifecycleOwner) this, new Observer<MessageResponse>() {
            @Override
            public void onChanged(MessageResponse messageResponse) {
                    messages = messageResponse.getMessages();
                    checkmessages();

            }
        });
    }

    private void checkmessages() {
        if (messages != null) {
           //  Toast.makeText(this, "message is not null", Toast.LENGTH_SHORT).show();
            for (Message message : messages) {
              //  isSendingMessages = true;
                //sendMessages("sshhshsh","252525258","1");
                sendMessages(message.getMessage(), message.getRecipient(),message.getId());
                // Start sending messages in a separate thread
//                new Thread(() -> {
//                //    while (isSendingMessages) {
//
//                        try {
//                            Thread.sleep(5000); // Adjust the delay between messages here
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                  //  }
//                }).start();
            }
        } else {
           // sendMessages("sshhshsh","252525258","1");
            Toast.makeText(this, "message is null", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);


        getmessages();
        // return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }


    private void sendMessages(String message, String recipient,String id) {
        //Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        setupSMSBroadcastReceiver();
        setupdeliveryBroadcastReceiver(id);
        PendingIntent sentIntent;
        PendingIntent deliveredPI;
        SharedPreferences sharedPreferences = this.getSharedPreferences("login_values", Context.MODE_PRIVATE);
        String sim = sharedPreferences.getString("sim","sim1");
//        String broadcastId = "SMS_SENT";
//        String deliverId = "SMS_DELIVERED";// + id;
        Intent intent  = new Intent("SMS_SENT");
        intent.putExtra("id",id);
        Intent i  = new Intent("SMS_DELIVERED");
        i.putExtra("id",id);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            //  Log.d("sentintent","null");
            //"SMS_DELIVERED"
            //SmsManager smsManager = SmsManager.getDefault();
            sentIntent = PendingIntent.getBroadcast(
                    this,
                    0,intent,
                    PendingIntent.FLAG_IMMUTABLE);
             deliveredPI = PendingIntent.getBroadcast(this, 0,
                    i, PendingIntent.FLAG_IMMUTABLE);

            // smsManager.sendTextMessage(recipient, null, message, sentIntent, null);
        } else {
            //  Log.d("sentintent","not null");
           // SmsManager smsManager = SmsManager.getDefault();
           // Toast.makeText(this, "gdgdggd", Toast.LENGTH_SHORT).show();
            sentIntent = PendingIntent.getBroadcast(
                    this,
                    0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            deliveredPI = PendingIntent.getBroadcast(this, 0,
                   i, PendingIntent.FLAG_UPDATE_CURRENT);


            //   smsManager.sendTextMessage(recipient, null, message, sentIntent, null);
        }
        //  PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), PendingIntent.FLAG_UPDATE_CURRENT);

        //  Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();
        // Add any additional logic for sending messages here
//        SmsManager smsManager = SmsManager.getDefault();
//        PendingIntent sentIntent = PendingIntent.getActivity(
//                this,
//                0, new Intent("SMS_SENT"),
//                PendingIntent.FLAG_IMMUTABLE);
//        smsManager.sendTextMessage(recipient, null, message, sentIntent, null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
          //  Toast.makeText(this, "gdgdggd", Toast.LENGTH_SHORT).show();
            SubscriptionManager localSubscriptionManager = SubscriptionManager.from(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

              //  Toast.makeText(SMSService.this, sim, Toast.LENGTH_SHORT).show();

                List localList = localSubscriptionManager.getActiveSubscriptionInfoList();

                if (sim.equals("sim1")){
                    SubscriptionInfo simInfo1 = (SubscriptionInfo) localList.get(0);

                    SmsManager.getSmsManagerForSubscriptionId(simInfo1.getSubscriptionId()).sendTextMessage(recipient, null, message, sentIntent, deliveredPI);
                           // .getSmsManagerForSubscriptionId(simInfo1.getSubscriptionId()).sendTextMessage(recipient, null, message, sentIntent, deliveredPI);


                }else {
                   //Toast.makeText(SMSService.this, sim, Toast.LENGTH_SHORT).show();
                    SubscriptionInfo simInfo2 = (SubscriptionInfo) localList.get(1);
                    SmsManager.getSmsManagerForSubscriptionId(simInfo2.getSubscriptionId()).sendTextMessage(recipient, null, message, sentIntent, deliveredPI);
                            //.getSmsManagerForSubscriptionId(simInfo2.getSubscriptionId()).sendTextMessage(recipient, null,message,sentIntent, deliveredPI);

                }

            }
        } else {
           // Toast.makeText(this, "gdgdggd", Toast.LENGTH_SHORT).show();
            SmsManager.getDefault().sendTextMessage(recipient, null, message,sentIntent, deliveredPI);
            //  Toast.makeText(getBaseContext(), R.string.sms_sending, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupdeliveryBroadcastReceiver(String id) {
       deliveredBroadcastReceiver = new DeliveredBroadcastReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_DELIVERED");
        intentFilter.addDataScheme(id);

        // Register the broadcast receiver with the intent filter for SMS_SENT action
      //  IntentFilter intentFilter = new IntentFilter("SMS_DELIVERED");
        registerReceiver(deliveredBroadcastReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        isSendingMessages = false;
    }
    private void setupSMSBroadcastReceiver() {
        // Create an instance of SMSBroadcastReceiver
        messageSentReceiver = new MessageSentReceiver();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_SENT");
      //  intentFilter.addDataScheme(id);
        // Register the broadcast receiver with the intent filter for SMS_SENT action
       // IntentFilter intentFilter = new IntentFilter("SMS_SENT");
        registerReceiver(messageSentReceiver, intentFilter);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //unregisterReceiver(messageSentReceiver);
        handler.removeCallbacks(runnable);
        return super.onUnbind(intent);
    }
}
