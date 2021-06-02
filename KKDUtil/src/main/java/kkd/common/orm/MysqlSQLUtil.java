package kkd.common.orm;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kkd.common.dao.dbuitl.SqlParameter;
import kkd.common.invoke.InvokeUtil;
import kkd.common.invoke.MethodAccess;
import kkd.common.util.ClobUtil;
import kkd.common.util.StringUtil;

/**
 * 有待修改
 * @author tanbin
 *
 */
public class MysqlSQLUtil {

	private static Map<String, MyBean> cacheMyBean = new HashMap<String, MyBean>();
	
	/**
	 * 解析类中的字段并缓存
	 * @param c
	 * @return
	 */
	private static MyBean parse(Class<?> c) {
		MyBean myBean = new MyBean();
		Table_ table = (Table_) c.getAnnotation(Table_.class);
		if (table != null) {
			myBean.setTableName(table.value());
		} else {
			myBean.setTableName(c.getSimpleName());
		}

		List<MyField> fieldList = new ArrayList<MyField>();
		Class<?> superObj=c.getSuperclass();
		Field[] fields1=null;
		if(!(superObj.equals(Object.class))){
			fields1=superObj.getDeclaredFields();
		}
		Field[] fields = c.getDeclaredFields();
		fields=margeArr(fields1, fields);
		for (int i = 0; i < fields.length; i++) {
			//如果是静态字段
			if((fields[i].getModifiers() & Modifier.STATIC) != 0){
				continue;
			}
			MyField myField = new MyField();
			myField.setTypeClass(fields[i].getType());
			myField.setType(fields[i].getType().getName());
			String name = fields[i].getName();
			Column_ col = fields[i].getAnnotation(Column_.class);
			if (col != null) {
				myField.setColName(col.value());
			} else {
				myField.setColName(name);
			}
			Update_ u = fields[i].getAnnotation(Update_.class);
			if (u != null) {
				myField.setUpdate(u.value());
			} else {
				myField.setUpdate(true);
			}
			Transient_ t = fields[i].getAnnotation(Transient_.class);
			if (t != null) {
				myField.setIgnore(true);
			} else {
				myField.setIgnore(false);
			}
			Id_ id = fields[i].getAnnotation(Id_.class);
			if (id != null) {
				myField.setPk(true);
			} else {
				myField.setPk(false);
			}
			Rel_ rel = fields[i].getAnnotation(Rel_.class);
			if (rel != null) {
				myField.setRel(true);
			} else {
				myField.setRel(false);
			}
			Seq_ seq = fields[i].getAnnotation(Seq_.class);
			if (seq != null) {
				myField.setSeq(seq.value());
			}
			myField.setName(name);
			String UperName=toUperFirst(name);
			myField.setGetM("get" + UperName);
			myField.setSetM("set" + UperName);
			fieldList.add(myField);
		}
		myBean.setFields(fieldList);
		cacheMyBean.put(c.getName(), myBean);
		return myBean;
	}
	
	
	private static Field[] margeArr(Field[] s,Field[] d){
		if(s!=null && s.length>0){
			if(d!=null && d.length==0){
				return s;
			}
			Map<String,Field> map=new HashMap<String, Field>();
			for(int i=0;i<s.length;i++){
				Field sf=s[i];
				map.put(sf.getName(),sf);
			}
			for(int j=0;j<d.length;j++){
				Field df=d[j];
				map.put(df.getName(),df);
			}
			Field[] f=new Field[map.size()];
			int i=0;
			for(Map.Entry<String, Field> entry:map.entrySet()){   
				f[i++]=entry.getValue();
			}   
		}
		return d;
	}
	
	/**
	 * 根据对象 反射插入语句
	 * @param ins
	 * @return
	 */
	public static String getInsertSql(Object ins,List<SqlParameter> args) {
		Class<?> c = ins.getClass();
		MethodAccess ma=InvokeUtil.getMethodAccess(c);
		MyBean myBean = cacheMyBean.get(c.getName());
		if (myBean == null) {
			myBean = parse(c);
		}
		List<MyField> fieldList = myBean.getFields();
		StringBuilder sb = new StringBuilder("insert into ");
		sb.append(myBean.getTableName());
		sb.append("(");
		boolean first=true;
		for (int i = 0,size=fieldList.size(); i < size; i++) {
			MyField field=fieldList.get(i);
			if (field.getIgnore()||field.getRel()||"".equals(field.getSeq()) ||!field.getUpdate()) {
				continue;
			}
			if(!first){
				sb.append(",");	
			}else{
				first=false;
			}
			sb.append("`");
			String colName=field.getColName()==null?field.getName():field.getColName();
			sb.append(colName);
			sb.append("`");
			
		}
		sb.append(") values(");
		try {
			first=true;
			for (int i = 0,size=fieldList.size(); i < size; i++) {
				MyField myField = fieldList.get(i);
				if (myField.getIgnore()||myField.getRel()||"".equals(myField.getSeq())||!myField.getUpdate()) {
					continue;
				}
				if(myField.getGetMIndex()==null){
					myField.setGetMIndex(ma.getIndex(myField.getGetM()));
				}
				if(!first){
					sb.append(",");	
				}else{
					first=false;
				}
				sb.append("?");
				Object value=ma.invoke(ins, myField.getGetMIndex(),(Object[]) null);
//				if(Integer.class.equals(myField.getTypeClass())){
//					args.add(new SqlParameter(Types.INTEGER, value));
//				}else if(Byte.class.equals(myField.getTypeClass())){
//					args.add(new SqlParameter(Types.INTEGER, value));
//				}else if(Short.class.equals(myField.getTypeClass())){
//					args.add(new SqlParameter(Types.INTEGER, value));
//				}else if(Boolean.class.equals(myField.getTypeClass())){
//					args.add(new SqlParameter(Types.BOOLEAN, value));
//				}else if(Float.class.equals(myField.getTypeClass())){
//					args.add(new SqlParameter(Types.FLOAT, value));
//				}else if(Double.class.equals(myField.getTypeClass())){
//					args.add(new SqlParameter(Types.DOUBLE, value));
//				}else 
				
				if(java.sql.Date.class.equals(myField.getTypeClass())){
					args.add(new SqlParameter(Types.TIMESTAMP, value));
				}else if(Date.class.equals(myField.getTypeClass())){
					args.add(new SqlParameter(Types.TIMESTAMP, value));
				}else if(Boolean.class.equals(myField.getTypeClass())){
					args.add(new SqlParameter(Types.BOOLEAN, value));
				}
				else{
					args.add(new SqlParameter(Types.VARCHAR, value));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		sb.append(")");
		return sb.toString();
	}
	
	/**
	 * 根据对象反射更新语句 不包括where条件
	 * @param ins
	 * @return
	 */
	public static String getUpdateSql(Object ins,List<SqlParameter> args) {
		Class<?> c = ins.getClass();
		MethodAccess ma=InvokeUtil.getMethodAccess(c);
		MyBean myBean = cacheMyBean.get(c.getName());
		if (myBean == null) {
			myBean = parse(c);
		}
		StringBuilder sb = new StringBuilder("update ");
		List<MyField> fieldList = myBean.getFields();
		sb.append(myBean.getTableName());
		sb.append(" set ");
		try {
			for (int i = 0,size=fieldList.size(); i < size; i++) {
				MyField myfield=fieldList.get(i);
				if (myfield.getIgnore() 
				|| myfield.getPk()
				|| !myfield.getUpdate()
				|| myfield.getRel()) {
					continue;
				}
				String colName=myfield.getColName()==null?myfield.getName():myfield.getColName();
				sb.append("`");
				sb.append(colName);
				sb.append("`=?");
				if(myfield.getGetMIndex()==null){
					myfield.setGetMIndex(ma.getIndex(myfield.getGetM()));
				}
				Object value=ma.invoke(ins, myfield.getGetMIndex(),(Object[]) null);
				if(java.sql.Date.class.equals(myfield.getTypeClass())){
					args.add(new SqlParameter(Types.TIMESTAMP, value));
				}else if(Date.class.equals(myfield.getTypeClass())){
					args.add(new SqlParameter(Types.TIMESTAMP, value));
				}else{
					args.add(new SqlParameter(Types.VARCHAR, value));
				}
				sb.append(",");
			}
			sb = sb.deleteCharAt(sb.length() - 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	
	
	/**
	 * 更加结果集 反射javaBean
	 * @param c 目标反射类型
	 * @param rs 结果集
	 * @param sb 列名 格式(,id,name,desc,) 用于匹配javaBean中的字段
	 * @return
	 * @throws SQLException 
	 */
	public static <T> T getData(Class<T> c, ResultSet rs, Map<String,Boolean> colums) throws SQLException {
		return getDataFor(c, rs, colums,"",2);
	}


	@SuppressWarnings("unchecked")
	private static <T> T getDataFor(Class<T> c, ResultSet rs,
			Map<String, Boolean> colums,String path,Integer deep) throws SQLException {
		if(deep==0){
			return null;
		}
		if(rsBase(c)){
			T data=null;
			data=(T)rs2Type(rs, null,c);
			return data;
		}
		MyBean myBean = cacheMyBean.get(c.getName());
		MethodAccess ma=InvokeUtil.getMethodAccess(c);
		if (myBean == null) {
			myBean = parse(c);
		}
		T ins = null;
		List<MyField> fieldList = myBean.getFields();
		try {
			ins = (T)ma.newInstance();
			for (int i = 0; i < fieldList.size(); i++) {
				Object d = null;
				MyField myField = fieldList.get(i);
//				sb.indexOf(","+myField.getColName().toUpperCase()+",") == -1
				if(myField.getIgnore()){
					continue;
				}
				String path1=StringUtil.isEmpty(path)?myField.getColName():path+"."+myField.getColName();
				path1=path1.toUpperCase();
				if (colums.get(path1+".")==null) {
					continue;
				}
				if(!(rsBase(myField.getTypeClass()))){
					d=getDataFor(myField.getTypeClass(), rs, colums,path1,deep-1);
				}else{
					//将结果集转换成目标类型
					d = rs2Type(rs, path1,myField.getTypeClass());
				}
				
				
				if(myField.getSetMIndex()==null){
					myField.setSetMIndex(ma.getIndex(myField.getSetM()));
				}
				ma.invoke(ins, myField.getSetMIndex(), d);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ins;
	}
	
	/**
	 * 将结果集转换成目标类型
	 * @param rs
	 * @param myField
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	private static Object rs2Type(ResultSet rs, String colName,Class<?> type)
			throws SQLException {
		Object d=null;
		//得到列的索引
		int columnIndex=colName==null?1:rs.findColumn(colName);
		if(String.class.equals(type)){
			d=rs.getObject(columnIndex);
			if(d instanceof Clob){
				Clob clob=(Clob)d;
				try {
					d=ClobUtil.Clob2String(clob);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				if(colName==null){
					d=(String)d;
				}else{
					d=(String)d;
				}
			}
		}else if(Date.class.equals(type)){
			Timestamp ts=null;
			ts=rs.getTimestamp(columnIndex);
			if(ts!=null){
				d=new Date(ts.getTime());	
			}
		}else if(Boolean.class.equals(type)){
			d=rs.getBoolean(columnIndex);
		}else if(Byte.class.equals(type)){
			d=rs.getByte(columnIndex);
		}else if(Short.class.equals(type)){
			d=rs.getShort(columnIndex);
		}else if(Integer.class.equals(type)){
			d=rs.getInt(columnIndex);
		}else if(Float.class.equals(type)){
			d=rs.getFloat(columnIndex);
		}else if(Double.class.equals(type)){
			d=rs.getDouble(columnIndex);
		}else if(Long.class.equals(type)){
			d=rs.getLong(columnIndex);
		}else{
			d=rs.getObject(columnIndex);
		}
		//判断得到的数据是否为空 只能在getxxx()后执行。
		d=rs.wasNull()?null:d;
		return d;
	}
	
	
	
	/** 首字母大写 */
	private static String toUperFirst(String source) {
		if (source == null) {
			return null;
		}
		char ch[] = source.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}
		return new String(ch);
	}
	
	/**
	 * 判断是不是八大基本类型
	 * @param c
	 * @return
	 */
	public static  boolean isBaseType(Class<?> c){
		if(c.equals(Short.class)
				||c.equals(short.class)
				||c.equals(Integer.class)
				||c.equals(int.class)
				||c.equals(Integer.class)
				||c.equals(long.class)
				||c.equals(Long.class)
				||c.equals(short.class)
				||c.equals(Short.class)
				||c.equals(float.class)
				||c.equals(Float.class)
				||c.equals(double.class)
				||c.equals(Double.class)
				||c.equals(char.class)
				||c.equals(Character.class)
				||c.equals(boolean.class)
				||c.equals(Boolean.class)
				||c.equals(byte.class)
				||c.equals(Byte.class)
				)
			
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 判断是不是八大基本类型
	 * @param c
	 * @return
	 */
	public static  boolean rsBase(Class<?> c){
		if(isBaseType(c)
				||String.class.equals(c)
				||Date.class.equals(c)
				||BigDecimal.class.equals(c)		
		){
			return true;
		}else{
			return false;
		}
	}


	public static String getUpdateSqlByValue(Object ins, List<SqlParameter> args) {
		Class<?> c = ins.getClass();
		MethodAccess ma=InvokeUtil.getMethodAccess(c);
		MyBean myBean = cacheMyBean.get(c.getName());
		if (myBean == null) {
			myBean = parse(c);
		}
		StringBuilder sb = new StringBuilder("update ");
		List<MyField> fieldList = myBean.getFields();
		sb.append(myBean.getTableName());
		sb.append(" set ");
		try {
			for (int i = 0,size=fieldList.size(); i < size; i++) {
				MyField myfield=fieldList.get(i);
				if (myfield.getIgnore() 
				|| myfield.getPk()
				|| myfield.getRel()) {
					continue;
				}
				if(myfield.getGetMIndex()==null){
					myfield.setGetMIndex(ma.getIndex(myfield.getGetM()));
				}
				Object value=ma.invoke(ins, myfield.getGetMIndex(),(Object[]) null);
				if(value!=null){
					String colName=myfield.getColName()==null?myfield.getName():myfield.getColName();
					sb.append("`");
					sb.append(colName);
					sb.append("`=?");
					if(java.sql.Date.class.equals(myfield.getTypeClass())){
						args.add(new SqlParameter(Types.TIMESTAMP, value));
					}else if(Date.class.equals(myfield.getTypeClass())){
						args.add(new SqlParameter(Types.TIMESTAMP, value));
					}else{
						args.add(new SqlParameter(Types.VARCHAR, value));
					}
					sb.append(",");
				}
			}
			sb = sb.deleteCharAt(sb.length() - 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		Class<?> c=MysqlSQLUtil.class;
		Field[] f=c.getDeclaredFields();
		for (int i = 0; i < f.length; i++) {
			Field f1=f[i];
			System.out.println((f[i].getModifiers() & Modifier.STATIC) != 0);
			String mod = Modifier.toString(f[i].getModifiers());
			System.out.println(f[i].getName());
		}
	}
	

}
