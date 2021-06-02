package kkd.common.orm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kkd.common.util.ClobUtil;
import kkd.common.util.DateUtil;
/**
 * 通过反射生成oracle 相关sql语句
 * @author Administrator
 *
 */
public class OracleSQLUtil {

	private static Map<String, MyBean> cacheMyBean = new HashMap<String, MyBean>();
	
	public static void main(String[] args) {
//		Class c = AreaBean.class;
//		UserBean u = new UserBean();
//		u.setBirthday(new Date());
//		u.setBlood("A");
//		u.setCollegeId("123");
//		u.setCreatetime(new Date());
//		BigDecimal b=new BigDecimal(1);
	}
	/**
	 * 解析类中的字段并缓存
	 * @param c
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static MyBean parse(Class c) {
		MyBean myBean = new MyBean();
		Table_ table = (Table_) c.getAnnotation(Table_.class);
		if (table != null) {
			myBean.setTableName(table.value());
		} else {
			myBean.setTableName(c.getSimpleName());
		}

		List<MyField> fieldList = new ArrayList<MyField>();
		Field[] fields = c.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			MyField myField = new MyField();
			myField.setTypeClass(fields[i].getType());
			String name = fields[i].getName();
			Column_ col = fields[i].getAnnotation(Column_.class);
			if (col != null) {
				myField.setColName(col.value());
			} else {
				myField.setColName(name);
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
			String getM = "get" + toUperFirst(name);
			String setM = "set" + toUperFirst(name);
			myField.setGetM(getM);
			myField.setSetM(setM);
			myField.setType(fields[i].getType().getName());
			fieldList.add(myField);
		}
		myBean.setFields(fieldList);
		cacheMyBean.put(c.getName(), myBean);
		return myBean;
	}
	/**
	 * 根据对象 反射插入语句
	 * @param ins
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String getInsertSql(Object ins) {
		Class c = ins.getClass();
		MyBean myBean = cacheMyBean.get(c.getClass().getName());
		if (myBean == null) {
			myBean = parse(c);
		}
		StringBuffer sb = new StringBuffer("insert into ");
		List<MyField> fieldList = myBean.getFields();
		if (myBean.getTableName() == null) {
			sb.append(c.getSimpleName());
		} else {
			sb.append(myBean.getTableName());
		}
		sb.append("(");
		for (int i = 0; i < fieldList.size(); i++) {
			if (fieldList.get(i).getIgnore()||fieldList.get(i).getRel()) {
				continue;
			}
			if (fieldList.get(i).getColName() == null) {
				sb.append(fieldList.get(i).getName());
			} else {
				sb.append(fieldList.get(i).getColName());
			}
			sb.append(",");
		}
		sb = sb.deleteCharAt(sb.length() - 1);
		sb.append(") values(");
		try {
			for (int i = 0; i < fieldList.size(); i++) {
				MyField myField = fieldList.get(i);
				if (myField.getIgnore()||fieldList.get(i).getRel()) {
					continue;
				}
				Method m1 = c.getDeclaredMethod(myField.getGetM());
				Object value = m1.invoke(ins, (Object[])null);
				if(myField.getType().equalsIgnoreCase(Integer.class.getName())){
					if(myField.getSeq()!=null){
						sb.append(myField.getSeq()+".nextval");
					}else{
						sb.append(value);
					}
				}else if (value == null) {
					sb.append(value);
				} else if (myField.getType().equalsIgnoreCase(Date.class.getName())) {
					sb.append("TO_DATE('");
					sb.append(DateUtil.format(value));
					sb.append("','yyyy-mm-dd hh24:mi:ss')");
				}else if(
						myField.getType().equalsIgnoreCase(Double.class.getName())||
						myField.getType().equalsIgnoreCase(Double.class.getName())||
						myField.getType().equalsIgnoreCase(Long.class.getName())||
						myField.getType().equalsIgnoreCase(Boolean.class.getName())
								){
					sb.append(value);
				}else {
					sb.append("'");
					sb.append(value);
					sb.append("'");
				}
				sb.append(",");

			}
			sb = sb.deleteCharAt(sb.length() - 1);
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String getUpdateSql(Object ins) {
		Class c = ins.getClass();
		MyBean myBean = cacheMyBean.get(c.getClass().getName());
		if (myBean == null) {
			myBean = parse(c);
		}
		StringBuffer sb = new StringBuffer("update ");
		List<MyField> fieldList = myBean.getFields();
		if (myBean.getTableName() == null) {
			sb.append(c.getSimpleName());
		} else {
			sb.append(myBean.getTableName());
		}
		sb.append(" set ");
		try {
			for (int i = 0; i < fieldList.size(); i++) {
				if (fieldList.get(i).getIgnore() 
				|| fieldList.get(i).getPk()
				|| fieldList.get(i).getRel()) {
					continue;
				}
				if (fieldList.get(i).getColName() == null) {
					sb.append(fieldList.get(i).getName());
				} else {
					sb.append(fieldList.get(i).getColName());
				}
				sb.append("=");
				MyField myField = fieldList.get(i);
				Method m1 = c.getDeclaredMethod(myField.getGetM());
				Object value = m1.invoke(ins, (Object[])null);
				if (value == null) {
					sb.append(value);
				} else if (compareType(myField.getTypeClass(), Date.class)) {
					sb.append("TO_DATE('");
					sb.append(DateUtil.format(value));
					sb.append("','yyyy-mm-dd hh24:mi:ss')");
				} else if(
						compareType(myField.getTypeClass(), Integer.class)||
						compareType(myField.getTypeClass(), Double.class)||
						compareType(myField.getTypeClass(), Float.class)||
						compareType(myField.getTypeClass(), Long.class)||
						compareType(myField.getTypeClass(), Boolean.class)
								){
					sb.append(value);
				} else {
					sb.append("'");
					sb.append(value);
					sb.append("'");
				}
				sb.append(",");
			}
			sb = sb.deleteCharAt(sb.length() - 1);
		} catch (Exception e) {
		}

		return sb.toString();
	}
	/**
	 * 更加结果集 反射javaBean
	 * @param c 目标反射类型
	 * @param rs 结果集
	 * @param sb 列名 格式(,id,name,desc,) 用于匹配javaBean中的字段
	 * @return
	 */
	public static <T>T getData(Class<T> c, ResultSet rs, String sb) {
		MyBean myBean = cacheMyBean.get(c.getClass().getName());
		if (myBean == null) {
			myBean = parse(c);
		}
		T ins = null;
		List<MyField> fieldList = myBean.getFields();
		try {
			ins = c.newInstance();
			for (int i = 0; i < fieldList.size(); i++) {
				if (fieldList.get(i).getIgnore()
				||sb.indexOf(fieldList.get(i).getColName().toUpperCase()) == -1) {
					continue;
				}
				MyField myField = fieldList.get(i);
				Method m1 = c.getDeclaredMethod(myField.getSetM(),
						myField.getTypeClass());
				Object d = rs.getObject(myField.getColName());
				if(d instanceof BigDecimal){
					if(compareType(myField.getTypeClass(),Integer.class)){
						BigDecimal d1=(BigDecimal)d;
						d=d1.intValue();
					}else if(compareType(myField.getTypeClass(),Double.class)){
						BigDecimal d1=(BigDecimal)d;
						d=d1.doubleValue();
					}else if(compareType(myField.getTypeClass(),Float.class)){
						BigDecimal d1=(BigDecimal)d;
						d=d1.floatValue();
					}else if(compareType(myField.getTypeClass(),Long.class)){
						BigDecimal d1=(BigDecimal)d;
						d=d1.longValue();
					}else if(compareType(myField.getTypeClass(),Short.class)){
						BigDecimal d1=(BigDecimal)d;
						d=d1.shortValue();
					}else if(compareType(myField.getTypeClass(),Byte.class)){
						BigDecimal d1=(BigDecimal)d;
						d=d1.byteValue();
					}
				}else if(d instanceof Clob){
					Clob clob=(Clob)d;
					d=ClobUtil.Clob2String(clob);
				}
				m1.invoke(ins, d);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ins;
	}
	@SuppressWarnings("rawtypes")
	private static boolean compareType(Class c ,Class c1){
		if(c.getName().equals(c1.getName())){
			return true;
		}
		return false;
	}
	/** 首字母大写 */
	public static String toUperFirst(String source) {
		if (source == null) {
			return null;
		}
		char ch[] = source.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}
		return new String(ch);
	}

}
