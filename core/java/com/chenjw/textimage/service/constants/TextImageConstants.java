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

	public static final Logger LOGGER = LoggerFactory
			.getLogger("textImageLogger");

	// 生成图片的格式
	public static final String IMAGE_FORMAT = "png";

	// 默认版本号
	public static final String DEFAULT_STYLE_VERSION = "0.1";
	// 表示尺寸未设置
	public static final int SIZE_NOT_SET = -1;

	// 默认样式的字体名称
	public static final String DEFAULT_FONT_NAME = "宋体";

}
