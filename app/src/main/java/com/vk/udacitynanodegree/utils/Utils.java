package com.vk.udacitynanodegree.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by Vinay on 20/7/16.
 */
public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }

    public static void showToast(Context c, String msg) {

        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }

    public static void watchYoutubeVideo(Context c, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            c.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            c.startActivity(webIntent);
        }
    }
}
