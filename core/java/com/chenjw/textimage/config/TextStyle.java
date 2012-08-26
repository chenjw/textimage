/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.textimage.config;

import java.awt.Color;

import com.chenjw.textimage.config.constants.HAlignEnum;
import com.chenjw.textimage.config.constants.VAlignEnum;

/**
 * 文本样式
 * 
 * @author chenjw
 */
public class TextStyle {

	// 默认样式的字体名称
	private static final String DEFAULT_FONT_NAME = "宋体";

	public static final int LINE_SIZE_UNLIMIT = -1;

	// 字体名
	private String fontName = DEFAULT_FONT_NAME;
	// 字体尺寸
	private int fontSize = 14;

	// 是否粗体
	private boolean isBold = false;
	// 是否斜体
	private boolean isItalic = false;

	// 是否抗锯齿模式
	private boolean isAntialias = false;
	// 行高
	private int lineHeight = LINE_SIZE_UNLIMIT;
	// 行宽
	private int lineWidth = LINE_SIZE_UNLIMIT;
	// 左右居中方式
	private HAlignEnum hAlign = HAlignEnum.LEFT;
	// 上下居中方式
	private VAlignEnum vAlign = VAlignEnum.CENTER;

	// 颜色
	private Color color = Color.black;
	// 背景颜色
	private Color backgroundColor;

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

	public boolean getIsAntialias() {
		return isAntialias;
	}

	public void setIsAntialias(boolean isAntialias) {
		this.isAntialias = isAntialias;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public HAlignEnum getHAlign() {
		return hAlign;
	}

	public void setHAlign(HAlignEnum hAlign) {
		this.hAlign = hAlign;
	}

	public VAlignEnum getVAlign() {
		return vAlign;
	}

	public void setVAlign(VAlignEnum vAlign) {
		this.vAlign = vAlign;
	}

}
