package com.chenjw.textimage.service.config.parser;

import java.awt.Color;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.chenjw.textimage.service.config.StyleConfig;
import com.chenjw.textimage.service.config.TextPosition;
import com.chenjw.textimage.service.config.TextStyle;
import com.chenjw.textimage.service.config.constants.HAlignEnum;
import com.chenjw.textimage.service.config.constants.VAlignEnum;

/**
 * 用于解析xml格式的样式配置
 * 
 * @author chenjw 2012-8-27 下午4:47:46
 */
public class ConfigParser {
	public static StyleConfig parse(InputStream is) {
		try {
			StyleConfig styleConfig = new StyleConfig();
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			documentBuilderFactory.setValidating(false);
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();
			Document doc = documentBuilder.parse(is);
			parseConfig(styleConfig, doc.getDocumentElement());
			parseClasses(styleConfig, doc.getDocumentElement());
			parseClassMappings(styleConfig, doc.getDocumentElement());
			parsePositions(styleConfig, doc.getDocumentElement());
			return styleConfig;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static void parseConfig(StyleConfig styleConfig, Element doc) {
		NodeList list = doc.getElementsByTagName("is-fix-size");

		if (list.getLength() > 0) {
			styleConfig.setIsFixSize(Boolean.parseBoolean(list.item(0)
					.getTextContent()));
		}
		list = doc.getElementsByTagName("canvas-width");
		if (list.getLength() > 0) {
			styleConfig.setCanvasWidth(Integer.parseInt(list.item(0)
					.getTextContent()));
		}

		list = doc.getElementsByTagName("canvas-height");
		if (list.getLength() > 0) {
			styleConfig.setCanvasHeight(Integer.parseInt(list.item(0)
					.getTextContent()));
		}
		list = doc.getElementsByTagName("background-color");
		if (list.getLength() > 0) {
			styleConfig.setBackgroundColor(parseColor(list.item(0)
					.getTextContent()));
		}
		list = doc.getElementsByTagName("background-image");
		if (list.getLength() > 0) {
			String url = list.item(0).getTextContent();
			InputStream is = ConfigParser.class.getResourceAsStream(url);
			try {
				styleConfig.setBackgroundImage(IOUtils.toByteArray(is));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(is);
			}
		}
	}

	private static void parseClasses(StyleConfig styleConfig, Element doc) {
		NodeList list = doc.getElementsByTagName("class");
		for (int i = 0; i < list.getLength(); i++) {
			String id = list.item(i).getAttributes().getNamedItem("id")
					.getTextContent();
			TextStyle textStyle = parseTextStyle(list.item(i));
			if ("default".equals(id)) {
				styleConfig.setDefaultStyle(textStyle);
			} else {
				styleConfig.addClass(id, textStyle);
			}
		}
	}

	private static void parseClassMappings(StyleConfig styleConfig, Element doc) {
		NodeList list = doc.getElementsByTagName("mapping");
		for (int i = 0; i < list.getLength(); i++) {
			String key = list.item(i).getAttributes().getNamedItem("key")
					.getTextContent();
			String ref = list.item(i).getAttributes().getNamedItem("class")
					.getTextContent();
			styleConfig.addClassMapping(key, ref);
		}
	}

	private static void parsePositions(StyleConfig styleConfig, Element doc) {
		NodeList list = doc.getElementsByTagName("position");
		for (int i = 0; i < list.getLength(); i++) {
			TextPosition position = new TextPosition();
			String key = list.item(i).getAttributes().getNamedItem("key")
					.getTextContent();
			if (list.item(i).getAttributes().getNamedItem("x") != null) {
				position.setX(Integer.parseInt(list.item(i).getAttributes()
						.getNamedItem("x").getTextContent()));
			}
			if (list.item(i).getAttributes().getNamedItem("y") != null) {
				position.setY(Integer.parseInt(list.item(i).getAttributes()
						.getNamedItem("y").getTextContent()));
			}

			styleConfig.addTextPosition(key, position);
		}
	}

	private static TextStyle parseTextStyle(Node node) {
		TextStyle style = new TextStyle();

		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			Node n = node.getChildNodes().item(i);
			if ("font-name".equals(n.getNodeName())) {
				style.setFontName(n.getTextContent());
			} else if ("font-size".equals(n.getNodeName())) {
				style.setFontSize(Integer.parseInt(n.getTextContent()));
			} else if ("line-width".equals(n.getNodeName())) {
				style.setLineWidth(Integer.parseInt(n.getTextContent()));
			} else if ("line-height".equals(n.getNodeName())) {
				style.setLineHeight(Integer.parseInt(n.getTextContent()));
			} else if ("line-height".equals(n.getNodeName())) {
				style.setLineHeight(Integer.parseInt(n.getTextContent()));
			} else if ("h-align".equals(n.getNodeName())) {
				style.setHAlign(HAlignEnum.valueOf(n.getTextContent()));
			} else if ("v-align".equals(n.getNodeName())) {
				style.setVAlign(VAlignEnum.valueOf(n.getTextContent()));
			} else if ("is-bold".equals(n.getNodeName())) {
				style.setIsBold(Boolean.parseBoolean(n.getTextContent()));
			} else if ("is-italic".equals(n.getNodeName())) {
				style.setIsItalic(Boolean.parseBoolean(n.getTextContent()));
			} else if ("is-antialias".equals(n.getNodeName())) {
				style.setIsAntialias(Boolean.parseBoolean(n.getTextContent()));
			} else if ("color".equals(n.getNodeName())) {
				Color color = parseColor(n.getTextContent());
				if (color != null) {
					style.setColor(color);
				}
			} else if ("background-color".equals(n.getNodeName())) {
				Color color = parseColor(n.getTextContent());
				if (color != null) {
					style.setBackgroundColor(color);
				}
			}
		}
		return style;
	}

	private static Color parseColor(String str) {
		if (str.indexOf(",") != -1) {
			String[] ss = str.split(",");
			return new Color(Integer.parseInt(ss[0].trim()),
					Integer.parseInt(ss[1].trim()), Integer.parseInt(ss[2]
							.trim()));
		} else {
			if ("black".equals(str)) {
				return Color.black;
			} else if ("blue".equals(str)) {
				return Color.blue;
			} else if ("cyan".equals(str)) {
				return Color.cyan;
			} else if ("darkGray".equals(str)) {
				return Color.darkGray;
			} else if ("gray".equals(str)) {
				return Color.gray;
			} else if ("green".equals(str)) {
				return Color.green;
			} else if ("lightGray".equals(str)) {
				return Color.lightGray;
			} else if ("magenta".equals(str)) {
				return Color.magenta;
			} else if ("orange".equals(str)) {
				return Color.orange;
			} else if ("pink".equals(str)) {
				return Color.pink;
			} else if ("red".equals(str)) {
				return Color.red;
			} else if ("white".equals(str)) {
				return Color.white;
			} else if ("yellow".equals(str)) {
				return Color.yellow;
			}
			return null;
		}
	}
}
