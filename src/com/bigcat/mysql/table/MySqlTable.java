package com.bigcat.mysql.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bigcat.mysql.MySqlDBConnection;

public class MySqlTable extends MySqlTableData {

	public static <T extends MySqlTableData> List<T> queryPage(
			MySqlDBConnection connection, Class<T> cls, String tableName, int offset, int limit)  
			throws SQLException, InstantiationException, IllegalAccessException {

		String sqlCmd = String.format("SELECT * FROM %s ORDER BY id LIMIT ? OFFSET ?", tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		preparedStatement.setInt(1, limit);
		preparedStatement.setInt(2, offset);
		ResultSet resultSet = preparedStatement.executeQuery();

		T data = null;
		List<T> result = new ArrayList<T>();
		while (resultSet.next()) {	
			data = cls.newInstance();
			data.init(resultSet);
			result.add(data);
		}
		
		resultSet.close();
		return result;
		
	}

	
	public static <T extends MySqlTableData> T findItemById(MySqlDBConnection connection, Class<T> cls, String tableName, int id) 
			throws SQLException, InstantiationException, IllegalAccessException {
		
		String sqlCmd = String.format("SELECT * FROM %s WHERE id=?", tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();

		T result = null;
		if (resultSet.next()) {	
			result = cls.newInstance();
			result.init(resultSet);
		}
		
		resultSet.close();
		return result;
	}
	
	public static void deleteItem(MySqlDBConnection connection, String tableName, int id) throws SQLException {
		String sqlCmd = String.format("DELETE FROM %s WHERE id=?", tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		preparedStatement.setInt(1, id);
		preparedStatement.executeUpdate();
	}
	
	
	private static final String tableName = "TABLES";
	
	public static MySqlTable findTable(MySqlDBConnection connection, String schema, String name) throws SQLException {

		String sqlCmd = String.format("SELECT TABLE_ROWS FROM %s WHERE BINARY TABLE_SCHEMA=? AND TABLE_NAME=?", MySqlTable.tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		preparedStatement.setString(1, schema);
		preparedStatement.setString(2, name);
		ResultSet resultSet = preparedStatement.executeQuery();

		MySqlTable result = new MySqlTable(schema, name);
		if (resultSet.next()) {
			result.init(resultSet);
		}
		
		resultSet.close();
		return result;
	}
	
	private String schema;
	private String name;
	private int rows;


	public MySqlTable(String tableSchema, String tableName) {
		// TODO Auto-generated constructor stub
		this.rows = 0;
		this.name = tableName;
		this.schema = tableSchema;
	}

	public void init(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated method stub
		this.rows = resultSet.getInt("TABLE_ROWS");
	}
	
	public int getPageSize(int offset, int pageSize) {
		if (this.rows == 0 || this.rows < offset) {
			return 0;
		}
		
		if (offset + pageSize > this.rows) {
			return this.rows - offset;
		}
		
		return pageSize;
	}


	public String getSchema() {
		return schema;
	}


	public void setSchema(String schema) {
		this.schema = schema;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getRows() {
		return rows;
	}


	public void setRows(int rows) {
		this.rows = rows;
	}
		
}
