/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.textimage.service.impl;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.chenjw.textimage.exception.TextImageException;
import com.chenjw.textimage.model.StyleConfig;
import com.chenjw.textimage.model.TextCanvas;
import com.chenjw.textimage.model.TextField;
import com.chenjw.textimage.model.TextInfo;
import com.chenjw.textimage.model.TextLine;
import com.chenjw.textimage.model.TextStyle;
import com.chenjw.textimage.service.Painter;
import com.chenjw.textimage.service.TextImageBuilder;
import com.chenjw.textimage.service.TextImageContext;
import com.chenjw.textimage.service.impl.fillstrategy.FillStrategy2;
import com.chenjw.textimage.utils.logger.Logger;
import com.chenjw.textimage.utils.logger.LoggerFactory;

/**
 * 文字图片构造器默认实现
 * 
 * @author chenjw 2011-11-14 下午5:04:23
 */
public class DefaultTextImageBuilder implements TextImageBuilder {

	private final static Logger LOGGER = LoggerFactory
			.getLogger("textImageLogger");
	private FillStrategy fillStrategy = new FillStrategy2();
	private ThreadLocal<Long> timeMark = new ThreadLocal<Long>();

	@Override
	public void buildImage(TextImageContext context, Painter painter)
			throws TextImageException {
		/****************************
		 * 初始化输入参数
		 ***************************/
		// 输入文本
		Map<String, String> textMap = context.getTextMap();
		// 文本元数据
		TextInfo textInfo = context.getTextInfo();
		// 文本样式
		StyleConfig styleConfig = context.getStyleConfig();
		// 输出画布的尺寸
		List<Dimension> canvasSize = new ArrayList<Dimension>();
		// 输出画布
		List<TextCanvas> textCanvasList = context.getTextCanvasList();
		// 保存textLine到文字片段的映射
		Map<TextLine, String> textLineStrMap = new HashMap<TextLine, String>();
		// 缓存已绘制的文本，为了使同一文本在画布上只出现一次
		Map<TextStyle, Map<String, TextLine>> styleStrTextLineStrMap = new HashMap<TextStyle, Map<String, TextLine>>();

		/****************************
		 * 预处理文字，包括分割文字和计算每小段文字的长和宽
		 ***************************/
		_preprocess(textMap, styleConfig, textInfo, painter, textLineStrMap,
				styleStrTextLineStrMap);
		/****************************
		 * 尝试填充画布，计算每个textLine的填充位置，返回所需画布的尺寸
		 ***************************/
		_tryFillCanvas(canvasSize, context);
		/****************************
		 * 创建所需画布
		 ***************************/
		_createCanvas(canvasSize, textCanvasList, painter);
		/****************************
		 * 在画布上绘制文字
		 ***************************/
		_drawText(textInfo, styleConfig, painter, textCanvasList,
				textLineStrMap);

	}

	/**
	 * 预处理文字，包括分割文字和计算每小段文字的长和宽
	 * 
	 * @param textMap
	 * @param styleConfig
	 * @param painter
	 * @param textLineStrMap
	 * @param styleStrTextLineStrMap
	 * @throws TextImageException
	 */
	private void _preprocess(Map<String, String> textMap,
			StyleConfig styleConfig, TextInfo textInfo, Painter painter,
			Map<TextLine, String> textLineStrMap,
			Map<TextStyle, Map<String, TextLine>> styleStrTextLineStrMap)
			throws TextImageException {
		// 计算宽度和高度
		for (Entry<String, String> textEntry : textMap.entrySet()) {
			String key = textEntry.getKey();
			String text = textEntry.getValue();
			if (!StringUtils.isBlank(text)) {
				TextStyle style = styleConfig.getTextStyle(key);
				TextField textField = new TextField();
				Map<String, TextLine> strTextLineMap = styleStrTextLineStrMap
						.get(style);
				if (strTextLineMap == null) {
					strTextLineMap = new HashMap<String, TextLine>();
					styleStrTextLineStrMap.put(style, strTextLineMap);
				}
				List<String> strList = new ArrayList<String>();
				List<TextLine> textLineList = new ArrayList<TextLine>();
				// 计算长宽
				painter.calculateWidth(text, style, strList, textLineList);
				for (int i = 0, s = textLineList.size(); i < s; i++) {
					String str = strList.get(i);
					TextLine testLine = textLineList.get(i);
					// 如果已经存在了
					if (strTextLineMap.containsKey(str)) {
						testLine = strTextLineMap.get(str);
					} else {
						textLineStrMap.put(testLine, str);
						strTextLineMap.put(str, testLine);
					}
					textField.getTextLines().add(testLine);
				}
				textInfo.getTextFieldMap().put(key, textField);
			}
		}
	}

	/**
	 * 在画布上绘制文本
	 * 
	 * @param textInfo
	 * @param styleConfig
	 * @param painter
	 * @param textCanvasList
	 * @param textLineStrMap
	 * @throws TextImageException
	 */
	private void _drawText(TextInfo textInfo, StyleConfig styleConfig,
			Painter painter, List<TextCanvas> textCanvasList,
			Map<TextLine, String> textLineStrMap) throws TextImageException {
		// 开始画图
		for (Entry<String, TextField> textFieldentry : textInfo
				.getTextFieldMap().entrySet()) {
			for (TextLine textLine : textFieldentry.getValue().getTextLines()) {
				TextCanvas textCanvas = textCanvasList.get(textLine
						.getImageIndex());
				String text = textLineStrMap.get(textLine);
				// 绘制某一行文字
				painter.drawTextLine(textCanvas, text,
						styleConfig.getTextStyle(textFieldentry.getKey()),
						textLine.getX(), textLine.getY(), textLine.getWidth(),
						textLine.getHeight());
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("drawText " + text + "(x:" + textLine.getX()
							+ ",y:" + textLine.getY() + ",w:"
							+ textLine.getWidth() + ",h:"
							+ textLine.getHeight() + ",index:"
							+ textLine.getImageIndex() + ")");
				}

			}
		}
	}

	/**
	 * 创建画布
	 * 
	 * @param canvasSize
	 * @param textCanvasList
	 * @param painter
	 * @throws TextImageException
	 */
	private void _createCanvas(List<Dimension> canvasSize,
			List<TextCanvas> textCanvasList, Painter painter)
			throws TextImageException {
		for (Dimension dimension : canvasSize) {
			TextCanvas canvas = painter.createTextCanvas(
					(int) dimension.getWidth(), (int) dimension.getHeight());
			textCanvasList.add(canvas);
		}
	}

	/**
	 * 尝试填充画布，计算每个textLine的填充位置，返回需要的画布尺寸
	 * 
	 * @throws TextImageException
	 */
	private void _tryFillCanvas(List<Dimension> canvasSize,
			TextImageContext context) throws TextImageException {
		if (LOGGER.isDebugEnabled()) {
			timeMark.set(System.currentTimeMillis());
		}
		// 计算需要的画布大小，和每个文字行放置的位置
		canvasSize.addAll(fillStrategy.tryFillCanvas(context));
		if (LOGGER.isDebugEnabled()) {
			// 计算空间使用率
			_viewFillStat(canvasSize, context.getTextInfo());
		}
	}

	/**
	 * 打印空间利用率
	 * 
	 * @param canvasSize
	 * @param textInfo
	 */
	private void _viewFillStat(List<Dimension> canvasSize, TextInfo textInfo) {
		long start = timeMark.get();
		timeMark.remove();
		List<int[][]> canvasList = new ArrayList<int[][]>();
		for (Dimension d : canvasSize) {
			int[][] canvas = new int[(int) d.getHeight()][(int) d.getWidth()];
			for (int j = 0; j < canvas.length; j++) {
				for (int i = 0; i < canvas[j].length; i++) {
					canvas[j][i] = 0;
				}
			}
			canvasList.add(canvas);
		}
		Set<TextLine> textLineSet = new HashSet<TextLine>();
		for (TextField f : textInfo.getTextFieldMap().values()) {
			for (TextLine l : f.getTextLines()) {
				textLineSet.add(l);
			}
		}
		int c = 1;
		for (TextLine l : textLineSet) {
			int[][] canvas = canvasList.get(l.getImageIndex());
			for (int j = l.getY(); j < l.getHeight() + l.getY(); j++) {
				for (int i = l.getX(); i < l.getWidth() + l.getX(); i++) {
					if (canvas[j][i] > 0) {
						LOGGER.error("overlapping in(" + i + "," + j + ","
								+ l.getImageIndex() + ")！");
					}
					canvas[j][i] = c;
				}
			}
			c++;
		}
		int all = 0;
		int fill = 0;
		for (int[][] canvas : canvasList) {
			for (int j = 0; j < canvas.length; j++) {
				for (int i = 0; i < canvas[j].length; i++) {
					all++;
					if (canvas[j][i] > 0) {
						fill++;
					}
				}
			}
		}
		LOGGER.debug((fill + 0.0) / all * 100 + "% filled! use "
				+ (System.currentTimeMillis() - start) + " ms");
	}

	/**
	 * 填充策略，定义了把文字块排布到画布上的算法
	 * 
	 * @author chenjw 2011-11-11 上午11:49:26
	 */
	public interface FillStrategy {

		/**
		 * 输入时context中的文字元数据只有width和height 输出时新设置了x和y 并返回使用的画布的范围
		 * 
		 * @param context
		 * @return
		 * @throws TextImageException
		 */
		List<Dimension> tryFillCanvas(TextImageContext context)
				throws TextImageException;
	}

}
