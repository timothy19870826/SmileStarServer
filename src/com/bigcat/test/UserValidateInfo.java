package com.bigcat.test;

public class UserValidateInfo {

	public static String getEmailCodeField() {
		return "emailCode";
	}
	public static String getLastOpTimeField() {
		return "lastOpTime";
	}
	public static String getCreateimeField() {
		return "createTime";
	}
	
	private String emailCode;
	private String lastOpTime;
	private String createTime;
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getEmailCode() {
		return emailCode;
	}
	public void setEmailCode(String emailCode) {
		this.emailCode = emailCode;
	}
	public String getLastOpTime() {
		return lastOpTime;
	}
	public void setLastOpTime(String opTime) {
		this.lastOpTime = opTime;
	}
	
}
