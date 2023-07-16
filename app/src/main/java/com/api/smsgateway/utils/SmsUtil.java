package com.api.smsgateway.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

public class SmsUtil {
    public static void openAppPermissionsSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
     //   intent.setAction(Settings.ACTION_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(),null );
        intent.setData(uri);
        context.startActivity(intent);
    }



}
