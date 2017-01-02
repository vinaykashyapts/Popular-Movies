package com.vk.udacitynanodegree.db;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.provider.BaseColumns;

import com.vk.udacitynanodegree.models.MovieItem;

import java.util.ArrayList;

public final class CpProvider extends ContentProvider {

    private static final String LOG_TAG = CpProvider.class.getSimpleName();

    public static final String PROVIDER_NAME = "com.vk.udacitynanodegree.db.CpProvider";

    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private enum UriType {
        MOVIES(MovieItem.TABLE_NAME, MovieItem.TABLE_NAME, MovieItem.TYPE_TABLE, true) {
            @Override
            public CpContent getObject() {
                return new MovieItem();
            }
        };

        private String mTableName;
        private String mType;
        private boolean isTable;

        UriType(String matchPath, String tableName, String type, boolean isTable) {
            mTableName = tableName;
            mType = type;
            this.isTable = isTable;
            sUriMatcher.addURI(PROVIDER_NAME, matchPath, ordinal());
        }

        String getTableName() {
            return mTableName;
        }

        String getType() {
            return mType;
        }

        public boolean isTable() {
            return isTable;
        }

        public abstract CpContent getObject();
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    static {
        // Ensures UriType is initialized
        UriType.values();
    }

    private static UriType matchUri(Uri uri) {
        int match = sUriMatcher.match(uri);
        if (match < 0) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return UriType.class.getEnumConstants()[match];
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        UriType uriType = matchUri(uri);
        Context context = getContext();

        // Pick the correct database for this operation
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
        String id;

        //  Logger.logInfo(LOG_TAG, "delete: uri=" + uri + ", match is " + uriType.name());

        int result = -1;

        if (uriType.isTable()) {
            result = db.delete(uriType.getTableName(), selection, selectionArgs);
        } else {
            id = uri.getPathSegments().get(1);
            result = db.delete(uriType.getTableName(), whereWithId(selection),
                    addIdToSelectionArgs(id, selectionArgs));
        }

        getContext().getContentResolver().notifyChange(uri, null, true);
        return result;
    }

    @Override
    public String getType(Uri uri) {
        return matchUri(uri).getType();
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        UriType uriType = matchUri(uri);
        Context context = getContext();

        // Pick the correct database for this operation
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
        long id;

        //    Logger.logInfo(LOG_TAG, "insert: uri=" + uri + ", match is " + uriType.name());

        Uri resultUri = null;

        if (uriType.isTable()) {
            id = db.insertWithOnConflict(uriType.getTableName(), "foo", values, SQLiteDatabase.CONFLICT_REPLACE);
            resultUri = id == -1 ? null : ContentUris.withAppendedId(uri, id);
        }
        // Notify with the base uri, not the new uri (nobody is watching a new
        // record)
        getContext().getContentResolver().notifyChange(uri, null, true);
        return resultUri;
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        SQLiteDatabase db = DBHelper.getInstance(getContext()).getWritableDatabase();
        db.beginTransaction();
        try {
            int numOperations = operations.size();
            ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
                db.yieldIfContendedSafely();
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        UriType uriType = matchUri(uri);
        Context context = getContext();

        // Pick the correct database for this operation
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();

        //  Logger.logInfo(LOG_TAG, "bulkInsert: uri=" + uri + ", match is " + uriType.name());

        int numberInserted = 0;
        db.beginTransaction();
        try {
            CpContent cpContent = uriType.getObject();
            numberInserted = bulkInsert(values, db, cpContent);
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }

        // Notify with the base uri, not the new uri (nobody is watching a new
        // record)
        context.getContentResolver().notifyChange(uri, null, true);
        return numberInserted;
    }

    private int bulkInsert(ContentValues[] values, SQLiteDatabase db, CpContent cpContent) {
        SQLiteStatement insertStmt;
        int numberInserted;
        insertStmt = db.compileStatement(cpContent.getBulkInsertString());
        for (ContentValues value : values) {
            cpContent.bindValuesInBulkInsert(insertStmt, value);
            insertStmt.execute();
            insertStmt.clearBindings();
        }
        insertStmt.close();
        numberInserted = values.length;

        //    Logger.logInfo(LOG_TAG, "bulkInsert: uri=" + uri + " | nb inserts : " + numberInserted);
        return numberInserted;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Cursor c = null;
        UriType uriType = matchUri(uri);
        Context context = getContext();
        // Pick the correct database for this operation
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
        String id;

        //     Logger.logInfo(LOG_TAG, "query: uri=" + uri + ", match is " + uriType.name());

        if (uriType.isTable()) {
            c = db.query(uriType.getTableName(), projection, selection, selectionArgs,
                    null, null, sortOrder);
        } else {
            id = uri.getPathSegments().get(1);
            c = db.query(uriType.getTableName(), projection, whereWithId(selection),
                    addIdToSelectionArgs(id, selectionArgs), null, null, sortOrder);
        }

        if ((c != null) && !isTemporary()) {
            c.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return c;
    }

    private String whereWithId(String selection) {
        StringBuilder sb = new StringBuilder(256);
        sb.append(BaseColumns._ID);
        sb.append(" = ?");
        if (selection != null) {
            sb.append(" AND (");
            sb.append(selection);
            sb.append(')');
        }
        return sb.toString();
    }

    private String[] addIdToSelectionArgs(String id, String[] selectionArgs) {

        if (selectionArgs == null) {
            return new String[]{id};
        }

        int length = selectionArgs.length;
        String[] newSelectionArgs = new String[length + 1];
        newSelectionArgs[0] = id;
        System.arraycopy(selectionArgs, 0, newSelectionArgs, 1, length);
        return newSelectionArgs;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        UriType uriType = matchUri(uri);
        // Pick the correct database for this operation
        SQLiteDatabase db = DBHelper.getInstance(getContext()).getWritableDatabase();

        //   Logger.logInfo(LOG_TAG, "update: uri=" + uri + ", match is " + uriType.name());

        int result = -1;
        if (uriType.isTable()) {
            result = db.update(uriType.getTableName(), values, selection, selectionArgs);
        } else {
            String id = uri.getPathSegments().get(1);
            result = db.update(uriType.getTableName(), values, whereWithId(selection),
                    addIdToSelectionArgs(id, selectionArgs));
        }

        getContext().getContentResolver().notifyChange(uri, null, true);
        return result;
    }
}