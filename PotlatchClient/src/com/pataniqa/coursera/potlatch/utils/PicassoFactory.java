package com.pataniqa.coursera.potlatch.utils;

import android.content.Context;

import com.squareup.picasso.Picasso;

public interface PicassoFactory {
    Picasso with(final Context context); 
}
