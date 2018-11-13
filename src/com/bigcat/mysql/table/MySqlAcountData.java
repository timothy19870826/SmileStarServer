package com.bigcat.mysql.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.bigcat.mysql.MySqlDBConnection;

public class MySqlAcountData extends MySqlTableData {
	
	public static final String tableName = "account_base_info";
	
	public static List<MySqlAcountData> queryPage(MySqlDBConnection connection, int offset, int limit) 
			throws SQLException, InstantiationException, IllegalAccessException {
		return MySqlTable.queryPage(connection, MySqlAcountData.class, MySqlAcountData.tableName, offset, limit);
	}
	
	public static MySqlAcountData findByUid(MySqlDBConnection connection, long uid) throws SQLException {
		String sqlCmd = String.format("SELECT * FROM %s WHERE uid=?", MySqlAcountData.tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		preparedStatement.setLong(1, uid);
		ResultSet resultSet = preparedStatement.executeQuery();

		MySqlAcountData data = null;
		if (resultSet.next()) {	
			data = new MySqlAcountData(resultSet);
		}

		resultSet.close();
		return data;
		
	}
	
	public static MySqlAcountData findByAccount(MySqlDBConnection connection, String account) throws SQLException {
		String sqlCmd = String.format("SELECT * FROM %s WHERE BINARY name=?", MySqlAcountData.tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		preparedStatement.setString(1, account);
		ResultSet resultSet = preparedStatement.executeQuery();

		MySqlAcountData data = null;
		if (resultSet.next()) {	
			data = new MySqlAcountData(resultSet);
		}

		resultSet.close();
		return data;
		
	}
	
	public static MySqlAcountData findByAccountPwd(MySqlDBConnection connection, String account, String password) throws SQLException {
		String sqlCmd = String.format("SELECT * FROM %s WHERE BINARY name=? AND password=?", MySqlAcountData.tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		preparedStatement.setString(1, account);
		preparedStatement.setString(2, password);
		ResultSet resultSet = preparedStatement.executeQuery();

		MySqlAcountData data = null;
		if (resultSet.next()) {	
			data = new MySqlAcountData(resultSet);
		}

		resultSet.close();
		return data;
	}
	
	public static MySqlAcountData insert(MySqlDBConnection connection, String name, String password, long uid) throws SQLException {
		MySqlAcountData data = new MySqlAcountData(name, password, uid);
		String sqlCmd = String.format(
				"INSERT INTO %s(name,password,regDate,lastLoginDate,uid) VALUES(?,?,?,?,?)", MySqlAcountData.tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		preparedStatement.setString(1, name);
		preparedStatement.setString(2, password);
		preparedStatement.setLong(3, data.getRegDate());
		preparedStatement.setLong(4, data.getLastLoginDate());
		preparedStatement.setLong(5, uid);
		preparedStatement.executeUpdate();
		return data;
	}
	
	public static void updateLastLoginDate(MySqlDBConnection connection, long uid) throws SQLException {
		String sqlCmd = String.format("UPDATE %s set lastLoginDate=? where uid=?", MySqlAcountData.tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		Date now = new Date();
		preparedStatement.setLong(1, now.getTime());
		preparedStatement.setLong(2, uid);
		preparedStatement.executeUpdate();
	}
	
	public static void updateCapitalWeibi(MySqlDBConnection connection, long uid, int newCapital) throws SQLException {
		String sqlCmd = String.format("UPDATE %s set capitalWeibi=? where uid=?", MySqlAcountData.tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		preparedStatement.setInt(1, newCapital);
		preparedStatement.setLong(2, uid);
		preparedStatement.executeUpdate();
	}

	private long id;
	private long uid;
	private String name;
	private String password;
	private String email;
	private String mobile;
	private String title;
	private long regDate;
	private long lastLoginDate;
	private short level;
	private int capitalWeibi;
	
	public MySqlAcountData(String name, String password, long uid) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.password = password;
		this.uid = uid;
		Date now = new Date();
		this.regDate = now.getTime();
		this.lastLoginDate = now.getTime();
	}
	
	public MySqlAcountData(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated constructor stub
		this.init(resultSet);
	}

	@Override
	public void init(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated method stub
		this.id = resultSet.getLong("id");
		this.uid = resultSet.getLong("uid");
		this.name = resultSet.getString("name");
		this.password = resultSet.getString("password");
		this.email = resultSet.getString("email");
		this.mobile = resultSet.getString("mobile");
		this.title = resultSet.getString("title");
		this.level = resultSet.getShort("level");
		this.capitalWeibi = resultSet.getInt("capitalWeibi");
		this.regDate = resultSet.getLong("regDate");
		this.lastLoginDate = resultSet.getLong("lastLoginDate");
	}

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


	public long getRegDate() {
		return regDate;
	}


	public void setRegDate(long regDate) {
		this.regDate = regDate;
	}


	public long getLastLoginDate() {
		return lastLoginDate;
	}


	public void setLastLoginDate(long lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}


	public short getLevel() {
		return level;
	}


	public void setLevel(short level) {
		this.level = level;
	}


	public int getCapitalWeibi() {
		return capitalWeibi;
	}


	public void setCapitalWeibi(int capitalWeibi) {
		this.capitalWeibi = capitalWeibi;
	}	
	
}
