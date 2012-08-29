package com.chenjw.textimage.service.model;

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
	// 内边距，如果为null表示内边距都为0，该字段为生成图片过程中使用，服务结果返回的模型中这个字段为Null。
	private Padding padding;

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

	public Padding getPadding() {
		return padding;
	}

	public void setPadding(Padding padding) {
		this.padding = padding;
	}

}
