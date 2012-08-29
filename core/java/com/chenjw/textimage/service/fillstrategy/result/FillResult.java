package com.chenjw.textimage.service.fillstrategy.result;

import java.util.List;

import com.chenjw.textimage.service.model.TextMetaInfo;

/**
 * 填充结果
 * 
 * @author chenjw
 * 
 */
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
