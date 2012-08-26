/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.textimage.fillstrategy.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.chenjw.textimage.config.StyleConfig;
import com.chenjw.textimage.config.TextPosition;
import com.chenjw.textimage.exception.TextImageException;
import com.chenjw.textimage.fillstrategy.CanvasInfo;
import com.chenjw.textimage.fillstrategy.FillResult;
import com.chenjw.textimage.fillstrategy.FillStrategy;
import com.chenjw.textimage.service.model.TextField;
import com.chenjw.textimage.service.model.TextLine;
import com.chenjw.textimage.service.model.TextMetaInfo;

/**
 * 按照样式中指定的位置生成图片
 * 
 * @author chenjw 2011-11-14 下午5:01:53
 */
public class FixPositionFillStrategy implements FillStrategy {

	@Override
	public FillResult tryFillCanvas(TextMetaInfo textInfo,
			StyleConfig styleConfig) throws TextImageException {
		FillResult result = new FillResult();
		int maxWidth = 0;
		int maxHeight = 0;
		for (Entry<String, TextField> entry : textInfo.getTextFieldMap()
				.entrySet()) {
			String key = entry.getKey();
			TextPosition position = styleConfig.getPositionMap().get(key);
			TextField field = entry.getValue();
			if (field.getTextLines().size() > 0) {
				TextLine line = field.getTextLines().get(0);
				if (position.getX() + line.getWidth() > maxWidth) {
					maxWidth = position.getX() + line.getWidth();
				}
				if (position.getY() + line.getHeight() > maxHeight) {
					maxHeight = position.getY() + line.getHeight();
				}
				line.setX(position.getX());
				line.setY(position.getY());
				// line.setWidth(position.getWidth());
				// line.setHeight(position.getHeight());
				line.setImageIndex(0);
			}
		}
		result.setTextInfo(textInfo);
		List<CanvasInfo> canvasInfoList = new ArrayList<CanvasInfo>();
		if (maxWidth > 0 && maxHeight > 0) {
			CanvasInfo canvasInfo = new CanvasInfo();
			canvasInfo.setWidth(maxWidth);
			canvasInfo.setHeight(maxHeight);
			canvasInfoList.add(canvasInfo);
		}
		result.setCanvasInfoList(canvasInfoList);
		return result;
	}
}
