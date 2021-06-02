package kkd.common.dao.dbuitl.test;

import java.util.Date;

import kkd.common.orm.Id_;
import kkd.common.orm.Seq_;
import kkd.common.orm.Table_;

/**
 * 用户领取奖品的发送记录
 * @author tanbins
 *
 */
@Table_("Act_TFD_SendGiftHis")
public class SendGift {
	@Id_
	@Seq_("")
	private Integer	id;
	private Integer	userId;//登录的用户ID
	private String	lanId;//登录的宽带账号
	private String	phoneNum;//登录的电话号码
	private Integer	sendNum;
	private Integer	giftId;//礼品Id
	private String giftCode;//礼品code
	private String	cardNum;//卡号
	private String	cardPwd;//卡密
	private String	area;
	private String	revTrueName;//收货的人名
	private String	revAddress;//收货的地址
	private String	revPhoneNum;//收货的手机号码
	private Integer	state;
	private Date	sendTime;
	private SendGift1 send;
	
	
	
	public SendGift1 getSend() {
		return send;
	}
	public void setSend(SendGift1 send) {
		this.send = send;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getGiftCode() {
		return giftCode;
	}
	public void setGiftCode(String giftCode) {
		this.giftCode = giftCode;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getLanId() {
		return lanId;
	}
	public void setLanId(String lanId) {
		this.lanId = lanId;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public Integer getSendNum() {
		return sendNum;
	}
	public void setSendNum(Integer sendNum) {
		this.sendNum = sendNum;
	}
	public Integer getGiftId() {
		return giftId;
	}
	public void setGiftId(Integer giftId) {
		this.giftId = giftId;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getCardPwd() {
		return cardPwd;
	}
	public void setCardPwd(String cardPwd) {
		this.cardPwd = cardPwd;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getRevTrueName() {
		return revTrueName;
	}
	public void setRevTrueName(String revTrueName) {
		this.revTrueName = revTrueName;
	}
	public String getRevAddress() {
		return revAddress;
	}
	public void setRevAddress(String revAddress) {
		this.revAddress = revAddress;
	}
	public String getRevPhoneNum() {
		return revPhoneNum;
	}
	public void setRevPhoneNum(String revPhoneNum) {
		this.revPhoneNum = revPhoneNum;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	
	
}
