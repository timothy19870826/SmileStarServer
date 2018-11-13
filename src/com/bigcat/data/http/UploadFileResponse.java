package com.bigcat.data.http;

import java.util.ArrayList;

public class UploadFileResponse {
	
	private ArrayList<String> srcList = new ArrayList<>();
	private ArrayList<String> saveList = new ArrayList<>();

	public UploadFileResponse() {
		// TODO Auto-generated constructor stub
	}

	public ArrayList<String> getSrcList() {
		return srcList;
	}

	public void setSrcList(ArrayList<String> srcList) {
		this.srcList = srcList;
	}

	public ArrayList<String> getSaveList() {
		return saveList;
	}

	public void setSaveList(ArrayList<String> saveList) {
		this.saveList = saveList;
	}

}
