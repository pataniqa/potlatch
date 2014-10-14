package com.pataniqa.coursera.potlatch.store;

import java.util.ArrayList;
import java.util.Arrays;

import rx.Observable;
import rx.functions.Func1;

import com.pataniqa.coursera.potlatch.model.GiftResult;

import android.util.Log;

public class GiftResults {
    
    private static final String LOG_TAG = GiftResults.class.getCanonicalName();
    
    public static Observable<ArrayList<GiftResult>> hideFlaggedContent(Observable<ArrayList<GiftResult>> results,
            final boolean hide) {
        return results.map(new Func1<ArrayList<GiftResult>, ArrayList<GiftResult>>() {
            @Override
            public ArrayList<GiftResult> call(ArrayList<GiftResult> results) {
                if (!hide)
                Log.i(LOG_TAG, Arrays.toString(results.toArray()));
                ArrayList<GiftResult> out = new ArrayList<GiftResult>();
                for (GiftResult gift : results) {
                    if (!gift.isFlagged())
                        out.add(gift);
                }
                return out;
            }
        });
    }
}
