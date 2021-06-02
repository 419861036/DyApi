package com.exe.web.driver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.exe.web.util.MyConfUtil;
import java.io.File;
import java.io.PrintStream;
import java.util.Date;
import java.util.Set;
import kkd.common.util.FileUtil;
import kkd.common.util.FileUtil.FileUtilReader;
import kkd.common.util.StringUtil;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CookieUtil
{
    private static final Logger logger = LoggerFactory.getLogger(CookieUtil.class);

    public static void loadCookieLocal(WebDriver driver)
    {
        System.out.println("����������������");
        String cookieStr = getCookie();
        if (!StringUtil.isEmpty(cookieStr)) {
            try
            {
                System.out.println("����cookie");
                JSONArray ja = JSON.parseArray(cookieStr);
                driver.manage().deleteAllCookies();
                for (Object object : ja)
                {
                    JSONObject jo = (JSONObject)object;
                    String name = jo.getString("name");
                    String value = jo.getString("value");
                    String path = jo.getString("path");
                    String domain = jo.getString("domain");
                    Date expiry = jo.getDate("expiry");
                    Boolean isSecure = jo.getBoolean("isSecure");
                    isSecure = Boolean.valueOf(isSecure == null ? false : isSecure.booleanValue());
                    Boolean isHttpOnly = jo.getBoolean("isHttpOnly");
                    isHttpOnly = Boolean.valueOf(isHttpOnly == null ? false : isHttpOnly.booleanValue());

                    Cookie c = new Cookie(name, value, domain, path, expiry, isSecure.booleanValue(), isHttpOnly.booleanValue());
                    driver.manage().addCookie(c);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                String conf = MyConfUtil.getRootPath() + File.separator + "conf" + File.separator;
                logger.debug("������" + conf);
                FileUtil.writeMethodB(conf + "cookie.txt", "", false);
            }
        }
    }

    public static void saveCookie(WebDriver driver)
    {
        System.out.println("������������������cookie");
        Set<Cookie> cookieSet = driver.manage().getCookies();
        String content = JSON.toJSONString(cookieSet);
        String conf = MyConfUtil.getRootPath() + File.separator + "conf" + File.separator;
        logger.debug("������" + conf);
        FileUtil.writeMethodB(conf + "cookie.txt", content, false);
    }

    public static String getCookie()
    {
        StringBuilder sb = new StringBuilder();
        FileUtil.FileUtilReader f = new FileUtil.FileUtilReader()
        {
            public void read(Object data)
            {
                sb.append(data.toString() + "\r\n");
            }
        };
        String conf = MyConfUtil.getRootPath() + File.separator + "conf" + File.separator;
        logger.debug("������" + conf);
        FileUtil.readFileByLines(conf + "cookie.txt", f, "utf-8");
        return sb.toString();
    }
}
