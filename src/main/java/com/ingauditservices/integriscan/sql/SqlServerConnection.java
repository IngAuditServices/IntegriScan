package com.ingauditservices.integriscan.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.ingauditservices.integriscan.sql.exceptions.ConnectionInitializationException;

public class SqlServerConnection {
	private static SqlServerConnection instance = null;
	private Connection connection;
	
	private SqlServerConnection(String connectionString) throws SQLException {
		this.connection = DriverManager.getConnection(connectionString);
	}
	
	public static void initialize(String connectionString) throws SQLException
	{
		if(instance != null) {
			return;
		}
		
		instance = new SqlServerConnection(connectionString);
		
		return;
	}
	
	public static SqlServerConnection getInstance() throws ConnectionInitializationException {
		if(instance == null)
		{
			throw new ConnectionInitializationException("The connection must be initialized first. Try with SqlServerConnection.initialize(String");
		}
		return instance;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public static void close() throws SQLException {
		if(instance == null) {
			return;
		}
		
		instance.connection.close();
		instance = null;
	}
}


