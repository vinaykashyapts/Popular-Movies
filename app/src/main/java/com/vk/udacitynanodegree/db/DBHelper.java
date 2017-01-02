package com.vk.udacitynanodegree.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.vk.udacitynanodegree.models.MovieItem;

/**
 * Created by Vinay on 12/5/16.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = DBHelper.class.getSimpleName();
    protected static final String DATABASE_NAME = "VkNanodegree.db";
    public static final int DATABASE_VERSION = 1;

    private static DBHelper dbHelper;

    public static synchronized DBHelper getInstance(Context context) {
        // Always return the cached database, if we've got one
        if (dbHelper == null) {
            dbHelper = new DBHelper(context);
        }

        return dbHelper;
    }

    private DBHelper(Context context) {
//        super(mActivity, DATABASE_NAME, null, DATABASE_VERSION);
        super(context, Environment.getExternalStorageDirectory() + "/" + DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "Creating CpProvider database");

        // Create all tables here; each class has its own method
        MovieItem.createTable(db, MovieItem.TABLE_NAME, MovieItem.getMovieColumns());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Upgrade all tables here; each class has its own method
        MovieItem.upgradeTable(db, MovieItem.TABLE_NAME, MovieItem.getMovieColumns(), oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
    }
}