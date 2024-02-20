package com.ingauditservices.integriscan;

import javax.swing.*;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private static final Logger logger = LogManager.getLogger();

    public AnomalyFrame(String connectionString, String databaseName) {
        this.connectionString = connectionString;

        setTitle("Anomalias de la base de datos " + databaseName);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            SqlServerConnection.initialize(connectionString);
            logger.log(Level.INFO, "La conexión a la base de datos " + databaseName + " se ha realizado correctamente.");
            anomalyDao = ReferentialIntegrityAnomalyDao.getInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al inicializar la conexión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.ERROR, "Error al establecer la conexión: " + ex.getMessage());
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
					logger.log(Level.INFO, "Se ejecutó un análisis de anomalías en los datos en la base " + databaseName);
					if (dataAnomaly.size() == 0 ) {
						anomalyDetailsTextArea.setText("No se han encontrado anomalias en los datos");
						logger.log(Level.INFO, "No se han encontrado constraints inhabilitados en la base " + databaseName);
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
                        logger.log(Level.INFO, "Se han encontrado anomalías en los datos de la base " + databaseName+ ":\n" + anomaliesText.toString());
					}
				} catch (SQLException e1) {
					anomalyDetailsTextArea.setText("Fracasamos\n" + e1.getMessage());
					logger.log(Level.ERROR, "Ha ocurrido un error al intentar hacer el análisis de anomalías: " + e1.getMessage());
				}
				
			}
		});

        disabledCheckConstraintsButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					List<DisabledCheckConstraintsAnomaly> disabledCheckConstraintsAnomaly = anomalyDao.getAllDisabledCheckConstraintsAnomalies();
					logger.log(Level.INFO, "Se ejecutó un análisis de anomalías de constraints inhabilitados en la base " + databaseName);
					if (disabledCheckConstraintsAnomaly.size() == 0 ) {
						anomalyDetailsTextArea.setText("No se han encontrado constraints inhabilitados");
						logger.log(Level.INFO, "No se han encontrado constraints inhabilitados.");
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
                        logger.log(Level.INFO, "Se han encontrado constraints deshabilitados en la base " + databaseName+ ":\n" + anomaliesText.toString());
					}
					
				} catch (Exception e2) {
					anomalyDetailsTextArea.setText("Fracasamos\n" + e2.getMessage());
					logger.log(Level.ERROR, "Ha ocurrido un error al intentar hacer el análisis de anomalías: " + e2.getMessage());
				}
				
			}
		});
        
        disabledTriggerButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					List<DisabledTriggerAnomaly> disabledTriggerAnomaly = anomalyDao.getAllDisabledTriggerAnomalies();
					logger.log(Level.INFO, "Se ejecutó un análisis de anomalías de triggers inhabilitados en la base " + databaseName);
					if (disabledTriggerAnomaly.size() == 0 ) {
						anomalyDetailsTextArea.setText("No se han encontrado triggers inhabilitados");
						logger.log(Level.INFO, "No se han encontrado triggers inhabilitados.");
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
                        logger.log(Level.INFO, "Se encontraron triggers inhabilitados la base " + databaseName + ":\n" + anomaliesText.toString());
					}
					
				} catch (Exception e2) {
					anomalyDetailsTextArea.setText("Fracasamos\n" + e2.getMessage());
					logger.log(Level.ERROR, "Ha ocurrido un error al intentar hacer el análisis de anomalías: " + e2.getMessage());
				}
				
			}
		});
        isolatedTablesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    List<IsolatedTableAnomaly> isolatedTableAnomalies = anomalyDao.getAllIsolatedTableAnomalies();
                    logger.log(Level.INFO, "Se ejecutó un análisis de anomalías de tablas aisladas en la base " + databaseName);
                    if (isolatedTableAnomalies.size() == 0) {
                        anomalyDetailsTextArea.setText("No se han encontrado errores de tablas aisladas");
                        logger.log(Level.INFO, "No se han encontrado tablas aisladas.");
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
                        logger.log(Level.INFO, "Se encontraron tablas aisladas la base " + databaseName + ":\n" + anomaliesText.toString());
                    }
                } catch (Exception ex) {
                    anomalyDetailsTextArea.setText("Fracasamos\n" + ex.getMessage());
                    logger.log(Level.ERROR, "Ha ocurrido un error al intentar hacer el análisis de anomalías: " + ex.getMessage());
                }
            }
        });

        fakeForeignKeysButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					List<FakeForeignKeyAnomaly> fakeForeignKeyAnomaly = anomalyDao.getAllFakeForeignKeyAnomalies();
					logger.log(Level.INFO, "Se ejecutó un análisis de anomalías de claves foráneas falsas en la base " + databaseName);
					if (fakeForeignKeyAnomaly.size() == 0) {
                        anomalyDetailsTextArea.setText("No se han encontrado claves foráneas falsas");
                        logger.log(Level.INFO, "No se han encontrado claves foráneas falsas.");
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
                        logger.log(Level.INFO, "Se encontraron claves foráneas falsas la base " + databaseName + ":\n" + anomaliesText.toString());
                    }
				} catch (SQLException e1) {
					anomalyDetailsTextArea.setText("Fracasamos\n" + e1.getMessage());
					logger.log(Level.ERROR, "Ha ocurrido un error al intentar hacer el análisis de anomalías: " + e1.getMessage());
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
            JOptionPane.showMessageDialog(this, "Error al cerrar la conexión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.ERROR, "Error al cerrar la conexión: " + ex.getMessage());
        }
    }
}
