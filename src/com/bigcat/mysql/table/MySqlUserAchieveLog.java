package com.bigcat.mysql.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.bigcat.mysql.MySqlDBConnection;

public class MySqlUserAchieveLog extends MySqlTableData {
	
	private static final String tableName = "user_achieve_log";
	
	public static List<MySqlUserAchieveLog> queryPage(MySqlDBConnection connection, int offset, int limit) 
			throws SQLException, InstantiationException, IllegalAccessException {
		return MySqlTable.queryPage(connection, MySqlUserAchieveLog.class, MySqlUserAchieveLog.tableName, offset, limit);
	}
	
	public static void addLog(MySqlDBConnection connection, int uid, int achieveId) throws SQLException {
		String sqlCmd = String.format("INSERT INTO %s(uid,achieveId,date) VALUES(?,?,?)", tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		preparedStatement.setLong(1, uid);
		preparedStatement.setInt(2, achieveId);
		Date now = new Date();
		preparedStatement.setLong(3, now.getTime());
		preparedStatement.executeUpdate();
	}
	
	private int id;
	private long uid;
	private int achieveId;
	private long date;
	public MySqlUserAchieveLog(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated constructor stub
		this.init(resultSet);
	}

	@Override
	public void init(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated method stub
		this.id = resultSet.getInt("id");
		this.uid = resultSet.getLong("uid");
		this.achieveId = resultSet.getInt("achieveId");
		this.date = resultSet.getLong("date");
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public int getAchieveId() {
		return achieveId;
	}
	public void setAchieveId(int achieveId) {
		this.achieveId = achieveId;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}

}
