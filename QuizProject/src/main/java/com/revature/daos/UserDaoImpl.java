package com.revature.daos;

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
	"SELECT *" 
    + " FROM"
    	+ " (SELECT users.*,row_number()"
    	+ " over (ORDER BY users.user_id ASC) line_number"
        + " FROM users"
        + " WHERE users.type = 1)"
    + " WHERE line_number BETWEEN ? AND ? ORDER BY line_number";
	
	private static final String REGISTER_USER =
	"CALL register_user(?, ?, ?, ?)";
	
	private static final String REGISTER_MANAGER =
	"CALL register_manager(?, ?, ?, ?)";
	
	private static final String LOGIN =
	"SELECT * FROM users"
	+ " WHERE username = ? AND password = ?";
	
	// @formatter:on
	
	private User extractUser(ResultSet rs) throws SQLException {
		int userid = rs.getInt("user_id");
		String username = rs.getString("username");
		boolean type = rs.getInt("type") == 1;
		String firstname = rs.getString("firstname");
		String lastname = rs.getString("lastname");
		return new User(userid, username, type, firstname, lastname);
	}
	
	@Override
	public List<User> getUsersByPage(int page) {
		
		log.trace("attempting to find all reimbs");
		try (Connection c = connectionUtil.getPooledConnection()) {

			PreparedStatement ps = c.prepareStatement(GET_USERS_BY_PAGE);
			int i = 0;
			ps.setInt(++i, (page-1)*5+1);
			ps.setInt(++i, page*5);

			ResultSet rs = ps.executeQuery();
			List<User> users= new ArrayList<User>();
			while (rs.next()) {
				users.add(extractUser(rs));
			}

			return users;

		} catch (SQLException e) {
			log.debug("connection failed");
			log.warn("Stack Trace: ", e);
			return null;
		}
	}

	@Override
	public boolean registerUser(String username, String password, String firstname, String lastname) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean registerManager(String username, String password, String firstname, String lastname) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public User login(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

}
