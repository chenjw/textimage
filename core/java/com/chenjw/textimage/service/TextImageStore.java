package com.chenjw.textimage.service;

public interface TextImageStore {
	public String storeImage(byte[] bytes);

	public void dropImage(String url);
}
