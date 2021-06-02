package kkd.common.util;

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

public class MyX509TrustManager implements X509TrustManager {
    
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) {
//        System.out.println("cert: " + chain[0].toString() + ", authType: " + authType);
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}