package com.revature.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.daos.QuizDao;
import com.revature.models.Quiz;
import com.revature.util.Json;

public class QuizzesServlet extends HttpServlet {

	private static final Logger log = LogManager.getRootLogger();
	private static final QuizDao quizDao = QuizDao.instance;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.trace("uri = " + req.getRequestURI());

		if ("/QuizProject/quizzes".equals(req.getRequestURI())) {
			String useridParam = req.getParameter("userid");
			if (useridParam != null) {
				int userid = Integer.parseInt(useridParam);

				List<Quiz> quizzes = quizDao.getQuizzesByUserId(userid);
				if (quizzes == null) {
					log.trace("No valid Quizzes found");
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					return;
				} else {
					resp.setStatus(HttpServletResponse.SC_OK);
					resp.getOutputStream().write(Json.write(quizzes));
					return;
				}
			}

			String gidParam = req.getParameter("groupid");
			if (gidParam != null) {
				int gid = Integer.parseInt(gidParam);

				List<Quiz> quizzes = quizDao.getQuizzesByGroupId(gid);
				if (quizzes == null) {
					log.trace("No valid Quizzes found");
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					return;
				} else {
					resp.setStatus(HttpServletResponse.SC_OK);
					resp.getOutputStream().write(Json.write(quizzes));
					return;
				}
			}

			String pageParam = req.getParameter("page");
			if (pageParam != null) {
				int page = Integer.parseInt(pageParam);

				List<Quiz> quizzes = quizDao.getQuizzesByPage(page);
				if (quizzes == null) {
					log.trace("No valid Quizzes found");
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					return;
				} else {
					resp.setStatus(HttpServletResponse.SC_OK);
					resp.addHeader("Access-Control-Expose-Headers", "X-page");
					resp.addHeader("X-page", quizDao.getMaxPage());
					resp.getOutputStream().write(Json.write(quizzes));
					return;
				}
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.trace("uri = " + req.getRequestURI());

		if ("/QuizProject/quizzes".equals(req.getRequestURI())) {

			// assigning question to quiz
			String quidParam = req.getParameter("quid");
			String qidParam = req.getParameter("qid");
			if (qidParam != null && quidParam != null) {
				int quid = Integer.parseInt(quidParam);
				int qid = Integer.parseInt(qidParam);
				if (quizDao.assignQuestionToQuiz(quid, qid)) {
					log.trace("Question assigned to Quiz");
					resp.setStatus(HttpServletResponse.SC_CREATED);
					return;
				} else {
					log.trace("Unable to insert");
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					return;
				}
			}
			
			String useridParam = req.getParameter("userid");
			if (qidParam != null && useridParam != null) {
				int userid = Integer.parseInt(useridParam);
				int qid = Integer.parseInt(qidParam);
				if (quizDao.assignQuizToUser(userid, qid)) {
					log.trace("Quiz assigned to User");
					resp.setStatus(HttpServletResponse.SC_CREATED);
					return;
				} else {
					log.trace("Unable to insert");
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					return;
				}
			}

			// creating Quiz
			Quiz quiz = (Quiz) Json.read(req.getInputStream(), Quiz.class);
			if (quiz != null) {
				if (quizDao.createQuiz(quiz.getName(), quiz.getCreatedBy())) {
					log.trace("Quiz inserted");
					resp.setStatus(HttpServletResponse.SC_CREATED);
					return;
				} else {
					log.trace("Unable to insert");
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					return;
				}
			}

		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String quidParam = req.getParameter("quid");
		String qidParam = req.getParameter("qid");
		if (qidParam != null && quidParam != null) {
			int quid = Integer.parseInt(quidParam);
			int qid = Integer.parseInt(qidParam);
			if (quizDao.unassignQuestionFromQuiz(quid, qid)) {
				log.trace("Question unassigned from Quiz");
				resp.setStatus(HttpServletResponse.SC_OK);
				return;
			} else {
				log.trace("Unable to insert");
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
		}
	}
}
