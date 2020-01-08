package com.revature.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class ConnectionUtil {

	private static Logger log = LogManager.getRootLogger();

	private static final ConnectionUtil instance;
	static {
		instance = new ConnectionUtil();
	}
	
	private Connection c;
	
	private DataSource ds;

	private ConnectionUtil() {
		super();
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setConnectionPool();
	}
	
	public static ConnectionUtil get() {
		return instance;
	}
	
	public Connection getConnection() {
		int totalTimeout = 0;
		try {
			log.trace("Checking connection.");
			while (!c.isValid(2000)) {
				log.debug("Disconnected, attempting to reconnect");
				setConnection();
				totalTimeout += 2000;
				if (totalTimeout >= 10000) {
					log.debug("Reconnection failed");
					return null;
				}
				log.debug("Reconnect successful");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			log.trace("Connection not initialized, initializing connection.");
			setConnection(); // if connection not initialized, initialize.
		}
		log.trace("Connection successfully retrieved.");
		return c;
	}
	
	public void setConnection() { // initialize connection.
		log.trace("Trying to connect to the DB");
		try {
			log.trace("Successfully connected to the DB");
			this.c = connect();
			this.c.setAutoCommit(false);
		} catch (SQLException e) {
			log.debug("Unable to connect the the DB");
			e.printStackTrace();
		}
	}

	public static Connection connect() throws SQLException {
		String url = System.getenv("DB_URL");
		String username = System.getenv("QUIZ_APP_USERNAME");
		String password = System.getenv("QUIZ_APP_PASSWORD");
		return DriverManager.getConnection(url, username, password);
	}
	
	// Using tomcat-jdbc connection pool
	
	public void setConnectionPool() {
		log.trace("Setting up the Connection Pool through Tomcat-jdbc");
		ds = setProperties();
	}
	
	public Connection getPooledConnection() {
		
		/* (Eager loading by default)
		// Lazy loading pool properties;
		if (ds == null) {
			setConnectionPool();
		}
		*/
		
		log.trace("Retrieving pooled connection");
		
		Connection c = null;
		try {
			c = ds.getConnection();
		} catch (SQLException e) {
			log.warn("failed to connect to the Database");
			log.warn("Stack Trace: ", e);
		}
		return c;
	}
	
	public static DataSource setProperties() {
		PoolProperties p = new PoolProperties();
		p.setDriverClassName("oracle.jdbc.OracleDriver");
		p.setUrl(System.getenv("DB_URL"));
		p.setUsername(System.getenv("QUIZ_APP_USERNAME"));
		p.setPassword(System.getenv("QUIZ_APP_PASSWORD"));
		p.setInitialSize(5);
		p.setMaxIdle(10);
		p.setMaxActive(100);
		p.setMaxWait(5000);
		p.setValidationQuery("SELECT 1 from DUAL");
		p.setDefaultAutoCommit(false);
		
		DataSource ds = new DataSource(p);
		ds.setPoolProperties(p);
		return ds;
	}
	
	public static boolean heartbeat() {
		log.trace("Seeing if Database connection is alive");
		try (Connection c = instance.getPooledConnection()) {
			
			PreparedStatement ps = c.prepareStatement("SELECT 1 from DUAL");
			
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				log.trace("Connection to the Database is alive");
				return true;
			} else {
				log.debug("Failed to connect to the Database");
				return false;
			}
			
		} catch (SQLException sqle) {
			log.debug("connection failed");
			log.warn("Stack Trace: ", sqle);
		} catch (Exception ge) {
			log.debug("Generic Exception");
			log.warn("Stack Trace: ", ge);
		}
		
		return true;
	}
}