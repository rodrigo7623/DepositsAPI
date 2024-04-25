package cavapy.api.py.config;

import cavapy.api.py.DepositsApiApplication;
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

    @Bean
    public RestTemplate restTemplate() throws Exception {

        FileInputStream fileInputStream = null;
        ClientHttpRequestFactory factory = null;

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
            keyStore.load(fileInputStream, "password".toCharArray());
            CloseableHttpClient httpClient = HttpClients.custom()
                    .setSSLContext(SSLContextBuilder.create().loadKeyMaterial(keyStore, "password".toCharArray())
                            .build())
                    .build();
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
