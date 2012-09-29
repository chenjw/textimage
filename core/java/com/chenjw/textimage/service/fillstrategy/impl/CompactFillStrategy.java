package com.chenjw.textimage.service.fillstrategy.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.chenjw.logger.Logger;
import com.chenjw.textimage.service.config.StyleConfig;
import com.chenjw.textimage.service.config.StyleConfigHelper;
import com.chenjw.textimage.service.constants.TextImageConstants;
import com.chenjw.textimage.service.fillstrategy.FillStrategy;
import com.chenjw.textimage.service.fillstrategy.result.CanvasInfo;
import com.chenjw.textimage.service.fillstrategy.result.FillResult;
import com.chenjw.textimage.service.model.TextField;
import com.chenjw.textimage.service.model.TextLine;
import com.chenjw.textimage.service.model.TextMetaInfo;

/**
 * 紧凑型的填充算法 创建队列来管理空闲块，目前用的是這個算法，主要逻辑:<br>
 * 1. 查询“空闲块链表”。<br>
 * 2. 如果“空闲块链表”中查不到，申请一整行。<br>
 * 3. 使用剩下的空间重新放入“空闲块链表”。<br>
 * 4. 行高，按照“最大块行高”，“最大块行高/2”，“最大块行高/4”，“最大块行高/8”。。。的规则来分。申请块时自动匹配到相应行高区间，
 * 如指定区间没有空闲块，就从上一级区间找，如果递归到最高级空间都没有就重新申请行。<br>
 * 
 * @author chenjw 2011-11-14 下午5:01:53
 */
public class CompactFillStrategy implements FillStrategy {

	private final static Logger LOGGER = TextImageConstants.LOGGER;

	public FillResult tryFillCanvas(TextMetaInfo textInfo,
			StyleConfig styleConfig) {
		FillResult result = new FillResult();
		int maxCanvasHeight = styleConfig.getCanvasHeight();
		// 所有textLine取出来，算出最高的
		// 在这里用set过滤掉指向相同文本的textLine，相同文字的图片块在画布上只绘制一次
		Set<TextLine> textLineList = new HashSet<TextLine>();
		int maxLineHeight = Integer.MIN_VALUE;
		for (Entry<String, TextField> textFieldentry : textInfo
				.getTextFieldMap().entrySet()) {
			for (TextLine textLine : textFieldentry.getValue().getTextLines()) {
				if (textLine.getHeight() > maxLineHeight) {
					maxLineHeight = textLine.getHeight();
				}
				textLineList.add(textLine);
			}
		}
		// 按高度区间分类，每个区间最高为上一区间最高值的一半
		Map<Integer, List<TextLine>> textLineListMap = new HashMap<Integer, List<TextLine>>();
		for (TextLine textLine : textLineList) {
			int height = maxLineHeight;
			while (textLine.getHeight() <= height / 2) {
				height = height / 2;
			}
			List<TextLine> list = textLineListMap.get(height);
			if (list == null) {
				list = new ArrayList<TextLine>();
				textLineListMap.put(height, list);
			}

			list.add(textLine);
		}
		// 为每个区间排序
		for (Entry<Integer, List<TextLine>> entry : textLineListMap.entrySet()) {
			Collections.sort(entry.getValue(), new Comparator<TextLine>() {

				@Override
				public int compare(TextLine o1, TextLine o2) {
					return o2.getWidth() - o1.getWidth();
				}
			});
		}
		// 算画布宽度
		int maxWidth = _calculateMaxWidth(textInfo, styleConfig);
		// 填充
		List<CanvasInfo> canvasList = _fill(textLineListMap, maxLineHeight,
				maxWidth, maxCanvasHeight);
		result.setCanvasInfoList(canvasList);
		result.setTextInfo(textInfo);
		return result;
	}

	/**
	 * 计算画布宽度，算法为： 1.计算出现最多次数的行限定宽度 2.计算小于画布限宽的出现最多行宽整数倍的值
	 * 
	 * @param textInfo
	 * @param styleConfig
	 * @return
	 */
	private int _calculateMaxWidth(TextMetaInfo textInfo,
			StyleConfig styleConfig) {
		// 1.遍历所有行，计算出现次数最多的行最大宽度值
		Map<Integer, Integer> countMap = new HashMap<Integer, Integer>();
		for (Entry<String, TextField> textFieldentry : textInfo
				.getTextFieldMap().entrySet()) {
			String key = textFieldentry.getKey();
			int lineWidth = StyleConfigHelper.getTextStyle(styleConfig, key)
					.getLineWidth();
			TextField textField = textFieldentry.getValue();
			Integer count = countMap.get(lineWidth);
			if (count == null) {
				count = 0;
			}
			count += textField.getTextLines().size();
			countMap.put(lineWidth, count);
		}
		int mostLineWidth = 0;
		int maxCountLineWidth = Integer.MIN_VALUE;
		for (Entry<Integer, Integer> entry : countMap.entrySet()) {
			if (entry.getValue() > maxCountLineWidth) {
				maxCountLineWidth = entry.getValue();
				mostLineWidth = entry.getKey();
			}
		}
		// 2.计算小于画布限宽的出现最多行宽整数倍的值
		return styleConfig.getCanvasWidth() / mostLineWidth * mostLineWidth;
	}

	/**
	 * 填充画布
	 * 
	 * @param textLineListMap
	 * @param maxLineHeight
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	private List<CanvasInfo> _fill(
			Map<Integer, List<TextLine>> textLineListMap, int maxLineHeight,
			int maxWidth, int maxHeight) {

		int height = maxLineHeight;
		// 当前位置
		int nY = 0;
		// 用来放空闲块的链表
		FreeBlockList freeBlockList = new FreeBlockList();
		List<CanvasInfo> canvasList = new ArrayList<CanvasInfo>();
		for (;;) {
			List<TextLine> list = textLineListMap.get(height);
			if (list == null) {
				break;
			} else {
				for (TextLine textLine : list) {
					// 设置
					Point p = freeBlockList.getBlock(textLine.getWidth(),
							textLine.getHeight());
					if (p == null) {
						if (nY + textLine.getHeight() > maxHeight) {
							CanvasInfo canvasInfo = new CanvasInfo();
							canvasInfo.setWidth(maxWidth);
							canvasInfo.setHeight(nY);
							canvasList.add(canvasInfo);
							nY = 0;
						}
						textLine.setX(0);
						textLine.setY(nY);
						textLine.setImageIndex(canvasList.size());
						if (textLine.getWidth() < maxWidth) {
							freeBlockList.addBlock(textLine.getWidth(), nY,
									maxWidth - textLine.getWidth(),
									textLine.getHeight(), canvasList.size());
						}
						nY += textLine.getHeight();
					} else {
						textLine.setX(p.x);
						textLine.setY(p.y);
						textLine.setImageIndex(p.index);
					}
				}
				height = height / 2;
			}
		}
		CanvasInfo canvasInfo = new CanvasInfo();
		canvasInfo.setWidth(maxWidth);
		canvasInfo.setHeight(nY);
		canvasList.add(canvasInfo);
		return canvasList;
	}

	/**
	 * 用来放置空间块的链表，按照高度、行宽的顺序排列
	 * 
	 * @author chenjw 2011-11-11 下午5:32:18
	 */
	private class FreeBlockList {

		private LinkedList<Block> list = new LinkedList<Block>();

		/**
		 * 在链表上添加块
		 * 
		 * @param x
		 * @param y
		 * @param width
		 * @param height
		 * @param index
		 */
		public void addBlock(int x, int y, int width, int height, int index) {
			ListIterator<Block> listIterator = list.listIterator();
			while (listIterator.hasNext()) {
				Block b = listIterator.next();
				if (b.height > height) {
					listIterator.previous();
					listIterator.add(new Block(x, y, width, height, index));
					print();
					return;
				} else if (b.height == height) {
					if (b.width >= width) {
						listIterator.previous();
						listIterator.add(new Block(x, y, width, height, index));
						print();
						return;
					}
				}
			}
			listIterator.add(new Block(x, y, width, height, index));
			print();

		}

		/**
		 * 打印块信息
		 */
		private void print() {
			if (LOGGER.isDebugEnabled()) {
				StringBuffer sb = new StringBuffer();
				ListIterator<Block> i = list.listIterator();
				while (i.hasNext()) {
					Block b = i.next();
					sb.append("(x:" + b.x + ",y:" + b.y + ",w:" + b.width
							+ ",h:" + b.height + "),");
				}
				LOGGER.debug(sb.toString());
			}
		}

		public Point getBlock(int width, int height) {
			ListIterator<Block> listIterator = list.listIterator();
			while (listIterator.hasNext()) {
				Block b = listIterator.next();
				if (b.height >= height && b.width >= width) {
					Point p = new Point(b.x, b.y, b.index);
					listIterator.previous();
					listIterator.remove();
					if (b.width - width > 0) {
						// 右边
						addBlock(b.x + width, b.y, b.width - width, b.height,
								b.index);
					}
					if (b.height - height > 0) {
						addBlock(b.x, b.y + height, width, b.height - height,
								b.index);
					}

					return p;
				}
			}
			return null;
		}

	}

	private static class Block {

		Block(int x, int y, int width, int height, int index) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.index = index;
		}

		int x;
		int y;
		int width;
		int height;
		int index;
	}

	private static class Point {

		Point(int x, int y, int index) {
			this.x = x;
			this.y = y;
			this.index = index;
		}

		int x;
		int y;
		int index;
	}
}
