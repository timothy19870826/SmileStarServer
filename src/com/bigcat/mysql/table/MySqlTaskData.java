package com.bigcat.mysql.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bigcat.mysql.MySqlDBConnection;

public class MySqlTaskData extends MySqlTableData {

	public static final String tableName = "task_item_list";
	
	public static List<MySqlTaskData> queryPage(MySqlDBConnection connection, int offset, int limit, boolean active) 
			throws SQLException {

		String sqlCmd = active ? 
				String.format("SELECT * FROM %s WHERE state==1 ORDER BY id LIMIT ? OFFSET ? ", tableName) :
				String.format("SELECT * FROM %s ORDER BY id LIMIT ? OFFSET ?", tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		preparedStatement.setInt(1, limit);
		preparedStatement.setInt(2, offset);
		ResultSet resultSet = preparedStatement.executeQuery();

		MySqlTaskData data = null;
		List<MySqlTaskData> result = new ArrayList<MySqlTaskData>();
		while (resultSet.next()) {	
			data = new MySqlTaskData(resultSet);
			result.add(data);
		}
		
		resultSet.close();	
		return result;
		
	}
	
	public static void addTask(MySqlDBConnection connection, String name, String desc, String publisher,
			int awardVal, int needPerson, short expire) 
			throws SQLException {
		String sqlCmd = String.format(
				"INSERT INTO %s(name,awardVal,needPersonNum,expire,date,publisher,detail) VALUES(?,?,?,?,?,?,?)",
				tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		preparedStatement.setString(1, name);
		preparedStatement.setInt(2, awardVal);
		preparedStatement.setInt(3, needPerson);
		preparedStatement.setInt(4, expire);
		Date now = new Date();
		preparedStatement.setLong(5, now.getTime());
		preparedStatement.setString(6, publisher);
		preparedStatement.setString(7, desc);
		preparedStatement.executeUpdate();
	}
	
	public static void closeTask(MySqlDBConnection connection, int id) 
			throws SQLException {
		String sqlCmd = String.format("UPDATE %s SET state=2 WHERE id=?", tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		preparedStatement.setInt(1, id);
		preparedStatement.executeUpdate();
	}
	
	public static void updateTask(MySqlDBConnection connection, int id, int curPerson) 
			throws SQLException {
		String sqlCmd = String.format("UPDATE %s SET curPersonNum=? WHERE id=?", tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		preparedStatement.setInt(1, curPerson);
		preparedStatement.setInt(2, id);
		preparedStatement.executeUpdate();
	}
	
	private int id;
	private String name;
	private String desc;
	private short needPerson;
	private short curPerson;
	private int awardVal;
	private short state; // 1：进行中，2：已结束，3：已过期
	private long date;
	private short expire;
	
	public MySqlTaskData() {
		// TODO Auto-generated constructor stub
	}
	
	public MySqlTaskData(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated constructor stub
		this.init(resultSet);
	}

	@Override
	public void init(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated method stub
		this.id = resultSet.getInt("id");
		this.name = resultSet.getString("name");
		this.desc = resultSet.getString("detail");
		this.needPerson = resultSet.getShort("needPersonNum");
		this.curPerson = resultSet.getShort("curPersonNum");
		this.awardVal = resultSet.getInt("awardVal");
		this.state = resultSet.getShort("state");
		this.date = resultSet.getLong("date");
		this.expire = resultSet.getShort("expire");
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public short getNeedPerson() {
		return needPerson;
	}
	public void setNeedPerson(short needPerson) {
		this.needPerson = needPerson;
	}
	public short getCurPerson() {
		return curPerson;
	}
	public void setCurPerson(short curPerson) {
		this.curPerson = curPerson;
	}

	public int getAwardVal() {
		return awardVal;
	}
	public void setAwardVal(int awardVal) {
		this.awardVal = awardVal;
	}

	public short getState() {
		return state;
	}

	public void setState(short state) {
		this.state = state;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public short getExpire() {
		return expire;
	}

	public void setExpire(short expire) {
		this.expire = expire;
	}

}
