package com.pataniqa.coursera.potlatch.store.remote.unsafe;

/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;


/**
 * Easy to use Http and Https client, that transparently adds gzip compression
 * and ignores all Https certificates. It can also be used for using credentials
 * in your connection.
 * 
 * This class was created for Android applications, where the appropriate apache
 * libraries are already available. If you are developing for another platform,
 * make sure to add the httpclient, httpcore and commons-logging libs to your
 * buildpath. They can be downloaded from <a
 * href="http://hc.apache.org/downloads.cgi">Apache</a>
 * 
 * <code><br/>
        EasyHttpClient client = new EasyHttpClient();<br/>
        System.out.println(client.get("https://encrypted.google.com/"));<br/>
 *  </code>
 * 
 * @author match2blue software development GmbH
 * @author Rene Fischer, Ulrich Scheller
 */
@SuppressWarnings("deprecation")
public class UnsafeHttpClient extends DefaultHttpClient {
    /**
     * Default http port
     */
    private final static int HTTP_PORT = 80;

    /**
     * Default https port
     */
    private final static int HTTPS_PORT = 443;

    protected int lastStatusCode;

    protected String lastReasonPhrase;

    /**
     * Default constructor that initializes gzip handling. It adds the
     * Accept-Encoding gzip flag and also decompresses the response from the
     * server.
     */
    public UnsafeHttpClient() {
        addRequestInterceptor(new HttpRequestInterceptor() {
            public void process(final HttpRequest request, final HttpContext context)
                    throws HttpException, IOException {
                if (!request.containsHeader("Accept-Encoding")) {
                    request.addHeader("Accept-Encoding", "gzip");
                }
            }
        });

        addResponseInterceptor(new HttpResponseInterceptor() {
            public void process(final HttpResponse response, final HttpContext context)
                    throws HttpException, IOException {
                HttpEntity entity = response.getEntity();
                Header ceheader = entity.getContentEncoding();
                if (ceheader != null) {
                    for (HeaderElement headerElement : ceheader.getElements()) {
                        if (headerElement.getName().equalsIgnoreCase("gzip")) {
                            response.setEntity(new GzipEntityWrapper(response.getEntity()));
                            lastStatusCode = response.getStatusLine().getStatusCode();
                            lastReasonPhrase = response.getStatusLine().getReasonPhrase();
                            return;
                        }
                    }
                }
            }
        });
    }

    /**
     * Constructor which handles credentials for the connection (only if
     * username and password are set)
     * 
     * @param username
     * @param password
     */
    public UnsafeHttpClient(String username, String password) {
        if (username != null && password != null) {
            UsernamePasswordCredentials c = new UsernamePasswordCredentials(username, password);
            BasicCredentialsProvider cP = new BasicCredentialsProvider();
            cP.setCredentials(AuthScope.ANY, c);
            setCredentialsProvider(cP);
        }
    }

    /**
     * Function that creates a ClientConnectionManager which can handle http and
     * https. In case of https self signed or invalid certificates will be
     * accepted.
     */
    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "utf-8");
        params.setBooleanParameter("http.protocol.expect-continue", false);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), HTTP_PORT));
        registry.register(new Scheme("https", new EasySSLSocketFactory(), HTTPS_PORT));
        ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);

        return manager;
    }

    /**
     * Make a get request to the specified url
     * 
     * @param url
     * @return the response string, null if there was an error
     */
    public String get(String url) {
        HttpGet getReq = new HttpGet(url);
        InputStream content = null;
        try {
            content = execute(getReq).getEntity().getContent();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = content.read(buf)) > 0) {
                bout.write(buf, 0, len);
            }
            content.close();
            return bout.toString();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        UnsafeHttpClient client = new UnsafeHttpClient();
        System.out.println(client.get("https://encrypted.google.com/"));
    }
}
