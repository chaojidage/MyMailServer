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
		
		label = new JLabel("����������");
		label.setFont(MyFont.biaoti);
		label.setBounds(280, 0, 200, 50);
		this.add(label);

		smtpLabel = new JLabel("SMTP������ :");
		smtpLabel.setFont(MyFont.servertext);
		smtpLabel.setBounds(50, 100, 600, 50);
		this.add(smtpLabel);

		smtpMail = new JButton("��SMTP������");
		smtpMail.setFont(MyFont.text);
		smtpMail.setBounds(450, 100, 200, 50);
		smtpMail.addActionListener(this);

		popLabel = new JLabel("POP3������ :");
		popLabel.setFont(MyFont.servertext);
		popLabel.setBounds(50, 300, 600, 50);
		this.add(popLabel);

		popMail = new JButton("��POP3������");
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
			if (smtpMail.getText().equals("��SMTP������")) {
				recordLog("��SMTP������");
				smtpTask = new Task(smtpLabel, "SMTP������ : ");
				timer.schedule(smtpTask, new Date(), 1000);
				smtpMail.setText("�ر�SMTP������");
				SMTPServer.openServer();
			} else if (smtpMail.getText().equals("�ر�SMTP������")) {
				recordLog("�ر�SMTP������");
				smtpMail.setText("��SMTP������");
				smtpTask.cancel();
				smtpLabel.setText("SMTP������ : ");
				SMTPServer.closeServer();
			}
		} else if (e.getSource() == popMail) {
			if (popMail.getText().equals("��POP3������")) {
				recordLog("��POP3������");
				popTask = new Task(popLabel, "POP3������ : ");
				timer.schedule(popTask, new Date(), 1000);
				popMail.setText("�ر�POP3������");
				POPServer.openServer();
			} else if (popMail.getText().equals("�ر�POP3������")) {
				recordLog("�ر�POP3������");
				popTask.cancel();
				popLabel.setText("POP3������ : ");
				popMail.setText("��POP3������");
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
			label.setText(serverName + "�ѹ��� " + TimeUtil.formatSeconds(time));
		}
	}
}
