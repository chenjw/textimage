package com.chenjw.textimage.service.model;

import java.util.List;

/**
 * 包括文字图片的图像字节数组和对应的元数据
 * 
 * @author chenjw
 * 
 */
public class TextImageInfo {
	// 元数据
	private TextMetaInfo textMetaInfo;
	// 图像列表
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
