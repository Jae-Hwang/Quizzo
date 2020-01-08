package com.revature.daos;

import java.util.List;

import com.revature.models.Group;

public interface GroupDao {
	
	public static final GroupDao instance = new GroupDaoImpl();
	
	List<Group> getGroupsByPage(int page);
	
	List<Group> getGroupsByUserId(int userid);
	
	boolean createGroup(String name);
	
	boolean assignUserToGroup(int userid, int gid);
	
	boolean unassignUserFromGroup(int userid, int gid);
}
