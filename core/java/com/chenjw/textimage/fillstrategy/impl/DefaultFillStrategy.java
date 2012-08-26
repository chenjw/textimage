package com.chenjw.textimage.fillstrategy.impl;

import com.chenjw.textimage.config.StyleConfig;
import com.chenjw.textimage.config.TextPosition;
import com.chenjw.textimage.exception.TextImageException;
import com.chenjw.textimage.fillstrategy.FillResult;
import com.chenjw.textimage.fillstrategy.FillStrategy;
import com.chenjw.textimage.service.model.TextMetaInfo;

public class DefaultFillStrategy implements FillStrategy {
	private CompactFillStrategy compactFillStrategy = new CompactFillStrategy();
	private FixPositionFillStrategy fixPositionFillStrategy = new FixPositionFillStrategy();

	private boolean isFixPosition(TextMetaInfo textInfo, StyleConfig styleConfig) {
		for (String key : textInfo.getTextFieldMap().keySet()) {
			TextPosition textPosition = styleConfig.getPositionMap().get(key);
			if (textPosition == null) {
				return false;
			}
		}
		return true;
	}

	@Override
	public FillResult tryFillCanvas(TextMetaInfo textInfo, StyleConfig styleConfig)
			throws TextImageException {
		// 指定位置
		if (isFixPosition(textInfo, styleConfig)) {
			return fixPositionFillStrategy.tryFillCanvas(textInfo, styleConfig);
		} else {
			return compactFillStrategy.tryFillCanvas(textInfo, styleConfig);
		}
	}
}
