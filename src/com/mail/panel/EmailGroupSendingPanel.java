package com.mail.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.mail.dao.UserDao;
import com.mail.daoImpl.UserDaoImpl;
import com.mail.entity.User;
import com.mail.util.SendMail;

public class EmailGroupSendingPanel extends JPanel{
	JLabel subjectLabel,contentLabel,systemAccount,success;
	JTextField subjectText;
	JTextArea contentArea;
	JButton send;
	
	private static final long serialVersionUID = 1L;

	public EmailGroupSendingPanel() {
		setLayout(null);
		initComponent();
		
		send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				groupSending();
			}
		});
	}
	
	public void groupSending(){
		String subject = "系统邮件-"+subjectText.getText();
		String content = contentArea.getText();
		System.out.println(subject +"  "+content);
		try {
			SendMail.myEmailAccount = "000000@gqm.com";
			SendMail.myEmailPassword = "000000";
			
			SendMail.content = content;
			SendMail.subject = subject;
			
			UserDao userDao = UserDaoImpl.getInstanse();
			List<User> list = userDao.getUserList();
			SendMail.receiveMailAccount = list.get(0).getEmail();
			for(int i=1;i<list.size();i++){
				SendMail.receiveMailAccount = SendMail.receiveMailAccount+" "+list.get(i).getEmail();
			}
			SendMail.send();
			success.setText("发送成功");
			
			subjectText.setText("");
			contentArea.setText("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initComponent(){
		success  = new JLabel("");
		subjectLabel = new JLabel("标题 : ");
		contentLabel = new JLabel("内容 : ");
		systemAccount = new JLabel("系统邮箱账号 : 000000@gqm.com");
		
		subjectText = new JTextField();
		contentArea = new JTextArea();
		send = new JButton("发送");
		
		success.setBounds(250, 10, 200, 30);
		this.add(success);
		
		systemAccount.setBounds(80, 50, 200, 30);
		this.add(systemAccount);
		
		subjectLabel.setBounds(80, 100, 100, 30);
		this.add(subjectLabel);
		
		subjectText.setBounds(180, 100, 200, 30);
		this.add(subjectText);
		
		contentLabel.setBounds(80, 150, 100, 30);
		this.add(contentLabel);
		
		JScrollPane scrollPane = new JScrollPane(contentArea);
		scrollPane.setBounds(80, 200, 600, 200);
		this.add(scrollPane);
		
		send.setBounds(330, 450, 80, 30);
		this.add(send);
	}
	public static void main(String[] args) {
		JFrame jFrame = new JFrame("userList");
		jFrame.add(new EmailGroupSendingPanel());

		jFrame.setSize(800, 600);
		jFrame.setLocationRelativeTo(null);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setVisible(true);
		
	}
}
