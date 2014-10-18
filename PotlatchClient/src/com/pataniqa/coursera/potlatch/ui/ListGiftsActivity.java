package com.pataniqa.coursera.potlatch.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.pataniqa.coursera.potlatch.R;
import com.pataniqa.coursera.potlatch.model.GiftResult;
import com.pataniqa.coursera.potlatch.store.Gifts.ResultOrder;
import com.pataniqa.coursera.potlatch.store.Gifts.ResultOrderDirection;
import com.pataniqa.coursera.potlatch.utils.GiftQuery;
import com.pataniqa.coursera.potlatch.utils.UploadService;

public class ListGiftsActivity extends GiftActivity implements
        SwipeRefreshLayout.OnRefreshListener, ListGiftsCallback {

    private static int UNDEFINED = -1;

    private static final String LOG_TAG = ListGiftsActivity.class.getCanonicalName();

    private ArrayList<GiftResult> giftData;
    private GiftDataArrayAdapter arrayAdapter;

    @InjectView(R.id.list_gifts_swipe_container) SwipeRefreshLayout swipeLayout;
    @InjectView(R.id.list_gifts_list_view) ListView listView;
    @InjectView(R.id.query_description) TextView queryDescription;

    private GiftQuery query;
    private Menu menu;
    private SharedPreferences prefs;
    private int updateFrequency = UNDEFINED;
    private Subscription subscription = null;
    private ResponseReceiver receiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");

        // Set up the UI
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().setDisplayShowTitleEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
        setContentView(R.layout.list_gifts_activity);
        getActionBar().show();

        ButterKnife.inject(this);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // Instantiate the resolver and the ArrayList
        giftData = new ArrayList<GiftResult>();

        // Instantiate the adapter using our local GiftData ArrayList.
        arrayAdapter = new GiftDataArrayAdapter(this,
                R.layout.gift_listview_custom_row,
                giftData,
                this,
                getPicasso());

        loadPreferences();
        setUpdateFrequency();
        query = new GiftQuery(getUserID(), getUserName(), prefs);
        updateGifts();

        // Tell the ListView which adapter to use to display the data.
        listView.setAdapter(arrayAdapter);

        // Set the click listener for the list view
        final Context context = this;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Log.d(LOG_TAG, "onListItemClick");
                GiftResult gift = giftData.get(position);
                Log.d(LOG_TAG, "position: " + position + "id = " + gift.getId());

                // When an item is clicked, open the ViewGiftActivity so the
                // user can view it in full screen
                if (gift.getUserID() == getUserID()) {
                    long index = giftData.get(position).getId();
                    Log.d(LOG_TAG, "openEditGiftActivity(" + index + ")");
                    Intent intent = new Intent(context, EditGiftActivity.class);
                    intent.putExtra(ROW_IDENTIFIER_TAG, index);
                    startActivity(intent);
                } else if (gift.getVideoUri() != null) {
                    Intent intent = new Intent(context, VideoDetailActivity.class);
                    intent.putExtra(GIFT_ID_TAG, gift.getId());
                    startActivity(intent);
                } else {
                    Log.d(LOG_TAG, "cannot edit this gift - not created by this user");
                }
            }
        });
        
        IntentFilter filter = new IntentFilter(UploadService.UPLOAD_RESPONSE);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new ResponseReceiver();
        registerReceiver(receiver, filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(LOG_TAG, "onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu);
        this.menu = menu;
        getMenuInflater().inflate(R.menu.list_gifts_activity_actions, menu);

        updateMenu();

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        search.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String title) {
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String title) {
                Log.d(LOG_TAG, "onQueryTextSubmit: " + title);
                query.setTitle(title);
                updateGifts();
                search.clearFocus();
                return false;
            }

        });
        search.setOnCloseListener(new OnCloseListener() {
            @Override
            public boolean onClose() {
                query.clearTitle();
                updateGifts();
                return false;
            }
        });

        return true;
    }

    void loadPreferences() {
        Log.d(LOG_TAG, "loadPreferences");
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    void setUpdateFrequency() {
        String updates = prefs.getString(SettingsActivity.UPDATE_FREQUENCY, "1");
        try {
            updateFrequency = Integer.parseInt(updates);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        Log.d(LOG_TAG, "Update frequency is: " + updateFrequency);
        finishSubscription();
        if (updateFrequency != UNDEFINED) {
            Log.d(LOG_TAG, "Creating new timer subscription");
            subscription = Schedulers.newThread().createWorker()
                    .schedulePeriodically(new Action0() {
                        @Override
                        public void call() {
                            Log.d(LOG_TAG, "Updating results due to timer");
                            updateGifts();
                        }
                    }, updateFrequency, updateFrequency, TimeUnit.SECONDS);
        }
    }

    void finishSubscription() {
        if (subscription != null) {
            Log.d(LOG_TAG, "Unsubscribing from timer");
            subscription.unsubscribe();
            subscription = null;
        }
    }

    void savePreferences() {
        Log.d(LOG_TAG, "savePreferences");
        query.saveToSharedPreferences(prefs);
    }

    @Override
    protected void onResume() {
        Log.d(LOG_TAG, "onResume");
        super.onResume();
        updateGifts();
        setUpdateFrequency();
    }

    @Override
    protected void onPause() {
        Log.d(LOG_TAG, "onPause");
        super.onPause();
        savePreferences();
        finishSubscription();
    }

    @Override
    protected void onStop() {
        Log.d(LOG_TAG, "onStop");
        super.onStop();
        savePreferences();
        finishSubscription();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_TAG, "onOptionsItemSelected");
        switch (item.getItemId()) {
        // action with ID action_refresh was selected
        case R.id.action_new:
            Log.d(LOG_TAG, "openCreateGiftActivity");
            startActivity(new Intent(this, CreateGiftActivity.class));
            break;
        case R.id.action_query_type:
            query.rotateQueryType();
            update();
            break;
        case R.id.action_result_order:
            query.changeResultOrder();
            update();
            break;
        case R.id.action_result_order_direction:
            query.changeResultDirection();
            update();
            break;
        case R.id.action_settings:
            Log.d(LOG_TAG, "openPreferencesActivity");
            startActivity(new Intent(this, SettingsActivity.class));
            break;
        default:
            break;
        }

        return true;
    }

    void update() {
        updateGifts();
        updateMenu();
    }

    void updateMenu() {
        MenuItem queryTypeMenu = menu.findItem(R.id.action_query_type);
        int icon;
        switch (query.getQueryType()) {
        case USER:
            icon = R.drawable.ic_action_person;
            break;
        case TOP_GIFT_GIVERS:
            icon = R.drawable.ic_fa_trophy;
            break;
        case CHAIN:
            icon = R.drawable.ic_fa_link;
            break;
        default:
            icon = R.drawable.ic_fa_group;
            break;
        }
        queryTypeMenu.setIcon(icon);

        menu.findItem(R.id.action_result_order)
                .setIcon(query.getResultOrder() == ResultOrder.LIKES ? R.drawable.ic_fa_heart
                        : R.drawable.ic_fa_clock_o);

        menu.findItem(R.id.action_result_order_direction)
                .setIcon(query.getResultDirection() == ResultOrderDirection.DESCENDING ? R.drawable.ic_fa_sort_amount_desc
                        : R.drawable.ic_fa_sort_amount_asc);
    }

    public void updateGifts() {
        Log.d(LOG_TAG, "updateGifts");
        swipeLayout.setRefreshing(true);
        query.query(getDataService().gifts()).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .forEach(new Action1<ArrayList<GiftResult>>() {
                    @Override
                    public void call(ArrayList<GiftResult> results) {
                        giftData.clear();
                        Log.d(LOG_TAG, Arrays.toString(results.toArray()));
                        if (results != null)
                            for (GiftResult result : results)
                                if (result.getImageUri() != null)
                                    giftData.add(result);
                        swipeLayout.setRefreshing(false);
                        queryDescription.setText(query.getDescription());
                        arrayAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void createGiftChainQuery(long giftChainID, String giftChainName) {
        query.setChainQuery(giftChainID, giftChainName);
        update();
    }

    @Override
    public void createUserQuery(long queryUserID, String queryUserName) {
        query.setUserQuery(queryUserID, queryUserName);
        update();
    }

    @Override
    public void onRefresh() {
        Log.d(LOG_TAG, "onRefresh");
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                updateGifts();
            }
        });
    }

    @Override
    public void setLike(final GiftResult gift) {
        Log.d(LOG_TAG, "Setting like for gift " + gift.getId());
        getDataService().giftMetadata().setLike(gift.getId(), gift.isLike())
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .forEach(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean arg0) {
                        Log.d(LOG_TAG, "Set like for gift " + gift.getId() + " successfully");
                    }
                });
    }

    @Override
    public void setFlag(final GiftResult gift) {
        Log.d(LOG_TAG, "Setting flag for gift " + gift.getId());
        getDataService().giftMetadata().setFlag(gift.getId(), gift.isFlag())
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .forEach(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean arg0) {
                        Log.d(LOG_TAG, "Set flag for gift " + gift.getId() + " successfully");
                    }
                });
    }
    
    public class ResponseReceiver extends BroadcastReceiver {
        @Override
         public void onReceive(Context context, Intent intent) {
            updateGifts();
         }
     }

}