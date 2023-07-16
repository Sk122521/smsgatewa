package com.api.smsgateway.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SimUtils {


//    public static void setDefaultSmsSubscription(Context context, int subscriptionId) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
//            SubscriptionManager subscriptionManager = SubscriptionManager.from(context);
//            if (subscriptionManager != null) {
//                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                    SubscriptionInfo subscriptionInfo = subscriptionManager.getActiveSubscriptionInfo(subscriptionId);
//                    if (subscriptionInfo != null) {
//                        subscriptionManager.set(subscriptionInfo.getSubscriptionId());
//                    }
//                }
//            }
//        }
//    }

    public static SubscriptionInfo getDefaultSmsSubscriptionId(Context context,String sim) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager subscriptionManager = SubscriptionManager.from(context);
            if (subscriptionManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
                        if (subscriptionInfoList != null) {
                            if (sim.equals("sim1")){
                                return  subscriptionInfoList.get(0);
                            }else{
                                return subscriptionInfoList.get(1);
                            }
//                            for (SubscriptionInfo subscriptionInfo : subscriptionInfoList) {
//
//
//                                int subscriptionId = subscriptionInfo.getSubscriptionId();
//                                int simSlotIndex = subscriptionInfo.getSimSlotIndex();
//                                String simDisplayName = "SIM " + (simSlotIndex + 1);
//                                if ()
//                                //   Toast.makeText(context, simDisplayName, Toast.LENGTH_SHORT).show();
//                            }
                        }
                    }
                    return null;
                }
            }
        }
        return null;
    }


    public static List<SubscriptionInfo> getActiveSimCards(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager subscriptionManager = SubscriptionManager.from(context);
            if (subscriptionManager != null) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return subscriptionManager.getActiveSubscriptionInfoList();
                }
            }
        }
        return null;
    }

    public static boolean isMultiSimEnabled(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return telephonyManager.getPhoneCount() > 1;
            }
        }
        return false;
    }

    public static String getSimDisplayName(Context context, int subscriptionId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager subscriptionManager = SubscriptionManager.from(context);
            if (subscriptionManager != null) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    SubscriptionInfo subscriptionInfo = subscriptionManager.getActiveSubscriptionInfo(subscriptionId);
                    if (subscriptionInfo != null) {
                        return subscriptionInfo.getDisplayName().toString();
                    }
                }
            }
        }
        return "";
    }

    public static ArrayList<String> mapSubscriptionIdToSim(Context context) {
        ArrayList<String> simnames = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager subscriptionManager = SubscriptionManager.from(context);

            if (subscriptionManager != null) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                 //   Toast.makeText(context, "ddddjjjss", Toast.LENGTH_SHORT).show();
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                    // Check if SIM card is available
                    if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY) {
                        // Get SIM details
                      //  String simOperatorName = telephonyManager.getSimOperatorName();
                    //    String simSerialNumber = telephonyManager.getSimSerialNumber();

                        // Display SIM details
                     //   System.out.println("SIM Operator Name: " + simOperatorName);
                      //  System.out.println("SIM Serial Number: " + simSerialNumber);

                        // Get SIM names and details for dual SIM devices
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                            // Get active subscription info list
                            final List<SubscriptionInfo> activeSubscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
                            if (activeSubscriptionInfoList != null) {
                                for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
                                  //  String displayName = subscriptionInfo.getCardId().toString();
                                    int simSlotIndex = subscriptionInfo.getSimSlotIndex();
                                    String displayName = "SIM " + (simSlotIndex + 1);
                                   // String simNumber = subscriptionInfo.getNumber();

                                    simnames.add(displayName);
                                  //  Toast.makeText(context, displayName + "  "+simNumber , Toast.LENGTH_SHORT).show();
                                    // Display SIM names and details
                                }
                            }
                        }
                    }
                }
            }
        }

        return simnames;
    }

    public static void setDefaultSmsSubscription(Context context, String simSelection) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {

            SubscriptionManager subscriptionManager = SubscriptionManager.from(context);
            if (subscriptionManager != null) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    List<SubscriptionInfo> subscriptionInfoList = subscriptionManager.getActiveSubscriptionInfoList();
                    if (subscriptionInfoList != null) {
                        for (SubscriptionInfo subscriptionInfo : subscriptionInfoList) {
                            int subscriptionId = subscriptionInfo.getSubscriptionId();
                            int simSlotIndex = subscriptionInfo.getSimSlotIndex();
                            String simDisplayName = "SIM " + (simSlotIndex + 1);
                         //   Toast.makeText(context, simDisplayName, Toast.LENGTH_SHORT).show();
                            if (simSelection.equalsIgnoreCase("SIM 1") && simDisplayName.contains("SIM 1")) {
                                setDefaultSmsSubscriptionId(context, subscriptionId);
                                break;
                            } else if (simSelection.equalsIgnoreCase("SIM 2") && simDisplayName.contains("SIM 2")) {
                                setDefaultSmsSubscriptionId(context, subscriptionId);
                                break;
                            }
                        }
                    }
                }

            }
        }
    }

    private static void setDefaultSmsSubscriptionId(Context context, int subscriptionId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                try {
                    Method setSmsSubscriptionIdMethod = TelephonyManager.class.getMethod("setDefaultSmsSubscriptionId", int.class);
                    setSmsSubscriptionIdMethod.invoke(telephonyManager, subscriptionId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

