package com.chenjw.textimage.service.config;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import com.chenjw.textimage.service.constants.TextImageConstants;

/**
 * 文字图片的总体样式描述
 * 
 * @author chenjw 2011-11-14 下午4:59:33
 */
public class StyleConfig {

	/** 画布样式 开始 **/
	/**
	 * 画布尺寸规则:
	 * <ul>
	 * <li>1. 如果是固定尺寸，则canvasWdith表示固定尺寸的大小</li>
	 * <li>2. 如果不是固定尺寸，则canvasWdith表示画布最大的大小，会根据具体内容调整大小，但不会超过这个值</li>
	 * </ul>
	 * **/
	// 是否固定尺寸
	private boolean isFixSize = false;

	// 最大画布宽度
	private int canvasWidth = 800;
	// 最大画布高度
	private int canvasHeight = 600;

	/**
	 * 背景样式显示规则
	 * <ul>
	 * <li>1. 优先使用backgroundImage的值</li>
	 * <li>2. 如果backgroundImage为null使用backgroundColor的值</li>
	 * <li>3. 如果backgroundColor为null使用透明背景</li>
	 * </ul>
	 * **/
	// 背景颜色
	private Color backgroundColor = Color.black;
	// 背景图片
	private byte[] backgroundImage = null;

	/** 画布样式 结束 **/
	// 样式版本，框架会根据版本号来判断样式是否变化，如果样式有变化需要重新生成图片，请修改样式版本的值
	private String styleVersion = TextImageConstants.DEFAULT_STYLE_VERSION;

	// 默认样式,当没有找到某文本key的样式时会指到默认样式
	private TextStyle defaultStyle = new TextStyle();
	// 样式名称到样式类的映射
	private Map<String, TextStyle> classMap = new HashMap<String, TextStyle>();
	// 文本key到样式名称的映射
	private Map<String, String> classMappingMap = new HashMap<String, String>();
	// 样式名称到样式类的映射
	private Map<String, TextPosition> positionMap = new HashMap<String, TextPosition>();

	public void addClass(String className, TextStyle style) {
		classMap.put(className, style);
	}

	public void addClassMapping(String key, String className) {
		classMappingMap.put(key, className);
	}

	public void addTextPosition(String key, TextPosition position) {
		positionMap.put(key, position);
	}

	/*******************
	 * get/set方法
	 *******************/

	public boolean getIsFixSize() {
		return isFixSize;
	}

	public void setIsFixSize(boolean isFixSize) {
		this.isFixSize = isFixSize;
	}

	public int getCanvasWidth() {
		return canvasWidth;
	}

	public void setCanvasWidth(int canvasWidth) {
		this.canvasWidth = canvasWidth;
	}

	public int getCanvasHeight() {
		return canvasHeight;
	}

	public void setCanvasHeight(int canvasHeight) {
		this.canvasHeight = canvasHeight;
	}

	public String getStyleVersion() {
		return styleVersion;
	}

	public void setStyleVersion(String styleVersion) {
		this.styleVersion = styleVersion;
	}

	public TextStyle getDefaultStyle() {
		return defaultStyle;
	}

	public void setDefaultStyle(TextStyle defaultStyle) {
		this.defaultStyle = defaultStyle;
	}

	public Map<String, TextStyle> getClassMap() {
		return classMap;
	}

	public void setClassMap(Map<String, TextStyle> classMap) {
		this.classMap = classMap;
	}

	public Map<String, String> getClassMappingMap() {
		return classMappingMap;
	}

	public void setClassMappingMap(Map<String, String> classMappingMap) {
		this.classMappingMap = classMappingMap;
	}

	public Map<String, TextPosition> getPositionMap() {
		return positionMap;
	}

	public void setPositionMap(Map<String, TextPosition> positionMap) {
		this.positionMap = positionMap;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public byte[] getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(byte[] backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

}
