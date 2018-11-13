package com.bigcat.mysql.table;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.bigcat.mysql.MySqlDBConnection;


public class MySqlAwardData extends MySqlTableData {

	public static final String tableName = "award_item_list";
	
	public static List<MySqlAwardData> queryPage(MySqlDBConnection connection, int offset, int limit) 
			throws SQLException, InstantiationException, IllegalAccessException {
		return MySqlTable.queryPage(connection, MySqlAwardData.class, MySqlAwardData.tableName, offset, limit);
	}
	
	public static void addAwardData(MySqlDBConnection connection, String name, String imgUrl, int price, int count) 
			throws SQLException {
		String sqlCmd = String.format("INSERT INTO %s(name,imgUrl,price,count) VALUES(?,?,?,?)", tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		preparedStatement.setString(1, name);
		preparedStatement.setString(2, imgUrl);
		preparedStatement.setInt(3, price);
		preparedStatement.setInt(4, count);
		preparedStatement.executeUpdate();
	}
	
	public static void updateAwardCount(MySqlDBConnection connection, int id, int count) 
			throws SQLException {
		String sqlCmd = String.format("UPDATE %s SET count=? WHERE id=? AND count>?", tableName);
		PreparedStatement preparedStatement = connection.prepareStatement(sqlCmd);
		preparedStatement.setInt(1, count);
		preparedStatement.setInt(2, id);
		preparedStatement.setInt(3, count);
		preparedStatement.executeUpdate();
	}
		
	
	private int id;
	private String name;
	private String imgUrl;
	private short count;
	private int price;
	
	public MySqlAwardData() {
		// TODO Auto-generated constructor stub
	}
	
	public MySqlAwardData(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated constructor stub
		this.init(resultSet);
	}

	@Override
	public void init(ResultSet resultSet) throws SQLException {
		// TODO Auto-generated method stub
		this.id = resultSet.getInt("id");
		this.name = resultSet.getString("name");
		this.imgUrl = resultSet.getString("imgUrl");
		this.count = resultSet.getShort("count");
		this.price = resultSet.getInt("price");
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
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public short getCount() {
		return count;
	}
	public void setCount(short count) {
		this.count = count;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}

}
