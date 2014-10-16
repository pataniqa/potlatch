package com.pataniqa.coursera.potlatch.store.remote;

import java.util.concurrent.Executor;

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
import retrofit.client.Client.Provider;
import retrofit.client.OkClient;
import retrofit.converter.Converter;

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
                    accessToken = OAuthUtils.getAccessToken(client,
                            username,
                            password,
                            loginUrl,
                            clientId,
                            clientSecret);
                    // Add the access_token to this request as the
                    // "Authorization"
                    // header.
                    request.addHeader("Authorization", "Bearer " + accessToken);

                    // Let future calls know we've already fetched the access
                    // token
                    loggedIn = true;
                } catch (Exception e) {

                }
            } else {
                // Add the access_token that we previously obtained to this
                // request as
                // the "Authorization" header.
                request.addHeader("Authorization", "Bearer " + accessToken);
            }
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
            throw new SecuredRestException("You must specify both a username and password for a "
                    + "SecuredRestBuilder before calling the build() method.");
        }

        if (client == null) {
            client = new OkClient();
        }
        OAuthHandler hdlr = new OAuthHandler();
        setRequestInterceptor(hdlr);

        return super.build();
    }
}
