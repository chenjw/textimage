package com.chenjw.tools.project;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

public class ProjectFile {
	private static PathMatcher pathMatcher = new AntPathMatcher();
	Properties prop;
	File baseFolder;
	String currentPack;
	String basePack;
	String fileName;
	String encoding;

	public ProjectFile(Properties prop, File baseFolder, String basePack,
			String currentPack, String fileName, String encoding) {
		this.prop = prop;
		this.baseFolder = baseFolder;
		this.basePack = basePack;
		this.currentPack = currentPack;
		this.fileName = fileName;
		this.encoding = encoding;
	}

	public void copyTo(ProjectFile targetFile) {
		try {
			String fromPath = StringUtils.replace(currentPack, ".", "/") + "/"
					+ fileName;
			String toPath = StringUtils.replace(targetFile.currentPack, ".",
					"/") + "/" + targetFile.fileName;
			File base = new File(baseFolder, fromPath);
			String fileStr = FileUtils.readFileToString(base, encoding);
			fileStr = handle(fileStr, targetFile);
			FileUtils.writeStringToFile(
					new File(targetFile.baseFolder, toPath), fileStr,
					targetFile.encoding);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String handle(String fileStr, ProjectFile targetFile) {
		for (Entry<Object, Object> entry : prop.entrySet()) {
			if (entry.getKey().toString().startsWith("replace_pattern")) {
				String[] configs = StringUtils.split(StringUtils
						.substringAfter(entry.getKey().toString(),
								"replace_pattern"), "$$");
				String pattern = configs[0];
				String fromPath = StringUtils.replace(currentPack, ".", "/")
						+ "/" + fileName;
				if (!pathMatcher.match(pattern, fromPath)) {
					continue;
				}
				String[] pair = entry.getValue().toString().split(":");
				fileStr = StringUtils.replace(fileStr, pair[0], pair[1]);
			}

		}
		return StringUtils.replace(fileStr, basePack, targetFile.basePack);
	}

	public static void main(String[] args) {
		PathMatcher pathMatcher = new AntPathMatcher();
		System.out.println(pathMatcher.match("*", "ccc.dd"));
	}
}
