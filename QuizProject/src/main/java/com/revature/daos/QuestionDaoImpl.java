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
import com.revature.util.ConnectionUtil;

public class QuestionDaoImpl implements QuestionDao {

	private static final Logger log = LogManager.getRootLogger();
	private static final ConnectionUtil connectionUtil = ConnectionUtil.get();

	// @formatter:off
	private static final String GET_QUESTIONS_BY_PAGE =
	"SELECT *"
    	+ " FROM"
        	+ " (SELECT questions.*, row_number()"
            + " over (ORDER BY questions.quid ASC) line_number"
            + " FROM questions"
            + " WHERE questions.active = 1)"
        + " WHERE line_number BETWEEN ? AND ? ORDER BY line_number";

	private static final String GET_QUESTIONS_BY_QUIZ_ID =
	"SELECT * FROM questions"
		+ " JOIN quizzes_composite_key using (quid)"
		+ " WHERE qid = ?";
	
	private static final String CREATE_QUESTION_MULTI =
	"CALL create_question_multi"
		+ " (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String CREATE_QUESTION_TRUEFALSE =
	"CALL create_question_truefalse"
			+ " (?, ?, ?, ?)";

	private static final String CREATE_QUESTION_SHORT =
	"CALL create_question_short"
			+ " (?, ?, ?, ?)";
	
	private static final String MAX_PAGE_NUM = 
	"SELECT count(*) count FROM questions";
	// @formatter:on

	private static final int ROWS_PER_PAGE = 5;

	public static Question extractQuestion(ResultSet rs) throws SQLException {
		int quid = rs.getInt("quid");
		String desc = rs.getString("description");
		int type = rs.getInt("type");
		String[] selections = new String[5];
		if (type == 1) {
			selections[0] = rs.getString("s1");
			selections[1] = rs.getString("s2");
			selections[2] = rs.getString("s3");
			selections[3] = rs.getString("s4");
			selections[4] = rs.getString("s5");
		}
		String catagory = rs.getString("catagory");
		Timestamp created = rs.getTimestamp("created");
		int createdBy = rs.getInt("created_by");
		return new Question(quid, desc, type, selections, catagory, created, createdBy);
	}

	@Override
	public List<Question> getQuestionsByPage(int page) {

		log.trace("Getting Questions with pagination, page: " + page);
		try (Connection c = connectionUtil.getPooledConnection()) {

			PreparedStatement ps = c.prepareStatement(GET_QUESTIONS_BY_PAGE);
			int i = 0;
			ps.setInt(++i, (page - 1) * ROWS_PER_PAGE + 1);
			ps.setInt(++i, page * ROWS_PER_PAGE);

			ResultSet rs = ps.executeQuery();

			List<Question> questions = new ArrayList<Question>();
			while (rs.next()) {
				questions.add(extractQuestion(rs));
			}
			return questions;

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
	public List<Question> getQuestionsByQuizId(int qid) {
		log.trace("Getting Questions by Quiz Id: " + qid);
		try (Connection c = connectionUtil.getPooledConnection()) {

			PreparedStatement ps = c.prepareStatement(GET_QUESTIONS_BY_QUIZ_ID);
			int i = 0;
			ps.setInt(++i, qid);

			ResultSet rs = ps.executeQuery();

			List<Question> questions = new ArrayList<Question>();
			while (rs.next()) {
				questions.add(extractQuestion(rs));
			}
			return questions;

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
	public boolean createQuestionMulti(String desc, String s1, String s2, String s3, String s4, String s5,
			String catagory, int userid, int answer) {

		log.trace("creating multiple choice question");
		try (Connection c = connectionUtil.getPooledConnection()) {

			CallableStatement cs = c.prepareCall(CREATE_QUESTION_MULTI);
			int i = 0;
			cs.setString(++i, desc);
			cs.setString(++i, s1);
			cs.setString(++i, s2);
			cs.setString(++i, s3);
			cs.setString(++i, s4);
			cs.setString(++i, s5);
			cs.setString(++i, catagory);
			cs.setInt(++i, userid);
			cs.setString(++i, Integer.toString(answer));

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
	public boolean createQuestionTrueFalse(String desc, String catagory, int userid, boolean answer) {

		log.trace("creating true/false question");
		try (Connection c = connectionUtil.getPooledConnection()) {

			CallableStatement cs = c.prepareCall(CREATE_QUESTION_TRUEFALSE);
			int i = 0;
			cs.setString(++i, desc);
			cs.setString(++i, catagory);
			cs.setInt(++i, userid);
			if (answer) {
				cs.setString(++i, "t");
			} else {
				cs.setString(++i, "f");
			}

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
	public boolean createQuestionShort(String desc, String catagory, int userid, String answer) {

		log.trace("creating true/false question");
		try (Connection c = connectionUtil.getPooledConnection()) {

			CallableStatement cs = c.prepareCall(CREATE_QUESTION_TRUEFALSE);
			int i = 0;
			cs.setString(++i, desc);
			cs.setString(++i, catagory);
			cs.setInt(++i, userid);
			cs.setString(++i, answer);

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

	public String getMaxPage() {
		log.trace("Getting max page number");
		try (Connection c = connectionUtil.getPooledConnection()) {
			String sql = MAX_PAGE_NUM;
			PreparedStatement ps = c.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				int result = rs.getInt("count");
				log.trace("number of rows: " + result);
				return Integer.toString((result - 1) / ROWS_PER_PAGE + 1);
			}

			return "";

		} catch (SQLException e) {
			log.debug("SQL failed");
			e.printStackTrace();
			return null;
		}
	}

}
