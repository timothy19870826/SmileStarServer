package com.bigcat.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;


public abstract class MySqlDBConnection {

	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	//private static final String DB_URL = "jdbc:mysql://localhost/smile_star";
	//private static final String USER_NAME = "root";
	//private static final String PASSWORD = "MySQL1513";//"mysql";//
	
	private Connection connection;
	
	public MySqlDBConnection() throws ClassNotFoundException, SQLException {
		Class.forName(JDBC_DRIVER);
		connection = DriverManager.getConnection(getDBURL(), getLoginName(), getPassword());
		//connection.setAutoCommit(false);
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public void close() throws SQLException {
		if (connection != null) {
			releasePreparedStatement();
			connection.close();
		}
	}
	
	private ArrayList<PreparedStatement> activeList = new ArrayList<>();
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		activeList.add(preparedStatement);
		return preparedStatement;
	}
	
	public void releasePreparedStatement() throws SQLException {
		for (int idx = activeList.size() - 1; idx >= 0; idx--) {
			activeList.get(idx).close();
		}	
		activeList.clear();
	}
	
	public void begin() throws SQLException {
		connection.setAutoCommit(false);
	}
	
	public void commit() throws SQLException {
		connection.commit();
		connection.setAutoCommit(true);
	}
	
	public void rollback() throws SQLException {
		connection.rollback();
		connection.setAutoCommit(true);
	}
	
	public Savepoint setSavepoint() throws SQLException {
		return connection.setSavepoint();
	}
	
	public Savepoint savepoint(String name) throws SQLException {
		return connection.setSavepoint(name);
	}
	
	public void rollback(Savepoint savepoint) throws SQLException {
		connection.rollback(savepoint);
		connection.setAutoCommit(true);
	}
	
	public void lockTable(String[] lockRead, String[] lockWrite) throws SQLException {
		StringBuffer lockTabls = new StringBuffer();
		for (int idx = 0; idx < lockRead.length; idx++) {
			lockTabls.append(",").append(lockRead[idx]).append(" read");
		}
		for (int idx = 0; idx < lockWrite.length; idx++) {
			lockTabls.append(",").append(lockWrite[idx]).append(" write");
		}
		String sql = String.format("lock tables %s", lockTabls.substring(1));
		PreparedStatement preparedStatement = prepareStatement(sql);
		preparedStatement.execute();
	}
	
	public void lockWrite(String ...tabName) throws SQLException {
		StringBuffer lockTabls = new StringBuffer();
		for (int idx = 0; idx < tabName.length; idx++) {
			lockTabls.append(",").append(tabName[idx]).append(" write");
		}
		String sql = String.format("lock tables %s", lockTabls.substring(1));
		PreparedStatement preparedStatement = prepareStatement(sql);
		preparedStatement.executeQuery();
	}
	
	public void lockRead(String ...tabName) throws SQLException {
		StringBuffer lockTabls = new StringBuffer();
		for (int idx = 0; idx < tabName.length; idx++) {
			lockTabls.append(",").append(tabName[idx]).append(" read");;
		}
		String sql = String.format("lock tables %s", lockTabls.substring(1));
		PreparedStatement preparedStatement = prepareStatement(sql);
		preparedStatement.executeQuery();
	}
	
	public void unlockTable() throws SQLException {
		PreparedStatement preparedStatement = prepareStatement("unlock tables");
		preparedStatement.execute();
	}
	
	protected abstract String getDBURL();
	
	protected abstract String getLoginName();
	
	protected abstract String getPassword();
	

	/**
	 * SELECT field1, field2,...fieldN FROM table_name1, table_name2...
	 */
	public static final String CMD_SELECT = "SELECT %s FROM %s WHERE %s";
	/**
	 * SELECT field1, field2,...fieldN FROM table_name1, table_name2... [WHERE
	 * condition1 [AND [OR]] condition2.....
	 */
	public static final String CMD_SELECT_WHERE = "SELECT %s FROM %s WHERE %s";
	/**
	 * SELECT column_name,column_name FROM table_name	[WHERE Clause] [OFFSET M ][LIMIT N]
	 */
	public static final String CMD_SELECT_WHERE_OFFSET = "SELECT %s FROM %s WHERE %s OFFSET %s";
	/**
	 * SELECT column_name,column_name FROM table_name	[WHERE Clause] [OFFSET M ][LIMIT N]
	 */
	public static final String CMD_SELECT_WHERE_LIMIT = "SELECT %s FROM %s WHERE %s LIMIT %s";
	/**
	 * SELECT column_name,column_name FROM table_name	[WHERE Clause] [OFFSET M ][LIMIT N]
	 */
	public static final String CMD_SELECT_WHERE_OFFSET_LIMIT = "SELECT %s FROM %s WHERE %s LIMIT %s OFFSET %s";
	/**
	 * INSERT INTO table_name(field1, field2,...fieldN) VALUES(value1,
	 * value2,...valueN)
	 */
	public static final String CMD_INSERT_TO = "INSERT INTO %s(%s) VALUES(%s)";
	/**
	 * UPDATE table_name SET field1=new-value1, field2=new-value2
	 */
	public static final String CMD_UPDATE = "UPDATE %s SET %s";
	/**
	 * UPDATE table_name SET field1=new-value1, field2=new-value2 [WHERE Clause]
	 */
	public static final String CMD_UPDATE_WHERE = "UPDATE %s SET %s WHERE %s";
	/**
	 * DELETE FROM table_name [WHERE Clause]
	 */
	public static final String CMD_DELETE = "DELETE FROM %s WHERE %s";
}
