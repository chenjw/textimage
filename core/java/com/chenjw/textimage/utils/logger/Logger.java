package com.chenjw.textimage.utils.logger;

import org.apache.commons.logging.Log;

public class Logger {
	public Logger(Log log) {

	}

	public void trace(Object key, Object[] params) {
		System.out.println(key);
	}

	public void trace(Object key, Object[] params, Throwable cause) {
		System.out.println(key);
	}

	public void debug(Object key, Object[] params) {
		System.out.println(key);
	}

	public boolean isDebugEnabled() {
		return true;
	}

	public void debug(Object key, Object[] params, Throwable cause) {
		System.out.println(key);
	}

	public void info(Object key, Object[] params) {
		System.out.println(key);
	}

	public void info(Object key, Object[] params, Throwable cause) {
		System.out.println(key);
	}

	public void warn(Object key, Object[] params) {
		System.out.println(key);
	}

	public void warn(Object key, Object[] params, Throwable cause) {
		System.out.println(key);
	}

	public void error(Object key, Object[] params) {
		System.out.println(key);
	}

	public void error(Object key, Object[] params, Throwable cause) {
		System.out.println(key);
	}

	public void fatal(Object key, Object[] params) {
		System.out.println(key);
	}

	public void fatal(Object key, Object[] params, Throwable cause) {
		System.out.println(key);
	}

	// /////////

	public void trace(Object key) {
		System.out.println(key);
	}

	public void trace(Object key, Throwable cause) {
		System.out.println(key);
	}

	public void debug(Object key) {
		System.out.println(key);
	}

	public void debug(Object key, Throwable cause) {
		System.out.println(key);
	}

	public void info(Object key) {
		System.out.println(key);
	}

	public void info(Object key, Throwable cause) {
		System.out.println(key);
	}

	public void warn(Object key) {
		System.out.println(key);
	}

	public void warn(Object key, Throwable cause) {
		System.out.println(key);
	}

	public void error(Object key) {
		System.out.println(key);
	}

	public void error(Object key, Throwable cause) {
		System.out.println(key);
	}

	public void fatal(Object key) {
		System.out.println(key);
	}

	public void fatal(Object key, Throwable cause) {
		System.out.println(key);
	}

}
