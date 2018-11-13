package com.bigcat.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class TaskTableInMySql extends TableBase {

	public TaskTableInMySql(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "task_item_list";
	}
	
	public ArrayList<TaskData> getAwardDatas(int pageIdx, int pageSize) 
			throws ClassNotFoundException, SQLException {
		setAutoCommit(false);
		String sqlCmd = String.format("SELECT * FROM %s LIMIT ? OFFSET ?", getTableName());
		PreparedStatement preparedStatement = getConnection().prepareStatement(sqlCmd);
		preparedStatement.setInt(1, pageSize);
		preparedStatement.setInt(2, pageSize * pageIdx);
		ResultSet resultSet = preparedStatement.executeQuery();
		commit();

		ArrayList<TaskData> result = new ArrayList<TaskData>();
		while (resultSet.next()) {	
			result.add(new TaskData(resultSet));
		}
		
		resultSet.close();
		preparedStatement.close();
		
		return result;
	}
	
	public boolean addData(String name, String imgUrl, float price, int count) 
			throws ClassNotFoundException, SQLException {
		setAutoCommit(false);
		String sqlCmd = String.format("INSERT INTO %s(name,img_url,price,count) VALUES(?,?,?,?)", getTableName());
		PreparedStatement preparedStatement = getConnection().prepareStatement(sqlCmd);
		preparedStatement.setString(1, name);
		preparedStatement.setString(2, imgUrl);
		preparedStatement.setFloat(3, price);
		preparedStatement.setInt(4, count);
		preparedStatement.executeUpdate();
		commit();
		preparedStatement.close();
		return false;
	}
	
	public boolean delData(int id) 
			throws ClassNotFoundException, SQLException {
		setAutoCommit(false);
		String sqlCmd = String.format("DELETE FROM %s WHERE id=?", getTableName());
		PreparedStatement preparedStatement = getConnection().prepareStatement(sqlCmd);
		preparedStatement.setInt(1, id);
		preparedStatement.executeUpdate();
		commit();
		preparedStatement.close();
		return false;
	}
	
	public TaskData findData(int id) 
			throws ClassNotFoundException, SQLException {
		String sqlCmd = String.format("SELECT * FROM %s WHERE id=?", getTableName());
		PreparedStatement preparedStatement = getConnection().prepareStatement(sqlCmd);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		commit();

		TaskData result = null;
		if (resultSet.next()) {	
			result = new TaskData(resultSet);
		}
		else {
			result = null;
		}
		
		resultSet.close();
		preparedStatement.close();
		return result;
	}
	
	public boolean updateAwardCount(int id, int count) 
			throws ClassNotFoundException, SQLException {

		String sqlCmd = String.format("UPDATE %s SET count=? WHERE id=? AND count>?", getTableName());
		PreparedStatement preparedStatement = getConnection().prepareStatement(sqlCmd);
		preparedStatement.setInt(1, count);
		preparedStatement.setInt(2, id);
		preparedStatement.setInt(3, count);
		commit();
		preparedStatement.close();
		return true;		
	}

	public class TaskData{
		private int id;
		private String name;
		private String desc;
		private short needPerson;
		private float awardVal;
		
		public TaskData(ResultSet resultSet) throws SQLException {
			this.id = resultSet.getInt("id");
			this.name = resultSet.getString("name");
			this.desc = resultSet.getString("desc");
			this.needPerson = resultSet.getShort("needPersonNum");
			this.awardVal = resultSet.getFloat("awardVal");
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
		public float getAwardVal() {
			return awardVal;
		}
		public void setAwardVal(float awardVal) {
			this.awardVal = awardVal;
		}
	}
}
