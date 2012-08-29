package com.chenjw.textimage.service.spi;

import java.util.List;

import com.chenjw.textimage.service.config.StyleConfig;
import com.chenjw.textimage.service.config.TextPosition;
import com.chenjw.textimage.service.config.TextStyle;
import com.chenjw.textimage.service.exception.TextImageException;
import com.chenjw.textimage.service.model.TextLine;

/**
 * 绘图接口，所有图像相关的操作都封装到这个接口的实现中
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
