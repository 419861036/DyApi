package kkd.common.util;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.SQLException;

public class ClobUtil {
	public static String Clob2String(Clob clob) throws SQLException, IOException{
		StringBuffer stringBuf = new StringBuffer();
		int length = 0;
		Reader  reader=null;
		try {
			reader =clob.getCharacterStream();
			char[] buffer = new char[128];
			// 读取数据库 //每10个10个读取
			while ((length = reader.read(buffer)) != -1){
				for (int j = 0; j < length; j++) {
					stringBuf.append(buffer[j]);
				}
			}
			return stringBuf.toString();
		}finally{
			if(reader!=null){
				reader.close();
			}
		}
		
	}
}
