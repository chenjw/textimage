/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.textimage.service;

import java.io.OutputStream;

import com.chenjw.textimage.exception.TextImageException;

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
