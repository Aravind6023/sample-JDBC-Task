package com.example.demo.model;

import java.io.Serializable;

public class Response implements Serializable {

	private static final long serialVersionUID = 1L;
	private String responseMessage;
	private int responseCode;
	private String data;
	private Object jData;
	
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public Object getjData() {
		return jData;
	}
	public void setjData(Object jData) {
		this.jData = jData;
	}
	
	

}
