package com.exe.web.plugins;

import java.text.DecimalFormat;

public class Test {
    public static void main(String[] args) {
        String s="187233";
        genNum(s);
    }

    /**
     * 根据号段生成 该号段所有号码
     * @param telNum
     */
    public static  void genNum(String telNum){
        int len=11-telNum.length();
        StringBuilder maxStr=new StringBuilder();
        for (int i=0;i<len;i++){
            maxStr.append("9");
        }
        StringBuilder minStr=new StringBuilder();
        for (int i=0;i<len;i++){
            minStr.append("0");
        }
        int maxNum=Integer.parseInt(maxStr.toString());
        for(int i=0;i<maxNum;i++){
            DecimalFormat df1 = new DecimalFormat(minStr.toString());
            System.out.println(df1.format(i));
        }
        System.out.println(maxStr);
    }


}
