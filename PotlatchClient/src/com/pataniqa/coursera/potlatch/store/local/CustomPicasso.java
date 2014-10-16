package com.pataniqa.coursera.potlatch.store.local;

import java.io.IOException;
import java.net.HttpURLConnection;

import retrofit.client.Client;

import android.content.Context;
import android.net.Uri;

import com.pataniqa.coursera.potlatch.store.remote.OAuthUtils;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

public class CustomPicasso {

    private static Picasso sPicasso;

    public static Picasso getImageLoader(final Context context, Client client,
            String username,
            String password,
            String loginUrl,
            String clientId,
            String clientSecret) {
        if (sPicasso == null) {
            Picasso.Builder builder = new Picasso.Builder(context);
            String accessToken = OAuthUtils.getAccessToken(client, username, password, loginUrl, clientId, clientSecret);
            builder.downloader(new CustomOkHttpDownloader(context, accessToken));
            sPicasso = builder.build();
        }
        return sPicasso;
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