package com.maxistar.textpad.utils;

import android.app.Activity;
/**
 * Contiene informacion referida al sistema
 */
public class System {
    /**
     * Abandona la app
     * @param activity
     */
    static public void exitFromApp(Activity activity) {
        activity.finish();
        java.lang.System.exit(0);
    }
}
