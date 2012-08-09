package com.chenjw.textimage.model;

import java.util.List;

public class TextImage {
	private TextInfo textInfo;

	private List<byte[]> imageList;

	public TextInfo getTextInfo() {
		return textInfo;
	}

	public void setTextInfo(TextInfo textInfo) {
		this.textInfo = textInfo;
	}

	public List<byte[]> getImageList() {
		return imageList;
	}

	public void setImageList(List<byte[]> imageList) {
		this.imageList = imageList;
	}

}
