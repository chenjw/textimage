/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.textimage.service;

import java.util.List;

import com.chenjw.textimage.config.StyleConfig;
import com.chenjw.textimage.config.TextPosition;
import com.chenjw.textimage.config.TextStyle;
import com.chenjw.textimage.exception.TextImageException;
import com.chenjw.textimage.service.model.TextLine;

/**
 * 绘图接口
 * 
 * @author chenjw 2011-11-14 下午5:03:55
 */
public interface TextImagePainter {

	/**
	 * 创建画布
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	public TextCanvas createTextCanvas(StyleConfig config, int width, int height)
			throws TextImageException;

	/**
	 * 文字分行并计算尺寸
	 * 
	 * @param key
	 * @param text
	 * @param style
	 */
	public void calculateWidth(String text, TextStyle style,
			TextPosition position, List<String> strList,
			List<TextLine> textLineList) throws TextImageException;

	/**
	 * 在画布上画一行文字
	 * 
	 * @param textCanvas
	 * @param textLine
	 * @param x
	 * @param y
	 */
	public void drawTextLine(TextCanvas textCanvas, String text,
			TextStyle style, TextLine line) throws TextImageException;

}
