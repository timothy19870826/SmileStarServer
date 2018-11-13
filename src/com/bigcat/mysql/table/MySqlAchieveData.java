package com.bigcat.mysql.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.bigcat.mysql.MySqlDBConnection;

public class MySqlAchieveData extends MySqlTableData {
	
	private static final String tableName = "achieve_list";
	
	public static List<MySqlAchieveData> queryPage(MySqlDBConnection connection, int offset, int limit) 
			throws SQLException, InstantiationException, IllegalAccessException {
		return MySqlTable.queryPage(connection, MySqlAchieveData.class, MySqlAchieveData.tableName, offset, limit);
	}

	private int id;
	private String name;
	private String condition;
	public MySqlAchieveData() {
		// TODO Auto-generated constructor stub
	}
	
	public MySqlAchieveData(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated constructor stub
		this.init(resultSet);
	}

	@Override
	public void init(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated method stub
		this.id = resultSet.getInt("id");
		this.name = resultSet.getString("name");
		this.condition = resultSet.getString("condition");
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
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}

}
