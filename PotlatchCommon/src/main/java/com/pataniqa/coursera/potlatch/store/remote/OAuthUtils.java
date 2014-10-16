package com.pataniqa.coursera.potlatch.store.remote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.FormUrlEncodedTypedOutput;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OAuthUtils {

    public static String getAccessToken(Client client,
            String username,
            String password,
            String loginUrl,
            String clientId,
            String clientSecret) throws SecuredRestException {
        // This code below programmatically builds an OAuth 2.0 password
        // grant request and sends it to the server.

        // Encode the username and password into the body of the request.
        FormUrlEncodedTypedOutput to = new FormUrlEncodedTypedOutput();
        to.addField("username", username);
        to.addField("password", password);

        // Add the client ID and client secret to the body of the request.
        to.addField("client_id", clientId);
        to.addField("client_secret", clientSecret);

        // Indicate that we're using the OAuth Password Grant Flow
        // by adding grant_type=password to the body
        to.addField("grant_type", "password");

        // The password grant requires BASIC authentication of the client.
        // In order to do BASIC authentication, we need to concatenate the
        // client_id and client_secret values together with a colon and then
        // Base64 encode them. The final value is added to the request as
        // the "Authorization" header and the value is set to "Basic "
        // concatenated with the Base64 client_id:client_secret value described
        // above.
        String clientString = clientId + ":" + clientSecret;
        String base64Auth = new String(Base64.encodeBase64(clientString.getBytes()));
        // Add the basic authorization header
        List<Header> headers = new ArrayList<Header>();
        headers.add(new Header("Authorization", "Basic " + base64Auth));

        // Create the actual password grant request using the data above
        Request req = new Request("POST", loginUrl, headers, to);

        // Request the password grant.
        try {
            Response resp = client.execute(req);

            // Make sure the server responded with 200 OK
            if (resp.getStatus() >= 200 && resp.getStatus() < 299) {
                // Extract the string body from the response
                String body = IOUtils.toString(resp.getBody().in());

                // Extract the access_token (bearer token) from the response so
                // that
                // we
                // can add it to future requests.
                ObjectMapper mapper = new ObjectMapper();
                TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
                };
                HashMap<String, String> o = mapper.readValue(body, typeRef);
                return o.get("access_token");
            } else {
                throw new SecuredRestException("Login failure: " + resp.getStatus() + " - "
                        + resp.getReason());
            }
        } catch (IOException e) {
            //
        }
        throw new SecuredRestException();
    }

}
