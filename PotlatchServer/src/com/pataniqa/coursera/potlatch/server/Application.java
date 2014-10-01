package com.pataniqa.coursera.potlatch.server;

import java.io.File;

import javax.servlet.MultipartConfigElement;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MultiPartConfigFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.pataniqa.coursera.potlatch.auth.OAuth2SecurityConfiguration;

@EnableAutoConfiguration
@EnableWebMvc
@ComponentScan
@Configuration
@Import(OAuth2SecurityConfiguration.class)
public class Application {

    private static final String MAX_REQUEST_SIZE = "150MB";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // This configuration element adds the ability to accept multipart
    // requests to the web container.
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        final MultiPartConfigFactory factory = new MultiPartConfigFactory();
        // Place upper bounds on the size of the requests to ensure that
        // clients don't abuse the web container by sending huge requests
        factory.setMaxFileSize(MAX_REQUEST_SIZE);
        factory.setMaxRequestSize(MAX_REQUEST_SIZE);

        return factory.createMultipartConfig();
    }

    private static final String DEFAULT_KEYSTORE_FILE = "${keystore.file:src/main/resources/private/keystore}";
    
    private static final String DEFAULT_KEYSTORE_PASS = "${keystore.pass:changeit}";
    
    // This version uses the Tomcat web container and configures it to
    // support HTTPS. The code below performs the configuration of Tomcat
    // for HTTPS. Each web container has a different API for configuring
    // HTTPS.
    //
    // The app now requires that you pass the location of the keystore and
    // the password for your private key that you would like to setup HTTPS
    // with. In Eclipse, you can set these options by going to:
    // 1. Run->Run Configurations
    // 2. Under Java Applications, select your run configuration for this app
    // 3. Open the Arguments tab
    // 4. In VM Arguments, provide the following information to use the
    // default keystore provided with the sample code:
    //
    // -Dkeystore.file=src/main/resources/private/keystore
    // -Dkeystore.pass=changeit
    //
    // 5. Note, this keystore is highly insecure! If you want more securtiy, you
    // should obtain a real SSL certificate:
    //
    // http://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html
    //
    @Bean
    EmbeddedServletContainerCustomizer containerCustomizer(
            @Value(DEFAULT_KEYSTORE_FILE) String keystoreFile,
            @Value(DEFAULT_KEYSTORE_PASS) final String keystorePass) throws Exception {

        // If you were going to reuse this class in another
        // application, this is one of the key sections that you
        // would want to change

        final String absoluteKeystoreFile = new File(keystoreFile).getAbsolutePath();

        return new EmbeddedServletContainerCustomizer() {

            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
                tomcat.addConnectorCustomizers(new TomcatConnectorCustomizer() {
                    @Override
                    public void customize(Connector connector) {
                        connector.setPort(8443);
                        connector.setSecure(true);
                        connector.setScheme("https");

                        Http11NioProtocol proto = (Http11NioProtocol) connector
                                .getProtocolHandler();
                        proto.setSSLEnabled(true);
                        proto.setKeystoreFile(absoluteKeystoreFile);
                        proto.setKeystorePass(keystorePass);
                        proto.setKeystoreType("JKS");
                        proto.setKeyAlias("tomcat");
                    }
                });

            }
        };
    }

}
