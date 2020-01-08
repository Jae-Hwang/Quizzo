package com.revature.daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.models.Question;
import com.revature.models.Quiz;
import com.revature.util.ConnectionUtil;

public class QuizDaoImpl implements QuizDao {

	private static final Logger log = LogManager.getRootLogger();
	private static final ConnectionUtil connectionUtil = ConnectionUtil.get();

	// @formatter:off
	private static final String GET_QUIZZES_BY_PAGE =
	"SELECT qid, name, created, created_by"
	    + " FROM"
	    	+ " (SELECT quizzes.qid, quizzes.name, quizzes.created, quizzes.created_by, row_number()"
	            + " over (ORDER BY quizzes.qid ASC) line_number"
	            + " FROM quizzes"
	            + " WHERE quizzes.active = 1)"
	        + " WHERE line_number BETWEEN ? AND ? ORDER BY line_number";
	
	private static final String GET_QUIZZES_BY_USER_ID =
	"SELECT qid, name, created, created_by FROM quizzes"
	    + " INNER JOIN assigned_users_quizzes using (qid)"
	    + " WHERE user_id = ?";
	
	private static final String GET_QUIZZES_BY_GROUP_ID =
	"SELECT qid, quizzes.name, created, created_by FROM quizzes"
		    + " INNER JOIN assigned_groups_quizzes using (qid)"
		    + " WHERE gid = ?";
	
	private static final String CREATE_QUIZ =
	"CALL create_QUIZ(?, ?)";
	
	private static final String ASSIGN_QUIZ_TO_USER =
	"INSERT INTO assigned_users_quizzes (user_id, qid)"
	    + " VALUES (?, ?)";
	
	private static final String UNASSIGN_QUIZ_FROM_USER =
	"DELETE FROM assigned_users_quizzes"
		+ " WHERE user_id = ? AND qid = ?";
	
	private static final String ASSIGN_QUIZ_TO_GROUP =
	"INSERT INTO assigned_groups_quizzes (gid, qid)"
 	    + " VALUES (?, ?)";
	
	private static final String UNASSIGN_QUIZ_FROM_GROUP =
	"DELETE FROM assigned_groups_quizzes"
		+ " WHERE gid = ? AND qid = ?";
	
	private static final String MAX_PAGE_NUM = 
	"SELECT count(*) count FROM quizzes";
	
	private static final String ASSIGN_QUESTION_TO_QUIZ =
	"INSERT INTO quizzes_composite_key (quid, qid)"
 	    + " VALUES (?, ?)";
	
	private static final String UNASSIGN_QUESTION_FROM_QUIZ =
	"DELETE FROM quizzes_composite_key"
		+ " WHERE quid = ? AND qid = ?";
	// @formatter:on

	private static final int ROWS_PER_PAGE = 5;
	
	public static Quiz extractQuiz(ResultSet rs) throws SQLException {
		int qid = rs.getInt("qid");
		String name = rs.getString("name");
		Timestamp created = rs.getTimestamp("created");
		int createdBy = rs.getInt("created_by");
		return new Quiz(qid, name, created, createdBy);
	}

	@Override
	public List<Quiz> getQuizzesByPage(int page) {
		log.trace("Getting Quizzes with pagination, page: " + page);
		try (Connection c = connectionUtil.getPooledConnection()) {

			PreparedStatement ps = c.prepareStatement(GET_QUIZZES_BY_PAGE);
			int i = 0;
			ps.setInt(++i, (page - 1) * ROWS_PER_PAGE + 1);
			ps.setInt(++i, page * ROWS_PER_PAGE);

			ResultSet rs = ps.executeQuery();

			List<Quiz> quizzes = new ArrayList<Quiz>();
			while (rs.next()) {
				quizzes.add(extractQuiz(rs));
			}
			return quizzes;

		} catch (SQLException sqle) {
			log.debug("connection failed");
			log.warn("Stack Trace: ", sqle);
		} catch (Exception ge) {
			log.debug("Generic Exception");
			log.warn("Stack Trace: ", ge);
		}

		return null;
	}

	@Override
	public List<Quiz> getQuizzesByUserId(int userid) {
		log.trace("Getting Quizzes with userid: " + userid);
		try (Connection c = connectionUtil.getPooledConnection()) {

			PreparedStatement ps = c.prepareStatement(GET_QUIZZES_BY_USER_ID);
			int i = 0;
			ps.setInt(++i, userid);

			ResultSet rs = ps.executeQuery();

			List<Quiz> quizzes = new ArrayList<Quiz>();
			while (rs.next()) {
				quizzes.add(extractQuiz(rs));
			}
			return quizzes;

		} catch (SQLException sqle) {
			log.debug("connection failed");
			log.warn("Stack Trace: ", sqle);
		} catch (Exception ge) {
			log.debug("Generic Exception");
			log.warn("Stack Trace: ", ge);
		}

		return null;
	}

	@Override
	public List<Quiz> getQuizzesByGroupId(int gid) {
		log.trace("Getting Quizzes with group id: " + gid);
		try (Connection c = connectionUtil.getPooledConnection()) {

			PreparedStatement ps = c.prepareStatement(GET_QUIZZES_BY_GROUP_ID);
			int i = 0;
			ps.setInt(++i, gid);

			ResultSet rs = ps.executeQuery();

			List<Quiz> quizzes = new ArrayList<Quiz>();
			while (rs.next()) {
				quizzes.add(extractQuiz(rs));
			}
			return quizzes;

		} catch (SQLException sqle) {
			log.debug("connection failed");
			log.warn("Stack Trace: ", sqle);
		} catch (Exception ge) {
			log.debug("Generic Exception");
			log.warn("Stack Trace: ", ge);
		}

		return null;
	}

	@Override
	public boolean createQuiz(String name, int createdBy) {
		log.trace("creating quiz");
		try (Connection c = connectionUtil.getPooledConnection()) {

			CallableStatement cs = c.prepareCall(CREATE_QUIZ);
			int i = 0;
			cs.setString(++i, name);
			cs.setInt(++i, createdBy);

			cs.execute();
			c.commit();
			return true;

		} catch (SQLException sqle) {
			log.debug("connection failed");
			log.warn("Stack Trace: ", sqle);
		} catch (Exception ge) {
			log.debug("Generic Exception");
			log.warn("Stack Trace: ", ge);
		}

		return false;
	}

	@Override
	public boolean assignQuizToUser(int userid, int qid) {
		log.trace("Assigning Quiz: " + qid + " to user: " + userid);
		try (Connection c = connectionUtil.getPooledConnection()) {

			PreparedStatement ps = c.prepareStatement(ASSIGN_QUIZ_TO_USER);
			int i = 0;
			ps.setInt(++i, userid);
			ps.setInt(++i, qid);
			
			if (ps.executeQuery().next()) {
				c.commit();
				return true;
			}

		} catch (SQLException sqle) {
			log.debug("connection failed");
			log.warn("Stack Trace: ", sqle);
		} catch (Exception ge) {
			log.debug("Generic Exception");
			log.warn("Stack Trace: ", ge);
		}

		return false;
	}

	@Override
	public boolean unassignQuizFromUser(int userid, int qid) {
		log.trace("Unassigning user: " + qid + " from user: " + userid);
		try (Connection c = connectionUtil.getPooledConnection()) {

			PreparedStatement ps = c.prepareStatement(UNASSIGN_QUIZ_FROM_USER);
			int i = 0;
			ps.setInt(++i, userid);
			ps.setInt(++i, qid);
			
			if (ps.executeQuery().next()) {
				c.commit();
				return true;
			}

		} catch (SQLException sqle) {
			log.debug("connection failed");
			log.warn("Stack Trace: ", sqle);
		} catch (Exception ge) {
			log.debug("Generic Exception");
			log.warn("Stack Trace: ", ge);
		}

		return false;
	}

	@Override
	public boolean assignQuizToGroup(int gid, int qid) {
		log.trace("Assigning Quiz: " + qid + " to group: " + gid);
		try (Connection c = connectionUtil.getPooledConnection()) {

			PreparedStatement ps = c.prepareStatement(ASSIGN_QUIZ_TO_USER);
			int i = 0;
			ps.setInt(++i, gid);
			ps.setInt(++i, qid);
			
			if (ps.executeQuery().next()) {
				c.commit();
				return true;
			}

		} catch (SQLException sqle) {
			log.debug("connection failed");
			log.warn("Stack Trace: ", sqle);
		} catch (Exception ge) {
			log.debug("Generic Exception");
			log.warn("Stack Trace: ", ge);
		}

		return false;
	}

	@Override
	public boolean unassignQuizFromGroup(int gid, int qid) {
		log.trace("Unassigning Quiz: " + qid + " from Group: " + gid);
		try (Connection c = connectionUtil.getPooledConnection()) {

			PreparedStatement ps = c.prepareStatement(UNASSIGN_QUIZ_FROM_GROUP);
			int i = 0;
			ps.setInt(++i, gid);
			ps.setInt(++i, qid);
			
			if (ps.executeQuery().next()) {
				c.commit();
				return true;
			}

		} catch (SQLException sqle) {
			log.debug("connection failed");
			log.warn("Stack Trace: ", sqle);
		} catch (Exception ge) {
			log.debug("Generic Exception");
			log.warn("Stack Trace: ", ge);
		}

		return false;
	}

	public String getMaxPage() {
		log.trace("Getting max page number");
		try (Connection c = connectionUtil.getPooledConnection()) {
			String sql = MAX_PAGE_NUM;
			PreparedStatement ps = c.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				int result = rs.getInt("count");
				log.trace("number of rows: " + result);
				return Integer.toString((result-1)/ROWS_PER_PAGE +1);
			}

			return "";

		} catch (SQLException e) {
			log.debug("SQL failed");
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean assignQuestionToQuiz(int quid, int qid) {
		log.trace("Assigning Question: " + quid + " to Quiz: " + qid);
		try (Connection c = connectionUtil.getPooledConnection()) {

			PreparedStatement ps = c.prepareStatement(ASSIGN_QUESTION_TO_QUIZ);
			int i = 0;
			ps.setInt(++i, quid);
			ps.setInt(++i, qid);
			
			log.trace(quid);
			log.trace(qid);
			
			if (ps.executeQuery().next()) {
				c.commit();
				return true;
			}

		} catch (SQLException sqle) {
			log.debug("connection failed");
			log.warn("Stack Trace: ", sqle);
		} catch (Exception ge) {
			log.debug("Generic Exception");
			log.warn("Stack Trace: ", ge);
		}

		return false;
	}

	@Override
	public boolean unassignQuestionFromQuiz(int quid, int qid) {
		log.trace("Unassigning Question: " + quid + " from Quiz: " + qid);
		try (Connection c = connectionUtil.getPooledConnection()) {

			PreparedStatement ps = c.prepareStatement(UNASSIGN_QUESTION_FROM_QUIZ);
			int i = 0;
			ps.setInt(++i, quid);
			ps.setInt(++i, qid);
			
			if (ps.executeQuery().next()) {
				c.commit();
				return true;
			}

		} catch (SQLException sqle) {
			log.debug("connection failed");
			log.warn("Stack Trace: ", sqle);
		} catch (Exception ge) {
			log.debug("Generic Exception");
			log.warn("Stack Trace: ", ge);
		}

		return false;
	}
	
}
