package com.revature.daos;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.models.User;
import com.revature.util.ConnectionUtil;

public class UserDaoImpl implements UserDao {
	
	private static final Logger log = LogManager.getRootLogger();
	private static final ConnectionUtil connectionUtil = ConnectionUtil.get();

	// @formatter:off
	private static final String GET_USERS_BY_PAGE =
	"SELECT user_id, type, username, firstname, lastname" 
    + " FROM"
    	+ " (SELECT users.user_id, users.type, users.username, users.firstname, users.lastname, row_number()"
    	+ " over (ORDER BY users.user_id ASC) line_number"
        + " FROM users"
        + " WHERE users.type = 1 AND users.active = 1)"
    + " WHERE line_number BETWEEN ? AND ? ORDER BY line_number";
	
	private static final String REGISTER_USER =
	"CALL regist_user(?, ?, ?, ?)";
	
	private static final String REGISTER_MANAGER =
	"CALL regist_manager(?, ?, ?, ?)";
	
	private static final String LOGIN =
	"SELECT user_id, type, username, firstname, lastname FROM users"
	+ " WHERE username = ? AND password = ?";
	
	private static final String MAX_PAGE_NUM = 
	"SELECT count(*) count FROM users";
	
	// @formatter:on
	private static final int ROWS_PER_PAGE = 5;
	
	
	public static User extractUser(ResultSet rs) throws SQLException {
		int userid = rs.getInt("user_id");
		String username = rs.getString("username");
		boolean type = rs.getInt("type") == 1;
		String firstname = rs.getString("firstname");
		String lastname = rs.getString("lastname");
		return new User(userid, username, type, firstname, lastname);
	}
	
	@Override
	public List<User> getUsersByPage(int page) {
		
		log.trace("Finding Users with pagination, page: " + page);
		try (Connection c = connectionUtil.getPooledConnection()) {

			PreparedStatement ps = c.prepareStatement(GET_USERS_BY_PAGE);
			int i = 0;
			ps.setInt(++i, (page - 1) * ROWS_PER_PAGE +1);
			ps.setInt(++i, page * ROWS_PER_PAGE);

			ResultSet rs = ps.executeQuery();
			List<User> users= new ArrayList<User>();
			while (rs.next()) {
				users.add(extractUser(rs));
			}

			return users;

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
	public boolean registerUser(String username, String password, String firstname, String lastname) {
		
		log.trace("Registering a User");
		try (Connection c = connectionUtil.getPooledConnection()) {
			
			CallableStatement cs = c.prepareCall(REGISTER_USER);
			int i = 0;
			cs.setString(++i, username);
			cs.setString(++i, password);
			cs.setString(++i, firstname);
			cs.setString(++i, lastname);
			
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
	public boolean registerManager(String username, String password, String firstname, String lastname) {
	
		log.trace("Registering a Manager");
		try (Connection c = connectionUtil.getPooledConnection()) {
			
			CallableStatement cs = c.prepareCall(REGISTER_MANAGER);
			int i = 0;
			cs.setString(++i, username);
			cs.setString(++i, password);
			cs.setString(++i, firstname);
			cs.setString(++i, lastname);
			
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
	public User login(String username, String password) {

		log.trace("Finding User with credentials");
		try (Connection c = connectionUtil.getPooledConnection()) {

			PreparedStatement ps = c.prepareStatement(LOGIN);
			int i = 0;
			ps.setString(++i, username);
			ps.setString(++i, password);

			ResultSet rs = ps.executeQuery();
			User user= null;
			if (rs.next()) {
				user = extractUser(rs);
			}
			log.trace("User: " + user.toString());
			return user;

		} catch (SQLException sqle) {
			log.debug("connection failed");
			log.warn("Stack Trace: ", sqle);
		} catch (Exception ge) {
			log.debug("Generic Exception");
			log.warn("Stack Trace: ", ge);
		}
		
		return null;
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

}
