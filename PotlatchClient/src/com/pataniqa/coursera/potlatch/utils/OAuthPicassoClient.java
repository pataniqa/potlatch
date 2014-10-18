package com.pataniqa.coursera.potlatch.utils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;

import retrofit.client.ApacheClient;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import android.content.Context;
import android.net.Uri;

import com.pataniqa.coursera.potlatch.store.remote.SecuredRestBuilder;
import com.pataniqa.coursera.potlatch.store.remote.SecuredRestException;
import com.pataniqa.coursera.potlatch.store.remote.unsafe.UnsafeHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class OAuthPicassoClient implements PicassoFactory {
    private String accessToken;
    private Picasso picasso;
    private String endpoint;

    public OAuthPicassoClient(final String username,
            final String password,
            final String endpoint,
            final String clientId,
            final String clientSecret) {
        this.endpoint = endpoint;
        Observable
                .create(new Observable.OnSubscribe<String>() {

                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        String result;
                        try {
                            result = SecuredRestBuilder.getAccessToken(new ApacheClient(new UnsafeHttpClient()),
                                    username,
                                    password,
                                    SecuredRestBuilder.getLoginUrl(endpoint),
                                    clientId,
                                    clientSecret);
                            subscriber.onNext(result);
                            subscriber.onCompleted();
                        } catch (SecuredRestException e) {
                            subscriber.onError(e);
                        }
                        
                    }
                }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .retry(1).forEach(new Action1<String>() {
                    @Override
                    public void call(String arg0) {
                        accessToken = arg0;
                    }
                });
    }

    private void setPicasso(Context context) {
        if (picasso == null) {
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.downloader(new CustomOkHttpDownloader(context, accessToken));
            picasso = builder.build();
        }
    }

    @Override
    public Picasso with(final Context context) {
        setPicasso(context);
        return picasso;
    }

    @Override
    public RequestCreator load(Context context, String url) {
        Uri uri;
        if (url.startsWith("/gift"))
            uri = Uri.parse(endpoint + url);
        else if (url.startsWith("file")) 
            uri = Uri.parse(url);
        else
            uri = Uri.fromFile(new File(url));
        setPicasso(context);
        return picasso.load(uri);
    }

    static class CustomOkHttpDownloader extends OkHttpDownloader {

        private String accessToken;

        public CustomOkHttpDownloader(Context context, String accessToken) {
            super(context);
            this.accessToken = accessToken;
        }

        @Override
        protected HttpURLConnection openConnection(final Uri uri) throws IOException {
            HttpURLConnection connection = super.openConnection(uri);
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            return connection;
        }
    }

}
