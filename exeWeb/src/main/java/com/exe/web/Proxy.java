package com.exe.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class Proxy {
    public static void proxy(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = null;
        String method=null;
        Map<String,String> header=new HashMap<>();
        StringBuffer params = new StringBuffer();
        Enumeration enu = request.getParameterNames();
        int total = 0;
        while (enu.hasMoreElements()) {
            String paramName=(String)enu.nextElement();
            if(paramName.equals("url")){
                url=request.getParameter(paramName);
            }else if(paramName.equals("method")){
                method=request.getParameter(method);
            }else if(paramName.startsWith("header")){
                header.put(paramName.replace("header",""),request.getParameter(paramName));
                //con.setRequestProperty((String)e.getKey(), (String)e.getValue());
            }else{
                if(total==0){
                    params.append(paramName).append("=").append(URLEncoder.encode(request.getParameter(paramName), "UTF-8"));
                } else {
                    params.append("&").append(paramName).append("=").append(URLEncoder.encode(request.getParameter(paramName), "UTF-8"));
                }
                ++total;
            }
        }
        url = url + "?" + params.toString();
        //out.println(url);
        if(url != null){
            // 使用GET方式向目的服务器发送请求
            URL connect = new URL(url.toString());
            HttpURLConnection connection = (HttpURLConnection) connect.openConnection();
            connection.setRequestMethod(method);
            for(Map.Entry<String,String> m: header.entrySet()){
                connection.setRequestProperty(m.getKey(),m.getValue());
            }
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while((line = reader.readLine()) != null){
                response.getWriter().println(line);
            }
            reader.close();
        }
    }
    public static void main(String[] args) {

    }
}
