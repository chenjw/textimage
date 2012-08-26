/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.textimage.service.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 文本元数据
 * 
 * @author chenjw
 */
public class TextMetaInfo {

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
