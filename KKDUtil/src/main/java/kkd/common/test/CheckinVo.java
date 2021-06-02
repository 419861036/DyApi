package kkd.common.test;

import java.util.Date;

/**
 * 签到实体
 * @author tanbins
 *
 */
public class CheckinVo {
	private Integer id;
	private String Uid;
	private String Lanid;
	private Date checkinTime;
	private String checkinTimeStr;
	private Integer state;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUid() {
		return Uid;
	}
	public void setUid(String uid) {
		Uid = uid;
	}
	public String getLanid() {
		return Lanid;
	}
	public void setLanid(String lanid) {
		Lanid = lanid;
	}
	public Date getCheckinTime() {
		return checkinTime;
	}
	public void setCheckinTime(Date checkinTime) {
		this.checkinTime = checkinTime;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getCheckinTimeStr() {
		return checkinTimeStr;
	}
	public void setCheckinTimeStr(String checkinTimeStr) {
		this.checkinTimeStr = checkinTimeStr;
	}
	
	
}
