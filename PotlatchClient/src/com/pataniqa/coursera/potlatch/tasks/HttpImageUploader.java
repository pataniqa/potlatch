package com.pataniqa.coursera.potlatch.tasks;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

public class HttpImageUploader extends AsyncTask<String, Void, String> {

    private final static String LOG_TAG = HttpImageUploader.class.getCanonicalName();

    static class Uploader {

        private final ByteArrayOutputStream bao = new ByteArrayOutputStream();
        private Bitmap bitmap;

        Uploader(String path, CompressFormat format, int imageWidth, int imageQuality) {
            // read the image
            
            bitmap = BitmapFactory.decodeFile(path);

            // Resize the image
            double width = bitmap.getWidth();
            double height = bitmap.getHeight();
            double ratio = imageWidth / width;
            int newheight = (int) (ratio * height);
            bitmap = Bitmap.createScaledBitmap(bitmap, imageWidth, newheight, true);

            // Here you can define .PNG as well
            bitmap.compress(format, imageQuality, bao);
        }

        String upload(String endpoint) {
            String result = null;
            String encoded = Base64.encodeToString(bao.toByteArray(), Base64.DEFAULT);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("image", encoded));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(endpoint);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                result = EntityUtils.toString(entity);
                bitmap.recycle();

            } catch (Exception e) {
                Log.e(LOG_TAG, "Caught Exception => " + e.getMessage(), e);
            }
            return result;
        }

    }

    @Override
    protected String doInBackground(String... paths) {
        String result = null;
        for (String path : paths) {
            Uploader uploader = new Uploader(path, Bitmap.CompressFormat.JPEG, 400, 95);
            result = uploader.upload("http://www.example.com/");

        }
        return result;
    }
}