package com.chenjw.textimage.service.builder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.chenjw.logger.Logger;
import com.chenjw.textimage.service.config.StyleConfigHelper;
import com.chenjw.textimage.service.config.TextStyle;
import com.chenjw.textimage.service.constants.TextImageConstants;
import com.chenjw.textimage.service.exception.TextImageException;
import com.chenjw.textimage.service.fillstrategy.FillStrategy;
import com.chenjw.textimage.service.fillstrategy.impl.DefaultFillStrategy;
import com.chenjw.textimage.service.fillstrategy.result.CanvasInfo;
import com.chenjw.textimage.service.fillstrategy.result.FillResult;
import com.chenjw.textimage.service.model.TextField;
import com.chenjw.textimage.service.model.TextImageContext;
import com.chenjw.textimage.service.model.TextLine;
import com.chenjw.textimage.service.model.TextMetaInfo;
import com.chenjw.textimage.service.spi.TextCanvas;
import com.chenjw.textimage.service.spi.TextImageBuilder;
import com.chenjw.textimage.service.spi.TextImagePainter;
import com.chenjw.textimage.service.utils.Profiler;

/**
 * 文字图片构造器默认实现
 * 
 * @author chenjw 2011-11-14 下午5:04:23
 */
public class TextImageBuilderImpl implements TextImageBuilder {

	private final static Logger LOGGER = TextImageConstants.LOGGER;
	private FillStrategy fillStrategy = new DefaultFillStrategy();

	@Override
	public void buildImage(TextImageContext context, TextImagePainter painter)
			throws TextImageException {
		/****************************
		 * 初始化输入参数
		 ***************************/

		// 保存textLine到文字片段的映射
		Map<TextLine, String> textLineStrMap = new HashMap<TextLine, String>();
		// 缓存已绘制的文本，为了使同一文本在画布上只出现一次
		Map<TextStyle, Map<String, TextLine>> styleStrTextLineStrMap = new HashMap<TextStyle, Map<String, TextLine>>();

		/****************************
		 * 预处理文字，包括分割文字和计算每小段文字的长和宽
		 ***************************/
		preprocess(context, painter, textLineStrMap, styleStrTextLineStrMap);
		/****************************
		 * 尝试填充画布，计算每个textLine的填充位置，返回所需画布的尺寸
		 ***************************/
		tryFillCanvas(context);
		/****************************
		 * 创建所需画布
		 ***************************/
		createCanvas(context, painter);
		/****************************
		 * 在画布上绘制文字
		 ***************************/
		drawText(context, painter, textLineStrMap);
		/****************************
		 * 生成图像
		 ***************************/
		outputImage(context);
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
	private void preprocess(TextImageContext context, TextImagePainter painter,
			Map<TextLine, String> textLineStrMap,
			Map<TextStyle, Map<String, TextLine>> styleStrTextLineStrMap)
			throws TextImageException {
		TextMetaInfo textInfo = new TextMetaInfo();
		// 计算宽度和高度
		for (Entry<String, String> textEntry : context.getTextMap().entrySet()) {
			String key = textEntry.getKey();
			String text = textEntry.getValue();
			if (!StringUtils.isBlank(text)) {
				TextStyle style = StyleConfigHelper.getTextStyle(
						context.getStyleConfig(), key);
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
				painter.calculateWidth(text, style, context.getStyleConfig()
						.getPositionMap().get(key), strList, textLineList);
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
		context.setTextInfo(textInfo);
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
	private void drawText(TextImageContext context, TextImagePainter painter,
			Map<TextLine, String> textLineStrMap) throws TextImageException {
		// 开始画图
		for (Entry<String, TextField> textFieldentry : context.getTextInfo()
				.getTextFieldMap().entrySet()) {
			for (TextLine textLine : textFieldentry.getValue().getTextLines()) {
				TextCanvas textCanvas = context.getTextCanvasList().get(
						textLine.getImageIndex());
				String text = textLineStrMap.get(textLine);
				// 绘制某一行文字
				painter.drawTextLine(textCanvas, text, StyleConfigHelper
						.getTextStyle(context.getStyleConfig(),
								textFieldentry.getKey()), textLine);
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
	private void createCanvas(TextImageContext context, TextImagePainter painter)
			throws TextImageException {
		List<TextCanvas> canvasList = new ArrayList<TextCanvas>();
		for (CanvasInfo d : context.getCanvasInfoList()) {
			TextCanvas canvas = painter.createTextCanvas(
					context.getStyleConfig(), d.getWidth(), d.getHeight());
			canvasList.add(canvas);
		}
		context.setTextCanvasList(canvasList);
	}

	/**
	 * 尝试填充画布，计算每个textLine的填充位置，返回需要的画布尺寸
	 * 
	 * @throws TextImageException
	 */
	private void tryFillCanvas(TextImageContext context)
			throws TextImageException {
		if (LOGGER.isDebugEnabled()) {
			Profiler.getInstance().begin();
		}
		// 计算需要的画布大小，和每个文字行放置的位置

		FillResult result = fillStrategy.tryFillCanvas(context.getTextInfo(),
				context.getStyleConfig());
		context.setCanvasInfoList(result.getCanvasInfoList());
		context.setTextInfo(result.getTextInfo());
		if (LOGGER.isDebugEnabled()) {
			// 计算空间使用率
			_viewFillStat(context.getCanvasInfoList(), context.getTextInfo());
		}
	}

	/**
	 * 打印空间利用率
	 * 
	 * @param canvasSize
	 * @param textInfo
	 */
	private void _viewFillStat(List<CanvasInfo> canvasSize,
			TextMetaInfo textInfo) {
		List<int[][]> canvasList = new ArrayList<int[][]>();
		for (CanvasInfo d : canvasSize) {
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
				+ Profiler.getInstance().getMillisInterval() + " ms");
	}

	private void outputImage(TextImageContext context)
			throws TextImageException {
		try {
			List<TextCanvas> textCanvasList = context.getTextCanvasList();
			List<byte[]> imageList = new ArrayList<byte[]>();
			for (int i = 0, s = textCanvasList.size(); i < s; i++) {
				ByteArrayOutputStream os = null;
				try {
					os = new ByteArrayOutputStream();
					textCanvasList.get(i).buildImage(os);
					os.flush();
					imageList.add(os.toByteArray());
				} finally {
					if (os != null) {
						os.close();
					}
				}
			}
			context.setImageList(imageList);
		} catch (IOException e) {
			throw new TextImageException("build image failed", e);
		}
	}

}
