package com.chenjw.textimage.service.fillstrategy.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.chenjw.logger.Logger;
import com.chenjw.textimage.service.config.StyleConfig;
import com.chenjw.textimage.service.constants.TextImageConstants;
import com.chenjw.textimage.service.exception.TextImageException;
import com.chenjw.textimage.service.fillstrategy.FillStrategy;
import com.chenjw.textimage.service.fillstrategy.result.CanvasInfo;
import com.chenjw.textimage.service.fillstrategy.result.FillResult;
import com.chenjw.textimage.service.model.TextField;
import com.chenjw.textimage.service.model.TextLine;
import com.chenjw.textimage.service.model.TextMetaInfo;

/**
 * 普通填充算法<br>
 * 逐行填充，宽度不够就换行<br>
 * 当前没有用着个算法<br>
 * 
 * @author chenjw 2011-11-14 下午5:01:22
 */
public class CommonFillStrategy implements FillStrategy {
	private final static Logger LOGGER = TextImageConstants.LOGGER;

	@Override
	public FillResult tryFillCanvas(TextMetaInfo textInfo,
			StyleConfig styleConfig) throws TextImageException {
		FillResult result = new FillResult();
		int maxCanvasWidth = styleConfig.getCanvasWidth();
		int maxCanvasHeight = styleConfig.getCanvasHeight();
		List<CanvasInfo> canvasList = new ArrayList<CanvasInfo>();
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
						maxCanvasWidth, maxCanvasHeight, canvasList.size());
				if (!success) {
					CanvasInfo canvasInfo = new CanvasInfo();
					canvasInfo.setWidth(maxCanvasWidth);
					canvasInfo.setHeight(maxCanvasHeight);
					canvasList.add(canvasInfo);
					nX = 0;
					nY = 0;
					nMaxLineHeight = 0;
					if (!_fillCanvas(nX, nY, nMaxLineHeight, textLine,
							maxCanvasWidth, maxCanvasHeight, canvasList.size())) {
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
		CanvasInfo canvasInfo = new CanvasInfo();
		canvasInfo.setWidth(maxCanvasWidth);
		canvasInfo.setHeight(nY + nMaxLineHeight);
		canvasList.add(canvasInfo);
		result.setCanvasInfoList(canvasList);
		result.setTextInfo(textInfo);
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
