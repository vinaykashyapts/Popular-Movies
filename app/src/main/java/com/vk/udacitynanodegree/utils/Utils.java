package com.vk.udacitynanodegree.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.widget.Toast;

import com.vk.udacitynanodegree.R;
import com.vk.udacitynanodegree.models.MovieTrailers;

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

    public static void shareTrailer(Activity activity, MovieTrailers.Trailer trailer ) {
        activity.startActivity(Intent.createChooser(
                createShareIntent(activity, R.string.shareMsg, trailer.getName(), trailer.getKey()),
                activity.getString(R.string.shareTitle)));
    }

    public static Intent createShareIntent(Activity activity, int msgResId, String title, String key) {
        ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(activity)
                .setType("text/plain")
                .setText(activity.getString(msgResId, title, " http://www.youtube.com/watch?v=" + key));
        return builder.getIntent();
    }
}
