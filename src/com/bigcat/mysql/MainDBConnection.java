package com.bigcat.mysql;

import java.sql.SQLException;

public class MainDBConnection extends MySqlDBConnection {

	public static final String Schema = "smile_star";
	
	private final String DB_URL = "jdbc:mysql://localhost/smile_star";
	private final String USER_NAME = "root";
	private final String PASSWORD = "MySQL1513";//"mysql";//
	
	public MainDBConnection() throws ClassNotFoundException, SQLException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getDBURL() {
		// TODO Auto-generated method stub
		return DB_URL;
	}

	@Override
	protected String getLoginName() {
		// TODO Auto-generated method stub
		return USER_NAME;
	}

	@Override
	protected String getPassword() {
		// TODO Auto-generated method stub
		return PASSWORD;
	}

}
