package com.pataniqa.coursera.potlatch.utils;

import java.io.File;

import android.content.Context;
import android.net.Uri;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class LocalPicassoFactory implements PicassoFactory {
    
    @Override
    public RequestCreator load(final Context context, String url) {
        Uri uri = url.startsWith("file") ?  Uri.parse(url) : Uri.fromFile(new File(url));
        return Picasso.with(context).load(uri);
    }

    @Override
    public Picasso with(Context context) {
        return Picasso.with(context);
    }

}
