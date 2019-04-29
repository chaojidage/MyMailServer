package com.mail.panel;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.mail.util.MyFont;

//及修改服务器相关参数、查看端口、修改管理员密码
//添加管理员
public class ManageSystemPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	JLabel label;
	JLabel smtpLabel,pop3Label,userLabel;
	public ManageSystemPanel() {
		this.setLayout(null);
		
		label = new JLabel("系统管理");
		label.setFont(MyFont.biaoti);
		label.setBounds(280, 0, 120, 50);
		this.add(label);
		
		smtpLabel = new JLabel("SMTP服务器端口 : 25");
		smtpLabel.setBounds(50, 100, 250, 50);
		smtpLabel.setFont(MyFont.text);
		this.add(smtpLabel);
		
		pop3Label = new JLabel("POP3服务器端口 : 995");
		pop3Label.setFont(MyFont.text);
		pop3Label.setBounds(50, 200, 250, 50);
		this.add(pop3Label);
		
		userLabel = new JLabel("用  户服务器端口 : 8000");
		userLabel.setFont(MyFont.text);
		userLabel.setBounds(50, 300, 250, 50);
		this.add(userLabel);
		
	}
	
	public static void main(String[] args) {
		JFrame jFrame = new JFrame("系统管理");
		jFrame.add(new ManageSystemPanel());

		jFrame.setSize(800, 600);
		jFrame.setLocationRelativeTo(null);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setVisible(true);
	}
}
