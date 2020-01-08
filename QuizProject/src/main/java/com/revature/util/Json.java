package com.revature.util;

import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {
	private static final ObjectMapper om = new ObjectMapper();
	private static final Logger log = LogManager.getRootLogger();

	public static byte[] write(Object o) {
		try {
			return om.writeValueAsBytes(o);
		} catch (JsonProcessingException e) {
			log.debug("Exception occurred while writing JSON Data");
			log.warn("Stack Trace: ", e);
		}
		return null;
	}

	public static Object read(InputStream is, Class<?> clazz) {
		try {
			return om.readValue(is, clazz);
		} catch (Exception e) {
			log.debug("Exception occurred while reading JSON Data");
			log.warn("Stack Trace: ", e);
		}
		return null;
	}
}
