package com.exe.web.plugins;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.exe.base.Cmd;
import com.exe.base.vo.CmdVo;
import com.exe.web.util.MyConfUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kkd.common.util.FileUtil;
import kkd.common.util.StringUtil;

public class FileCmd extends Cmd{
	private final static Logger logger = LoggerFactory.getLogger(FileCmd.class);
	
	@Override
	public void exe(CmdVo cmd, Map<String, String> param) {
//		logger.setRuleId(cmdHandle.getCaiRule().getId().toString());
		String op=param.get("op");
		String path=param.get("dir");
		String var=param.get("var");
		String key=param.get("key");
		String rootPath=MyConfUtil.getRootPath()+File.separator+"static";
		
		
		if(StringUtil.isEmpty(path)){
			path=rootPath;
		}else{
			path=rootPath+path;
		}
		if("rename".equalsIgnoreCase(op)){
			String dest=param.get("dest");
			File f=new File(path);
			File destFile=new File(dest);
			boolean r=false;
			r = f.renameTo(destFile);
			cmdHandle.getVars().put(var, r);
		}else if("newfile".equalsIgnoreCase(op)){
			File f=new File(path);
			boolean r=false;
			try {
				r = f.createNewFile();
			} catch (IOException e) {
				logger.error("", e);
			}
			cmdHandle.getVars().put(var, r);
		}else if("mkdir".equalsIgnoreCase(op)){
			String folder=param.get("folder");
			boolean r=FileUtil.mkdir(path+"/"+folder);
			cmdHandle.getVars().put(var, r);
		}else if("del".equalsIgnoreCase(op)){
			File f=new File(path);
			boolean r=f.delete();
			cmdHandle.getVars().put(var, r);
		}else if("read".equalsIgnoreCase(op)){
			//开始行
			String startLineStr=param.get("startLine");
			Long startLine = null;
			String limitStr=param.get("limit");
			Long limit=null;
			
			if(startLine==null && !StringUtil.isEmpty(startLineStr) ) {
				startLine=Long.parseLong(startLineStr);
			}else {
				startLine=0l;
			}
			
			if(limit==null && !StringUtil.isEmpty(limitStr)) {
				limit=Long.parseLong(limitStr);
			}else {
				limit=Long.MAX_VALUE;
			}
			FileReader file;
			try {
				file = new FileReader(path);
				LineNumberReader lineNumberReader =new LineNumberReader(file);
				if(startLine==-1){
					long c=lineNumberReader.skip(Long.MAX_VALUE);
					cmdHandle.getVars().put(var,"总字节数："+c+"");
					return;
				}
				lineNumberReader.skip(startLine);
				long hasRead=0;
				StringBuilder sb=new StringBuilder();
				String line=null;
				do{
					line=lineNumberReader.readLine();
					if(line!=null){
						sb.append(line);
						sb.append("\r\n");
						hasRead++;
					}
					
				}while(line!=null && hasRead<=limit-1);
				cmdHandle.getVars().put(var,sb);
			} catch (Exception e) {
				logger.error("", e);
			}
			
		}else if("write".equalsIgnoreCase(op)){
			String data=(String) cmdHandle.getVars().get(key);
			FileUtil.writeMethodB(path, data, false);
			cmdHandle.getVars().put(var,true);
		}else if("list".equalsIgnoreCase(op)){
			File f=new File(path);
			File[] list=f.listFiles();
			List<JSONObject> listFile=new ArrayList<JSONObject>(); 
			for (File file : list) {
				JSONObject obj=new JSONObject();
				String type="";
				String fileName=file.getName();
				if(file.isDirectory()){
					type="directory";
				}else{
					int pos=fileName.lastIndexOf(".");
					if(pos!=-1){
						String subfix=fileName.substring(pos+1);
						type=subfix;
					}
				}
				if("png".equalsIgnoreCase(type)
						||"gif".equalsIgnoreCase(type)
						||"jpg".equalsIgnoreCase(type)
						){
					String thumb=file.getPath().replace(rootPath, "");
					obj.put("thumb","/html"+thumb);
				}
				String url=file.getPath().replace(rootPath, "");
				obj.put("url","/html"+url);
				obj.put("type",type);
				obj.put("name", file.getName());
				path=StringUtil.replace(file.getPath(), rootPath, "");
				obj.put("path", path);
				listFile.add(obj);
			}
			cmdHandle.getVars().put(var, listFile);
		}
	}

	@Override
	public String op() {
		return null;
	}

	public static void main(String[] args) {
		File f=new File("E:\\workspaceGroup\\workspace_my\\exeWeb\\static\\html\\test\\test.txt");
		logger.debug("{}",f.delete());
	}
}
