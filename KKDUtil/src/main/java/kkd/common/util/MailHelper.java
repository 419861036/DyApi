/*************************************************************************
 * Copyright (c) 2010-2012 成都网丁科技有限公司（重庆分公司）
 * 功能描述：
 * 创建　人：Caojin
 * 日　　期：2012-11-28
 * 版　　本：
 * 修改　人：
 * 修改日期：
 * 修改描述：
 * ************************************************************************/

package kkd.common.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailHelper {

	/**
	 * 不需要验证的邮件发送 String fromMail 发件人地址 String password 发件人密码 String toMail
	 * 收件人地址 String messageText 发送的消息 String title 发送的标题 String serviceName
	 * 使用的邮件服务器
	 * 
	 * @throws Exception
	 */
	public static void setMessage(String fromMail, String toMail, String messageText, String serviceName) throws Exception {
		Properties props = System.getProperties();
		props.put("mail.smtp.host", serviceName); // 设置smtp的服务器地址：该邮件服务器不需要身份验证
		props.put("mail.smtp.auth", "false"); // 设置smtp服务器要身份验证：缺省设置为false

		Address from = new InternetAddress(fromMail);
		Address to = new InternetAddress(toMail);

		Session session = Session.getDefaultInstance(props, null);
		Message message = new MimeMessage(session);
		message.setFrom(from);
		message.addRecipient(Message.RecipientType.TO, to);
		message.setText(messageText);

		Transport.send(message);
	}

	/**
	 * 带授权的发送邮件 String fromMail 发件人地址 String password 发件人密码 String toMail 收件人地址
	 * String messageText 发送的消息 String title 发送的标题 String serviceName 使用的邮件服务器
	 * 
	 * @throws Exception
	 */
	public static void setMessageWithAuthentica(String fromMail, String password, String toMail, String messageText,
			String title, String serviceName) throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.host", serviceName); // 设置smtp的服务器地址是smtp.sohu.com
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true"); // 设置smtp服务器要身份验证。

		String formMailName = fromMail.split("@")[0];
		String toMailName = toMail.split("@")[0];

		PopupAuthenticator auth = new PopupAuthenticator(formMailName, fromMail);
		Session session = Session.getDefaultInstance(props, auth);

		session.setDebug(true);

		// 发送人地址
		Transport transport = null;
		try {
			Address addressFrom = new InternetAddress(fromMail, formMailName);
			// 收件人地址
			Address[] addressTo = new Address[] { new InternetAddress(toMail, toMailName) };

			// 抄送地址
			// Address addressCopy = new InternetAddress("xxx@gmail.com",
			// "xxx密码");
			Message message = new MimeMessage(session);
			message.setText(messageText);
			message.setSubject(title);
			message.setFrom(addressFrom);
			message.addRecipients(Message.RecipientType.TO, addressTo);
			// message.addRecipient(Message.RecipientType.CC,addressCopy);
			message.saveChanges();
			// session.setDebug(true);
			transport = session.getTransport("smtp"); // 创建连接
			transport.connect(serviceName, fromMail, password);// 连接服务器
			transport.sendMessage(message, addressTo); // 发送信息
			transport.close(); // 关闭连接
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			// Loggers.error("发送邮件错误：SendMail--setMessageWithAuthentica--UnsupportedEncodingException："
			// + e);
		} catch (MessagingException e) {
			e.printStackTrace();
			// Loggers.error("发送邮件错误：SendMail--setMessageWithAuthentica--MessagingException"
			// + e);
		} finally {
			if (transport != null & transport.isConnected()) {
				transport.close();
			}
		}
	}

	public static void setMessageWithDefaultAuthentica(String toMail, String mailContent, String mailTitle) throws Exception {
		Properties props = new Properties();
		String fromMail = "admin@dingdingbao.com";
		String password = "562614";
		String serviceName = "221.5.251.53";

		props.put("mail.smtp.host", serviceName); // 设置smtp的服务器地址是smtp.sohu.com
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true"); // 设置smtp服务器要身份验证。

		String formMailName = fromMail.split("@")[0];
		String toMailName = toMail.split("@")[0];

		PopupAuthenticator auth = new PopupAuthenticator(formMailName, fromMail);
		Session session = Session.getDefaultInstance(props, auth);

		session.setDebug(true);

		// 发送人地址
		Transport transport = null;
		try {
			Address addressFrom = new InternetAddress(fromMail, formMailName);
			// 收件人地址
			Address[] addressTo = new Address[] { new InternetAddress(toMail, toMailName) };

			// 抄送地址
			// Address addressCopy = new InternetAddress("xxx@gmail.com",
			// "xxx密码");
			Message message = new MimeMessage(session);
			message.setText(mailContent);
			message.setSubject(mailTitle);
			message.setFrom(addressFrom);
			message.addRecipients(Message.RecipientType.TO, addressTo);
			// message.addRecipient(Message.RecipientType.CC,addressCopy);
			message.saveChanges();
			// session.setDebug(true);
			transport = session.getTransport("smtp"); // 创建连接
			transport.connect(serviceName, fromMail, password);// 连接服务器
			transport.sendMessage(message, addressTo); // 发送信息
			transport.close(); // 关闭连接
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			// Loggers.error("发送邮件错误：SendMail--setMessageWithAuthentica--UnsupportedEncodingException："
			// + e);
		} catch (MessagingException e) {
			e.printStackTrace();
			// Loggers.error("发送邮件错误：SendMail--setMessageWithAuthentica--MessagingException"
			// + e);
		} finally {
			if (transport != null & transport.isConnected()) {
				transport.close();
			}
		}
	}

	public static void setMsg(String toMail, String title, String content) throws MessagingException {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.163.com");
		props.put("mail.smtp.auth", "true");
		Session s = Session.getInstance(props);
		s.setDebug(true);

		MimeMessage message = new MimeMessage(s);
		Transport transport = null;
		try {
			InternetAddress from = new InternetAddress("xxx@163.com");
			message.setFrom(from);
			InternetAddress to = new InternetAddress(toMail);
			message.setRecipient(Message.RecipientType.TO, to);
			message.setSubject(title);
			message.setText(content);
			message.setSentDate(new Date());
			message.saveChanges();
			transport = s.getTransport("smtp");
			transport.connect("smtp.163.com", "xxx", "xxx");
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (AddressException e) {
			e.printStackTrace();
			// Loggers.error("发送邮件错误：SendMail--setMsg--AddressException：" + e);
		} catch (MessagingException e) {
			e.printStackTrace();
			// Loggers.error("发送邮件错误：SendMail--setMsg--MessagingException：" +
			// e);
		} finally {
			if (transport != null & transport.isConnected()) {
				transport.close();
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			MailHelper.setMessageWithDefaultAuthentica("253384138@qq.com", "1", "test");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}