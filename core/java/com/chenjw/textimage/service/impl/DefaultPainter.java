/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.textimage.service.impl;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;

import com.chenjw.textimage.exception.TextImageException;
import com.chenjw.textimage.model.TextCanvas;
import com.chenjw.textimage.model.TextLine;
import com.chenjw.textimage.model.TextStyle;
import com.chenjw.textimage.service.Painter;
import com.chenjw.textimage.utils.PaintEnvironment;

/**
 * 绘图接口默认实现
 * 
 * @author chenjw 2011-11-14 下午5:03:32
 */
public class DefaultPainter implements Painter {

	@Override
	public void calculateWidth(String text, TextStyle style,
			List<String> strList, List<TextLine> textLineList) {

		Font font = TextStyle.getFont(style);
		Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
		attributes.put(TextAttribute.FONT, font);
		LineBreakMeasurer lineBreakMeasurer = new LineBreakMeasurer(
				new AttributedString(text, attributes).getIterator(),
				PaintEnvironment.FONT_RENDER_CONTEXT);
		// 按style中定义的lineWidth分割字符串
		while (lineBreakMeasurer.getPosition() < text.length()) {
			int start = lineBreakMeasurer.getPosition();
			TextLayout layout = lineBreakMeasurer.nextLayout(style
					.getLineWidth());
			int end = lineBreakMeasurer.getPosition();
			// 子字符串
			String subText = StringUtils.substring(text, start, end);
			Rectangle r = layout.getPixelBounds(
					PaintEnvironment.FONT_RENDER_CONTEXT, 0, 0);
			// 加了这1个像素才不会重叠
			int width = r.width + 1;
			TextLine textLine = new TextLine();
			// 宽度使用计算的宽度
			textLine.setWidth(width);
			// 高度使用设定
			textLine.setHeight(style.getLineHeight());
			strList.add(subText);
			textLineList.add(textLine);
		}
	}

	@Override
	public void drawTextLine(TextCanvas textCanvas, String text,
			TextStyle style, int x, int y, int width, int height)
			throws TextImageException {
		if (!(textCanvas instanceof DefaultTextCanvas)) {
			throw new TextImageException("DefaultTextCanvas support only");
		}
		((DefaultTextCanvas) textCanvas).drawTextLine(text, style, x, y, width,
				height);

	}

	@Override
	public TextCanvas createTextCanvas(int width, int height) {
		return new DefaultTextCanvas(width, height);
	}

	public static class DefaultTextCanvas implements TextCanvas {

		private BufferedImage image;
		private Graphics2D g;

		private DefaultTextCanvas(int width, int height) {
			// 目前图片银行还不支持透明背景
			// image =
			// GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(width,
			// height,
			// Transparency.TRANSLUCENT);

			// System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment());
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			g = (Graphics2D) image.getGraphics();
		}

		@Override
		public void buildImage(OutputStream os) throws TextImageException {
			try {
				g.dispose();
				ImageIO.write(image, "png", os);
			} catch (IOException e) {
				throw new TextImageException("build image failed!", e);
			}

		}

		private void drawTextLine(String text, TextStyle style, int x, int y,
				int width, int height) {
			g.setBackground(style.getBackgroundColor());
			g.clearRect(x, y, width, height);
			TextLayout textLayout = new TextLayout(text,
					TextStyle.getFont(style),
					PaintEnvironment.FONT_RENDER_CONTEXT);
			g.setColor(style.getColor());
			// 偏移
			double pX = -textLayout.getPixelBounds(null, 0, 0).getX();
			double pY = (style.getLineHeight() - style.getFontSize()) / 2
					- textLayout.getPixelBounds(null, 0, 0).getY();
			if (style.isAntialias()) {
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
						RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			} else {
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_OFF);
				g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
						RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
			}
			textLayout.draw(g, (int) Math.ceil((x + pX)),
					(int) Math.ceil((y + pY)));

		}
	}

	public static void main() {

	}
}
