package com.pataniqa.coursera.potlatch.store.local;

import java.util.Map;

import lombok.Getter;
import lombok.experimental.Accessors;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pataniqa.coursera.potlatch.store.DataService;
import com.pataniqa.coursera.potlatch.store.GiftChains;
import com.pataniqa.coursera.potlatch.store.GiftMetadata;
import com.pataniqa.coursera.potlatch.store.Users;

@Accessors(fluent=true)
public class LocalService implements DataService {
    
    @Getter private final LocalGiftQuery gifts;
    @Getter private final GiftChains giftChains;
    @Getter private final GiftMetadata giftMetadata;
    @Getter private final Users users;

    public LocalService(Context context) {
        LocalDatabase helper = new LocalDatabase(context);
        users = new LocalUserStore(helper);
        giftChains = new LocalGiftChainStore(helper);
        LocalGiftStore store = new LocalGiftStore(helper, users, giftChains);
        gifts = new LocalGiftQuery(helper, users, giftChains, store);
        giftMetadata = new LocalGiftMetadataStore(helper, gifts);
    }
}

class LocalDatabase extends SQLiteOpenHelper {
    
    private static final String LOG_TAG = LocalDatabase.class.getCanonicalName();

    public LocalDatabase(Context context) {
        super(context, LocalSchema.PARENT_DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createDatabase(db,
                LocalSchema.Gift.TABLE_NAME,
                LocalSchema.Cols.ID,
                LocalSchema.Gift.COLUMNS);
        createDatabase(db,
                LocalSchema.GiftChain.TABLE_NAME,
                LocalSchema.Cols.ID,
                LocalSchema.GiftChain.COLUMNS);
        createDatabase(db,
                LocalSchema.User.TABLE_NAME,
                LocalSchema.Cols.ID,
                LocalSchema.User.COLUMNS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO need to add support to upgrade database when schema
        // changes
    }
    
    static void createDatabase(SQLiteDatabase db,
            String tableName,
            String idColumn,
            Map<String, String> columns) {
        StringBuilder createTable = new StringBuilder();
        createTable.append("create table if not exists " + tableName + " (");
        createTable.append(idColumn + " integer primary key autoincrement ");
        for (Map.Entry<String, String> entry : columns.entrySet()) {
            if (!entry.getKey().equals(LocalSchema.Cols.ID))
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
}
