package com.chenjw.textimage.service.spi;

import com.chenjw.textimage.service.exception.TextImageException;
import com.chenjw.textimage.service.model.TextImageContext;

/**
 * 文字图片构建器接口，实现了文字和画布上构建的逻辑
 * 
 * @author chenjw 2011-11-14 下午5:06:42
 */
public interface TextImageBuilder {

	public void buildImage(TextImageContext context, TextImagePainter painter)
			throws TextImageException;

}
