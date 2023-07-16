package com.api.smsgateway.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

    public class BatteryUtil {

        public static boolean isBatteryOptimizationEnabled(Context context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                return !powerManager.isIgnoringBatteryOptimizations(context.getPackageName());
            } else {
                // Battery optimization not applicable for pre-M devices
                return false;
            }
        }

        public static void openBatteryOptimizationSettings(Context context) {
            Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
            context.startActivity(intent);
        }

        public static boolean isBatteryOptimizationSupported(Context context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PackageManager packageManager = context.getPackageManager();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                return intent.resolveActivity(packageManager) != null;
            } else {
                // Battery optimization not applicable for pre-M devices
                return false;
            }
        }


}
