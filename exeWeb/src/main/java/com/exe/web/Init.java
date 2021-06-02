package com.exe.web;

import java.util.List;

import com.exe.base.CmdHandle;
import com.exe.base.util.GroovyUtil;
import com.exe.base.vo.ExeResourceVo;
import com.exe.bo.RuleBo;
import com.exe.param.RuleParam;

import kkd.common.entity.Msg;

public class Init {
    public static void regPlugins(){
        try{
            List<ExeResourceVo> list=initData();
            CmdHandle.clearCmd();
            for (ExeResourceVo exeResourceVo : list) {
                try{
                    String path=exeResourceVo.getPath();
                    String config=exeResourceVo.getCaiConfig();
                    int pos=path.indexOf(".java");
                    String shortCmd=path.substring(0, pos);
                    Class cmd = GroovyUtil.getCmd(config);
                    System.out.println("init:"+shortCmd);
                    CmdHandle.register(shortCmd, cmd);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    private static List<ExeResourceVo> initData() {
        Msg<List<ExeResourceVo>> msg=new Msg<>();
        RuleParam param=new RuleParam();
		param.setType("plugin");
		RuleBo.list(param,msg,false);
		return msg.getV();
	}
}