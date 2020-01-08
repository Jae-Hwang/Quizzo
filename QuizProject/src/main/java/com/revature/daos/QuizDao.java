package com.revature.daos;

import java.util.List;

import com.revature.models.Quiz;

public interface QuizDao {
	
	public static final QuizDao instance = new QuizDaoImpl();
	
	List<Quiz> getQuizzesByPage(int page);
	
	List<Quiz> getQuizzesByUserId(int userid);
	
	List<Quiz> getQuizzesByGroupId(int gid);
	
	boolean createQuiz(String name, int createdBy);
	
	boolean assignQuizToUser(int userid, int qid);
	
	boolean unassignQuizFromUser(int userid, int qid);
	
	boolean assignQuizToGroup(int gid, int qid);
	
	boolean unassignQuizFromGroup(int gid, int qid);
	
	boolean assignQuestionToQuiz(int quid, int qid);
	
	boolean unassignQuestionFromQuiz(int quid, int qid);
	
	String getMaxPage();

}
