package com.chenjw.textimage.service.config;

import java.awt.Color;

import com.chenjw.textimage.service.config.constants.HAlignEnum;
import com.chenjw.textimage.service.config.constants.VAlignEnum;
import com.chenjw.textimage.service.constants.TextImageConstants;

/**
 * 某一段文字的样式
 * 
 * @author chenjw
 */
public class TextStyle {

	// 字体名
	private String fontName = TextImageConstants.DEFAULT_FONT_NAME;
	// 字体尺寸
	private int fontSize = 14;

	// 是否粗体
	private boolean isBold = false;
	// 是否斜体
	private boolean isItalic = false;

	// 是否抗锯齿模式
	private boolean isAntialias = false;
	// 行高
	private int lineHeight = TextImageConstants.SIZE_NOT_SET;
	// 行宽
	private int lineWidth = TextImageConstants.SIZE_NOT_SET;
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
