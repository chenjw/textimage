package com.chenjw.textimage.service.config;

import java.awt.Font;

import org.apache.commons.lang.StringUtils;

/**
 * 样式配置工具类
 * 
 * @author chenjw
 * 
 */
public class StyleConfigHelper {
	/**
	 * 查找某个Key对应文本的样式
	 * 
	 * @param key
	 * @return
	 */
	public static TextStyle getTextStyle(StyleConfig styleConfig, String key) {
		String className = styleConfig.getClassMappingMap().get(key);
		if (!StringUtils.isBlank(className)) {
			TextStyle style = styleConfig.getClassMap().get(className);
			if (style != null) {
				return style;
			}
		}
		return styleConfig.getDefaultStyle();
	}

	/**
	 * 获得字体
	 * 
	 * @param style
	 * @return
	 */
	public static Font getFont(TextStyle style) {

		int fontWeight;
		if (style.getIsBold()) {
			if (style.getIsItalic()) {
				fontWeight = Font.BOLD | Font.ITALIC;
			} else {
				fontWeight = Font.BOLD;
			}
		} else {
			if (style.getIsItalic()) {
				fontWeight = Font.ITALIC;
			} else {
				fontWeight = Font.PLAIN;
			}
		}
		Font font = new Font(style.getFontName(), fontWeight,
				style.getFontSize());
		return font;

	}

}
