package com.vk.udacitynanodegree.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;

import com.vk.udacitynanodegree.db.metaData.DataType;
import com.vk.udacitynanodegree.db.metaData.TableColumn;
import com.vk.udacitynanodegree.utils.Logger;

import java.util.ArrayList;

public abstract class CpContent {
    private static final String LOG_TAG = CpContent.class.getSimpleName();
    public static final Uri CONTENT_URI = Uri.parse("content://" + CpProvider.PROVIDER_NAME);

    public static String tableName;
    public ArrayList<TableColumn> columns = new ArrayList<>();

    public CpContent(String tableName) {
        this.tableName = tableName;
    }

    public static void createTable(SQLiteDatabase db, String tableName, ArrayList<TableColumn> columns) {
        if (tableName == null || tableName.isEmpty()) {
            throw new NullPointerException("Table name is missing!");
        }

        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sb.append(tableName);
        sb.append(" ( ");
        for (TableColumn tableColumn : columns) {
            sb.append(tableColumn.getColumnName());
            sb.append(" ");
            sb.append(tableColumn.getType());
            if (tableColumn.isPrimaryKey()) {
                sb.append(" PRIMARY KEY ");
                if (tableColumn.isAutoIncrement()) {
                    sb.append(" AUTOINCREMENT ");
                }
            }
            if (tableColumn.isUnique()) {
                sb.append(" not null unique ");
            }
            sb.append(" DEFAULT '' ");
            sb.append(" , ");
        }
        String sqlQuery = sb.toString();
        sqlQuery = sqlQuery.substring(0, sqlQuery.lastIndexOf(","));
        sqlQuery += ")";

        db.execSQL(sqlQuery);
        Logger.logInfo(LOG_TAG, sqlQuery);
    }

    // Version 1 : Creation of the table
    public static void upgradeTable(SQLiteDatabase db, String tableName, ArrayList<TableColumn> columns, int oldVersion, int newVersion) {
        Logger.logError(LOG_TAG, tableName + " | upgradeTable start");

        if (tableName == null || tableName.isEmpty()) {
            throw new NullPointerException("Table name is missing!");
        }

        if (oldVersion < 1) {
            Logger.logError(LOG_TAG, "Upgrading from version " + oldVersion + " to " + newVersion + ", data will be lost!");

            db.execSQL("DROP TABLE IF EXISTS " + tableName + ";");
            createTable(db, tableName, columns);
            return;
        }

        if (oldVersion != newVersion) {
            throw new IllegalStateException("Error upgrading the database to version "
                    + newVersion);
        }
    }

    public String getBulkInsertString() {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(tableName);
        sb.append(" ( ");
        for (TableColumn tableColumn : columns) {
            sb.append(tableColumn.getColumnName());
            sb.append(", ");
        }
        sb.append(" ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)").toString();
        return sb.toString();
    }

    public void bindValuesInBulkInsert(SQLiteStatement stmt, ContentValues values) {
        int i = 1;

        for (TableColumn tableColumn : columns) {
            String columnName = tableColumn.getColumnName();
            String value = values.getAsString(columnName);
            switch (tableColumn.getType()) {
                case DataType.INTEGER:
                    stmt.bindLong(i++, values.getAsInteger(columnName));
                    break;
                case DataType.TEXT:
                    stmt.bindString(i++, value != null ? value : "");
                    break;
                case DataType.DOUBLE:
                    stmt.bindDouble(i++, values.getAsDouble(columnName));
                    break;
                case DataType.LONG:
                    stmt.bindLong(i++, values.getAsLong(columnName));
                    break;
                case DataType.BLOB:
                    stmt.bindBlob(i++, values.getAsByteArray(columnName));
                    break;
            }
        }
    }

    public String[] getProjection() {
        String projection[] = new String[columns.size()];
        int columnCount = columns.size();
        for (int j = 0; j < columnCount; j++) {
            projection[j] = columns.get(j).getColumnName();
        }
        return projection;
    }
}