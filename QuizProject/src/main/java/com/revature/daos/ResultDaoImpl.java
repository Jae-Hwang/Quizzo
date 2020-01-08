package com.revature.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.models.Result;
import com.revature.util.ConnectionUtil;

public class ResultDaoImpl implements ResultDao {

	private static final Logger log = LogManager.getRootLogger();
	private static final ConnectionUtil connectionUtil = ConnectionUtil.get();

	// @formatter:off
	private static final String GET_RESULTS_BY_USER_ID = 
	"SELECT * FROM results"
	+ " WHERE user_id = ?";
	
	private static final String CREATE_RESULT =
	"INSERT INTO results (user_id, qid, grade, answers, finished)"
	+ " VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
	
	private static final String GET_ANSWERS = 
	"SELECT quid, answer FROM answers"
	+ " JOIN quizzes_composite_key using (quid)"
	+ " WHERE qid = ?";
	
	// @formatter:on

	public static Result extractResult(ResultSet rs) throws SQLException {
		int userid = rs.getInt("user_id");
		int qid = rs.getInt("qid");
		double grade = rs.getDouble("grade");
		String answers = rs.getString("answers");
		Timestamp finished = rs.getTimestamp("finished");
		return new Result(userid, qid, grade, answers, finished);
	}

	@Override
	public List<Result> getResultsByUserId(int userid) {
		log.trace("getting results by user id: " + userid);
		try (Connection c = connectionUtil.getPooledConnection()) {

			PreparedStatement ps = c.prepareStatement(GET_RESULTS_BY_USER_ID);
			int i = 0;
			ps.setInt(++i, userid);
			
			ResultSet rs = ps.executeQuery();
			List<Result> results = new ArrayList<Result>();
			while (rs.next()) {
				results.add(extractResult(rs));
			}
			return results;

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
	public boolean createResult(int userid, int qid, double grade, String answers) {
		log.trace("creating results");
		try (Connection c = connectionUtil.getPooledConnection()) {

			PreparedStatement ps = c.prepareStatement(CREATE_RESULT);
			int i = 0;
			ps.setInt(++i, userid);
			ps.setInt(++i, qid);
			ps.setDouble(++i, grade);
			ps.setString(++i, answers);
			
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
	public Map<Integer, String> getAnswers(int qid) {
		log.trace("getting answers");
		try (Connection c = connectionUtil.getPooledConnection()) {

			PreparedStatement ps = c.prepareStatement(GET_ANSWERS);
			int i = 0;
			ps.setInt(++i, qid);
			
			ResultSet rs = ps.executeQuery();
			Map<Integer, String> answers = new HashMap<Integer, String>();
			while (rs.next()) {
				answers.put(rs.getInt("quid"), rs.getString("answer"));
			}
			return answers;
			
		} catch (SQLException sqle) {
			log.debug("connection failed");
			log.warn("Stack Trace: ", sqle);
		} catch (Exception ge) {
			log.debug("Generic Exception");
			log.warn("Stack Trace: ", ge);
		}
		
		return null;
	}

}

