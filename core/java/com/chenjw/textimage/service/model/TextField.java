/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.textimage.service.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 文本字段元数据
 * 
 * @author chenjw
 */
public class TextField {

	/**
	 * 某个字段的多行
	 */
	private List<TextLine> textLines = new ArrayList<TextLine>();

	public List<TextLine> getTextLines() {
		return textLines;
	}

	public void setTextLines(List<TextLine> textLines) {
		this.textLines = textLines;
	}

}
