package nanodegree.udacity.vk.com.udacitynanodegree.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * Created by cropin on 20/7/16.
 */
public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    public static void showToast(Context c, String msg) {

        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }
}
