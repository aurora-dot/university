/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webapps2022.ejb;

import com.webapps2022.entity.CurrencyEnum;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import javax.ejb.Stateless;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author blankie
 */
@Stateless
public class RestService {

    private final Client client;

    public RestService() throws KeyManagementException, NoSuchAlgorithmException {
        // Source: https://stackoverflow.com/questions/6047996/ignore-self-signed-ssl-cert-using-jersey-client
        SSLContext sslcontext = SSLContext.getInstance("TLS");
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};

        sslcontext.init(null, trustAllCerts, new java.security.SecureRandom());

        client = ClientBuilder.newBuilder()
                .sslContext(sslcontext)
                .hostnameVerifier((s1, s2) -> true)
                .build();
    }

    public synchronized String retrieveConversion(CurrencyEnum currency1, CurrencyEnum currency2, Float amountOfCurrency) {
        String worked;
        WebTarget resource = client.target("https://localhost:8181/webapps2022")
                .path("conversion")
                .path(currency1.toString())
                .path(currency2.toString())
                .path(amountOfCurrency.toString());

        Float conversion = resource.request(MediaType.APPLICATION_JSON).get(Float.class);
        if (conversion == null) {
            worked = "error";
        } else {
            worked = conversion.toString();
        }

        return worked;
    }
}
