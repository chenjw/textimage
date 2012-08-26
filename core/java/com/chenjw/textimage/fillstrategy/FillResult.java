package com.chenjw.textimage.fillstrategy;

import java.util.List;

import com.chenjw.textimage.service.model.TextMetaInfo;

public class FillResult {
	// 画布尺寸信息
	private List<CanvasInfo> canvasInfoList;
	// 文本信息
	private TextMetaInfo textInfo;

	public List<CanvasInfo> getCanvasInfoList() {
		return canvasInfoList;
	}

	public void setCanvasInfoList(List<CanvasInfo> canvasInfoList) {
		this.canvasInfoList = canvasInfoList;
	}

	public TextMetaInfo getTextInfo() {
		return textInfo;
	}

	public void setTextInfo(TextMetaInfo textInfo) {
		this.textInfo = textInfo;
	}

}
