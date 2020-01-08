package com.revature.drivers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.daos.GroupDao;
import com.revature.daos.QuestionDao;
import com.revature.daos.UserDao;
import com.revature.util.ConnectionUtil;

public class TestDriver {
	
	private static Logger log = LogManager.getRootLogger();
	private static UserDao userDao = UserDao.instance;
	private static QuestionDao questionDao = QuestionDao.instance;
	private static GroupDao groupDao = GroupDao.instance;
			
	
	public static void main(String[] args) {
		log.trace("=======Test Driver=======");
		log.trace("=======Users Test=======");
		
		if (!ConnectionUtil.heartbeat()) {
			return;
		}

		log.trace(userDao.getUsersByPage(1).toString());
		log.trace(userDao.getUsersByPage(2).toString());
		log.trace(userDao.getUsersByPage(3).toString());
		log.trace(userDao.getUsersByPage(4).toString());
		log.trace(userDao.getUsersByPage(5).toString());
		
		// log.trace(userDao.registerUser("test1", "pass", "Te", "St"));
		// log.trace(userDao.registerManager("TestAdmin", "adm", "adm", "adm"));

		log.trace("=======Questions Test=======");
		log.trace(questionDao.getQuestionsByPage(1).toString());
		log.trace(questionDao.getQuestionsByQuizId(15).toString());
		
		// log.trace(questionDao.createQuestionMulti("Which one is 3rd one?", "s1", "s2", "s3", "s4", "s5", "Test", 50, 3));
		// log.trace(questionDao.createQuestionTrueFalse("Which one is false?", "Test", 50, false));
		// log.trace(questionDao.createQuestionShort("What is the name of THE trainer?", "Test", 50, "Williams"));
		
		log.trace(questionDao.getQuestionsByPage(1).toString());
		log.trace(questionDao.getQuestionsByPage(2).toString());
		log.trace(questionDao.getQuestionsByPage(3).toString());
		
		log.trace("=======Groups Test=======");
		log.trace(groupDao.getGroupsByPage(1).toString());
		log.trace(groupDao.getGroupsByPage(2).toString());
		log.trace(groupDao.getGroupsByPage(3).toString());
		
		log.trace(groupDao.getGroupsByUserId(29).toString());
		
		log.trace(groupDao.createGroup("Driver Test Group 4"));
		
		log.trace(groupDao.assignUserToGroup(31, 21));
		log.trace(groupDao.getGroupsByUserId(31).toString());
		log.trace(groupDao.unassignUserFromGroup(31, 21));
		log.trace(groupDao.getGroupsByUserId(31).toString());
		
		
		
		if (!ConnectionUtil.heartbeat()) {
			return;
		}
	}
}
