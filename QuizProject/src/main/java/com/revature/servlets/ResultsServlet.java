package com.revature.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.daos.ResultDao;
import com.revature.models.Result;
import com.revature.util.Json;

public class ResultsServlet extends HttpServlet {

	private static final Logger log = LogManager.getRootLogger();
	private static final ResultDao resultDao = ResultDao.instance;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.trace("uri = " + req.getRequestURI());

		if ("/QuizProject/results".equals(req.getRequestURI())) {
			String useridParam = req.getParameter("userid");
			if (useridParam != null) {
				int userid = Integer.parseInt(useridParam);

				List<Result> results = resultDao.getResultsByUserId(userid);
				if (results == null) {
					log.trace("No valid results found");
					resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
					return;
				} else {
					resp.setStatus(HttpServletResponse.SC_OK);
					resp.getOutputStream().write(Json.write(results));
					return;
				}
			}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.trace("uri = " + req.getRequestURI());

		if ("/QuizProject/results".equals(req.getRequestURI())) {
			String useridParam = req.getParameter("userid");
			String qidParam = req.getParameter("qid");
			String answers = req.getParameter("answers");
			if (useridParam != null && qidParam != null && answers != null) {
				int userid = Integer.parseInt(useridParam);
				int qid = Integer.parseInt(qidParam);

				Map<Integer, String> answerKey = resultDao.getAnswers(qid);
				int total = answerKey.size();
				int count = 0;

				String[] userAnswers = answers.split(",");
				for (String answer : userAnswers) {
					String[] keyVal = answer.split(":");
					int key = Integer.parseInt(keyVal[0]);
					if (answerKey.containsKey(key)) {
						if (answerKey.get(key).equals(keyVal[1])) {
							count++;
						}
					}
				}

				double grade = (double)count / (double)total * 100;
				log.trace("Grade: " + grade);

				if (resultDao.createResult(userid, qid, grade, answers)) {
					log.trace("Result inserted");
					resp.setStatus(HttpServletResponse.SC_CREATED);
					resp.addHeader("Access-Control-Expose-Headers", "X-grade");
					resp.addHeader("X-grade", Double.toString(grade));
					return;
				} else {
					log.trace("Unable to insert");
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					return;
				}
			}
		}
	}
}
