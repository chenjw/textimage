/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.textimage.model;

import java.awt.Color;
import java.awt.Font;

import com.chenjw.textimage.utils.logger.Logger;
import com.chenjw.textimage.utils.logger.LoggerFactory;

/**
 * 文本样式
 * 
 * @author chenjw
 */
public class TextStyle {

	// 默认样式的字体名称
	private static final String DEFAULT_FONT_NAME = "宋体";

	private static Logger LOGGER = LoggerFactory.getLogger("textImageLogger");

	// 字体名
	private String fontName;
	// 字体尺寸
	private int fontSize;

	// 是否粗体
	private boolean isBold;
	// 是否斜体
	private boolean isItalic;

	// 是否抗锯齿模式
	private boolean isAntialias;
	// 行高
	private int lineHeight;
	// 行宽
	private int lineWidth;
	// 颜色
	private Color color;
	// 背景颜色
	private Color backgroundColor;

	/**
	 * 获得字体
	 * 
	 * @param style
	 * @return
	 */
	public static Font getFont(TextStyle style) {

		int fontWeight;
		if (style.getIsBold()) {
			if (style.getIsItalic()) {
				fontWeight = Font.BOLD | Font.ITALIC;
			} else {
				fontWeight = Font.BOLD;
			}
		} else {
			if (style.getIsItalic()) {
				fontWeight = Font.ITALIC;
			} else {
				fontWeight = Font.PLAIN;
			}
		}
		Font font = new Font(style.getFontName(), fontWeight,
				style.getFontSize());
		return font;

	}

	public static TextStyle defaultStyle() {
		TextStyle style = new TextStyle();
		style.color = Color.black;
		style.backgroundColor = Color.white;
		style.fontName = DEFAULT_FONT_NAME;
		style.lineHeight = 22;
		style.fontSize = 14;
		style.isBold = false;
		style.isItalic = false;
		style.isAntialias = false;
		style.lineWidth = 300;

		return style;
	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public boolean getIsBold() {
		return isBold;
	}

	public void setIsBold(boolean isBold) {
		this.isBold = isBold;
	}

	public boolean getIsItalic() {
		return isItalic;
	}

	public void setIsItalic(boolean isItalic) {
		this.isItalic = isItalic;
	}

	public int getLineHeight() {
		return lineHeight;
	}

	public void setLineHeight(int lineHeight) {
		this.lineHeight = lineHeight;
	}

	public int getLineWidth() {
		return lineWidth;
	}

	public void setLineWidth(int lineWidth) {
		this.lineWidth = lineWidth;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isAntialias() {
		return isAntialias;
	}

	public void setAntialias(boolean isAntialias) {
		this.isAntialias = isAntialias;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

}
