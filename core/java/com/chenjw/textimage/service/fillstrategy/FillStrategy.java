package com.chenjw.textimage.service.fillstrategy;

import com.chenjw.textimage.service.config.StyleConfig;
import com.chenjw.textimage.service.exception.TextImageException;
import com.chenjw.textimage.service.fillstrategy.result.FillResult;
import com.chenjw.textimage.service.model.TextMetaInfo;

/**
 * 填充策略，定义了把文字块排布到画布上的算法
 * 
 * @author chenjw 2011-11-11 上午11:49:26
 */
public interface FillStrategy {

	/**
	 * 输入时文字元数据只有width和height 输出时新设置了x和y 并返回使用的画布的范围
	 * 
	 * @param context
	 * @return
	 * @throws TextImageException
	 */
	public FillResult tryFillCanvas(TextMetaInfo textInfo,
			StyleConfig styleConfig) throws TextImageException;
}