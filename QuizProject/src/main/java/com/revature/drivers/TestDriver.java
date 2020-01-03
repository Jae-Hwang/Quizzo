package com.revature.drivers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.daos.UserDao;
import com.revature.util.ConnectionUtil;

public class TestDriver {
	
	private static Logger log = LogManager.getRootLogger();
	private static UserDao userDao = UserDao.instance;
			
	
	public static void main(String[] args) {
		log.trace("=======Test Driver=======");
		log.trace("Trace");
		log.warn("Warn");
		
		log.trace(userDao.getUsersByPage(1).toString());
	}
}
