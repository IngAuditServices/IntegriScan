package com.ingauditservices.integriscan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    public MainFrame() {

        setTitle("IngAudit Services");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 


        JLabel titleLabel = new JLabel("IngAudit Services");
        titleLabel.setHorizontalAlignment(JLabel.CENTER); 
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); 

        JLabel subTitleLabel = new JLabel("Audit for SQL Server");
        subTitleLabel.setHorizontalAlignment(JLabel.CENTER);

        JButton iniciarButton = new JButton("Iniciar");
        
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1)); 
        panel.add(titleLabel);
        panel.add(subTitleLabel);
        panel.add(iniciarButton);


        iniciarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {           
                ConnectionSettingsFrame conectionString = new ConnectionSettingsFrame();
                conectionString.setVisible(true);
                dispose(); 
            }
        });

        add(panel, BorderLayout.CENTER);
    }
}
