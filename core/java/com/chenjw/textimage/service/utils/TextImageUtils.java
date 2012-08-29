package com.chenjw.textimage.service.utils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.fastjson.JSON;
import com.chenjw.textimage.service.model.TextField;
import com.chenjw.textimage.service.model.TextLine;
import com.chenjw.textimage.service.model.TextMetaInfo;
import com.chenjw.textimage.service.model.TextUrlInfo;

/**
 * 一些工具方法
 * 
 * @author chenjw
 * 
 */
public class TextImageUtils {
	public static TextLine cloneTextLine(TextLine line) {
		TextLine newTextLine = new TextLine();
		newTextLine.setImageIndex(line.getImageIndex());
		newTextLine.setX(line.getX());
		newTextLine.setY(line.getY());
		newTextLine.setWidth(line.getWidth());
		newTextLine.setHeight(line.getHeight());
		return newTextLine;
	}

	/**
	 * md5编码成定长32位字符串
	 * 
	 * @param input
	 * @return
	 */
	public static String encode(String input) {
		try {
			return DigestUtils.md5Hex(input.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public static TextUrlInfo unmarshalTextUrlInfo(String str) {
		return JSON.parseObject(str, TextUrlInfo.class);
	}

	public static String marshalTextUrlInfo(TextUrlInfo textUrl) {
		TextMetaInfo textInfo = textUrl.getTextMetaInfo();
		if (textInfo.getTextFieldMap() != null) {
			for (Entry<String, TextField> entry : textInfo.getTextFieldMap()
					.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().getTextLines() != null) {
					for (int i = 0; i < entry.getValue().getTextLines().size(); i++) {
						TextLine textLine = entry.getValue().getTextLines()
								.get(i);
						entry.getValue().getTextLines()
								.set(i, TextImageUtils.cloneTextLine(textLine));
					}
				}
			}
		}
		String value = JSON.toJSONString(textUrl);
		return value;
	}

	/**
	 * 获得数据的版本编号
	 * 
	 * @param textMap
	 * @return
	 */
	public static String getVersion(Map<String, String> textMap,
			String styleVersion) {
		@SuppressWarnings("unchecked")
		Entry<String, String>[] entrys = textMap.entrySet().toArray(
				new Entry[textMap.size()]);
		Arrays.sort(entrys, new Comparator<Entry<String, String>>() {
			@Override
			public int compare(Entry<String, String> o1,
					Entry<String, String> o2) {
				return o1.getKey().hashCode() >= o2.getKey().hashCode() ? 1
						: -1;
			}
		});
		StringBuffer sb = new StringBuffer();
		sb.append(styleVersion);
		for (Entry<String, String> entry : entrys) {
			sb.append(entry.getKey());
			sb.append(entry.getValue());
		}
		return TextImageUtils.encode(sb.toString());
	}
}
