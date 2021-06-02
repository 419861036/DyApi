package com.exe.bo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.exe.base.vo.CmdVo;
import com.exe.base.vo.ExeResourceVo;
import com.exe.base.vo.SegVo;
import com.exe.dao.RuleDao;
import com.exe.param.RuleParam;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kkd.common.dao.dbuitl.JDBC;
import kkd.common.dao.dbuitl.JDBC.DbHelper;
import kkd.common.dao.dbuitl.JDBC.MyBack;
import kkd.common.entity.Msg;
import kkd.common.util.StringUtil;

public class RuleBo {
	private final static Logger logger = LoggerFactory.getLogger(RuleBo.class);
	public static void getJDBC(final MyBack myBack) {
		final JDBC jdbc = new JDBC();
		jdbc.execute(myBack);
	}

	
	public static void list(RuleParam param,final Msg<List<ExeResourceVo>> msg,Boolean parse) {
		getJDBC(new MyBack() {
			public Object exe(final DbHelper dh) throws Exception {
				final List<ExeResourceVo> list = RuleDao.list(param, dh);
				//parseRule(rule);
				//parseRule1(rule);
				msg.setV(list);
				return null;
			}
		});
		if(parse){
			final List<ExeResourceVo> list =msg.getV();
			for (final ExeResourceVo rule : list) {
				try {
					parseRule1(rule);
				} catch (final Exception e) {
					logger.error("解析规则："+rule.getId());
					throw new RuntimeException(e);
				}
			}
		}
		
	}
	public static void list(RuleParam param,final Msg<List<ExeResourceVo>> msg) {
		list(param,msg,true);
	}
	
	public static void getById(final Integer id, final Msg<ExeResourceVo> msg) {
		getJDBC(new MyBack() {
			public Object exe(final DbHelper dh) throws Exception {
				final ExeResourceVo rule = RuleDao.getById(id, dh);
				//parseRule(rule);
				//parseRule1(rule);
				msg.setV(rule);
				return null;
			}
		});
		final ExeResourceVo rule =msg.getV();
		try {
			parseRule1(rule);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
	public static void getByPath(final String path, final Msg<ExeResourceVo> msg) {
		getJDBC(new MyBack() {
			public Object exe(final DbHelper dh) throws Exception {
				final ExeResourceVo rule = RuleDao.getByPath(path, dh);
				//parseRule(rule);
				//parseRule1(rule);
				msg.setV(rule);
				return null;
			}
		});
		final ExeResourceVo rule =msg.getV();
	}
	
	private static void parseRule1(final ExeResourceVo rule) throws Exception {
		final String caiRuleXml = rule.getCaiConfig();
		if(!StringUtil.isEmpty(caiRuleXml)){
			readXML1(caiRuleXml, rule);
		}
	}
	

	public static void readXML1(final String txt,final ExeResourceVo rule) throws Exception {
		// 创建saxReader对象
		final SAXReader reader = new SAXReader();
		// 通过read方法读取一个文件 转换成Document对象
		final InputStream is=new ByteArrayInputStream(txt.getBytes("UTF-8"));
		final Document document = reader.read(is);
		// 获取根节点元素对象
		final Element node = document.getRootElement();


		final Map<String,SegVo> segMap=new LinkedHashMap<>();
		rule.setSegMap(segMap);
		final List<Element> pages=node.elements("seg");
		for (final Element element : pages) {
			final String name=element.attributeValue("name");
			final SegVo seg=new SegVo();
			if(segMap.get(name)!=null){
				throw new RuntimeException("seg name重复："+name);
			}
			segMap.put(name, seg);
			seg.setName(name);

			final Map<String,CmdVo> actionMap=new LinkedHashMap<>();
			seg.setActionMap(actionMap);

			getCmd(seg,element,actionMap);
		}
	}
	
	private static void getCmd(final SegVo seg, final Element element,final Map<String, CmdVo> actionMap) {
		final List<Element> pages=element.elements();
		int i=0;
		for (final Element actionEle : pages) {
			final CmdVo cmd=new CmdVo();
			cmd.setCaiSeg(seg);
			
			cmd.setName(actionEle.attributeValue("name"));
			cmd.setOp(actionEle.getName());
			final String delayStr=actionEle.attributeValue("delay");
			//延迟执行
			final Integer delay=delayStr==null?null:Integer.parseInt(delayStr);
			cmd.setDelay(delay);
			final String retryStr=actionEle.attributeValue("retry");
			//延迟执行
			final Integer retry=retryStr==null?null:Integer.parseInt(retryStr);
			cmd.setRetry(retry);
			cmd.setOrder(i++);
			final List<Attribute> attrList=actionEle.attributes();
			final Map<String, String> param=new HashMap<>();
			for (final Attribute attribute : attrList) {
				param.put(attribute.getName(), attribute.getValue());
			}
			cmd.setParam(param);
			final String hasBody=param.get("hasBody");
			
			if("true".equalsIgnoreCase(hasBody)){
				cmd.setBody(actionEle.getText());
			}
			final String parseBody=param.get("parseBody");
			if("true".equalsIgnoreCase(parseBody)){

				JSONArray jsonBody=parseBody(actionEle);
				cmd.setJsonBody(jsonBody);
			}
//			Map<String, RuleField> fieldMap=new HashMap<>();
			if(cmd.getName()==null){
				System.out.println("cmd name为空");
			}
			if(cmd.getName().equalsIgnoreCase("找到列表元素")){
				logger.debug("列表元素:{}",cmd.getName());
			}
			
			//getField1(actionEle, fieldMap);
//			cmd.setFieldMap(fieldMap);
			if(actionMap.get(cmd.getName())!=null){
				throw new RuntimeException("cmd name重复："+cmd.getName());
			}
			actionMap.put(cmd.getName(), cmd);
		}
	}

	private static JSONArray parseBody(Element element) {
		JSONArray ja=new JSONArray();
		final List<Element> fields=element.elements("field");
		for (Element element2 : fields) {
			JSONObject field=new JSONObject();
			ja.add(field);
			List<Attribute> attrList=element2.attributes();
			for (Attribute attr : attrList) {
				field.put(attr.getName(), attr.getValue());
			}
		}
		return ja;
	}

	

	
//	private static void getField1( Element element,
//			Map<String, RuleField> fieldMap) {
//		List<Element> fields=element.elements("field");
//		for (Element element2 : fields) {
//			String name=element2.attributeValue("name");
//			String to=element2.attributeValue("to");
//			String handle=element2.attributeValue("handle");
//			String attr=element2.attributeValue("attr");
//			String cssSelector=element2.attributeValue("cssSelector");
//			String desc=element2.attributeValue("desc");
//			String var=element2.attributeValue("var");
//			String codes=element2.getText();
//			RuleField caiRuleField=new RuleField();
//			caiRuleField.setAttr(attr);
//			caiRuleField.setCodes(codes);
//			caiRuleField.setDesc(desc);
//			caiRuleField.setCssSelector(cssSelector);
//			caiRuleField.setHandle(handle);
//			caiRuleField.setName(name);
//			caiRuleField.setTo(to);
//			caiRuleField.setVar(var);
//			if(fieldMap.get(name)!=null){
//				throw new RuntimeException("field name重复："+name);
//			}
//			fieldMap.put(name, caiRuleField);
//			LogUtil.log("{}",name);
//			List<Element> childs=element.elements("field");
//			if(childs!=null && !childs.isEmpty()){
//				Map<String,RuleField> fieldMap1=new HashMap<>();
//				caiRuleField.setFieldMap(fieldMap1);
//				getField1(element2, fieldMap1);
//			}
//			
//		}
//	}
}
