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

import com.chenjw.textimage.config.ConfigHelper;
import com.chenjw.textimage.config.StyleConfig;
import com.chenjw.textimage.config.TextPosition;
import com.chenjw.textimage.config.TextStyle;
import com.chenjw.textimage.config.constants.HAlignEnum;
import com.chenjw.textimage.config.constants.VAlignEnum;
import com.chenjw.textimage.exception.TextImageException;
import com.chenjw.textimage.service.TextCanvas;
import com.chenjw.textimage.service.TextImagePainter;
import com.chenjw.textimage.service.model.Padding;
import com.chenjw.textimage.service.model.TextLine;
import com.chenjw.textimage.utils.PaintEnvironment;

/**
 * 绘图接口默认实现
 * 
 * @author chenjw 2011-11-14 下午5:03:32
 */
public class TextImagePainterImpl implements TextImagePainter {
	public TextImagePainterImpl() {
		// 启动时就加载类，避免第一个用户访问时才初始化
		PaintEnvironment.class.getClass();
	}

	@Override
	public void calculateWidth(String text, TextStyle style,
			TextPosition position, List<String> strList,
			List<TextLine> textLineList) {

		Font font = ConfigHelper.getFont(style);
		Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
		attributes.put(TextAttribute.FONT, font);
		LineBreakMeasurer lineBreakMeasurer = new LineBreakMeasurer(
				new AttributedString(text, attributes).getIterator(),
				PaintEnvironment.getDefaultFontRenderContext());
		// 按style中定义的lineWidth分割字符串
		while (lineBreakMeasurer.getPosition() < text.length()) {
			int start = lineBreakMeasurer.getPosition();
			int lineWidth = style.getLineWidth();
			if (lineWidth == TextStyle.LINE_SIZE_UNLIMIT) {
				lineWidth = Integer.MAX_VALUE;
			}
			TextLayout layout = lineBreakMeasurer.nextLayout(lineWidth);
			int end = lineBreakMeasurer.getPosition();
			// 子字符串
			String subText = StringUtils.substring(text, start, end);
			Rectangle r = layout.getPixelBounds(
					PaintEnvironment.getDefaultFontRenderContext(), 0, 0);
			// 加了这1个像素才不会重叠
			int textWidth = r.width + 1;
			int textHeight = r.height + 1;

			TextLine textLine = new TextLine();
			// 宽度使用计算的宽度
			if (style.getLineWidth() == TextStyle.LINE_SIZE_UNLIMIT) {
				textLine.setWidth(textWidth);
			} else {
				textLine.setWidth(style.getLineWidth());
			}
			// 高度使用设定
			if (style.getLineHeight() == TextStyle.LINE_SIZE_UNLIMIT) {
				textLine.setHeight(textHeight);
			} else {
				textLine.setHeight(style.getLineHeight());
			}
			// 内边距
			textLine.setPadding(calculatePadding(style, textWidth, textHeight));
			strList.add(subText);
			textLineList.add(textLine);
		}
	}

	private Padding calculatePadding(TextStyle style, int textWidth,
			int textHeight) {
		Padding padding = new Padding();
		// height
		int lineHeight = style.getLineHeight();
		if (lineHeight == TextStyle.LINE_SIZE_UNLIMIT) {
			padding.setTop(0);
			padding.setBottom(0);
		} else {
			int paddingHeight = lineHeight - textHeight;
			if (style.getVAlign() == VAlignEnum.TOP) {
				padding.setTop(0);
				padding.setBottom(paddingHeight);
			} else if (style.getVAlign() == VAlignEnum.BOTTOM) {
				padding.setTop(paddingHeight);
				padding.setBottom(0);
			} else {
				int paddingTop = paddingHeight / 2;
				int paddingBottom = paddingHeight - paddingTop;
				padding.setTop(paddingTop);
				padding.setBottom(paddingBottom);
			}
		}

		// width
		int lineWidth = style.getLineWidth();
		if (lineWidth == TextStyle.LINE_SIZE_UNLIMIT) {
			padding.setLeft(0);
			padding.setRight(0);

		} else {
			int paddingWidth = lineWidth - textWidth;
			if (style.getHAlign() == HAlignEnum.LEFT) {
				padding.setLeft(0);
				padding.setRight(paddingWidth);
			} else if (style.getHAlign() == HAlignEnum.RIGHT) {
				padding.setLeft(paddingWidth);
				padding.setRight(0);
			} else {
				int paddingLeft = paddingWidth / 2;
				int paddingRight = paddingWidth - paddingLeft;
				padding.setLeft(paddingLeft);
				padding.setRight(paddingRight);
			}
		}
		return padding;

	}

	@Override
	public void drawTextLine(TextCanvas textCanvas, String text,
			TextStyle style, TextLine line) throws TextImageException {
		if (!(textCanvas instanceof DefaultTextCanvas)) {
			throw new TextImageException("DefaultTextCanvas support only");
		}
		((DefaultTextCanvas) textCanvas).drawTextLine(text, style, line);

	}

	@Override
	public TextCanvas createTextCanvas(StyleConfig config, int width, int height) {
		return new DefaultTextCanvas(config, width, height);
	}

	private static class DefaultTextCanvas implements TextCanvas {

		private BufferedImage image;
		private Graphics2D g;

		private DefaultTextCanvas(StyleConfig config, int width, int height) {
			// 目前图片银行还不支持透明背景
			// image = GraphicsEnvironment
			// .getLocalGraphicsEnvironment()
			// .getDefaultScreenDevice()
			// .getDefaultConfiguration()
			// .createCompatibleImage(width, height,
			// Transparency.TRANSLUCENT);
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

		private void drawTextLine(String text, TextStyle style, TextLine line) {
			if (style.getBackgroundColor() != null) {
				g.setBackground(style.getBackgroundColor());
				g.clearRect(line.getX(), line.getY(), line.getWidth(),
						line.getHeight());
			}
			TextLayout textLayout = new TextLayout(text,
					ConfigHelper.getFont(style),
					PaintEnvironment.getDefaultFontRenderContext());
			g.setColor(style.getColor());
			// 偏移
			double pX = -textLayout.getPixelBounds(null, 0, 0).getX();
			double pY = // (style.getLineHeight() - style.getFontSize()) / 2
			-textLayout.getPixelBounds(null, 0, 0).getY();
			if (style.getIsAntialias()) {
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
			textLayout.draw(g, (int) Math.ceil((line.getX()
					+ line.getPadding().getLeft() + pX)), (int) Math.ceil((line
					.getY() + line.getPadding().getTop() + pY)));

		}
	}

	public static void main() {

	}
}
