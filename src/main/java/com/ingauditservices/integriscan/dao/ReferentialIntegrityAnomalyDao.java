package com.ingauditservices.integriscan.dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ingauditservices.integriscan.dtos.DataAnomaly;
import com.ingauditservices.integriscan.dtos.DisabledCheckConstraintsAnomaly;
import com.ingauditservices.integriscan.dtos.DisabledTriggerAnomaly;
import com.ingauditservices.integriscan.dtos.FakeForeignKeyAnomaly;
import com.ingauditservices.integriscan.dtos.IsolatedTableAnomaly;

public class ReferentialIntegrityAnomalyDao {
	private final String DATA_ANOMALIES_QUERY = """
			DBCC CHECKCONSTRAINTS WITH ALL_CONSTRAINTS;
			""";
	
	private final String DISABLED_CHECK_CONSTRAINTS_ANOMALIES_QUERY = """
			SELECT
				QUOTENAME(SCHEMA_NAME(t.schema_id)) + '.' + QUOTENAME(t.name) AS 'Table',
				c.name AS 'Check Constraint'
			FROM sys.tables t
			JOIN sys.foreign_keys c ON t.object_id = c.parent_object_id
			WHERE c.is_disabled = 1;
			""";
	
	private final String DISABLED_TRIGGERS_ANOMALIES_QUERY = """
			SELECT
				QUOTENAME(SCHEMA_NAME(t.schema_id)) + '.' + QUOTENAME(t.name) AS 'Table',
				tr.name AS 'Trigger'
			FROM sys.tables t
			JOIN sys.triggers tr ON t.object_id = tr.parent_id
			WHERE tr.is_disabled = 1;
			""";
	
	private final String ISOLATED_TABLES_ANOMALIES_QUERY = """
			SELECT
				QUOTENAME(SCHEMA_NAME(t.schema_id)) + '.' + QUOTENAME(t.name) AS 'Table'
			FROM
				sys.tables t
			WHERE
				NOT EXISTS (
					SELECT 1
					FROM sys.foreign_keys fk
					WHERE fk.parent_object_id = t.object_id OR fk.referenced_object_id =
			t.object_id
				)
				AND t.is_ms_shipped = 0;
			""";
	
	private final String FAKE_FOREIGN_KEYS_ANOMALIES_QUERY = """
			SELECT
				QUOTENAME(SCHEMA_NAME(t.schema_id)) + '.' + QUOTENAME(t.name) AS 'Table',
				c.name AS 'Column'
			FROM
				sys.tables t
			JOIN
				sys.columns c ON t.object_id = c.object_id
			WHERE
				c.name IN (
				SELECT col_name(ic.object_id, ic.column_id) AS ColumnName
				FROM sys.indexes AS i
				INNER JOIN sys.index_columns AS ic ON i.object_id = ic.object_id AND
			i.index_id = ic.index_id
				WHERE i.is_primary_key = 1
				)
				AND NOT EXISTS (
				SELECT 1
				FROM sys.foreign_keys AS fk
				WHERE fk.parent_object_id = t.object_id OR fk.referenced_object_id =
			t.object_id
				)
				AND NOT EXISTS (
				SELECT 1
				FROM sys.index_columns AS ic
				JOIN sys.indexes AS i ON ic.object_id = i.object_id AND ic.index_id =
			i.index_id
				WHERE ic.object_id = t.object_id AND ic.column_id = c.column_id AND
			i.is_primary_key = 1
			);
			""";

	private Connection connection;

	public ReferentialIntegrityAnomalyDao(Connection connection) {
		this.connection = connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public List<DataAnomaly> getAllDataAnomalies() throws SQLException {
		List<DataAnomaly> dataAnomalies = new ArrayList<DataAnomaly>();
		
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery(DATA_ANOMALIES_QUERY);

		while (resultSet.next()) {
			String tableName = resultSet.getString(1);
			String constraintName = resultSet.getString(2);
			String anomalyLocation = resultSet.getString(3);
			DataAnomaly dataAnomaly = new DataAnomaly(tableName, constraintName, anomalyLocation);
			dataAnomalies.add(dataAnomaly);
		}
		
		resultSet.close();
		statement.close();

		return dataAnomalies;
	}

	public List<DisabledCheckConstraintsAnomaly> getAllDisabledCheckConstraintsAnomalies() throws SQLException {
		List<DisabledCheckConstraintsAnomaly> disabledCheckConstraintsAnomalies = new ArrayList<DisabledCheckConstraintsAnomaly>();
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery(DISABLED_CHECK_CONSTRAINTS_ANOMALIES_QUERY);

		while (resultSet.next()) {
			String tableName = resultSet.getString(1);
			String constraintName = resultSet.getString(2);
			DisabledCheckConstraintsAnomaly disabledCheckConstraintsAnomaly = new DisabledCheckConstraintsAnomaly(tableName, constraintName);
			disabledCheckConstraintsAnomalies.add(disabledCheckConstraintsAnomaly);
		}
		
		resultSet.close();
		statement.close();
		return disabledCheckConstraintsAnomalies;
	}

	public List<DisabledTriggerAnomaly> getAllDisabledTriggerAnomalies() throws SQLException {
		List<DisabledTriggerAnomaly> disabledTriggerAnomalies = new ArrayList<DisabledTriggerAnomaly>();
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery(DISABLED_TRIGGERS_ANOMALIES_QUERY);
		
		while(resultSet.next())
		{
			String tableName = resultSet.getString(1);
			String triggerName = resultSet.getString(2);
			DisabledTriggerAnomaly disabledTriggerAnomaly = new DisabledTriggerAnomaly(tableName, triggerName);
			disabledTriggerAnomalies.add(disabledTriggerAnomaly);
		}
		
		resultSet.close();
		statement.close();
		
		return disabledTriggerAnomalies;
	}

	public List<IsolatedTableAnomaly> getAllIsolatedTableAnomalies() throws SQLException {
		List<IsolatedTableAnomaly> isolatedTableAnomalies = new ArrayList<IsolatedTableAnomaly>();
		
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery(ISOLATED_TABLES_ANOMALIES_QUERY);
		
		while(resultSet.next())
		{
			String tableName = resultSet.getString(1);
			IsolatedTableAnomaly isolatedTableAnomaly = new IsolatedTableAnomaly(tableName);
			isolatedTableAnomalies.add(isolatedTableAnomaly);
		}
		
		resultSet.close();
		statement.close();
		
		return isolatedTableAnomalies;
	}
	
	public List<FakeForeignKeyAnomaly> getAllFakeForeignKeyAnomalies() throws SQLException {
		List<FakeForeignKeyAnomaly> fakeForeignKeyAnomalies = new ArrayList<FakeForeignKeyAnomaly>();
		
		Statement statement = this.connection.createStatement();
		ResultSet resultSet = statement.executeQuery(FAKE_FOREIGN_KEYS_ANOMALIES_QUERY);
		
		while(resultSet.next())
		{
			String tableName = resultSet.getString(1);
			String columnName = resultSet.getString(2);
			FakeForeignKeyAnomaly fakeForeignKeyAnomaly = new FakeForeignKeyAnomaly(tableName, columnName);
			fakeForeignKeyAnomalies.add(fakeForeignKeyAnomaly);
		}
		
		resultSet.close();
		statement.close();
		
		return fakeForeignKeyAnomalies;
	}
}
