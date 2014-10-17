package com.pataniqa.coursera.potlatch.utils;

import android.content.Context;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public interface PicassoFactory {
    RequestCreator load(final Context context, final String url); 
    
    Picasso with(final Context context);
}
