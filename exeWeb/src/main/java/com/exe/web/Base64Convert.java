package com.exe.web;


import com.exe.web.util.MyConfUtil;
import kkd.common.util.FileUtil;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

public class Base64Convert {
    private String binFile;

    BASE64Decoder decoder = new BASE64Decoder();

    public static void main(String[] args) throws IOException {
        Base64Convert b=new Base64Convert();
        String binFile="E:\\workespaceGroup\\dev\\module\\exe\\exeWeb\\src\\main\\static\\logs\\keystore.p12"; //源文件
        String bstr=b.ioToBase64(binFile);
        FileUtil.writeMethodB("E:\\test.txt",bstr,false);
//        StringBuilder sb=new StringBuilder();
//        String staticPath=MyConfUtil.getStaticPath();
//        FileUtil.readFileByLines(staticPath+"\\logs\\test.txt",new FileUtil.FileUtilReader(){
//            @Override
//            public void read(Object o) {
//                    sb.append(o);
//            }
//        });
//        String newBinFile=staticPath+"\\logs\\test.jar"; //生成的新文件
//        b.base64ToIo(newBinFile,sb.toString());
    }

    public String ioToBase64(String fileName) throws IOException {
        //=
        String strBase64 = null;
        try {
            InputStream in = new FileInputStream(fileName);
            // in.available()返回文件的字节长度
            byte[] bytes = new byte[in.available()];
            // 将文件中的内容读入到数组中
            in.read(bytes);
            strBase64 = new BASE64Encoder().encode(bytes);      //将字节流数组转换为字符串
            in.close();
        } catch (FileNotFoundException fe) {
            fe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return strBase64;
    }

    public void base64ToIo(String binFile,String strBase64) throws IOException {
        String string = strBase64;
        String fileName = binFile;
        try {
            // 解码，然后将字节转换为文件
            byte[] bytes = new BASE64Decoder().decodeBuffer(string);   //将字符串转换为byte数组
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            byte[] buffer = new byte[1024];
            FileOutputStream out = new FileOutputStream(fileName);
            int bytesum = 0;
            int byteread = 0;
            while ((byteread = in.read(buffer)) != -1) {
                bytesum += byteread;
                out.write(buffer, 0, byteread); //文件写操作
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}