package com.revature.drivers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestDriver {
	
	private static Logger log = LogManager.getRootLogger();
			
	
	public static void main(String[] args) {
		log.trace("=======Test Driver=======");
		log.trace("Trace");
		log.warn("Warn");
	}
}
