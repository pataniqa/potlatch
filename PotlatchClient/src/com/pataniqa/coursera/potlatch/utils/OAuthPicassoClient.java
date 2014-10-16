package com.pataniqa.coursera.potlatch.utils;

import java.io.IOException;
import java.net.HttpURLConnection;

import retrofit.client.Client;
import retrofit.client.OkClient;
import android.content.Context;
import android.net.Uri;

import com.pataniqa.coursera.potlatch.store.remote.OAuthUtils;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

public class OAuthPicassoClient implements PicassoFactory {
    private final String accessToken;
    private Picasso picasso;

    public OAuthPicassoClient(String username,
            String password,
            String loginUrl,
            String clientId,
            String clientSecret) {
        Client client = new OkClient();
        accessToken = OAuthUtils.getAccessToken(client,
                username,
                password,
                loginUrl,
                clientId,
                clientSecret);
    }

    public Picasso with(final Context context) {
        if (picasso == null) {
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.downloader(new CustomOkHttpDownloader(context, accessToken));
            picasso = builder.build();
        }
        return picasso;
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
