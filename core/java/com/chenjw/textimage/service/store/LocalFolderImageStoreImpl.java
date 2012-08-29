package com.chenjw.textimage.service.store;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.chenjw.textimage.service.exception.TextImageException;
import com.chenjw.textimage.service.spi.TextImageStore;

/**
 * 使用临时文件夹存放生成的图片，可以用于本地测试
 * 
 * @author chenjw
 * 
 */
public class LocalFolderImageStoreImpl implements TextImageStore {

	@Override
	public String storeImage(byte[] bytes) throws TextImageException {
		File f = null;
		try {
			f = File.createTempFile("textimage", ".png");
			FileUtils.writeByteArrayToFile(f, bytes);
			return "file://" + f.getAbsolutePath();
		} catch (IOException e) {
			throw new TextImageException("store file fail");
		}
	}

	@Override
	public void dropImage(String url) throws TextImageException {
		File f = new File(url);
		f.delete();
	}
}
