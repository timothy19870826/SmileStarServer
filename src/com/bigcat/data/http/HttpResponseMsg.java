package com.bigcat.data.http;


public class HttpResponseMsg {
	private int code;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	private String message;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	private Object data;
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	public HttpResponseMsg() {
		this.code = 0;
		this.message = "";
		this.data = null;
	}
}
