package com.chenjw.textimage.service.fillstrategy.impl;

import com.chenjw.textimage.service.config.StyleConfig;
import com.chenjw.textimage.service.config.TextPosition;
import com.chenjw.textimage.service.exception.TextImageException;
import com.chenjw.textimage.service.fillstrategy.FillStrategy;
import com.chenjw.textimage.service.fillstrategy.result.FillResult;
import com.chenjw.textimage.service.model.TextMetaInfo;

/**
 * 默认使用的策略，如果全部区块都是固定位置的，就是用“固定位置策略”，否则使用“紧凑策略”
 * 
 * @author chenjw
 * 
 */
public class DefaultFillStrategy implements FillStrategy {
	private CompactFillStrategy compactFillStrategy = new CompactFillStrategy();
	private FixPositionFillStrategy fixPositionFillStrategy = new FixPositionFillStrategy();

	private boolean isFixPosition(TextMetaInfo textInfo, StyleConfig styleConfig) throws TextImageException {
		// 0 初始化，1 有position 2 没positon
		boolean isInit=false;
		boolean isPos=false;
		for (String key : textInfo.getTextFieldMap().keySet()) {
			TextPosition textPosition = styleConfig.getPositionMap().get(key);
			if(!isInit){
				isInit=true;
				isPos=textPosition!=null;
			}
			else{
				if(isPos!=(textPosition!=null)){
					throw new TextImageException("必须所有的字段都设置position，或都不设置position，（"+key+"）");
				}
			}
		}
		return isPos;
	}

	@Override
	public FillResult tryFillCanvas(TextMetaInfo textInfo,
			StyleConfig styleConfig) throws TextImageException {
		// 指定位置
		if (isFixPosition(textInfo, styleConfig)) {
			return fixPositionFillStrategy.tryFillCanvas(textInfo, styleConfig);
		} else {
			return compactFillStrategy.tryFillCanvas(textInfo, styleConfig);
		}
	}
}
