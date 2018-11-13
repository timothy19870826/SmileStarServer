package com.bigcat.mysql.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bigcat.mysql.MySqlDBConnection;

public class MySqlUserMoneyLog extends MySqlTableData {
	
	private static final String tableName = "user_capital_log";
	
	public static List<MySqlUserMoneyLog> queryPage(MySqlDBConnection connection, int offset, int limit) 
			throws SQLException, InstantiationException, IllegalAccessException {
		return MySqlTable.queryPage(connection, MySqlUserMoneyLog.class, MySqlUserMoneyLog.tableName, offset, limit);
	}
	
	public static void addLog(MySqlDBConnection connection, long uid, int expenditure, int income, int surplusFund, String desc) 
			throws SQLException {
		PreparedStatement unlockPreparedStatement = connection.prepareStatement("unlock tables");
		unlockPreparedStatement.executeUpdate();
		String sqlCmd = String.format("INSERT INTO %s(uid,expenditure,income,surplusFund,detail,date) VALUES(?,?,?,?,?,?)", tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		preparedStatement.setLong(1, uid);
		preparedStatement.setInt(2, expenditure);
		preparedStatement.setInt(3, income);
		preparedStatement.setInt(4, surplusFund);
		preparedStatement.setString(5, desc);
		Date now = new Date();
		preparedStatement.setLong(6, now.getTime());
		preparedStatement.executeUpdate();
	}
	
	public static List<MySqlUserMoneyLog> findByUserUid(MySqlDBConnection connection, long uid, int pageIdx, int pageSize)
			throws SQLException {
		String sqlCmd = String.format("SELECT * FROM %s WHERE uid=? ORDER BY id LIMIT ? OFFSET ?", tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		preparedStatement.setLong(1, uid);
		preparedStatement.setInt(2, pageSize);
		preparedStatement.setInt(3, pageSize * pageIdx);
		ResultSet resultSet = preparedStatement.executeQuery();

		List<MySqlUserMoneyLog> result = new ArrayList<MySqlUserMoneyLog>();
		while (resultSet.next()) {	
			result.add(new MySqlUserMoneyLog(resultSet));
		}
		
		resultSet.close();	
		return result;
	}
	
	private int expenditure; //支出
	private int income; //收入
	private int surplusFund; //结余
	private long uid; //用户uid
	private String desc; //描述
	private long date;

	public MySqlUserMoneyLog(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated constructor stub
		this.init(resultSet);
	}

	@Override
	public void init(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated method stub
		this.expenditure = resultSet.getInt("expenditure");
		this.income = resultSet.getInt("income");
		this.surplusFund = resultSet.getInt("surplusFund");
		this.uid = resultSet.getLong("uid");
		this.desc = resultSet.getString("detail");
		this.date = resultSet.getLong("date");
	}

	public int getExpenditure() {
		return expenditure;
	}

	public void setExpenditure(int expenditure) {
		this.expenditure = expenditure;
	}

	public int getIncome() {
		return income;
	}

	public void setIncome(int income) {
		this.income = income;
	}

	public int getSurplusFund() {
		return surplusFund;
	}

	public void setSurplusFund(int surplusFund) {
		this.surplusFund = surplusFund;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

}
