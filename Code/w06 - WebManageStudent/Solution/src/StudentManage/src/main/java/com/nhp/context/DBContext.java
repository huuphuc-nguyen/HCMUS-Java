package com.nhp.context;
import com.nhp.entity.*;

import java.util.List;
import java.util.ArrayList;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBContext {
	
	public Connection getConnection() throws Exception {
		final String serverName = "DESKTOP-IT54C15\\SQLEXPRESS";
		final String dbName = "QLSV";
		final String portNumber = "1433";
		final String userID = "DESKTOP-IT54C15\\lenovo";
		final String password = "";
		final String ssl = "false";

		try {
			// Register JDBC driver
		     Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		      
		   // Open a connection
		     String url = "jdbc:sqlserver:// DESKTOP-IT54C15\\SQLEXPRESS;databaseName=QLSV;encrypt=false;integratedSecurity=true;";
			   
			 Connection conn = DriverManager.getConnection(url, userID, password);
			 
			 return conn;
		}catch (SQLException se) {
			System.out.print("LOI KET NOI SQL");
		      se.printStackTrace();
		}
		return null;
	}
}	
