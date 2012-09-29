package com.chenjw.textimage.service;

import java.util.Map;

import com.chenjw.textimage.service.config.StyleConfig;
import com.chenjw.textimage.service.exception.TextImageException;
import com.chenjw.textimage.service.model.TextImageInfo;
import com.chenjw.textimage.service.model.TextUrlInfo;

/**
 * 文字图片服务实现，实现了图片的渲染和存储功能
 * 
 * @author chenjw
 */
public interface TextImageService {

	/**
	 * 生成图片信息
	 * 
	 * @param textMap
	 * @param styleConfig
	 * @return
	 * @throws TextImageException
	 */
	public TextImageInfo buildImage(Map<String, String> textMap,
			StyleConfig styleConfig) throws TextImageException;

	/**
	 * 根据输入文字查询图片 <br>
	 * 1. 如果图片缓存存在，且版本未变化，直接返回。 <br>
	 * 2. 如果图片缓存不存在或版本已更新，同步重新生成图片后返回（耗时约250毫秒左右), 如果生成图片失败，抛出异常。<br>
	 * 
	 * @param key
	 * @param textMap
	 * @param styleConfig
	 * @return
	 */
	public TextUrlInfo queryUrl(String key, Map<String, String> textMap,
			StyleConfig config) throws TextImageException;

	/**
	 * 刷新文字图片的缓存
	 * 
	 * @param key
	 */
	public void refreshCache(String key) throws TextImageException;

}
