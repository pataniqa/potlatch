package com.pataniqa.coursera.potlatch.store.local;

import android.database.sqlite.SQLiteOpenHelper;

import com.pataniqa.coursera.potlatch.model.HasID;

class Base <T extends HasID>  {

    Creator<T> creator;
    String tableName;
    String id;
    SQLiteOpenHelper helper;
}