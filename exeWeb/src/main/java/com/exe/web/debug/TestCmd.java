package com.exe.web.debug;

import com.exe.base.Cmd;
import com.exe.base.vo.CmdVo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

public class TestCmd extends Cmd {

    public static void main(String[] args) {

    }

    protected static void printHello() {
        Random r = new Random();
        int i = r.nextInt();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());

        System.out.println(date + " : " + i);
    }

    @Override
    public void exe(CmdVo cmd, Map<String, String> param) {
        new Thread() {

            @Override
            public void run() {
                TestCmd test = new TestCmd();
                while (true) {
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    printHello();
                }
            }
        }.start();
    }

    @Override
    public String op() {
        return null;
    }
}