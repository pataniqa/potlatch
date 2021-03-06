package com.pataniqa.coursera.potlatch.ui;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import retrofit.client.OkClient;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.pataniqa.coursera.potlatch.store.remote.SecuredRestException;
import com.pataniqa.coursera.potlatch.store.remote.UnsafeOkHttpClient;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

interface AuthenticationTokenFactory {
    Observable<String> getToken();

    String getEndpoint();
}

@RequiredArgsConstructor
class AuthenticationTokenFactoryImpl implements AuthenticationTokenFactory {

    private final String username;
    private final String password;
    @Getter private final String endpoint;

    @Override
    public Observable<String> getToken() {
        return Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                String result;
                try {
                    result = AndroidOAuthHandler.getAccessToken(new OkClient(UnsafeOkHttpClient
                            .getUnsafeOkHttpClient()),
                            username,
                            password,
                            endpoint);
                    subscriber.onNext(result);
                    subscriber.onCompleted();
                } catch (SecuredRestException e) {
                    subscriber.onError(e);
                }

            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).retry(1);
    }
}

@RequiredArgsConstructor
class OAuthPicasso {

    private final static String LOG_TAG = OAuthPicasso.class.getCanonicalName();

    private static Picasso picasso;
    private static String accessToken;

    public static RequestCreator load(OkHttpClient client,
            Context context,
            String endpoint,
            String token,
            String url) {
        Uri uri;
        if (url.startsWith("/gift"))
            uri = Uri.parse(endpoint + url);
        else if (url.startsWith("file"))
            uri = Uri.parse(url);
        else
            uri = Uri.fromFile(new File(url));
        if (picasso == null || !accessToken.equals(token)) {
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.downloader(new CustomOkHttpDownloader(context, client, token));
            picasso = builder.build();
            picasso.setLoggingEnabled(true);
            accessToken = token;
        }
        Log.d(LOG_TAG, "Loading image: " + uri.toString());
        return picasso.load(uri);
    }

    static class CustomOkHttpDownloader extends OkHttpDownloader {

        private String accessToken;

        public CustomOkHttpDownloader(Context context, OkHttpClient client, String accessToken) {
            super(client);
            this.accessToken = accessToken;
        }

        @Override
        protected HttpURLConnection openConnection(final Uri uri) throws IOException {
            HttpURLConnection connection = super.openConnection(uri);
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            Log.d(LOG_TAG, "Creating connection for " + uri + " with " + accessToken);
            return connection;
        }
    }

}
