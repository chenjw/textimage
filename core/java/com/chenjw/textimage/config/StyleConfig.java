/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.textimage.config;

import java.util.HashMap;
import java.util.Map;

/**
 * 文字图片的样式描述
 * 
 * @author chenjw 2011-11-14 下午4:59:33
 */
public class StyleConfig {
	// 默认版本号
	private static String DEFAULT_STYLE_VERSION = "0.1";
	// 画布最大宽度
	private int maxCanvasWidth = 800;
	// 画布最大高度
	private int maxCanvasHeight = 600;
	// 样式版本，框架会根据版本号来判断样式是否变化，如果样式有变化需要重新生成图片，请修改样式版本的值
	private String styleVersion = DEFAULT_STYLE_VERSION;
	// 默认样式,当没有找到某文本key的样式时会指到默认样式
	private TextStyle defaultStyle = new TextStyle();
	// 样式名称到样式类的映射
	private Map<String, TextStyle> classMap = new HashMap<String, TextStyle>();
	// 文本key到样式名称的映射
	private Map<String, String> classMappingMap = new HashMap<String, String>();
	// 样式名称到样式类的映射
	private Map<String, TextPosition> positionMap = new HashMap<String, TextPosition>();

	public void addClass(String className, TextStyle style) {
		classMap.put(className, style);
	}

	public void addClassMapping(String key, String className) {
		classMappingMap.put(key, className);
	}

	public void addTextPosition(String key, TextPosition position) {
		positionMap.put(key, position);
	}

	/*******************
	 * get/set方法
	 *******************/
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

	public TextStyle getDefaultStyle() {
		return defaultStyle;
	}

	public void setDefaultStyle(TextStyle defaultStyle) {
		this.defaultStyle = defaultStyle;
	}

	public Map<String, TextStyle> getClassMap() {
		return classMap;
	}

	public void setClassMap(Map<String, TextStyle> classMap) {
		this.classMap = classMap;
	}

	public Map<String, String> getClassMappingMap() {
		return classMappingMap;
	}

	public void setClassMappingMap(Map<String, String> classMappingMap) {
		this.classMappingMap = classMappingMap;
	}

	public Map<String, TextPosition> getPositionMap() {
		return positionMap;
	}

	public void setPositionMap(Map<String, TextPosition> positionMap) {
		this.positionMap = positionMap;
	}

}
