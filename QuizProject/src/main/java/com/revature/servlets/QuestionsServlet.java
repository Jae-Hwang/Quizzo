package com.revature.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.daos.QuestionDao;
import com.revature.models.Question;
import com.revature.util.Json;

public class QuestionsServlet extends HttpServlet {
	
	private static final Logger log = LogManager.getRootLogger();
	private static final QuestionDao questionDao = QuestionDao.instance;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.trace("uri = " + req.getRequestURI());

		if ("/QuizProject/questions".equals(req.getRequestURI())) {
			String qidParam = req.getParameter("qid");
			if (qidParam != null) {
				int qid = Integer.parseInt(qidParam);

				List<Question> questions = questionDao.getQuestionsByQuizId(qid);
				if (questions == null) {
					log.trace("No valid Questions found");
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					return;
				} else {
					resp.setStatus(HttpServletResponse.SC_OK);
					resp.getOutputStream().write(Json.write(questions));
					return;
				}
			}
			
			String pageParam = req.getParameter("page");
			if (pageParam != null) {
				int page = Integer.parseInt(pageParam);
				
				List<Question> questions = questionDao.getQuestionsByPage(page);
				if (questions == null) {
					log.trace("No valid Questions found");
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					return;
				} else {
					resp.setStatus(HttpServletResponse.SC_OK);
					resp.addHeader("Access-Control-Expose-Headers", "X-page");
					resp.addHeader("X-page", questionDao.getMaxPage());
					resp.getOutputStream().write(Json.write(questions));
					return;
				}
			}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.trace("uri = " + req.getRequestURI());

		if ("/QuizProject/questions".equals(req.getRequestURI())) {
			Question qu = (Question) Json.read(req.getInputStream(), Question.class);
			if (qu != null) {
				log.trace("Type: " + qu.getType());
				if (qu.getType() == 1) {
					if (questionDao.createQuestionMulti(
							qu.getDescription(), 
							qu.getSelections()[0], 
							qu.getSelections()[1], 
							qu.getSelections()[2], 
							qu.getSelections()[3], 
							qu.getSelections()[4], 
							qu.getCatagory(), 
							qu.getCreatedBy(),
							Integer.parseInt(req.getParameter("answer")))) {
						log.trace("Multi-Choice Question inserted");
						resp.setStatus(HttpServletResponse.SC_CREATED);
						return;	
					} else {
						log.trace("Unable to insert");
						resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						return;
					}
				} else if (qu.getType() == 2) {
					if (questionDao.createQuestionTrueFalse(
							qu.getDescription(), 
							qu.getCatagory(), 
							qu.getCreatedBy(),
							Boolean.parseBoolean(req.getParameter("answer")))) {
						log.trace("True-False Question inserted");
						resp.setStatus(HttpServletResponse.SC_CREATED);
						return;
					} else {
						log.trace("Unable to insert");
						resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						return;
					}
				} else if (qu.getType() == 3) {
					if (questionDao.createQuestionShort(
							qu.getDescription(), 
							qu.getCatagory(), 
							qu.getCreatedBy(),
							req.getParameter("answer"))) {
						log.trace("Short-Answer Question inserted");
						resp.setStatus(HttpServletResponse.SC_CREATED);
						return;
					} else {
						log.trace("Unable to insert");
						resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						return;
					}
				} else {
					log.trace("Wrong Type: Bad Request");
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					return;
				}
			}
			
		} else {
			log.trace("Missing Body: Bad Request");
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
	}

}
