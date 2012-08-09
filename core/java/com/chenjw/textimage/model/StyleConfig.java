/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.textimage.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 文字图片的样式描述
 * 
 * @author chenjw 2011-11-14 下午4:59:33
 */
public class StyleConfig {

	private static String DEFAULT_STYLE_VERSION = "0.1";
	// 画布最大宽度
	private int maxCanvasWidth = 800;
	// 画布最大高度
	private int maxCanvasHeight = 600;
	// 样式版本
	private String styleVersion = DEFAULT_STYLE_VERSION;
	// 默认样式,当没有找到某文本key的样式时会指到默认样式
	private TextStyle defaultStyle = TextStyle.defaultStyle();
	// 样式名称到样式类的映射
	private Map<String, TextStyle> textStyleMap = new HashMap<String, TextStyle>();
	// 文本key到样式名称的映射
	private Map<String, String> textStyleMappingMap = new HashMap<String, String>();

	/**
	 * 查找某个Key对应文本的样式
	 * 
	 * @param key
	 * @return
	 */
	public TextStyle getTextStyle(String key) {
		String className = textStyleMappingMap.get(key);
		if (!StringUtils.isBlank(className)) {
			TextStyle style = textStyleMap.get(className);
			if (style != null) {
				return style;
			}
		}
		return defaultStyle;
	}

	/**
	 * 获得样式列表
	 * 
	 * @return
	 */
	public Collection<TextStyle> getAllTextStyleList() {
		return textStyleMap.values();
	}

	public void addTextStyle(String className, TextStyle style) {
		textStyleMap.put(className, style);
	}

	public void addTextStyleMapping(String key, String className) {
		textStyleMappingMap.put(key, className);
	}

	public void setDefaultTextStyle(TextStyle style) {
		this.defaultStyle = style;
	}

	public int getMaxCanvasWidth() {
		return maxCanvasWidth;
	}

	public void setMaxCanvasWidth(int maxCanvasWidth) {
		this.maxCanvasWidth = maxCanvasWidth;
	}

	public int getMaxCanvasHeight() {
		return maxCanvasHeight;
	}

	public void setMaxCanvasHeight(int maxCanvasHeight) {
		this.maxCanvasHeight = maxCanvasHeight;
	}

	public String getStyleVersion() {
		return styleVersion;
	}

	public void setStyleVersion(String styleVersion) {
		this.styleVersion = styleVersion;
	}

}
