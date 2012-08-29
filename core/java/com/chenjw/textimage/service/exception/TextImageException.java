package com.chenjw.textimage.service.exception;

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
