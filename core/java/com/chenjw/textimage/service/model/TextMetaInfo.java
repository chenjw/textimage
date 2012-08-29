package com.chenjw.textimage.service.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 文字图片的元数据
 * 
 * @author chenjw
 */
public class TextMetaInfo {
	// 字段映射，从key到这个Key对应的textField
	private Map<String, TextField> textFieldMap = new HashMap<String, TextField>();

	private String version;

	public Map<String, TextField> getTextFieldMap() {
		return textFieldMap;
	}

	public void setTextFieldMap(Map<String, TextField> textFieldMap) {
		this.textFieldMap = textFieldMap;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
