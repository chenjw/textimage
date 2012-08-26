/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.textimage.service;

import java.util.Map;

import com.chenjw.textimage.config.StyleConfig;
import com.chenjw.textimage.exception.TextImageException;
import com.chenjw.textimage.service.model.TextImageInfo;
import com.chenjw.textimage.service.model.TextUrlInfo;

/**
 * 文字图片服务实现，实现了图片的渲染和存储功能
 * 
 * @author chenjw
 */
public interface TextImageService {

	/**
	 * 输入要生成的文字内容，生成文字图片并返回文字元数据，该接口不会通过缓存
	 * 
	 * @param text
	 * @return
	 */
	public TextImageInfo buildImage(Map<String, String> textMap,
			StyleConfig styleConfig) throws TextImageException;

	/**
	 * 根据输入文字查询图片 <br>
	 * 1. 如果图片缓存存在，且版本未变化，直接返回。 <br>
	 * 2. 如果图片缓存不存在或版本已更新，同步重新生成图片后返回（耗时约250毫秒左右), 如果生成图片失败，抛出异常。<br>
	 * 
	 * @param id
	 * @param type
	 * @param map
	 * @return
	 */
	public TextUrlInfo queryUrl(String key, Map<String, String> textMap,
			StyleConfig config) throws TextImageException;

	/**
	 * 刷新文字图片的缓存
	 * 
	 * @param id
	 * @param type
	 */
	public void refreshCache(String key) throws TextImageException;

}
