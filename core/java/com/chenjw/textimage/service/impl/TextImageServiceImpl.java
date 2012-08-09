/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.textimage.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.chenjw.textimage.exception.TextImageException;
import com.chenjw.textimage.model.StyleConfig;
import com.chenjw.textimage.model.TextCanvas;
import com.chenjw.textimage.model.TextImage;
import com.chenjw.textimage.model.TextInfo;
import com.chenjw.textimage.model.TextStyle;
import com.chenjw.textimage.service.Painter;
import com.chenjw.textimage.service.TextImageBuilder;
import com.chenjw.textimage.service.TextImageContext;
import com.chenjw.textimage.service.TextImageService;
import com.chenjw.textimage.utils.PaintEnvironment;
import com.chenjw.textimage.utils.logger.Logger;
import com.chenjw.textimage.utils.logger.LoggerFactory;

/**
 * 文字图片服务实现，实现了图片的渲染和存储功能(图片银行)
 * 
 * @author chenjw
 */
public class TextImageServiceImpl implements TextImageService {

	private final static Logger LOGGER = LoggerFactory
			.getLogger("textImageLogger");

	/**
	 * 输入要生成的文字内容，生成文字图片并返回文字元数据
	 * 
	 * @param text
	 * @return
	 * @throws TextImageException
	 */
	@Override
	public TextImage buildTextImage(Map<String, String> textMap,
			StyleConfig config) throws TextImageException {
		// 验证输入参数
		_verifyParam(textMap, config);
		// 图像构建器
		TextImageBuilder builder = new DefaultTextImageBuilder();
		// 图像绘制器
		Painter painter = new DefaultPainter();
		TextImageContext context = new TextImageContext(textMap, config);
		// 构建图片结构
		builder.buildImage(context, painter);
		// 保存图片
		List<byte[]> imageList = _buildImage(context.getTextCanvasList(),
				context.getTextInfo(), textMap);
		TextImage textImage = new TextImage();
		textImage.setImageList(imageList);
		textImage.setTextInfo(context.getTextInfo());
		return textImage;
	}

	private void _verifyParam(Map<String, String> textMap,
			StyleConfig styleConfig) throws TextImageException {
		// 验证ID和TYPE

		// 验证textMap
		if (textMap == null || textMap.size() == 0) {
			throw new TextImageException("textMap cant be empty!");
		}
		// 如果未传入样式，创建默认样式
		if (styleConfig == null) {
			throw new TextImageException("styleConfig cant be null ");
		} else {
			// 验证styleConfig
			for (TextStyle style : styleConfig.getAllTextStyleList()) {
				// 验证字体是否存在
				if (!PaintEnvironment.isSupportFont(style.getFontName())) {
					throw new TextImageException("not support font "
							+ style.getFontName());
				}
			}
		}
		// 清除值为Null的项
		@SuppressWarnings("unchecked")
		Entry<String, String>[] entrys = textMap.entrySet().toArray(
				new Entry[textMap.size()]);
		for (Entry<String, String> entry : entrys) {
			if (StringUtils.isBlank(entry.getValue())) {
				textMap.remove(entry.getKey());
			}
		}
	}

	/**
	 * 通过图片银行的接口上传图片
	 * 
	 * @param textCanvasList
	 * @param textInfo
	 * @param textMap
	 * @throws TextImageException
	 */
	private List<byte[]> _buildImage(List<TextCanvas> textCanvasList,
			TextInfo textInfo, Map<String, String> textMap)
			throws TextImageException {

		try {
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
			return imageList;
		} catch (IOException e) {
			throw new TextImageException("build image failed", e);
		}
	}

}
