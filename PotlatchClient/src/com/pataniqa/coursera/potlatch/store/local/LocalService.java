package com.pataniqa.coursera.potlatch.store.local;

import java.util.ArrayList;
import java.util.Map;

import lombok.experimental.Accessors;
import rx.Observable;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pataniqa.coursera.potlatch.model.Gift;
import com.pataniqa.coursera.potlatch.model.GiftChain;
import com.pataniqa.coursera.potlatch.model.GiftResult;
import com.pataniqa.coursera.potlatch.model.User;
import com.pataniqa.coursera.potlatch.store.DataService;
import com.pataniqa.coursera.potlatch.store.GiftChains;
import com.pataniqa.coursera.potlatch.store.GiftMetadata;
import com.pataniqa.coursera.potlatch.store.Gifts;
import com.pataniqa.coursera.potlatch.store.Media;
import com.pataniqa.coursera.potlatch.store.ResultOrder;
import com.pataniqa.coursera.potlatch.store.ResultOrderDirection;
import com.pataniqa.coursera.potlatch.store.Users;

@Accessors(fluent = true)
public class LocalService implements DataService {

    private final LocalGiftQuery gifts;
    private final LocalGiftChains giftChains;
    private final LocalGiftMetadata giftMetadata;
    private final LocalUserStore users;

    public LocalService(Context context) {
        LocalDatabase helper = new LocalDatabase(context);
        users = new LocalUserStore(helper);
        giftChains = new LocalGiftChainStore(helper);
        LocalGiftStore store = new LocalGiftStore(helper, users, giftChains);
        gifts = new LocalGiftQuery(helper, store);
        giftMetadata = new LocalGiftMetadataStore(helper, gifts);
    }

    @Override
    public Media media() {
        // We never use the media store locally
        throw new RuntimeException("Media store interface should never be called on local store");
    }
    
    @Override
    public Gifts gifts() {
        return new Gifts() {

            @Override
            public Observable<ArrayList<GiftResult>> findAll() {
                return Observable.just(gifts.findAll());
            }

            @Override
            public Observable<GiftResult> findOne(long id) {
                return Observable.just(gifts.findOne(id));
            }

            @Override
            public Observable<Boolean> delete(long id) {
                return Observable.just(gifts.delete(id));
            }

            @Override
            public <S extends Gift> Observable<S> save(S data) {
                return Observable.just(gifts.save(data));
            }

            @Override
            public Observable<ArrayList<GiftResult>> queryByTitle(String title,
                    ResultOrder resultOrder,
                    ResultOrderDirection resultOrderDirection,
                    boolean hide) {
                return Observable.just(gifts.queryByTitle(title,
                        resultOrder,
                        resultOrderDirection,
                        hide));
            }

            @Override
            public Observable<ArrayList<GiftResult>> queryByUser(String title,
                    long userID,
                    ResultOrder resultOrder,
                    ResultOrderDirection resultOrderDirection,
                    boolean hide) {
                return Observable.just(gifts.queryByUser(title,
                        userID,
                        resultOrder,
                        resultOrderDirection,
                        hide));
            }

            @Override
            public Observable<ArrayList<GiftResult>> queryByTopGiftGivers(String title,
                    ResultOrderDirection resultOrderDirection,
                    boolean hide) {
                return Observable.just(gifts
                        .queryByTopGiftGivers(title, resultOrderDirection, hide));
            }

            @Override
            public Observable<ArrayList<GiftResult>> queryByGiftChain(String title,
                    long giftChainID,
                    ResultOrder resultOrder,
                    ResultOrderDirection resultOrderDirection,
                    boolean hide) {
                return Observable.just(gifts.queryByGiftChain(title,
                        giftChainID,
                        resultOrder,
                        resultOrderDirection,
                        hide));
            }
        };
    }

    @Override
    public GiftChains giftChains() {
        return new GiftChains() {

            @Override
            public Observable<ArrayList<GiftChain>> findAll() {
                return Observable.just(giftChains.findAll());
            }

            @Override
            public Observable<GiftChain> findOne(long id) {
                return Observable.just(giftChains.findOne(id));
            }

            @Override
            public Observable<Boolean> delete(long id) {
                return Observable.just(giftChains.delete(id));
            }

            @Override
            public <S extends GiftChain> Observable<S> save(S data) {
                return Observable.just(giftChains.save(data));
            }
        };
    }

    @Override
    public GiftMetadata giftMetadata() {
        return new GiftMetadata() {

            @Override
            public Observable<Boolean> setLike(long giftID, boolean like) {
                return Observable.just(giftMetadata.setLike(giftID, like));
            }

            @Override
            public Observable<Boolean> setFlag(long giftID, boolean flag) {
                return Observable.just(giftMetadata.setFlag(giftID, flag));
            }
        };
    }

    @Override
    public Users users() {
        return new Users() {

            @Override
            public Observable<ArrayList<User>> findAll() {
                return Observable.just(users.findAll());
            }

            @Override
            public Observable<User> findOne(long id) {
                return Observable.just(users.findOne(id));
            }

            @Override
            public Observable<Boolean> delete(long id) {
                return Observable.just(users.delete(id));
            }

            @Override
            public <S extends User> Observable<S> save(S data) {
                return Observable.just(users.save(data));
            }
        };
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
