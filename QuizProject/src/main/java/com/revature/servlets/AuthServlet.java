package com.revature.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.daos.UserDao;
import com.revature.models.User;
import com.revature.util.Json;

public class AuthServlet extends HttpServlet {

	private static final Logger log = LogManager.getRootLogger();
	private static final UserDao userDao = UserDao.instance;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.trace("uri = " + req.getRequestURI());

		if ("/QuizProject/auth/login".equals(req.getRequestURI())) {
			String username = req.getParameter("username");
			String password = req.getParameter("password");
			User loggedInUser = userDao.login(username, password);

			if (loggedInUser == null) {
				log.trace("User Credential does not match. 401");
				resp.setStatus(401); // Unauthorized status code
				return;
			} else {
				log.trace("User Credential match, creating and sending back the Session. 201");
				resp.setStatus(201);
				req.getSession().setAttribute("user", loggedInUser);
				resp.getOutputStream().write(Json.write(loggedInUser));
				return;
			}
		} else if ("/QuizProject/auth/logout".equals(req.getRequestURI())) {
			resp.setStatus(202);
			log.trace("Attempting to setting user on session to null. 202");
			HttpSession session = req.getSession();
			session.setAttribute("user", null);
			session.invalidate();
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		if ("/QuizProject/auth/session-user".equals(req.getRequestURI())) {
			log.trace("Sending current user Session 200");
			resp.setStatus(200);
			resp.getOutputStream().write(Json.write(req.getSession().getAttribute("user")));
		}
	}
}
