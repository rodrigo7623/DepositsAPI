package cavapy.api.py.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URL;
import java.security.KeyStore;

@Configuration
public class RestClientConfig {

    @Value("${server.ssl.key-store}")
    Resource trustStore;

    @Value("${server.ssl.key-store-password}")
    String trustedStorePassword;

    ClassLoader classLoader = getClass().getClassLoader();

    @Bean
    public RestTemplate restTemplate() throws Exception {

        FileInputStream fileInputStream;
        File keyFile = null;

        URL resourceUrl = classLoader.getResource("keystore/keystore.p12");

        if (resourceUrl != null) {
            keyFile = new File(resourceUrl.getFile());
        }

        if (keyFile != null) {
            fileInputStream = new FileInputStream(keyFile);
        } else {
            fileInputStream = new FileInputStream(trustStore.getFile());
        }

        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(fileInputStream, "password".toCharArray());
        CloseableHttpClient httpClient = HttpClients.custom().setSSLContext(SSLContextBuilder.create().loadKeyMaterial(
                keyStore, "password".toCharArray()).build()).build();

        ClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(factory);
    }
}
