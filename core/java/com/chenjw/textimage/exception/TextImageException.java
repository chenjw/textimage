/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.textimage.exception;

/**
 * 文字图片异常
 * 
 * @author chenjw 2011-11-11 上午1:09:14
 */

public class TextImageException extends Exception {

	private static final long serialVersionUID = 2595430636404345331L;

	public TextImageException() {
		super();
	}

	public TextImageException(String message) {
		super(message);
	}

	public TextImageException(String message, Throwable cause) {
		super((cause instanceof TextImageException) ? message + ":"
				+ cause.getLocalizedMessage() : message,
				(cause instanceof TextImageException) ? cause.getCause()
						: cause);
	}

	public TextImageException(Throwable cause) {
		super((cause instanceof TextImageException) ? cause.getCause() : cause);
	}
}
