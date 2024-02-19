package com.ingauditservices.integriscan;

import javax.swing.*;
import java.util.List;
import com.ingauditservices.integriscan.dao.ReferentialIntegrityAnomalyDao;
import com.ingauditservices.integriscan.dtos.DataAnomaly;
import com.ingauditservices.integriscan.dtos.DisabledCheckConstraintsAnomaly;
import com.ingauditservices.integriscan.dtos.DisabledTriggerAnomaly;
import com.ingauditservices.integriscan.dtos.FakeForeignKeyAnomaly;
import com.ingauditservices.integriscan.dtos.IsolatedTableAnomaly;
import com.ingauditservices.integriscan.sql.SqlServerConnection;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AnomalyFrame extends JFrame {

    private String connectionString;
    private ReferentialIntegrityAnomalyDao anomalyDao;

    public AnomalyFrame(String connectionString, String databaseName) {
        this.connectionString = connectionString;

        setTitle("Anomalias de la base de datos " + databaseName);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            SqlServerConnection.initialize(connectionString);
            anomalyDao = ReferentialIntegrityAnomalyDao.getInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al inicializar la conexión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        

        JPanel sidebarPanel = new JPanel();
        JPanel contentPanel = new JPanel();
        sidebarPanel.setLayout(new GridLayout(5, 1));

        JButton dataAnomaliesButton = new JButton("Anomalías de Datos");
        JButton disabledCheckConstraintsButton = new JButton("Anomalías de Constraints Deshabilitadas");
        JButton disabledTriggerButton = new JButton("Anomalías de Triggers Deshabilitados");
        JButton isolatedTablesButton = new JButton("Anomalías de Tablas Aisladas");
        JButton fakeForeignKeysButton = new JButton("Anomalías de Foreign Keys Falsos");

        sidebarPanel.add(dataAnomaliesButton);
        sidebarPanel.add(disabledCheckConstraintsButton);
        sidebarPanel.add(disabledTriggerButton);
        sidebarPanel.add(isolatedTablesButton);
        sidebarPanel.add(fakeForeignKeysButton);

        contentPanel.setLayout(new BorderLayout());
        JTextArea anomalyDetailsTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(anomalyDetailsTextArea);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        dataAnomaliesButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					List<DataAnomaly> dataAnomaly = anomalyDao.getAllDataAnomalies();
					if (dataAnomaly.size() == 0 ) {
						anomalyDetailsTextArea.setText("No se han encontrado anomalias en los datos");
					} else {
						StringBuilder anomaliesText = new StringBuilder();
                        anomaliesText.append("Anomalías de Datos: \n");
                        for (DataAnomaly dataAnomaly1 : dataAnomaly) {
                            anomaliesText.append("-----------------------------\n");
                            anomaliesText.append("Tabla: ")
                            .append(dataAnomaly1.getTableName())
                            .append("\n")
                            .append("Constraint: ")
                            .append(dataAnomaly1.getConstraintName())
                            .append("\n")
                            .append(" Ubicacion: ")
                            .append(dataAnomaly1.getAnomalyLocation())
                            .append("\n");
                        }
                        anomalyDetailsTextArea.setText(anomaliesText.toString());
					}
				} catch (SQLException e1) {
					anomalyDetailsTextArea.setText("Fracasamos\n" + e1.getMessage());
                    e1.printStackTrace();
				}
				
			}
		});

        disabledCheckConstraintsButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					List<DisabledCheckConstraintsAnomaly> disabledCheckConstraintsAnomaly = anomalyDao.getAllDisabledCheckConstraintsAnomalies();
					
					if (disabledCheckConstraintsAnomaly.size() == 0 ) {
						anomalyDetailsTextArea.setText("No se han encontrado constraints inhabilitados");
					} else {
						StringBuilder anomaliesText = new StringBuilder();
                        anomaliesText.append("Anomalías de Constraints Deshabilitados:\n");
                        for (DisabledCheckConstraintsAnomaly disabledCheckConstraintsAnomaly1 : disabledCheckConstraintsAnomaly) {
                            anomaliesText.append("-----------------------------\n");
                            anomaliesText.append("Tabla: ")
                            .append(disabledCheckConstraintsAnomaly1.getTableName())
                            .append("\n")
                            .append("Constraint: ")
                            .append(disabledCheckConstraintsAnomaly1.getConstraintName())
                            .append("\n");
                        }
                        anomalyDetailsTextArea.setText(anomaliesText.toString());
					}
					
				} catch (Exception e2) {
					anomalyDetailsTextArea.setText("Fracasamos\n" + e2.getMessage());
                    e2.printStackTrace();
				}
				
			}
		});
        
        disabledTriggerButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					List<DisabledTriggerAnomaly> disabledTriggerAnomaly = anomalyDao.getAllDisabledTriggerAnomalies();
					
					if (disabledTriggerAnomaly.size() == 0 ) {
						anomalyDetailsTextArea.setText("No se han encontrado triggers inhabilitados");
					} else {
						StringBuilder anomaliesText = new StringBuilder();
                        anomaliesText.append("Anomalías de Triggers Deshabilitados:\n");
                        for (DisabledTriggerAnomaly disabledTriggerAnomaly1 : disabledTriggerAnomaly) {
                            anomaliesText.append("-----------------------------\n");
                            anomaliesText.append("Tabla: ")
                            .append(disabledTriggerAnomaly1.getTableName())
                            .append("\n")
                            .append("Trigger: ")
                            .append(disabledTriggerAnomaly1.getTriggerName())
                            .append("\n");
                        }
                        anomalyDetailsTextArea.setText(anomaliesText.toString());
					}
					
				} catch (Exception e2) {
					anomalyDetailsTextArea.setText("Fracasamos\n" + e2.getMessage());
                    e2.printStackTrace();
				}
				
			}
		});
        isolatedTablesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<IsolatedTableAnomaly> isolatedTableAnomalies = anomalyDao.getAllIsolatedTableAnomalies();

                    if (isolatedTableAnomalies.size() == 0) {
                        anomalyDetailsTextArea.setText("No se han encontrado errores de integridad referencial");
                    } else {
                        StringBuilder anomaliesText = new StringBuilder();
                        anomaliesText.append("Anomalías de Tablas Aisladas:\n");
                        for (IsolatedTableAnomaly isolatedTableAnomaly : isolatedTableAnomalies) {
                            anomaliesText.append("-----------------------------\n");
                            anomaliesText.append("Tabla: ")
                            .append(isolatedTableAnomaly.getTableName())
                            .append("\n");
                        }
                        anomalyDetailsTextArea.setText(anomaliesText.toString());
                    }
                } catch (Exception ex) {
                    anomalyDetailsTextArea.setText("Fracasamos\n" + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        fakeForeignKeysButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					List<FakeForeignKeyAnomaly> fakeForeignKeyAnomaly = anomalyDao.getAllFakeForeignKeyAnomalies();
					if (fakeForeignKeyAnomaly.size() == 0) {
                        anomalyDetailsTextArea.setText("No se han encontrado llaves foraneas falsas");
                    } else {
                        StringBuilder anomaliesText = new StringBuilder();
                        anomaliesText.append("Anomalias de llaves fornaeas falsas:\n");
                        for (FakeForeignKeyAnomaly fakeForeignKeyAnomaly1 : fakeForeignKeyAnomaly) {
                            anomaliesText.append("-----------------------------\n");
                            anomaliesText.append("Tabla: ")
                            .append(fakeForeignKeyAnomaly1.getTableName())
                            .append("\n")
                            .append("Columna: ")
                            .append(fakeForeignKeyAnomaly1.getColumnName())
                            .append("\n");
                        }
                        anomalyDetailsTextArea.setText(anomaliesText.toString());
                    }
				} catch (SQLException e1) {
					anomalyDetailsTextArea.setText("Fracasamos\n" + e1.getMessage());
					e1.printStackTrace();
				}
				
				
			}
		});
        
        setLayout(new BorderLayout());
        add(sidebarPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    private void closeConnection() {
        try {
            SqlServerConnection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cerrar la conexión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
