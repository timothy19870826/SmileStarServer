package com.bigcat.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GlobalInfoTab extends TableBase {

	public GlobalInfoTab(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "global_info";
	}
	
	public GlobalInfoData getGlobalInfoData() throws SQLException {

		setAutoCommit(false);
		String sqlCmd = String.format("SELECT * FROM %s WHERE BINARY id=0", getTableName());
		PreparedStatement preparedStatement = getConnection().prepareStatement(sqlCmd);
		ResultSet resultSet = preparedStatement.executeQuery();
		commit();

		GlobalInfoData result = null;
		if (resultSet.next()) {	
			result = new GlobalInfoData(resultSet);
		}
		else {
			result = null;
		}
		
		resultSet.close();
		preparedStatement.close();
		
		return result;
	}
	
	public void setGlobalInfoData(long totalRegister, long validUser) throws SQLException {
		
		setAutoCommit(false);
		String sqlCmd = String.format("Update %s set totalRegister=?, validUser=? WHERE id=0", getTableName());
		PreparedStatement preparedStatement = getConnection().prepareStatement(sqlCmd);
		preparedStatement.setLong(1, totalRegister);
		preparedStatement.setLong(2, validUser);
		preparedStatement.executeUpdate();
		commit();
		preparedStatement.close();
	}

	
	public class GlobalInfoData{
		
		private long totalRegister;
		private long validUser;
		
		public GlobalInfoData(ResultSet resultSet) throws SQLException {
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
}
