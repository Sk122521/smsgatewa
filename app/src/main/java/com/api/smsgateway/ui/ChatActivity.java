package com.api.smsgateway.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.api.smsgateway.R;
import com.api.smsgateway.databinding.ActivityChatBinding;
import com.api.smsgateway.service.SMSService;
import com.api.smsgateway.ui.slideshow.SlideshowFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class ChatActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityChatBinding binding;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private SMSService myService;
    private boolean isBound = false;

    FloatingActionButton fab;
    private String callbackurl,username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("login_values", Context.MODE_PRIVATE);
       // callbackurl  = sharedPreferences.getString("callbackurl","");
        username = sharedPreferences.getString("username","dear user");

        setSupportActionBar(binding.appBarMain.toolbar);
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.SEND_SMS},
//                    PERMISSION_REQUEST_CODE);
//        }

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        View headerView = navigationView.getHeaderView(0);
        TextView headerNameTextView = headerView.findViewById(R.id.header_name);
        headerNameTextView.setText(username);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      //  switch (item.getItemId()) {

         if (item.getItemId() == R.id.logout) {

            }


//            case R.id.nav_logout:
//                Intent intent = new Intent(this, LauncherActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
//                break;
//            case R.id.logout:
//                Intent i = new Intent(this, LauncherActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(i);
//                finish();
//                // code to be executed if expression matches value1
//                  break;
        //}
        return super.onOptionsItemSelected(item);
    }
    private void showLogoutAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sign out confirmation");
        builder.setMessage("Do you want to signout from the application? This will prevent messages from being sent.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // OK button clicked

                SharedPreferences sharedPreferences = getSharedPreferences("login_values", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("logout",true);
                editor.apply();

                if (sharedPreferences.getBoolean("syncenabled",false)){
                    editor.putBoolean("syncenabled",false);
                    editor.apply();

                }
                // Dismiss progress dialog
               // progressDialog.dismiss();
                //  Toast.makeText(ChatActivity.this,
                //     Boolean.toString(sharedPreferences.getBoolean("logout",true)),
                //      Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ChatActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
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

