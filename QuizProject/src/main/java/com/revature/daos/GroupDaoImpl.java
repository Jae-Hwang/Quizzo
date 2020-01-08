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

import com.revature.models.Group;
import com.revature.util.ConnectionUtil;

public class GroupDaoImpl implements GroupDao {

	private static final Logger log = LogManager.getRootLogger();
	private static final ConnectionUtil connectionUtil = ConnectionUtil.get();

	// @formatter:off
	private static final String GET_GROUPS_BY_PAGE =
	"SELECT gid, name"
	    + " FROM"
	    	+ " (SELECT groups.gid, groups.name, row_number()"
	            + " over (ORDER BY groups.gid ASC) line_number"
	            + " FROM groups"
	            + " WHERE groups.active = 1)"
	        + " WHERE line_number BETWEEN ? AND ? ORDER BY line_number";
	
	private static final String GET_GROUPS_BY_USER_ID =
	"SELECT gid, name FROM groups"
	    + " INNER JOIN groups_composite_key using (gid)"
	    + " JOIN users using (user_id)"
	    + " WHERE user_id = ?";
	
	private static final String CREATE_GROUP =
	"CALL create_group(?)";
	
	private static final String ASSIGN_USER_TO_GROUP =
	"INSERT INTO groups_composite_key (user_id, gid)"
 	    + " VALUES (?, ?)";
	
	private static final String UNASSIGN_USER_FROM_GROUP =
	"DELETE FROM groups_composite_key"
		+ " WHERE user_id = ? AND gid = ?";
	
	// @formatter:on
	
	private static final int ROWS_PER_PAGE = 5;

	private static final Group extractGroup(ResultSet rs) throws SQLException {
		int gid = rs.getInt("gid");
		String name = rs.getString("name");
		return new Group(gid, name);
	}

	@Override
	public List<Group> getGroupsByPage(int page) {
		log.trace("Getting groups with pagination, page: " + page);
		try (Connection c = connectionUtil.getPooledConnection()) {

			PreparedStatement ps = c.prepareStatement(GET_GROUPS_BY_PAGE);
			int i = 0;
			ps.setInt(++i, (page - 1) * ROWS_PER_PAGE + 1);
			ps.setInt(++i, page * ROWS_PER_PAGE);

			ResultSet rs = ps.executeQuery();

			List<Group> groups = new ArrayList<Group>();
			while (rs.next()) {
				groups.add(extractGroup(rs));
			}
			return groups;

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
	public List<Group> getGroupsByUserId(int userid) {
		log.trace("Getting groups with userid: " + userid);
		try (Connection c = connectionUtil.getPooledConnection()) {

			PreparedStatement ps = c.prepareStatement(GET_GROUPS_BY_USER_ID);
			int i = 0;
			ps.setInt(++i, userid);

			ResultSet rs = ps.executeQuery();

			List<Group> groups = new ArrayList<Group>();
			while (rs.next()) {
				groups.add(extractGroup(rs));
			}
			return groups;

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
	public boolean createGroup(String name) {
		log.trace("creating a group: " + name);
		try (Connection c = connectionUtil.getPooledConnection()) {

			CallableStatement cs = c.prepareCall(CREATE_GROUP);
			int i = 0;
			cs.setString(++i, name);


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
	public boolean assignUserToGroup(int userid, int gid) {
		log.trace("Assigning user: " + userid + " to group: " + gid);
		try (Connection c = connectionUtil.getPooledConnection()) {

			PreparedStatement ps = c.prepareStatement(ASSIGN_USER_TO_GROUP);
			int i = 0;
			ps.setInt(++i, userid);
			ps.setInt(++i, gid);
			
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
	public boolean unassignUserFromGroup(int userid, int gid) {
		log.trace("Unassigning user: " + userid + " from group: " + gid);
		try (Connection c = connectionUtil.getPooledConnection()) {

			PreparedStatement ps = c.prepareStatement(UNASSIGN_USER_FROM_GROUP);
			int i = 0;
			ps.setInt(++i, userid);
			ps.setInt(++i, gid);
			
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
