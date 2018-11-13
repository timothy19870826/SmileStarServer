package com.bigcat.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class AwardTableInMySql extends TableBase {

	public AwardTableInMySql(Connection connection) {
		super(connection);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "award_item_list";
	}
	
	public ArrayList<AwardData> getAwardDatas(int pageIdx, int pageSize) 
			throws ClassNotFoundException, SQLException {
		setAutoCommit(false);
		String sqlCmd = String.format("SELECT * FROM %s LIMIT ? OFFSET ?", getTableName());
		PreparedStatement preparedStatement = getConnection().prepareStatement(sqlCmd);
		preparedStatement.setInt(1, pageSize);
		preparedStatement.setInt(2, pageSize * pageIdx);
		ResultSet resultSet = preparedStatement.executeQuery();
		commit();

		ArrayList<AwardData> result = new ArrayList<AwardData>();
		while (resultSet.next()) {	
			result.add(new AwardData(resultSet));
		}
		
		resultSet.close();
		preparedStatement.close();
		
		return result;
	}
	
	public boolean addAwardData(String name, String imgUrl, float price, int count) 
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
	
	public boolean delAwardData(int id) 
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
	
	public AwardData findAwardData(int id) 
			throws ClassNotFoundException, SQLException {
		String sqlCmd = String.format("SELECT * FROM %s WHERE id=?", getTableName());
		PreparedStatement preparedStatement = getConnection().prepareStatement(sqlCmd);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		commit();

		AwardData result = null;
		if (resultSet.next()) {	
			result = new AwardData(resultSet);
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
		
	
	public class AwardData {
		private int id;
		private String name;
		private String imgUrl;
		private short count;
		private float price;
		
		public AwardData(ResultSet resultSet) throws SQLException {
			this.id = resultSet.getInt("id");
			this.name = resultSet.getString("name");
			this.imgUrl = resultSet.getString("img_url");
			this.count = resultSet.getShort("count");
			this.price = resultSet.getFloat("price");
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
		public float getPrice() {
			return price;
		}
		public void setPrice(float price) {
			this.price = price;
		}
	}

}
