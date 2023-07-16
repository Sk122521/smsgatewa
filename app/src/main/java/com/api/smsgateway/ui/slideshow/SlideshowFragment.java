package com.api.smsgateway.ui.slideshow;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.P;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.text.PrecomputedText;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.api.smsgateway.R;
import com.api.smsgateway.databinding.FragmentSlideshowBinding;
import com.api.smsgateway.service.SMSService;
import com.api.smsgateway.service.SmsReceiver;
import com.api.smsgateway.ui.ChatActivity;
import com.api.smsgateway.ui.LoginActivity;
import com.api.smsgateway.utils.SimUtils;
import com.api.smsgateway.viewmodel.HeartbeatModel;
import com.api.smsgateway.viewmodel.HeartbeatvieModelFactory;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    ImageView rotatingIcon;
    SwitchMaterial syncsmsbtn,smsgatewaybtn,debugbtn;
    private ObjectAnimator rotationAnimator;

    public SMSService myService;
    public boolean isBound = false;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            SMSService.MyBinder binder = (SMSService.MyBinder) service;
            myService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String[] PERMISSIONS = {Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS};
    SimUtils simUtils = new SimUtils();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
  //  SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
 //   SharedPreferences.Editor editor = prefs.edit();
    //SharedPreferences prefs = getContext().getSharedPreferences("login_values", Context.MODE_PRIVATE);
   // SharedPreferences.Editor editor = prefs.edit();
    SmsReceiver smsReciever ;

    ArrayList<String> simOptions;
    public Intent intent;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentSlideshowBinding.inflate(inflater, container, false);

        rotatingIcon = binding.synchearbeatIcon;

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            rotatingIcon.setImageResource(R.drawable.ic_play);
        } else {
            rotatingIcon.setImageResource(R.drawable.ic_sync_animation);
        }

        setHasOptionsMenu(true);

        sharedPreferences = getContext().getSharedPreferences("login_values", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        intent = new Intent(getContext(), SMSService.class);
        String sim = sharedPreferences.getString("sim","sim1");

        String name = sharedPreferences.getString("username", "Dear User");


    //    getContext().registerReceiver(smsReciever, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));

        binding.username.setText("Hi! Dear "+ name);

        smsReciever = new SmsReceiver();

         rotatingIcon = binding.synchearbeatIcon;
         syncsmsbtn = binding.syncsmstb ;
         smsgatewaybtn = binding.smsgateway;
        // debugbtn = binding.Debugbtn;

        checksimpermissions();
        checksmssyncpermission();
        checksmsgateway();


        View root = binding.getRoot();
        simOptions = simUtils.mapSubscriptionIdToSim(getContext());
        simOptions.add("Random");
        Spinner spinner = binding.spinner;

     //   String[] simOptions = {"SIM1", "SIM2", "SIM3"};

// Create an ArrayAdapter using the dropdown options array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item,simOptions);

// Set the adapter to the spinner
        spinner.setAdapter(adapter);

        if (sim.equals("sim1")){
            spinner.setSelection(0);
        }else{
            spinner.setSelection(1);
        }


        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Saving Settings");

        rotationAnimator = ObjectAnimator.ofFloat(rotatingIcon, View.ROTATION, 0f, 360f);
        rotationAnimator.setDuration(1000);
        rotationAnimator.setRepeatCount(ObjectAnimator.INFINITE);

        rotatingIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (rotationAnimator.isRunning()) {
                   // rotationAnimator.cancel();
                } else {
                    rotationAnimator = ObjectAnimator.ofFloat(rotatingIcon, "rotation", 0f, 360f);
                    rotationAnimator.setDuration(500);
                    rotationAnimator.setRepeatCount(ObjectAnimator.INFINITE);
                    rotationAnimator.setRepeatMode(ObjectAnimator.RESTART);
                    rotationAnimator.start();
                    HeartbeatvieModelFactory factory= new HeartbeatvieModelFactory(getContext());
                    HeartbeatModel hvm = new ViewModelProvider(getActivity(),factory).get(HeartbeatModel.class);
                    hvm.checkhearbeat().observe(getActivity(), new Observer<Map<String, Object>>() {
                        @Override
                        public void onChanged(Map<String, Object> stringObjectMap) {
                            if (stringObjectMap != null){
                                String status = stringObjectMap.get("statuscode").toString() ;
                                String value = stringObjectMap.get("result").toString();
                                if(value.equals("yes")){
                                    rotationAnimator.cancel();
                                    Toast.makeText(getContext(), "status code : "+ status + ", Api running successfully", Toast.LENGTH_LONG).show();
                                }else{
                                    rotationAnimator.cancel();
                                    Toast.makeText(getContext(), "status code : "+ status + ", Error running API", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                rotationAnimator.cancel();
                                Toast.makeText(getContext(), ", Error running API", Toast.LENGTH_SHORT).show();
                                rotationAnimator.cancel();
                            }
                        }
                    });
//                    // Perform your task here
//                    // Once the task is completed, call rotationAnimator.cancel()
                }
            }
        });


        smsgatewaybtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    getContext().startService(intent);
                    getContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                    editor.putBoolean("syncenabled",true);
                    editor.apply();
                    //    fab.setImageResource(R.drawable.ic_play);
                    Toast.makeText(getContext(), "bounded", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "unbounded", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("syncenabled",false);
                    editor.apply();
                    getContext().stopService(intent);
                    getContext().unbindService(serviceConnection);
                }
            }
        });

     binding.saveBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             String simselected = spinner.getSelectedItem().toString();
           //  Toast.makeText(getContext(), simselected, Toast.LENGTH_SHORT).show();
             pd.show();
             if(simselected.equals("random")){
                 Random random = new Random();
                 int x =  random.nextInt(2) ;
                 if(x == 0){
                     editor.putString("sim","sim1");
                     editor.apply();
                     simUtils.setDefaultSmsSubscription(getContext(),"SIM 1");
                     pd.dismiss();
                     binding.saveBtn.setText("Saved");

                 }else{
                     editor.putString("sim","sim2");
                     editor.apply();
                     simUtils.setDefaultSmsSubscription(getContext(),"SIM 2");
                     pd.dismiss();
                     binding.saveBtn.setText("Saved");
                 }

             }else{
                 if (simselected.equals("SIM 1")){
                     editor.putString("sim","sim1");
                     editor.apply();
                 }else{
                     editor.putString("sim","sim2");
                     editor.apply();
                 }
                 simUtils.setDefaultSmsSubscription(getContext(),simselected);
                 pd.dismiss();
                 binding.saveBtn.setText("Saved");
             }
         }
     });
        syncsmsbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked ){
                 //   optimizebtn.setChecked(false);
                   editor.putBoolean("receiver_enabled", true);
                   editor.apply();
                   // getContext().registerReceiver(smsReciever, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
                }else{
                  //  Toast.makeText(myService, "", Toast.LENGTH_SHORT).show();
                    editor.putBoolean("receiver_enabled", false);
                    editor.apply();
                  //  getContext().unregisterReceiver(smsReciever);
                }
            }
        });

      //  smsMan.sendTextMessage(toNum, null, smsText, null, null);

        return root;
    }

    private void checksmsgateway() {
        if(sharedPreferences.getBoolean("syncenabled",false)){
          //  Toast.makeText(getContext(), "shhshsh", Toast.LENGTH_SHORT).show();
            getContext().startService(intent);
            getContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            smsgatewaybtn.setChecked(true);
        }else{
          //  Toast.makeText(getContext(), "shhjjjjjj", Toast.LENGTH_SHORT).show();
            smsgatewaybtn.setChecked(false);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (isBound) {
//           // getContext().unbindService(serviceConnection);
//           // isBound = false;
//            if(smsgatewaybtn.isChecked()){
//               // Toast.makeText(getContext(), "shhshsh", Toast.LENGTH_SHORT).show();
//                editor.putBoolean("syncenabled",true);
//                editor.apply();
//            }else{
//                 isBound = false;
//              //  Toast.makeText(getContext(), "ytytytyyt", Toast.LENGTH_SHORT).show();
//                editor.putBoolean("syncenabled",false);
//                editor.apply();
//            }
//        }
      //  editor.putBoolean("receiver_enabled", false);
     //   editor.apply();
    }


    @Override
    public void onResume() {
        super.onResume();
        checksmssyncpermission();
    }
    public void checksimpermissions(){
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted, proceed with SIM details retrieval
            simOptions = simUtils.mapSubscriptionIdToSim(getContext());
        }
    }
    public void checksmssyncpermission(){
        boolean receiverEnabled = sharedPreferences.getBoolean("receiver_enabled", false);
        if(receiverEnabled){
            syncsmsbtn.setChecked(true);
        }else{
            syncsmsbtn.setChecked(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout) {

            // Show progress dialog
           //ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Please wait", "Loading...");
            showLogoutAlertDialog();
// Create a new thread to execute the code
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    // Your code to be executed
//                    // Execute remaining code here
//                    // ...
//                }
//            });

// Start the thread
          //  thread.start();
        }



        return super.onOptionsItemSelected(item);

    }
    private void showLogoutAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Sign out confirmation");
        builder.setMessage("Do you want to signout from the application? This will prevent messages from being sent.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Please wait", "Logging out....");
                // OK button clicked
                editor.putBoolean("logout",true);
                editor.apply();

                if (sharedPreferences.getBoolean("syncenabled",false)){
                    editor.putBoolean("syncenabled",false);
                    editor.apply();
                    getContext().stopService(intent);
                    getContext().unbindService(serviceConnection);

                }
                // Dismiss progress dialog
                // progressDialog.dismiss();
                //  Toast.makeText(ChatActivity.this,
                //     Boolean.toString(sharedPreferences.getBoolean("logout",true)),
                //      Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
                progressDialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel button clicked
                dialog.dismiss();
               // progressDialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        // Access the options menu of the parent Activity
        MenuItem menuItem = menu.findItem(R.id.logout);
        // Manipulate the menu item as needed
    }
}