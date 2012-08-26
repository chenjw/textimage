/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.textimage.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import com.chenjw.textimage.utils.logger.Logger;
import com.chenjw.textimage.utils.logger.LoggerFactory;

/**
 * 绘图环境
 * 
 * @author chenjw
 */
public abstract class PaintEnvironment {

	private final static Logger LOGGER = LoggerFactory
			.getLogger("textImageLogger");
	private static final FontRenderContext FONT_RENDER_CONTEXT;

	public static FontRenderContext getDefaultFontRenderContext() {
		return FONT_RENDER_CONTEXT;
	}

	private static final Set<String> SUPPORTED_FONTNAME;

	/**
	 * 判断当前环境是否支持某字体
	 * 
	 * @param fontName
	 * @return
	 */
	public static boolean isSupportFont(String fontName) {
		return SUPPORTED_FONTNAME.contains(fontName);
	}

	/**
	 * 加载字体
	 * 
	 * @param path
	 */
	public static void loadFont(String path) {
		InputStream in = null;
		try {
			in = PaintEnvironment.class.getClassLoader().getResourceAsStream(
					path);
			Font f = Font.createFont(Font.TRUETYPE_FONT, in);
			String fontName = f.getFontName();
			if (isSupportFont(fontName)) {
				LOGGER.warn("font " + fontName + " already registered!");
			} else {
				GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(
						f);
				SUPPORTED_FONTNAME.add(fontName);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("font " + fontName + " registered!");
				}
			}
		} catch (FontFormatException e) {
			LOGGER.error("not support font " + path + " !", e);
		} catch (IOException e) {
			LOGGER.error("load font with io error " + path + " !", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
	}

	static {
		// 初始化fontRenderContext,还有其他方式获得fontRenderContext吗？
		BufferedImage image = new BufferedImage(1, 1,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		FONT_RENDER_CONTEXT = g.getFontRenderContext();
		// 加载需要的字体
		SUPPORTED_FONTNAME = new HashSet<String>();
		for (Font font : GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getAllFonts()) {
			SUPPORTED_FONTNAME.add(font.getFontName());
		}
		// 宋体
		loadFont("com/chenjw/textimage/utils/simsun.ttc");
		loadFont("com/chenjw/textimage/utils/tahoma.ttf");
	}
}
