package com.chenjw.textimage.service.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.chenjw.textimage.service.TextImageStore;

public class LocalFolderImageStore implements TextImageStore {
	private int i = 0;

	@Override
	public String storeImage(byte[] bytes) {
		i++;
		try {
			File f = File.createTempFile("textimage", i + ".png");
			FileUtils.writeByteArrayToFile(f, bytes);
			return "file://" + f.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public void dropImage(String url) {
		File f = new File(url);
		f.delete();
	}
}
