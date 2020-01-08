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
import com.revature.daos.UserDao;
import com.revature.models.User;
import com.revature.util.Json;

public class UsersServlet extends HttpServlet {

	private static final Logger log = LogManager.getRootLogger();
	private static final UserDao userDao = UserDao.instance;
	private static final ResultDao resultDao = ResultDao.instance;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.trace("uri = " + req.getRequestURI());

		if ("/QuizProject/users".equals(req.getRequestURI())) {
			String pageParam = req.getParameter("page");
			if (pageParam != null) {
				int page = Integer.parseInt(pageParam);

				List<User> users = userDao.getUsersByPage(page);
				if (users == null) {
					log.trace("No valid Quizzes found");
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					return;
				} else {
					resp.setStatus(HttpServletResponse.SC_OK);
					resp.addHeader("Access-Control-Expose-Headers", "X-page");
					resp.addHeader("X-page", userDao.getMaxPage());
					resp.getOutputStream().write(Json.write(users));
					return;
				}
			}
		}
	}
}
