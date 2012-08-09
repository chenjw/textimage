/*
 * Copyright 1999-2011 Alibaba.com All right reserved. This software is the confidential and proprietary information of
 * Alibaba.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Alibaba.com.
 */
package com.chenjw.textimage.model;

/**
 * 某一行文字图片的元数据
 * 
 * @author chenjw
 */
public class TextLine {

	// 图片索引
	private int imageIndex;
	// x轴偏移位置
	private int x;
	// y轴偏移位置
	private int y;
	// 宽度
	private int width;
	// 高度
	private int height;

	public TextLine clone() {
		TextLine newTextLine = new TextLine();
		newTextLine.imageIndex = this.imageIndex;
		newTextLine.x = this.x;
		newTextLine.y = this.y;
		newTextLine.width = this.width;
		newTextLine.height = this.height;
		return newTextLine;
	}

	public int getImageIndex() {
		return imageIndex;
	}

	public void setImageIndex(int imageIndex) {
		this.imageIndex = imageIndex;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
