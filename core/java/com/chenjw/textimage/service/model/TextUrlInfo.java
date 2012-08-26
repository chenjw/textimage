package com.chenjw.textimage.service.model;

import java.util.ArrayList;
import java.util.List;

public class TextUrlInfo {
	private TextMetaInfo textMetaInfo;
	private List<String> urls = new ArrayList<String>();

	public TextMetaInfo getTextMetaInfo() {
		return textMetaInfo;
	}

	public void setTextMetaInfo(TextMetaInfo textMetaInfo) {
		this.textMetaInfo = textMetaInfo;
	}

	public List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
	}

}
