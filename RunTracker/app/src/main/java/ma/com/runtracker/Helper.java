package ma.com.runtracker;

import android.content.Context;
import android.content.pm.PackageManager;

public class Helper {
    private static Helper helper;
    private Context context;

    private Helper (Context c) {
        context = c;
    }

    public static Helper get(Context c) {
        if (helper == null) {
            helper = new Helper(c.getApplicationContext());
        }
        return helper;
    }

    public boolean checkWriteExternalPermission(String permission) {
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
}
