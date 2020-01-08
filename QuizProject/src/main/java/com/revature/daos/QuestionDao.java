package com.revature.daos;

import java.util.List;

import com.revature.models.Question;

public interface QuestionDao {
	
	public static final QuestionDao instance = new QuestionDaoImpl();
	
	List<Question> getQuestionsByPage(int page);
	
	List<Question> getQuestionsByQuizId(int qid);
	
	boolean createQuestionMulti(String desc, String s1, String s2, String s3, String s4, String s5, String catagory, int userid, int answer);
	
	boolean createQuestionTrueFalse(String desc, String catagory, int userid, boolean answer);
	
	boolean createQuestionShort(String desc, String catagory, int userid, String answer);
	
	String getMaxPage();
}
