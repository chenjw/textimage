/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.textimage.service;

import com.chenjw.textimage.exception.TextImageException;

/**
 * 文字图片构建器接口，实现了文字和画布上构建的逻辑
 * 
 * @author chenjw 2011-11-14 下午5:06:42
 */
public interface TextImageBuilder {

	public void buildImage(TextImageContext context, Painter painter)
			throws TextImageException;

}
