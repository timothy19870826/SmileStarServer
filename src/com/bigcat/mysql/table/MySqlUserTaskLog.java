package com.bigcat.mysql.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bigcat.mysql.MySqlDBConnection;

public class MySqlUserTaskLog extends MySqlTableData {

	public static final String tableName = "user_task_log";
	
	public static List<MySqlUserTaskLog> queryPage(MySqlDBConnection connection, int offset, int limit) 
			throws SQLException, InstantiationException, IllegalAccessException {
		return MySqlTable.queryPage(connection, MySqlUserTaskLog.class, MySqlUserTaskLog.tableName, offset, limit);
	}
	
	public static List<MySqlUserTaskLog> findByTaskId(MySqlDBConnection connection, int taskId)
			throws SQLException {
		String sqlCmd = String.format("SELECT * FROM %s WHERE taskId=?", tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		preparedStatement.setInt(1, taskId);
		ResultSet resultSet = preparedStatement.executeQuery();

		List<MySqlUserTaskLog> result = new ArrayList<MySqlUserTaskLog>();
		while (resultSet.next()) {	
			result.add(new MySqlUserTaskLog(resultSet));
		}
		
		resultSet.close();	
		return result;
	}
	
	public static List<MySqlUserTaskLog> findByUserUid(MySqlDBConnection connection, long uid, int pageIdx, int pageSize)
			throws SQLException {
		String sqlCmd = String.format("SELECT * FROM %s WHERE uid=? ORDER BY id LIMIT ? OFFSET ?", tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		preparedStatement.setLong(1, uid);
		preparedStatement.setInt(2, pageSize);
		preparedStatement.setInt(3, pageSize * pageIdx);
		ResultSet resultSet = preparedStatement.executeQuery();

		List<MySqlUserTaskLog> result = new ArrayList<MySqlUserTaskLog>();
		while (resultSet.next()) {	
			result.add(new MySqlUserTaskLog(resultSet));
		}
		
		resultSet.close();	
		return result;
	}
	
	public static void completedTaskLog(MySqlDBConnection connection, int taskId) throws SQLException {
		String sqlCmd = String.format("UPDATE %s SET state=? WHERE taskId=?", tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		preparedStatement.setInt(1, 2);
		preparedStatement.setInt(2, taskId);
		preparedStatement.executeUpdate();
	}
	
	public static void addLog(MySqlDBConnection connection, long uid, int taskId, int state, String name) throws SQLException {
		String sqlCmd = String.format("INSERT INTO %s(uid,taskId,state,name,date) VALUES(?,?,?,?,?)", tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		preparedStatement.setLong(1, uid);
		preparedStatement.setInt(2, taskId);
		preparedStatement.setInt(3, state);
		preparedStatement.setString(4, name);
		Date now = new Date();
		preparedStatement.setLong(5, now.getTime());
		preparedStatement.executeUpdate();
	}

	private int id;
	private int taskId;
	private short state;
	private long uid;
	private String name;
	private long date;
	
	public MySqlUserTaskLog(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated constructor stub
		this.init(resultSet);
	}

	@Override
	public void init(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated method stub
		this.id = resultSet.getInt("id");
		this.taskId = resultSet.getInt("taskId");
		this.state = resultSet.getShort("state");
		this.uid = resultSet.getLong("uid");
		this.name = resultSet.getString("name");
		this.date = resultSet.getLong("date");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public short getState() {
		return state;
	}

	public void setState(short state) {
		this.state = state;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

}
