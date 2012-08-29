package com.chenjw.textimage.service.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 某一个字段的元数据<br>
 * 每个字段都可以设置自己的最大宽度，超过最大宽度后会根据最大宽度分割成多个TextLine <br>
 * 如无需分割，请不要设置样式中的lineWidth属性
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
