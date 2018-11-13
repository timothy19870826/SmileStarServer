package com.bigcat.mysql.table;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class MySqlTableData {
	
	public MySqlTableData() {
		// TODO Auto-generated constructor stub
	}
	
	public abstract void init(ResultSet resultSet) throws SQLException;
}
