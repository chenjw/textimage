package com.chenjw.textimage.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.RuntimeInstance;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.chenjw.textimage.service.TextImageService;
import com.chenjw.textimage.service.config.StyleConfig;
import com.chenjw.textimage.service.config.parser.ConfigParser;
import com.chenjw.textimage.service.exception.TextImageException;
import com.chenjw.textimage.service.model.TextUrlInfo;

public class TestMain {
	private static StyleConfig readStyleConfig(String path) throws IOException {
		InputStream is = null;
		try {
			is = TestMain.class.getClassLoader().getResourceAsStream(path);
			StyleConfig styleConfig = ConfigParser.parse(is);
			return styleConfig;
		} finally {
			is.close();
		}
	}

	@SuppressWarnings("unchecked")
	private static Map<String, String> readData(String path) throws IOException {
		InputStream is = null;
		Map<String, String> data = new HashMap<String, String>();
		try {
			is = TestMain.class.getClassLoader().getResourceAsStream(path);
			Properties properties = new Properties();
			properties.load(is);

			for (Object o : properties.entrySet()) {
				Entry<String, String> entry = (Entry<String, String>) o;
				data.put(entry.getKey(), entry.getValue());
			}
		} finally {
			is.close();
		}
		return data;
	}

	private static void buildHtml(TextUrlInfo textUrl) {
		for (String url : textUrl.getUrls()) {
			System.out.println(url);
		}
		VelocityContext context = new VelocityContext();
		context.put("textUrlInfo", textUrl);
		Writer writer = null;
		InputStream in = null;
		try {
			File f = File.createTempFile("textimage", ".html");
			writer = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");

			in = TestMain.class.getClassLoader().getResourceAsStream(
					"com/chenjw/textimage/test/template/test.vm");
			render(context, writer, "textimage", IOUtils.toString(in, "UTF-8"));
			System.out.println(f.getAbsolutePath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				writer.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static boolean render(Context context, Writer writer,
			String logTag, String str) throws IOException {
		RuntimeInstance instance = new RuntimeInstance();
		SimpleNode nodeTree = null;
		try {
			nodeTree = instance.parse(new StringReader(str), logTag);
		} catch (ParseException pex) {
			throw new ParseErrorException(pex);
		} catch (TemplateInitException pex) {
			throw new ParseErrorException(pex);
		}
		if (nodeTree == null)
			return false;
		else
			return instance.render(context, writer, logTag, nodeTree);
	}

	public static void main(String[] args) throws IOException,
			TextImageException {
		XmlWebApplicationContext context = new XmlWebApplicationContext();
		context.setConfigLocation("classpath:com/chenjw/textimage/test/context.xml");
		context.refresh();
		StyleConfig styleConfig = readStyleConfig("com/chenjw/textimage/test/style_config.xml");
		Map<String, String> data = readData("com/chenjw/textimage/test/data.properties");
		TextImageService textImageService = (TextImageService) context
				.getBean("textImageService");
		TextUrlInfo textUrl = textImageService.queryUrl("123", data,
				styleConfig);
		buildHtml(textUrl);
	}
}
