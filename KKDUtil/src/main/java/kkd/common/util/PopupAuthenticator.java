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

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class PopupAuthenticator extends Authenticator {
	private String uname;// 用户名
	private String password;// 密码

	// 属性方法
	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// 构造方法
	public PopupAuthenticator() {

	}

	public PopupAuthenticator(String uname, String password) {
		this.uname = uname;
		this.password = password;
	}

	public PasswordAuthentication getPasswordAuthentication() {
		String username = uname; // 邮箱登录帐号
		String pwd = password; // 登录密码
		return new PasswordAuthentication(username, pwd);
	}
}