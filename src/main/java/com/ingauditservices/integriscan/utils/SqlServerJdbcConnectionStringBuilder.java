package com.ingauditservices.integriscan.utils;

public class SqlServerJdbcConnectionStringBuilder {
	public static String buildConnectionString(
		String serverHost,
		String databaseName,
		String username,
		String password
	) {
		StringBuilder connectionString = new StringBuilder();
		connectionString.append("jdbc:sqlserver://")
			.append(serverHost)
			.append(";databaseName=")
			.append(databaseName)
			.append(";user=")
			.append(username)
			.append(";password=")
			.append(password)
			.append(";trustServerCertificate=true")
			.append(";loginTimeout=10;");
		return connectionString.toString();
	}
}
