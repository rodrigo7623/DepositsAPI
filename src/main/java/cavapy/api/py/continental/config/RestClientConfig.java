package cavapy.api.py.continental.config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.KeyStore;

@Configuration
public class RestClientConfig {

    @Value("${server.ssl.key-store}")
    Resource trustStore;

    @Value("${server.ssl.key-store-password}")
    String trustedStorePassword;

    @Bean
    public RestTemplate restTemplate() throws Exception {

        FileInputStream fileInputStream = null;
        HttpComponentsClientHttpRequestFactory factory = null;

        InputStream inputStream = getClass().getResourceAsStream("/keystore/keystore.p12");

        // Crear un archivo temporal
        File tempFile = File.createTempFile("tempfile", ".tmp");

        // Escribir el contenido del InputStream en el archivo temporal
        try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            // Crear un FileInputStream a partir del archivo temporal
            fileInputStream = new FileInputStream(tempFile);

            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(fileInputStream, trustedStorePassword.toCharArray());

            SSLContext sslContext = new SSLContextBuilder().loadKeyMaterial(keyStore, trustedStorePassword.toCharArray()).build();
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
            HttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create().setSSLSocketFactory(socketFactory).build();
            CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager).evictExpiredConnections().build();
            factory = new HttpComponentsClientHttpRequestFactory(httpClient);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Cerrar los InputStreams
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new RestTemplate(factory);
    }
}
