package com.chenjw.tools.project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public class ProjectPackage {
	Properties prop;
	File baseFolder;
	String currentPack;
	String basePack;
	String packName;
	String encoding;

	public ProjectPackage(Properties prop, File baseFolder, String basePack,
			String currentPack, String encoding) {
		this.prop = prop;
		this.baseFolder = baseFolder;
		this.basePack = basePack;
		this.currentPack = currentPack;
		this.packName = StringUtils.substringAfterLast(currentPack, ".");
		this.encoding = encoding;
	}

	public void copyTo(ProjectPackage targetPack) {
		for (ProjectPackage pack : listPackage()) {
			pack.copyTo(new ProjectPackage(prop, targetPack.baseFolder,
					targetPack.basePack, targetPack.currentPack + "."
							+ pack.packName, targetPack.encoding));
		}
		for (ProjectFile file : listFile()) {
			file.copyTo(new ProjectFile(prop, targetPack.baseFolder,
					targetPack.basePack, targetPack.currentPack, file.fileName,
					targetPack.encoding));
		}
	}

	public List<ProjectPackage> listPackage() {
		List<ProjectPackage> result = new ArrayList<ProjectPackage>();
		for (File f : new File(baseFolder, StringUtils.replace(currentPack,
				".", "/")).listFiles()) {
			if (f.isDirectory()) {
				result.add(new ProjectPackage(prop, baseFolder, basePack,
						currentPack + "." + f.getName(), encoding));
			}
		}
		return result;
	}

	public List<ProjectFile> listFile() {
		List<ProjectFile> result = new ArrayList<ProjectFile>();
		for (File f : new File(baseFolder, StringUtils.replace(currentPack,
				".", "/")).listFiles()) {
			if (!f.isDirectory()) {
				result.add(new ProjectFile(prop, baseFolder, basePack,
						currentPack, f.getName(), encoding));
			}
		}
		return result;
	}
}
