package com.mail.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.mail.server.Server;

public class MenuPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	Server server;
	JButton bt1,bt2,bt3,bt4,bt5;
	public MenuPanel(Server server) {
		this.server = server;
		
		this.setLayout(null);
		
		bt1 = new JButton("服务器管理");
		bt1.setBounds(0, 0, 200, 50);
		bt1.addActionListener(this);
		
		bt2 = new JButton("用户管理");		
		bt2.setBounds(0, 50, 200, 50);	
		bt2.addActionListener(this);
		
		bt3 = new JButton("系统管理");
		bt3.setBounds(0, 100, 200, 50);	
		bt3.addActionListener(this);
		
		bt4 = new JButton("日志管理");
		bt4.setBounds(0, 150, 200, 50);
		bt4.addActionListener(this);
		
		bt5 = new JButton("群发邮件");
		bt5.setBounds(0, 200, 200, 50);
		bt5.addActionListener(this);

		this.add(bt1);
		this.add(bt2);
		this.add(bt3);
		this.add(bt4);
		this.add(bt5);

		this.setSize(200, 400);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==bt1){
			server.cardLayout.show(server.rightPanel, "mServerPanel");
		}else if(e.getSource()==bt2){
			server.cardLayout.show(server.rightPanel, "mUserPanel");
		}else if(e.getSource()==bt3){
			server.cardLayout.show(server.rightPanel, "mSystemPanel");
		}else if(e.getSource()==bt4){
			server.cardLayout.show(server.rightPanel, "mLogPanel");
		}else if(e.getSource()==bt5){
			server.cardLayout.show(server.rightPanel, "emailGroupSendingPanel");
		}
	}
}
