/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.textimage.service.impl.fillstrategy;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.chenjw.textimage.exception.TextImageException;
import com.chenjw.textimage.model.StyleConfig;
import com.chenjw.textimage.model.TextField;
import com.chenjw.textimage.model.TextInfo;
import com.chenjw.textimage.model.TextLine;
import com.chenjw.textimage.service.TextImageContext;
import com.chenjw.textimage.service.impl.DefaultTextImageBuilder.FillStrategy;
import com.chenjw.textimage.utils.logger.Logger;
import com.chenjw.textimage.utils.logger.LoggerFactory;

/**
 * 填充算法1<br>
 * 逐行填充，宽度不够就换行<br>
 * 当前没有用着个算法，使用的是FillStrategy2<br>
 * 
 * @author chenjw 2011-11-14 下午5:01:22
 */
public class FillStrategy1 implements FillStrategy {
	private final static Logger LOGGER = LoggerFactory
			.getLogger("textImageLogger");

	@Override
	public List<Dimension> tryFillCanvas(TextImageContext context)
			throws TextImageException {
		TextInfo textInfo = context.getTextInfo();
		StyleConfig styleConfig = context.getStyleConfig();
		int maxCanvasWidth = styleConfig.getMaxCanvasWidth();
		int maxCanvasHeight = styleConfig.getMaxCanvasHeight();
		List<Dimension> result = new ArrayList<Dimension>();
		// 这个实现很烂，待优化
		// 当前位置
		int nX = 0;
		int nY = 0;
		// 当前的最高y
		int nMaxLineHeight = 0;
		for (Entry<String, TextField> textFieldentry : textInfo
				.getTextFieldMap().entrySet()) {
			for (TextLine textLine : textFieldentry.getValue().getTextLines()) {
				//
				boolean success = _fillCanvas(nX, nY, nMaxLineHeight, textLine,
						maxCanvasWidth, maxCanvasHeight, result.size());
				if (!success) {
					result.add(new Dimension(maxCanvasWidth, maxCanvasHeight));
					nX = 0;
					nY = 0;
					nMaxLineHeight = 0;
					if (!_fillCanvas(nX, nY, nMaxLineHeight, textLine,
							maxCanvasWidth, maxCanvasHeight, result.size())) {
						throw new TextImageException(
								"整张图片都装不下一段文字，我也没辙了: width="
										+ textLine.getWidth() + ",height="
										+ textLine.getHeight());
					}
				}
				nX = textLine.getX() + textLine.getWidth();
				if (textLine.getY() > nY) {
					nMaxLineHeight = textLine.getHeight();
					nY = textLine.getY();
				} else {
					if (textLine.getHeight() > nMaxLineHeight) {
						nMaxLineHeight = textLine.getHeight();
					}
				}
			}
		}
		result.add(new Dimension(maxCanvasWidth, nY + nMaxLineHeight));
		return result;
	}

	/**
	 * 递归填充画布
	 * 
	 * @param nX
	 * @param nY
	 * @param nMaxLineHeight
	 * @param textLine
	 * @param maxCanvasWidth
	 * @param maxCanvasHeight
	 * @param currentCanvasIndex
	 * @return
	 */
	private boolean _fillCanvas(int nX, int nY, int nMaxLineHeight,
			TextLine textLine, int maxCanvasWidth, int maxCanvasHeight,
			int currentCanvasIndex) {
		// 这行能装得下
		if (nX + textLine.getWidth() <= maxCanvasWidth) {
			if (nY + textLine.getHeight() > maxCanvasHeight) {
				return false;
			}
			textLine.setX(nX);
			textLine.setY(nY);
			textLine.setImageIndex(currentCanvasIndex);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("(x:" + textLine.getX() + ",y:" + textLine.getY()
						+ ",w:" + textLine.getWidth() + ",h:"
						+ textLine.getHeight() + ",i:" + currentCanvasIndex
						+ ")");
			}

			return true;
		}
		// 这行装不下
		else {
			// 换一行，从头开始写
			return _fillCanvas(0, nY + nMaxLineHeight, 0, textLine,
					maxCanvasWidth, maxCanvasHeight, currentCanvasIndex);
		}
	}
}
