package com.chenjw.textimage.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.chenjw.textimage.model.StyleConfig;
import com.chenjw.textimage.model.TextCanvas;
import com.chenjw.textimage.model.TextInfo;

public class TextImageContext {
	// 输入文本
	Map<String, String> textMap;
	// 文本样式
	StyleConfig styleConfig;
	// 文本元数据
	TextInfo textInfo;
	// 输出画布
	List<TextCanvas> textCanvasList;

	public TextImageContext(Map<String, String> textMap, StyleConfig styleConfig) {

		this.textMap = textMap;
		this.styleConfig = styleConfig;
		this.textInfo = new TextInfo();
		this.textCanvasList = new ArrayList<TextCanvas>();
	}

	public Map<String, String> getTextMap() {
		return textMap;
	}

	public void setTextMap(Map<String, String> textMap) {
		this.textMap = textMap;
	}

	public StyleConfig getStyleConfig() {
		return styleConfig;
	}

	public void setStyleConfig(StyleConfig styleConfig) {
		this.styleConfig = styleConfig;
	}

	public TextInfo getTextInfo() {
		return textInfo;
	}

	public void setTextInfo(TextInfo textInfo) {
		this.textInfo = textInfo;
	}

	public List<TextCanvas> getTextCanvasList() {
		return textCanvasList;
	}

	public void setTextCanvasList(List<TextCanvas> textCanvasList) {
		this.textCanvasList = textCanvasList;
	}

}
