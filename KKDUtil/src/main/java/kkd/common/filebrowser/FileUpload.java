package kkd.common.filebrowser;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
@SuppressWarnings("rawtypes")
public class FileUpload {
	
    /** request对象 */
    private HttpServletRequest request = null;
    /** 上传文件的路径 */
    private String uploadPath = null;
    /** 每次读取得字节的大小 */
    private static int BUFSIZE = 1024;
    /** 存储参数的Hashtable */
	private Hashtable paramHt = new Hashtable();
   
	/** 存储上传的文件的文件名的ArrayList */
    private ArrayList updFileArr = new ArrayList();
    private boolean rp;
    
    
    public Hashtable getParamHt() {
		return paramHt;
	}
    
    public boolean isRp() {
		return rp;
	}
    /**
     * 替换存在的文件
     * @param rp
     */
	public void setRp(boolean rp) {
		this.rp = rp;
	}
	/**
     * 设定request对象。
     * 
     * @param request
     *            HttpServletRequest request对象
     */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
    /**
     * 设定文件上传路径。
     * 
     * @param path
     *            用户指定的文件的上传路径。
     */
    public void setUploadPath(String path) {
        this.uploadPath = path;
    }
    

    /**
     * 文件上传处理主程序。&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;&#65533;B
     * 
     * @return int 操作结果 0 文件操作成功；1 request对象不存在。 2 没有设定文件保存路径或者文件保存路径不正确；3
     *         没有设定正确的enctype；4 文件操作异常。
     */
    @SuppressWarnings("unchecked")
	public int process() {
        int status = 0;
        // 文件上传前，对request对象，上传路径以及enctype进行check。
        status = preCheck();
        // 出错的时候返回错误代码。
        if (status != 0)
            return status;
        FileOutputStream baos = null;
        BufferedOutputStream bos = null;
        ServletInputStream sis =null;
        try {
            // &#65533;&#65533;参数或者文件名&#65533;u&#65533;&#65533;
            String name = null;
            // 参数的value
            String value = null;
            // 读取的流是否为文件的标志位
            boolean fileFlag = false;
            // 要存储的文件。
            File tmpFile = null;
            // 上传的文件的名字
            String fName = null;
           
            // &#65533;&#65533;存储参数的Hashtable
            paramHt = new Hashtable();
            updFileArr = new ArrayList();
            int rtnPos = 0;
            int tmpRtnPos = 0;
            byte[] buffs = new byte[BUFSIZE];
            byte[] tmpBuffs = new byte[BUFSIZE];
            boolean first=true;
            // &#65533;取得ContentType
            String contentType = request.getContentType();
            String boundaryStr="boundary=";
            int index = contentType.indexOf(boundaryStr);
            String boundary = "--" + contentType.substring(index + boundaryStr.length());
            String endBoundary = boundary + "--";
            // &#65533;从request对象中取得流。
            sis = request.getInputStream();
            // 读取1行
            while ((rtnPos = sis.readLine(buffs, 0, buffs.length)) != -1) {
            	 String strBuff = new String(buffs, 0, rtnPos);
                // 读取1行数据
                if (strBuff.startsWith(boundary)) {
                    if (name != null && name.trim().length() > 0) {
                        if (fileFlag) {
                        	bos.write(tmpBuffs, 0, tmpRtnPos-2);
                   		 	baos.flush();
                            bos.flush();
                            baos.close();
                            bos.close();
                            baos = null;
                            bos = null;
                            updFileArr.add(fName);
                        } else {
                            Object obj = paramHt.get(name);
                            ArrayList al = new ArrayList();
                            if (obj != null) {
                                al = (ArrayList) obj;
                            }
                            al.add(value);
                            paramHt.put(name, al);
                        }
                    }
                    name = new String();
                    value = new String();
                    fileFlag = false;
                    fName = new String();
                    rtnPos = sis.readLine(buffs, 0, buffs.length);
                    if (rtnPos != -1) {
                        strBuff = new String(buffs, 0, rtnPos);
                        if (strBuff.toLowerCase().startsWith(
                                "content-disposition: form-data; ")) {
                            int nIndex = strBuff.toLowerCase().indexOf(
                                    "name=\"");
                            int nLastIndex = strBuff.toLowerCase().indexOf(
                                    "\"", nIndex + 6);
                            name = strBuff.substring(nIndex + 6, nLastIndex);
                        }
                        int fIndex = strBuff.toLowerCase().indexOf(
                                "filename=\"");
                        if (fIndex != -1) {
                            fileFlag = true;
                            int fLastIndex = strBuff.toLowerCase().indexOf(
                                    "\"", fIndex + 10);
                            fName = strBuff.substring(fIndex + 10, fLastIndex);
                            fName = getFileName(fName);
                            if (fName == null || fName.trim().length() == 0) {
                                fileFlag = false;
                                sis.readLine(buffs, 0, buffs.length);
                                sis.readLine(buffs, 0, buffs.length);
                                sis.readLine(buffs, 0, buffs.length);
                                continue;
                            }else{
                            	if(!rp){
                            		fName = getFileNameByTime(fName);
                            	}
                                int ss=sis.readLine(buffs, 0, buffs.length);
                                ss=sis.readLine(buffs, 0, buffs.length);
                            }
                            tmpFile = new File(uploadPath +File.separator+ fName);
                            baos = new FileOutputStream(tmpFile);
                            bos = new BufferedOutputStream(baos);
                        }
                    }
                } else if (strBuff.startsWith(endBoundary)) {
                	//结束标识
                    if (name != null && name.trim().length() > 0) {
                        if (fileFlag) {
                        	bos.write(tmpBuffs, 0, tmpRtnPos-2);
                   		 	baos.flush();
                            bos.flush();
                            baos.close();
                            bos.close();
                            baos = null;
                            bos = null;
                            updFileArr.add(fName);
                        } else {
                            Object obj = paramHt.get(name);
                            ArrayList al = new ArrayList();
                            if (obj != null) {
                                al = (ArrayList) obj;
                            }
                            al.add(value);
                            paramHt.put(name, al);
                        }
                    }
                } else {
                    if (fileFlag) {
                    	if(first){
                    		first=false;
                    		System.arraycopy(buffs, 0, tmpBuffs, 0, rtnPos);
                    		tmpRtnPos=rtnPos;
                    	}else{
                    		 bos.write(tmpBuffs, 0, tmpRtnPos);
                    		 baos.flush();
                    		 System.arraycopy(buffs, 0, tmpBuffs, 0, rtnPos);
                     	     tmpRtnPos=rtnPos;
                    	}
                    } else {
                        value = value + strBuff;
                    }
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
            status = 4;
        }finally{
        	try {
        		if(sis!=null){
        			sis.close();
        		}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(baos!=null){
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(bos!=null){
					bos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return status;
    }
    
   
    
    private int preCheck() {
        int errCode = 0;
        if ( request == null )
            return 1;
        if ( uploadPath == null || uploadPath.trim().length() == 0 )
            return 2;
        else{
            File tmpF = new File(uploadPath);
            if (!tmpF.exists())
                return 2;
        }
        String contentType = request.getContentType();
        if(contentType==null){
        	return 3;
        }else if ( contentType.indexOf("multipart/form-data") == -1 )
            return 3;
        return errCode;
    }
    public String getParameter(String name){
        String value = "";
        if ( name == null || name.trim().length() == 0 )
            return value;
        value = (paramHt.get(name) == null)?"":(String)((ArrayList)paramHt.get(name)).get(0);
        return value.replaceAll("\r", "").replaceAll("\n", "");
    }
    public String[] getParameters(String name){
        if ( name == null || name.trim().length() == 0 )
            return null;
        if ( paramHt.get(name) == null )
            return null;
        List al = (List)paramHt.get(name);
        String[] strArr = new String[al.size()];
        for ( int i=0;i<al.size();i++ )
            strArr[i] = (String)al.get(i);
        return strArr;
    }
    
    public int getUpdFileSize(){
        return updFileArr.size();
    }
    
    public String[] getUpdFileNames(){
        String[] strArr = new String[updFileArr.size()];
        for ( int i=0;i<updFileArr.size();i++ )
            strArr[i] = (String)updFileArr.get(i);
        return strArr;
    }
    
    /**
     * 得到绝对路径
     * @return
     */
    public String[] getAbsFileNames(){
        String[] strArr = new String[updFileArr.size()];
        for ( int i=0;i<updFileArr.size();i++ ){
            strArr[i] = this.uploadPath+File.separator+(String)updFileArr.get(i);
         }
        return strArr;
    }
    
    private String getFileName(String input){
        int fIndex = input.lastIndexOf("\\");
        if (fIndex == -1) {
            fIndex = input.lastIndexOf("/");
            if (fIndex == -1) {
                return input;
            }
        } 
        input = input.substring(fIndex + 1);
        return input;
    }
    private String getFileNameByTime(String input){
        int index = input.indexOf(".");
        if(index == -1) {
        	return input+".jpg";
        }
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return  sdf.format(dt) + input.substring(index);
    }
}