package com.bigcat.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class AccountBaseInfoTab extends TableBase {
	
	public AccountBaseInfoTab(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "account_base_info";
	}
	
	public AccountBaseInfo findAccount(String name) 
			throws ClassNotFoundException, SQLException {
		
		setAutoCommit(false);
		String sqlCmd = String.format("SELECT * FROM %s WHERE BINARY name=?", getTableName());
		PreparedStatement preparedStatement = getConnection().prepareStatement(sqlCmd);
		preparedStatement.setString(1, name);
		ResultSet resultSet = preparedStatement.executeQuery();
		commit();

		AccountBaseInfo result = null;
		if (resultSet.next()) {	
			result = new AccountBaseInfo(resultSet);
		}
		else {
			result = null;
		}
		
		resultSet.close();
		preparedStatement.close();
		
		return result;
	}	
	
	public AccountBaseInfo findAccount(String name, String password) 
			throws ClassNotFoundException, SQLException {
		
		setAutoCommit(false);
		String sqlCmd = String.format("SELECT * FROM %s WHERE BINARY name=? and password=?", getTableName());
		PreparedStatement preparedStatement = getConnection().prepareStatement(sqlCmd);
		preparedStatement.setString(1, name);
		preparedStatement.setString(2, password);
		ResultSet resultSet = preparedStatement.executeQuery();
		commit();

		AccountBaseInfo result = null;
		if (resultSet.next()) {	
			result = new AccountBaseInfo(resultSet);
		}
		else {
			result = null;
		}
		
		resultSet.close();
		preparedStatement.close();
		
		return result;
	}
	
	public void addAccount(String name, String password, long uid) 
			throws ClassNotFoundException, SQLException {		
		setAutoCommit(false);
		String sqlCmd = String.format("INSERT INTO %s(name,password,reg_date,last_login_date,uid) VALUES(?,?,?,?,?)", getTableName());
		PreparedStatement preparedStatement = getConnection().prepareStatement(sqlCmd);
		preparedStatement.setString(1, name);
		preparedStatement.setString(2, password);
		Date now = new Date();
		preparedStatement.setLong(3, now.getTime());
		preparedStatement.setLong(4, now.getTime());
		preparedStatement.setLong(5, uid);
		preparedStatement.executeUpdate();
		commit();
		preparedStatement.close();
		
	}

	public void updateAccount(long id) throws SQLException {
		setAutoCommit(false);
		String sqlCmd = String.format("UPDATE %s set last_login_date=? where id=?", getTableName());
		PreparedStatement preparedStatement = getConnection().prepareStatement(sqlCmd);
		Date now = new Date();
		preparedStatement.setLong(1, now.getTime());
		preparedStatement.setLong(2, id);
		preparedStatement.executeUpdate();
		commit();
		preparedStatement.close();
	}

	public class AccountBaseInfo{

		private long id;
		private long uid;
		private String name;
		private String password;
		private String email;
		private String mobile;
		private String title;
		private long reg_date;
		private long last_login_date;
		private short level;
		private float capital_weibi;
		
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
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
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public long getReg_date() {
			return reg_date;
		}
		public void setReg_date(long reg_date) {
			this.reg_date = reg_date;
		}
		public long getLast_login_date() {
			return last_login_date;
		}
		public void setLast_login_date(long last_login_date) {
			this.last_login_date = last_login_date;
		}
		public short getLevel() {
			return level;
		}
		public void setLevel(short level) {
			this.level = level;
		}
		public float getCapital_weibi() {
			return capital_weibi;
		}
		public void setCapital_weibi(float capital_weibi) {
			this.capital_weibi = capital_weibi;
		}
		
		public AccountBaseInfo(ResultSet resultSet) throws SQLException {
			this.id = resultSet.getLong("id");
			this.uid = resultSet.getLong("uid");
			this.name = resultSet.getString("name");
			this.password = resultSet.getString("password");
			this.email = resultSet.getString("email");
			this.mobile = resultSet.getString("mobile");
			this.title = resultSet.getString("title");
			this.level = resultSet.getShort("level");
			this.capital_weibi = resultSet.getFloat("capital_weibi");
			this.reg_date = resultSet.getLong("reg_date");
			this.last_login_date = resultSet.getLong("last_login_date");
		}	
	}

}
