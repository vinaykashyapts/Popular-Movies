package com.vk.udacitynanodegree.db.metaData;

import android.support.annotation.NonNull;

/**
 * Created by Vinay on 11/5/16.
 */
public final class TableColumn implements DataType {

    private String columnName;
    private String type;
    private boolean isPrimaryKey;
    private boolean isAutoIncrement;
    private String value;

    public boolean isUnique() {
        return isUnique;
    }

    private boolean isUnique;

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TableColumn() {

    }

    public TableColumn(@NonNull String columnName, @NonNull String type) {
        this(columnName, type, false, false);
    }

    public TableColumn(@NonNull String columnName, @NonNull String type, boolean isPrimaryKey, boolean isAutoIncrement) {
        this.columnName = columnName;
        this.type = type;
        this.isPrimaryKey = isPrimaryKey;
        this.isAutoIncrement = isAutoIncrement;
    }

    public TableColumn(@NonNull String columnName, @NonNull String type, boolean isUnique) {
        this.columnName = columnName;
        this.type = type;
        this.isUnique = isUnique;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

}
