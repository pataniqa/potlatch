package com.pataniqa.coursera.potlatch.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.apache.commons.io.IOUtils;

import retrofit.RequestInterceptor;
import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.FormUrlEncodedTypedOutput;
import android.util.Base64;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pataniqa.coursera.potlatch.store.remote.RemoteGiftApi;
import com.pataniqa.coursera.potlatch.store.remote.SecuredRestException;

@RequiredArgsConstructor
public class AndroidOAuthHandler implements RequestInterceptor {
    
    private static final String LOG_TAG = AndroidOAuthHandler.class.getCanonicalName();

    private final Client client;
    private final String username;
    private final String password;
    private final String endpoint;
    private final static String CLIENT_ID = "mobile";
    private final static String CLIENT_SECRET = "";

    private String token = null;

    /**
     * Every time a method on the client interface is invoked, this method is
     * going to get called. The method checks if the client has previously
     * obtained an OAuth 2.0 bearer token. If not, the method obtains the bearer
     * token by sending a password grant request to the server.
     * 
     * Once this method has obtained a bearer token, all future invocations will
     * automatically insert the bearer token as the "Authorization" header in
     * outgoing HTTP requests.
     * 
     */
    @Override
    public void intercept(RequestFacade request) {
        Log.d(LOG_TAG, "Username: " + username + " password " + password + " endpoint " + endpoint);
        if (token == null) {
            try {
                token = getAccessToken(client,
                        username,
                        password,
                        endpoint);
            } catch (Exception e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }
        }
        Log.d(LOG_TAG, "Request: " + request.toString() + " token " + token);
        request.addHeader("Authorization", "Bearer " + token);
    }
    
    public static String getAccessToken(Client client,
            String username,
            String password,
            String endpoint) throws SecuredRestException {
        // This code below programmatically builds an OAuth 2.0 password
        // grant request and sends it to the server.

        // Encode the username and password into the body of the request.
        FormUrlEncodedTypedOutput to = new FormUrlEncodedTypedOutput();
        to.addField("username", username);
        to.addField("password", password);

        // Add the client ID and client secret to the body of the request.
        to.addField("client_id", CLIENT_ID);
        to.addField("client_secret", CLIENT_SECRET);

        to.addField("grant_type", "password");

        // The password grant requires BASIC authentication of the client.
        // In order to do BASIC authentication, we need to concatenate the
        // client_id and client_secret values together with a colon and then
        // Base64 encode them. The final value is added to the request as
        // the "Authorization" header and the value is set to "Basic "
        // concatenated with the Base64 client_id:client_secret value described
        // above.
        String clientString = CLIENT_ID + ":" + CLIENT_SECRET;
        String base64Auth = new String(Base64.encode(clientString.getBytes(), Base64.DEFAULT));
        // Add the basic authorization header
        List<Header> headers = new ArrayList<Header>();
        headers.add(new Header("Authorization", "Basic " + base64Auth));

        // Create the actual password grant request using the data above
        Request req = new Request("POST", endpoint + RemoteGiftApi.TOKEN_PATH, headers, to);
        Log.d(LOG_TAG, "Request " + req.toString());

        // Request the password grant.
        try {
            Response resp = client.execute(req);

            // Make sure the server responded with 200 OK
            if (resp.getStatus() >= 200 && resp.getStatus() <= 299) {
                // Extract the string body from the response
                String body = IOUtils.toString(resp.getBody().in());
                Log.d(LOG_TAG, "Response: " + body);

                // Extract the access_token (bearer token) from the response so
                // that we can add it to future requests.
                ObjectMapper mapper = new ObjectMapper();
                TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
                };
                HashMap<String, String> o = mapper.readValue(body, typeRef);
                return o.get("access_token");
            } else {
                Log.e(LOG_TAG, resp.getBody().toString());
                throw new SecuredRestException("Login failure: " + resp.getStatus() + " - "
                        + resp.getReason());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            throw new SecuredRestException();
        }
    }
}
