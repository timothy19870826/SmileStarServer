package com.bigcat.mysql.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bigcat.mysql.MySqlDBConnection;

public class MySqlGlobalInfo {
	
	public static final String tableName = "global_info";
	
	public static MySqlGlobalInfo loadGlobalInfo(MySqlDBConnection connection) throws SQLException {
		String sqlCmd = String.format("SELECT * FROM %s WHERE id=0", MySqlGlobalInfo.tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		ResultSet resultSet = preparedStatement.executeQuery();

		MySqlGlobalInfo data = null;
		if (resultSet.next()) {	
			data = new MySqlGlobalInfo(resultSet);
		}

		resultSet.close();
		return data;
		
	}
	
	public static void writeGlobalInfo(MySqlDBConnection connection, long totalRegister) throws SQLException {
		String sqlCmd = String.format("UPDATE %s SET totalRegister=?", MySqlGlobalInfo.tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		preparedStatement.setLong(1, totalRegister);
		preparedStatement.executeUpdate();
		
	}

	private long totalRegister;
	private long validUser;
	
	public MySqlGlobalInfo(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated constructor stub
		this.totalRegister = resultSet.getLong("totalRegister");
		this.validUser = resultSet.getLong("validUser");
	}
	
	public long getTotalRegister() {
		return totalRegister;
	}
	public void setTotalRegister(long totalRegister) {
		this.totalRegister = totalRegister;
	}
	public long getValidUser() {
		return validUser;
	}
	public void setValidUser(long validUser) {
		this.validUser = validUser;
	}	

}
