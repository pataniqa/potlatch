package com.pataniqa.coursera.potlatch.store.remote;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Setter;
import lombok.experimental.Accessors;
import retrofit.Endpoint;
import retrofit.ErrorHandler;
import retrofit.Profiler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RestAdapter.Log;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.client.Client.Provider;
import retrofit.client.OkClient;
import retrofit.converter.Converter;
import retrofit.mime.FormUrlEncodedTypedOutput;

/**
 * A Builder class for a Retrofit REST Adapter. Extends the default
 * implementation by providing logic to handle an OAuth 2.0 password grant login
 * flow. The RestAdapter that it produces uses an interceptor to automatically
 * obtain a bearer token from the authorization server and insert it into all
 * client requests.
 * 
 * You can use it like this:
 * 
 * private VideoSvcApi videoService = new SecuredRestBuilder()
 * .setLoginEndpoint(TEST_URL + VideoSvcApi.TOKEN_PATH) .setUsername(USERNAME)
 * .setPassword(PASSWORD) .setClientId(CLIENT_ID) .setClient(new
 * ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
 * .setEndpoint(TEST_URL).setLogLevel(LogLevel.FULL).build()
 * .create(VideoSvcApi.class);
 * 
 * @author Jules, Mitchell
 * 
 */
@Accessors(fluent = true)
public class SecuredRestBuilder extends RestAdapter.Builder {

    @Setter private String username;
    @Setter private String password;
    @Setter private String loginUrl;
    @Setter private String clientId;
    @Setter private String clientSecret = "";
    private Client client;

    private class OAuthHandler implements RequestInterceptor {

        private boolean loggedIn;
        private String accessToken;

        /**
         * Every time a method on the client interface is invoked, this method
         * is going to get called. The method checks if the client has
         * previously obtained an OAuth 2.0 bearer token. If not, the method
         * obtains the bearer token by sending a password grant request to the
         * server.
         * 
         * Once this method has obtained a bearer token, all future invocations
         * will automatically insert the bearer token as the "Authorization"
         * header in outgoing HTTP requests.
         * 
         */
        @Override
        public void intercept(RequestFacade request) {
            // If we're not logged in, login and store the authentication token.
            if (!loggedIn) {
                try {
                    accessToken = getAccessToken(client,
                            username,
                            password,
                            loginUrl,
                            clientId,
                            clientSecret);
                    // Let future calls know we've already fetched the access
                    // token
                    loggedIn = true;
                } catch (Exception e) {

                }
            }
            request.addHeader("Authorization", "Bearer " + accessToken);
        }
    }

    @Override
    public SecuredRestBuilder setEndpoint(String endpoint) {
        return (SecuredRestBuilder) super.setEndpoint(endpoint);
    }

    @Override
    public SecuredRestBuilder setEndpoint(Endpoint endpoint) {
        return (SecuredRestBuilder) super.setEndpoint(endpoint);
    }

    @Override
    public SecuredRestBuilder setClient(Client client) {
        this.client = client;
        return (SecuredRestBuilder) super.setClient(client);
    }

    @Override
    public SecuredRestBuilder setClient(Provider clientProvider) {
        client = clientProvider.get();
        return (SecuredRestBuilder) super.setClient(clientProvider);
    }

    @Override
    public SecuredRestBuilder setErrorHandler(ErrorHandler errorHandler) {
        return (SecuredRestBuilder) super.setErrorHandler(errorHandler);
    }

    @Override
    public SecuredRestBuilder setExecutors(Executor httpExecutor, Executor callbackExecutor) {
        return (SecuredRestBuilder) super.setExecutors(httpExecutor, callbackExecutor);
    }

    @Override
    public SecuredRestBuilder setRequestInterceptor(RequestInterceptor requestInterceptor) {
        return (SecuredRestBuilder) super.setRequestInterceptor(requestInterceptor);
    }

    @Override
    public SecuredRestBuilder setConverter(Converter converter) {
        return (SecuredRestBuilder) super.setConverter(converter);
    }

    @Override
    public SecuredRestBuilder setProfiler(@SuppressWarnings("rawtypes") Profiler profiler) {
        return (SecuredRestBuilder) super.setProfiler(profiler);
    }

    @Override
    public SecuredRestBuilder setLog(Log log) {
        return (SecuredRestBuilder) super.setLog(log);
    }

    @Override
    public SecuredRestBuilder setLogLevel(LogLevel logLevel) {
        return (SecuredRestBuilder) super.setLogLevel(logLevel);
    }

    @Override
    public RestAdapter build() {
        if (username == null || password == null) {
            throw new RuntimeException("You must specify both a username and password for a "
                    + "SecuredRestBuilder before calling the build() method.");
        }

        if (client == null) {
            client = new OkClient();
        }
        OAuthHandler hdlr = new OAuthHandler();
        setRequestInterceptor(hdlr);

        return super.build();
    }
    
    private static String accessToken = null;
    
    public static String getAccessToken(Client client,
            String username,
            String password,
            String loginUrl,
            String clientId,
            String clientSecret) throws SecuredRestException {
        if (accessToken != null)
            return accessToken;
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
                // that we can add it to future requests.
                ObjectMapper mapper = new ObjectMapper();
                TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
                };
                HashMap<String, String> o = mapper.readValue(body, typeRef);
                accessToken = o.get("access_token");
                return accessToken;
            } else {
                throw new SecuredRestException("Login failure: " + resp.getStatus() + " - "
                        + resp.getReason());
            }
        } catch (IOException e) {
            throw new SecuredRestException();
        }
    }

    public static String getLoginUrl(String endpoint) {
        return endpoint + RemoteGiftApi.TOKEN_PATH;
    }
}
