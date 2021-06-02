package kkd.common.entity;

import java.util.Date;

import kkd.common.logger.LogWriter;
import kkd.common.util.DateUtil;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * 自定义日期转换器
 * 
 * @author Administrator
 * 
 */
public class CustomDateConverter implements Converter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class cla) {
		if(cla.getName().equals(Date.class.getName())
		||cla.getName().equals(java.sql.Date.class.getName())
		||cla.getName().equals(java.sql.Timestamp.class.getName())
		||cla.getName().equals(java.sql.Time.class.getName())
		){
			return true;
		}
		return false;
	}

	@Override
	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		if (value == null) {
			writer.setValue("");
			return;
		}
		Date date = (Date) value;
		String dateStr = "";
		try {
			dateStr = DateUtil.format(date, DateUtil.DATETIME_PATTERN);
		} catch (IllegalArgumentException except) {
			except.printStackTrace();
		}
		writer.setValue(dateStr);
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		Date date = null;
		if (reader!=null&&reader.getValue() != null) {
			try {
				date = DateUtil.parse(reader.getValue(), DateUtil.DATETIME_PATTERN);
			} catch (Exception e) {
				LogWriter.error(reader.getValue());
			}
		}
		return date;
	}

}
