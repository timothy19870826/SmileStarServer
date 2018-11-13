package com.bigcat.test;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class TableBase {

	private Connection connection;
	public TableBase(Connection connection) {
		this.connection = connection;
		if (this.connection == null) {
			throw new NullPointerException("connection is NULL");
		}
	}
	
	public void dispose() {
		connection = null;
	}
	
	public abstract String getTableName();
	
	protected Connection getConnection() {
		return connection;
	}
	
	protected void lockRead() {
		
	}
	
	protected void lockWrite() {
		
	}
	
	protected void unlock() {
		
	}
	
	protected void setAutoCommit(boolean autoCommit) throws SQLException {
		connection.setAutoCommit(autoCommit);
	}
	
	protected void commit() throws SQLException {
		connection.commit();
	}
	
}
