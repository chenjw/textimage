package com.chenjw.textimage.service.model;

import java.util.List;

public class TextImageInfo {
	private TextMetaInfo textMetaInfo;

	private List<byte[]> imageList;

	public TextMetaInfo getTextMetaInfo() {
		return textMetaInfo;
	}

	public void setTextMetaInfo(TextMetaInfo textMetaInfo) {
		this.textMetaInfo = textMetaInfo;
	}

	public List<byte[]> getImageList() {
		return imageList;
	}

	public void setImageList(List<byte[]> imageList) {
		this.imageList = imageList;
	}

}
