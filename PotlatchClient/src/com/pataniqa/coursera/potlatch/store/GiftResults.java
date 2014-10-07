package com.pataniqa.coursera.potlatch.store;

import java.util.ArrayList;

import rx.Observable;
import rx.functions.Func1;

import com.pataniqa.coursera.potlatch.model.GiftResult;

public class GiftResults {
    public static Observable<ArrayList<GiftResult>> hideFlaggedContent(Observable<ArrayList<GiftResult>> results,
            final boolean hide) {
        return results.map(new Func1<ArrayList<GiftResult>, ArrayList<GiftResult>>() {
            @Override
            public ArrayList<GiftResult> call(ArrayList<GiftResult> results) {
                if (!hide)
                    return results;
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
