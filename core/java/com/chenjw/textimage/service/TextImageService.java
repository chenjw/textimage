/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.textimage.service;

import java.util.Map;

import com.chenjw.textimage.exception.TextImageException;
import com.chenjw.textimage.model.StyleConfig;
import com.chenjw.textimage.model.TextImage;

/**
 * 文字图片服务实现，实现了图片的渲染和存储功能
 * 
 * @author chenjw
 */
public interface TextImageService {

	/**
	 * 输入要生成的文字内容，生成文字图片并返回文字元数据
	 * 
	 * @param text
	 * @return
	 */
	public TextImage buildTextImage(Map<String, String> textMap,
			StyleConfig config) throws TextImageException;

}
