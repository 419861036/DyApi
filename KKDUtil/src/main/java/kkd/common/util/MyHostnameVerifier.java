package kkd.common.util;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * 重写一个方法
 * @author Administrator
 *
 */
public class MyHostnameVerifier implements HostnameVerifier {

    public boolean verify(String hostname, SSLSession session) {
        System.out.println("Warning: URL Host: " + hostname + " vs. " + session.getPeerHost());
        return true;
    }
}
