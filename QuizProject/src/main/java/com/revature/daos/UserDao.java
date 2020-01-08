package com.revature.daos;

import java.util.List;

import com.revature.models.User;

public interface UserDao {
	
	public static final UserDao instance = new UserDaoImpl();
	
	List<User> getUsersByPage(int page);

	boolean registerUser(String username, String password, String firstname, String lastname);

	boolean registerManager(String username, String password, String firstname, String lastname);
	
	User login(String username, String password);
	
	String getMaxPage();
}
