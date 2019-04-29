package com.mail.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.mail.service.POPServer;
import com.mail.service.SMTPServer;
import com.mail.util.LogUtil;
import com.mail.util.MyFont;
import com.mail.util.TimeUtil;

public class ManageServerPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	JButton smtpMail, popMail;
	JLabel smtpLabel, popLabel;
	long smtpTime, popTime;
	Timer timer = new Timer();
	Task smtpTask, popTask;
	JLabel label;

	public ManageServerPanel() {
		this.setLayout(null);
		
		label = new JLabel("服务器管理");
		label.setFont(MyFont.biaoti);
		label.setBounds(280, 0, 200, 50);
		this.add(label);

		smtpLabel = new JLabel("SMTP服务器 :");
		smtpLabel.setFont(MyFont.servertext);
		smtpLabel.setBounds(50, 100, 600, 50);
		this.add(smtpLabel);

		smtpMail = new JButton("打开SMTP服务器");
		smtpMail.setFont(MyFont.text);
		smtpMail.setBounds(450, 100, 200, 50);
		smtpMail.addActionListener(this);

		popLabel = new JLabel("POP3服务器 :");
		popLabel.setFont(MyFont.servertext);
		popLabel.setBounds(50, 300, 600, 50);
		this.add(popLabel);

		popMail = new JButton("打开POP3服务器");
		popMail.setFont(MyFont.text);
		popMail.setBounds(450, 300, 200, 50);
		popMail.addActionListener(this);

		this.add(smtpMail);
		this.add(popMail);
		this.setSize(600, 600);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == smtpMail) {
			if (smtpMail.getText().equals("打开SMTP服务器")) {
				recordLog("打开SMTP服务器");
				smtpTask = new Task(smtpLabel, "SMTP服务器 : ");
				timer.schedule(smtpTask, new Date(), 1000);
				smtpMail.setText("关闭SMTP服务器");
				SMTPServer.openServer();
			} else if (smtpMail.getText().equals("关闭SMTP服务器")) {
				recordLog("关闭SMTP服务器");
				smtpMail.setText("打开SMTP服务器");
				smtpTask.cancel();
				smtpLabel.setText("SMTP服务器 : ");
				SMTPServer.closeServer();
			}
		} else if (e.getSource() == popMail) {
			if (popMail.getText().equals("打开POP3服务器")) {
				recordLog("打开POP3服务器");
				popTask = new Task(popLabel, "POP3服务器 : ");
				timer.schedule(popTask, new Date(), 1000);
				popMail.setText("关闭POP3服务器");
				POPServer.openServer();
			} else if (popMail.getText().equals("关闭POP3服务器")) {
				recordLog("关闭POP3服务器");
				popTask.cancel();
				popLabel.setText("POP3服务器 : ");
				popMail.setText("打开POP3服务器");
				POPServer.closeServer();
			}
		}
	}

	public void recordLog(String log) {
		LogUtil.addLog("admin", log);
	}

	private class Task extends TimerTask {
		private JLabel label;
		String serverName;
		int time = 0;

		public Task(JLabel label, String serverName) {
			this.label = label;
			this.serverName = serverName;
		}

		@Override
		public void run() {
			time = time + 1;
			label.setText(serverName + "已工作 " + TimeUtil.formatSeconds(time));
		}
	}
}
