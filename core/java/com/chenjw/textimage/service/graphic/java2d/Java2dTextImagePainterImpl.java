package com.chenjw.textimage.service.graphic.java2d;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.chenjw.textimage.service.config.StyleConfig;
import com.chenjw.textimage.service.config.StyleConfigHelper;
import com.chenjw.textimage.service.config.TextPosition;
import com.chenjw.textimage.service.config.TextStyle;
import com.chenjw.textimage.service.config.constants.HAlignEnum;
import com.chenjw.textimage.service.config.constants.VAlignEnum;
import com.chenjw.textimage.service.constants.TextImageConstants;
import com.chenjw.textimage.service.exception.TextImageException;
import com.chenjw.textimage.service.model.Padding;
import com.chenjw.textimage.service.model.TextLine;
import com.chenjw.textimage.service.spi.TextCanvas;
import com.chenjw.textimage.service.spi.TextImagePainter;

/**
 * 绘图接口默认实现
 * 
 * @author chenjw 2011-11-14 下午5:03:32
 */
public class Java2dTextImagePainterImpl implements TextImagePainter {
	public Java2dTextImagePainterImpl() {
		// 启动时就加载类，避免第一个用户访问时才初始化
		PaintEnvironment.class.getClass();
	}

	@Override
	public void calculateWidth(String text, TextStyle style,
			TextPosition position, List<String> strList,
			List<TextLine> textLineList) {

		Font font = StyleConfigHelper.getFont(style);
		Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
		attributes.put(TextAttribute.FONT, font);
		LineBreakMeasurer lineBreakMeasurer = new LineBreakMeasurer(
				new AttributedString(text, attributes).getIterator(),
				PaintEnvironment.getDefaultFontRenderContext());
		// 按style中定义的lineWidth分割字符串
		while (lineBreakMeasurer.getPosition() < text.length()) {
			int start = lineBreakMeasurer.getPosition();
			int lineWidth = style.getLineWidth();
			if (lineWidth == TextImageConstants.SIZE_NOT_SET) {
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
			if (style.getLineWidth() == TextImageConstants.SIZE_NOT_SET) {
				textLine.setWidth(textWidth);
			} else {
				textLine.setWidth(style.getLineWidth());
			}
			// 高度使用设定
			if (style.getLineHeight() == TextImageConstants.SIZE_NOT_SET) {
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
		if (lineHeight == TextImageConstants.SIZE_NOT_SET) {
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
		if (lineWidth == TextImageConstants.SIZE_NOT_SET) {
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
		if (!(textCanvas instanceof Java2dCanvas)) {
			throw new TextImageException("DefaultTextCanvas support only");
		}
		((Java2dCanvas) textCanvas).drawTextLine(text, style, line);

	}

	@Override
	public TextCanvas createTextCanvas(StyleConfig config, int width, int height) {
		return new Java2dCanvas(config, width, height);
	}

	public static void main() {

	}
}
