package com.chenjw.textimage.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.chenjw.logger.Logger;
import com.chenjw.textimage.service.TextImageService;
import com.chenjw.textimage.service.config.StyleConfig;
import com.chenjw.textimage.service.config.TextStyle;
import com.chenjw.textimage.service.constants.TextImageConstants;
import com.chenjw.textimage.service.exception.TextImageException;
import com.chenjw.textimage.service.graphic.java2d.PaintEnvironment;
import com.chenjw.textimage.service.model.TextImageContext;
import com.chenjw.textimage.service.model.TextImageInfo;
import com.chenjw.textimage.service.model.TextMetaInfo;
import com.chenjw.textimage.service.model.TextUrlInfo;
import com.chenjw.textimage.service.spi.TextImageBuilder;
import com.chenjw.textimage.service.spi.TextImagePainter;
import com.chenjw.textimage.service.spi.TextImageStore;
import com.chenjw.textimage.service.spi.TextUrlCache;
import com.chenjw.textimage.service.utils.Profiler;
import com.chenjw.textimage.service.utils.TextImageUtils;

/**
 * 文字图片服务（对外暴露）实现
 * 
 * @author chenjw
 */
public class TextImageServiceImpl implements TextImageService {
	private final static Logger LOGGER = TextImageConstants.LOGGER;
	// 图片文字元数据缓存
	private TextUrlCache textUrlCache;
	// 图片银行上传服务
	private TextImageStore testImageStore;
	// 图像构建器
	private TextImageBuilder textImageBuilder;
	// 图像绘制器
	private TextImagePainter textImagePainter;

	public TextImageInfo buildImage(Map<String, String> textMap,
			StyleConfig styleConfig) throws TextImageException {
		try {
			verifyAndPrepareParam(textMap, styleConfig);
			return internalbuildImage(textMap, styleConfig);
		} catch (TextImageException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error("生成图片时出错:", e);
			throw new TextImageException("生成图片时出错:", e);
		}
	}

	public TextUrlInfo queryUrl(String key, Map<String, String> textMap,
			StyleConfig styleConfig) throws TextImageException {
		try {
			// 验证参数
			verifyAndPrepareParam(textMap, styleConfig);
			// 生成版本号
			String version = TextImageUtils.getVersion(textMap,
					styleConfig.getStyleVersion());
			// 从缓存中查询文字图片元数据
			TextUrlInfo textInfo = queryTextUrlFromCache(key);
			if (textInfo != null) {
				// 判断取出的元数据是否是当前版本的缓存
				if (isCachedVersion(textInfo, version)) {
					return textInfo;
				}
			}
			// 创建图片
			return this.buildImage(key, version, textMap, styleConfig);
		} catch (TextImageException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error("查询或生成图片时出错:", e);
			throw new TextImageException("查询或生成图片时出错:", e);
		}
	}

	@Override
	public void refreshCache(String key) throws TextImageException {
		// 清空版本信息
		cacheTextUrl(key, null);
	}

	/**
	 * 从缓存中取得元数据
	 * 
	 * @param memberId
	 * @return
	 */
	private TextUrlInfo queryTextUrlFromCache(String key) {
		if (textUrlCache == null) {
			return null;
		}
		String str = textUrlCache.getTextUrl(key);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[cache]get " + key + " " + str);
		}
		if (StringUtils.isBlank(str)) {
			return null;
		}
		return TextImageUtils.unmarshalTextUrlInfo(str);
	}

	/**
	 * 缓存元数据
	 * 
	 * @param key
	 * @param textUrl
	 */
	private void cacheTextUrl(String key, TextUrlInfo textUrl) {
		if (textUrlCache == null) {
			return;
		}
		String str = TextImageUtils.marshalTextUrlInfo(textUrl);
		textUrlCache.putTextUrl(key, str);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("[cache]put " + key + " " + str);
		}
	}

	/**
	 * 判断缓存的元数据和传入的版本号是否一致
	 * 
	 * @param textInfo
	 * @param version
	 * @return
	 */
	private boolean isCachedVersion(TextUrlInfo textUrl, String version) {
		TextMetaInfo textInfo = textUrl.getTextMetaInfo();
		return StringUtils.equals(textInfo.getVersion(), version);
	}

	/**
	 * 验证输入参数
	 * 
	 * @param id
	 * @param type
	 * @param textMap
	 * @param styleConfig
	 * @throws TextImageException
	 */
	private void verifyAndPrepareParam(Map<String, String> textMap,
			StyleConfig styleConfig) throws TextImageException {
		// 验证textMap
		if (textMap == null || textMap.size() == 0) {
			throw new TextImageException("textMap cant be empty!");
		}
		// 如果未传入样式，创建默认样式
		if (styleConfig == null) {
			throw new TextImageException("styleConfig cant be null ");
		} else {
			// 验证styleConfig
			for (TextStyle style : styleConfig.getClassMap().values()) {
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
	 * 生成图片，如果失败抛出异常
	 * 
	 * @param key
	 * @param version
	 * @param textMap
	 * @param styleConfig
	 * @return
	 * @throws TextImageException
	 */
	private TextUrlInfo buildImage(String key, String version,
			Map<String, String> textMap, StyleConfig styleConfig)
			throws TextImageException {

		// 从缓存中查询原有的文字图片元数据
		TextUrlInfo oldTextUrl = queryTextUrlFromCache(key);
		// 生成图片前再确认下是否有缓存，这样同一虚拟机内不会有并发更新缓存的问题了，多机器间还会存在，有很小几率会生成最多2（tyr部署机器数量）次图片，但不会影响数据准确性。
		// 如果老版本和当前创建的版本一致，说明之前已经被创建过了，直接返回结果
		if (oldTextUrl != null && this.isCachedVersion(oldTextUrl, version)) {
			return oldTextUrl;
		}
		// 创建新版本数据
		else {
			if (LOGGER.isDebugEnabled()) {
				Profiler.getInstance().begin();
			}
			List<String> oldUrlList = null;
			if (oldTextUrl != null) {
				oldUrlList = oldTextUrl.getUrls();
			}
			TextImageInfo textImage = null;
			// 生成图片
			textImage = this.internalbuildImage(textMap, styleConfig);
			if (textImage == null) {
				throw new TextImageException("图片构建失败!" + textMap);
			}
			TextMetaInfo textMetaInfo = textImage.getTextMetaInfo();
			// 设置版本号
			textMetaInfo.setVersion(version);
			// 存储图片
			List<String> urls = new ArrayList<String>();
			for (byte[] bytes : textImage.getImageList()) {
				String url = testImageStore.storeImage(bytes);
				urls.add(url);
			}
			TextUrlInfo textUrlInfo = new TextUrlInfo();
			textUrlInfo.setTextMetaInfo(textMetaInfo);
			textUrlInfo.setUrls(urls);
			// 更新元数据缓存
			cacheTextUrl(key, textUrlInfo);
			// 删除老图片
			if (oldUrlList != null) {
				// 删除图片
				for (String url : oldUrlList) {
					try {
						testImageStore.dropImage(url);
					} catch (Exception e) {
						LOGGER.error("drop image fail, url=" + url, e);
					}

				}
			}
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("build textimage finished, use "
						+ Profiler.getInstance().getMillisInterval() + " ms");
			}
			return textUrlInfo;
		}
	}

	/**
	 * 输入要生成的文字内容，生成文字图片并返回文字元数据
	 * 
	 * @param text
	 * @return
	 * @throws TextImageException
	 */
	private TextImageInfo internalbuildImage(Map<String, String> textMap,
			StyleConfig styleConfig) throws TextImageException {

		TextImageContext context = initContext(textMap, styleConfig);
		// 构建图片结构
		textImageBuilder.buildImage(context, textImagePainter);
		TextImageInfo textImage = new TextImageInfo();
		textImage.setImageList(context.getImageList());
		textImage.setTextMetaInfo(context.getTextInfo());
		return textImage;
	}

	private TextImageContext initContext(Map<String, String> textMap,
			StyleConfig config) {
		TextImageContext context = new TextImageContext();
		context.setTextMap(textMap);
		context.setStyleConfig(config);
		return context;
	}

	public void setTextImageBuilder(TextImageBuilder textImageBuilder) {
		this.textImageBuilder = textImageBuilder;
	}

	public void setTextImagePainter(TextImagePainter textImagePainter) {
		this.textImagePainter = textImagePainter;
	}

	public void setTextUrlCache(TextUrlCache textUrlCache) {
		this.textUrlCache = textUrlCache;
	}

	public void setTestImageStore(TextImageStore testImageStore) {
		this.testImageStore = testImageStore;
	}

}
