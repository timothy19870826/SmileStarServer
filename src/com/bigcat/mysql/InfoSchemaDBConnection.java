package com.bigcat.mysql;

import java.sql.SQLException;

public class InfoSchemaDBConnection extends MySqlDBConnection {

	public static final String Schema = "information_schema";
	
	private final String DB_URL = "jdbc:mysql://localhost/information_schema";
	private final String USER_NAME = "root";
	private final String PASSWORD = "MySQL1513";//"mysql";//
	
	public InfoSchemaDBConnection() throws ClassNotFoundException, SQLException {
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
