package com.ingauditservices.integriscan;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.ingauditservices.integriscan.utils.SqlServerJdbcConnectionStringBuilder;

public class ConnectionSettingsFrame extends JFrame {
	private JTextField hostField, dbNameField, usernameField, passwordField;
	private JButton connectButton;

	public ConnectionSettingsFrame() {
		setTitle("Database Connection Settings");
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(5, 2, 5, 5));

		JLabel hostLabel = new JLabel("Server Host:");
		hostField = new JTextField(20);

		JLabel dbNameLabel = new JLabel("Database Name:");
		dbNameField = new JTextField(20);

		JLabel usernameLabel = new JLabel("Username:");
		usernameField = new JTextField(20);

		JLabel passwordLabel = new JLabel("Password:");
		passwordField = new JPasswordField(20);

		connectButton = new JButton("Connect");

		formPanel.add(hostLabel);
		formPanel.add(hostField);
		formPanel.add(dbNameLabel);
		formPanel.add(dbNameField);
		formPanel.add(usernameLabel);
		formPanel.add(usernameField);
		formPanel.add(passwordLabel);
		formPanel.add(passwordField);
		formPanel.add(new JLabel());
		formPanel.add(connectButton);

		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		JLabel titleLabel = new JLabel("Ingrese los Datos de Conexión");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

		titlePanel.add(titleLabel);

		mainPanel.add(titlePanel, BorderLayout.NORTH);
		mainPanel.add(formPanel, BorderLayout.CENTER);

		add(mainPanel);

		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String serverHost = hostField.getText();
				String dbName = dbNameField.getText();
				String username = usernameField.getText();
				String password = passwordField.getText();

				String connectionString = SqlServerJdbcConnectionStringBuilder.buildConnectionString(serverHost, dbName,
						username, password);
				AnomalyFrame anomalyFrame = new AnomalyFrame(connectionString, dbName);
				anomalyFrame.setVisible(true);
				dispose();
			}
		});

	}
}
