package com.api.smsgateway.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.api.smsgateway.utils.BatteryUtil;
import com.api.smsgateway.utils.SmsUtil;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.api.smsgateway.databinding.ActivityPermissionsBinding;

import com.api.smsgateway.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class PermissionsActivity extends AppCompatActivity {

    private ActivityPermissionsBinding binding;
    SwitchMaterial smspermoissionbtn;
    private static final int PERMISSION_REQUEST_CODE = 1;

    PowerManager powerManager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String[] PERMISSIONS = {Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS};
    private static final String[] PHNPERMISSIONS = {Manifest.permission.READ_PHONE_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPermissionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("login_values", Context.MODE_PRIVATE);

        if(checkPermissions() && checksimpermissions()){

            if(!sharedPreferences.getBoolean("logout",true)){
                Intent intent =  new Intent(PermissionsActivity.this, ChatActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else{
                Intent intent  = new Intent(PermissionsActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }else{
            Toast.makeText(PermissionsActivity.this, "You need to grant SMS permission and phone state permissions both", Toast.LENGTH_SHORT).show();
        }

        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        sharedPreferences = getSharedPreferences("permissions", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

//        editor.putString("callbackurl", callbackurl);
//        editor.putString("username", username);
//        editor.putString("deviceid",deviceId);

        editor.apply();

        smspermoissionbtn = binding.smspermissionbtn;

       // checkbatterypermission();
        checksmspermission();

        checkphonepermissiosn();
        //checkAutostartpermission();
        showAutostartAlertDialog();
        showBatteryAlertDialog();


        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Saving Settings");



        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermissions() && checksimpermissions()){
                    if(!sharedPreferences.getBoolean("logout",true)){
                        Intent intent =  new Intent(PermissionsActivity.this, ChatActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        Intent intent  = new Intent(PermissionsActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(PermissionsActivity.this, "You need to grant SMS and Phone permission", Toast.LENGTH_SHORT).show();
                }
            }
        });

        smspermoissionbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    requestPermissions();
                }else{
                    SmsUtil.openAppPermissionsSettings(PermissionsActivity.this);
                }
            }
        });
        binding.phnpermissionbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    requestphonepermissions();
                }else{
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    //   intent.setAction(Settings.ACTION_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(),null );
                    intent.setData(uri);
                    startActivity(intent);
                }
            }
        });
    }

//    private void checkAutostartpermission() {
//        if (sharedPreferences.getBoolean("Autostart",false)){
//            optimizebtn.setChecked(true);
//        }else{
//            optimizebtn.setChecked(false);
//        }

//        if (isAutostartEnabled()){
//
//        }else{
//
//        }

  //  }


    private void openAutostartSettings() {
        Intent intent = new Intent();
        String manufacturer = Build.MANUFACTURER.toLowerCase();
        if (manufacturer.equals("xiaomi")) {
            //Toast.makeText(this, "ioio", Toast.LENGTH_SHORT).show();
            intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
        } else if (manufacturer.equals("oppo")) {
            intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
        } else if (manufacturer.equals("vivo")) {
            intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
        } else if (manufacturer.equals("asus")) {
            intent.setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.autostart.AutoStartActivity"));
        } else if ("Letv".equalsIgnoreCase(manufacturer)) {
            intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
        } else if ("Honor".equalsIgnoreCase(manufacturer)) {
            intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
        }
        else {
            // For other manufacturers, open the general settings screen
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        }

        startActivity(intent);
    }
  //  public void checkbatterypermission() {
//        if (BatteryUtil.isBatteryOptimizationSupported(PermissionsActivity.this)){
//            if (BatteryUtil.isBatteryOptimizationEnabled(PermissionsActivity.this)){
//                optimizebtn.setChecked(true);
//            }else{
//                optimizebtn.setChecked(false);
//            }
//        }else{
//            optimizebtn.setChecked(false);
//        }
//        if(sharedPreferences.getBoolean("Norestriction",false)){
//            optimizebtn.setChecked(true);
//        }else{
//            optimizebtn.setChecked(false);
//        }
//        if (sharedPreferences.getBoolean("Norestriction",false)){
//            //optimizebtn.setChecked(true);
//            optimizebtn.setChecked(true);
//        }else{
//            //optimizebtn.setChecked(false);
//            optimizebtn.setChecked(false);
//        }

 //   }

public  void startNorestrictionpage(){
    Intent intent = new Intent( "miui.intent.action.POWER_HIDE_MODE_APP_LIST");
    //"miui.intent.action.POWER_HIDE_MODE_APP_LIST"
    intent.addCategory("android.intent.category.DEFAULT");
//    Uri uri = Uri.fromParts("package", getPackageName(),null );
//    intent.setData(uri);
    // String callbackurl  = sharedPreferences.getString("callbackurl","");
    try {
        // Toast.makeText(PermissionsActivity.this, "yes", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    } catch (ActivityNotFoundException e) {
//          Toast.makeText(PermissionsActivity.this, "no", Toast.LENGTH_SHORT).show();
        // Handle the case where the battery saver settings activity is not found
        // Display an error message or provide an alternative action
        e.printStackTrace();
    }
}
    private boolean checkPermissions() {
        for (String permission : PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }else {
                return  false;
            }
        }
        return false;
    }

    public Boolean checksimpermissions(){
        for (String permission : PHNPERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }else {
                return  false;
            }
        }
        return false;
    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    private void requestphonepermissions(){
        ActivityCompat.requestPermissions(this, PHNPERMISSIONS, PERMISSION_REQUEST_CODE);
    }
    private void checkphonepermissiosn() {
            if (checksimpermissions()){
                binding.phnpermissionbtn.setChecked(true);
            }else{
                binding.phnpermissionbtn.setChecked(false);
            }
    }

    public void checksmspermission(){
        if(checkPermissions()){
            smspermoissionbtn.setChecked(true);
        }else{
            smspermoissionbtn.setChecked(false);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
      //  checkbatterypermission();
        checksmspermission();
        checkphonepermissiosn();
      //  checkAutostartpermission();
    }
//
//    private boolean isAutostartEnabled() {
//        String manufacturer = Build.MANUFACTURER.toLowerCase();
//
//        if (manufacturer.equals("xiaomi")) {
//            return isComponentEnabled("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity");
//        } else if (manufacturer.equals("oppo")) {
//            return isComponentEnabled("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity");
//        } else if (manufacturer.equals("vivo")) {
//            return isComponentEnabled("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity");
//        } else if (manufacturer.equals("asus")) {
//            return isComponentEnabled("com.asus.mobilemanager", "com.asus.mobilemanager.autostart.AutoStartActivity");
//        }  else if ("Letv".equalsIgnoreCase(manufacturer)) {
//          return isComponentEnabled("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity");
//        } else if ("Honor".equalsIgnoreCase(manufacturer)) {
//           return isComponentEnabled("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity");
//        }
//        else {
//            // For other manufacturers, return false as there's no specific autostart setting
//            return false;
//        }
//    }
//    private boolean isComponentEnabled(String packageName, String className) {
//        try {
////            PackageManager pm = getPackageManager();
////            ComponentName componentName = new ComponentName(packageName, className);
////            int state = pm.getComponentEnabledSetting(componentName);
//            ContentResolver contentResolver = getContentResolver();
//            String autostartValue = Settings.Secure.getString(contentResolver, "miui_autostart");
//         //   Toast.makeText(this,autostartValue, Toast.LENGTH_SHORT).show();
//            return true;//state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
private void showAutostartAlertDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Autostart");
    builder.setMessage("Please enable Autostart button.");
    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            editor.putBoolean("Autostart",true);
            openAutostartSettings();
            // OK button clicked
            dialog.dismiss();
        }
    });
    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            editor.putBoolean("Autostart",false);
           // openAutostartSettings();
            // Cancel button clicked
            dialog.dismiss();
        }
    });
    AlertDialog dialog = builder.create();
    dialog.show();
}
    private void showBatteryAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No battery restriction");
        builder.setMessage("Please Click on No battery Restriction on battery saver.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // OK button clicked
                editor.putBoolean("Norestriction",true);
                startNorestrictionpage();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cancel button clicked
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}