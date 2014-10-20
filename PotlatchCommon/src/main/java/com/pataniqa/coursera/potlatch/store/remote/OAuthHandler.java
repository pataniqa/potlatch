package com.pataniqa.coursera.potlatch.store.remote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import retrofit.RequestInterceptor;
import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.FormUrlEncodedTypedOutput;

@RequiredArgsConstructor
public class OAuthHandler implements RequestInterceptor {

    private final Client client;
    private final String username;
    private final String password;
    private final String endpoint;
    private final String clientId = "mobile";
    private final String clientSecret = "";

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
        if (token == null) {
            try {
                token = getAccessToken(client,
                        username,
                        password,
                        endpoint,
                        clientId,
                        clientSecret,
                        "password");
            } catch (Exception e) {

            }
        }
        request.addHeader("Authorization", "Bearer " + token);
    }
    
    public static String getAccessToken(Client client,
            String username,
            String password,
            String endpoint,
            String clientId,
            String clientSecret,
            String grantType) throws SecuredRestException {
        // This code below programmatically builds an OAuth 2.0 password
        // grant request and sends it to the server.

        // Encode the username and password into the body of the request.
        FormUrlEncodedTypedOutput to = new FormUrlEncodedTypedOutput();
        to.addField("username", username);
        to.addField("password", password);

        // Add the client ID and client secret to the body of the request.
        to.addField("client_id", clientId);
        to.addField("client_secret", clientSecret);

        to.addField("grant_type", grantType);

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
        Request req = new Request("POST", endpoint + RemoteGiftApi.TOKEN_PATH, headers, to);

        // Request the password grant.
        try {
            Response resp = client.execute(req);

            // Make sure the server responded with 200 OK
            if (resp.getStatus() >= 200 && resp.getStatus() < 299) {
                // Extract the string body from the response
                String body = IOUtils.toString(resp.getBody().in());

                // Extract the access_token (bearer token) from the response so
                // that we can add it to future requests.
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
            throw new SecuredRestException();
        }
    }
}
