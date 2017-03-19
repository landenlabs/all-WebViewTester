package com.landenlabs.all_webviewtester.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

/**
 * Created by Dennis Lang on 8/15/16.
 */
public class MemUtil {
    public static String getMem(Activity activity) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager)
                activity.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        String memStr;
        if (Build.VERSION.SDK_INT > 15) {
            memStr = String.format(" Mem Avail:%,d Free:%,d ", mi.availMem, mi.totalMem - mi.availMem);
        } else {
            memStr = String.format(" Mem Avail:%,%d ", mi.availMem);
        }

        return memStr;
    }
}
