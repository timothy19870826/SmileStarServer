package com.bigcat.mysql.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bigcat.mysql.MySqlDBConnection;

public class MySqlAdminData extends MySqlTableData {

	
	public static final String tableName = "admin_account";

	public static MySqlAdminData findByAccountPwd(MySqlDBConnection connection, String account, String password) throws SQLException {
		String sqlCmd = String.format("SELECT * FROM %s WHERE BINARY account=? AND password=?", MySqlAdminData.tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		preparedStatement.setString(1, account);
		preparedStatement.setString(2, password);
		ResultSet resultSet = preparedStatement.executeQuery();

		MySqlAdminData data = null;
		if (resultSet.next()) {	
			data = new MySqlAdminData(resultSet);
		}

		resultSet.close();
		return data;
	}
	
	private String account;
	private String password;
	private String desc;
	
	public MySqlAdminData() {
		// TODO Auto-generated constructor stub
	}
	
	public MySqlAdminData(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated constructor stub
		this.init(resultSet);
	}

	@Override
	public void init(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated method stub
		this.account = resultSet.getString("account");
		this.password = resultSet.getString("password");
		this.desc = resultSet.getString("desc");
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	
}
