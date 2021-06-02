package kkd.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kkd.common.exception.KKDException;

/**
 * 日期类型与字符串类型相互转换工具类
 * 
 * @since fanhoujun 2008-3-20
 */
public class DateUtil {

	private static long debugTime = 0;

	public static void debugCurrentDate(String debugDate) {
		if (StringUtil.isEmpty(debugDate)) {
			return;
		}
		final long time = DateUtil.parse(debugDate).getTime();
		debugTime = time - time % MILLIS_PER_DAY + MILLIS_PER_DAY;
	}

	public static void debugCurrentDate(Date debugDate) {
		if (debugDate == null) {
			return;
		}
		final long time = debugDate.getTime();
		debugTime = time - time % MILLIS_PER_DAY + MILLIS_PER_DAY;
	}

	/** 1000 */
	public static final long MILLIS_PER_SECOND = 1000;

	/** 60×1000 */
	public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;

	/** 60×60×1000 */
	public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;

	/** 24×60×60×1000 */
	public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

	/** yyyyMMdd */
	public static final String COMPACT_DATE_PATTERN = "yyyyMMdd";

	/** yyyy-MM-dd */
	public static final String DATE_PATTERN = "yyyy-MM-dd";

	/** yyyy-MM-dd HH:mm:ss */
	public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/** yyyy-MM-dd HH:mm */
	public static final String TRIM_SECOND_PATTERN = "yyyy-MM-dd HH:mm";
	
	/** MM-dd HH:mm */
	public static final String SECOND_PATTERN = "MM-dd HH:mm";

	/** yyyyMMdd HH:mm */
	public static final String COMPACT_TRIM_SECOND_PATTERN = "yyyyMMdd HH:mm";
	
	/** yyyyMMddhhmmss */
	public static final String COMPACT_TRIM_SECOND_PATTERN_NO_SPACE = "yyyyMMddhhmmss";
	/** yyyyMMddHHmmss */
	public static final String COMPACT_TRIM_HH_SECOND_PATTERN_NO_SPACE = "yyyyMMddHHmmss";

	/** 默认的pattern: yyyy-MM-dd */
	public static Date parse(String str) {
		return parse(str, DATE_PATTERN);
	}

	/**
	 * 将字符串按照一定的格式转化为日期
	 */
	public static Date parse(String str, String pattern) {
		if (StringUtil.isBlank(str)) {
			return null;
		}
		DateFormat parser = new SimpleDateFormat(pattern);
		try {
			return parser.parse(str);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Can't parse " + str + " using " + pattern);
		}
	}

	/**
	 * 判断两个时间是否同一年
	 * 
	 * @since Lu Song 2009-1-13
	 */
	public static boolean isSameYear(Date firstDate, Date secondDate) {
		if (firstDate == null || secondDate == null) {
			return false;
		}
		return DateUtil.dayOfYear(firstDate) == DateUtil.dayOfYear(secondDate);
	}

	/**
	 * 设置某年为最后一天
	 * 
	 * @since Lu Song 2009-1-13
	 */
	public static Date setLastDay(Date date) {
		if (date == null) {
			return null;
		}
		return DateUtil.parse(DateUtil.dayOfYear(date) + "1231", "yyyyMMdd");
	}

	/**
	 * 得到指定期号对应之月的最后一秒
	 * 
	 * @param issue
	 * @return
	 * @since Lu Song 2009-1-22
	 */
	public static Date getLastSecondInMonth(Integer issue) {
		Calendar calendar = Calendar.getInstance();
		Date date = parse(String.valueOf(issue), "yyyyMM");
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.SECOND, -1);
		return calendar.getTime();
	}

	public static int thisYear() {
		return dayOfYear(DateUtil.currentDate());
	}


	/**
	 * 获得当前时间
	 */
	public static Date currentDate() {
		if (debugTime == 0) {
			return new Date();
		} else {
			return new Date(debugTime + System.currentTimeMillis() % MILLIS_PER_DAY);
		}
	}

	/**
	 * 格式化当前时间，返回字符串
	 */
	public static String format(String pattern) {
		return format(currentDate(), pattern);
	}

	/**
	 * 根据时间返回字符串:yyyy-MM-dd
	 */
	public static String format(Object date) {
		return format(date, DATE_PATTERN);
	}

	/**
	 * 根据时间返回字符串
	 */
	public static String format(Object date, String pattern) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat  df=new SimpleDateFormat(pattern);
		return df.format(date);
	}

	/**
	 * 截取到月
	 */
	public static Date trimMonth(Date date) {
		return DateUtil.parse(DateUtil.format(date, "yyyyMM"), "yyyyMM");
	}
	/**
	 * 截取当前日期
	 * 
	 * @param date
	 * @param type<br/>
	 * Calendar.MONTH，Calendar.DAY_OF_MONTH，Calendar.HOUR_OF_DAY，Calendar
	 *            .MINUTE Calendar.SECOND ,Calendar.MILLISECOND
	 * @return Date
	 */
	public static void trim(Calendar cal, int type) {
		boolean tmp = type == Calendar.MONTH;
		if (tmp) {
			cal.set(Calendar.MONTH, 0);
		}
		tmp |= type == Calendar.DAY_OF_MONTH;
		if (tmp) {
			cal.set(Calendar.DAY_OF_MONTH, 1);
		}
		
		tmp |= type == Calendar.HOUR_OF_DAY;
		if (tmp) {
			cal.set(Calendar.HOUR_OF_DAY, 0);
		}
		tmp |= type == Calendar.MINUTE;
		if (tmp) {
			cal.set(Calendar.MINUTE, 0);
		}
		tmp |= type == Calendar.SECOND;
		if(tmp){
			cal.set(Calendar.SECOND, 0);
		}
		tmp |= type == Calendar.MILLISECOND;
		if(tmp){
			cal.set(Calendar.MILLISECOND, 0);
		}
	}
	public static void addHours(Calendar cal, int hours) {
		add(cal, Calendar.HOUR_OF_DAY, hours);
	}

	public static void addMinutes(Calendar cal, int minutes) {
		add(cal, Calendar.MINUTE, minutes);
	}
	public static void addSeconds(Calendar cal, int second) {
		add(cal, Calendar.SECOND, second);
	}

	public static void addDays(Calendar cal, int days) {
		add(cal, Calendar.DATE, days);
	}

	public static void addMonths(Calendar cal, int months) {
		add(cal, Calendar.MONTH, months);
	}

	public static void addYears(Calendar cal, int years) {
		add(cal, Calendar.YEAR, years);
	}
	/**
	 * 截取当前日期
	 * 
	 * @param date
	 * @param type<br/>
	 * Calendar.MONTH，Calendar.DAY_OF_MONTH，Calendar.HOUR_OF_DAY，Calendar
	 *            .MINUTE Calendar.SECOND ,Calendar.MILLISECOND
	 * @return Date
	 */
	public static Date trim(Date date, int type) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		trim(cal, type);
		return cal.getTime();
	}

	public static Date addHours(Date date, int hours) {
		return add(date, Calendar.HOUR_OF_DAY, hours);
	}

	public static Date addMinutes(Date date, int minutes) {
		return add(date, Calendar.MINUTE, minutes);
	}
	public static Date addSeconds(Date date, int second) {
		return add(date, Calendar.SECOND, second);
	}

	public static Date addDays(Date date, int days) {
		return add(date, Calendar.DATE, days);
	}

	public static Date addMonths(Date date, int months) {
		return add(date, Calendar.MONTH, months);
	}

	public static Date addYears(Date date, int years) {
		return add(date, Calendar.YEAR, years);
	}
	
	private static void add(Calendar cal, int field, int amount) {
		cal.add(field, amount);
	}
	private static Date add(Date date, int field, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(field, amount);
		return cal.getTime();
	}

	/**
	 * 计算两个日期之间的天数
	 */
	public static final int daysBetween(Date early, Date late) {
		early=trim(early, Calendar.HOUR_OF_DAY);
		late=trim(late, Calendar.HOUR_OF_DAY);
		return (int) ((early.getTime() - late.getTime()) / MILLIS_PER_DAY);
	}
	
	/**
	 * 计算两个日期之间的天数的绝对值
	 */
	public static final int daysBetweenAbs(Date early, Date late) {
		return Math.abs(daysBetween(early, late));
	}

	/**
	 * 对两个日期按照先后顺序排列，Date是可变类，因此可以直接交换它们的值
	 * 
	 * @param begin
	 * @param end
	 */
	public static final void sorted(Date begin, Date end) {
		sorted(begin, end, false);
	}

	/**
	 * 对两个日期按照先后顺序排列，Date是可变类，因此可以直接交换它们的值
	 * 
	 * @param begin
	 * @param end
	 * @param roundEnd
	 *            :把end设为下一天的0点0时0分
	 * @since Fan Houjun 2010-1-29
	 */
	public static final void sorted(Date begin, Date end, boolean roundEnd) {
		if (begin != null && end != null && begin.after(end)) {
			long t = begin.getTime();
			begin.setTime(end.getTime());
			end.setTime(t);
		}
		if (roundEnd && end != null) {
			end.setTime(roundDay(end).getTime());
		}
	}

	/**
	 * @param date
	 * @return 下一天的0时0分0秒0...
	 */
	public static final Date roundDay(Date date) {
		return addDays(trim(date, Calendar.HOUR_OF_DAY), 1);
	}
	
	public static Date beforeDay(Date date){
		return addDays(trim(date, Calendar.HOUR_OF_DAY), -1);
	}
	/**
	 * 比较某个时间和当前时间的 分钟数是否在某个区间之内
	 * 
	 * @param date
	 *            要比较的日期
	 * @param minutes
	 *            比较的范围
	 * @return boolean 在范围之内 true 不在范围之内 false
	 * @since 黄显宏 2008-8-4
	 */
	public static boolean isBetweenInMins(Date date, int minutes) {
		Date now=new Date();
		now=trim(now, Calendar.SECOND);
		date=trim(date, Calendar.SECOND);
		int s=(int) ((date.getTime() - now.getTime()) / MILLIS_PER_MINUTE);
		return Math.abs(s)<minutes;
//		Calendar now = Calendar.getInstance();
//		int hour = now.get(Calendar.HOUR_OF_DAY);
//		int min = now.get(Calendar.MINUTE);
//
//		Calendar com = Calendar.getInstance();
//		com.setTime(date);
//		int hours = com.get(Calendar.HOUR_OF_DAY);
//		int mins = com.get(Calendar.MINUTE);
//		return (hours == hour && Math.abs(mins - min) < minutes);
	}

	/**
	 * 判断两个日期年月日是否相等
	 * 
	 * @param begin
	 * @param end
	 * @return boolean
	 * @since 黄显宏 2008-08-06
	 */
	public static final boolean isSameDate(Date begin, Date end) {
		if (begin == null || end == null) {
			return false;
		}
		Calendar calB = Calendar.getInstance();
		calB.setTime(begin);
		Calendar calE = Calendar.getInstance();
		calE.setTime(end);
		return calB.get(Calendar.YEAR) == calE.get(Calendar.YEAR)
				&& calB.get(Calendar.MONTH) == calE.get(Calendar.MONTH)
				&& calB.get(Calendar.DATE) == calE.get(Calendar.DATE);
	}

	/**
	 * 判断两个日期是否是同一月
	 * 
	 * @param begin
	 * @param end
	 * @return
	 * @since ChenDan 2009-1-17
	 */
	public static final boolean isSameMonth(Date begin, Date end) {
		if (begin == null || end == null) {
			return false;
		}
		Calendar calB = Calendar.getInstance();
		calB.setTime(begin);
		Calendar calE = Calendar.getInstance();
		calE.setTime(end);
		return calB.get(Calendar.YEAR) == calE.get(Calendar.YEAR)
				&& calB.get(Calendar.MONTH) == calE.get(Calendar.MONTH);
	}

	/**
	 * 返回日期的年
	 * 
	 * @param date
	 *            日期
	 * @return
	 * @since zengqiao 2008-12-29
	 */
	public static int dayOfYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 返回日期的月
	 * 
	 * @param date
	 *            日期
	 * @return
	 * @since zengqiao 2008-12-29
	 */
	public static int dayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 返回指定日期的下一个月的年月
	 * 
	 * @param date
	 * @return
	 * @since Ma Yanna 2009-8-14
	 */
	public static int dayOfNextYearMonth(Date date) {
		int year = dayOfYear(date);
		int month = dayOfMonth(date);
		if (month == 12) {
			return (year + 1) * 100 + 1;
		}
		return year * 100 + month + 1;
	}

	/**
	 * 返回日期的日
	 * 
	 * @param date
	 *            日期
	 * @return
	 * @since zengqiao 2008-12-29
	 */
	public static int dayOfDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar.get(Calendar.DATE);
	}

	/**
	 * 返回当前月第一天，即yyyy-MM-01 00:00:00
	 * 
	 * @return yyyy-MM-01 00:00:00
	 * @since ChenDan 2009-1-7
	 */
	public static Date getStartDateTimeOfCurrentMonth() {
		return trim(new Date(), Calendar.DAY_OF_MONTH);
//		return getStartDateTimeOfMonth(DateUtil.currentDate());
	}

	/**
	 * The value of
	 * <ul>
	 * <li>Calendar.HOUR_OF_DAY
	 * <li>Calendar.MINUTE
	 * <li>Calendar.MINUTE
	 * </ul>
	 * will be set 0.
	 */
	public static Date getStartDateTimeOfMonth(Date date) {
		return trim(date, Calendar.DAY_OF_MONTH);
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		cal.set(Calendar.DAY_OF_MONTH, 1);
//		cal.set(Calendar.HOUR_OF_DAY, 0);
//		cal.set(Calendar.MINUTE, 0);
//		cal.set(Calendar.SECOND, 0);
//		cal.set(Calendar.MILLISECOND, 0);
//		return cal.getTime();
	}

	public static Date getEndDateTimeOfMonth(Date date) {
		date=trim(date, Calendar.DAY_OF_MONTH);
		date=addMonths(date, 1);
		date=addSeconds(date, -1);
		return date;
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
//		cal.set(Calendar.HOUR_OF_DAY, 23);
//		cal.set(Calendar.MINUTE, 59);
//		cal.set(Calendar.SECOND, 59);
//		return cal.getTime();
	}

	/**
	 * 返回日期的当年第一天
	 * 
	 * @param date
	 * @return
	 * @since ChenDan 2009-2-5
	 */
	public static Date getFirstDayOfYear(Date date) {
//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime(date);
//		calendar.set(dayOfYear(date), 0, 1, 0, 0, 0);
//		return calendar.getTime();
		return trim(date, Calendar.MONTH);
	}
	
	/**
	 * 返回日期的当年最后一天 最后一秒
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfYear(Date date){
	//	Calendar calendar = Calendar.getInstance();
	//	calendar.setTime(date);
	//	calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
	//	calendar.set(Calendar.HOUR_OF_DAY, 23);
	//	calendar.set(Calendar.MINUTE, 59);
	//	calendar.set(Calendar.SECOND, 59);
	//	return calendar.getTime();
		date=trim(date, Calendar.MONTH);
		date=addYears(date, 1);
		date=addSeconds(date, -1);
		return date;
	}
/**
 * 获取当前时间所处小时的第一秒
 * @param date
 * @return
 */
public static Date getFirstOfHour(Date date){
//	Calendar calendar = Calendar.getInstance();
//	calendar.setTime(date);
//	calendar.set(Calendar.MINUTE, 0);
//	calendar.set(Calendar.SECOND, 0);
//	return calendar.getTime();
	return trim(date, Calendar.MINUTE);
}
/**
 * 获取当前时间所处小时的最后一秒
 * @param date
 * @return
 */
public static Date getLastOfHour(Date date){
//	Calendar calendar = Calendar.getInstance();
//	calendar.setTime(date);
//	calendar.set(Calendar.MINUTE, 59);
//	calendar.set(Calendar.SECOND, 59);
//	return calendar.getTime();
	date=trim(date, Calendar.MINUTE);
	date=addHours(date, 1);
	date=addSeconds(date, -1);
	return date;
}
/**
 * 获取当前时间所在分钟的第一秒
 * @param date
 * @return
 */
public static Date getFirstOfMin(Date date){
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.set(Calendar.SECOND, 0);
	return calendar.getTime();
}
/**
 * 获取当前时间所在分钟的最后一秒
 * @param date
 * @return
 */
public static Date getLastOfMin(Date date){
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.set(Calendar.SECOND, 59);
	return calendar.getTime();
}
/**
 * 获取指定时间所在秒的第一毫秒
 * @param date
 * @return
 */
public static Date getFirstOfSecond(Date date){
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.set(Calendar.MILLISECOND, 0);
	return calendar.getTime();
}

/**
 * 获取指定时间所在秒的最后一毫秒
 * @param date
 * @return
 */
public static Date getLastOfSecond(Date date){
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(date);
	calendar.set(Calendar.MILLISECOND, 999);
	return calendar.getTime();
}

	/**
	 * 返回今年的第一天
	 * 
	 * @return
	 * @since ChenDan 2009-2-5
	 */
	public static Date getFirstDayOfYear() {
		return getFirstDayOfYear(currentDate());
	}

	/**
	 * 返回year对应年份的第一天
	 * 
	 * @param year
	 * @return
	 * @since Zengjun 2009-8-18
	 */
	public static Date getFirstDayOfYear(int year) {
		return getFirstDayOfYear(DateUtil.parse(year + "0101", "yyyyMMdd"));
	}

	/**
	 * 返回year对应年份的生日
	 * 
	 * @param birthday
	 * @return
	 * @since Zengjun 2009-8-18
	 */
	public static Date getCurrentBirthday(int year, Date birthday) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getFirstDayOfYear(year));
		calendar.set(Calendar.MONTH, dayOfMonth(birthday) - 1);
		calendar.set(Calendar.DAY_OF_MONTH, dayOfDate(birthday));
		return calendar.getTime();
	}

	/**
	 * 功能描述 <li>获取年费</li>
	 * 
	 * @since 黄显宏 2009-2-14
	 */
	public static int getYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.YEAR);
	}

	/**
	 * 返回两个日期之间的月数<br>
	 * date1和date2无前后之分,计算精确到日
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 * @since Chendan 2009-8-17
	 */
	public static int getMonths(Date date1, Date date2) {
		int iMonth = 0;
		int flag = 0;
		try {
			Calendar objCalendarDate1 = Calendar.getInstance();
			objCalendarDate1.setTime(date1);

			Calendar objCalendarDate2 = Calendar.getInstance();
			objCalendarDate2.setTime(date2);

			if (objCalendarDate2.equals(objCalendarDate1))
				return 0;
			if (objCalendarDate1.after(objCalendarDate2)) {
				Calendar temp = objCalendarDate1;
				objCalendarDate1 = objCalendarDate2;
				objCalendarDate2 = temp;
			}
			if (objCalendarDate2.get(Calendar.DAY_OF_MONTH) < objCalendarDate1
					.get(Calendar.DAY_OF_MONTH))
				flag = 1;

			if (objCalendarDate2.get(Calendar.YEAR) > objCalendarDate1.get(Calendar.YEAR))
				iMonth = ((objCalendarDate2.get(Calendar.YEAR) - objCalendarDate1
						.get(Calendar.YEAR))
						* 12 + objCalendarDate2.get(Calendar.MONTH) - flag)
						- objCalendarDate1.get(Calendar.MONTH);
			else
				iMonth = objCalendarDate2.get(Calendar.MONTH)
						- objCalendarDate1.get(Calendar.MONTH) - flag;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return iMonth;
	}
	
	/**
	 * @功能描述  ： 将日期转化成毫秒返回
	 * @输入参数  ：日期
	 * @返回值     ：日期毫秒数
	 * @version ： 1.0
	 * @author  ： 曾俊
	 * @更新时间  ： 2011-7-19 下午01:56:01
	 */
	public static long getTimeInMillis(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis();
	}
	
	/**
	 * @功能描述  ：将毫秒数转化成日期返回 
	 * @输入参数  ：毫秒数
	 * @返   回  值     ：日期
	 * @version ： 1.0
	 * @author  ： 曾俊
	 * @更新时间  ： 2011-7-19 下午01:56:32
	 */
	public static Date getTime(long dateInMillis){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dateInMillis);
		return calendar.getTime();
	}
	/**
	 * @功能： 计算星座
	 * @author 谭斌 
	 * @param birthday
	 * @return
	 */
	public static String getStar(Date birthday) {
		if (birthday == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(birthday);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String star = "";
		if (month == 1 && day >= 20 || month == 2 && day <= 18) {
			star = "水瓶座";
		} else if (month == 2 && day >= 19 || month == 3 && day <= 20) {
			star = "双鱼座";
		} else if (month == 3 && day >= 21 || month == 4 && day <= 19) {
			star = "白羊座";
		} else if (month == 4 && day >= 20 || month == 5 && day <= 20) {
			star = "金牛座";
		} else if (month == 5 && day >= 21 || month == 6 && day <= 21) {
			star = "双子座";
		} else if (month == 6 && day >= 22 || month == 7 && day <= 22) {
			star = "巨蟹座";
		} else if (month == 7 && day >= 23 || month == 8 && day <= 22) {
			star = "狮子座";
		} else if (month == 8 && day >= 23 || month == 9 && day <= 22) {
			star = "处女座";
		} else if (month == 9 && day >= 23 || month == 10 && day <= 22) {
			star = "天秤座";
		} else if (month == 10 && day >= 23 || month == 11 && day <= 21) {
			star = "天蝎座";
		} else if (month == 11 && day >= 22 || month == 12 && day <= 21) {
			star = "射手座";
		} else if (month == 12 && day >= 22 || month == 1 && day <= 19) {
			star = "摩羯座";
		}
		return star;
	}
	/**
	 * @功能： 计算年龄
	 * @author 谭斌 
	 * @param birthday
	 * @return
	 */
	public static Integer getAge(Date birthday){
		if (birthday == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		long t = calendar.getTimeInMillis();
		int cyear = calendar.get(Calendar.YEAR);
		int cmonth = calendar.get(Calendar.MONTH);
		int cday = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.setTime(birthday);
		if (t < calendar.getTimeInMillis()) {
			throw new KKDException("不能大于当前时间");
		}
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int age = cyear - year;
		if (cmonth > month && cday > day) {
			age = age - 1;
		}
		return age;
	}
	/**
	 * 计算生日到指定日期时的年龄，计算到月
	 * 
	 * @param birthday
	 *            生日
	 * @param date
	 *            指定日期
	 * @return 年龄, -1：年龄比指定日期大
	 * @since zengqiao 2008-12-29
	 */
	public static int getAge(Date birthday, Date date) {
		if (birthday.after(date)) {
			return -1;
		}
		int age = DateUtil.dayOfYear(date) - DateUtil.dayOfYear(birthday);
		if (DateUtil.dayOfMonth(date) < DateUtil.dayOfMonth(birthday)) {
			age--;
		}

		return age;
	}

	/**
	 * 计算年龄，只计算到年
	 * 
	 * @param birthday
	 * @param date
	 * @return
	 * @since Chendan 2009-9-15
	 */
	public static int getAgeByYear(Date birthday, Date date) {
		if (birthday.after(date)) {
			return -1;
		}
		int age = DateUtil.dayOfYear(date) - DateUtil.dayOfYear(birthday);
		return age;
	}

	/**
	 * 计算生日到指定日期时的精确年龄
	 * 
	 * @param birthday
	 *            生日
	 * @param date
	 *            指定日期
	 * @return 年龄, -1：年龄比指定日期大
	 * @since zengqiao 2008-12-29
	 */
	public static int getAgePrecise(Date birthday, Date date) {
		if (birthday.after(date)) {
			return -1;
		}
		int age = DateUtil.dayOfYear(date) - DateUtil.dayOfYear(birthday);
		if (DateUtil.dayOfMonth(date) < DateUtil.dayOfMonth(birthday)) {
			age--;
		} else if (DateUtil.dayOfMonth(date) == DateUtil.dayOfMonth(birthday)
				&& DateUtil.dayOfDate(date) < DateUtil.dayOfDate(birthday)) {
			age--;
		}

		return age;
	}
	/**
	 * 王川远
	 * 获取日期前或后指定天数的日期
	 * @param desDate
	 * @param days
	 * @return
	 * @see addDays()
	 */
	@Deprecated
	public static Date getDesDate(Date desDate,int days){
		if(desDate==null ){
			desDate = new Date();
		}
		Calendar cal =Calendar.getInstance();
		cal.setTime(desDate);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}
	
	//给一个日期,计算出此日期当天00:00:00的时间戳(王川远)
	public static Timestamp getZeroHour(Date date){
		//			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		//			String date_str=sdf.format(date);
		//			try {
		//				date=sdf.parse(date_str);
		//			} catch (ParseException e) {
		//			
		//				e.printStackTrace();
		//			}
		//			Timestamp ts=new Timestamp(date.getTime());
		//			return ts;
		date=trim(date, Calendar.HOUR_OF_DAY);
		return new Timestamp(date.getTime());
	}

	//给一个日期,计算出此日期当天23:59:59的时间戳(王川远)
	public static Timestamp getLatestHour(Date date){
//		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//		String date_str=sdf.format(date);
//		try {
//			date=sdf.parse(date_str);
//		} catch (ParseException e) {
//
//			e.printStackTrace();
//		}
//		Calendar cal=Calendar.getInstance();
//		cal.setTime(date);
//		cal.add(Calendar.HOUR, 23);
//		cal.add(Calendar.MINUTE,59);
//		cal.add(Calendar.SECOND, 59);
//		date =cal.getTime();
//		Timestamp ts=new Timestamp(date.getTime());
//		return ts;
		date=trim(date, Calendar.HOUR_OF_DAY);
		date=addDays(date, 1);
		date=addSeconds(date, -1);
		return new Timestamp(date.getTime());
	}
	
	
	public static void main(String[] args) {
//		Calendar c=Calendar.getInstance();
//		c.set(1987, 21, 11);
//		Calendar c2=Calendar.getInstance();
//		c2.set(1992, 10, 10);
//		int a=DateUtil.daysBetween(c2.getTime(), c.getTime());
//		System.out.println(a%365);
//		Date d=DateUtil.parse("2013-07-26 11:31:00",DATETIME_PATTERN);
//		Boolean b=DateUtil.isBetweenInMins(d, 8);
////		System.out.println(b);
//		Date date = new Date();
////		Date date1 = getDesDate(new Date(),3);
////		System.out.println(DateUtil.format(date,DateUtil.DATETIME_PATTERN));
////		System.out.println(DateUtil.format(date1,DateUtil.DATETIME_PATTERN));
//		System.out.println(getFirstOfHour(date));
//		System.out.println(getLastOfHour(date));
//		System.out.println(getFirstOfMin(date));
//		System.out.println(getLastOfMin(date));
//		System.out.println(getFirstOfSecond(date));
//		System.out.println(getLastOfSecond(date));
//		
//		int s=dayOfDate(new Date());
//		System.out.println(s);
		
		//Date d=DateUtil.parse("2014-12-31 10:38:01.123","yyyy-MM-dd HH:mm:ss.SSS");
//		Date d1=DateUtil.parse("2014-12-01 23:59:0",DateUtil.DATETIME_PATTERN);
//		boolean a=DateUtil.isBetweenInMins(d, 1);
//		int a=dayOfNextYearMonth(d);
//		d=getLatestHour(new Date());
//		String s=DateUtil.format(d, DateUtil.DATETIME_PATTERN);
//		
		//System.out.println(s);
		
		
		System.out.println(beforeDay(new Date()));
	}
}
