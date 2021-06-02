package com.exe.web;

import java.io.*;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kkd.common.util.FileUtil;
import org.eclipse.jetty.util.security.Password;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Test {
    private final static Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {
//        String[] a={"Tanbin123456"};
//        Password.main(a);

//        logger.debug(getRealURL("jdbc:mysql://172.16.5.13:3306/ndp"));
//        getOriginalInfo("4:4","12323");
    }

    private final static Pattern pattern = Pattern.compile("^(jdbc:\\w+://\\S+:[0-9]+)\\s*");

    private static String getRealURL(String url) {


        Matcher matcher = pattern.matcher(url.trim());
        matcher.find();

        String fileName = null;
        try {
            fileName = URLDecoder.decode(fileName, "UTF-8");
            System.out.println(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return matcher.group(1);
    }


    public static String getOriginalInfo(String key, String Str) {
        if (key == null) {
            return null;
        }

        if (Str == null) {
            return null;
        }

        if (!key.contains(":")) {
            return null;
        }

        String result = "";

        String[] keys = null;
        try {
            keys = key.split(":");

            char[] use = Str.toCharArray();

            for (int i = 0; i < Str.length(); i++) {
                if ((i + 1) % 2 == 1) {
                    use[i] = (char) (use[i] + Integer.parseInt(keys[1]));
                } else {
                    use[i] = (char) (use[i] - Integer.parseInt(keys[1]));
                }
            }

            for (int i = 0; i < use.length; i++) {
                result += use[i];
            }
        } catch (Exception e) {
        }

        String m1 = result.substring(0,
                result.length() - Integer.parseInt(keys[0]));

        String m2 = result.substring(
                result.length() - Integer.parseInt(keys[0]), result.length());

        int s = 0, e = m1.length() - 1;

        char[] us = m1.toCharArray();

        while (s < e) {
            char temp = us[e];
            us[e] = us[s];
            us[s] = temp;

            s++;
            e--;
        }

        m1 = "";
        for (int i = 0; i < us.length; i++)
            m1 += us[i];

        result = m2 + m1;

        return result;
    }
}
