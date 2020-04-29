package com.example.CRUDVaadin.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

public class PostgresHelper {

	private Connection conn;
	private String host;
	private String dbName;
	private String user;
	private String pass;
	
	//we don't like this constructor
	protected PostgresHelper() {}
	
	public PostgresHelper(String host, String dbName, String user, String pass) {
		this.host = host;
		this.dbName = dbName;
		this.user = user;
		this.pass = pass;
	}
	
	public boolean connect() throws SQLException, ClassNotFoundException {
		if (host.isEmpty() || dbName.isEmpty() || user.isEmpty() || pass.isEmpty()) {
			throw new SQLException("------------- Database credentials missing ------------------");
		}
		
		Class.forName("org.postgresql.Driver");
		this.conn = DriverManager.getConnection(this.host + this.dbName,this.user, this.pass);
		return true;
	}
	
	public int insert(String table, LinkedHashMap<String,Object> values) throws SQLException {
		
		StringBuilder columns = new StringBuilder();
		StringBuilder vals = new StringBuilder();
		
		for (String col : values.keySet()) {
			columns.append(col).append(",");
			
			if (values.get(col) instanceof String) {
				vals.append("'").append(values.get(col)).append("',");
			}else {
				vals.append(values.get(col)).append(",");
			}
		}
		
		columns.setLength(columns.length()-1);
		vals.setLength(vals.length()-1);

		String query = String.format("INSERT INTO %s (%s) VALUES (%s)", table, columns.toString(), vals.toString());
		//String sql = "INSERT INTO \"Test1\" (\"ID\",name,address)" + "VALUES " + "(4,'C','D')";
		
		return this.conn.createStatement().executeUpdate(query);
	}
}