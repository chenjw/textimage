package com.chenjw.textimage.service.spi;

import com.chenjw.textimage.service.exception.TextImageException;

/**
 * 图像存储接口，用于存储图片并返回地址url
 * 
 * @author chenjw
 * 
 */
public interface TextImageStore {
	/**
	 * 存储图像
	 * 
	 * @param bytes
	 * @return
	 */
	public String storeImage(byte[] bytes) throws TextImageException;

	/**
	 * 删除已存储的图像
	 * 
	 * @param url
	 */
	public void dropImage(String url) throws TextImageException;
}
