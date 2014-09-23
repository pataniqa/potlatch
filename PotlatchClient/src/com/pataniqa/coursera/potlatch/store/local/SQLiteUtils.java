package com.pataniqa.coursera.potlatch.store.local;

import java.util.Map;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class SQLiteUtils {

    private static final String LOG_TAG = SQLiteUtils.class.getCanonicalName();

    public static void createDatabase(SQLiteDatabase db,
            String tableName,
            String idColumn,
            Map<String, String> columns) {
        StringBuilder createTable = new StringBuilder();
        createTable.append("create table if not exists " + tableName + " (");
        createTable.append(idColumn + " integer primary key autoincrement ");
        for (Map.Entry<String, String> entry : columns.entrySet()) {
            if (!entry.getKey().equals(PotlatchSchema.Cols.ID))
                createTable.append(", " + entry.getKey() + " " + entry.getValue());
        }
        createTable.append(");");

        Log.d(LOG_TAG, "onCreate() called: " + createTable.toString());

        try {
            db.execSQL(createTable.toString());
        } catch (SQLException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
    }

    public static ContentValues initializeWithDefault(Map<String, String> columns,
            final ContentValues assignedValues) {
        final ContentValues setValues = (assignedValues == null) ? new ContentValues()
                : assignedValues;
        for (Map.Entry<String, String> entry : columns.entrySet()) {
            String key = entry.getKey();
            if (!key.equals(BaseColumns._ID)) {
                if (!setValues.containsKey(key)) {
                    if (entry.getValue().equals(PotlatchSchema.INTEGER))
                        setValues.put(key, 0);
                    else
                        setValues.put(key, "");
                }
            }
        }
        return setValues;
    }

}
