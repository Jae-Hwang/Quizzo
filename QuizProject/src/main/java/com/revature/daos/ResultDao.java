package com.revature.daos;

import java.util.List;
import java.util.Map;

import com.revature.models.Result;

public interface ResultDao {
	
	public static final ResultDao instance = new ResultDaoImpl();

	List<Result> getResultsByUserId(int userid);
	
	boolean createResult(int userid, int qid, double grade, String answers);
	
	Map<Integer, String> getAnswers(int qid);
}
