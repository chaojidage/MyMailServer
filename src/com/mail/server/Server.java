package com.mail.server;

import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.mail.panel.EmailGroupSendingPanel;
import com.mail.panel.ManageLogPanel;
import com.mail.panel.ManageServerPanel;
import com.mail.panel.ManageUserPanel;
import com.mail.panel.MenuPanel;
import com.mail.service.UserServer;
import com.mail.panel.ManageSystemPanel;


public class Server extends JFrame{
	private static final long serialVersionUID = 1L;
	public JSplitPane jSplitPane = new JSplitPane();
	public JPanel rightPanel; 
	public CardLayout cardLayout = new CardLayout();
	public Server() {				
		MenuPanel menuPanel = new MenuPanel(this);
		rightPanel = new JPanel();
		rightPanel.setLayout(cardLayout);
		rightPanel.add("mServerPanel", new ManageServerPanel());
		rightPanel.add("mUserPanel", new ManageUserPanel());
		rightPanel.add("mLogPanel", new ManageLogPanel());
		rightPanel.add("mSystemPanel", new ManageSystemPanel());
		rightPanel.add("emailGroupSendingPanel", new EmailGroupSendingPanel());
		
		jSplitPane.setEnabled(false);
		jSplitPane.setDividerSize(2);
		jSplitPane.setDividerLocation(200);
		jSplitPane.setLeftComponent(menuPanel);
		jSplitPane.setRightComponent(rightPanel);
		
		this.add(jSplitPane);
		this.setSize(1000, 600);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}	
	
	public static void main(String[] args){
		new Server();
		//new UserServer().start();
	}
}
