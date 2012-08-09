package com.chenjw.textimage.utils.logger;

import org.apache.commons.logging.LogFactory;

public class LoggerFactory {

	public static Logger getLogger(Class<?> clazz) {
		return new Logger(LogFactory.getLog(clazz));
	}

	public static Logger getLogger(String name) {
		return new Logger(LogFactory.getLog(name));
	}

}
