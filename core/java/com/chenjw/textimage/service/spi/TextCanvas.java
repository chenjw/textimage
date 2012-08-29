package com.chenjw.textimage.service.spi;

import java.io.OutputStream;

import com.chenjw.textimage.service.exception.TextImageException;

/**
 * 画布接口
 * 
 * @author chenjw 2011-11-14 下午4:59:47
 */
public interface TextCanvas {

	/**
	 * 生成图片
	 * 
	 * @param os
	 */
	public void buildImage(OutputStream os) throws TextImageException;

}
