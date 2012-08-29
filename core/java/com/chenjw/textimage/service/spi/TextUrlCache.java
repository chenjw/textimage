package com.chenjw.textimage.service.spi;

/**
 * 图像元数据的缓存
 * 
 * @author chenjw
 * 
 */
public interface TextUrlCache {

	/**
	 * 不存在返回null
	 * 
	 * @param key
	 * @return
	 */
	public String getTextUrl(String key);

	public void putTextUrl(String key, String value);
}
