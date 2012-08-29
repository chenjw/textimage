package com.chenjw.textimage.service.model;

import java.util.List;
import java.util.Map;

import com.chenjw.textimage.service.config.StyleConfig;
import com.chenjw.textimage.service.fillstrategy.result.CanvasInfo;
import com.chenjw.textimage.service.spi.TextCanvas;

/**
 * 文字处理过程中的上下文
 * 
 * @author chenjw
 * 
 */
public class TextImageContext {
	// 输入文本
	private Map<String, String> textMap;
	// 文本样式
	private StyleConfig styleConfig;
	// 文本元数据
	private TextMetaInfo textInfo;
	// 输出画布的尺寸
	private List<CanvasInfo> canvasInfoList;
	// 输出画布
	private List<TextCanvas> textCanvasList;
	// 生成的图片数据
	private List<byte[]> imageList;

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

	public TextMetaInfo getTextInfo() {
		return textInfo;
	}

	public void setTextInfo(TextMetaInfo textInfo) {
		this.textInfo = textInfo;
	}

	public List<TextCanvas> getTextCanvasList() {
		return textCanvasList;
	}

	public void setTextCanvasList(List<TextCanvas> textCanvasList) {
		this.textCanvasList = textCanvasList;
	}

	public List<CanvasInfo> getCanvasInfoList() {
		return canvasInfoList;
	}

	public void setCanvasInfoList(List<CanvasInfo> canvasInfoList) {
		this.canvasInfoList = canvasInfoList;
	}

	public List<byte[]> getImageList() {
		return imageList;
	}

	public void setImageList(List<byte[]> imageList) {
		this.imageList = imageList;
	}

}
