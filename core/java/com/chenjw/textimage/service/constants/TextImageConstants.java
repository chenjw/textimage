package com.chenjw.textimage.service.constants;

import com.chenjw.logger.Logger;
import com.chenjw.logger.LoggerFactory;

/**
 * 一些常量
 * 
 * @author chenjw
 * 
 */
public class TextImageConstants {
	//
	public static final Logger LOGGER = LoggerFactory
			.getLogger("textImageLogger");

	// 生成图片的格式
	public final static String IMAGE_FORMAT = "png";

	// 是否使用透明模式(取决于imageStore是否支持)
	public final static boolean IS_USE_TRANSLUCENT = true;
}
